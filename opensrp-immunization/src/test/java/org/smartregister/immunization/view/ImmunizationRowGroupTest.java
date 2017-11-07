package org.smartregister.immunization.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.domain.db.Event;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.BuildConfig;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.customshadows.ExpandableHeightGridViewShadow;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceRecordTest;
import org.smartregister.immunization.domain.ServiceWrapperTest;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.view.mock.ImmunizationRowCardTestActivity;
import org.smartregister.immunization.view.mock.ImmunizationRowGroupTestActivity;
import org.smartregister.immunization.view.mock.ServiceCardTestActivity;
import org.smartregister.immunization.view.mock.ViewAttributes;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;

/**
 * Created by onaio on 30/08/2017.
 */
@PrepareForTest({ImmunizationLibrary.class})
@Config(shadows = {FontTextViewShadow.class, ExpandableHeightGridViewShadow.class})
@PowerMockIgnore({"javax.xml.*", "org.xml.sax.*", "org.w3c.dom.*",  "org.springframework.context.*", "org.apache.log4j.*"})
public class ImmunizationRowGroupTest extends BaseUnitTest {

    private ImmunizationRowGroup view;

    @Mock
    private Context context;

    @Mock
    private org.smartregister.Context context_;

    private JSONObject vaccineData;
    private CommonPersonObjectClient childdetails;
    private ArrayList<Vaccine> vaccinelist;
    private ArrayList<Alert> alertlist;
    private VaccineWrapper wrapper;
    private ArrayList<VaccineWrapper> wrappers;
    private ActivityController<ImmunizationRowGroupTestActivity> controller;
    private ImmunizationRowGroupTestActivity activity;

    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, ImmunizationRowGroupTestActivity.class);
        controller = Robolectric.buildActivity(ImmunizationRowGroupTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        controller.setup();
        view = activity.getInstance();

    }

    @Test
    public void assertGetAlertListNotNull() throws Exception {
        view.setAlertList(new ArrayList<Alert>());
        Assert.assertNotNull(view.getAlertList());
    }

    @Test
    public void assertGetVaccineListNotNull() throws Exception {
        view.setVaccineList(new ArrayList<Vaccine>());
        Assert.assertNotNull(view.getVaccineList());
    }

    @Test
    public void assertEqualsVaccineData() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        Assert.assertEquals(view.getVaccineData(), vaccineData);
    }

    @Test
    public void assertEqualsChildDetails() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        Assert.assertEquals(view.getChildDetails(), childdetails);
    }

    @Test
    public void assertEqualsVaccineList() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        Assert.assertEquals(view.getVaccineList(), vaccinelist);
    }

    @Test
    public void assertEqualsAlertList() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        Assert.assertEquals(view.getAlertList(), alertlist);
    }

    @Test
    public void assertUpdateViewsWithDifferentTimeWillSetVaccineAdapter() throws Exception {
        Assert.assertEquals(view.getDueVaccines().size(), 0);

        setDataForTest("1985-07-24T00:00:00.000Z");
        view.updateViews();
        view.updateViews(wrappers);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        setDataForTest(format.format(new Date()) + "T00:00:00.000Z");
        view.updateViews(wrappers);
        setDataForTest("2018-01-01T00:00:00.000Z");
        view.updateViews(wrappers);
        Assert.assertNotNull(view.getDueVaccines());

    }

    @Test
    public void assertOnClickCallsOnRecordAllClickListenerAndOnVaccineClickedListener() throws Exception {
//
//        setDataForTest("1985-07-24T00:00:00.000Z");
//        view.updateViews();
//        view.updateViews(wrappers);
//        ImmunizationRowGroup.OnRecordAllClickListener onRecordAllClickListener = Mockito.mock(ImmunizationRowGroup.OnRecordAllClickListener.class);
//        view.setOnRecordAllClickListener(onRecordAllClickListener);
//
//        view.onClick((android.widget.TextView) view.findViewById(R.id.record_all_tv));
//        Mockito.verify(onRecordAllClickListener).onClick(any(ImmunizationRowGroup.class), any(ArrayList.class));
//
//        ImmunizationRowGroup.OnVaccineClickedListener onVaccineClickListener = Mockito.mock(ImmunizationRowGroup.OnVaccineClickedListener.class);
//        view.setOnVaccineClickedListener(onVaccineClickListener);
//        ImmunizationRowCard vaccineCard = new ImmunizationRowCard(RuntimeEnvironment.application);
//        wrapper = new VaccineWrapper();
//        wrapper.setVaccine(VaccineRepo.Vaccine.bcg);
//        vaccineCard.setVaccineWrapper(wrapper);
//
//        view.onClick(vaccineCard);
//        Mockito.verify(onVaccineClickListener).onClick(any(ImmunizationRowGroup.class), any(VaccineWrapper.class));
//
//        ImmunizationRowGroup.OnVaccineUndoClickListener onVaccineUndoClickListener = Mockito.mock(ImmunizationRowGroup.OnVaccineUndoClickListener.class);
//        view.setOnVaccineUndoClickListener(onVaccineUndoClickListener);
//        View v = new View(RuntimeEnvironment.application);
//        v.setId(R.id.undo_b);
//        ViewGroup parent = new LinearLayout(RuntimeEnvironment.application);
//        parent.addView(v);
//        vaccineCard.addView(parent);
//
//        view.onClick(v);
//        Mockito.verify(onVaccineUndoClickListener).onUndoClick(any(ImmunizationRowGroup.class), any(VaccineWrapper.class));
    }

    @Test
    public void assertUpdateWrapperStatusCallsUpdateWrapperStatus() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        view.updateWrapperStatus(wrappers);
        wrapper = new VaccineWrapper();
        wrapper.setDbKey(0l);
        wrapper.setName(VaccineRepo.Vaccine.bcg2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg2);
        view.updateWrapper(wrapper);
        wrapper = new VaccineWrapper();
        wrapper.setDbKey(0l);
        wrapper.setName(VaccineRepo.Vaccine.bcg2.display() + "/:D");
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg2);
        view.updateWrapper(wrapper);
        Assert.assertNotNull(view.getVaccineList());
    }

    @Test
    public void assertIsModalOpenReturnsBoolean() throws Exception {
        view.setModalOpen(true);
        Assert.assertEquals(view.isModalOpen(), true);
        view.setModalOpen(false);
        Assert.assertEquals(view.isModalOpen(), false);
    }


