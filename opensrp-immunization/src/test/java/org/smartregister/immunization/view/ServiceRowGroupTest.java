package org.smartregister.immunization.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceRecordTest;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceTypeTest;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.ServiceWrapperTest;
import org.smartregister.immunization.view.mock.ServiceRowGroupTestActivity;

/**
 * Created by onaio on 30/08/2017.
 */


public class ServiceRowGroupTest extends BaseUnitTest {

    private ServiceRowGroup view;

    @Mock
    private Context context;

    private ActivityController<ServiceRowGroupTestActivity> controller;

    @InjectMocks
    private ServiceRowGroupTestActivity activity;

    @Mock
    private org.smartregister.Context context_;

    private ServiceWrapper wrapper;
    private String type = "SERVICETYPE";

    @Before
    public void setUp() throws Exception {
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

//        setDataForTest("1985-07-24T00:00:00.000Z");
//        view.updateViews();
//        ServiceRowGroup.OnServiceClickedListener onServiceClickedListener = Mockito.mock(ServiceRowGroup.OnServiceClickedListener.class);
//        view.setOnServiceClickedListener(onServiceClickedListener);
//        ServiceRowCard serviceCard = new ServiceRowCard(RuntimeEnvironment.application);
//        wrapper = new ServiceWrapper();
//        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
//        serviceCard.setServiceWrapper(wrapper);
//        view.onClick(serviceCard);
//
//        Mockito.verify(onServiceClickedListener).onClick(any(ServiceRowGroup.class), any(ServiceWrapper.class));
//
//        ServiceRowGroup.OnServiceUndoClickListener onServiceUndoClickListener = Mockito.mock(ServiceRowGroup.OnServiceUndoClickListener.class);
//        view.setOnServiceUndoClickListener(onServiceUndoClickListener);
//        View v = new View(RuntimeEnvironment.application);
//        v.setId(R.id.undo_b);
//        ViewGroup parent = new LinearLayout(RuntimeEnvironment.application);
//        parent.addView(v);
//        serviceCard.addView(parent);
//        view.onClick(v);
//        Mockito.verify(onServiceUndoClickListener).onUndoClick(any(ServiceRowGroup.class), any(ServiceWrapper.class));


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
    public void assertIsModalOpenReturnsBoolean() throws Exception {
        view.setModalOpen(true);
        Assert.assertEquals(view.isModalOpen(), true);
        view.setModalOpen(false);
        Assert.assertEquals(view.isModalOpen(), false);
    }

    public void setDataForTest(String dateTimeString) throws Exception {
        ArrayList<ServiceWrapper> wrappers = new ArrayList<ServiceWrapper>();
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

        ArrayList<Alert> alertlist = new ArrayList<Alert>();
        alertlist.add(alert);
        Map<String, List<ServiceType>> serviceTypeMap = new HashMap<String, List<ServiceType>>();
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
        ArrayList<ServiceType> serviceTypes = new ArrayList<ServiceType>();
        serviceTypes.add(serviceType);
        serviceTypeMap.put(type, serviceTypes);
        ArrayList<ServiceRecord> servcServiceRecords = new ArrayList<ServiceRecord>();
        ServiceRecord serviceRecord = new ServiceRecord(0l, ServiceRecordTest.BASEENTITYID, ServiceRecordTest.PROGRAMCLIENTID, 0l, ServiceRecordTest.VALUE, new Date(), ServiceRecordTest.ANMID, ServiceRecordTest.LOCATIONID, ServiceRecordTest.SYNCED, ServiceRecordTest.EVENTID, ServiceRecordTest.FORMSUBMISSIONID, 0l);
        serviceRecord.setDate(new Date());
        serviceRecord.setName(ServiceWrapperTest.DEFAULTNAME);
        servcServiceRecords.add(serviceRecord);
        view.setData(childdetails, serviceTypes, servcServiceRecords, alertlist);
    }
}