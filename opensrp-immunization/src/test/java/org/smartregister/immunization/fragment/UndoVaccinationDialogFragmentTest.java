package org.smartregister.immunization.fragment;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.VaccineWrapper;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class UndoVaccinationDialogFragmentTest extends BaseUnitTest {

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        assertNotNull(UndoVaccinationDialogFragment.newInstance(new VaccineWrapper()));
    }
}
