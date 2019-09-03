package org.smartregister.immunization.fragment;

import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.db.VaccineRepo;
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
@Config (shadows = {FontTextViewShadow.class, DrishtiApplicationShadow.class})
@PrepareForTest({ImmunizationLibrary.class})
public class VaccinationEditDialogFragmentTest extends BaseUnitTest {

    private ActivityController<VaccinationEditDialogFragmentTestActivity> controller;

    @InjectMocks
    private VaccinationEditDialogFragmentTestActivity activity;

    @Mock
    private org.smartregister.Context context_;

    @Mock
    private ImmunizationLibrary immunizationLibrary;
    @Mock
    private Context context;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() {
        CoreLibrary.init(context_);
        org.mockito.MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);

        Mockito.doReturn(VaccineRepo.Vaccine.values()).when(immunizationLibrary).getVaccines();

        activity = Robolectric.buildActivity(VaccinationEditDialogFragmentTestActivity.class).create().start().get();
        activity.setContentView(R.layout.service_dialog_view);

        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);

        listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = JsonFormUtils.gson
                .fromJson(VaccineData.special_vacines, listType);

        VaccineSchedule.init(vaccines, specialVaccines, "child");
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

        Assert.assertNotNull(VaccinationEditDialogFragment
                .newInstance(null, new Date(), Collections.EMPTY_LIST, new ArrayList<VaccineWrapper>(), null));
        Assert.assertNotNull(VaccinationEditDialogFragment
                .newInstance(null, new Date(), Collections.EMPTY_LIST, new ArrayList<VaccineWrapper>(), null, true));
    }
}
