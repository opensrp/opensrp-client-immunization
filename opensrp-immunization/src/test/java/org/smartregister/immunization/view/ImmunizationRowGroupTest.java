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

@PrepareForTest({ImmunizationRowGroup.class})
public class ImmunizationRowGroupTest extends BaseUnitTest {

    private ImmunizationRowGroup immunizationRowGroup;

    @Mock
    private Context context;

    @Mock
    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        immunizationRowGroup = Mockito.mock(ImmunizationRowGroup.class);
        initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {

        ImmunizationRowGroup immunizationRowGroupSpy = PowerMockito.spy(immunizationRowGroup);
        PowerMockito.doReturn(null).when(immunizationRowGroupSpy, "init", context);


        assertNotNull(new ImmunizationRowGroup(context, true));
        assertNotNull(new ImmunizationRowGroup(context, attributeSet));
        assertNotNull(new ImmunizationRowGroup(context, attributeSet, 0));
        assertNotNull(new ImmunizationRowGroup(context, attributeSet, 0, 0));
    }


}
