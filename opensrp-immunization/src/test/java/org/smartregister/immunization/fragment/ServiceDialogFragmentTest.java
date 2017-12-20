package org.smartregister.immunization.fragment;

import android.content.Intent;
import android.util.Log;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.CoreLibrary;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.fragment.mock.DrishtiApplicationShadow;
import org.smartregister.immunization.fragment.mock.ServiceDialogFragmentTestActivity;

import java.util.Collections;

/**
 * Created by onaio on 30/08/2017.
 */
@Config(shadows = {FontTextViewShadow.class, DrishtiApplicationShadow.class})
public class ServiceDialogFragmentTest extends BaseUnitTest {

    private ActivityController<ServiceDialogFragmentTestActivity> controller;

    @InjectMocks
    private ServiceDialogFragmentTestActivity activity;

    @Mock
    private org.smartregister.Context context_;

    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);

        Intent intent = new Intent(RuntimeEnvironment.application, ServiceDialogFragmentTestActivity.class);
        controller = Robolectric.buildActivity(ServiceDialogFragmentTestActivity.class, intent);
        activity = controller.start().resume().get();

        CoreLibrary.init(context_);
        controller.setup();

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
    public void assertThatCallToNewInstanceCreatesAFragment() {
        junit.framework.Assert.assertNotNull(ServiceDialogFragment.newInstance(Collections.EMPTY_LIST, new ServiceWrapper()));
        junit.framework.Assert.assertNotNull(ServiceDialogFragment.newInstance(new DateTime(), Collections.EMPTY_LIST, new ServiceWrapper(), true));
    }

    @Test
    public void assertOnCreateViewTestSetsUpTheActivity() throws Exception {
        destroyController();
        Intent intent = new Intent(RuntimeEnvironment.application, ServiceDialogFragmentTestActivity.class);
        controller = Robolectric.buildActivity(ServiceDialogFragmentTestActivity.class, intent);
        activity = controller.get();
        controller.setup();
        junit.framework.Assert.assertNotNull(activity);
    }

}
