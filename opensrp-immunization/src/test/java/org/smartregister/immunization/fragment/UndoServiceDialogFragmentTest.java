package org.smartregister.immunization.fragment;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceWrapper;

/**
 * Created by onaio on 30/08/2017.
 */

public class UndoServiceDialogFragmentTest extends BaseUnitTest {

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        junit.framework.Assert.assertNotNull(UndoServiceDialogFragment.newInstance(new ServiceWrapper()));
    }
}
