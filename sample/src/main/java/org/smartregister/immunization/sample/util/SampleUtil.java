package org.smartregister.immunization.sample.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.LinearLayout;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.fragment.ServiceDialogFragment;
import org.smartregister.immunization.fragment.UndoServiceDialogFragment;
import org.smartregister.immunization.fragment.UndoVaccinationDialogFragment;
import org.smartregister.immunization.fragment.VaccinationDialogFragment;
import org.smartregister.immunization.sample.R;
import org.smartregister.immunization.view.ServiceGroup;
import org.smartregister.immunization.view.VaccineGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by keyman on 01/08/2017.
 */
public class SampleUtil {
    public static final String ENTITY_ID = "1";
    public static final String DOB_STRING = "2017-01-01T00:00:00.000Z";
    public static final String GENDER = (new Random()).nextBoolean() ? "male" : "female";

    public static CommonPersonObjectClient dummyDetatils() {
        HashMap<String, String> columnMap = new HashMap<String, String>();
        columnMap.put("first_name", "Test");
        columnMap.put("last_name", "Doe");
        columnMap.put("zeir_id", "1");
        columnMap.put("dob", DOB_STRING);
        columnMap.put("gender", GENDER);


        CommonPersonObjectClient personDetails = new CommonPersonObjectClient(ENTITY_ID, columnMap, "Test");
        personDetails.setColumnmaps(columnMap);

        return personDetails;
    }

    public static void addVaccinationDialogFragment(Activity context, String tag, ArrayList<VaccineWrapper> vaccineWrappers, VaccineGroup vaccineGroup) {
        vaccineGroup.setModalOpen(true);
        String dobString = SampleUtil.DOB_STRING;
        Date dob = Calendar.getInstance().getTime();
        if (!TextUtils.isEmpty(dobString)) {
            DateTime dateTime = new DateTime(dobString);
            dob = dateTime.toDate();
        }

        List<Vaccine> vaccineList = ImmunizationLibrary.getInstance().vaccineRepository()
                .findByEntityId(ENTITY_ID);
        if (vaccineList == null) vaccineList = new ArrayList<>();

        VaccinationDialogFragment vaccinationDialogFragment = VaccinationDialogFragment.newInstance(dob, vaccineList, vaccineWrappers);
        vaccinationDialogFragment.show(initFragmentTransaction(context, tag), tag);
    }

    public static void addServiceDialogFragment(Activity context, String tag, ServiceWrapper serviceWrapper, ServiceGroup serviceGroup) {
        serviceGroup.setModalOpen(true);

        List<ServiceRecord> serviceRecordList = ImmunizationLibrary.getInstance().recurringServiceRecordRepository()
                .findByEntityId(ENTITY_ID);

        ServiceDialogFragment serviceDialogFragment = ServiceDialogFragment.newInstance(serviceRecordList, serviceWrapper);
        serviceDialogFragment.show(initFragmentTransaction(context, tag), tag);
    }

    public static void addVaccineUndoDialogFragment(Activity context, String tag, VaccineGroup vaccineGroup, VaccineWrapper vaccineWrapper) {
        vaccineGroup.setModalOpen(true);

        UndoVaccinationDialogFragment undoVaccinationDialogFragment = UndoVaccinationDialogFragment.newInstance(vaccineWrapper);
        undoVaccinationDialogFragment.show(initFragmentTransaction(context, tag), tag);
        ;
    }

    public static void addServiceUndoDialogFragment(Activity context, String tag, ServiceGroup serviceGroup, ServiceWrapper serviceWrapper) {
        serviceGroup.setModalOpen(true);

        UndoServiceDialogFragment undoServiceDialogFragment = UndoServiceDialogFragment.newInstance(serviceWrapper);
        undoServiceDialogFragment.show(initFragmentTransaction(context, tag), tag);
    }

    public static VaccineGroup addVaccineGroup(final Activity context, final String tag, int canvasId, JSONObject vaccineGroupData, List<Vaccine> vaccineList, List<Alert> alerts) {
        LinearLayout vaccineGroupCanvasLL = (LinearLayout) context.findViewById(R.id.vaccine_group_canvas_ll);
        VaccineGroup curGroup = new VaccineGroup(context);
        curGroup.setData(vaccineGroupData, SampleUtil.dummyDetatils(), vaccineList, alerts);
        curGroup.setOnRecordAllClickListener(new VaccineGroup.OnRecordAllClickListener() {
            @Override
            public void onClick(VaccineGroup vaccineGroup, ArrayList<VaccineWrapper> dueVaccines) {
                addVaccinationDialogFragment(context, tag, dueVaccines, vaccineGroup);
            }
        });
        curGroup.setOnVaccineClickedListener(new VaccineGroup.OnVaccineClickedListener() {
            @Override
            public void onClick(VaccineGroup vaccineGroup, VaccineWrapper vaccine) {
                ArrayList<VaccineWrapper> vaccineWrappers = new ArrayList<VaccineWrapper>();
                vaccineWrappers.add(vaccine);
                addVaccinationDialogFragment(context, tag, vaccineWrappers, vaccineGroup);
            }
        });
        curGroup.setOnVaccineUndoClickListener(new VaccineGroup.OnVaccineUndoClickListener() {
            @Override
            public void onUndoClick(VaccineGroup vaccineGroup, VaccineWrapper vaccine) {
                addVaccineUndoDialogFragment(context, tag, vaccineGroup, vaccine);
            }
        });

        LinearLayout parent;
        if (canvasId == -1) {
            Random r = new Random();
            canvasId = r.nextInt(4232 - 213) + 213;
            parent = new LinearLayout(context);
            parent.setId(canvasId);
            vaccineGroupCanvasLL.addView(parent);
        } else {
            parent = (LinearLayout) context.findViewById(canvasId);
            parent.removeAllViews();
        }
        parent.addView(curGroup);
        curGroup.setTag(R.id.vaccine_group_vaccine_data, vaccineGroupData.toString());
        curGroup.setTag(R.id.vaccine_group_parent_id, String.valueOf(canvasId));

        return curGroup;
    }

    public static FragmentTransaction initFragmentTransaction(Activity context, String tag) {
        FragmentTransaction ft = context.getFragmentManager().beginTransaction();
        Fragment prev = context.getFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return ft;
    }

}
