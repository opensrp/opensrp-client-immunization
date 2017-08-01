package org.smartregister.immunization.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceSchedule;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineSchedule;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.listener.ServiceActionListener;
import org.smartregister.immunization.listener.VaccinationActionListener;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.sample.util.SampleUtil;
import org.smartregister.immunization.util.RecurringServiceUtils;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.view.ImmunizationRowGroup;
import org.smartregister.immunization.view.ServiceGroup;
import org.smartregister.immunization.view.VaccineGroup;
import org.smartregister.service.AlertService;
import org.smartregister.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements VaccinationActionListener, ServiceActionListener {
    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final String DIALOG_TAG = "DIALOG_TAAAGGG";

    private ArrayList<VaccineGroup> vaccineGroups;
    private ArrayList<ServiceGroup> serviceGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        updateViews();
    }

    private void updateViews() {

        VaccineRepository vaccineRepository = ImmunizationLibrary.getInstance().vaccineRepository();
        RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance().recurringServiceTypeRepository();
        RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance().recurringServiceRecordRepository();
        AlertService alertService = ImmunizationLibrary.getInstance().context().alertService();

        UpdateViewTask updateViewTask = new UpdateViewTask();
        updateViewTask.setVaccineRepository(vaccineRepository);
        updateViewTask.setRecurringServiceTypeRepository(recurringServiceTypeRepository);
        updateViewTask.setRecurringServiceRecordRepository(recurringServiceRecordRepository);
        updateViewTask.setAlertService(alertService);
        Utils.startAsyncTask(updateViewTask, null);
    }

    private void updateServiceViews(Map<String, List<ServiceType>> serviceTypeMap, List<ServiceRecord> serviceRecordList, List<Alert> alerts) {

        Map<String, List<ServiceType>> foundServiceTypeMap = new LinkedHashMap<>();
        if (serviceGroups == null) {
            for (String type : serviceTypeMap.keySet()) {
                if (foundServiceTypeMap.containsKey(type)) {
                    continue;
                }

                for (ServiceRecord serviceRecord : serviceRecordList) {
                    if (serviceRecord.getSyncStatus().equals(RecurringServiceTypeRepository.TYPE_Unsynced)) {
                        if (serviceRecord.getType().equals(type)) {
                            foundServiceTypeMap.put(type, serviceTypeMap.get(type));
                            break;
                        }
                    }
                }

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
            LinearLayout serviceGroupCanvasLL = (LinearLayout) findViewById(R.id.service_group_canvas_ll);

            ServiceGroup curGroup = new ServiceGroup(this);
            curGroup.setData(SampleUtil.dummyDetatils(), foundServiceTypeMap, serviceRecordList, alerts);
            curGroup.setOnServiceClickedListener(new ServiceGroup.OnServiceClickedListener() {
                @Override
                public void onClick(ServiceGroup serviceGroup, ServiceWrapper
                        serviceWrapper) {
                    SampleUtil.addServiceDialogFragment(MainActivity.this, DIALOG_TAG, serviceWrapper, serviceGroup);
                }
            });
            curGroup.setOnServiceUndoClickListener(new ServiceGroup.OnServiceUndoClickListener() {
                @Override
                public void onUndoClick(ServiceGroup serviceGroup, ServiceWrapper serviceWrapper) {
                    SampleUtil.addServiceUndoDialogFragment(MainActivity.this, DIALOG_TAG, serviceGroup, serviceWrapper);
                }
            });
            serviceGroupCanvasLL.addView(curGroup);
            serviceGroups.add(curGroup);
        }

    }

    private void updateVaccinationViews(List<Vaccine> vaccineList, List<Alert> alerts) {

        if (vaccineGroups == null) {
            vaccineGroups = new ArrayList<>();
            String supportedVaccinesString = VaccinatorUtils.getSupportedVaccines(this);

            try {
                JSONArray supportedVaccines = new JSONArray(supportedVaccinesString);

                for (int i = 0; i < supportedVaccines.length(); i++) {
                    vaccineGroups.add(SampleUtil.addVaccineGroup(MainActivity.this, DIALOG_TAG, -1, supportedVaccines.getJSONObject(i), vaccineList, alerts));
                }
            } catch (JSONException e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }
    }


    private void updateVaccineGroupViews(View view) {
        if (view == null || !(view instanceof ImmunizationRowGroup)) {
            return;
        }
        final ImmunizationRowGroup vaccineGroup = (ImmunizationRowGroup) view;

        if (Looper.myLooper() == Looper.getMainLooper()) {
            vaccineGroup.updateViews();
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    vaccineGroup.updateViews();
                }
            });
        }
    }

    private void updateVaccineGroupViews(View view, final ArrayList<VaccineWrapper> wrappers, final List<Vaccine> vaccineList, final boolean undo) {
        if (view == null || !(view instanceof ImmunizationRowGroup)) {
            return;
        }
        final ImmunizationRowGroup vaccineGroup = (ImmunizationRowGroup) view;
        vaccineGroup.setModalOpen(false);

        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (undo) {
                vaccineGroup.setVaccineList(vaccineList);
                vaccineGroup.updateWrapperStatus(wrappers);
            }
            vaccineGroup.updateViews(wrappers);

        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (undo) {
                        vaccineGroup.setVaccineList(vaccineList);
                        vaccineGroup.updateWrapperStatus(wrappers);
                    }
                    vaccineGroup.updateViews(wrappers);
                }
            });
        }
    }

    // Hooks for vaccine
    public void onVaccinateToday(ArrayList<VaccineWrapper> tags, View view) {
        if (tags != null && !tags.isEmpty()) {
            saveVaccine(tags, view);
            Utils.startAsyncTask(new UpdateOfflineAlertsTask(), null);
        }
    }

    @Override
    public void onVaccinateEarlier(ArrayList<VaccineWrapper> tags, View view) {
        if (tags != null && !tags.isEmpty()) {
            saveVaccine(tags, view);
            Utils.startAsyncTask(new UpdateOfflineAlertsTask(), null);
        }
    }

    @Override
    public void onUndoVaccination(VaccineWrapper tag, View view) {
        if (tag != null) {

            if (tag.getDbKey() != null) {
                final VaccineRepository vaccineRepository = ImmunizationLibrary.getInstance().vaccineRepository();
                Long dbKey = tag.getDbKey();
                vaccineRepository.deleteVaccine(dbKey);

                tag.setUpdatedVaccineDate(null, false);
                tag.setDbKey(null);


                List<Vaccine> vaccineList = vaccineRepository.findByEntityId(SampleUtil.ENTITY_ID);

                ArrayList<VaccineWrapper> wrappers = new ArrayList<>();
                wrappers.add(tag);
                updateVaccineGroupViews(view, wrappers, vaccineList, true);

                Utils.startAsyncTask(new UpdateOfflineAlertsTask(), null);
            }
        }
    }

    // Hooks for recurring services
    @Override
    public void onGiveToday(ServiceWrapper tag, View view) {
        if (tag != null) {
            saveService(tag, view);
        }
    }

    @Override
    public void onGiveEarlier(ServiceWrapper tag, View view) {
        if (tag != null) {
            saveService(tag, view);
        }
    }

    @Override
    public void onUndoService(ServiceWrapper tag, View view) {
        Utils.startAsyncTask(new UndoServiceTask(tag, view), null);
    }


    private void saveVaccine(List<VaccineWrapper> tags, final View view) {
        if (tags.isEmpty()) {
            return;
        } else if (tags.size() == 1) {
            saveVaccine(tags.get(0));
            updateVaccineGroupViews(view);
        } else {
            VaccineWrapper[] arrayTags = tags.toArray(new VaccineWrapper[tags.size()]);
            SaveVaccinesTask backgroundTask = new SaveVaccinesTask();
            backgroundTask.setView(view);
            backgroundTask.execute(arrayTags);
        }
    }


    private void saveVaccine(VaccineWrapper tag) {
        VaccineRepository vaccineRepository = ImmunizationLibrary.getInstance().vaccineRepository();

        Vaccine vaccine = new Vaccine();
        if (tag.getDbKey() != null) {
            vaccine = vaccineRepository.find(tag.getDbKey());
        }
        vaccine.setBaseEntityId(SampleUtil.ENTITY_ID);
        vaccine.setName(tag.getName());
        vaccine.setDate(tag.getUpdatedVaccineDate().toDate());
        vaccine.setUpdatedAt(tag.getUpdatedVaccineDate().toDate().getTime());
        vaccine.setAnmId(ImmunizationLibrary.getInstance().context().allSharedPreferences().fetchRegisteredANM());

        String lastChar = vaccine.getName().substring(vaccine.getName().length() - 1);
        if (StringUtils.isNumeric(lastChar)) {
            vaccine.setCalculation(Integer.valueOf(lastChar));
        } else {
            vaccine.setCalculation(-1);
        }
        vaccineRepository.add(vaccine);
        tag.setDbKey(vaccine.getId());
    }

    public void saveService(ServiceWrapper tag, final View view) {
        if (tag == null) {
            return;
        }

        ServiceWrapper[] arrayTags = {tag};
        SaveServiceTask backgroundTask = new SaveServiceTask();

        backgroundTask.setView(view);
        Utils.startAsyncTask(backgroundTask, arrayTags);
    }

    //////////////////////////////////////////////////////////////////////////////
    //Async Tasks
    private class SaveVaccinesTask extends AsyncTask<VaccineWrapper, Void, Void> {

        private View view;

        public void setView(View view) {
            this.view = view;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            updateVaccineGroupViews(view);
        }

        @Override
        protected Void doInBackground(VaccineWrapper... vaccineWrappers) {
            for (VaccineWrapper tag : vaccineWrappers) {
                saveVaccine(tag);
            }
            return null;
        }

    }

    public class SaveServiceTask extends AsyncTask<ServiceWrapper, Void, Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>>> {

        private View view;

        public void setView(View view) {
            this.view = view;
        }

        @Override
        protected void onPostExecute(Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>> triple) {
            RecurringServiceUtils.updateServiceGroupViews(view, triple.getLeft(), triple.getMiddle(), triple.getRight());
        }

        @Override
        protected Triple<ArrayList<ServiceWrapper>, List<ServiceRecord>, List<Alert>> doInBackground(ServiceWrapper... params) {

            ArrayList<ServiceWrapper> list = new ArrayList<>();

            for (ServiceWrapper tag : params) {
                RecurringServiceUtils.saveService(tag, SampleUtil.ENTITY_ID, null, null);
                list.add(tag);

                ServiceSchedule.updateOfflineAlerts(tag.getType(), SampleUtil.ENTITY_ID, Utils.dobToDateTime(SampleUtil.dummyDetatils()));
            }

            RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance().recurringServiceRecordRepository();
            List<ServiceRecord> serviceRecordList = recurringServiceRecordRepository.findByEntityId(SampleUtil.ENTITY_ID);

            RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance().recurringServiceTypeRepository();
            List<ServiceType> serviceTypes = recurringServiceTypeRepository.fetchAll();
            String[] alertArray = VaccinateActionUtils.allAlertNames(serviceTypes);

            AlertService alertService = ImmunizationLibrary.getInstance().context().alertService();
            List<Alert> alertList = alertService.findByEntityIdAndAlertNames(SampleUtil.ENTITY_ID, alertArray);

            return Triple.of(list, serviceRecordList, alertList);

        }
    }

    private class UpdateOfflineAlertsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DateTime birthDateTime = Utils.dobToDateTime(SampleUtil.dummyDetatils());
            if (birthDateTime != null) {
                VaccineSchedule.updateOfflineAlerts(SampleUtil.ENTITY_ID, birthDateTime, "child");
            }
            return null;
        }
    }

    private class UpdateViewTask extends AsyncTask<Void, Void, Map<String, NamedObject<?>>> {

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


        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(Map<String, NamedObject<?>> map) {

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
        protected Map<String, NamedObject<?>> doInBackground(Void... voids) {
            String dobString = SampleUtil.DOB_STRING;
            if (!TextUtils.isEmpty(dobString)) {
                DateTime dateTime = new DateTime(dobString);
                VaccineSchedule.updateOfflineAlerts(SampleUtil.ENTITY_ID, dateTime, "child");
                ServiceSchedule.updateOfflineAlerts(SampleUtil.ENTITY_ID, dateTime);
            }

            List<Vaccine> vaccineList = new ArrayList<>();
            Map<String, List<ServiceType>> serviceTypeMap = new LinkedHashMap<>();
            List<ServiceRecord> serviceRecords = new ArrayList<>();

            List<Alert> alertList = new ArrayList<>();
            if (vaccineRepository != null) {
                vaccineList = vaccineRepository.findByEntityId(SampleUtil.ENTITY_ID);
            }

            if (recurringServiceRecordRepository != null) {
                serviceRecords = recurringServiceRecordRepository.findByEntityId(SampleUtil.ENTITY_ID);
            }

            if (recurringServiceTypeRepository != null) {
                List<String> types = recurringServiceTypeRepository.fetchTypes();
                for (String type : types) {
                    List<ServiceType> subTypes = recurringServiceTypeRepository.findByType(type);
                    serviceTypeMap.put(type, subTypes);
                }
            }

            if (alertService != null) {
                alertList = alertService.findByEntityId(SampleUtil.ENTITY_ID);
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

    private class UndoServiceTask extends AsyncTask<Void, Void, Void> {

        private View view;
        private ServiceWrapper tag;
        private List<ServiceRecord> serviceRecordList;
        private ArrayList<ServiceWrapper> wrappers;
        private List<Alert> alertList;

        public UndoServiceTask(ServiceWrapper tag, View view) {
            this.tag = tag;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (tag != null) {

                if (tag.getDbKey() != null) {
                    RecurringServiceRecordRepository recurringServiceRecordRepository = ImmunizationLibrary.getInstance().recurringServiceRecordRepository();
                    Long dbKey = tag.getDbKey();
                    recurringServiceRecordRepository.deleteServiceRecord(dbKey);

                    serviceRecordList = recurringServiceRecordRepository.findByEntityId(SampleUtil.ENTITY_ID);

                    wrappers = new ArrayList<>();
                    wrappers.add(tag);

                    ServiceSchedule.updateOfflineAlerts(tag.getType(), SampleUtil.ENTITY_ID, Utils.dobToDateTime(SampleUtil.dummyDetatils()));

                    RecurringServiceTypeRepository recurringServiceTypeRepository = ImmunizationLibrary.getInstance().recurringServiceTypeRepository();
                    List<ServiceType> serviceTypes = recurringServiceTypeRepository.fetchAll();
                    String[] alertArray = VaccinateActionUtils.allAlertNames(serviceTypes);

                    alertList = ImmunizationLibrary.getInstance().context().alertService().findByEntityIdAndAlertNames(SampleUtil.ENTITY_ID, alertArray);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void params) {
            super.onPostExecute(params);

            tag.setUpdatedVaccineDate(null, false);
            tag.setDbKey(null);

            RecurringServiceUtils.updateServiceGroupViews(view, wrappers, serviceRecordList, alertList, true);
        }
    }


    ///////////////////////////////
    // Private classes
    private class NamedObject<T> {
        public final String name;
        public final T object;

        public NamedObject(String name, T object) {
            this.name = name;
            this.object = object;
        }
    }

}
