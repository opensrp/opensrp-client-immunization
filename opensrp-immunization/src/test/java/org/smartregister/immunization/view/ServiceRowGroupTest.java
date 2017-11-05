package org.smartregister.immunization.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.test.mock.MockContext;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.view.mock.ServiceRowCardTestActivity;
import org.smartregister.immunization.view.mock.ServiceRowGroupTestActivity;

/**
 * Created by onaio on 30/08/2017.
 */


public class ServiceRowGroupTest extends BaseUnitTest {

    @Mock
    private ServiceRowGroup vaccineGroup;

    @Mock
    private Context context;

    private ActivityController<ServiceRowGroupTestActivity> controller;

    @InjectMocks
    private ServiceRowGroupTestActivity activity;

    @Mock
    private org.smartregister.Context context_;
    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, ServiceRowGroupTestActivity.class);
        controller = Robolectric.buildActivity(ServiceRowGroupTestActivity.class, intent);
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
