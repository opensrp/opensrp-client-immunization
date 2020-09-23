package org.smartregister.immunization.view;

import android.content.Intent;
import android.util.Log;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.adapter.ServiceRowAdapter;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceRecordTest;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceTypeTest;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.ServiceWrapperTest;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.view.mock.ServiceRowGroupTestActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onaio on 30/08/2017.
 */


public class ServiceRowGroupTest extends BaseUnitTest {

    private final String magicDate = "1985-07-24T00:00:00.000Z";
    private ServiceRowGroup view;

    private ActivityController<ServiceRowGroupTestActivity> controller;

    @InjectMocks
    private ServiceRowGroupTestActivity activity;

    @Mock
    private org.smartregister.Context context_;

    private ServiceWrapper wrapper;
    private String type = "SERVICETYPE";

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, ServiceRowGroupTestActivity.class);
        controller = Robolectric.buildActivity(ServiceRowGroupTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        controller.setup();
        view = activity.getInstance();
    }

    @Test
    public void assertConstructorsNotNull() {
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

    @Test
    public void assertOnClickCallsOnUndoServiceAllClickListenerAndOnServiceClickedListener() throws Exception {

        setDataForTest(magicDate);
        view.updateViews();

        ServiceRowGroup.OnServiceClickedListener onServiceClickedListener = Mockito
                .mock(ServiceRowGroup.OnServiceClickedListener.class);
        view.setOnServiceClickedListener(onServiceClickedListener);

        ExpandableHeightGridView expandableHeightGridView = view.getServicesGV();
        ServiceRowAdapter adapter = view.getServiceRowAdapter();

        ServiceRowCard serviceRowCard = new ServiceRowCard(RuntimeEnvironment.application);
        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
        serviceRowCard.setServiceWrapper(wrapper);

        serviceRowCard.setState(State.NOT_DUE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.DONE_CAN_BE_UNDONE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.DONE_CAN_NOT_BE_UNDONE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.DUE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.times(1)).onClick(view, wrapper);

        serviceRowCard.setState(State.OVERDUE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.times(2)).onClick(view, wrapper);

        // UNDO never called since isEditmode is false and isStatusForMoreThanThreeMonths is true
        ServiceRowGroup.OnServiceUndoClickListener onServiceUndoClickListener = Mockito
                .mock(ServiceRowGroup.OnServiceUndoClickListener.class);
        view.setOnServiceUndoClickListener(onServiceUndoClickListener);

        serviceRowCard.setState(State.DUE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.OVERDUE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.NOT_DUE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.DONE_CAN_BE_UNDONE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never()).onUndoClick(view, wrapper);

        serviceRowCard.setState(State.DONE_CAN_NOT_BE_UNDONE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never()).onUndoClick(view, wrapper);

    }

    public void setDataForTest(String dateTimeString) {
        ArrayList<ServiceWrapper> wrappers = new ArrayList<>();
        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);

        wrappers.add(wrapper);
        wrapper = new ServiceWrapper();

        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
        wrappers.add(wrapper);
        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);

        wrappers.add(wrapper);

        HashMap<String, String> detail = new HashMap<>();
        detail.put("dob", dateTimeString);
        CommonPersonObjectClient childdetails = new CommonPersonObjectClient("1", detail, "NME");
        childdetails.setColumnmaps(detail);
        Alert alert = new Alert("", "", "", AlertStatus.complete, "", "");

        ArrayList<Alert> alertlist = new ArrayList<>();
        alertlist.add(alert);
        Map<String, List<ServiceType>> serviceTypeMap = new HashMap<>();
        ServiceType serviceType = new ServiceType();
        serviceType.setId(0l);
        serviceType.setType(ServiceTypeTest.TYPE);
        serviceType.setName(ServiceTypeTest.NAME);
        serviceType.setServiceNameEntity(ServiceTypeTest.SERVICENAMEENTITY);
        serviceType.setServiceNameEntityId(ServiceTypeTest.SERVICENAMEENTITYID);
        serviceType.setDateEntity(ServiceTypeTest.DATEENTITY);
        serviceType.setDateEntityId(ServiceTypeTest.DATEENTITYID);
        serviceType.setUnits(ServiceTypeTest.UNITS);
        serviceType.setServiceLogic(ServiceTypeTest.SERVICELOGIC);
        serviceType.setPrerequisite(ServiceTypeTest.PREREQUISITE);
        serviceType.setPreOffset(ServiceTypeTest.PREOFFSET);
        serviceType.setExpiryOffset(ServiceTypeTest.EXPIRYOFFSET);
        serviceType.setMilestoneOffset(ServiceTypeTest.MILESTONEOFFSET);
        serviceType.setUpdatedAt(0l);
        ArrayList<ServiceType> serviceTypes = new ArrayList<>();
        serviceTypes.add(serviceType);
        serviceTypeMap.put(type, serviceTypes);
        ArrayList<ServiceRecord> servcServiceRecords = new ArrayList<>();
        ServiceRecord serviceRecord = new ServiceRecord(0l, ServiceRecordTest.BASEENTITYID,
                ServiceRecordTest.PROGRAMCLIENTID, 0l, ServiceRecordTest.VALUE, new Date(), ServiceRecordTest.ANMID,
                ServiceRecordTest.LOCATIONID, ServiceRecordTest.SYNCED, ServiceRecordTest.EVENTID,
                ServiceRecordTest.FORMSUBMISSIONID, 0l, new Date());
        serviceRecord.setDate(new Date());
        serviceRecord.setName(ServiceWrapperTest.DEFAULTNAME);
        servcServiceRecords.add(serviceRecord);
        view.setData(childdetails, serviceTypes, servcServiceRecords, alertlist);
    }

    @Test
    public void assertOnClickCallsOnUndoServiceAllClickListener() throws Exception {

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        setDataForTest(simpleDateFormat.format(date));
        view.updateViews();

        ServiceRowGroup.OnServiceClickedListener onServiceClickedListener = Mockito
                .mock(ServiceRowGroup.OnServiceClickedListener.class);
        view.setOnServiceClickedListener(onServiceClickedListener);

        ExpandableHeightGridView expandableHeightGridView = view.getServicesGV();
        ServiceRowAdapter adapter = view.getServiceRowAdapter();

        ServiceRowCard serviceRowCard = new ServiceRowCard(RuntimeEnvironment.application, true);
        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
        serviceRowCard.setServiceWrapper(wrapper);

        // UNDO
        ServiceRowGroup.OnServiceUndoClickListener onServiceUndoClickListener = Mockito
                .mock(ServiceRowGroup.OnServiceUndoClickListener.class);
        view.setOnServiceUndoClickListener(onServiceUndoClickListener);

        serviceRowCard.setState(State.DUE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.OVERDUE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.NOT_DUE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceRowGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceRowCard.setState(State.DONE_CAN_BE_UNDONE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.times(1)).onUndoClick(view, wrapper);

        serviceRowCard.setState(State.DONE_CAN_NOT_BE_UNDONE);
        expandableHeightGridView.performItemClick(serviceRowCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.times(2)).onUndoClick(view, wrapper);

    }

    @Test
    public void assertUpdateWrapperStatusCallsUpdateWrapperStatus() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");

        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
        view.updateWrapperStatus(wrapper);
        view.updateWrapper(wrapper);

        Assert.assertNotNull(view.getServiceRecordList());
    }

    @Test
    public void assertIsModalOpenReturnsBoolean() {
        view.setModalOpen(true);
        Assert.assertEquals(view.isModalOpen(), true);
        view.setModalOpen(false);
        Assert.assertEquals(view.isModalOpen(), false);
    }
}