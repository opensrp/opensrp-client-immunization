package org.smartregister.immunization.fragment;

import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.smartregister.CoreLibrary;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.fragment.mock.UndoVaccinationDialogFragmentTestActivity;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.service.UserService;
import org.smartregister.util.AppProperties;

import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */

public class UndoVaccinationDialogFragmentTest extends BaseUnitTest {

    private ActivityController<UndoVaccinationDialogFragmentTestActivity> controller;

    @InjectMocks
    private UndoVaccinationDialogFragmentTestActivity activity;

    @Mock
    private org.smartregister.Context context_;

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

        CoreLibrary.init(context_);
        Mockito.doReturn(properties).when(context_).getAppProperties();

        activity = Robolectric.buildActivity(UndoVaccinationDialogFragmentTestActivity.class).create().start().get();
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
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UndoVaccinationDialogFragmentTestActivity.class);
        controller = Robolectric.buildActivity(UndoVaccinationDialogFragmentTestActivity.class, intent);
        activity = controller.get();
        controller.setup();
        Assert.assertNotNull(activity);
    }

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        Assert.assertNotNull(UndoVaccinationDialogFragment.newInstance(new VaccineWrapper()));
    }

    @Test
    public void testSetFilterTouchesWhenObscuredSetsFlagToTrue() {

        List<Fragment> fragmentList = activity.getSupportFragmentManager().getFragments();

        Assert.assertNotNull(fragmentList);
        Assert.assertTrue(fragmentList.size() > 0);

        UndoVaccinationDialogFragment fragment = (UndoVaccinationDialogFragment) fragmentList.get(0);
        Assert.assertNotNull(fragment);

        boolean isEnabled = fragment.getView().getFilterTouchesWhenObscured();
        Assert.assertTrue(isEnabled);

    }


}
