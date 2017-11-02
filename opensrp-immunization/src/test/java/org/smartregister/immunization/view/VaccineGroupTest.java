package org.smartregister.immunization.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.smartregister.immunization.BaseUnitTest;

/**
 * Created by onaio on 30/08/2017.
 */
@Config(manifest = "src/main/AndroidManifest.xml")
public class VaccineGroupTest extends BaseUnitTest {

    @Mock
    private VaccineGroup vaccineGroup;

    @Mock
    private Context context;

//    @Mock
//    private AttributeSet attributeSet;
//    private ActivityView activity;
    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
        //activity = Robolectric.setupActivity(ActivityView.class);
    }

    @Test
    public void assertVaccineGroupTest() throws Exception {
//        VaccineGroup vaccineGroupSpy = PowerMockito.spy(vaccineGroup);
//        PowerMockito.doReturn(null).when(vaccineGroupSpy, "init", context);
//        org.junit.Assert.assertNotNull(new VaccineGroup(context));
//        org.junit.Assert.assertNotNull(new VaccineGroup(context, attributeSet));
//        org.junit.Assert.assertNotNull(new VaccineGroup(context, attributeSet, 0));
//        org.junit.Assert.assertNotNull(new VaccineGroup(context, attributeSet, 0, 0));
//        Activity activity = Robolectric.buildActivity(ActivityView.class).create().get();

    }

}
