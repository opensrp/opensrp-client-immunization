package org.smartregister.immunization.fragment;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import androidx.test.core.app.ApplicationProvider;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.R;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.fragment.mock.UndoServiceDialogFragmentTestActivity;
import org.smartregister.util.AppProperties;

import java.util.List;

import timber.log.Timber;

/**
 * Created by onaio on 30/08/2017.
 */
@Config(shadows = {FontTextViewShadow.class})
public class UndoServiceDialogFragmentTest extends BaseUnitTest {

    private ActivityController<UndoServiceDialogFragmentTestActivity> controller;

    @InjectMocks
    private UndoServiceDialogFragmentTestActivity activity;

    @Mock
    private org.smartregister.Context context_;

    @Mock
    private AppProperties properties;

    @Before
    public void setUp() {

        org.mockito.MockitoAnnotations.initMocks(this);

        Mockito.doReturn(properties).when(context_).getAppProperties();

        activity = Robolectric.buildActivity(UndoServiceDialogFragmentTestActivity.class).create().start().get();
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
            Timber.e(e);
        }

        System.gc();
    }

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        junit.framework.Assert.assertNotNull(UndoServiceDialogFragment.newInstance(new ServiceWrapper()));
    }

    @Test
    public void assertOnCreateViewTestSetsUpTheActivity() throws Exception {
        destroyController();
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), UndoServiceDialogFragmentTestActivity.class);
        controller = Robolectric.buildActivity(UndoServiceDialogFragmentTestActivity.class, intent);
        activity = controller.get();
        controller.setup();
        Assert.assertNotNull(activity);
    }

    @Test
    public void testSetFilterTouchesWhenObscuredSetsFlagToTrue() {

        List<Fragment> fragmentList = activity.getSupportFragmentManager().getFragments();

        Assert.assertNotNull(fragmentList);
        Assert.assertTrue(fragmentList.size() > 0);

        UndoServiceDialogFragment fragment = (UndoServiceDialogFragment) fragmentList.get(0);
        Assert.assertNotNull(fragment);

        boolean isEnabled = fragment.getView().getFilterTouchesWhenObscured();
        Assert.assertTrue(isEnabled);

    }
}
