package org.smartregister.immunization.fragment;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ApplicationProvider;

import android.util.Log;
import android.view.View;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.CoreLibrary;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.R;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.fragment.mock.DrishtiApplicationShadow;
import org.smartregister.immunization.fragment.mock.FragmentUtilActivityUsingServiceActionListener;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.service.UserService;
import org.smartregister.util.AppProperties;

import java.util.Collections;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */
@Config(shadows = {FontTextViewShadow.class, DrishtiApplicationShadow.class})
public class ServiceEditDialogFragmentTest extends BaseUnitTest {

    private ActivityController<FragmentUtilActivityUsingServiceActionListener> controller;

    @InjectMocks
    private FragmentUtilActivityUsingServiceActionListener activity;

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
        MockitoAnnotations.initMocks(this);

        Mockito.doReturn(allSharedPreferences).when(userService).getAllSharedPreferences();
        Mockito.doReturn(userService).when(context_).userService();

        Mockito.doReturn(5).when(allSharedPreferences).getDBEncryptionVersion();
        Mockito.doReturn(allSharedPreferences).when(context_).allSharedPreferences();
        CoreLibrary.init(context_);

        Mockito.doReturn(properties).when(context_).getAppProperties();

        activity = Robolectric.buildActivity(FragmentUtilActivityUsingServiceActionListener.class).create().start().get();
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
    public void assertThatCallToNewInstanceCreatesAFragment() {
        View view = Mockito.mock(View.class);
        Assert.assertNotNull(ServiceEditDialogFragment.newInstance(Collections.EMPTY_LIST, new ServiceWrapper(), view));
        Assert.assertNotNull(ServiceEditDialogFragment
                .newInstance(new DateTime(), Collections.EMPTY_LIST, new ServiceWrapper(), view, true));
    }

    @Test
    public void assertOnCreateViewTestSetsUpTheActivity() throws Exception {
        destroyController();
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), FragmentUtilActivityUsingServiceActionListener.class);
        controller = Robolectric.buildActivity(FragmentUtilActivityUsingServiceActionListener.class, intent);
        activity = controller.get();
        controller.setup();
        junit.framework.Assert.assertNotNull(activity);
    }

    @Test
    public void testSetFilterTouchesWhenObscuredSetsFlagToTrue() {

        List<Fragment> fragmentList = activity.getSupportFragmentManager().getFragments();

        Assert.assertNotNull(fragmentList);
        Assert.assertTrue(fragmentList.size() > 0);

        ServiceEditDialogFragment fragment = (ServiceEditDialogFragment) fragmentList.get(0);
        Assert.assertNotNull(fragment);

        boolean isEnabled = fragment.getView().getFilterTouchesWhenObscured();
        Assert.assertTrue(isEnabled);

    }
}
