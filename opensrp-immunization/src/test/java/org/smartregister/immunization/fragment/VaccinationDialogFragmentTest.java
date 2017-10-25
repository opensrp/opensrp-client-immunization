package org.smartregister.immunization.fragment;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.VaccineWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccinationDialogFragmentTest extends BaseUnitTest {

    @Mock
    VaccinationDialogFragment vaccinationDialogFragment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        junit.framework.Assert.assertNotNull(VaccinationDialogFragment.newInstance(new Date(), Collections.EMPTY_LIST, new ArrayList<VaccineWrapper>()));
        junit.framework.Assert.assertNotNull(VaccinationDialogFragment.newInstance(new Date(), Collections.EMPTY_LIST, new ArrayList<VaccineWrapper>(),true));
    }

    @Test
    public void onCreateTest() {
        vaccinationDialogFragment.onCreate(Mockito.mock(Bundle.class));
    }
}
