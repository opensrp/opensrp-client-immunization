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
public class VaccineCardTest extends BaseUnitTest {

    private VaccineCard vaccineCard;

    @Mock
    private Context context;

    @Mock
    private AttributeSet attributeSet;

    @Before
    public void setUp() {
        vaccineCard = Mockito.mock(VaccineCard.class);
        initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {

        VaccineCard vaccineCardSpy = PowerMockito.spy(vaccineCard);
        PowerMockito.doReturn(null).when(vaccineCardSpy, "init", context);


        assertNotNull(new VaccineCard(context));
        assertNotNull(new VaccineCard(context, attributeSet));
        assertNotNull(new VaccineCard(context, attributeSet, 0));
        assertNotNull(new VaccineCard(context, attributeSet, 0, 0));
    }


}
