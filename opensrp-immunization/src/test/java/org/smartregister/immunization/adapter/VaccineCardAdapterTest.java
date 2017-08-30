package org.smartregister.immunization.adapter;

import android.content.Context;
import android.util.AttributeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.view.VaccineCard;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineCardAdapterTest extends BaseUnitTest {

    @Mock
    Context context;

    @Mock
    VaccineCardAdapter vaccineCardAdapter;
    @Mock
    AttributeSet attributeSet;

    @Before
    public void setUp() {
        vaccineCardAdapter = Mockito.mock(VaccineCardAdapter.class);
        initMocks(this);
    }

    @Test(expected = Exception.class)
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws Exception {

        VaccineCardAdapter vaccineCardSpy = PowerMockito.spy(vaccineCardAdapter);
        PowerMockito.doReturn(null).when(vaccineCardSpy, "init", context);


        assertNotNull(new VaccineCard(context));
        assertNotNull(new VaccineCard(context, attributeSet));
        assertNotNull(new VaccineCard(context, attributeSet, 0));
        assertNotNull(new VaccineCard(context, attributeSet, 0, 0));
    }

}
