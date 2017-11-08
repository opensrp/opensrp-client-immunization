package org.smartregister.immunization.utils;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.smartregister.domain.Alert;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.VaccinateFormSubmissionWrapper;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.VaccineWrapperTest;

/**
 * Created by real on 24/10/17.
 */

public class VaccinateFormSubmissionWrapperTest extends BaseUnitTest {

    @InjectMocks
    private VaccinateFormSubmissionWrapper vaccineFormSubmisionWrapper;
    private VaccineWrapper wrapper;

    @Test
    public void assertConstructorInitializationTest() {
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper);
    }

    @Test
    public void assertGetOvveridesReturnsFormOverieds() {
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper.getOverrides());
    }

    @Test
    public void assertGetFormNameReturnsFormName() {
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper.getFormName());
    }

    @Test
    public void assertUpdateFormSumissionReturnsFormSubmission() {
        org.junit.Assert.assertNull(vaccineFormSubmisionWrapper.updateFormSubmission());
    }

    @Test
    public void assertMapReturnsWrappersMap() {
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper.map());
    }

    @Test
    public void assertGetEntityIdReturnsEntityId() {
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper.getEntityId());
    }

    @Test
    public void assertUpdatesReturnsWrapperUpdateCount() {
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper.updates());
    }

    @Test
    public void assertUpdateWrappeCountrAfterAdingRemovingWapper() throws Exception {
        vaccineFormSubmisionWrapper.add(wrapper);
        vaccineFormSubmisionWrapper.remove(wrapper);
        org.junit.Assert.assertEquals(vaccineFormSubmisionWrapper.updates(), 0);
        vaccineFormSubmisionWrapper.add(wrapper);
        org.junit.Assert.assertEquals(vaccineFormSubmisionWrapper.updates(), 1);
    }

    @Before
    public void setUp() {
        vaccineFormSubmisionWrapper = new VaccinateFormSubmissionWrapper(null, "1", "form", null, "child");
        setWrapperForTest();
    }

    public void setWrapperForTest() {
        wrapper = new VaccineWrapper();
        wrapper.setId(VaccineWrapperTest.ID);
        wrapper.setAlert(Mockito.mock(Alert.class));
        wrapper.setColor(VaccineWrapperTest.COLOR);
        wrapper.setUpdatedVaccineDate(new DateTime(), true);
        wrapper.setGender(VaccineWrapperTest.GENDER);
        wrapper.setDbKey(0l);
        wrapper.setCompact(true);
        wrapper.setDefaultName(VaccineWrapperTest.DEFAULTNAME);
        wrapper.setExistingAge(VaccineWrapperTest.AGE);
        wrapper.setFormattedVaccineDate(VaccineWrapperTest.DD_MM_YYYY);
        wrapper.setPatientName(VaccineWrapperTest.PATIENTNAME);
        wrapper.setName(VaccineWrapperTest.NAME);
        wrapper.setSynced(true);
        wrapper.setVaccineDate(new DateTime());
        wrapper.setPhoto(Mockito.mock(Photo.class));
        wrapper.setPatientNumber(VaccineWrapperTest.NUMBER);
    }
}
