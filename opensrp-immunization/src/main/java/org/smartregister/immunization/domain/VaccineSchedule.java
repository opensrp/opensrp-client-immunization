package org.smartregister.immunization.domain;

import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.smartregister.clientandeventmodel.DateUtil;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.jsonmapping.Condition;
import org.smartregister.immunization.domain.jsonmapping.Due;
import org.smartregister.immunization.domain.jsonmapping.Expiry;
import org.smartregister.immunization.domain.jsonmapping.Schedule;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.service.AlertService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Jason Rogena - jrogena@ona.io on 19/05/2017.
 */

public class VaccineSchedule {

    private static HashMap<String, HashMap<String, VaccineSchedule>> vaccineSchedules;
    private final ArrayList<VaccineTrigger> dueTriggers;
    private final ArrayList<VaccineTrigger> expiryTriggers;
    private final VaccineRepo.Vaccine vaccine;
    private final ArrayList<VaccineCondition> conditions;

    public VaccineSchedule(ArrayList<VaccineTrigger> dueTriggers,
                           ArrayList<VaccineTrigger> expiryTriggers,
                           VaccineRepo.Vaccine vaccine,
                           ArrayList<VaccineCondition> conditions) {
        this.dueTriggers = dueTriggers;
        this.expiryTriggers = expiryTriggers;
        this.vaccine = vaccine;
        this.conditions = conditions;
    }

    public static void init(List<VaccineGroup> vaccines,
                            List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines,
                            String vaccineCategory) {
        if (vaccineSchedules == null) {
            vaccineSchedules = new HashMap<>();
        }
        vaccineSchedules.put(vaccineCategory, new HashMap<>());

        for (VaccineGroup vaccineGroup : vaccines) {
            for (org.smartregister.immunization.domain.jsonmapping.Vaccine vaccine : vaccineGroup.vaccines) {
                initVaccine(vaccineCategory, vaccine);
            }
        }

        if (specialVaccines != null) {
            for (org.smartregister.immunization.domain.jsonmapping.Vaccine vaccine : specialVaccines) {
                initVaccine(vaccineCategory, vaccine);
            }
        }
    }

    private static void initVaccine(String vaccineCategory,
                                    org.smartregister.immunization.domain.jsonmapping.Vaccine curVaccine) {
        if (TextUtils.isEmpty(curVaccine.vaccine_separator)) {
            String vaccineName = curVaccine.name;
            VaccineSchedule vaccineSchedule;
            if (curVaccine.schedule != null) {
                vaccineSchedule = getVaccineSchedule(vaccineName, vaccineCategory, curVaccine.schedule);
                if (vaccineSchedule != null) {
                    vaccineSchedules.get(vaccineCategory).put(vaccineName.toUpperCase(), vaccineSchedule);
                }
            }
        } else {
            String[] splitNames = curVaccine.name
                    .split(curVaccine.vaccine_separator);
            for (int nameIndex = 0; nameIndex < splitNames.length; nameIndex++) {
                String vaccineName = splitNames[nameIndex];
                VaccineSchedule vaccineSchedule = getVaccineSchedule(vaccineName, vaccineCategory, curVaccine.schedules.get(vaccineName));
                if (vaccineSchedule != null) {
                    vaccineSchedules.get(vaccineCategory).put(vaccineName.toUpperCase(), vaccineSchedule);
                }
            }
        }
    }

    private static VaccineSchedule getVaccineSchedule(String vaccineName, String vaccineCategory, Schedule schedule) {
        ArrayList<VaccineTrigger> dueTriggers = new ArrayList<>();
        for (Due due : schedule.due) {
            VaccineTrigger curTrigger = VaccineTrigger.init(vaccineCategory, due);
            if (curTrigger != null) {
                dueTriggers.add(curTrigger);
            }
        }

        ArrayList<VaccineTrigger> expiryTriggers = new ArrayList<>();
        if (schedule.expiry != null) {
            for (Expiry expiry : schedule.expiry) {
                VaccineTrigger curTrigger = VaccineTrigger.init(expiry);
                if (curTrigger != null) {
                    expiryTriggers.add(curTrigger);
                }
            }
        }

        VaccineRepo.Vaccine vaccine = VaccineRepo.getVaccine(vaccineName, vaccineCategory);


        if (vaccine != null) {
            ArrayList<VaccineCondition> conditions = new ArrayList<>();
            if (schedule.conditions != null) {
                for (Condition condition : schedule.conditions) {
                    VaccineCondition curCondition = VaccineCondition.init(vaccineCategory,
                            condition);
                    if (curCondition != null) {
                        conditions.add(curCondition);
                    }
                }
            }

            return new VaccineSchedule(dueTriggers, expiryTriggers, vaccine, conditions);
        }

        return null;
    }

