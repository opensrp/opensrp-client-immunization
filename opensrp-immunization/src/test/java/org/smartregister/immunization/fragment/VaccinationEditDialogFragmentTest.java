package org.smartregister.immunization.fragment;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.VaccineWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccinationEditDialogFragmentTest extends BaseUnitTest {

    @Mock
    private Context context;
    private View view;

    @Before
    public void setUp() throws Exception {
        view = org.powermock.api.mockito.PowerMockito.mock(LinearLayout.class);
    }

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        junit.framework.Assert.assertNotNull(VaccinationEditDialogFragment.newInstance(context, new Date(), Collections.EMPTY_LIST, new ArrayList<VaccineWrapper>(), view));
        junit.framework.Assert.assertNotNull(VaccinationEditDialogFragment.newInstance(context, new Date(), Collections.EMPTY_LIST, new ArrayList<VaccineWrapper>(), view,true));
    }
}
