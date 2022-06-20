package org.smartregister.immunization.view;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import androidx.test.core.app.ApplicationProvider;

import org.joda.time.DateTime;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.util.ReflectionHelpers;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.domain.Event;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceRecordTest;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.ServiceWrapperTest;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.view.mock.ServiceRowCardTestActivity;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;
import org.smartregister.service.UserService;

import java.util.Date;

/**
 * Created by onaio on 30/08/2017.
 */

@Config(shadows = {FontTextViewShadow.class})
public class ServiceRowCardTest extends BaseUnitTest {

    private final String magicOne = "1";
    private final String magicDue = "due";
    private final String magicDefault = "DEFAULT";
    private final String magicExpired = "expired";


    @InjectMocks
    private ServiceRowCardTestActivity activity;
    @Mock
    private org.smartregister.Context openSRPContext;
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Mock
    private UserService userService;

    @Mock
    private CoreLibrary coreLibrary;

    private ServiceRowCard view;

    private ActivityController<ServiceRowCardTestActivity> controller;

    @Before
    public void setUp() {

        Mockito.doReturn(allSharedPreferences).when(userService).getAllSharedPreferences();
        Mockito.doReturn(userService).when(openSRPContext).userService();

        Mockito.doReturn(5).when(allSharedPreferences).getDBEncryptionVersion();
        Mockito.doReturn(allSharedPreferences).when(openSRPContext).allSharedPreferences();

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ServiceRowCardTestActivity.class);
        controller = Robolectric.buildActivity(ServiceRowCardTestActivity.class, intent);
        activity = controller.start().resume().get();
        ReflectionHelpers.setStaticField(CoreLibrary.class, "instance", coreLibrary);
        Mockito.when(coreLibrary.context()).thenReturn(openSRPContext);
        Mockito.doReturn(allSharedPreferences).when(openSRPContext).allSharedPreferences();

        ServiceRecord serviceRecord = new ServiceRecord(0l, ServiceRecordTest.BASEENTITYID,
                ServiceRecordTest.PROGRAMCLIENTID, 0l, ServiceRecordTest.VALUE, new Date(), ServiceRecordTest.ANMID,
                ServiceRecordTest.LOCATIONID, ServiceRecordTest.SYNCED, ServiceRecordTest.EVENTID,
                ServiceRecordTest.FORMSUBMISSIONID, 0l, new Date());
        serviceRecord.setDate(new Date());
        serviceRecord.setName(ServiceWrapperTest.DEFAULTNAME);
        serviceRecord.setEventId(magicOne);
        Event event = new Event();
        event.setEventId(magicOne);
        event.setDateCreated(new DateTime());

        EventClientRepository eventClientRepository = Mockito.mock(EventClientRepository.class);

        Mockito.mockStatic(ImmunizationLibrary.class);
        immunizationLibrary = Mockito.mock(ImmunizationLibrary.class);
        RecurringServiceRecordRepository recurringServiceRecordRepository = Mockito.mock(RecurringServiceRecordRepository.class);
        ImmunizationLibrary.init(Mockito.mock(org.smartregister.Context.class), Mockito.mock(Repository.class),
                Mockito.mock(CommonFtsObject.class), 0, 0);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(immunizationLibrary.recurringServiceRecordRepository())
                .thenReturn(recurringServiceRecordRepository);
        Mockito.when(recurringServiceRecordRepository.find(org.mockito.ArgumentMatchers.anyLong()))
                .thenReturn(serviceRecord);
        Mockito.when(immunizationLibrary.eventClientRepository()).thenReturn(eventClientRepository);
        Mockito.when(eventClientRepository
                        .convert(org.mockito.ArgumentMatchers.any(JSONObject.class), org.mockito.ArgumentMatchers.any(Class.class)))
                .thenReturn(event);
        //        controller.setup();
        view = new ServiceRowCard(ApplicationProvider.getApplicationContext());
        //        view = activity.getInstance();
    }

    @Test
    public void assertgetStateCallsUpdateStateReturnsWrapperState() {
        Alert alert = new Alert("", "", "", AlertStatus.normal, "", "");
        ServiceWrapper wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DUE);

        alert = new Alert("", "", "", AlertStatus.upcoming, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertNotNull(view.getState());

        alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.OVERDUE);

        alert = new Alert("", "", "", AlertStatus.expired, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.EXPIRED);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.EXPIRED);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName(magicDefault);
        wrapper.setUpdatedVaccineDate(new DateTime(), true);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DONE_CAN_NOT_BE_UNDONE);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setDbKey(0l);
        wrapper.setSynced(false);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setUpdatedVaccineDate(new DateTime(), true);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DONE_CAN_BE_UNDONE);
    }

    @Test
    public void assertConstructorsNotNull() {
        Assert.assertNotNull(activity.getInstance());
    }

    @Test
    public void testHideVaccineOverdueRowCardColor() {
        Mockito.when(immunizationLibrary.hideOverdueVaccineStatus()).thenReturn(true);

        Alert alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        ServiceWrapper wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());

        ServiceRowCard rowCard = Mockito.spy(view);
        Button statusIV = Mockito.mock(Button.class);
        ReflectionHelpers.setField(rowCard, "statusIV", statusIV);
        rowCard.setServiceWrapper(wrapper);

        Mockito.verify(statusIV).setBackgroundResource(R.drawable.vaccine_card_background_white);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        rowCard.setServiceWrapper(wrapper);

        Mockito.verify(statusIV, Mockito.times(2)).setBackgroundResource(R.drawable.vaccine_card_background_white);
    }

    @Test
    public void testShowVaccineOverdueRowCardColor() {
        Mockito.when(immunizationLibrary.hideOverdueVaccineStatus()).thenReturn(false);

        Alert alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        ServiceWrapper wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());

        ServiceRowCard rowCard = Mockito.spy(view);
        Button statusIV = Mockito.mock(Button.class);
        ReflectionHelpers.setField(rowCard, "statusIV", statusIV);
        rowCard.setServiceWrapper(wrapper);

        Mockito.verify(statusIV).setBackgroundResource(R.drawable.vaccine_card_background_red);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDbKey(0l);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        rowCard.setServiceWrapper(wrapper);

        Mockito.verify(statusIV).setBackgroundResource(R.drawable.vaccine_card_background_blue);
    }

    @After
    public void tearDown() {
        destroyController();
        activity = null;
        controller = null;
        ReflectionHelpers.setStaticField(CoreLibrary.class, "instance", null);
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
