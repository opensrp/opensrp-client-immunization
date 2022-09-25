package org.smartregister.immunization.sample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.constants.Gender;
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
import org.smartregister.immunization.listener.ServiceActionListener;
import org.smartregister.immunization.listener.VaccinationActionListener;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.sample.tabfragments.ImmunizationFragment;
import org.smartregister.immunization.util.CallableInteractor;
import org.smartregister.immunization.util.CallableInteractorCallBack;
import org.smartregister.immunization.util.GenericInteractor;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.RecurringServiceUtils;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.view.ImmunizationRowGroup;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.DetailsRepository;
import org.smartregister.service.AlertService;
import org.smartregister.util.DateUtil;
import org.smartregister.util.Utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static org.smartregister.util.Utils.getName;

import timber.log.Timber;

/**
 * Created by raihan on 1/03/2017.
 */

public class DetailActivity extends AppCompatActivity implements VaccinationActionListener, ServiceActionListener {

    private static final String TAG = DetailActivity.class.getCanonicalName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static Gender gender;

    public static final String EXTRA_CHILD_DETAILS = "child_details";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private ImmunizationFragment immunizationFragment;

    public CommonPersonObjectClient getChildDetails() {
        return childDetails;
    }

    private ViewPagerAdapter adapter;

    public ViewPagerAdapter getViewPagerAdapter() {
        return adapter;
    }

    // Data
    private CommonPersonObjectClient childDetails;
    private Map<String, String> detailmaps;
    AllSharedPreferences allSharedPreferences;

    public DetailsRepository detailsRepository;

    public DetailsRepository getDetailsRepository() {
        return detailsRepository;
    }

