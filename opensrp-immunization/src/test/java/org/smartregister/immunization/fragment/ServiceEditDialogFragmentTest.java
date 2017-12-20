package org.smartregister.immunization.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.CoreLibrary;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.fragment.mock.DrishtiApplicationShadow;
import org.smartregister.immunization.fragment.mock.FragmentUtilActivityUsingServiceActionListener;

import java.util.Collections;

/**
 * Created by onaio on 30/08/2017.
 */
@Config(shadows = {FontTextViewShadow.class, DrishtiApplicationShadow.class})
public class ServiceEditDialogFragmentTest extends BaseUnitTest {

    private ActivityController<FragmentUtilActivityUsingServiceActionListener> controller;

    @InjectMocks
    private FragmentUtilActivityUsingServiceActionListener activity;

    @Mock
    private org.smartregister.Context context_;


    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);

        Intent intent = new Intent(RuntimeEnvironment.application, FragmentUtilActivityUsingServiceActionListener.class);
        controller = Robolectric.buildActivity(FragmentUtilActivityUsingServiceActionListener.class, intent);
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

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        View view = Mockito.mock(View.class);
        junit.framework.Assert.assertNotNull(ServiceEditDialogFragment.newInstance(Collections.EMPTY_LIST, new ServiceWrapper(), view));
        junit.framework.Assert.assertNotNull(ServiceEditDialogFragment.newInstance(new DateTime(), Collections.EMPTY_LIST, new ServiceWrapper(), view, true));
    }

    @Test
    public void assertOnCreateViewTestSetsUpTheActivity() throws Exception {
        destroyController();
        Intent intent = new Intent(RuntimeEnvironment.application, FragmentUtilActivityUsingServiceActionListener.class);
        controller = Robolectric.buildActivity(FragmentUtilActivityUsingServiceActionListener.class, intent);
        activity = controller.get();
        controller.setup();
        junit.framework.Assert.assertNotNull(activity);
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
