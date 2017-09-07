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

@PrepareForTest({ServiceRowGroup.class})
public class ServiceRowGroupTest extends BaseUnitTest {

    private ServiceRowGroup serviceRowGroup;

    @Mock
    private Context context;

    @Mock
    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        serviceRowGroup = Mockito.mock(ServiceRowGroup.class);
        initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {

        ServiceRowGroup serviceRowGroupSpy = PowerMockito.spy(serviceRowGroup);
        PowerMockito.doReturn(null).when(serviceRowGroupSpy, "init", context);


        assertNotNull(new ServiceRowGroup(context,true));
        assertNotNull(new ServiceRowGroup(context, attributeSet));
        assertNotNull(new ServiceRowGroup(context, attributeSet, 0));
        assertNotNull(new ServiceRowGroup(context, attributeSet, 0, 0));
    }


}
