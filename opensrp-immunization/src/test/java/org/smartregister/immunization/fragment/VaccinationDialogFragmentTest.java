package org.smartregister.immunization.fragment;

import android.content.Intent;
import android.util.Log;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.CoreLibrary;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.fragment.mock.DrishtiApplicationShadow;
import org.smartregister.immunization.fragment.mock.VaccinationDialogFragmentTestActivity;
import org.smartregister.util.AppProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by onaio on 30/08/2017.
 */
@Config(shadows = {FontTextViewShadow.class, DrishtiApplicationShadow.class})
public class VaccinationDialogFragmentTest extends BaseUnitTest {

    private ActivityController<VaccinationDialogFragmentTestActivity> controller;

    @InjectMocks
    private VaccinationDialogFragmentTestActivity activity;

    @Mock
    private org.smartregister.Context context_;

    @Mock
    private AppProperties properties;

    @Before
    public void setUp() {

        org.mockito.MockitoAnnotations.initMocks(this);
        CoreLibrary.init(context_);

        Mockito.doReturn(properties).when(context_).getAppProperties();

        ImmunizationLibrary immunizationLibrary = Mockito.mock(ImmunizationLibrary.class);
        ReflectionHelpers.setStaticField(ImmunizationLibrary.class, "instance", immunizationLibrary);

        Mockito.doReturn(VaccineRepo.Vaccine.values()).when(immunizationLibrary).getVaccines();
        Mockito.doReturn(properties).when(immunizationLibrary).getProperties();

        activity = Robolectric.buildActivity(VaccinationDialogFragmentTestActivity.class).create().start().get();
        activity.setContentView(R.layout.service_dialog_view);
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
    public void assertOnCreateViewTestSetsUpTheActivity() {
        destroyController();
        Intent intent = new Intent(RuntimeEnvironment.application, VaccinationDialogFragmentTestActivity.class);
        controller = Robolectric.buildActivity(VaccinationDialogFragmentTestActivity.class, intent);
        activity = controller.get();
        controller.setup();
        Assert.assertNotNull(activity);
    }

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        Assert.assertNotNull(
                VaccinationDialogFragment.newInstance(new Date(), Collections.EMPTY_LIST, new ArrayList<VaccineWrapper>()));
        Assert.assertNotNull(VaccinationDialogFragment
                .newInstance(new Date(), Collections.EMPTY_LIST, new ArrayList<VaccineWrapper>(), true));
    }
}
