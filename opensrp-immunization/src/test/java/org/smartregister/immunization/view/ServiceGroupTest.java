package org.smartregister.immunization.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceRecordTest;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceTypeTest;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.ServiceWrapperTest;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.view.mock.ServiceCardTestActivity;
import org.smartregister.immunization.view.mock.ServiceGroupTestActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceGroupTest extends BaseUnitTest {


    private ServiceGroup view;

    @Mock
    private Context context;

    private ActivityController<ServiceGroupTestActivity> controller;

    @InjectMocks
    private ServiceGroupTestActivity activity;

    @Mock
    private org.smartregister.Context context_;
    private ArrayList<ServiceWrapper>wrappers;
    private ServiceWrapper wrapper;
    private CommonPersonObjectClient childdetails;

    private ArrayList<ServiceRecord>servcServiceRecords;
    private ArrayList<Alert>alertlist;
    private Map<String, List<ServiceType>> serviceTypeMap;
    private String type = "SERVICETYPE";
    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, ServiceGroupTestActivity.class);
        controller = Robolectric.buildActivity(ServiceGroupTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        controller.setup();
        view = activity.getInstance();
    }

    @Test
    public void assertConstructorsNotNull(){
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

        setDataForTest("1985-07-24T00:00:00.000Z");
        view.updateViews();
        ServiceGroup.OnServiceClickedListener onServiceClickedListener = Mockito.mock(ServiceGroup.OnServiceClickedListener.class);
        view.setOnServiceClickedListener(onServiceClickedListener);
        ServiceCard serviceCard = new ServiceCard(RuntimeEnvironment.application);
        wrapper= new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
        serviceCard.setServiceWrapper(wrapper);
        view.onClick(serviceCard);

        Mockito.verify(onServiceClickedListener).onClick(any(ServiceGroup.class),any(ServiceWrapper.class));

        ServiceGroup.OnServiceUndoClickListener onServiceUndoClickListener = Mockito.mock(ServiceGroup.OnServiceUndoClickListener.class);
        view.setOnServiceUndoClickListener(onServiceUndoClickListener);
        View v = new View(RuntimeEnvironment.application);
        v.setId(R.id.undo_b);
        ViewGroup parent = new LinearLayout(RuntimeEnvironment.application);
        parent.addView(v);
        serviceCard.addView(parent);
        view.onClick(v);
        Mockito.verify(onServiceUndoClickListener).onUndoClick(any(ServiceGroup.class),any(ServiceWrapper.class));


    }

    @Test
    public void assertUpdateWrapperStatusCallsUpdateWrapperStatus() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        view.updateWrapperStatus(wrappers);
        wrapper= new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);

        view.updateWrapper(wrapper);

        Assert.assertNotNull(view.getServiceRecordList());
    }
    @Test
    public void assertIsModalOpenReturnsBoolean() throws Exception {
        view.setModalOpen(true);
        Assert.assertEquals(view.isModalOpen(),true);
        view.setModalOpen(false);
        Assert.assertEquals(view.isModalOpen(),false);
    }
    @Test
    public void assertOnStateChangedCallsUpdateViews() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        view.onStateChanged(ServiceCard.State.DONE_CAN_BE_UNDONE);
        //calls updateViews which sets the adapter, we can check the the adapter is not null
        Assert.assertNotNull(view.getServiceRecordList());
    }
    public void setDataForTest(String dateTimeString) throws Exception {
        wrappers = new ArrayList<ServiceWrapper>();
        wrapper= new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);

        wrappers.add(wrapper);
        wrapper= new ServiceWrapper();

        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
        wrappers.add(wrapper);
        wrapper= new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);

        wrappers.add(wrapper);

        HashMap<String,String> detail = new HashMap<String,String>();
        detail.put("dob",dateTimeString);
        childdetails = new CommonPersonObjectClient("1",detail,"NME");
        childdetails.setColumnmaps(detail);
        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.measles2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        Alert alert = new Alert("","","", AlertStatus.complete,"","");

        alertlist =  new ArrayList<Alert>();
        alertlist.add(alert);
        serviceTypeMap = new HashMap<String,List<ServiceType>>();
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
        ArrayList<ServiceType>serviceTypes= new ArrayList<ServiceType>();
        serviceTypes.add(serviceType);
        serviceTypeMap.put(type,serviceTypes);
        servcServiceRecords = new ArrayList<ServiceRecord>();
        ServiceRecord serviceRecord= new ServiceRecord(0l, ServiceRecordTest.BASEENTITYID, ServiceRecordTest.PROGRAMCLIENTID, 0l, ServiceRecordTest.VALUE, new Date(), ServiceRecordTest.ANMID, ServiceRecordTest.LOCATIONID, ServiceRecordTest.SYNCED, ServiceRecordTest.EVENTID, ServiceRecordTest.FORMSUBMISSIONID, 0l);
        serviceRecord.setDate(new Date());
        serviceRecord.setName(ServiceWrapperTest.DEFAULTNAME);
        servcServiceRecords.add(serviceRecord);
        view.setData(childdetails,serviceTypeMap,servcServiceRecords,alertlist);
    }

}
