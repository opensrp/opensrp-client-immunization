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

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        assertNotNull(new ServiceWrapper());
    }


    @Test
    public void assetTestallgettersandsetters() {
        ServiceWrapper serviceWrapper = new ServiceWrapper();
        DateTime datetime = new DateTime();

        serviceWrapper.setId("ID");
        assertEquals("ID", serviceWrapper.getId());

        serviceWrapper.setDbKey(0l);
        assertEquals(0l, serviceWrapper.getDbKey().longValue());

        serviceWrapper.setStatus("status");
        assertEquals("status", serviceWrapper.getStatus());

        serviceWrapper.setVaccineDate(datetime);
        assertEquals(datetime, serviceWrapper.getVaccineDate());

        Alert alert = Mockito.mock(Alert.class);
        serviceWrapper.setAlert(alert);
        assertEquals(alert, serviceWrapper.getAlert());

        serviceWrapper.setDefaultName("defaultname");
        assertEquals("defaultname", serviceWrapper.getDefaultName());
        assertEquals("defaultname", serviceWrapper.getName());

        serviceWrapper.setPreviousVaccine("ID");
        assertEquals("ID", serviceWrapper.getPreviousVaccineId());

        serviceWrapper.setColor("color");
        assertEquals("color", serviceWrapper.getColor());

        serviceWrapper.setDob(datetime);
        assertEquals(datetime, serviceWrapper.getDob());

        ServiceType serviceType = new ServiceType();
        serviceType.setUnits("units");
        serviceType.setType("type");
        serviceType.setId(0l);
        serviceWrapper.setServiceType(serviceType);
        serviceType.setName("name");
        assertEquals("name", serviceWrapper.getName());
        assertEquals(serviceType, serviceWrapper.getServiceType());
        assertEquals(serviceType.getUnits(), serviceWrapper.getUnits());
        assertEquals(serviceType.getType(), serviceWrapper.getType());
        assertEquals(0l, serviceWrapper.getTypeId().longValue());

        serviceWrapper.setValue("value");
        assertEquals("value", serviceWrapper.getValue());

        serviceWrapper.setPatientName("patientName");
        assertEquals("patientName", serviceWrapper.getPatientName());

        serviceWrapper.setUpdatedVaccineDate(datetime,true);
        assertEquals(datetime, serviceWrapper.getVaccineDate());
        assertEquals(datetime, serviceWrapper.getUpdatedVaccineDate());
        assertEquals(true, serviceWrapper.isToday());
        assertEquals(datetime.toString("yyyy-MM-dd"), serviceWrapper.getUpdatedVaccineDateAsString());
        assertEquals(datetime.toString("yyyy-MM-dd"), serviceWrapper.getVaccineDateAsString());

        serviceWrapper.setPatientNumber("number");
        assertEquals("number", serviceWrapper.getPatientNumber());

        Photo photo = Mockito.mock(Photo.class);
        serviceWrapper.setPhoto(photo);
        assertEquals(photo, serviceWrapper.getPhoto());

        serviceWrapper.setGender("test");
        assertEquals("test", serviceWrapper.getGender());


        serviceWrapper.setSynced(true);
        assertEquals(true, serviceWrapper.isSynced());

    }





}