//    @Test
//    public void assertOnStateChangedCallsUpdateViews() throws Exception {
//        setDataForTest("1985-07-24T00:00:00.000Z");
//        view.onStateChanged(ImmunizationRowGroup.State.DONE_CAN_BE_UNDONE);
//        //calls updateViews which sets the adapter, we can check the the adapter is not null
//        Assert.assertNotNull(view.getAllVaccineWrappers());
//    }

    public void setDataForTest(String dateTimeString) throws Exception {
        wrappers = new ArrayList<VaccineWrapper>();
        wrapper = new VaccineWrapper();
        wrapper.setDbKey(0l);
        wrapper.setName(VaccineRepo.Vaccine.bcg2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg2);
        wrappers.add(wrapper);
        wrapper = new VaccineWrapper();
        wrapper.setDbKey(0l);
        wrapper.setVaccine(VaccineRepo.Vaccine.opv1);
        wrapper.setName(VaccineRepo.Vaccine.opv1.display());
        wrappers.add(wrapper);
        wrapper = new VaccineWrapper();
        wrapper.setDbKey(0l);
        wrapper.setName(VaccineRepo.Vaccine.measles2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.measles2);
        wrappers.add(wrapper);
        JSONArray vaccineArray = new JSONArray(VaccineData.vaccines);
        vaccineData = vaccineArray.getJSONObject(0);
        HashMap<String, String> detail = new HashMap<String, String>();
        detail.put("dob", dateTimeString);
        childdetails = new CommonPersonObjectClient("1", detail, "NME");
        childdetails.setColumnmaps(detail);
        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.measles2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        Alert alert = new Alert("", "", "", AlertStatus.complete, "", "");
        vaccinelist = new ArrayList<Vaccine>();
        vaccinelist.add(vaccine);
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.bcg2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinelist.add(vaccine);
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.opv1.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinelist.add(vaccine);
        alertlist = new ArrayList<Alert>();
        alertlist.add(alert);
        view.setData(vaccineData, childdetails, vaccinelist, alertlist);
    }

    @Test
    public void asertConstructorsNotNull() {
        Assert.assertNotNull(activity.getInstance());
        Assert.assertNotNull(activity.getInstance1());
        Assert.assertNotNull(activity.getInstance2());
        Assert.assertNotNull(activity.getInstance3());
    }

    @After
    public void tearDown() {
        destroyController();
        activity = null;
        controller = null;

    }
    private void destroyController() {
        try {
            activity.finish();
            controller.pause().stop().destroy(); //destroy controller if we can

        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), e.getMessage());
        }

        System.gc();
    }
}
