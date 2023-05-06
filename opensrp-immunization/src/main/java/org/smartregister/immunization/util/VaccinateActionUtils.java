package org.smartregister.immunization.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.DateUtil;
import org.smartregister.commonregistry.AllCommonsRepository;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.domain.form.FormSubmission;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.VaccinateFormSubmissionWrapper;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.fragment.VaccinationDialogFragment;
import org.smartregister.service.AlertService;
import org.smartregister.service.ZiggyService;
import org.smartregister.util.FormUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.smartregister.AllConstants.ENTITY_ID_PARAM;
import static org.smartregister.AllConstants.FORM_NAME_PARAM;
import static org.smartregister.AllConstants.INSTANCE_ID_PARAM;
import static org.smartregister.AllConstants.SYNC_STATUS;
import static org.smartregister.AllConstants.VERSION_PARAM;
import static org.smartregister.domain.SyncStatus.PENDING;
import static org.smartregister.util.EasyMap.create;
import static org.smartregister.util.Utils.convertDateFormat;

/**
 * Created by keyman on 17/11/2016.
 */
public class VaccinateActionUtils {

    public static String formData(Context context, String entityId, String formName, String metaData) {
        try {
            return FormUtils.getInstance(context).generateXMLInputForFormWithEntityId(entityId, formName, metaData);
        } catch (Exception e) {
            Log.e(VaccinateActionUtils.class.getName(), "", e);
            return null;
        }
    }

    public static void updateJson(JSONObject jsonObject, String field, String value) {
        try {
            if (jsonObject.has(field)) {
                JSONObject fieldJson = jsonObject.getJSONObject(field);
                fieldJson.put("content", value);
            }
        } catch (JSONException e) {
            Log.e(VaccinateActionUtils.class.getName(), "", e);
        }
    }

    public static JSONObject find(JSONObject jsonObject, String field) {
        try {
            if (jsonObject.has(field)) {
                return jsonObject.getJSONObject(field);

            }
        } catch (JSONException e) {
            Log.e(VaccinateActionUtils.class.getName(), "", e);
        }

        return null;
    }


    public static TableRow findRow(Set<TableLayout> tables, String tag) {
        for (TableLayout table : tables) {
            View view = table.findViewWithTag(tag);
            if (view != null && view instanceof TableRow) {
                return (TableRow) view;
            }
        }
        return null;
    }

    public static TableRow findRow(TableLayout table, String tag) {
        View view = table.findViewWithTag(tag);
        if (view != null && view instanceof TableRow) {
            return (TableRow) view;
        }
        return null;
    }

    public static void vaccinateToday(TableRow tableRow, VaccineWrapper tag) {
        TextView textView = tableRow.findViewById(R.id.date);
        textView.setText(R.string.done_today);

        Button button = tableRow.findViewById(R.id.undo);
        button.setVisibility(View.VISIBLE);

        String color = "#31B404";
        Button status = tableRow.findViewById(R.id.status);
        status.setBackgroundColor(Color.parseColor(color));

        tableRow.setOnClickListener(null);
    }

    public static void vaccinateEarlier(TableRow tableRow, VaccineWrapper tag) {
        String vaccineDate = convertDateFormat(tag.getUpdatedVaccineDateAsString(), true);

        TextView textView = tableRow.findViewById(R.id.date);
        textView.setText(vaccineDate);

        Button button = tableRow.findViewById(R.id.undo);
        button.setVisibility(View.VISIBLE);

        String color = "#31B404";
        Button status = tableRow.findViewById(R.id.status);
        status.setBackgroundColor(Color.parseColor(color));

        tableRow.setOnClickListener(null);
    }

