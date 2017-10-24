package org.smartregister.immunization.utils;

import junit.framework.Assert;

import org.joda.time.DateTime;
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
    VaccinateFormSubmissionWrapper vaccineFormSubmisionWrapper;


    @Test
    public void constructorTest(){
        vaccineFormSubmisionWrapper = new VaccinateFormSubmissionWrapper(null,"1","form",null,"child");
        Assert.assertNotNull(vaccineFormSubmisionWrapper);
        VaccineWrapper wrapper = new VaccineWrapper();
        wrapper.setId(VaccineWrapperTest.ID);
        wrapper.setAlert(Mockito.mock(Alert.class));
        wrapper.setColor(VaccineWrapperTest.COLOR);
        wrapper.setUpdatedVaccineDate(new DateTime(),true);
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

        vaccineFormSubmisionWrapper.add(wrapper);
        vaccineFormSubmisionWrapper.remove(wrapper);
        org.junit.Assert.assertEquals(vaccineFormSubmisionWrapper.updates(),0);
        vaccineFormSubmisionWrapper.add(wrapper);
        int n = vaccineFormSubmisionWrapper.updates();
        org.junit.Assert.assertEquals(n,1);
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper.getEntityId());
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper.updates());
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper.map());
        org.junit.Assert.assertNull(vaccineFormSubmisionWrapper.updateFormSubmission());
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper.getFormName());
        org.junit.Assert.assertNotNull(vaccineFormSubmisionWrapper.getOverrides());

    }
}
