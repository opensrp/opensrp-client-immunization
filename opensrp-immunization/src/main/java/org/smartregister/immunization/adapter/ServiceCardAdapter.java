package org.smartregister.immunization.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.view.ServiceCard;
import org.smartregister.immunization.view.ServiceGroup;
import org.smartregister.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.smartregister.immunization.util.VaccinatorUtils.generateScheduleList;
import static org.smartregister.immunization.util.VaccinatorUtils.nextServiceDue;
import static org.smartregister.util.Utils.getName;
import static org.smartregister.util.Utils.getValue;

/**
 * Created by keyman on 15/05/2017.
 */
public class ServiceCardAdapter extends BaseAdapter {
    private static final String TAG = "ServiceCardAdapter";
    private final Context context;
    private final ServiceGroup serviceGroup;
    private HashMap<String, ServiceCard> serviceCards;
    private List<ServiceRecord> serviceRecordList;
    private List<Alert> alertList;
    private Map<String, List<ServiceType>> serviceTypeMap;

    private boolean isChildActive = true;

    public ServiceCardAdapter(Context context, ServiceGroup serviceGroup, List<ServiceRecord> serviceRecordList,
                              List<Alert> alertList, Map<String, List<ServiceType>> serviceTypeMap) {
        this.context = context;
        this.serviceGroup = serviceGroup;
        this.serviceRecordList = serviceRecordList;
        this.alertList = alertList;
        this.serviceTypeMap = serviceTypeMap;
        serviceCards = new HashMap<>();
    }

    public void updateAll() {
        if (serviceCards != null) {
            // Update all vaccines
            for (ServiceCard curCard : serviceCards.values()) {
                if (curCard != null) curCard.updateState();
            }
        }

        visibilityCheck();
    }

