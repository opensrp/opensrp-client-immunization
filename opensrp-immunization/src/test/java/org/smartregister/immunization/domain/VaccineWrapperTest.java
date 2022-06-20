package org.smartregister.immunization.domain;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.domain.Alert;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.db.VaccineRepo;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineWrapperTest extends BaseUnitTest {

    public static final String ID = "ID";
    public static final String STATUS = "status";
    public static final String NAME = "name";
    public static final String COLOR = "color";
    public static final String DD_MM_YYYY = "dd-mm-yyyy";
    public static final String PATIENTNAME = "patientName";
    public static final String NUMBER = "number";
    public static final String DEFAULTNAME = "defaultname";
    public static final String GENDER = "gender";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String AGE = "5";

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        Assert.assertNotNull(new VaccineWrapper());
    }

    @Test
    public void assetTestallgettersandsetters() {
        VaccineWrapper vaccineWrapper = new VaccineWrapper();
        DateTime datetime = new DateTime();

        vaccineWrapper.setId(ID);
        Assert.assertEquals(ID, vaccineWrapper.getId());

        vaccineWrapper.setDbKey(0l);
        Assert.assertEquals(0l, vaccineWrapper.getDbKey().longValue());

        vaccineWrapper.setStatus(STATUS);
        Assert.assertEquals(STATUS, vaccineWrapper.getStatus());

        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.bcg);
        Assert.assertEquals(VaccineRepo.Vaccine.bcg, vaccineWrapper.getVaccine());


        vaccineWrapper.setVaccineDate(datetime);
        Assert.assertEquals(datetime, vaccineWrapper.getVaccineDate());

        Alert alert = Mockito.mock(Alert.class);
        vaccineWrapper.setAlert(alert);
        Assert.assertEquals(alert, vaccineWrapper.getAlert());

        vaccineWrapper.setName(NAME);
        Assert.assertEquals(NAME, vaccineWrapper.getName());

        vaccineWrapper.setPreviousVaccine(ID);
        Assert.assertEquals(ID, vaccineWrapper.getPreviousVaccineId());

        vaccineWrapper.setCompact(true);
        Assert.assertEquals(true, vaccineWrapper.isCompact());

        vaccineWrapper.setColor(COLOR);
        Assert.assertEquals(COLOR, vaccineWrapper.getColor());

        vaccineWrapper.setFormattedVaccineDate(DD_MM_YYYY);
        Assert.assertEquals(DD_MM_YYYY, vaccineWrapper.getFormattedVaccineDate());

        vaccineWrapper.setExistingAge(AGE);
        Assert.assertEquals(AGE, vaccineWrapper.getExistingAge());

        vaccineWrapper.setPatientName(PATIENTNAME);
        Assert.assertEquals(PATIENTNAME, vaccineWrapper.getPatientName());

        vaccineWrapper.setUpdatedVaccineDate(datetime, true);
        Assert.assertEquals(datetime, vaccineWrapper.getVaccineDate());
        Assert.assertEquals(datetime, vaccineWrapper.getUpdatedVaccineDate());
        Assert.assertEquals(true, vaccineWrapper.isToday());
        Assert.assertEquals(datetime.toString(YYYY_MM_DD), vaccineWrapper.getUpdatedVaccineDateAsString());
        Assert.assertEquals(datetime.toString(YYYY_MM_DD), vaccineWrapper.getVaccineDateAsString());

        vaccineWrapper.setPatientNumber(NUMBER);
        Assert.assertEquals(NUMBER, vaccineWrapper.getPatientNumber());

        Photo photo = Mockito.mock(Photo.class);
        vaccineWrapper.setPhoto(photo);
        Assert.assertEquals(photo, vaccineWrapper.getPhoto());

        vaccineWrapper.setGender(GENDER);
        Assert.assertEquals(GENDER, vaccineWrapper.getGender());

        vaccineWrapper.setDefaultName(DEFAULTNAME);
        Assert.assertEquals(DEFAULTNAME, vaccineWrapper.getDefaultName());

        vaccineWrapper.setSynced(true);
        Assert.assertEquals(true, vaccineWrapper.isSynced());

    }

}
