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
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.listener.VaccineCardAdapterLoadingListener;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.view.VaccineCard;
import org.smartregister.immunization.view.VaccineGroup;
import org.smartregister.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.smartregister.immunization.util.VaccinatorUtils.generateScheduleList;
import static org.smartregister.util.Utils.getName;
import static org.smartregister.util.Utils.getValue;

/**
 * Created by Jason Rogena - jrogena@ona.io on 22/02/2017.
 */
public class VaccineCardAdapter extends BaseAdapter {
    private static final String TAG = "VaccineCardAdapter";
    private final Context context;
    private final VaccineGroup vaccineGroup;
    private final String type;
    private HashMap<String, VaccineCard> vaccineCards;
    private List<Vaccine> vaccineList;
    private List<Alert> alertList;

    private boolean isChildActive = true;
    private int remainingAsyncTasks;
    private VaccineCardAdapterLoadingListener vaccineCardAdapterLoadingListener;

    public VaccineCardAdapter(Context context, VaccineGroup vaccineGroup, String type,
                              List<Vaccine> vaccineList, List<Alert> alertList) {
        this.context = context;
        this.vaccineGroup = vaccineGroup;
        this.vaccineList = vaccineList;
        this.alertList = alertList;
        vaccineCards = new HashMap<>();
        this.type = type;
        remainingAsyncTasks = vaccineGroup.getVaccineData().vaccines.size();
    }

    @Override
    public int getCount() {
        return vaccineGroup.getVaccineData().vaccines.size();
    }

    @Override
    public Object getItem(int position) {
        return vaccineCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 231231 + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            org.smartregister.immunization.domain.jsonmapping.Vaccine vaccineData
                    = vaccineGroup.getVaccineData().vaccines.get(position);
            String vaccineName = vaccineData.name;
            if (!vaccineCards.containsKey(vaccineName)) {
                VaccineCard vaccineCard = new VaccineCard(context);
                vaccineCard.setChildActive(isChildActive);
                vaccineCard.setId((int) getItemId(position));
                vaccineCards.put(vaccineName, vaccineCard);

                VaccineRowTask vaccineRowTask = new VaccineRowTask(vaccineCard, vaccineData,
                        vaccineGroup.getChildDetails(),
                        vaccineGroup.getVaccineData().days_after_birth_due, position);
                Utils.startAsyncTask(vaccineRowTask, null);
            }

            return vaccineCards.get(vaccineName);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public void update(ArrayList<VaccineWrapper> vaccinesToUpdate) {
        if (vaccineCards != null) {
            if (vaccinesToUpdate == null) {// Update all vaccines
                for (VaccineCard curCard : vaccineCards.values()) {
                    if (curCard != null) curCard.updateState();
                }
            } else {// Update just the vaccines specified
                for (VaccineWrapper currWrapper : vaccinesToUpdate) {
                    if (vaccineCards.containsKey(currWrapper.getName())) {
                        vaccineCards.get(currWrapper.getName()).setVaccineWrapper(currWrapper);
                    }
                }
            }
        }
    }

    public void setChildActive(boolean childActive) {
        isChildActive = childActive;
    }

    public void updateChildsActiveStatus() {
        if (vaccineCards != null) {
            for (VaccineCard curCard : vaccineCards.values()) {
                curCard.setChildActive(isChildActive);
                curCard.updateChildsActiveStatus();
            }
        }
    }

    public ArrayList<VaccineWrapper> getDueVaccines() {
        ArrayList<VaccineWrapper> dueVaccines = new ArrayList<>();
        if (vaccineCards != null) {
            for (VaccineCard curCard : vaccineCards.values()) {
                if (curCard != null && (curCard.getState().equals(State.DUE)
                        || curCard.getState().equals(State.OVERDUE))) {
                    dueVaccines.add(curCard.getVaccineWrapper());
                }
            }
        }

        return dueVaccines;
    }

    public ArrayList<VaccineWrapper> getAllVaccineWrappers() {
        ArrayList<VaccineWrapper> allWrappers = new ArrayList<>();
        for (VaccineCard curCard : vaccineCards.values()) {
            allWrappers.add(curCard.getVaccineWrapper());
        }

        return allWrappers;
    }

    public void updateWrapperStatus(ArrayList<VaccineWrapper> tags, String type, CommonPersonObjectClient childDetails) {
        if (tags == null) {
            return;
        }

        for (VaccineWrapper tag : tags) {
            updateWrapperStatus(tag, type, childDetails);
        }
    }

    public void updateWrapperStatus(VaccineWrapper tag, String type, CommonPersonObjectClient childDetails) {
        List<Vaccine> vaccineList = getVaccineList();

        List<Alert> alertList = getAlertList();

        Map<String, Date> receivedVaccines = VaccinatorUtils.receivedVaccines(vaccineList);

        String dobString = getValue(childDetails.getColumnmaps(), "dob", false);
        List<Map<String, Object>> sch = generateScheduleList(type, new DateTime(dobString), receivedVaccines, alertList);


        for (Map<String, Object> m : sch) {
            VaccineRepo.Vaccine vaccine = (VaccineRepo.Vaccine) m.get("vaccine");
            if (tag.getName().toLowerCase().contains(vaccine.display().toLowerCase())) {

                //Add exception for bcg 2
                if (tag.getName().equalsIgnoreCase(VaccineRepo.Vaccine.bcg2.display()) && !tag.getName()
                        .equalsIgnoreCase(vaccine.display())) {
                    continue;
                }

                if ((vaccine.equals(VaccineRepo.Vaccine.measles2)
                        || vaccine.equals(VaccineRepo.Vaccine.mr2)
                        || vaccine.equals(VaccineRepo.Vaccine.measles1)
                        || vaccine.equals(VaccineRepo.Vaccine.mr1) && tag.getAlert() != null) && tag.getStatus() != null) {
                    break;
                }
                tag.setStatus(m.get("status").toString());
                tag.setAlert((Alert) m.get("alert"));
            }
        }
    }

    public List<Vaccine> getVaccineList() {
        return vaccineList;
    }

    public List<Alert> getAlertList() {
        return alertList;
    }

    public void updateWrapper(VaccineWrapper tag) {
        List<Vaccine> vaccineList = getVaccineList();

        if (!vaccineList.isEmpty()) {
            for (Vaccine vaccine : vaccineList) {
                if (tag.getName().toLowerCase().contains(vaccine.getName().toLowerCase()) && vaccine.getDate() != null) {

                    //Add exception for bcg 2
                    if (tag.getName().equalsIgnoreCase(VaccineRepo.Vaccine.bcg2.display()) && !tag.getName()
                            .equalsIgnoreCase(vaccine.getName())) {
                        continue;
                    }

                    long diff = vaccine.getUpdatedAt() - vaccine.getDate().getTime();
                    if (diff > 0 && TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) > 1) {
                        tag.setUpdatedVaccineDate(new DateTime(vaccine.getDate()), false);
                    } else {
                        tag.setUpdatedVaccineDate(new DateTime(vaccine.getDate()), true);
                    }
                    tag.setDbKey(vaccine.getId());
                    tag.setSynced(vaccine.getSyncStatus() != null && vaccine.getSyncStatus()
                            .equals(VaccineRepository.TYPE_Synced));
                    if (tag.getName().contains("/")) {
                        String[] array = tag.getName().split("/");

                        if ((array[0]).toLowerCase().contains(vaccine.getName().toLowerCase())) {
                            tag.setName(array[0]);
                        } else if ((array[1]).toLowerCase().contains(vaccine.getName().toLowerCase())) {

                            tag.setName(array[1]);
                        }
                    }
                }
            }
        }

    }

