package org.smartregister.immunization.fragment;

import android.content.Intent;
import android.util.Log;
import org.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.CoreLibrary;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineSchedule;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.fragment.mock.VaccinationEditDialogFragmentTestActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccinationEditDialogFragmentTest extends BaseUnitTest {

    private ActivityController<VaccinationEditDialogFragmentTestActivity> controller;

    @InjectMocks
    private VaccinationEditDialogFragmentTestActivity activity;

    @Mock
    private org.smartregister.Context context_;

    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);

        Intent intent = new Intent(RuntimeEnvironment.application, VaccinationEditDialogFragmentTestActivity.class);
        controller = Robolectric.buildActivity(VaccinationEditDialogFragmentTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        VaccineSchedule.init(new JSONArray(VaccineData.vaccines), new JSONArray(VaccineData.special_vacines), "child");
        controller.setup();
    }

    @After
    public void tearDown() {
        destroyController();
        activity = null;
        controller = null;
    }

    @Test
    public void onCreateViewTest() throws Exception {
        destroyController();
        Intent intent = new Intent(RuntimeEnvironment.application, VaccinationEditDialogFragmentTestActivity.class);
        controller = Robolectric.buildActivity(VaccinationEditDialogFragmentTestActivity.class, intent);
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

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        junit.framework.Assert.assertNotNull(VaccinationEditDialogFragment.newInstance(null, new Date(), Collections.EMPTY_LIST, new ArrayList<VaccineWrapper>(), null));
        junit.framework.Assert.assertNotNull(VaccinationEditDialogFragment.newInstance(null, new Date(), Collections.EMPTY_LIST, new ArrayList<VaccineWrapper>(), null, true));
    }
}
