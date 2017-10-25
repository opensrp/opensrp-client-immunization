package org.smartregister.immunization.fragment;

import org.joda.time.DateTime;
import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceWrapper;

import java.util.Collections;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceDialogFragmentTest extends BaseUnitTest {

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        junit.framework.Assert.assertNotNull(ServiceDialogFragment.newInstance(Collections.EMPTY_LIST, new ServiceWrapper()));
        junit.framework.Assert.assertNotNull(ServiceDialogFragment.newInstance(new DateTime(),Collections.EMPTY_LIST, new ServiceWrapper(),true));
    }

}