    private void notifyAsyncTaskCompleted() {
        remainingAsyncTasks--;
        checkRemainingAsyncTasksStatus();
    }

    private void checkRemainingAsyncTasksStatus() {
        if (remainingAsyncTasks == 0 && vaccineCardAdapterLoadingListener != null) {
            vaccineCardAdapterLoadingListener.onFinishedLoadingVaccineWrappers();
        }
    }

    public VaccineCardAdapterLoadingListener getVaccineCardAdapterLoadingListener() {
        return vaccineCardAdapterLoadingListener;
    }

    public void setVaccineCardAdapterLoadingListener(VaccineCardAdapterLoadingListener vaccineCardAdapterLoadingListener) {
        this.vaccineCardAdapterLoadingListener = vaccineCardAdapterLoadingListener;

        checkRemainingAsyncTasksStatus();
    }

    class VaccineRowTask extends AsyncTask<Void, Void, VaccineWrapper> {

        private VaccineCard vaccineCard;

        private String vaccineName;

        private CommonPersonObjectClient childDetails;

        private int days_after_birth_due;
        private int position;

        private org.smartregister.immunization.domain.jsonmapping.Vaccine vaccineData;

        VaccineRowTask(VaccineCard vaccineCard, org.smartregister.immunization.domain.jsonmapping.Vaccine vaccineData,
                       CommonPersonObjectClient childDetails, Integer days_after_birth_due, int position) {
            this.vaccineCard = vaccineCard;
            vaccineName = vaccineData.name;
            this.childDetails = childDetails;
            this.days_after_birth_due = days_after_birth_due;
            this.position = position;
            this.vaccineData = vaccineData;
        }

        @Override
        protected VaccineWrapper doInBackground(Void... params) {
            VaccineWrapper vaccineWrapper = new VaccineWrapper();
            vaccineWrapper.setId(childDetails.entityId());
            vaccineWrapper.setGender(childDetails.getDetails().get("gender"));
            vaccineWrapper.setName(vaccineName);
            vaccineWrapper.setDefaultName(vaccineName);
            if (vaccineData.schedule != null && vaccineData.schedule.conditions != null) {
                vaccineWrapper.setNotGivenCondition(vaccineData.schedule.conditions.get(0).vaccine);
            }

            String dobString = getValue(childDetails.getColumnmaps(), "dob", false);
            if (StringUtils.isNotBlank(dobString)) {
                Calendar dobCalender = Calendar.getInstance();
                DateTime dateTime = new DateTime(dobString);
                dobCalender.setTime(dateTime.toDate());
                dobCalender.add(Calendar.DATE, days_after_birth_due);
                vaccineWrapper.setVaccineDate(new DateTime(dobCalender.getTime()));
            }

            Photo photo = ImageUtils.profilePhotoByClient(childDetails);
            vaccineWrapper.setPhoto(photo);

            String zeirId = getValue(childDetails.getColumnmaps(), "zeir_id", false);
            vaccineWrapper.setPatientNumber(zeirId);

            String firstName = getValue(childDetails.getColumnmaps(), "first_name", true);
            String lastName = getValue(childDetails.getColumnmaps(), "last_name", true);
            String childName = getName(firstName, lastName);
            vaccineWrapper.setPatientName(childName.trim());

            updateWrapper(vaccineWrapper);
            updateWrapperStatus(vaccineWrapper, type, childDetails);

            return vaccineWrapper;
        }

        @Override
        protected void onPostExecute(VaccineWrapper vaccineWrapper) {
            vaccineCard.setVaccineWrapper(vaccineWrapper);

            //If last position, toggle RecordAll
            if (position == (getCount() - 1)) {
                vaccineGroup.toggleRecordAllTV();
            }
            notifyDataSetChanged();
            notifyAsyncTaskCompleted();
        }
    }

}
