package org.smartregister.immunization.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;

import org.joda.time.DateTime;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.domain.db.Event;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceRecordTest;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.ServiceWrapperTest;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.view.mock.ServiceGroupTestActivity;
import org.smartregister.immunization.view.mock.ServiceRowCardTestActivity;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

/**
 * Created by onaio on 30/08/2017.
 */


public class ServiceRowCardTest extends BaseUnitTest {



    private ServiceRowCard view;

    @Mock
    private Context context;

    private ActivityController<ServiceRowCardTestActivity> controller;

    @InjectMocks
    private ServiceRowCardTestActivity activity;

    @Mock
    private org.smartregister.Context context_;
    private Alert alert;
    private ServiceWrapper wrapper;
    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, ServiceRowCardTestActivity.class);
        controller = Robolectric.buildActivity(ServiceRowCardTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        controller.setup();
        view = activity.getInstance();
//        ServiceRecord serviceRecord= new ServiceRecord(0l, ServiceRecordTest.BASEENTITYID, ServiceRecordTest.PROGRAMCLIENTID, 0l, ServiceRecordTest.VALUE, new Date(), ServiceRecordTest.ANMID, ServiceRecordTest.LOCATIONID, ServiceRecordTest.SYNCED, ServiceRecordTest.EVENTID, ServiceRecordTest.FORMSUBMISSIONID, 0l);
//        serviceRecord.setDate(new Date());
//        serviceRecord.setName(ServiceWrapperTest.DEFAULTNAME);
//        serviceRecord.setEventId("1");
//        Event event = new Event();
//        event.setEventId("1");
//        event.setDateCreated(new DateTime());
//        EventClientRepository eventClientRepository = Mockito.mock(EventClientRepository.class);
//        PowerMockito.mockStatic(ImmunizationLibrary.class);
//        ImmunizationLibrary.init(Mockito.mock(org.smartregister.Context.class),Mockito.mock(Repository.class),Mockito.mock(CommonFtsObject.class));
//        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(Mockito.mock(ImmunizationLibrary.class));
//        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceRecordRepository()).thenReturn(Mockito.mock(RecurringServiceRecordRepository.class));
//        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceRecordRepository().find(anyLong())).thenReturn(serviceRecord);
//        PowerMockito.when(ImmunizationLibrary.getInstance().eventClientRepository()).thenReturn(eventClientRepository);
//        PowerMockito.when(eventClientRepository.convert(any(JSONObject.class),Event.class)).thenReturn(event);

    }


    public void assertgetStateCallsUpdateStateReturnsWrapperState() throws Exception {
        alert = new Alert("","","", AlertStatus.normal,"","");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("due");
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName("DEFAULT");
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(),ServiceRowCard.State.DUE);

        alert = new Alert("","","", AlertStatus.upcoming,"","");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("due");
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName("DEFAULT");
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertNotNull(view.getState());

        alert = new Alert("","","", AlertStatus.urgent,"","");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("due");
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName("DEFAULT");
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(),ServiceRowCard.State.OVERDUE);

        alert = new Alert("","","", AlertStatus.expired,"","");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("due");
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName("DEFAULT");
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(),ServiceRowCard.State.EXPIRED);

        alert = new Alert("","","", AlertStatus.normal,"","");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("expired");
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName("DEFAULT");
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(),ServiceRowCard.State.EXPIRED);

        alert = new Alert("","","", AlertStatus.normal,"","");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("expired");
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName("DEFAULT");
        wrapper.setUpdatedVaccineDate(new DateTime(),true);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(),ServiceRowCard.State.DONE_CAN_NOT_BE_UNDONE);

        alert = new Alert("","","", AlertStatus.normal,"","");
        wrapper = new ServiceWrapper();
        wrapper.setDbKey(0l);
        wrapper.setSynced(false);
        wrapper.setStatus("expired");
        wrapper.setAlert(alert);
        wrapper.setDefaultName("DEFAULT");
        wrapper.setUpdatedVaccineDate(new DateTime(),true);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(),ServiceRowCard.State.DONE_CAN_BE_UNDONE);
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


}
