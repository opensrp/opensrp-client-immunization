package org.smartregister.immunization.domain;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.domain.Alert;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.db.VaccineRepo;

import java.util.Date;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineWrapperTest extends BaseUnitTest {

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        assertNotNull(new VaccineWrapper());
    }


    @Test
    public void assetTestallgettersandsetters(){
        VaccineWrapper vaccineWrapper = new VaccineWrapper();
        DateTime datetime = new DateTime();

        vaccineWrapper.setId("ID");
        assertEquals("ID",vaccineWrapper.getId());

        vaccineWrapper.setDbKey(0l);
        assertEquals(0l,vaccineWrapper.getDbKey().longValue());

        vaccineWrapper.setStatus("status");
        assertEquals("status",vaccineWrapper.getStatus());

        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.bcg);
        assertEquals(VaccineRepo.Vaccine.bcg,vaccineWrapper.getVaccine());


        vaccineWrapper.setVaccineDate(datetime);
        assertEquals(datetime,vaccineWrapper.getVaccineDate());

        Alert alert = Mockito.mock(Alert.class);
        vaccineWrapper.setAlert(alert);
        assertEquals(alert,vaccineWrapper.getAlert());

        vaccineWrapper.setName("name");
        assertEquals("name",vaccineWrapper.getName());

        vaccineWrapper.setPreviousVaccine("ID");
        assertEquals("ID",vaccineWrapper.getPreviousVaccineId());

        vaccineWrapper.setCompact(true);
        assertEquals(true,vaccineWrapper.isCompact());

        vaccineWrapper.setColor("color");
        assertEquals("color",vaccineWrapper.getColor());

        vaccineWrapper.setFormattedVaccineDate("dd-mm-yyyy");
        assertEquals("dd-mm-yyyy",vaccineWrapper.getFormattedVaccineDate());

        vaccineWrapper.setExistingAge("5");
        assertEquals("5",vaccineWrapper.getExistingAge());

        vaccineWrapper.setPatientName("patientName");
        assertEquals("patientName",vaccineWrapper.getPatientName());

        vaccineWrapper.setUpdatedVaccineDate(datetime,true);
        assertEquals(datetime,vaccineWrapper.getVaccineDate());
        assertEquals(datetime,vaccineWrapper.getUpdatedVaccineDate());
        assertEquals(true,vaccineWrapper.isToday());
        assertEquals(datetime.toString("yyyy-MM-dd"),vaccineWrapper.getUpdatedVaccineDateAsString());
        assertEquals(datetime.toString("yyyy-MM-dd"),vaccineWrapper.getVaccineDateAsString());

        vaccineWrapper.setPatientNumber("number");
        assertEquals("number",vaccineWrapper.getPatientNumber());

        Photo photo = Mockito.mock(Photo.class);
        vaccineWrapper.setPhoto(photo);
        assertEquals(photo,vaccineWrapper.getPhoto());

        vaccineWrapper.setGender("test");
        assertEquals("test",vaccineWrapper.getGender());

        vaccineWrapper.setDefaultName("defaultname");
        assertEquals("defaultname",vaccineWrapper.getDefaultName());

        vaccineWrapper.setSynced(true);
        assertEquals(true,vaccineWrapper.isSynced());

    }





}
