package org.smartregister.immunization.view;

import android.content.Intent;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.CoreLibrary;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.State;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.view.mock.VaccineCardTestActivity;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.Repository;
import org.smartregister.service.UserService;
import org.smartregister.util.AppProperties;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineCardTest extends BaseUnitTest {

    private final String magicDue = "due";
    private final String magicMR = "mr";
    private final String magicExpired = "expired";
    private VaccineCard view;

    private ActivityController<VaccineCardTestActivity> controller;
    @InjectMocks
    private VaccineCardTestActivity activity;
    @Mock
    private org.smartregister.Context context_;

    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {

        Mockito.doReturn(allSharedPreferences).when(userService).getAllSharedPreferences();
        Mockito.doReturn(userService).when(context_).userService();

        Mockito.doReturn(5).when(allSharedPreferences).getDBEncryptionVersion();
        Mockito.doReturn(allSharedPreferences).when(context_).allSharedPreferences();

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), VaccineCardTestActivity.class);
        controller = Robolectric.buildActivity(VaccineCardTestActivity.class, intent);
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
    public void testConstructors() {
        Assert.assertNotNull(new VaccineCard(ApplicationProvider.getApplicationContext()));
        Assert.assertNotNull(new VaccineCard(ApplicationProvider.getApplicationContext(), Robolectric.buildAttributeSet().build()));
        Assert.assertNotNull(new VaccineCard(ApplicationProvider.getApplicationContext(), Robolectric.buildAttributeSet().build(), 0));
        Assert.assertNotNull(new VaccineCard(ApplicationProvider.getApplicationContext(), Robolectric.buildAttributeSet().build(), 0, 0));
    }

    @Test
    public void assertgetStateCallsUpdateStateReturnsWrapperState() {
        AppProperties appProperties = Mockito.mock(AppProperties.class);
        Mockito.when(appProperties.isTrue(IMConstants.APP_PROPERTIES.VACCINE_EXPIRED_ENTRY_ALLOW)).thenReturn(true);
        Mockito.when(context_.getAppProperties()).thenReturn(appProperties);
        ImmunizationLibrary.init(context_, Mockito.mock(Repository.class), null, 1, 1);

        Alert alert = new Alert("", "", "", AlertStatus.normal, "", "");
        VaccineWrapper wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DUE);

        alert = new Alert("", "", "", AlertStatus.upcoming, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertNotNull(view.getState());

        alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.OVERDUE);

        alert = new Alert("", "", "", AlertStatus.expired, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.EXPIRED);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setName("measles");
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.EXPIRED);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setUpdatedVaccineDate(new DateTime(), true);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DONE_CAN_NOT_BE_UNDONE);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(false);
        wrapper.setStatus(magicExpired);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setUpdatedVaccineDate(new DateTime(), true);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(), State.DONE_CAN_BE_UNDONE);

        ReflectionHelpers.setStaticField(ImmunizationLibrary.class, "instance", null);
    }

    @Test
    public void assertConstructorsNotNull() {
        Assert.assertNotNull(activity.getInstance());
        //Assert.assertNotNull(activity.getInstance1());
        //Assert.assertNotNull(activity.getInstance2());
        //Assert.assertNotNull(activity.getInstance3());
    }

    @Test
    public void testHideVaccineOverdueVaccineCardColor() {
        AppProperties appProperties = Mockito.mock(AppProperties.class);
        Mockito.when(appProperties.hasProperty(IMConstants.APP_PROPERTIES.HIDE_OVERDUE_VACCINE_STATUS)).thenReturn(true);
        Mockito.when(appProperties.getPropertyBoolean(IMConstants.APP_PROPERTIES.HIDE_OVERDUE_VACCINE_STATUS)).thenReturn(true);
        Mockito.when(context_.getAppProperties()).thenReturn(appProperties);
        ImmunizationLibrary.init(context_, Mockito.mock(Repository.class), null, 1, 1);

        Alert alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        VaccineWrapper wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());

        VaccineCard cardView = Mockito.spy(view);
        cardView.setVaccineWrapper(wrapper);
//        cardView.getState();

        Mockito.verify(cardView).setBackgroundResource(R.drawable.vaccine_card_background_white);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        cardView.setVaccineWrapper(wrapper);

        Mockito.verify(cardView, Mockito.times(2)).setBackgroundResource(R.drawable.vaccine_card_background_white);
    }

    @Test
    public void testShowVaccineOverdueVaccineCardColor() {
        ImmunizationLibrary immunizationLibrary = Mockito.mock(ImmunizationLibrary.class);
        AppProperties appProperties = Mockito.mock(AppProperties.class);
        Mockito.when(context_.getAppProperties()).thenReturn(appProperties);

        ReflectionHelpers.setStaticField(ImmunizationLibrary.class, "instance", immunizationLibrary);

        Mockito.when(immunizationLibrary.hideOverdueVaccineStatus()).thenReturn(false);

        Alert alert = new Alert("", "", "", AlertStatus.urgent, "", "");
        VaccineWrapper wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());

        VaccineCard cardView = Mockito.spy(view);
        cardView.setVaccineWrapper(wrapper);
//        cardView.getState();

        Mockito.verify(cardView).setBackgroundResource(R.drawable.vaccine_card_background_red);

        alert = new Alert("", "", "", AlertStatus.normal, "", "");
        wrapper.setSynced(true);
        wrapper.setStatus(magicDue);
        wrapper.setAlert(alert);
        wrapper.setName(magicMR);
        wrapper.setVaccineDate(new DateTime());
        cardView.setVaccineWrapper(wrapper);

        Mockito.verify(cardView).setBackgroundResource(R.drawable.vaccine_card_background_blue);

        ReflectionHelpers.setStaticField(ImmunizationLibrary.class, "instance", null);
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
