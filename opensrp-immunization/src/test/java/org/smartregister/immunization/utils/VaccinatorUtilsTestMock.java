package org.smartregister.immunization.utils;

import android.content.Intent;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.immunization.BaseUnitTest;

import timber.log.Timber;

/**
 * Created by kaderchowdhury on 14/12/17.
 */

public class VaccinatorUtilsTestMock extends BaseUnitTest {

    ActivityController<VaccinatorUtilsTestMockActivity> controller;
    VaccinatorUtilsTestMockActivity activity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, VaccinatorUtilsTestMockActivity.class);
        controller = Robolectric.buildActivity(VaccinatorUtilsTestMockActivity.class, intent);
        activity = controller.get();
        controller.setup();
    }

    @Test
    public void assertAddVaccineDetailTestReturnsChildCount() {
        Assert.assertEquals(activity.addVaccineDetail(), 4);
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
            Timber.e(e);
        }
        System.gc();
    }
}
