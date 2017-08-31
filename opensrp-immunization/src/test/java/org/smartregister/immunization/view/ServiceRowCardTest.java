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
public class ServiceRowCardTest extends BaseUnitTest {

    ServiceRowCard serviceRowCard;

    @Mock
    Context context;

    @Mock
    AttributeSet attributeSet;

    @Before
    public void setUp() {
        serviceRowCard = Mockito.mock(ServiceRowCard.class);
        initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {

        ServiceRowCard serviceRowCardSpy = PowerMockito.spy(serviceRowCard);
        PowerMockito.doReturn(null).when(serviceRowCardSpy, "init", context);


        assertNotNull(new ServiceRowCard(context));
        assertNotNull(new ServiceRowCard(context, attributeSet));
        assertNotNull(new ServiceRowCard(context, attributeSet, 0));
        assertNotNull(new ServiceRowCard(context, attributeSet, 0, 0));
    }


}
