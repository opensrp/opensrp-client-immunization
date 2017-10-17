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

@PrepareForTest({ImmunizationRowCard.class})
public class ImmunizationRowCardTest extends BaseUnitTest {

    private ImmunizationRowCard immunizationRowCard;

    @Mock
    private Context context;

    @Mock
    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        immunizationRowCard = Mockito.mock(ImmunizationRowCard.class);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {
        ImmunizationRowCard immunizationRowCardSpy = PowerMockito.spy(immunizationRowCard);
        PowerMockito.doReturn(null).when(immunizationRowCardSpy, "init", context);
        org.junit.Assert.assertNotNull(new ImmunizationRowCard(context));
        org.junit.Assert.assertNotNull(new ImmunizationRowCard(context, attributeSet));
        org.junit.Assert.assertNotNull(new ImmunizationRowCard(context, attributeSet, 0));
        org.junit.Assert.assertNotNull(new ImmunizationRowCard(context, attributeSet, 0, 0));
    }

}
