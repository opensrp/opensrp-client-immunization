package org.smartregister.immunization.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.BuildConfig;
import org.smartregister.immunization.R;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

/**
 * Created by onaio on 30/08/2017.
 */
@PrepareForTest({ImmunizationRowGroupTest.class})
public class ImmunizationRowGroupTest extends BaseUnitTest {


    private ImmunizationRowGroup immunizationRowGroup;

    @Mock
    private Context context;

    @Mock
    private AttributeSet attributeSet;
    @Mock
    private LayoutInflater layoutInflater;

    @Before
    public void setUp() {
//        immunizationRowGroup = Mockito.mock(ImmunizationRowGroup.class);
//        ActivityController<Activity> activityController = Robolectric.buildActivity(Activity.class);
//        immunizationRowGroup = (ImmunizationRowGroup) LayoutInflater.from(activityController.get()).inflate(R.layout.view_immunization_row_group, null);
        immunizationRowGroup = Mockito.mock(ImmunizationRowGroup.class);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void constructorTest() throws Exception {
//        Context context = RuntimeEnvironment.application;

//        Context context = Mockito.mock(android.content.Context.class);
//

//
//        PowerMockito.when((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(layoutInflater);
//        PowerMockito.when(layoutInflater.inflate(anyInt(), any(ImmunizationRowGroup.class), anyBoolean())).thenReturn(Mockito.mock(View.class));
//        ImmunizationRowGroup immunizationRowGroup = PowerMockito.spy(new ImmunizationRowGroup(RuntimeEnvironment.application,true));
//        org.junit.Assert.assertNotNull(new ImmunizationRowGroup(context, true));
//        org.junit.Assert.assertNotNull(new ImmunizationRowGroup(context, attributeSet));
//        org.junit.Assert.assertNotNull(new ImmunizationRowGroup(context, attributeSet, 0));
//        org.junit.Assert.assertNotNull(new ImmunizationRowGroup(context, attributeSet, 0, 0));

//        ImmunizationRowGroup immunizationRowGroup = new ImmunizationRowGroup(context, true);
//        immunizationRowGroup.setAlertList(new ArrayList<Alert>());
//        immunizationRowGroup.setData(mock(JSONObject.class),mock(CommonPersonObjectClient.class),mock(List.class),mock(List.class));
//        immunizationRowGroup.setModalOpen(false);
//

        immunizationRowGroup.editmode = true;
        Assert.assertNotNull(immunizationRowGroup.getAlertList());
        Assert.assertNull(immunizationRowGroup.getChildDetails());
        Assert.assertNotNull(immunizationRowGroup.getDueVaccines());
        Assert.assertNotNull(immunizationRowGroup.getVaccineList());
        Assert.assertNull(immunizationRowGroup.getVaccineData());

    }

}
