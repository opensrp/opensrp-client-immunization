package org.smartregister.immunization.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.joda.time.DateTime;
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
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.CoreLibrary;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.BuildConfig;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.view.mock.ServiceCardTestActivity;
import org.smartregister.repository.Repository;
import org.smartregister.util.AppProperties;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceCardTest extends BaseUnitTest {

    private final String magicDue = "due";
    private final String magicExpired = "expired";
    private final String magicDefault = "DEFAULT";
    private ServiceCard view;
    @Mock
    private Context context;
    private ActivityController<ServiceCardTestActivity> controller;
    @InjectMocks
    private ServiceCardTestActivity activity;
    @Mock
    private org.smartregister.Context context_;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, ServiceCardTestActivity.class);
        controller = Robolectric.buildActivity(ServiceCardTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        controller.setup();
        view = activity.getInstance();

    }

    @Test
    public void assertGetUndoBNotNull() {
        Assert.assertNotNull(view.getUndoB());
    }

    @Test
    public void assertgetStateCallsUpdateStateReturnsWrapperState() {
        Alert alert = new Alert("", "", "", AlertStatus.normal, "", "");
        ServiceWrapper wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DUE);

        alert = new Alert("", "", "", AlertStatus.upcoming, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertNotNull(view.getState());

        alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.OVERDUE);

        alert = new Alert("", "", "", AlertStatus.expired, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.EXPIRED);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.EXPIRED);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setUpdatedVaccineDate(new DateTime(), true);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DONE_CAN_NOT_BE_UNDONE);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new ServiceWrapper();
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
        Assert.assertNotNull(activity.getInstance1());
        Assert.assertNotNull(activity.getInstance2());
        Assert.assertNotNull(activity.getInstance3());
    }

    @Test
    public void testHideVaccineOverdueVaccineCardColor() {
        ReflectionHelpers.setStaticField(ImmunizationLibrary.class, "instance", null);

        AppProperties appProperties = Mockito.mock(AppProperties.class);
        Mockito.when(appProperties.hasProperty(IMConstants.APP_PROPERTIES.VACCINE_OVERDUE_STATUS_HIDE)).thenReturn(true);
        Mockito.when(appProperties.getPropertyBoolean(IMConstants.APP_PROPERTIES.VACCINE_OVERDUE_STATUS_HIDE)).thenReturn(true);
        Mockito.when(context_.getAppProperties()).thenReturn(appProperties);
        ImmunizationLibrary.init(context_, Mockito.mock(Repository.class), null, BuildConfig.VERSION_CODE, 1);

        Alert alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        ServiceWrapper wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);

        ServiceCard cardView = Mockito.spy(view);
        cardView.setServiceWrapper(wrapper);
//        cardView.getState();

        Mockito.verify(cardView).setBackgroundResource(R.drawable.vaccine_card_background_white);


        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        cardView.setServiceWrapper(wrapper);

        Mockito.verify(cardView, Mockito.times(2)).setBackgroundResource(R.drawable.vaccine_card_background_white);

    }

    @Test
    public void testShowVaccineOverdueVaccineCardColor() {
        ReflectionHelpers.setStaticField(ImmunizationLibrary.class, "instance", null);

        AppProperties appProperties = Mockito.mock(AppProperties.class);
        Mockito.when(appProperties.hasProperty(IMConstants.APP_PROPERTIES.VACCINE_OVERDUE_STATUS_HIDE)).thenReturn(false);
        Mockito.when(appProperties.getPropertyBoolean(IMConstants.APP_PROPERTIES.VACCINE_OVERDUE_STATUS_HIDE)).thenReturn(false);
        Mockito.when(context_.getAppProperties()).thenReturn(appProperties);
        ImmunizationLibrary.init(context_, Mockito.mock(Repository.class), null, BuildConfig.VERSION_CODE, 1);

        Alert alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        ServiceWrapper wrapper = new ServiceWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        view.setServiceWrapper(wrapper);

        ServiceCard cardView = Mockito.spy(view);
        cardView.setServiceWrapper(wrapper);
//        cardView.getState();

        Mockito.verify(cardView).setBackgroundResource(R.drawable.vaccine_card_background_red);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setDefaultName(magicDefault);
        wrapper.setVaccineDate(new DateTime());
        cardView.setServiceWrapper(wrapper);

        Mockito.verify(cardView).setBackgroundResource(R.drawable.vaccine_card_background_blue);

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
