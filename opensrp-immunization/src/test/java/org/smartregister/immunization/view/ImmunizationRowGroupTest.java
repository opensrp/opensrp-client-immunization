package org.smartregister.immunization.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.adapter.ImmunizationRowAdapter;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.view.mock.ImmunizationRowGroupTestActivity;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */
@PrepareForTest ({ImmunizationLibrary.class})
@Config (shadows = {FontTextViewShadow.class})
@PowerMockIgnore ({"javax.xml.*", "org.xml.sax.*", "org.w3c.dom.*", "org.springframework.context.*", "org.apache.log4j.*"})
public class ImmunizationRowGroupTest extends BaseUnitTest {

    private final String magicDate = "1985-07-24T00:00:00.000Z";
    private ImmunizationRowGroup view;
    @Mock
    private Context context;

    @Mock
    private org.smartregister.Context context_;

    private org.smartregister.immunization.domain.jsonmapping.VaccineGroup vaccineData;
    private CommonPersonObjectClient childdetails;
    private ArrayList<Vaccine> vaccinelist;
    private ArrayList<Alert> alertlist;
    private VaccineWrapper wrapper;
    private ArrayList<VaccineWrapper> wrappers;
    private ActivityController<ImmunizationRowGroupTestActivity> controller;
    private ImmunizationRowGroupTestActivity activity;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, ImmunizationRowGroupTestActivity.class);
        controller = Robolectric.buildActivity(ImmunizationRowGroupTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        controller.setup();
        view = activity.getInstance();

    }

    @Test
    public void assertGetAlertListNotNull() {
        view.setAlertList(new ArrayList<Alert>());
        Assert.assertNotNull(view.getAlertList());
    }

    @Test
    public void assertGetVaccineListNotNull() {
        view.setVaccineList(new ArrayList<Vaccine>());
        Assert.assertNotNull(view.getVaccineList());
    }

    @Test
    public void assertEqualsVaccineData() {
        setDataForTest(magicDate);
        Assert.assertEquals(view.getVaccineData(), vaccineData);
    }

    public void setDataForTest(String dateTimeString) {
        wrappers = new ArrayList<>();
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
        Type listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.VaccineGroup>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.VaccineGroup> vaccines = JsonFormUtils.gson
                .fromJson(VaccineData.vaccines, listType);

        vaccineData = vaccines.get(0);
        HashMap<String, String> detail = new HashMap<>();
        detail.put("dob", dateTimeString);
        childdetails = new CommonPersonObjectClient("1", detail, "NME");
        childdetails.setColumnmaps(detail);
        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.measles2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        Alert alert = new Alert("", "", "", AlertStatus.complete, "", "");
        vaccinelist = new ArrayList<>();
        vaccinelist.add(vaccine);
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.bcg2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinelist.add(vaccine);
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.opv1.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinelist.add(vaccine);
        alertlist = new ArrayList<>();
        alertlist.add(alert);
        view.setData(vaccineData, childdetails, vaccinelist, alertlist);
    }

    @Test
    public void assertEqualsChildDetails() {
        setDataForTest(magicDate);
        Assert.assertEquals(view.getChildDetails(), childdetails);
    }

    @Test
    public void assertEqualsVaccineList() {
        setDataForTest(magicDate);
        Assert.assertEquals(view.getVaccineList(), vaccinelist);
    }

    @Test
    public void assertEqualsAlertList() {
        setDataForTest(magicDate);
        Assert.assertEquals(view.getAlertList(), alertlist);
    }

    @Test
    public void assertUpdateViewsWithDifferentTimeWillSetVaccineAdapter() {
        Assert.assertEquals(view.getDueVaccines().size(), 0);

        setDataForTest(magicDate);
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
    public void assertOnClickCallsOnRecordAllClickListenerAndOnVaccineClickedListener() {

        setDataForTest(magicDate);
        view.updateViews();
        view.updateViews(wrappers);

        // Record All
        ImmunizationRowGroup.OnRecordAllClickListener onRecordAllClickListener = Mockito
                .mock(ImmunizationRowGroup.OnRecordAllClickListener.class);
        view.setOnRecordAllClickListener(onRecordAllClickListener);

        view.onClick(view.findViewById(R.id.record_all_tv));
        Mockito.verify(onRecordAllClickListener).onClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                org.mockito.ArgumentMatchers.any(ArrayList.class));

        // Vaccine Clicked
        ImmunizationRowGroup.OnVaccineClickedListener onVaccineClickListener = Mockito
                .mock(ImmunizationRowGroup.OnVaccineClickedListener.class);
        view.setOnVaccineClickedListener(onVaccineClickListener);

        ExpandableHeightGridView expandableHeightGridView = view.getVaccinesGV();
        ImmunizationRowAdapter adapter = view.getVaccineCardAdapter();

        ImmunizationRowCard immunizationRowCard = new ImmunizationRowCard(RuntimeEnvironment.application);
        wrapper = new VaccineWrapper();
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg);
        wrapper.setVaccineDate(DateTime.now());
        immunizationRowCard.setVaccineWrapper(wrapper);

        immunizationRowCard.setState(State.NOT_DUE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.DONE_CAN_BE_UNDONE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.DONE_CAN_NOT_BE_UNDONE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.DUE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.times(1)).onClick(view, wrapper);

        immunizationRowCard.setState(State.OVERDUE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineClickListener, Mockito.times(2)).onClick(view, wrapper);

        // UNDO never called since isEditmode is false and isStatusForMoreThanThreeMonths is true
        ImmunizationRowGroup.OnVaccineUndoClickListener onVaccineUndoClickListener = Mockito
                .mock(ImmunizationRowGroup.OnVaccineUndoClickListener.class);
        view.setOnVaccineUndoClickListener(onVaccineUndoClickListener);

        immunizationRowCard.setState(State.DUE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.OVERDUE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.NOT_DUE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.DONE_CAN_BE_UNDONE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never()).onUndoClick(view, wrapper);

        immunizationRowCard.setState(State.DONE_CAN_NOT_BE_UNDONE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never()).onUndoClick(view, wrapper);
    }

    @Test
    public void assertOnClickCallsUndoClickedListener() throws Exception {

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        setDataForTest(simpleDateFormat.format(date));
        view.updateViews();
        view.updateViews(wrappers);

        // Record All
        ImmunizationRowGroup.OnRecordAllClickListener onRecordAllClickListener = Mockito
                .mock(ImmunizationRowGroup.OnRecordAllClickListener.class);
        view.setOnRecordAllClickListener(onRecordAllClickListener);

        view.onClick(view.findViewById(R.id.record_all_tv));
        Mockito.verify(onRecordAllClickListener).onClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                org.mockito.ArgumentMatchers.any(ArrayList.class));

        // Vaccine Clicked
        ImmunizationRowGroup.OnVaccineClickedListener onVaccineClickListener = Mockito
                .mock(ImmunizationRowGroup.OnVaccineClickedListener.class);
        view.setOnVaccineClickedListener(onVaccineClickListener);

        ExpandableHeightGridView expandableHeightGridView = view.getVaccinesGV();
        ImmunizationRowAdapter adapter = view.getVaccineCardAdapter();

        ImmunizationRowCard immunizationRowCard = new ImmunizationRowCard(RuntimeEnvironment.application, true);
        wrapper = new VaccineWrapper();
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg);
        wrapper.setVaccineDate(DateTime.now());
        immunizationRowCard.setVaccineWrapper(wrapper);

        ImmunizationRowGroup.OnVaccineUndoClickListener onVaccineUndoClickListener = Mockito
                .mock(ImmunizationRowGroup.OnVaccineUndoClickListener.class);
        view.setOnVaccineUndoClickListener(onVaccineUndoClickListener);

        immunizationRowCard.setState(State.DUE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.OVERDUE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.NOT_DUE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ImmunizationRowGroup.class),
                        org.mockito.ArgumentMatchers.any(VaccineWrapper.class));

        immunizationRowCard.setState(State.DONE_CAN_BE_UNDONE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.times(1)).onUndoClick(view, wrapper);

        immunizationRowCard.setState(State.DONE_CAN_NOT_BE_UNDONE);
        expandableHeightGridView.performItemClick(immunizationRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onVaccineUndoClickListener, Mockito.times(2)).onUndoClick(view, wrapper);
    }

    @Test
    public void assertUpdateWrapperStatusCallsUpdateWrapperStatus() {
        setDataForTest(magicDate);
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


    //    @Test
    //    public void assertOnStateChangedCallsUpdateViews() throws Exception {
    //        setDataForTest(magicDate);
    //        view.onStateChanged(ImmunizationRowGroup.State.DONE_CAN_BE_UNDONE);
    //        //calls updateViews which sets the adapter, we can check the the adapter is not null
    //        Assert.assertNotNull(view.getAllVaccineWrappers());
    //    }

    @Test
    public void assertIsModalOpenReturnsBoolean() {
        view.setModalOpen(true);
        Assert.assertEquals(view.isModalOpen(), true);
        view.setModalOpen(false);
        Assert.assertEquals(view.isModalOpen(), false);
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
