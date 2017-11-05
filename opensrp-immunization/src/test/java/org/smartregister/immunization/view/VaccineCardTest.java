package org.smartregister.immunization.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;

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
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.view.mock.ServiceRowCardTestActivity;
import org.smartregister.immunization.view.mock.VaccineCardTestActivity;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineCardTest extends BaseUnitTest {


    @Mock
    private VaccineCard vaccineGroup;

    @Mock
    private Context context;

    private ActivityController<VaccineCardTestActivity> controller;

    @InjectMocks
    private VaccineCardTestActivity activity;

    @Mock
    private org.smartregister.Context context_;
    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, VaccineCardTestActivity.class);
        controller = Robolectric.buildActivity(VaccineCardTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        controller.setup();

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