    public void visibilityCheck() {
        // if all cards have been updated
        if (getCount() == serviceCards.size()) {
            if (atLeastOneVisibleCard()) {
                serviceGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        serviceGroup.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                serviceGroup.post(new Runnable() {
                    @Override
                    public void run() {
                        serviceGroup.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }

    @Override
    public int getCount() {
        List<String> types = serviceGroup.getServiceTypeKeys();
        if (types == null || types.isEmpty()) {
            return 0;
        }
        return types.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 231231 + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            String type = serviceGroup.getServiceTypeKeys().get(position);
            if (!serviceCards.containsKey(type)) {
                ServiceCard serviceCard = new ServiceCard(context);
                serviceCard.setChildActive(isChildActive);
                serviceCard.setId((int) getItemId(position));
                serviceCards.put(type, serviceCard);

                ServiceCardTask serviceRowTask = new ServiceCardTask(serviceCard, serviceGroup.getChildDetails(), type);
                Utils.startAsyncTask(serviceRowTask, null);
            }

            return serviceCards.get(type);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }

    }

    public boolean atLeastOneVisibleCard() {
        if (serviceCards != null) {
            for (ServiceCard serviceCard : serviceCards.values()) {
                if (serviceCard.getVisibility() == View.VISIBLE) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateChildsActiveStatus() {
        if (serviceCards != null) {
            // Update all vaccines
            for (ServiceCard curCard : serviceCards.values()) {
                if (curCard != null) {
                    curCard.setChildActive(isChildActive);
                    curCard.updateChildsActiveStatus();
                }
            }
        }
    }

    public void setChildActive(boolean isChildActive) {
        this.isChildActive = isChildActive;
    }

    public void updateWrapperStatus(ArrayList<ServiceWrapper> tags, CommonPersonObjectClient childDetails) {
        if (tags == null) {
            return;
        }

        for (ServiceWrapper tag : tags) {
            updateWrapperStatus(tag.getDefaultName(), tag, childDetails);
        }
    }

    public void updateWrapperStatus(String type, ServiceWrapper tag, CommonPersonObjectClient childDetails) {

        List<ServiceType> serviceTypes = getServiceTypeMap().get(type);

        List<ServiceRecord> serviceRecordList = new ArrayList<>();
        for (ServiceRecord serviceRecord : getServiceRecordList()) {
            //if (serviceRecord.getRecurringServiceId().equals(tag.getTypeId())) {
            //if (serviceRecord.getName().equalsIgnoreCase(tag.getDefaultName())) {
                serviceRecordList.add(serviceRecord);
            //}
        }

        List<Alert> alertList = getAlertList();

        Map<String, Date> receivedServices = VaccinatorUtils.receivedServices(serviceRecordList);

        String dobString = getValue(childDetails.getColumnmaps(), "dob", false);
        List<Map<String, Object>> sch = generateScheduleList(serviceTypes, new DateTime(dobString), receivedServices,
                alertList);


        Map<String, Object> nv = null;
        if (serviceRecordList.isEmpty()) {
            nv = nextServiceDue(sch, serviceTypes);
        } else {
            ServiceRecord lastServiceRecord = null;
            for (ServiceRecord serviceRecord : serviceRecordList) {
                if (serviceRecord.getSyncStatus().equalsIgnoreCase(RecurringServiceRecordRepository.TYPE_Unsynced)) {
                    lastServiceRecord = serviceRecord;
                }
            }

            if (lastServiceRecord != null) {
                nv = nextServiceDue(sch, lastServiceRecord);
            }
        }

        if (nv == null) {
            Date lastVaccine = null;
            if (!serviceRecordList.isEmpty()) {
                ServiceRecord serviceRecord = serviceRecordList.get(serviceRecordList.size() - 1);
                lastVaccine = serviceRecord.getDate();
            }

            nv = nextServiceDue(sch, lastVaccine);
        }

        if (nv != null) {
            ServiceType nextServiceType = (ServiceType) nv.get("service");
            tag.setStatus(nv.get("status").toString());
            tag.setAlert((Alert) nv.get("alert"));
            if (nv.get("date") != null && nv.get("date") instanceof DateTime) {
                tag.setVaccineDate((DateTime) nv.get("date"));
            }
            tag.setServiceType(nextServiceType);
        }
    }

    public Map<String, List<ServiceType>> getServiceTypeMap() {
        if (serviceTypeMap == null) {
            serviceTypeMap = new LinkedHashMap<>();
        }
        return serviceTypeMap;
    }

    public List<ServiceRecord> getServiceRecordList() {
        if (serviceRecordList == null) {
            serviceRecordList = new ArrayList<>();
        }
        return serviceRecordList;
    }

    public List<Alert> getAlertList() {
        return alertList;
    }

    public void updateAllWrapperStatus(CommonPersonObjectClient childDetails) {

        List<ServiceWrapper> tags = allWrappers();
        if (tags == null) {
            return;
        }

        for (ServiceWrapper tag : tags) {
            updateWrapperStatus(tag.getDefaultName(), tag, childDetails);
        }
    }

    public List<ServiceWrapper> allWrappers() {
        List<ServiceWrapper> serviceWrappers = new ArrayList<>();
        if (serviceCards != null) {
            for (ServiceCard serviceCard : serviceCards.values()) {
                serviceWrappers.add(serviceCard.getServiceWrapper());
            }
        }
        return serviceWrappers;
    }

    public void updateWrapper(ServiceWrapper tag) {
        List<ServiceRecord> serviceRecordList = getServiceRecordList();

        if (!serviceRecordList.isEmpty()) {
            for (ServiceRecord serviceRecord : serviceRecordList) {
                if (tag.getName().toLowerCase().contains(serviceRecord.getName().toLowerCase()) && serviceRecord
                        .getDate() != null) {
                    long diff = serviceRecord.getUpdatedAt() - serviceRecord.getDate().getTime();
                    if (diff > 0 && TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 1) {
                        tag.setUpdatedVaccineDate(new DateTime(serviceRecord.getDate()), false);
                    } else {
                        tag.setUpdatedVaccineDate(new DateTime(serviceRecord.getDate()), true);
                    }
                    tag.setDbKey(serviceRecord.getId());
                    tag.setSynced(serviceRecord.getSyncStatus() != null && serviceRecord.getSyncStatus()
                            .equals(VaccineRepository.TYPE_Synced));
                }
            }
        }

    }

    class ServiceCardTask extends AsyncTask<Void, Void, ServiceWrapper> {

        private ServiceCard serviceCard;

        private CommonPersonObjectClient childDetails;

        private String type;

        ServiceCardTask(ServiceCard serviceCard, CommonPersonObjectClient childDetails, String type) {
            this.serviceCard = serviceCard;
            this.childDetails = childDetails;
            this.type = type;
        }

        @Override
        protected ServiceWrapper doInBackground(Void... params) {
            ServiceWrapper serviceWrapper = new ServiceWrapper();
            serviceWrapper.setId(childDetails.entityId());
            serviceWrapper.setGender(childDetails.getDetails().get("gender"));
            serviceWrapper.setDefaultName(type);

            String dobString = getValue(childDetails.getColumnmaps(), "dob", false);
            if (StringUtils.isNotBlank(dobString)) {
                Calendar dobCalender = Calendar.getInstance();
                DateTime dateTime = new DateTime(dobString);
                dobCalender.setTime(dateTime.toDate());
                serviceWrapper.setDob(new DateTime(dobCalender.getTime()));
            }

            Photo photo = ImageUtils.profilePhotoByClient(childDetails);
            serviceWrapper.setPhoto(photo);

            String zeirId = getValue(childDetails.getColumnmaps(), "zeir_id", false);
            serviceWrapper.setPatientNumber(zeirId);

            String firstName = getValue(childDetails.getColumnmaps(), "first_name", true);
            String lastName = getValue(childDetails.getColumnmaps(), "last_name", true);
            String childName = getName(firstName, lastName);
            serviceWrapper.setPatientName(childName.trim());

            updateWrapperStatus(type, serviceWrapper, childDetails);
            updateWrapper(serviceWrapper);

            return serviceWrapper;
        }

        @Override
        protected void onPostExecute(ServiceWrapper serviceWrapper) {
            serviceCard.setServiceWrapper(serviceWrapper);
            visibilityCheck();
            notifyDataSetChanged();
        }
    }

    public void updateServiceRecordList(List<ServiceRecord> serviceRecordList) {
        this.serviceRecordList = serviceRecordList;
    }

    public void updateAlertList(List<Alert> alertList) {
        this.alertList = alertList;
    }
}