    public static void undoVaccination(final Context context, TableRow tableRow, final VaccineWrapper tag) {
        Button button = tableRow.findViewById(R.id.undo);
        button.setVisibility(View.GONE);

        String color = tag.getColor();
        Button status = tableRow.findViewById(R.id.status);
        status.setBackgroundColor(Color.parseColor(color));

        TextView v = tableRow.findViewById(R.id.date);
        v.setText(tag.getFormattedVaccineDate());

        if ("due".equalsIgnoreCase(tag.getStatus())) {
            tableRow.setOnClickListener(new TableRow.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    Fragment prev =
                            ((FragmentActivity) context).getSupportFragmentManager()
                                    .findFragmentByTag(VaccinationDialogFragment.DIALOG_TAG);
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    ArrayList<VaccineWrapper> list = new ArrayList<VaccineWrapper>();
                    list.add(tag);
                    VaccinationDialogFragment vaccinationDialogFragment = VaccinationDialogFragment
                            .newInstance(null, null, list);
                    vaccinationDialogFragment.show(ft, VaccinationDialogFragment.DIALOG_TAG);

                }
            });
        }

    }

    public static void saveFormSubmission(Context appContext, String formSubmission, String id, String formName,
                                          JSONObject fieldOverrides) {

        Log.v("fieldoverride", fieldOverrides.toString());

        // save the form
        try {
            FormUtils formUtils = FormUtils.getInstance(appContext);
            FormSubmission submission = formUtils
                    .generateFormSubmisionFromXMLString(id, formSubmission, formName, fieldOverrides);

            org.smartregister.Context context = ImmunizationLibrary.getInstance().context();
            ZiggyService ziggyService = context.ziggyService();
            ziggyService.saveForm(getParams(submission), submission.instance());

            // Update Fts Tables
            CommonFtsObject commonFtsObject = context.commonFtsObject();
            if (commonFtsObject != null) {
                String[] ftsTables = commonFtsObject.getTables();
                for (String ftsTable : ftsTables) {
                    AllCommonsRepository allCommonsRepository = context.allCommonsRepositoryobjects(ftsTable);
                    boolean updated = allCommonsRepository.updateSearch(submission.entityId());
                    if (updated) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(VaccinateActionUtils.class.getName(), "", e);
            e.printStackTrace();
        }
    }

    private static String getParams(FormSubmission submission) {
        return new Gson().toJson(
                create(INSTANCE_ID_PARAM, submission.instanceId())
                        .put(ENTITY_ID_PARAM, submission.entityId())
                        .put(FORM_NAME_PARAM, submission.formName())
                        .put(VERSION_PARAM, submission.version())
                        .put(SYNC_STATUS, PENDING.value())
                        .map());
    }

    public static JSONObject retrieveFieldOverides(String overrides) {
        try {
            //get the field overrides map
            if (overrides != null) {
                JSONObject json = new JSONObject(overrides);
                String overridesStr = json.getString("fieldOverrides");
                return new JSONObject(overridesStr);
            }
        } catch (Exception e) {
            Log.e(VaccinateActionUtils.class.getName(), "", e);
        }
        return new JSONObject();

    }

    public static String retrieveExistingAge(VaccinateFormSubmissionWrapper vaccinateFormSubmissionWrapper) {
        try {
            if (vaccinateFormSubmissionWrapper != null) {
                JSONObject fieldOverrides = vaccinateFormSubmissionWrapper.getOverrides();
                if (fieldOverrides.has("existing_age")) {
                    return fieldOverrides.getString("existing_age");
                }
            }
        } catch (JSONException e) {
            Log.e(VaccinateActionUtils.class.getName(), "", e);
        }
        return null;
    }

    public static boolean addDialogHookCustomFilter(VaccineWrapper tag) {
        boolean addHook = false;

        int age = 0;
        String existingAge = tag.getExistingAge();
        if (StringUtils.isNumeric(existingAge)) {
            age = Integer.valueOf(existingAge);
        }

        VaccineRepo.Vaccine vaccine = tag.getVaccine();
        switch (vaccine) {
            case penta1:
            case pcv1:
            case opv1:
                if (age > 35)
                    addHook = true;
                break;
            case penta2:
            case pcv2:
            case opv2:
                if (age > 63)
                    addHook = true;
                break;
            case penta3:
            case pcv3:
            case opv3:
            case ipv:
                if (age > 91)
                    addHook = true;
                break;
            case measles1:
                if (age > 250)
                    addHook = true;
                break;
            case measles2:
                if (age > 340)
                    addHook = true;
                break;
            default:
                addHook = true;
                break;
        }

        return addHook;

    }

    public static String[] allAlertNames(String category) {
        if (category == null) {
            return null;
        }
        if ("child".equals(category)) {

            ArrayList<VaccineRepo.Vaccine> vaccines = VaccineRepo.getVaccines("child");
            List<String> names = new ArrayList<>();

            for (VaccineRepo.Vaccine vaccine : vaccines) {
                names.add(vaccine.display());
                names.add(vaccine.name());
            }

            return names.toArray(new String[names.size()]);
        }
        if ("woman".equals(category)) {

            ArrayList<VaccineRepo.Vaccine> vaccines = VaccineRepo.getVaccines("woman");
            List<String> names = new ArrayList<>();

            for (VaccineRepo.Vaccine vaccine : vaccines) {
                names.add(vaccine.display());
                names.add(vaccine.name());
            }

            return names.toArray(new String[names.size()]);
        }
        return null;
    }

    public static String[] allAlertNames(Collection<List<ServiceType>> typeList) {
        if (typeList == null) {
            return null;
        }

        List<String> names = new ArrayList<>();

        for (List<ServiceType> serviceTypes : typeList) {
            if (serviceTypes != null) {
                String[] array = allAlertNames(serviceTypes);
                if (array != null) {
                    names.addAll(Arrays.asList(array));
                }
            }
        }

        return names.toArray(new String[names.size()]);
    }

    public static String[] allAlertNames(List<ServiceType> list) {
        if (list == null) {
            return null;
        }

        List<String> names = new ArrayList<>();

        for (ServiceType serviceType : list) {
            names.add(serviceType.getName().toLowerCase().replaceAll("\\s+", ""));
            names.add(serviceType.getName());
        }
        return names.toArray(new String[names.size()]);
    }

    public static String addHyphen(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replace(" ", "_");
        }
        return s;
    }

    public static String removeHyphen(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replace("_", " ");
        }
        return s;
    }

    public static void populateDefaultAlerts(AlertService alertService, List<Vaccine> vaccineList,
                                             List<Alert> alertList, String entityId,
                                             DateTime birthDateTime, VaccineRepo.Vaccine[] vList) {

        if (vList == null || vList.length == 0) {
            return;
        }

        List<Alert> defaultAlerts = new ArrayList<Alert>();
        for (VaccineRepo.Vaccine v : vList) {
            if ((!VaccinateActionUtils.hasVaccine(vaccineList, v)) && (!VaccinateActionUtils.hasAlert(alertList, v))) {
                defaultAlerts.add(VaccinateActionUtils.createDefaultAlert(v, entityId, birthDateTime));
            }
        }

        for (Alert alert : defaultAlerts) {
            alertService.create(alert);
            alertService.updateFtsSearch(alert, true);
        }
    }

    public static boolean hasVaccine(List<Vaccine> vaccineList, VaccineRepo.Vaccine v) {
        if (vaccineList == null || vaccineList.isEmpty() || v == null) {
            return false;
        }

        for (Vaccine vaccine : vaccineList) {
            if (vaccine.getName().equalsIgnoreCase(v.display().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAlert(List<Alert> alerts, VaccineRepo.Vaccine vaccine) {
        if (alerts == null || alerts.isEmpty() || vaccine == null) {
            return false;
        }

        for (Alert alert : alerts) {
            if (alert.scheduleName().replaceAll(" ", "").equalsIgnoreCase(vaccine.name())
                    || alert.visitCode().replaceAll(" ", "").equalsIgnoreCase(vaccine.name())) {
                return true;
            }
        }
        return false;
    }

    public static Alert createDefaultAlert(VaccineRepo.Vaccine vaccine, String entityId, DateTime birthDateTime) {


        AlertStatus alertStatus = AlertStatus.upcoming;
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Date birthDate = birthDateTime.toDate();
        if (birthDateTime != null && birthDateTime.plusDays(vaccine.expiryDays()).isBefore(DateTime.now())) {
            alertStatus = AlertStatus.expired;
        } else if (birthDate.getTime() > (today.getTimeInMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS))) {
            // Vaccination due more than one day from today
            alertStatus = AlertStatus.upcoming;
        } else if (birthDate.getTime() < (today.getTimeInMillis() - TimeUnit.MILLISECONDS.convert(10, TimeUnit.DAYS))) {
            // Vaccination overdue
            alertStatus = AlertStatus.urgent;
        } else {
            alertStatus = AlertStatus.normal;
        }

        return new Alert(entityId, vaccine.display(), vaccine.name(), alertStatus, DateUtil.yyyyMMdd.format(birthDate),
                null);

    }

    public static Alert getAlert(List<Alert> alerts, VaccineRepo.Vaccine vaccine) {
        if (alerts == null || alerts.isEmpty() || vaccine == null) {
            return null;
        }

        for (Alert alert : alerts) {
            if (alert.scheduleName().replaceAll(" ", "").equalsIgnoreCase(vaccine.name())
                    || alert.visitCode().replaceAll(" ", "").equalsIgnoreCase(vaccine.name())) {
                return alert;
            }
        }
        return null;
    }

    public static Vaccine getVaccine(List<Vaccine> vaccineList, VaccineRepo.Vaccine v) {
        if (vaccineList == null || vaccineList.isEmpty() || v == null) {
            return null;
        }

        for (Vaccine vaccine : vaccineList) {
            if (vaccine.getName().equalsIgnoreCase(v.display().toLowerCase())) {
                return vaccine;
            }
        }
        return null;
    }

    /**
     * Picks the BCG 2 special vaccine from the loaded list of special_vaccines and adds it to the birth group
     *
     * @param context
     * @param vaccineGroupObject
     * @param vaccineList
     * @return
     */
    public static boolean addBcg2SpecialVaccine(Context context, VaccineGroup vaccineGroupObject, List<Vaccine> vaccineList) {
        //Add BCG2 special vaccine to birth vaccine group
        if (VaccinateActionUtils.hasVaccine(vaccineList, VaccineRepo.Vaccine.bcg2)
                && vaccineGroupObject.days_after_birth_due != null
                && vaccineGroupObject.vaccines != null
                && "Birth".equalsIgnoreCase(vaccineGroupObject.name)
                && "0".equalsIgnoreCase(vaccineGroupObject.days_after_birth_due.toString())) {

            List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = VaccinatorUtils
                    .getSpecialVaccines(context);

            if (specialVaccines != null && !specialVaccines.isEmpty()) {
                for (org.smartregister.immunization.domain.jsonmapping.Vaccine vaccine : specialVaccines) {
                    if (vaccine.name != null
                            && vaccine.type != null
                            && vaccine.name.equalsIgnoreCase(VaccineRepo.Vaccine.bcg2.display())
                            && vaccine.type.equalsIgnoreCase(VaccineRepo.Vaccine.bcg.display())) {
                        vaccineGroupObject.vaccines.add(vaccine);
                        return true;
                    }
                }
            }
        }

        return false;
    }
    public static boolean isInvalidVaccine(DateTime vaccineDate, DateTime dueDate) {
        return vaccineDate!=null && dueDate!=null && vaccineDate.getMillis() - dueDate.getMillis()<0;
    }
    public static boolean isValidAfterInvalid(DateTime dueDate){
        return dueDate != null && dueDate.getMillis() < new DateTime().getMillis();
    }
    public static boolean moreThanThreeMonths(Date createdAt) {
        return createdAt != null && org.smartregister.util.DateUtil.checkIfDateThreeMonthsOlder(createdAt);
    }

    public static boolean lessThanThreeMonths(Vaccine vaccine) {
        ////////////////////////check 3 months///////////////////////////////
        return vaccine == null || vaccine.getCreatedAt() == null || !org.smartregister.util.DateUtil
                .checkIfDateThreeMonthsOlder(vaccine.getCreatedAt());
        ///////////////////////////////////////////////////////////////////////
    }


    public static boolean lessThanThreeMonths(ServiceRecord serviceRecord) {
        ////////////////////////check 3 months///////////////////////////////
        return serviceRecord == null || serviceRecord.getCreatedAt() == null || !org.smartregister.util.DateUtil
                .checkIfDateThreeMonthsOlder(serviceRecord.getCreatedAt());
        ///////////////////////////////////////////////////////////////////////
    }

}
