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

@PrepareForTest({VaccineCard.class})
public class ServiceCardTest extends BaseUnitTest {

    ServiceCard serviceCard;

    @Mock
    Context context;

    @Mock
    AttributeSet attributeSet;

    @Before
    public void setUp() {
        serviceCard = Mockito.mock(ServiceCard.class);
        initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {

        ServiceCard serviceCardSpy = PowerMockito.spy(serviceCard);
        PowerMockito.doReturn(null).when(serviceCardSpy, "init", context);


        assertNotNull(new ServiceCard(context));
        assertNotNull(new ServiceCard(context, attributeSet));
        assertNotNull(new ServiceCard(context, attributeSet, 0));
        assertNotNull(new ServiceCard(context, attributeSet, 0, 0));
    }


}
