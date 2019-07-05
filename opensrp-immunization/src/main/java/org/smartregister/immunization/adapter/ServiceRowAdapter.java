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
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.view.ServiceRowCard;
import org.smartregister.immunization.view.ServiceRowGroup;
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
import static org.smartregister.util.Utils.getName;
import static org.smartregister.util.Utils.getValue;

/**
 * Created by keyman on 15/05/2017.
 */
public class ServiceRowAdapter extends BaseAdapter {
    private static final String TAG = "ServiceRowAdapter";
    private final Context context;
    private final ServiceRowGroup serviceRowGroup;
    public boolean editmode;
    private HashMap<String, ServiceRowCard> serviceRowCards;
    private List<ServiceType> serviceTypeList;
    private List<ServiceRecord> serviceRecordList;
    private List<Alert> alertList;

    public ServiceRowAdapter(Context context, ServiceRowGroup serviceRowGroup, boolean editmode,
                             List<ServiceType> serviceTypeList, List<ServiceRecord> serviceRecordList,
                             List<Alert> alertList) {
        this.context = context;
        this.editmode = editmode;
        this.serviceRowGroup = serviceRowGroup;
        this.serviceTypeList = serviceTypeList;
        this.serviceRecordList = serviceRecordList;
        this.alertList = alertList;
        serviceRowCards = new LinkedHashMap<>();
    }

    @Override
    public int getCount() {
        List<ServiceType> types = serviceRowGroup.getServiceTypes();
        if (types == null || types.isEmpty()) {
            return 0;
        }
        return types.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceRowCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 231231 + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ServiceType serviceType = serviceRowGroup.getServiceTypes().get(position);
            if (!serviceRowCards.containsKey(serviceType.getName())) {
                ServiceRowCard serviceRowCard = new ServiceRowCard(context, editmode);
                serviceRowCard.setId((int) getItemId(position));
                serviceRowCards.put(serviceType.getName(), serviceRowCard);

                ServiceRowTask serviceRowTask = new ServiceRowTask(serviceRowGroup.getChildDetails()
                        , serviceType, serviceRowCard);
                Utils.startAsyncTask(serviceRowTask, null);
            }

            return serviceRowCards.get(serviceType.getName());
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return null;
        }
    }

    public void update(ArrayList<ServiceWrapper> servicesToUpdate) {
        if (serviceRowCards != null) {
            if (servicesToUpdate == null) {// Update all vaccines
                for (ServiceRowCard curCard : serviceRowCards.values()) {
                    if (curCard != null) curCard.updateState();
                }
            } else {// Update just the vaccines specified
                for (ServiceWrapper currWrapper : servicesToUpdate) {
                    if (serviceRowCards.containsKey(currWrapper.getName())) {
                        serviceRowCards.get(currWrapper.getName()).updateState();
                    }
                }
            }
        }
    }

    public void updateWrapperStatus(ServiceWrapper tag, CommonPersonObjectClient childDetails) {

        List<ServiceType> serviceTypes = getServiceTypes();

        List<ServiceRecord> serviceRecordList = getServiceRecordList();

        List<Alert> alertList = getAlertList();

        Map<String, Date> receivedServices = VaccinatorUtils.receivedServices(serviceRecordList);

        String dobString = getValue(childDetails.getColumnmaps(), "dob", false);
        List<Map<String, Object>> sch = generateScheduleList(serviceTypes, new DateTime(dobString), receivedServices,
                alertList);


        for (Map<String, Object> m : sch) {
            ServiceType serviceType = (ServiceType) m.get("service");
            if (tag.getName().equalsIgnoreCase(serviceType.getName())) {
                tag.setStatus(m.get("status").toString());
                tag.setAlert((Alert) m.get("alert"));
                tag.setServiceType(serviceType);
                tag.setVaccineDate((DateTime) m.get("date"));
            }
        }
    }

    public List<ServiceType> getServiceTypes() {
        return serviceTypeList;
    }

    public List<ServiceRecord> getServiceRecordList() {
        return serviceRecordList;
    }

    public List<Alert> getAlertList() {
        return alertList;
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
                    tag.setCreatedAt(serviceRecord.getCreatedAt());
                }
            }
        }

    }

    class ServiceRowTask extends AsyncTask<Void, Void, ServiceWrapper> {

        private CommonPersonObjectClient childDetails;

        private ServiceType serviceType;

        private ServiceRowCard serviceRowCard;

        ServiceRowTask(CommonPersonObjectClient childDetails, ServiceType serviceType, ServiceRowCard serviceRowCard) {
            this.childDetails = childDetails;
            this.serviceType = serviceType;
            this.serviceRowCard = serviceRowCard;
        }

        @Override
        protected ServiceWrapper doInBackground(Void... params) {
            ServiceWrapper serviceWrapper = new ServiceWrapper();
            serviceWrapper.setId(childDetails.entityId());
            serviceWrapper.setGender(childDetails.getDetails().get("gender"));
            serviceWrapper.setDefaultName(serviceType.getName());

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

            updateWrapperStatus(serviceWrapper, childDetails);
            updateWrapper(serviceWrapper);

            return serviceWrapper;
        }

        @Override
        protected void onPostExecute(ServiceWrapper serviceWrapper) {
            serviceRowCard.setServiceWrapper(serviceWrapper);
            notifyDataSetChanged();
        }
    }

}
