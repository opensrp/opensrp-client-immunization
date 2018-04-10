package org.smartregister.immunization.fragment;

import org.junit.Assert;
import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import static org.junit.Assert.*;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 10/04/2018.
 */
public class ActivateChildStatusDialogFragmentTest {

    @Test
    public void newInstanceShouldCreateNonNullInstance() throws Exception {
        ActivateChildStatusDialogFragment activateChildStatusDialogFragment = ActivateChildStatusDialogFragment.newInstance("her", "inactive", 0);

        Assert.assertNotNull(activateChildStatusDialogFragment);
    }

}