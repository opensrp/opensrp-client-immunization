package org.smartregister.immunization.sample;

import static org.smartregister.util.Utils.getName;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceSchedule;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineSchedule;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.fragment.ServiceDialogFragment;
import org.smartregister.immunization.fragment.UndoServiceDialogFragment;
import org.smartregister.immunization.fragment.UndoVaccinationDialogFragment;
import org.smartregister.immunization.fragment.VaccinationDialogFragment;
import org.smartregister.immunization.listener.ServiceActionListener;
import org.smartregister.immunization.listener.VaccinationActionListener;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.sample.util.SampleUtil;
import org.smartregister.immunization.service.intent.RecurringIntentService;
import org.smartregister.immunization.service.intent.VaccineIntentService;
import org.smartregister.util.AppExecutors;
import org.smartregister.util.CallableInteractorCallBack;
import org.smartregister.util.GenericInteractor;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.IMDatabaseConstants;
import org.smartregister.immunization.util.RecurringServiceUtils;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.view.ServiceGroup;
import org.smartregister.immunization.view.VaccineGroup;
import org.smartregister.service.AlertService;
import org.smartregister.util.DateUtil;
import org.smartregister.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import timber.log.Timber;

import timber.log.Timber;

/**
 * Created by Jason Rogena - jrogena@ona.io on 16/02/2017.
 */

public class MainActivity extends AppCompatActivity implements VaccinationActionListener, ServiceActionListener {


    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String DIALOG_TAG = "DIALOG_TAAAGGG";
    private static final String EXTRA_CHILD_DETAILS = "child_details";
    private static final boolean isChildActive = true;
    // Data
    private CommonPersonObjectClient childDetails = SampleUtil.dummyDetatils();
    private ArrayList<VaccineGroup> vaccineGroups;
    private ArrayList<ServiceGroup> serviceGroups;
    private GenericInteractor genericInteractor = new GenericInteractor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        setUpViewGroups();

        updateViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        startServices();
    }

    private void setUpViewGroups() {
        if (vaccineGroups != null) {
            LinearLayout vaccineGroupCanvasLL = findViewById(R.id.vaccine_group_canvas_ll);
            vaccineGroupCanvasLL.removeAllViews();
            vaccineGroups = null;
        }

        if (serviceGroups != null) {
            LinearLayout serviceGroupCanvasLL = findViewById(R.id.service_group_canvas_ll);
            serviceGroupCanvasLL.removeAllViews();
            serviceGroups = null;
        }
    }

    private boolean isDataOk() {
        return childDetails != null && childDetails.getDetails() != null;
    }

    private void updateViews() {
        findViewById(R.id.profile_name_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDetailActivity(MainActivity.this, childDetails);
            }
        });

        updateAgeViews();
        updateChildIdViews();

        VaccineRepository vaccineRepository = ImmunizationLibrary.getInstance().vaccineRepository();

        RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance().recurringServiceTypeRepository();

        RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance().recurringServiceRecordRepository();

        AlertService alertService = ImmunizationLibrary.getInstance().context().alertService();

        UpdateViewTaskCallableInteractorCallback updateViewTaskCallableInteractorCallback = new UpdateViewTaskCallableInteractorCallback();
        UpdateViewTaskCallable callable = new UpdateViewTaskCallable();
        callable.setVaccineRepository(vaccineRepository);
        callable.setRecurringServiceTypeRepository(recurringServiceTypeRepository);
        callable.setRecurringServiceRecordRepository(recurringServiceRecordRepository);
        callable.setAlertService(alertService);

        genericInteractor.execute(callable, updateViewTaskCallableInteractorCallback);

    }

    private void updateChildIdViews() {
        String name = "";
        String childId = "";
        if (isDataOk()) {
            name = constructChildName();
            childId = Utils.getValue(childDetails.getColumnmaps(), "zeir_id", false);
        }
        findViewById(R.id.outOfCatchment).setVisibility(ImmunizationLibrary.getInstance().getProperties().isTrue(IMConstants.APP_PROPERTIES.NOVEL_OUT_OF_CATCHMENT) ? View.VISIBLE : View.GONE);
        TextView nameTV = findViewById(R.id.name_tv);
        nameTV.setText(name);
        TextView childIdTV = findViewById(R.id.child_id_tv);
        childIdTV.setText(String.format("%s: %s", "ID", childId));
    }

    private void updateAgeViews() {
        String formattedAge = "";
        String formattedDob = "";
        if (isDataOk()) {
            String dobString = Utils.getValue(childDetails.getColumnmaps(), "dob", false);
            if (!TextUtils.isEmpty(dobString)) {
                DateTime dateTime = new DateTime(dobString);
                Date dob = dateTime.toDate();
                formattedDob = SampleUtil.DATE_FORMAT.format(dob);
                long timeDiff = Calendar.getInstance().getTimeInMillis() - dob.getTime();

                if (timeDiff >= 0) {
                    formattedAge = DateUtil.getDuration(timeDiff);
                }
            }
        }
        TextView dobTV = findViewById(R.id.dob_tv);
        dobTV.setText(String.format("%s: %s", "Birth Date", formattedDob));
        TextView ageTV = findViewById(R.id.age_tv);
        ageTV.setText(String.format("%s: %s", "Age", formattedAge));
    }

    private void updateServiceViews(Map<String, List<ServiceType>> serviceTypeMap, List<ServiceRecord> serviceRecordList, List<Alert> alerts) {

        Map<String, List<ServiceType>> foundServiceTypeMap = new LinkedHashMap<>();
        if (serviceGroups == null) {
            for (String type : serviceTypeMap.keySet()) {
                if (foundServiceTypeMap.containsKey(type)) {
                    continue;
                }

                for (ServiceRecord serviceRecord : serviceRecordList) {
                    //if (serviceRecord.getSyncStatus().equals(RecurringServiceTypeRepository.TYPE_Synced)) {
                    if (serviceRecord.getType().equals(type)) {
                        foundServiceTypeMap.put(type, serviceTypeMap.get(type));
                        break;
                    }
                    //}
                }
                foundServiceTypeMap.put(type, serviceTypeMap.get(type)); // put everything that is a service

                if (foundServiceTypeMap.containsKey(type)) {
                    continue;
                }

                for (Alert a : alerts) {
                    if (StringUtils.containsIgnoreCase(a.scheduleName(), type)
                            || StringUtils.containsIgnoreCase(a.visitCode(), type)) {
                        foundServiceTypeMap.put(type, serviceTypeMap.get(type));
                        break;
                    }
                }

            }

            if (foundServiceTypeMap.isEmpty()) {
                return;
            }

            serviceGroups = new ArrayList<>();
            LinearLayout serviceGroupCanvasLL = findViewById(R.id.service_group_canvas_ll);

            ServiceGroup curGroup = new ServiceGroup(this);
            curGroup.setChildActive(isChildActive);
            curGroup.setData(childDetails, foundServiceTypeMap, serviceRecordList, alerts);
            curGroup.setOnServiceClickedListener(new ServiceGroup.OnServiceClickedListener() {
                @Override
                public void onClick(ServiceGroup serviceGroup, ServiceWrapper
                        serviceWrapper) {
                    addServiceDialogFragment(serviceWrapper, serviceGroup);
                }
            });
            curGroup.setOnServiceUndoClickListener(new ServiceGroup.OnServiceUndoClickListener() {
                @Override
                public void onUndoClick(ServiceGroup serviceGroup, ServiceWrapper serviceWrapper) {
                    addServiceUndoDialogFragment(serviceGroup, serviceWrapper);
                }
            });
            serviceGroupCanvasLL.addView(curGroup);
            serviceGroups.add(curGroup);
        }

    }

    private void updateVaccinationViews(List<Vaccine> vaccineList, List<Alert> alerts) {

        if (vaccineGroups == null) {
            vaccineGroups = new ArrayList<>();
            List<org.smartregister.immunization.domain.jsonmapping.VaccineGroup> supportedVaccines =
                    VaccinatorUtils.getSupportedVaccines(this);

            for (org.smartregister.immunization.domain.jsonmapping.VaccineGroup vaccineGroupObject : supportedVaccines) {
                //Add BCG2 special vaccine to birth vaccine group
                VaccinateActionUtils.addBcg2SpecialVaccine(this, vaccineGroupObject, vaccineList);

                addVaccineGroup(-1, vaccineGroupObject, vaccineList, alerts);
            }
        }
    }


    private void addVaccineGroup(int canvasId, org.smartregister.immunization.domain.jsonmapping.VaccineGroup vaccineGroupData, List<Vaccine> vaccineList, List<Alert> alerts) {
        LinearLayout vaccineGroupCanvasLL = findViewById(R.id.vaccine_group_canvas_ll);
        VaccineGroup curGroup = new VaccineGroup(this);
        curGroup.setChildActive(isChildActive);
        curGroup.setData(vaccineGroupData, childDetails, vaccineList, alerts, "child");
        curGroup.setOnRecordAllClickListener(new VaccineGroup.OnRecordAllClickListener() {
            @Override
            public void onClick(VaccineGroup vaccineGroup, ArrayList<VaccineWrapper> dueVaccines) {
                addVaccinationDialogFragment(dueVaccines, vaccineGroup);
            }
        });
        curGroup.setOnVaccineClickedListener(new VaccineGroup.OnVaccineClickedListener() {
            @Override
            public void onClick(VaccineGroup vaccineGroup, VaccineWrapper vaccine) {
                ArrayList<VaccineWrapper> vaccineWrappers = new ArrayList<>();
                vaccineWrappers.add(vaccine);
                addVaccinationDialogFragment(vaccineWrappers, vaccineGroup);
            }
        });
        curGroup.setOnVaccineUndoClickListener(new VaccineGroup.OnVaccineUndoClickListener() {
            @Override
            public void onUndoClick(VaccineGroup vaccineGroup, VaccineWrapper vaccine) {
                addVaccineUndoDialogFragment(vaccineGroup, vaccine);
            }
        });

        LinearLayout parent;
        if (canvasId == -1) {
            Random r = new Random();
            canvasId = r.nextInt(4232 - 213) + 213;
            parent = new LinearLayout(this);
            parent.setId(canvasId);
            vaccineGroupCanvasLL.addView(parent);
        } else {
            parent = findViewById(canvasId);
            parent.removeAllViews();
        }
        parent.addView(curGroup);
        curGroup.setTag(R.id.vaccine_group_vaccine_data, vaccineGroupData);
        curGroup.setTag(R.id.vaccine_group_parent_id, String.valueOf(canvasId));
        vaccineGroups.add(curGroup);
    }

    private void addVaccineUndoDialogFragment(VaccineGroup vaccineGroup, VaccineWrapper vaccineWrapper) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        vaccineGroup.setModalOpen(true);

        UndoVaccinationDialogFragment undoVaccinationDialogFragment = UndoVaccinationDialogFragment.newInstance(vaccineWrapper);
        undoVaccinationDialogFragment.show(ft, DIALOG_TAG);
    }

    private void addServiceUndoDialogFragment(ServiceGroup serviceGroup, ServiceWrapper serviceWrapper) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        serviceGroup.setModalOpen(true);

        UndoServiceDialogFragment undoServiceDialogFragment = UndoServiceDialogFragment.newInstance(serviceWrapper);
        undoServiceDialogFragment.show(ft, DIALOG_TAG);
    }

    public void launchDetailActivity(Context fromContext, CommonPersonObjectClient childDetails) {
        Intent intent = new Intent(fromContext, DetailActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable(EXTRA_CHILD_DETAILS, childDetails);
        intent.putExtras(bundle);

        fromContext.startActivity(intent);
    }

    @Override
    public void onVaccinateToday(ArrayList<VaccineWrapper> tags, View v) {
        if (tags != null && !tags.isEmpty()) {
            View view = getLastOpenedView();
            saveVaccine(tags, view);
        }
    }

    @Override
    public void onVaccinateEarlier(ArrayList<VaccineWrapper> tags, View v) {
        if (tags != null && !tags.isEmpty()) {
            View view = getLastOpenedView();
            saveVaccine(tags, view);
        }
    }

    @Override
    public void onUndoVaccination(VaccineWrapper tag, View v) {
        UndoVaccineTaskRunnable runnable = new UndoVaccineTaskRunnable( tag, v );
        AppExecutors executors = new AppExecutors();
        executors.diskIO().execute(runnable);
    }

    public void addVaccinationDialogFragment(ArrayList<VaccineWrapper> vaccineWrappers, VaccineGroup vaccineGroup) {

        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        Fragment prev = this.getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        vaccineGroup.setModalOpen(true);
        String dobString = Utils.getValue(childDetails.getColumnmaps(), "dob", false);
        Date dob = Calendar.getInstance().getTime();
        if (!TextUtils.isEmpty(dobString)) {
            DateTime dateTime = new DateTime(dobString);
            dob = dateTime.toDate();
        }

        List<Vaccine> vaccineList = ImmunizationLibrary.getInstance().vaccineRepository()
                .findByEntityId(childDetails.entityId());
        if (vaccineList == null) vaccineList = new ArrayList<>();

        VaccinationDialogFragment vaccinationDialogFragment = VaccinationDialogFragment.newInstance(dob, vaccineList, vaccineWrappers, true);
        vaccinationDialogFragment.show(ft, DIALOG_TAG);
    }

    public void addServiceDialogFragment(ServiceWrapper serviceWrapper, ServiceGroup serviceGroup) {

        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        Fragment prev = this.getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        serviceGroup.setModalOpen(true);
        String dobString = Utils.getValue(childDetails.getColumnmaps(), "dob", false);
        DateTime dob = DateTime.now();
        if (!TextUtils.isEmpty(dobString)) {
            dob = new DateTime(dobString);
        }

        List<ServiceRecord> serviceRecordList = ImmunizationLibrary.getInstance().recurringServiceRecordRepository()
                .findByEntityId(childDetails.entityId());

        ServiceDialogFragment serviceDialogFragment = ServiceDialogFragment.newInstance(dob, serviceRecordList, serviceWrapper, true);
        serviceDialogFragment.show(ft, DIALOG_TAG);
    }

    private void saveVaccine(ArrayList<VaccineWrapper> tags, final View view) {
        if (tags.isEmpty()) {
            return;
        }

        VaccineRepository vaccineRepository = ImmunizationLibrary.getInstance().vaccineRepository();

        VaccineWrapper[] arrayTags = tags.toArray(new VaccineWrapper[tags.size()]);

        SaveVaccineTaskCallable callable = new SaveVaccineTaskCallable(arrayTags, vaccineRepository);
        SaveVaccineTaskCallbackInteractorCallback callback = new SaveVaccineTaskCallbackInteractorCallback(view);
        genericInteractor.execute(callable, callback);

    }

    private void saveVaccine(VaccineRepository vaccineRepository, VaccineWrapper tag) {
        if (tag.getUpdatedVaccineDate() == null) {
            return;
        }


        Vaccine vaccine = new Vaccine();
        if (tag.getDbKey() != null) {
            vaccine = vaccineRepository.find(tag.getDbKey());
        }
        vaccine.setBaseEntityId(childDetails.entityId());
        vaccine.setName(tag.getName());
        vaccine.setDate(tag.getUpdatedVaccineDate().toDate());
        vaccine.setAnmId(ImmunizationLibrary.getInstance().context().allSharedPreferences().fetchRegisteredANM());

        String lastChar = vaccine.getName().substring(vaccine.getName().length() - 1);
        if (StringUtils.isNumeric(lastChar)) {
            vaccine.setCalculation(Integer.valueOf(lastChar));
        } else {
            vaccine.setCalculation(-1);
        }

        vaccine.setTeam("testTeam");
        vaccine.setTeamId("testTeamId");
        vaccine.setChildLocationId("testChildLocationId");

        Vaccine existingVaccine = vaccineRepository.findByBaseEntityIdAndVaccineName(vaccine.getBaseEntityId(), vaccine.getName());
        if (existingVaccine != null) {
            vaccine.setId(existingVaccine.getId());
        }

        vaccineRepository.add(vaccine);
        tag.setDbKey(vaccine.getId());
    }

    private void updateVaccineGroupViews(View view, final ArrayList<VaccineWrapper> wrappers, List<Vaccine> vaccineList) {
        updateVaccineGroupViews(view, wrappers, vaccineList, false);
    }

    private void updateVaccineGroupViews(View view, final ArrayList<VaccineWrapper> wrappers, final List<Vaccine> vaccineList, final boolean undo) {
        if (view == null || !(view instanceof VaccineGroup)) {
            return;
        }
        final VaccineGroup vaccineGroup = (VaccineGroup) view;
        vaccineGroup.setModalOpen(false);

        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (undo) {
                vaccineGroup.setVaccineList(vaccineList);
                vaccineGroup.updateWrapperStatus(wrappers, "child");
            }
            vaccineGroup.updateViews(wrappers);

        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (undo) {
                        vaccineGroup.setVaccineList(vaccineList);
                        vaccineGroup.updateWrapperStatus(wrappers, "child");
                    }
                    vaccineGroup.updateViews(wrappers);
                }
            });
        }
    }

    public void startServices() {
        Intent vaccineIntent = new Intent(this, VaccineIntentService.class);
        startService(vaccineIntent);

        Intent serviceIntent = new Intent(this, RecurringIntentService.class);
        startService(serviceIntent);

    }

    private String constructChildName() {
        String firstName = Utils.getValue(childDetails.getColumnmaps(), "first_name", true);
        String lastName = Utils.getValue(childDetails.getColumnmaps(), "last_name", true);
        return getName(firstName, lastName).trim();
    }

    private VaccineGroup getLastOpenedView() {
        if (vaccineGroups == null) {
            return null;
        }

        for (VaccineGroup vaccineGroup : vaccineGroups) {
            if (vaccineGroup.isModalOpen()) {
                return vaccineGroup;
            }
        }

        return null;
    }

    private void updateVaccineGroupsUsingAlerts(List<String> affectedVaccines, List<Vaccine> vaccineList, List<Alert> alerts) {
        if (affectedVaccines != null && vaccineList != null) {
            // Update all other affected vaccine groups
            HashMap<VaccineGroup, ArrayList<VaccineWrapper>> affectedGroups = new HashMap<>();
            for (String curAffectedVaccineName : affectedVaccines) {
                boolean viewFound = false;
                // Check what group it is in
                for (VaccineGroup curGroup : vaccineGroups) {
                    ArrayList<VaccineWrapper> groupWrappers = curGroup.getAllVaccineWrappers();
                    if (groupWrappers == null) groupWrappers = new ArrayList<>();
                    for (VaccineWrapper curWrapper : groupWrappers) {
                        String curWrapperName = curWrapper.getName();

                        // Check if current wrapper is one of the combined vaccines
                        if (ImmunizationLibrary.getInstance().COMBINED_VACCINES.contains(curWrapperName)) {
                            // Check if any of the sister vaccines is currAffectedVaccineName
                            String[] allSisters = ImmunizationLibrary.getInstance().COMBINED_VACCINES_MAP.get(curWrapperName).split(" / ");
                            for (int i = 0; i < allSisters.length; i++) {
                                if (allSisters[i].replace(" ", "").equalsIgnoreCase(curAffectedVaccineName.replace(" ", ""))) {
                                    curWrapperName = allSisters[i];
                                    break;
                                }
                            }
                        }

                        if (curWrapperName.replace(" ", "").toLowerCase()
                                .contains(curAffectedVaccineName.replace(" ", "").toLowerCase())) {
                            if (!affectedGroups.containsKey(curGroup)) {
                                affectedGroups.put(curGroup, new ArrayList<VaccineWrapper>());
                            }

                            affectedGroups.get(curGroup).add(curWrapper);
                            viewFound = true;
                        }

                        if (viewFound) break;
                    }

                    if (viewFound) break;
                }
            }

            for (VaccineGroup curGroup : affectedGroups.keySet()) {
                try {
                    vaccineGroups.remove(curGroup);
                    addVaccineGroup(Integer.valueOf((String) curGroup.getTag(R.id.vaccine_group_parent_id)),
                            //TODO if error use immediately below
                            // (org.smartregister.immunization.domain.jsonmapping.VaccineGroup) curGroup.getTag(R.id.vaccine_group_vaccine_data),
                            curGroup.getVaccineData(),
                            vaccineList, alerts);
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }

    //Recurring Service
    @Override
    public void onGiveToday(ServiceWrapper tag, View v) {
        if (tag != null) {
            View view = RecurringServiceUtils.getLastOpenedServiceView(serviceGroups);
            saveService(tag, view);
        }
    }

    @Override
    public void onGiveEarlier(ServiceWrapper tag, View v) {
        if (tag != null) {
            View view = RecurringServiceUtils.getLastOpenedServiceView(serviceGroups);
            saveService(tag, view);
        }
    }

    @Override
    public void onUndoService(ServiceWrapper tag, View v) {
        UndoServiceTaskCallable callable = new UndoServiceTaskCallable(tag);
        UndoServiceTaskCallableInteractorCallback callback = new UndoServiceTaskCallableInteractorCallback(tag);
        genericInteractor.execute(callable, callback);
    }

    public void saveService(ServiceWrapper tag, final View view) {
        if (tag == null) {
            return;
        }

        ServiceWrapper[] arrayTags = {tag};
        String providerId = ImmunizationLibrary.getInstance().context().allSharedPreferences().fetchRegisteredANM();

        SaveServiceTaskCallable callable = new SaveServiceTaskCallable(arrayTags, providerId);
        SaveServiceTaskCallableInteractorCallback callback = new SaveServiceTaskCallableInteractorCallback(view);

        genericInteractor.execute(callable, callback);
    }

    public class SaveVaccineTaskCallbackInteractorCallback implements CallableInteractorCallBack<Triple<Pair<ArrayList<VaccineWrapper>, List<Vaccine>>, List<String>, List<Alert>>>{
        private View view;

        SaveVaccineTaskCallbackInteractorCallback(View view){
            this.view = view;
        }


        @Override
        public void onResult(Triple<Pair<ArrayList<VaccineWrapper>, List<Vaccine>>, List<String>, List<Alert>> triple) {
            Pair<ArrayList<VaccineWrapper>, List<Vaccine>> pair = triple.getLeft();
            List<String> affectedVaccines = triple.getMiddle();
            List<Vaccine> vaccineList = pair.second;
            ArrayList<VaccineWrapper> vaccineWrappers = pair.first;
            List<Alert> alertList = triple.getRight();

            updateVaccineGroupViews(view, vaccineWrappers, vaccineList);

            updateVaccineGroupsUsingAlerts(affectedVaccines, vaccineList, alertList);
        }

        @Override
        public void onError(Exception ex) {
            Timber.e(ex);
        }
    }

    public class SaveVaccineTaskCallable implements Callable<Triple<Pair<ArrayList<VaccineWrapper>, List<Vaccine>>, List<String>, List<Alert>>> {

        private VaccineRepository vaccineRepository;
        private AlertService alertService;
        private List<String> affectedVaccines;
        private List<Vaccine> vaccineList;
        private List<Alert> alertList;
        private VaccineWrapper[] vaccineWrappers;

        SaveVaccineTaskCallable(VaccineWrapper[] vaccineWrappers, VaccineRepository vaccineRepository){
            this.vaccineWrappers = vaccineWrappers;
            this.vaccineRepository = vaccineRepository;
            alertService = ImmunizationLibrary.getInstance().context().alertService();
        }

        @Override
        public Triple<Pair<ArrayList<VaccineWrapper>, List<Vaccine>>, List<String>, List<Alert>> call() {
            ArrayList<VaccineWrapper> list = new ArrayList<>();
            if (vaccineRepository != null) {
                for (VaccineWrapper tag : vaccineWrappers) {
                    saveVaccine(vaccineRepository, tag);
                    list.add(tag);
                }
            }

            String dobString = Utils.getValue(childDetails.getColumnmaps(), "dob", false);
            if (!TextUtils.isEmpty(dobString)) {
                DateTime dateTime = new DateTime(dobString);
                affectedVaccines = VaccineSchedule.updateOfflineAlertsAndReturnAffectedVaccineNames(childDetails.entityId(), dateTime, IMConstants.VACCINE_TYPE.CHILD);
            }
            vaccineList = vaccineRepository.findByEntityId(childDetails.entityId());
            alertList = alertService.findByEntityIdAndAlertNames(childDetails.entityId(),
                    VaccinateActionUtils.allAlertNames(IMConstants.VACCINE_TYPE.CHILD));

            Pair<ArrayList<VaccineWrapper>, List<Vaccine>> pair = new Pair<>(list, vaccineList);

            return Triple.of(pair, affectedVaccines, alertList);
        }
    }

    private class UpdateViewTaskCallable implements Callable<Map<String, NamedObject<?>>>{

        private VaccineRepository vaccineRepository;
        private RecurringServiceTypeRepository recurringServiceTypeRepository;
        private RecurringServiceRecordRepository recurringServiceRecordRepository;
        private AlertService alertService;

        public void setVaccineRepository(VaccineRepository vaccineRepository) {
            this.vaccineRepository = vaccineRepository;
        }

        public void setRecurringServiceTypeRepository(RecurringServiceTypeRepository recurringServiceTypeRepository) {
            this.recurringServiceTypeRepository = recurringServiceTypeRepository;
        }

        public void setRecurringServiceRecordRepository(RecurringServiceRecordRepository recurringServiceRecordRepository) {
            this.recurringServiceRecordRepository = recurringServiceRecordRepository;
        }

        public void setAlertService(AlertService alertService) {
            this.alertService = alertService;
        }
        @Override
        public Map<String, NamedObject<?>> call() {
            String dobString = Utils.getValue(childDetails.getColumnmaps(), IMDatabaseConstants.Client.DOB, false);
            if (!TextUtils.isEmpty(dobString)) {
                DateTime dateTime = new DateTime(dobString);
                VaccineSchedule.updateOfflineAlertsOnly(childDetails.entityId(), dateTime, IMConstants.VACCINE_TYPE.CHILD);
                ServiceSchedule.updateOfflineAlerts(childDetails.entityId(), dateTime);
            }

            List<Vaccine> vaccineList = new ArrayList<>();

            Map<String, List<ServiceType>> serviceTypeMap = new LinkedHashMap<>();
            List<ServiceRecord> serviceRecords = new ArrayList<>();

            List<Alert> alertList = new ArrayList<>();
            if (vaccineRepository != null) {
                vaccineList = vaccineRepository.findByEntityId(childDetails.entityId());

            }

            if (recurringServiceRecordRepository != null) {
                serviceRecords = recurringServiceRecordRepository.findByEntityId(childDetails.entityId());
            }

            if (recurringServiceTypeRepository != null) {
                List<ServiceType> serviceTypes = recurringServiceTypeRepository.fetchAll();
                for (ServiceType serviceType : serviceTypes) {
                    String type = serviceType.getType();
                    List<ServiceType> serviceTypeList = serviceTypeMap.get(type);
                    if (serviceTypeList == null) {
                        serviceTypeList = new ArrayList<>();
                    }
                    serviceTypeList.add(serviceType);
                    serviceTypeMap.put(type, serviceTypeList);
                }
            }

            if (alertService != null) {
                alertList = alertService.findByEntityId(childDetails.entityId());
            }

            Map<String, NamedObject<?>> map = new HashMap<>();

            NamedObject<List<Vaccine>> vaccineNamedObject = new NamedObject<>(Vaccine.class.getName(), vaccineList);
            map.put(vaccineNamedObject.name, vaccineNamedObject);

            NamedObject<Map<String, List<ServiceType>>> serviceTypeNamedObject = new NamedObject<>(ServiceType.class.getName(), serviceTypeMap);
            map.put(serviceTypeNamedObject.name, serviceTypeNamedObject);

            NamedObject<List<ServiceRecord>> serviceRecordNamedObject = new NamedObject<>(ServiceRecord.class.getName(), serviceRecords);
            map.put(serviceRecordNamedObject.name, serviceRecordNamedObject);

            NamedObject<List<Alert>> alertsNamedObject = new NamedObject<>(Alert.class.getName(), alertList);
            map.put(alertsNamedObject.name, alertsNamedObject);

            return map;
        }
    }

    private class UpdateViewTaskCallableInteractorCallback implements CallableInteractorCallBack<Map<String, NamedObject<?>>>{

        @Override
        public void onResult(Map<String, NamedObject<?>> map) {
            List<Vaccine> vaccineList = new ArrayList<>();

            Map<String, List<ServiceType>> serviceTypeMap = new LinkedHashMap<>();
            List<ServiceRecord> serviceRecords = new ArrayList<>();

            List<Alert> alertList = new ArrayList<>();

            if (map.containsKey(Vaccine.class.getName())) {
                NamedObject<?> namedObject = map.get(Vaccine.class.getName());
                if (namedObject != null) {
                    vaccineList = (List<Vaccine>) namedObject.object;
                }

            }

            if (map.containsKey(ServiceType.class.getName())) {
                NamedObject<?> namedObject = map.get(ServiceType.class.getName());
                if (namedObject != null) {
                    serviceTypeMap = (Map<String, List<ServiceType>>) namedObject.object;
                }

            }

            if (map.containsKey(ServiceRecord.class.getName())) {
                NamedObject<?> namedObject = map.get(ServiceRecord.class.getName());
                if (namedObject != null) {
                    serviceRecords = (List<ServiceRecord>) namedObject.object;
                }

            }

            if (map.containsKey(Alert.class.getName())) {
                NamedObject<?> namedObject = map.get(Alert.class.getName());
                if (namedObject != null) {
                    alertList = (List<Alert>) namedObject.object;
                }

            }

            updateServiceViews(serviceTypeMap, serviceRecords, alertList);
            updateVaccinationViews(vaccineList, alertList);
        }

        @Override
        public void onError(Exception ex) {
            Timber.e(ex);
        }
    }

    private class UndoVaccineTaskRunnable implements Runnable {
        private final VaccineRepository vaccineRepository;
        private final AlertService alertService;
        private VaccineWrapper tag;
        private View v;
        private List<Vaccine> vaccineList;
        private List<Alert> alertList;
        private List<String> affectedVaccines;

        public UndoVaccineTaskRunnable(VaccineWrapper tag, View v){
            this.tag = tag;
            this.v = v;
            vaccineRepository = ImmunizationLibrary.getInstance().vaccineRepository();
            alertService = ImmunizationLibrary.getInstance().context().alertService();

        }

        @Override
        public void run() {
            if (tag != null) {

                if (tag.getDbKey() != null) {
                    Long dbKey = tag.getDbKey();
                    vaccineRepository.deleteVaccine(dbKey);
                    String dobString = Utils.getValue(childDetails.getColumnmaps(), "dob", false);
                    if (!TextUtils.isEmpty(dobString)) {
                        DateTime dateTime = new DateTime(dobString);
                        affectedVaccines = VaccineSchedule.updateOfflineAlertsAndReturnAffectedVaccineNames(childDetails.entityId(), dateTime, IMConstants.VACCINE_TYPE.CHILD);
                        vaccineList = vaccineRepository.findByEntityId(childDetails.entityId());
                        alertList = alertService.findByEntityIdAndAlertNames(childDetails.entityId(),
                                VaccinateActionUtils.allAlertNames("child"));
                    }
                }
            }
        }
    }

    private class NamedObject<T> {
        public final String name;
        public final T object;

        public NamedObject(String name, T object) {
            this.name = name;
            this.object = object;
        }
    }

    public class SaveServiceTaskCallable implements Callable<Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>>>{

        private String providerId;
        private ServiceWrapper[] arrayTags;

        SaveServiceTaskCallable(ServiceWrapper[] arrayTags, String providerId){
            this.arrayTags = arrayTags;
            this.providerId = providerId;
        }

        @Override
        public Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>> call() {

            ArrayList<ServiceWrapper> list = new ArrayList<>();

            for (ServiceWrapper tag : arrayTags) {
                RecurringServiceUtils.saveService(tag, childDetails.entityId(), providerId, null, null, null, null);
                list.add(tag);

                ServiceSchedule.updateOfflineAlerts(tag.getType(), childDetails.entityId(), Utils.dobToDateTime(childDetails));
            }

            RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance().recurringServiceRecordRepository();
            List<ServiceRecord> serviceRecordList = recurringServiceRecordRepository.findByEntityId(childDetails.entityId());

            RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance().recurringServiceTypeRepository();
            List<ServiceType> serviceTypes = recurringServiceTypeRepository.fetchAll();
            String[] alertArray = VaccinateActionUtils.allAlertNames(serviceTypes);

            AlertService alertService = ImmunizationLibrary.getInstance().context().alertService();
            List<Alert> alertList = alertService.findByEntityIdAndAlertNames(childDetails.entityId(), alertArray);

            return Triple.of(list, serviceRecordList, alertList);
        }
    }

    public class SaveServiceTaskCallableInteractorCallback implements CallableInteractorCallBack<Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>>>{

        private View view;

        SaveServiceTaskCallableInteractorCallback(View view){
            this.view = view;
        }

        @Override
        public void onResult(Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>> triple) {
            RecurringServiceUtils.updateServiceGroupViews(view, triple.getLeft(), triple.getMiddle(), triple.getRight());
        }

        @Override
        public void onError(Exception ex) {
            Timber.e(ex);
        }
    }

    public class UndoServiceTaskCallableInteractorCallback implements CallableInteractorCallBack<Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>>>{

        private ServiceWrapper tag;
        private View view;


        public UndoServiceTaskCallableInteractorCallback(ServiceWrapper tag) {
            this.tag = tag;
            this.view = RecurringServiceUtils.getLastOpenedServiceView(serviceGroups);
        }

        @Override
        public void onResult(Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>> triple) {
            tag.setUpdatedVaccineDate(null, false);
            tag.setDbKey(null);

            RecurringServiceUtils.updateServiceGroupViews(view, triple.getLeft(), triple.getMiddle(), triple.getRight(), true);

        }

        @Override
        public void onError(Exception ex) {
            Timber.e(ex);
        }
    }
    public class UndoServiceTaskCallable implements Callable<Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>>>{

        private ServiceWrapper tag;

        UndoServiceTaskCallable(ServiceWrapper tag){
            this.tag = tag;
        }

        @Override
        public Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>> call() {
            ArrayList<ServiceWrapper> wrappers = new ArrayList<>();
            List<ServiceRecord> serviceRecordList = new ArrayList<>();
            List<Alert> alertList = new ArrayList<>();
            if (tag != null) {

                if (tag.getDbKey() != null) {
                    RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance().recurringServiceRecordRepository();
                    Long dbKey = tag.getDbKey();
                    recurringServiceRecordRepository.deleteServiceRecord(dbKey);

                    serviceRecordList = recurringServiceRecordRepository.findByEntityId(childDetails.entityId());

                    wrappers = new ArrayList<>();
                    wrappers.add(tag);

                    ServiceSchedule.updateOfflineAlerts(tag.getType(), childDetails.entityId(), Utils.dobToDateTime(childDetails));

                    RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance().recurringServiceTypeRepository();
                    List<ServiceType> serviceTypes = recurringServiceTypeRepository.fetchAll();
                    String[] alertArray = VaccinateActionUtils.allAlertNames(serviceTypes);

                    AlertService alertService = ImmunizationLibrary.getInstance().context().alertService();
                    alertList = alertService.findByEntityIdAndAlertNames(childDetails.entityId(), alertArray);
                }
            }
            return Triple.of(wrappers, serviceRecordList, alertList);
        }
    }
}
