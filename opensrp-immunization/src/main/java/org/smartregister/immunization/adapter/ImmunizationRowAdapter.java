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
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.immunization.view.ImmunizationRowCard;
import org.smartregister.immunization.view.ImmunizationRowGroup;
import org.smartregister.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.smartregister.immunization.util.VaccinatorUtils.generateScheduleList;
import static org.smartregister.immunization.util.VaccinatorUtils.receivedVaccines;
import static org.smartregister.util.Utils.getValue;

/**
 * Created by raihan on 13/03/2017.
 */
public class ImmunizationRowAdapter extends BaseAdapter {
    private static final String TAG = "ImmunizationRowAdapter";
    private final Context context;
    private final ImmunizationRowGroup vaccineGroup;
    public boolean editmode;
    private HashMap<String, ImmunizationRowCard> vaccineCards;
    private List<Vaccine> vaccineList;
    private List<Alert> alertList;

    public ImmunizationRowAdapter(Context context, ImmunizationRowGroup vaccineGroup,
                                  boolean editmode, List<Vaccine> vaccineList, List<Alert> alertList) {
        this.context = context;
        this.editmode = editmode;
        this.vaccineGroup = vaccineGroup;
        this.vaccineList = vaccineList;
        this.alertList = alertList;
        vaccineCards = new HashMap<>();
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
            org.smartregister.immunization.domain.jsonmapping.Vaccine vaccineData = vaccineGroup.getVaccineData().vaccines
                    .get(position);
            String vaccineName = vaccineData.name;
            if (!vaccineCards.containsKey(vaccineName)) {
                ImmunizationRowCard vaccineCard = new ImmunizationRowCard(context, editmode);
                vaccineCard.setId((int) getItemId(position));
                vaccineCards.put(vaccineName, vaccineCard);
                ImmunizationRowTask immunizationRowTask = new ImmunizationRowTask(vaccineCard, vaccineName,
                        vaccineGroup.getVaccineData().days_after_birth_due,
                        vaccineGroup.getChildDetails());
                Utils.startAsyncTask(immunizationRowTask, null);
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
                for (ImmunizationRowCard curCard : vaccineCards.values()) {
                    if (curCard != null) curCard.updateState();
                }
            } else {// Update just the vaccines specified
                for (VaccineWrapper currWrapper : vaccinesToUpdate) {
                    if (vaccineCards.containsKey(currWrapper.getName())) {
                        vaccineCards.get(currWrapper.getName()).updateState();
                    }
                }
            }
        }
    }

    public ArrayList<VaccineWrapper> getDueVaccines() {
        ArrayList<VaccineWrapper> dueVaccines = new ArrayList<>();
        if (vaccineCards != null) {
            for (ImmunizationRowCard curCard : vaccineCards.values()) {
                if (curCard != null && (curCard.getState().equals(State.DUE)
                        || curCard.getState().equals(State.OVERDUE))) {
                    dueVaccines.add(curCard.getVaccineWrapper());
                }
            }
        }

        return dueVaccines;
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
                        if ((array[0]).toLowerCase().contains(vaccine.getName())) {
                            tag.setName(array[0]);
                        } else if ((array[1]).toLowerCase().contains(vaccine.getName())) {
                            tag.setName(array[1]);
                        }
                    }
                    tag.setCreatedAt(vaccine.getCreatedAt());
                }
            }
        }
    }

    public List<Vaccine> getVaccineList() {
        return vaccineList;
    }

    public void setVaccineList(List<Vaccine> vaccineList) {
        this.vaccineList = vaccineList;
    }

    public void updateWrapperStatus(ArrayList<VaccineWrapper> tags, CommonPersonObjectClient childDetails) {
        if (tags == null) {
            return;
        }

        for (VaccineWrapper tag : tags) {
            updateWrapperStatus(tag, childDetails);
        }
    }

    public void updateWrapperStatus(VaccineWrapper tag, CommonPersonObjectClient childDetails) {
        List<Vaccine> vaccineList = getVaccineList();

        List<Alert> alertList = getAlertList();
        Map<String, Date> recievedVaccines = receivedVaccines(vaccineList);
        String dobString = getValue(childDetails.getColumnmaps(), "dob", false);
        DateTime dateTime = !dobString.isEmpty() ? new DateTime(dobString) : new DateTime();
        List<Map<String, Object>> sch = generateScheduleList("child", dateTime, recievedVaccines, alertList);

        for (Map<String, Object> m : sch) {
            VaccineRepo.Vaccine vaccine = (VaccineRepo.Vaccine) m.get("vaccine");
            if (tag.getName().toLowerCase().contains(vaccine.display().toLowerCase())) {

                //Add exception for bcg 2
                if (tag.getName().equalsIgnoreCase(VaccineRepo.Vaccine.bcg2.display()) && !tag.getName()
                        .equalsIgnoreCase(vaccine.display())) {
                    continue;
                }

                tag.setStatus(m.get("status").toString());
                tag.setAlert((Alert) m.get("alert"));

            }
        }
    }

    public List<Alert> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<Alert> alertList) {
        this.alertList = alertList;
    }

    class ImmunizationRowTask extends AsyncTask<Void, Void, VaccineWrapper> {

        private ImmunizationRowCard vaccineCard;

        private String vaccineName;

        private int days_after_birth_due;

        private CommonPersonObjectClient childDetails;

        ImmunizationRowTask(ImmunizationRowCard vaccineCard, String vaccineName, int days_after_birth_due,
                            CommonPersonObjectClient childDetails) {
            this.vaccineCard = vaccineCard;
            this.vaccineName = vaccineName;
            this.days_after_birth_due = days_after_birth_due;
            this.childDetails = childDetails;
        }

        @Override
        protected VaccineWrapper doInBackground(Void... params) {

            VaccineWrapper vaccineWrapper = new VaccineWrapper();
            vaccineWrapper.setId(childDetails.entityId());
            vaccineWrapper.setGender(childDetails.getDetails().get("gender"));
            vaccineWrapper.setName(vaccineName);

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
            vaccineWrapper.setPatientName(
                    getValue(childDetails.getColumnmaps(), "first_name", true) + " " + getValue(childDetails.getColumnmaps(),
                            "last_name", true));

            updateWrapper(vaccineWrapper);
            updateWrapperStatus(vaccineWrapper, childDetails);
            return vaccineWrapper;
        }

        @Override
        protected void onPostExecute(VaccineWrapper vaccineWrapper) {
            vaccineCard.setVaccineWrapper(vaccineWrapper);
            vaccineGroup.toggleRecordAllTV();
            notifyDataSetChanged();
        }
    }
}
