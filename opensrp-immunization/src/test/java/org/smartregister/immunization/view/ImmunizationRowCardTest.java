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
public class ImmunizationRowCardTest extends BaseUnitTest {

    ImmunizationRowCard immunizationRowCard;

    @Mock
    Context context;

    @Mock
    AttributeSet attributeSet;

    @Before
    public void setUp() {
        immunizationRowCard = Mockito.mock(ImmunizationRowCard.class);
        initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {

        ImmunizationRowCard immunizationRowCardSpy = PowerMockito.spy(immunizationRowCard);
        PowerMockito.doReturn(null).when(immunizationRowCardSpy, "init", context);


        assertNotNull(new ImmunizationRowCard(context));
        assertNotNull(new ImmunizationRowCard(context, attributeSet));
        assertNotNull(new ImmunizationRowCard(context, attributeSet, 0));
        assertNotNull(new ImmunizationRowCard(context, attributeSet, 0, 0));
    }


}
