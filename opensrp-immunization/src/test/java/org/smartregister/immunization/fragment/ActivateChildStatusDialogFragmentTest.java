package org.smartregister.immunization.fragment;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 10/04/2018.
 */
public class ActivateChildStatusDialogFragmentTest {

    @Test
    public void newInstanceShouldCreateNonNullInstance() throws Exception {
        ActivateChildStatusDialogFragment activateChildStatusDialogFragment = ActivateChildStatusDialogFragment.newInstance("her", "inactive", 0);

        assertNotNull(activateChildStatusDialogFragment);
    }

}