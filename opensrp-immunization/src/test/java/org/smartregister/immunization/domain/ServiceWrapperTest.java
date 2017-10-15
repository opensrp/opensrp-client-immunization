package org.smartregister.immunization.domain;

import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.domain.Alert;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.BaseUnitTest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceWrapperTest extends BaseUnitTest {

    public static final String ID = "ID" ;
    public static final String STATUS = "status";
    public static final String DEFAULTNAME = "defaultname";
    public static final String COLOR = "color";
    public static final String NAME = "name";
    public static final String VALUE = "value";
    public static final String YYYY_MM_DD = "yyyy-MM-dd" ;
    public static final String PATIENTNAME = "patientName";
    public static final String NUMBER = "number";
    public static final String GENDER = "gender" ;

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        assertNotNull(new ServiceWrapper());
    }

    @Test
    public void assetTestallgettersandsetters() {
        ServiceWrapper serviceWrapper = new ServiceWrapper();
        DateTime datetime = new DateTime();

        serviceWrapper.setId(ID);
        assertEquals(ID, serviceWrapper.getId());

        serviceWrapper.setDbKey(0l);
        assertEquals(0l, serviceWrapper.getDbKey().longValue());

        serviceWrapper.setStatus(STATUS);
        assertEquals(STATUS, serviceWrapper.getStatus());

        serviceWrapper.setVaccineDate(datetime);
        assertEquals(datetime, serviceWrapper.getVaccineDate());

        Alert alert = Mockito.mock(Alert.class);
        serviceWrapper.setAlert(alert);
        assertEquals(alert, serviceWrapper.getAlert());

        serviceWrapper.setDefaultName(DEFAULTNAME);
        assertEquals(DEFAULTNAME, serviceWrapper.getDefaultName());
        assertEquals(DEFAULTNAME, serviceWrapper.getName());

        serviceWrapper.setPreviousVaccine(ID);
        assertEquals(ID, serviceWrapper.getPreviousVaccineId());

        serviceWrapper.setColor(COLOR);
        assertEquals(COLOR, serviceWrapper.getColor());

        serviceWrapper.setDob(datetime);
        assertEquals(datetime, serviceWrapper.getDob());

        ServiceType serviceType = new ServiceType();
        serviceType.setUnits("units");
        serviceType.setType("type");
        serviceType.setId(0l);
        serviceWrapper.setServiceType(serviceType);
        serviceType.setName(NAME);
        assertEquals(NAME, serviceWrapper.getName());
        assertEquals(serviceType, serviceWrapper.getServiceType());
        assertEquals(serviceType.getUnits(), serviceWrapper.getUnits());
        assertEquals(serviceType.getType(), serviceWrapper.getType());
        assertEquals(0l, serviceWrapper.getTypeId().longValue());

        serviceWrapper.setValue(VALUE);
        assertEquals(VALUE, serviceWrapper.getValue());

        serviceWrapper.setPatientName(PATIENTNAME);
        assertEquals(PATIENTNAME, serviceWrapper.getPatientName());

        serviceWrapper.setUpdatedVaccineDate(datetime, true);
        assertEquals(datetime, serviceWrapper.getVaccineDate());
        assertEquals(datetime, serviceWrapper.getUpdatedVaccineDate());
        assertEquals(true, serviceWrapper.isToday());
        assertEquals(datetime.toString(YYYY_MM_DD), serviceWrapper.getUpdatedVaccineDateAsString());
        assertEquals(datetime.toString(YYYY_MM_DD), serviceWrapper.getVaccineDateAsString());

        serviceWrapper.setPatientNumber(NUMBER);
        assertEquals(NUMBER, serviceWrapper.getPatientNumber());

        Photo photo = Mockito.mock(Photo.class);
        serviceWrapper.setPhoto(photo);
        assertEquals(photo, serviceWrapper.getPhoto());

        serviceWrapper.setGender(GENDER);
        assertEquals(GENDER, serviceWrapper.getGender());


        serviceWrapper.setSynced(true);
        assertEquals(true, serviceWrapper.isSynced());

    }

}
