package org.smartregister.immunization.domain;

import org.joda.time.DateTime;
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
        junit.framework.Assert.assertNotNull(new ServiceWrapper());
    }

    @Test
    public void assetTestallgettersandsetters() {
        ServiceWrapper serviceWrapper = new ServiceWrapper();
        DateTime datetime = new DateTime();

        serviceWrapper.setId(ID);
        junit.framework.Assert.assertEquals(ID, serviceWrapper.getId());

        serviceWrapper.setDbKey(0l);
        junit.framework.Assert.assertEquals(0l, serviceWrapper.getDbKey().longValue());

        serviceWrapper.setStatus(STATUS);
        junit.framework.Assert.assertEquals(STATUS, serviceWrapper.getStatus());

        serviceWrapper.setVaccineDate(datetime);
        junit.framework.Assert.assertEquals(datetime, serviceWrapper.getVaccineDate());

        Alert alert = Mockito.mock(Alert.class);
        serviceWrapper.setAlert(alert);
        junit.framework.Assert.assertEquals(alert, serviceWrapper.getAlert());

        serviceWrapper.setDefaultName(DEFAULTNAME);
        junit.framework.Assert.assertEquals(DEFAULTNAME, serviceWrapper.getDefaultName());
        junit.framework.Assert.assertEquals(DEFAULTNAME, serviceWrapper.getName());

        serviceWrapper.setPreviousVaccine(ID);
        junit.framework.Assert.assertEquals(ID, serviceWrapper.getPreviousVaccineId());

        serviceWrapper.setColor(COLOR);
        junit.framework.Assert.assertEquals(COLOR, serviceWrapper.getColor());

        serviceWrapper.setDob(datetime);
        junit.framework.Assert.assertEquals(datetime, serviceWrapper.getDob());

        ServiceType serviceType = new ServiceType();
        serviceType.setUnits("units");
        serviceType.setType("type");
        serviceType.setId(0l);
        serviceWrapper.setServiceType(serviceType);
        serviceType.setName(NAME);
        junit.framework.Assert.assertEquals(NAME, serviceWrapper.getName());
        junit.framework.Assert.assertEquals(serviceType, serviceWrapper.getServiceType());
        junit.framework.Assert.assertEquals(serviceType.getUnits(), serviceWrapper.getUnits());
        junit.framework.Assert.assertEquals(serviceType.getType(), serviceWrapper.getType());
        junit.framework.Assert.assertEquals(0l, serviceWrapper.getTypeId().longValue());

        serviceWrapper.setValue(VALUE);
        junit.framework.Assert.assertEquals(VALUE, serviceWrapper.getValue());

        serviceWrapper.setPatientName(PATIENTNAME);
        junit.framework.Assert.assertEquals(PATIENTNAME, serviceWrapper.getPatientName());

        serviceWrapper.setUpdatedVaccineDate(datetime, true);
        junit.framework.Assert.assertEquals(datetime, serviceWrapper.getVaccineDate());
        junit.framework.Assert.assertEquals(datetime, serviceWrapper.getUpdatedVaccineDate());
        junit.framework.Assert.assertEquals(true, serviceWrapper.isToday());
        junit.framework.Assert.assertEquals(datetime.toString(YYYY_MM_DD), serviceWrapper.getUpdatedVaccineDateAsString());
        junit.framework.Assert.assertEquals(datetime.toString(YYYY_MM_DD), serviceWrapper.getVaccineDateAsString());

        serviceWrapper.setPatientNumber(NUMBER);
        junit.framework.Assert.assertEquals(NUMBER, serviceWrapper.getPatientNumber());

        Photo photo = Mockito.mock(Photo.class);
        serviceWrapper.setPhoto(photo);
        junit.framework.Assert.assertEquals(photo, serviceWrapper.getPhoto());

        serviceWrapper.setGender(GENDER);
        junit.framework.Assert.assertEquals(GENDER, serviceWrapper.getGender());


        serviceWrapper.setSynced(true);
        junit.framework.Assert.assertEquals(true, serviceWrapper.isSynced());

    }

}
