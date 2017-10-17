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

/**
 * Created by onaio on 30/08/2017.
 */

@PrepareForTest({ServiceGroup.class})
public class ServiceGroupTest extends BaseUnitTest {

    private ServiceGroup serviceGroup;

    @Mock
    private Context context;

    @Mock
    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        serviceGroup = Mockito.mock(ServiceGroup.class);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {

        ServiceGroup serviceGroupSpy = PowerMockito.spy(serviceGroup);
        PowerMockito.doReturn(null).when(serviceGroupSpy, "init", context);
        org.junit.Assert.assertNotNull(new ServiceGroup(context));
        org.junit.Assert.assertNotNull(new ServiceGroup(context, attributeSet));
        org.junit.Assert.assertNotNull(new ServiceGroup(context, attributeSet, 0));
        org.junit.Assert.assertNotNull(new ServiceGroup(context, attributeSet, 0, 0));
    }
    
}
