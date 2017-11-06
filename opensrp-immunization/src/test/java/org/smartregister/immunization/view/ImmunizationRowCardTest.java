package org.smartregister.immunization.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;

import org.joda.time.DateTime;
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
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.view.mock.ImmunizationRowCardTestActivity;
import org.smartregister.immunization.view.mock.VaccineGroupTestActivity;

/**
 * Created by onaio on 30/08/2017.
 */


public class ImmunizationRowCardTest extends BaseUnitTest {

    private ImmunizationRowCard view;

    @Mock
    private Context context;

    private ActivityController<ImmunizationRowCardTestActivity> controller;
    private Alert alert;
    private VaccineWrapper wrapper;
    @InjectMocks
    private ImmunizationRowCardTestActivity activity;

    @Mock
    private org.smartregister.Context context_;
    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, ImmunizationRowCardTestActivity.class);
        controller = Robolectric.buildActivity(ImmunizationRowCardTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        controller.setup();
        view = activity.getInstance();
    }
    @Test
    public void testActivity(){
        Assert.assertNotNull(activity);
    }
    @Test
    public void testConstructors(){
        Assert.assertNotNull(activity.getInstance());
        Assert.assertNotNull(activity.getInstance1());
        Assert.assertNotNull(activity.getInstance2());
        Assert.assertNotNull(activity.getInstance3());
        Assert.assertNotNull(activity.getInstance4());
    }

    public void assertgetStateCallsUpdateStateReturnsWrapperState() throws Exception {
        alert = new Alert("","","", AlertStatus.normal,"","");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("due");
        wrapper.setAlert(alert);
        wrapper.setName("mr");
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(),VaccineCard.State.DUE);

        alert = new Alert("","","", AlertStatus.upcoming,"","");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("due");
        wrapper.setAlert(alert);
        wrapper.setName("mr");
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertNotNull(view.getState());

        alert = new Alert("","","", AlertStatus.urgent,"","");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("due");
        wrapper.setAlert(alert);
        wrapper.setName("mr");
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(),VaccineCard.State.OVERDUE);

        alert = new Alert("","","", AlertStatus.expired,"","");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("due");
        wrapper.setAlert(alert);
        wrapper.setName("measles");
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(),VaccineCard.State.EXPIRED);

        alert = new Alert("","","", AlertStatus.normal,"","");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("expired");
        wrapper.setAlert(alert);
        wrapper.setName("measles");
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(),VaccineCard.State.EXPIRED);

        alert = new Alert("","","", AlertStatus.normal,"","");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(true);
        wrapper.setStatus("expired");
        wrapper.setAlert(alert);
        wrapper.setName("mr");
        wrapper.setUpdatedVaccineDate(new DateTime(),true);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(),VaccineCard.State.DONE_CAN_NOT_BE_UNDONE);

        alert = new Alert("","","", AlertStatus.normal,"","");
        wrapper = new VaccineWrapper();
        wrapper.setSynced(false);
        wrapper.setStatus("expired");
        wrapper.setAlert(alert);
        wrapper.setName("mr");
        wrapper.setUpdatedVaccineDate(new DateTime(),true);
        wrapper.setVaccineDate(new DateTime());
        view.setVaccineWrapper(wrapper);
        Assert.assertEquals(view.getState(),VaccineCard.State.DONE_CAN_BE_UNDONE);
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
