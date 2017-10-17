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

@PrepareForTest({ServiceCard.class})
public class ServiceCardTest extends BaseUnitTest {

    private ServiceCard serviceCard;

    @Mock
    private Context context;

    @Mock
    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        serviceCard = Mockito.mock(ServiceCard.class);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {

        ServiceCard serviceCardSpy = PowerMockito.spy(serviceCard);
        PowerMockito.doReturn(null).when(serviceCardSpy, "init", context);
        org.junit.Assert.assertNotNull(new ServiceCard(context));
        org.junit.Assert.assertNotNull(new ServiceCard(context, attributeSet));
        org.junit.Assert.assertNotNull(new ServiceCard(context, attributeSet, 0));
        org.junit.Assert.assertNotNull(new ServiceCard(context, attributeSet, 0, 0));
    }

}
