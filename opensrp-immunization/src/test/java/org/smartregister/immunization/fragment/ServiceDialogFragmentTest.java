package org.smartregister.immunization.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.junit.Test;
import org.mockito.Mock;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceWrapper;

import java.util.Collections;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceDialogFragmentTest extends BaseUnitTest {

    @Mock
    LayoutInflater layoutInflater;

    @Mock
    ViewGroup viewGroup;

    @Mock
    Bundle bundle;

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        assertNotNull(ServiceDialogFragment.newInstance(Collections.EMPTY_LIST, new ServiceWrapper()));
    }

}