    /**
     * Updates offline alerts for the provided person entity id.
     * <p>
     * This method is deprecated, please see the {@link #updateOfflineAlertsOnly(String baseEntityId, DateTime dob, String vaccineCategory)} and {@link #updateOfflineAlertsAndReturnAffectedVaccineNames(String baseEntityId, DateTime dob, String vaccineCategory)}
     *
     * @param baseEntityId
     * @param dob
     * @param vaccineCategory
     * @return The list of updated vaccine names
     */

    @Deprecated
    public static List<String> updateOfflineAlerts(String baseEntityId, DateTime dob, String vaccineCategory) {
        try {
            List<Alert> newAlerts = updateOfflineAlertsAndReturnNewAlertList(baseEntityId, dob, vaccineCategory);
            List<String> notInNew = getAlertsNotInNew(newAlerts);
            return notInNew;

        } catch (Exception e) {
            Timber.e(VaccineSchedule.class.getName(), e.toString(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Updates offline alerts for the provided person entity id
     * <p>
     * This method is recommended for performance reasons if you don't need the list of affected vaccine names returned.
     * If you need the list of affected vaccine names use {@link #updateOfflineAlertsAndReturnAffectedVaccineNames(String baseEntityId, DateTime dob, String vaccineCategory)}
     *
     * @param baseEntityId
     * @param dob
     * @param vaccineCategory
     */
    public static void updateOfflineAlertsOnly(String baseEntityId, DateTime dob, String vaccineCategory) {
        updateOfflineAlertsAndReturnNewAlertList(baseEntityId, dob, vaccineCategory);
    }

    /**
     * Updates offline alerts for the provided person entity id
     * <p>
     * Use this method if you need the list of affected vaccine names returned. If not use {@link #updateOfflineAlertsOnly(String baseEntityId, DateTime dob, String vaccineCategory)}
     *
     * @param baseEntityId
     * @param dob
     * @param vaccineCategory
     * @return The list of updated vaccines
     */

    public static List<String> updateOfflineAlertsAndReturnAffectedVaccineNames(String baseEntityId, DateTime dob, String vaccineCategory) {
        return updateOfflineAlerts(baseEntityId, dob, vaccineCategory);
    }

    /**
     * Updates offline alerts for the provided person entity id.
     * <p>
     * This is a private helper method invoked by {@link #updateOfflineAlertsOnly(String baseEntityId, DateTime dob, String vaccineCategory)} and {@link #updateOfflineAlertsAndReturnAffectedVaccineNames(String baseEntityId, DateTime dob, String vaccineCategory)}
     * <p>
     * The {@link #updateOfflineAlertsAndReturnAffectedVaccineNames(String baseEntityId, DateTime dob, String vaccineCategory) public method expects a list of Alerts to complete processing
     *
     * @param baseEntityId
     * @param dob
     * @param vaccineCategory
     */
    private static List<Alert> updateOfflineAlertsAndReturnNewAlertList(String baseEntityId, DateTime dob, String vaccineCategory) {

        List<Alert> newAlerts = new ArrayList<>();
        try {
            VaccineRepository vaccineRepository = ImmunizationLibrary.getInstance().vaccineRepository();
            AlertService alertService = ImmunizationLibrary.getInstance().context().alertService();
            if (vaccineSchedules != null && vaccineSchedules.containsKey(vaccineCategory)) {
                List<String> alertNames = new ArrayList<>();
                for (String curVaccineName : vaccineSchedules.get(vaccineCategory).keySet()) {
                    alertNames.add(curVaccineName.toLowerCase().replace(" ", ""));
                }

                // Get all the administered vaccines for the child
                List<Vaccine> issuedVaccines = vaccineRepository.findByEntityId(baseEntityId);
                if (issuedVaccines == null) {
                    issuedVaccines = new ArrayList<>();
                }

                alertService.deleteOfflineAlerts(baseEntityId, alertNames.toArray(new String[0]));

                boolean isAnyVaccineUrgent = false;

                for (VaccineSchedule curSchedule : vaccineSchedules.get(vaccineCategory).values()) {
                    Alert curAlert = curSchedule.getOfflineAlert(baseEntityId, dob.toDate(), issuedVaccines);

                    if (curAlert != null && !AlertStatus.complete.equals(curAlert.status())) {
                        // Insert alert into table
                        newAlerts.add(curAlert);
                        alertService.updateFtsSearch(curAlert, true);

                        if (!isAnyVaccineUrgent && (AlertStatus.urgent.equals(curAlert.status())
                                || AlertStatus.normal.equals(curAlert.status()))) {
                            try {
                                isAnyVaccineUrgent = true;
                                ImmunizationLibrary.getInstance().getVaccineOverdueCountRepository()
                                        .upsert(baseEntityId, curAlert.status().value());
                            } catch (Exception e) {
                                Timber.e(e);
                            }
                        }
                    }
                }

                if (!isAnyVaccineUrgent) {
                    try {
                        ImmunizationLibrary.getInstance().getVaccineOverdueCountRepository().delete(baseEntityId);
                    } catch (Exception e) {
                        Timber.e(e);
                    }
                }

                alertService.create(newAlerts);
            }

        } catch (
                Exception e) {
            Timber.e(VaccineSchedule.class.getName(), e.toString(), e);
        }

        return newAlerts;
    }

    @NotNull
    private static List<String> getAlertsNotInNew(List<Alert> newAlerts) {
        List<Alert> oldAlerts = new ArrayList<>();
        List<String> allVaccineNames = new ArrayList<>();
        List<String> oldVaccineNames = new ArrayList<>();
        HashMap<String, Alert> oldAlertsMap = new HashMap<>();
        for (Alert curAlert : oldAlerts) {
            if (!oldVaccineNames.contains(curAlert.scheduleName())) {
                oldVaccineNames.add(curAlert.scheduleName());
                oldAlertsMap.put(curAlert.scheduleName(), curAlert);
            }

            if (!allVaccineNames.contains(curAlert.scheduleName()))
                allVaccineNames.add(curAlert.scheduleName());
        }

        List<String> newVaccineNames = new ArrayList<>();
        HashMap<String, Alert> newAlertsMap = new HashMap<>();
        for (Alert curAlert : newAlerts) {
            if (!newVaccineNames.contains(curAlert.scheduleName())) {
                newVaccineNames.add(curAlert.scheduleName());
                newAlertsMap.put(curAlert.scheduleName(), curAlert);
            }

            if (!allVaccineNames.contains(curAlert.scheduleName()))
                allVaccineNames.add(curAlert.scheduleName());
        }

        // Get list of vaccines that are not in both
        List<String> notInOld = new ArrayList<>(newVaccineNames);
        notInOld.removeAll(oldVaccineNames);
        List<String> notInNew = new ArrayList<>(oldVaccineNames);
        notInNew.removeAll(newVaccineNames);
        notInNew.addAll(notInOld);

        allVaccineNames.removeAll(notInNew);

        // For the alerts in both, check if similar
        for (String curVaccineName : allVaccineNames) {
            Alert oldAlert = oldAlertsMap.get(curVaccineName);
            Alert newAlert = newAlertsMap.get(curVaccineName);

            if (!oldAlert.equals(newAlert)) {
                notInNew.add(oldAlert.scheduleName());
            }
        }

        return notInNew;
    }

    public static void standardiseCalendarDate(Calendar calendarDate) {
        calendarDate.set(Calendar.HOUR_OF_DAY, 0);
        calendarDate.set(Calendar.MINUTE, 0);
        calendarDate.set(Calendar.SECOND, 0);
        calendarDate.set(Calendar.MILLISECOND, 0);
    }

    /**
     * This method adds an offset to the provided calendar. Offsets can look like: "+5y,3m,2d" : Plus 5 years, 3 months, and
     * 2 days "-2d" : Minus 2 days
     * <p>
     * Accepted time units for the offset are: d : Days m : Months y : Years
     * <p>
     * Accepted operators for the offset are: - : Minus + : Plus
     *
     * @param calendar The calendar to add the offset
     * @param offset   The offset in the format above to add to the calendar
     * @return
     */
    public static Calendar addOffsetToCalendar(Calendar calendar, String offset) {
        if (calendar != null && offset != null) {
            VaccinatorUtils.processConfigCalendarOffset(calendar, offset);
        }

        return calendar;
    }

    public static VaccineSchedule getVaccineSchedule(String vaccineCategory, String vaccineName) {
        if (vaccineSchedules != null && vaccineName != null && vaccineSchedules.containsKey(vaccineCategory) && vaccineSchedules.get(vaccineCategory)
                .containsKey(vaccineName.toUpperCase())) {
            return vaccineSchedules.get(vaccineCategory).get(vaccineName.toUpperCase());
        }

        return null;
    }

    public static HashMap<String, HashMap<String, VaccineSchedule>> getVaccineSchedules() {
        return vaccineSchedules;
    }

    public static void setVaccineSchedules(HashMap<String, HashMap<String, VaccineSchedule>> vaccineSchedules) {
        VaccineSchedule.vaccineSchedules = vaccineSchedules;
    }

    /**
     * Returns the offline alert for a vaccine, if one exists. Currently, the only alert status returned is {@code
     * AlertStatus.normal}
     *
     * @return An {@link Alert} object if one exists, or {@code NULL} if non exists
     */
    public Alert getOfflineAlert(String baseEntityId, Date dateOfBirth,
                                 List<Vaccine> issuedVaccines) {
        Alert defaultAlert = null;

        // Check if all conditions pass
        for (VaccineCondition curCondition : conditions) {
            if (!curCondition.passes(dateOfBirth, issuedVaccines)) {
                return defaultAlert;
            }
        }

        Date dueDate = getDueDate(issuedVaccines, dateOfBirth);
        Date expiryDate = getExpiryDate(issuedVaccines, dateOfBirth);
        Date overDueDate = getOverDueDate(dueDate);

        // Use the trigger date as a reference, since that is what is mostly used
        // Generate only if its not in issued vaccines

        AlertStatus alertStatus = null;
        alertStatus = expiryDate != null && expiryDate.before(Calendar.getInstance().getTime()) ? AlertStatus.expired : null; //Check if expired first

        if (alertStatus == null) {
            alertStatus = isVaccineIssued(vaccine.name(), issuedVaccines) ? AlertStatus.complete : calculateAlertStatus(dueDate, overDueDate);
        }

        if (alertStatus != null) {

            Alert offlineAlert = new Alert(baseEntityId,
                    vaccine.display(),
                    vaccine.name().toLowerCase(Locale.ENGLISH).replace(" ", ""),
                    alertStatus,
                    dueDate == null ? null : DateUtil.yyyyMMdd.format(dueDate),
                    expiryDate == null ? null : DateUtil.yyyyMMdd.format(expiryDate),
                    true);

            return offlineAlert;
        }

        return defaultAlert;
    }

    protected boolean isVaccineIssued(String currentVaccine, List<Vaccine> issuedVaccines) {

        for (Vaccine vaccine : issuedVaccines) {
            if (currentVaccine.equalsIgnoreCase(vaccine.getName().replaceAll(" ", ""))) {
                return true;
            }
        }

        return false;
    }

    public Date getDueDate(List<Vaccine> issuedVaccines, Date dob) {
        Date dueDate = null;
        for (VaccineTrigger curTrigger : dueTriggers) {
            if (dueDate == null) {
                dueDate = curTrigger.getFireDate(issuedVaccines, dob);
            } else {
                Date curDate = curTrigger.getFireDate(issuedVaccines, dob);
                if (curDate != null && curDate.getTime() < dueDate.getTime()) {
                    dueDate = curDate;
                }
            }
        }

        return dueDate;
    }

    public Date getExpiryDate(List<Vaccine> issuedVaccines, Date dob) {
        Date expiryDate = null;
        for (VaccineTrigger curTrigger : expiryTriggers) {
            if (expiryDate == null) {
                expiryDate = curTrigger.getFireDate(issuedVaccines, dob);
            } else {
                Date curDate = curTrigger.getFireDate(issuedVaccines, dob);
                if (curDate != null && curDate.getTime() < expiryDate.getTime()) {
                    expiryDate = curDate;
                }
            }
        }

        return expiryDate;
    }

    public Date getOverDueDate(Date dueDate) {
        if (dueDate == null) {
            return null;
        }

        String window = null;
        for (VaccineTrigger curTrigger : dueTriggers) {
            if (curTrigger.getWindow() != null) {
                window = curTrigger.getWindow();
                break;
            }
        }

        if (window != null) {
            Calendar dueDateCalendar = Calendar.getInstance();
            dueDateCalendar.setTime(dueDate);
            standardiseCalendarDate(dueDateCalendar);

            Calendar overDueOffsetCalendar = addOffsetToCalendar(dueDateCalendar, window);
            overDueOffsetCalendar.add(Calendar.DATE, 1); //Add date it should actually be expired

            return overDueOffsetCalendar.getTime();
        }

        return null;
    }

    public VaccineRepo.Vaccine getVaccine() {
        return vaccine;
    }

    /**
     * Calculates the alert status based on the reference date provided. Currently, the only alert status returned is {@code
     * AlertStatus.normal}
     *
     * @param referenceDate The reference date to use to
     * @param overDueDate   The overdue date to use
     * @return {@link AlertStatus} if able to calculate or {@code NULL} if unable
     */
    private AlertStatus calculateAlertStatus(Date referenceDate, Date overDueDate) {
        if (referenceDate != null) {
            Calendar refCalendarDate = Calendar.getInstance();
            refCalendarDate.setTime(referenceDate);
            standardiseCalendarDate(refCalendarDate);

            Calendar overDueCalendarDate = Calendar.getInstance();
            if (overDueDate != null) {
                overDueCalendarDate.setTime(overDueDate);
                standardiseCalendarDate(overDueCalendarDate);
            }

            Calendar today = Calendar.getInstance();
            standardiseCalendarDate(today);


            if (overDueDate != null && overDueCalendarDate.getTimeInMillis() <= today.getTimeInMillis()) { //OverDue
                return AlertStatus.urgent;
            } else if (refCalendarDate.getTimeInMillis() <= today.getTimeInMillis()) { // Due
                return AlertStatus.normal;
            }
        }

        return null;
    }
}
