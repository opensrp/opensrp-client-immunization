package org.smartregister.immunization.fragment;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.google.gson.reflect.TypeToken;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.service.UserService;
import org.smartregister.util.AppProperties;
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

    @Mock
    private ImmunizationLibrary immunizationLibrary;
    @Mock
    private Context context;

    @Mock
    private AppProperties properties;

    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Mock
    private UserService userService;

    @Before
    public void setUp() {

        Mockito.doReturn(allSharedPreferences).when(userService).getAllSharedPreferences();
        Mockito.doReturn(userService).when(context_).userService();

        Mockito.doReturn(5).when(allSharedPreferences).getDBEncryptionVersion();
        Mockito.doReturn(allSharedPreferences).when(context_).allSharedPreferences();
        Mockito.doReturn(allSharedPreferences).when(context).allSharedPreferences();

        CoreLibrary.init(context_);
        Mockito.doReturn(properties).when(context_).getAppProperties();

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        Mockito.doReturn(properties).when(immunizationLibrary).getProperties();

        Mockito.doReturn(VaccineRepo.Vaccine.values()).when(immunizationLibrary).getVaccines(IMConstants.VACCINE_TYPE.CHILD);

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

    @Test
    public void testSetFilterTouchesWhenObscuredSetsFlagToTrue() {

        List<Fragment> fragmentList = activity.getSupportFragmentManager().getFragments();

        Assert.assertNotNull(fragmentList);
        Assert.assertTrue(fragmentList.size() > 0);

        VaccinationEditDialogFragment fragment = (VaccinationEditDialogFragment) fragmentList.get(0);
        Assert.assertNotNull(fragment);

        boolean isEnabled = fragment.getView().getFilterTouchesWhenObscured();
        Assert.assertTrue(isEnabled);

    }
}
