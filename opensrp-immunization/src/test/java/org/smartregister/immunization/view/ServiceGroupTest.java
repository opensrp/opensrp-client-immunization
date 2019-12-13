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
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.adapter.ServiceCardAdapter;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceRecordTest;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceTypeTest;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.ServiceWrapperTest;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.view.mock.ServiceGroupTestActivity;
import org.smartregister.util.AppProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceGroupTest extends BaseUnitTest {

    private final String magicDate = "1985-07-24T00:00:00.000Z";
    private final String type = "SERVICETYPE";
    private ServiceGroup view;
    private ActivityController<ServiceGroupTestActivity> controller;
    @InjectMocks
    private ServiceGroupTestActivity activity;
    @Mock
    private org.smartregister.Context context_;
    private ArrayList<ServiceWrapper> wrappers;
    private ServiceWrapper wrapper;

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private AppProperties properties;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, ServiceGroupTestActivity.class);
        controller = Robolectric.buildActivity(ServiceGroupTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        controller.setup();
        view = activity.getInstance();
        Mockito.doReturn(properties).when(immunizationLibrary).getProperties();
        view.setImmunizationLibraryInstance(immunizationLibrary);
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
    public void verifyOnClickCallsOnUndoServiceAllClickListenerAndOnServiceClickedListener() throws Exception {

        setDataForTest(magicDate);
        view.updateViews();

        ServiceGroup.OnServiceClickedListener onServiceClickedListener = Mockito
                .mock(ServiceGroup.OnServiceClickedListener.class);
        view.setOnServiceClickedListener(onServiceClickedListener);

        ExpandableHeightGridView expandableHeightGridView = view.getServicesGV();
        ServiceCardAdapter adapter = view.getServiceCardAdapter();

        ServiceCard serviceCard = new ServiceCard(RuntimeEnvironment.application);
        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
        serviceCard.setServiceWrapper(wrapper);

        serviceCard.setState(State.NOT_DUE);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ServiceGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceCard.setState(State.DONE_CAN_BE_UNDONE);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ServiceGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceCard.setState(State.DONE_CAN_NOT_BE_UNDONE);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ServiceGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.never())
                .onClick(org.mockito.ArgumentMatchers.any(ServiceGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceCard.setState(State.DUE);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.times(1)).onClick(view, wrapper);

        serviceCard.setState(State.OVERDUE);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceClickedListener, Mockito.times(2)).onClick(view, wrapper);

        // UNDO
        ServiceGroup.OnServiceUndoClickListener onServiceUndoClickListener = Mockito
                .mock(ServiceGroup.OnServiceUndoClickListener.class);
        view.setOnServiceUndoClickListener(onServiceUndoClickListener);

        serviceCard.setState(State.DONE_CAN_NOT_BE_UNDONE);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceCard.setState(State.DUE);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceCard.setState(State.OVERDUE);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceCard.setState(State.EXPIRED);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceCard.setState(State.NOT_DUE);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.never())
                .onUndoClick(org.mockito.ArgumentMatchers.any(ServiceGroup.class),
                        org.mockito.ArgumentMatchers.any(ServiceWrapper.class));

        serviceCard.setState(State.DONE_CAN_BE_UNDONE);
        expandableHeightGridView.performItemClick(serviceCard, 0, adapter.getItemId(0));
        Mockito.verify(onServiceUndoClickListener, Mockito.times(1)).onUndoClick(view, wrapper);
    }

    public void setDataForTest(String dateTimeString) {
        wrappers = new ArrayList<>();
        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);

        wrappers.add(wrapper);
        wrapper = new ServiceWrapper();

        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
        wrappers.add(wrapper);
        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);

        wrappers.add(wrapper);

        HashMap<String, String> detail = new HashMap<String, String>();
        detail.put("dob", dateTimeString);
        CommonPersonObjectClient childdetails = new CommonPersonObjectClient("1", detail, "NME");
        childdetails.setColumnmaps(detail);

        Alert alert = new Alert("", "", "", AlertStatus.complete, "", "");

        List<Alert> alertlist = new ArrayList<>();
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
        List<ServiceRecord> servcServiceRecords = new ArrayList<>();
        ServiceRecord serviceRecord = new ServiceRecord(0l, ServiceRecordTest.BASEENTITYID,
                ServiceRecordTest.PROGRAMCLIENTID, 0l, ServiceRecordTest.VALUE, new Date(), ServiceRecordTest.ANMID,
                ServiceRecordTest.LOCATIONID, ServiceRecordTest.SYNCED, ServiceRecordTest.EVENTID,
                ServiceRecordTest.FORMSUBMISSIONID, 0l, new Date());
        serviceRecord.setDate(new Date());
        serviceRecord.setName(ServiceWrapperTest.DEFAULTNAME);
        servcServiceRecords.add(serviceRecord);
        view.setData(childdetails, serviceTypeMap, servcServiceRecords, alertlist);
    }

    @Test
    public void assertUpdateWrapperStatusCallsUpdateWrapperStatus() throws Exception {
        setDataForTest(magicDate);
        view.updateWrapperStatus(wrappers);
        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);

        view.updateWrapper(wrapper);

        Assert.assertNotNull(view.getServiceRecordList());
    }

    @Test
    public void assertIsModalOpenReturnsBoolean() throws Exception {
        view.setModalOpen(true);
        Assert.assertEquals(view.isModalOpen(), true);
        view.setModalOpen(false);
        Assert.assertEquals(view.isModalOpen(), false);
    }

    @Test
    public void updateChildsActiveStatusShouldCheckIfServiceCardAdapterIsNull() {
        // If there is no null check, then this test should fail
        ServiceCardAdapter serviceCardAdapter = view.getServiceCardAdapter();
        ServiceCardAdapter nullServiceCardAdapter = null;

        Whitebox.setInternalState(view, "serviceCardAdapter", nullServiceCardAdapter);

        try {
            view.updateChildsActiveStatus();
        } catch (Exception e) {
            Whitebox.setInternalState(view, "serviceCardAdapter", serviceCardAdapter);
            Assert.fail();
        }
    }

    @Test
    public void updateChildsActivateStatusShouldCheckIfGridViewIsNull() {
        // If there is no null check, then this test should fail
        ExpandableHeightGridView servicesGV = view.getServicesGV();
        ExpandableHeightGridView nullServicesGV = null;

        Whitebox.setInternalState(view, "servicesGV", nullServicesGV);

        try {
            view.updateChildsActiveStatus();
        } catch (Exception e) {
            Whitebox.setInternalState(view, "servicesGV", servicesGV);
            Assert.fail();
        }
    }

    @Test
    public void updateAllWrapperStatusShouldCheckIfAdapterIsNull() {
        // If there is no null check, then this test should fail
        ServiceCardAdapter serviceCardAdapter = view.getServiceCardAdapter();
        ServiceCardAdapter nullServiceCardAdapter = null;

        Whitebox.setInternalState(view, "serviceCardAdapter", nullServiceCardAdapter);

        try {
            view.updateAllWrapperStatus();
        } catch (Exception e) {
            Whitebox.setInternalState(view, "serviceCardAdapter", serviceCardAdapter);
            Assert.fail();
        }
    }

}
