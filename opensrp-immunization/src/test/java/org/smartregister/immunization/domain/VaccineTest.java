package org.smartregister.immunization.domain;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.service.intent.VaccineIntentService;

import java.util.Date;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineTest extends BaseUnitTest {

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        assertNotNull(new Vaccine());
        assertNotNull(new Vaccine(0l, "baseEntityId", "name", 0, new Date(),
                "anmId", "locationId", "syncStatus", "hia2Status", 0l, "eventId", "formSubmissionId", 0));
        assertNotNull(new Vaccine(0l, "baseEntityId","programClientid", "name", 0, new Date(),
                "anmId", "locationId", "syncStatus", "hia2Status", 0l, "eventId", "formSubmissionId", 0));
    }

    @Test
    public void assetGetIDwillReturnID(){
        Vaccine vaccine = new Vaccine(0l, "baseEntityId", "name", 0, new Date(),
                "anmId", "locationId", "syncStatus", "hia2Status", 0l, "eventId", "formSubmissionId", 0);
        assertEquals(0l,vaccine.getId().longValue());
        vaccine.setId(1l);
        assertEquals(1l,vaccine.getId().longValue());
    }

    @Test
    public void assetGetBaseEntityIDwillReturnBaseEntityID(){
        Vaccine vaccine = new Vaccine(0l, "BaseEntityID", "name", 0, new Date(),
                "anmId", "locationId", "syncStatus", "hia2Status", 0l, "eventId", "formSubmissionId", 0);
        assertEquals("BaseEntityID",vaccine.getBaseEntityId());
        vaccine.setBaseEntityId("BaseEntityID2");
        assertEquals("BaseEntityID2",vaccine.getBaseEntityId());
    }

    @Test
    public void assetGetNamewillReturnName(){
        Vaccine vaccine = new Vaccine(0l, "BaseEntityID", "name", 0, new Date(),
                "anmId", "locationId", "syncStatus", "hia2Status", 0l, "eventId", "formSubmissionId", 0);
        assertEquals("name",vaccine.getName());
        vaccine.setName("Name2");
        assertEquals("Name2",vaccine.getName());
    }

    @Test
    public void assetGetDatewillReturnDate(){
        Date date = new Date();
        Vaccine vaccine = new Vaccine(0l, "BaseEntityID", "name", 0,date,
                "anmId", "locationId", "syncStatus", "hia2Status", 0l, "eventId", "formSubmissionId", 0);
        assertEquals(date,vaccine.getDate());
        Date date2 = new Date();
        vaccine.setDate(date2);
        assertEquals(date2,vaccine.getDate());
    }

    @Test
    public void assetTestallgettersandsetters(){
        Date date = new Date();
        Vaccine vaccine = new Vaccine(0l, "BaseEntityID", "name", 0,date,
                "anmId", "locationId", "syncStatus", "hia2Status", 0l, "eventId", "formSubmissionId", 0);
        assertEquals(0,vaccine.getCalculation().intValue());
        assertEquals("anmId",vaccine.getAnmId());
        assertEquals("locationId",vaccine.getLocationId());
        assertEquals("syncStatus",vaccine.getSyncStatus());
        assertEquals("hia2Status",vaccine.getHia2Status());
        assertEquals(0l,vaccine.getUpdatedAt().longValue());
        assertEquals("eventId",vaccine.getEventId());
        assertEquals("formSubmissionId",vaccine.getFormSubmissionId());
        assertEquals(0,vaccine.getOutOfCatchment().intValue());

        vaccine.setCalculation(1);
        assertEquals(1,vaccine.getCalculation().intValue());

        vaccine.setAnmId("anmId1");
        assertEquals("anmId1",vaccine.getAnmId());

        vaccine.setLocationId("locationId1");
        assertEquals("locationId1",vaccine.getLocationId());

        vaccine.setSyncStatus("syncStatus1");
        assertEquals("syncStatus1",vaccine.getSyncStatus());

        vaccine.setHia2Status("hia2Status1");
        assertEquals("hia2Status1",vaccine.getHia2Status());

        vaccine.setUpdatedAt(1l);
        assertEquals(1l,vaccine.getUpdatedAt().longValue());

        vaccine.setEventId("eventId1");
        assertEquals("eventId1",vaccine.getEventId());

        vaccine.setFormSubmissionId("formSubmissionId1");
        assertEquals("formSubmissionId1",vaccine.getFormSubmissionId());

        vaccine.setOutOfCatchment(1);
        assertEquals(1,vaccine.getOutOfCatchment().intValue());

        vaccine.setProgramClientId("programClientID");
        assertEquals("programClientID",vaccine.getProgramClientId());
    }

    @Test
    public void assetIdentifiersFromCLientID(){
        Vaccine vaccine = new Vaccine();
        vaccine.setProgramClientId("programClientID");
        HashMap<String,String> hashMap = new HashMap<String, String>();
        hashMap.put("ZEIR_ID","programClientID");
        assertEquals(hashMap,vaccine.getIdentifiers());
    }



}
