package org.smartregister.immunization.domain;

import android.util.Log;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.DateUtil;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.service.AlertService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Keyman on 26/05/2017.
 */

public class ServiceSchedule {

    private final ServiceTrigger dueTrigger;
    private final ServiceTrigger expiryTrigger;


    public ServiceSchedule(ServiceTrigger dueTrigger, ServiceTrigger expiryTrigger) {
        this.dueTrigger = dueTrigger;
        this.expiryTrigger = expiryTrigger;
    }

    public static ServiceSchedule getServiceSchedule(JSONObject schedule)
            throws JSONException {
        ServiceTrigger dueTrigger = ServiceTrigger.init(schedule.getJSONObject("due"));
        ServiceTrigger expiryTrigger = ServiceTrigger.init(schedule.optJSONObject("expiry"));
        return new ServiceSchedule(dueTrigger, expiryTrigger);
    }

    public static void updateOfflineAlerts(String baseEntityId, DateTime dob) {
        RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance()
                .recurringServiceTypeRepository();
        List<String> types = recurringServiceTypeRepository.fetchTypes();
        for (String type : types) {
            updateOfflineAlerts(type, baseEntityId, dob);
        }
    }

    public static void updateOfflineAlerts(String type, String baseEntityId, DateTime dob) {
        try {
            if (baseEntityId == null || dob == null) {
                return;
            }

            RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance()
                    .recurringServiceTypeRepository();
            RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance()
                    .recurringServiceRecordRepository();
            AlertService alertService = ImmunizationLibrary.getInstance().context().alertService();

            List<ServiceType> serviceTypes = recurringServiceTypeRepository.findByType(type);

            String[] alertArray = VaccinateActionUtils.allAlertNames(serviceTypes);

            List<Alert> newAlerts = new ArrayList<>();

            // Get all the administered services
            List<ServiceRecord> issuedServices = recurringServiceRecordRepository.findByEntityId(baseEntityId);
            alertService.deleteOfflineAlerts(baseEntityId, alertArray);

            List<Alert> existingAlerts = alertService.findByEntityIdAndAlertNames(baseEntityId, alertArray);

            for (ServiceType serviceType : serviceTypes) {
                Alert curAlert = getOfflineAlert(serviceType, issuedServices, baseEntityId, dob);

                if (curAlert == null) {
                    break;
                } else {
                    // Check if the current alert already exists for the entityId
                    boolean exists = false;
                    for (Alert curExistingAlert : existingAlerts) {
                        if (curExistingAlert.scheduleName().equalsIgnoreCase(curAlert.scheduleName())
                                && curExistingAlert.caseId().equalsIgnoreCase(curAlert.caseId())) {
                            exists = true;
                            break;
                        }
                    }

                    // Check if service is already given
                    if (!exists) {
                        for (ServiceRecord serviceRecord : issuedServices) {
                            if (curAlert.scheduleName().equalsIgnoreCase(serviceRecord.getName()) || curAlert.visitCode()
                                    .equalsIgnoreCase(serviceRecord.getName())) {
                                exists = true;
                                break;
                            }
                        }
                    }

                    if (!exists && !AlertStatus.complete.equals(curAlert.status())) {
                        // Insert alert into table
                        newAlerts.add(curAlert);
                        alertService.create(curAlert);
                    }
                }
            }

        } catch (Exception e) {
            Log.e(ServiceSchedule.class.getName(), e.toString(), e);
        }

    }


    public static Alert getOfflineAlert(ServiceType serviceType, List<ServiceRecord> issuedServices,
                                        String baseEntityId, DateTime dateOfBirth) {

        try {
            DateTime dueDateTime = VaccinatorUtils.getServiceDueDate(serviceType, dateOfBirth, issuedServices);
            DateTime expiryDateTime = VaccinatorUtils.getServiceExpiryDate(serviceType, dateOfBirth);

            AlertStatus alertStatus = null;
            alertStatus = expiryDateTime != null && expiryDateTime.isBeforeNow() ? AlertStatus.expired : null; //Check if expired first

            if (alertStatus == null) {
                alertStatus = isServiceIssued(serviceType.getName(), issuedServices) ? AlertStatus.complete : calculateAlertStatus(dueDateTime);
            }

            if (alertStatus != null) {
                Date startDate = dueDateTime == null ? dateOfBirth.toDate() : dueDateTime.toDate();
                Date expiryDate = expiryDateTime == null ? null : expiryDateTime.toDate();
                return new Alert(baseEntityId, serviceType.getName(), serviceType.getName().toLowerCase(Locale.ENGLISH).replace(" ", ""),
                        alertStatus, startDate == null ? null : DateUtil.yyyyMMdd.format(startDate),
                        expiryDate == null ? null : DateUtil.yyyyMMdd.format(expiryDate), true);
            }
            return null;
        } catch (Exception e) {
            Log.e(ServiceSchedule.class.getName(), e.toString(), e);
            return null;
        }
    }

    protected static boolean isServiceIssued(String currentVaccine, List<ServiceRecord> serviceRecords) {

        for (ServiceRecord serviceRecord : serviceRecords) {
            if (currentVaccine.equalsIgnoreCase(serviceRecord.getName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Use the trigger date as a reference, since that is what is mostly used
     *
     * @param referenceDate trigger date
     * @return evaluated alert status
     */
    private static AlertStatus calculateAlertStatus(DateTime referenceDate) {
        if (referenceDate != null) {
            Calendar refCalendarDate = Calendar.getInstance();
            refCalendarDate.setTime(referenceDate.toDate());
            standardiseCalendarDate(refCalendarDate);

            Calendar today = Calendar.getInstance();
            standardiseCalendarDate(today);

            if (refCalendarDate.getTimeInMillis() <= today.getTimeInMillis()) {// Due
                return AlertStatus.normal;
            }
        }

        return null;
    }

    public static void standardiseCalendarDate(Calendar calendarDate) {
        calendarDate.set(Calendar.HOUR_OF_DAY, 0);
        calendarDate.set(Calendar.MINUTE, 0);
        calendarDate.set(Calendar.SECOND, 0);
        calendarDate.set(Calendar.MILLISECOND, 0);
    }

    public static DateTime standardiseDateTime(DateTime dateTime) {
        if (dateTime != null) {
            return dateTime.withTime(0, 0, 0, 0);
        }
        return null;
    }

    public static DateTime addOffsetToDateTime(DateTime dateTime, List<String> offsets) {
        DateTime afterOffset = dateTime;
        if (dateTime != null && offsets != null && !offsets.isEmpty()) {
            for (String offset : offsets) {
                afterOffset = addOffsetToDateTime(afterOffset, offset);
            }
        }
        return afterOffset;
    }

    public static DateTime addOffsetToDateTime(DateTime dateTime, String offset) {
        try {
            DateTime afterOffset = dateTime;
            if (dateTime != null && offset != null) {
                afterOffset = VaccinatorUtils.processConfigDateTimeOffset(afterOffset, offset);
            }

            return afterOffset;
        } catch (Exception e) {
            Log.e(ServiceSchedule.class.getName(), e.toString(), e);
            return dateTime;
        }
    }



    public ServiceTrigger getDueTrigger() {
        return dueTrigger;
    }

    public ServiceTrigger getExpiryTrigger() {
        return expiryTrigger;
    }
}

