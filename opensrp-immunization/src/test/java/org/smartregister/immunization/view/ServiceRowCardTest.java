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

@PrepareForTest({ServiceRowCard.class})
public class ServiceRowCardTest extends BaseUnitTest {

    private ServiceRowCard serviceRowCard;

    @Mock
    private Context context;

    @Mock
    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        serviceRowCard = Mockito.mock(ServiceRowCard.class);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {

        ServiceRowCard serviceRowCardSpy = PowerMockito.spy(serviceRowCard);
        PowerMockito.doReturn(null).when(serviceRowCardSpy, "init", context);
        org.junit.Assert.assertNotNull(new ServiceRowCard(context));
        org.junit.Assert.assertNotNull(new ServiceRowCard(context, attributeSet));
        org.junit.Assert.assertNotNull(new ServiceRowCard(context, attributeSet, 0));
        org.junit.Assert.assertNotNull(new ServiceRowCard(context, attributeSet, 0, 0));
    }

}
