package org.smartregister.immunization.fragment;

import android.content.Intent;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

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
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineSchedule;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.fragment.mock.DrishtiApplicationShadow;
import org.smartregister.immunization.fragment.mock.VaccinationEditDialogFragmentTestActivity;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */
@Config(shadows = {FontTextViewShadow.class, DrishtiApplicationShadow.class})
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
        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);

        listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = JsonFormUtils.gson.fromJson(VaccineData.special_vacines, listType);

        VaccineSchedule.init(vaccines, specialVaccines, "child");
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
