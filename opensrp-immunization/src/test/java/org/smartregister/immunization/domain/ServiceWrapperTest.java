package org.smartregister.immunization.domain;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.domain.Alert;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.BaseUnitTest;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceWrapperTest extends BaseUnitTest {

    public static final String ID = "ID";
    public static final String STATUS = "status";
    public static final String DEFAULTNAME = "defaultname";
    public static final String COLOR = "color";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String PATIENTNAME = "patientName";
    public static final String NUMBER = "number";
    public static final String GENDER = "gender";

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        Assert.assertNotNull(new ServiceWrapper());
    }

    @Test
    public void assetTestallgettersandsetters() {
        ServiceWrapper serviceWrapper = new ServiceWrapper();
        DateTime datetime = new DateTime();

        serviceWrapper.setId(ID);
        Assert.assertEquals(ID, serviceWrapper.getId());

        serviceWrapper.setDbKey(0l);
        Assert.assertEquals(0l, serviceWrapper.getDbKey().longValue());

        serviceWrapper.setStatus(STATUS);
        Assert.assertEquals(STATUS, serviceWrapper.getStatus());

        serviceWrapper.setVaccineDate(datetime);
        Assert.assertEquals(datetime, serviceWrapper.getVaccineDate());

        Alert alert = Mockito.mock(Alert.class);
        serviceWrapper.setAlert(alert);
        Assert.assertEquals(alert, serviceWrapper.getAlert());

        serviceWrapper.setDefaultName(DEFAULTNAME);
        Assert.assertEquals(DEFAULTNAME, serviceWrapper.getDefaultName());
        Assert.assertEquals(DEFAULTNAME, serviceWrapper.getName());

        serviceWrapper.setPreviousVaccine(ID);
        Assert.assertEquals(ID, serviceWrapper.getPreviousVaccineId());

        serviceWrapper.setColor(COLOR);
        Assert.assertEquals(COLOR, serviceWrapper.getColor());

        serviceWrapper.setDob(datetime);
        Assert.assertEquals(datetime, serviceWrapper.getDob());

        ServiceType serviceType = new ServiceType();
        serviceType.setUnits("units");
        serviceType.setType("type");
        serviceType.setId(0l);
        serviceWrapper.setServiceType(serviceType);
        serviceType.setName(NAME);
        Assert.assertEquals(NAME, serviceWrapper.getName());
        Assert.assertEquals(serviceType, serviceWrapper.getServiceType());
        Assert.assertEquals(serviceType.getUnits(), serviceWrapper.getUnits());
        Assert.assertEquals(serviceType.getType(), serviceWrapper.getType());
        Assert.assertEquals(0l, serviceWrapper.getTypeId().longValue());

        serviceWrapper.setValue(VALUE);
        Assert.assertEquals(VALUE, serviceWrapper.getValue());

        serviceWrapper.setPatientName(PATIENTNAME);
        Assert.assertEquals(PATIENTNAME, serviceWrapper.getPatientName());

        serviceWrapper.setUpdatedVaccineDate(datetime, true);
        Assert.assertEquals(datetime, serviceWrapper.getVaccineDate());
        Assert.assertEquals(datetime, serviceWrapper.getUpdatedVaccineDate());
        Assert.assertEquals(true, serviceWrapper.isToday());
        Assert.assertEquals(datetime.toString(YYYY_MM_DD), serviceWrapper.getUpdatedVaccineDateAsString());
        Assert.assertEquals(datetime.toString(YYYY_MM_DD), serviceWrapper.getVaccineDateAsString());

        serviceWrapper.setPatientNumber(NUMBER);
        Assert.assertEquals(NUMBER, serviceWrapper.getPatientNumber());

        Photo photo = Mockito.mock(Photo.class);
        serviceWrapper.setPhoto(photo);
        Assert.assertEquals(photo, serviceWrapper.getPhoto());

        serviceWrapper.setGender(GENDER);
        Assert.assertEquals(GENDER, serviceWrapper.getGender());


        serviceWrapper.setSynced(true);
        Assert.assertEquals(true, serviceWrapper.isSynced());

    }

}