    Map<String, String> details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            Serializable serializable = extras.getSerializable(EXTRA_CHILD_DETAILS);
            if (serializable != null && serializable instanceof CommonPersonObjectClient) {
                childDetails = (CommonPersonObjectClient) serializable;
            }
        }


        detailsRepository = detailsRepository == null ? ImmunizationLibrary.getInstance().context().detailsRepository() : detailsRepository;
        details = detailsRepository.getAllDetailsForClient(childDetails.entityId());
        details.putAll(childDetails.getColumnmaps());

        setContentView(R.layout.detail_activity_simple_tabs);

        immunizationFragment = new ImmunizationFragment();
        immunizationFragment.setArguments(this.getIntent().getExtras());


        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        getSupportActionBar().
        initiallization();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    immunizationFragment.loadView();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        allSharedPreferences = Utils.getAllSharedPreferences();

    }

    private void initiallization() {

        detailsRepository = detailsRepository == null ? ImmunizationLibrary.getInstance().context().detailsRepository() : detailsRepository;
        detailmaps = detailsRepository.getAllDetailsForClient(childDetails.entityId());
        profileWidget();

    }


    @Override
    public void onBackPressed() {
        finish();
    }


    private void profileWidget() {

        findViewById(R.id.outOfCatchment).setVisibility(ImmunizationLibrary.getInstance().getProperties().isTrue(IMConstants.APP_PROPERTIES.NOVEL_OUT_OF_CATCHMENT) ? View.VISIBLE : View.GONE);
        TextView profilename = findViewById(R.id.name);
        TextView profileZeirID = findViewById(R.id.idforclient);
        TextView profileage = findViewById(R.id.ageforclient);
        String name = "";
        String childId = "";
        String formattedAge = "";
        if (isDataOk()) {
            name = Utils.getValue(childDetails.getColumnmaps(), "first_name", true)
                    + " " + Utils.getValue(childDetails.getColumnmaps(), "last_name", true);
            childId = Utils.getValue(childDetails.getColumnmaps(), "zeir_id", false);
            if (StringUtils.isNotBlank(childId)) {
                childId = childId.replace("-", "");
            }
            String dobString = Utils.getValue(childDetails.getColumnmaps(), "dob", false);
            if (!TextUtils.isEmpty(dobString)) {
                DateTime dateTime = new DateTime(dobString);
                Date dob = dateTime.toDate();
                long timeDiff = Calendar.getInstance().getTimeInMillis() - dob.getTime();
                if (timeDiff >= 0) {
                    formattedAge = DateUtil.getDuration(timeDiff);
                }
            }
        }


        profileage.setText(String.format("%s: %s", "Age", formattedAge));
        profileZeirID.setText(String.format("%s: %s", "ID", childId));
        profilename.setText(name);

    }


    private String updateActivityTitle() {
        String name = "";
        if (isDataOk()) {
            name = Utils.getValue(childDetails.getColumnmaps(), "first_name", true)
                    + " " + Utils.getValue(childDetails.getColumnmaps(), "last_name", true);
        }
        return String.format("%s's %s", name, "Health Details");
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(immunizationFragment, "Under Five History");
        viewPager.setAdapter(adapter);
    }

    private boolean isDataOk() {
        return childDetails != null && childDetails.getDetails() != null;
    }

    @Override
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


                List<Vaccine> vaccineList = vaccineRepository.findByEntityId(childDetails.entityId());

                ArrayList<VaccineWrapper> wrappers = new ArrayList<>();
                wrappers.add(tag);
                updateVaccineGroupViews(view, wrappers, vaccineList, true);

                Utils.startAsyncTask(new UpdateOfflineAlertsTask(), null);
            }
        }
    }

    private String constructChildName() {
        String firstName = Utils.getValue(childDetails.getColumnmaps(), "first_name", true);
        String lastName = Utils.getValue(childDetails.getColumnmaps(), "last_name", true);
        return getName(firstName, lastName).trim();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void saveVaccine(List<VaccineWrapper> tags, final View view) {
        if (tags.isEmpty()) {
            return;
        } else if (tags.size() == 1) {
            saveVaccine(tags.get(0));
            updateVaccineGroupViews(view);
        } else {
            VaccineWrapper[] arrayTags = tags.toArray(new VaccineWrapper[tags.size()]);
            Callable<Void> callable = ()->{
                for (VaccineWrapper tag:arrayTags){
                    saveVaccine(tag);
                }
                return null;
            };
            GenericInteractor interactor  = getGenericInteractor();
            SaveVaccineCallableInteractorCallback saveVaccineCallableInteractorCallback  = new SaveVaccineCallableInteractorCallback();
            saveVaccineCallableInteractorCallback.setView(view);
            interactor.execute(callable, saveVaccineCallableInteractorCallback);

        }
    }

    public GenericInteractor getGenericInteractor() {
        return new GenericInteractor();
    }

    private class SaveVaccineCallableInteractorCallback implements CallableInteractorCallBack<Void> {
        private View view;
        public void setView(View view){
            this.view = view;
        }

        @Override
        public void onResult(Void unused) {
            if ( view == null || !(view instanceof ImmunizationRowGroup) ) {
                return;
            }
            final ImmunizationRowGroup vaccineGroup = (ImmunizationRowGroup) view;
            Timber.e("before update views");
            vaccineGroup.updateViews();
            Timber.e("after update views");
        }

        @Override
        public void onError(Exception ex) {
            Timber.e(ex);
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

    private void saveVaccine(VaccineWrapper tag) {
        VaccineRepository vaccineRepository = ImmunizationLibrary.getInstance().vaccineRepository();

        Vaccine vaccine = new Vaccine();
        if (tag.getDbKey() != null) {
            vaccine = vaccineRepository.find(tag.getDbKey());
        }
        vaccine.setBaseEntityId(childDetails.entityId());
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


    private boolean insertVaccinesGivenAsOptions(JSONObject question) throws JSONException {
        VaccineRepository vaccineRepository = ImmunizationLibrary.getInstance().vaccineRepository();
        JSONObject omrsChoicesTemplate = question.getJSONObject("openmrs_choice_ids");
        JSONObject omrsChoices = new JSONObject();
        JSONArray choices = new JSONArray();
        List<Vaccine> vaccineList = vaccineRepository.findByEntityId(childDetails.entityId());

        boolean ok = false;
        if (vaccineList != null && vaccineList.size() > 0) {
            ok = true;
            for (int i = vaccineList.size() - 1; i >= 0; i--) {
                Vaccine curVaccine = vaccineList.get(i);
                String name = VaccinatorUtils.getVaccineDisplayName(this, curVaccine.getName())
                        + " (" + DATE_FORMAT.format(curVaccine.getDate()) + ")";
                choices.put(name);

                Iterator<String> vaccineGroupNames = omrsChoicesTemplate.keys();
                while (vaccineGroupNames.hasNext()) {
                    String curGroupName = vaccineGroupNames.next();
                    if (curVaccine.getName().toLowerCase().contains(curGroupName.toLowerCase())) {
                        omrsChoices.put(name, omrsChoicesTemplate.getString(curGroupName));
                        break;
                    }
                }
            }
        }

        question.put("values", choices);
        question.put("openmrs_choice_ids", omrsChoices);

        return ok;
    }

    @Override
    protected void onResume() {
        super.onResume();
        details = detailsRepository.getAllDetailsForClient(childDetails.entityId());
        //details.putAll(childDetails.getColumnmaps());
        //):( prrrr
        childDetails.getColumnmaps().putAll(details);
        updateActivityTitle();
        initiallization();
    }

    public static boolean check_if_date_three_months_older(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(cal.getTime());
        cal.add(Calendar.DATE, -90);
        Date dateBefore90Days = cal.getTime();
        return date.before(dateBefore90Days);
    }

    //Recurring Service
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

    public void saveService(ServiceWrapper tag, final View view) {
        if (tag == null) {
            return;
        }

        ServiceWrapper[] arrayTags = {tag};
        SaveServiceTask backgroundTask = new SaveServiceTask();

        backgroundTask.setView(view);
        Utils.startAsyncTask(backgroundTask, arrayTags);
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
                RecurringServiceUtils.saveService(tag, childDetails.entityId(), null, null, null, null, null);
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

    private class UpdateOfflineAlertsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            DateTime birthDateTime = Utils.dobToDateTime(childDetails);
            if (birthDateTime != null) {
                VaccineSchedule.updateOfflineAlertsOnly(childDetails.entityId(), birthDateTime, IMConstants.VACCINE_TYPE.CHILD);
            }
            return null;
        }
    }

}
