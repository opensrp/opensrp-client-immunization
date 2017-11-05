package org.smartregister.immunization.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONObject;
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
import org.robolectric.annotation.Config;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.BuildConfig;
import org.smartregister.immunization.R;
import org.smartregister.immunization.view.mock.ImmunizationRowCardTestActivity;
import org.smartregister.immunization.view.mock.ImmunizationRowGroupTestActivity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

/**
 * Created by onaio on 30/08/2017.
 */

public class ImmunizationRowGroupTest extends BaseUnitTest {

    @Mock
    private ImmunizationRowGroup vaccineGroup;

    @Mock
    private Context context;

    private ActivityController<ImmunizationRowGroupTestActivity> controller;

    @InjectMocks
    private ImmunizationRowGroupTestActivity activity;

    @Mock
    private org.smartregister.Context context_;
    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, ImmunizationRowGroupTestActivity.class);
        controller = Robolectric.buildActivity(ImmunizationRowGroupTestActivity.class, intent);
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
