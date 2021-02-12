package org.smartregister.immunization;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.repository.Repository;
import org.smartregister.util.AppProperties;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2020-02-27
 */

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {27})
public class ImmunizationLibraryRobolectricTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        Context context = Mockito.mock(Context.class);
        Mockito.doReturn(new AppProperties()).when(context).getAppProperties();
        ImmunizationLibrary.init(context, Mockito.mock(Repository.class), Mockito.mock(CommonFtsObject.class), 1, 1);
    }

    @After
    public void tearDown() throws Exception {
        ReflectionHelpers.setStaticField(ImmunizationLibrary.class, "instance", null);
    }

    @Test
    public void getVaccineSyncTimeShouldReturnDefault12Hours() {
        Assert.assertEquals(12 * 60, ImmunizationLibrary.getInstance().getVaccineSyncTime());
    }
}
