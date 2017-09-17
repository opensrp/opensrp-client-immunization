package org.smartregister.immunization.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.VaccineWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

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

//    @Test
//    public void assertThatCallToCreateViewCreatesview() {
//        layoutInflater = Mockito.mock(LayoutInflater.class);
//        viewGroup = Mockito.mock(ViewGroup.class);
//        bundle = Mockito.mock(Bundle.class);
//        assertNotNull(ServiceDialogFragment.newInstance(Collections.EMPTY_LIST, new ServiceWrapper()).onCreateView(layoutInflater,viewGroup,bundle));
//    }
}
