package org.smartregister.immunization.utils;

import android.content.Intent;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.immunization.BaseUnitTest;

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
}
