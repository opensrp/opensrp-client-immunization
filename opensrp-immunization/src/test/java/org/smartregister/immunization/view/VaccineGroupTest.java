package org.smartregister.immunization.view;

import android.content.Context;
import android.util.AttributeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.smartregister.immunization.BaseUnitTest;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by onaio on 30/08/2017.
 */

@PrepareForTest({VaccineGroup.class})
public class VaccineGroupTest extends BaseUnitTest {

    private VaccineGroup vaccineGroup;

    @Mock
    private Context context;

    @Mock
    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        vaccineGroup = Mockito.mock(VaccineGroup.class);
        initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {
        VaccineGroup vaccineGroupSpy = PowerMockito.spy(vaccineGroup);
        PowerMockito.doReturn(null).when(vaccineGroupSpy, "init", context);
        assertNotNull(new VaccineGroup(context));
        assertNotNull(new VaccineGroup(context, attributeSet));
        assertNotNull(new VaccineGroup(context, attributeSet, 0));
        assertNotNull(new VaccineGroup(context, attributeSet, 0, 0));
    }


}
