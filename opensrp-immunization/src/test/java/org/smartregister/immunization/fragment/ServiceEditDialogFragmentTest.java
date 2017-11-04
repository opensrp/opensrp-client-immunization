package org.smartregister.immunization.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

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
import org.smartregister.domain.Alert;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceRecordTest;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.ServiceWrapperTest;
import org.smartregister.immunization.fragment.mock.FragmentUtilActivityUsingServiceActionListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;
/**
 * Created by onaio on 30/08/2017.
 */
@Config(shadows = {FontTextViewShadow.class})
public class ServiceEditDialogFragmentTest extends BaseUnitTest {

    private ActivityController<FragmentUtilActivityUsingServiceActionListener> controller;

    @InjectMocks
    private FragmentUtilActivityUsingServiceActionListener activity;

    @Mock
    private org.smartregister.Context context_;


//    @Rule
//    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception {
//        view = org.powermock.api.mockito.PowerMockito.mock(LinearLayout.class);
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
        junit.framework.Assert.assertNotNull(ServiceEditDialogFragment.newInstance(new DateTime(),Collections.EMPTY_LIST, new ServiceWrapper(), view,true));
    }

    @Test
    public void onCreateViewTest() throws Exception {
        destroyController();
        Intent intent = new Intent(RuntimeEnvironment.application, FragmentUtilActivityUsingServiceActionListener.class);
        controller = Robolectric.buildActivity(FragmentUtilActivityUsingServiceActionListener.class, intent);
        activity = controller.get();
        controller.setup();

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
