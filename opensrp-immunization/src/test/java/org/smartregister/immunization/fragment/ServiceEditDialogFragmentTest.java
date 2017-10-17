package org.smartregister.immunization.fragment;

import android.view.View;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceWrapper;

import java.util.Collections;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceEditDialogFragmentTest extends BaseUnitTest {
    private View view;

    @Before
    public void setUp() throws Exception {
        view = org.powermock.api.mockito.PowerMockito.mock(LinearLayout.class);
    }

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        junit.framework.Assert.assertNotNull(ServiceEditDialogFragment.newInstance(Collections.EMPTY_LIST, new ServiceWrapper(), view));
    }
}
