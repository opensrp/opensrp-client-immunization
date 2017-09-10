package org.smartregister.immunization.domain;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import java.util.Date;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceRecordTest extends BaseUnitTest {

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        assertNotNull(new ServiceRecord());
        assertNotNull(new ServiceRecord(0l, "baseEntityId", 0l, "value",
                new Date(),"anmId", "locationId","syncStatus","eventId","formSubmissionId", 0l));
        assertNotNull(new ServiceRecord(0l, "baseEntityId", "programClientId", 0l, "value",new Date(), "anmId", "locationId","syncStatus","eventId","formSubmissionId", 0l));
    }

    @Test
    public void assetTestallgettersandsetters(){
        ServiceRecord serviceRecord = new ServiceRecord();

        serviceRecord.setId(0l);
        assertEquals(0l,serviceRecord.getId().longValue());

        serviceRecord.setBaseEntityId( "baseEntityId");
        assertEquals( "baseEntityId",serviceRecord.getBaseEntityId());

        serviceRecord.setRecurringServiceId(0l);
        assertEquals(0l,serviceRecord.getRecurringServiceId().longValue());

        serviceRecord.setValue("value");
        assertEquals("value",serviceRecord.getValue());

        Date date = new Date();
        serviceRecord.setDate(date);
        assertEquals(date,serviceRecord.getDate());

        serviceRecord.setAnmId("anmId");
        assertEquals("anmId",serviceRecord.getAnmId());

        serviceRecord.setLocationId("locationID");
        assertEquals("locationID",serviceRecord.getLocationId());

        serviceRecord.setSyncStatus("synced");
        assertEquals("synced",serviceRecord.getSyncStatus());

        serviceRecord.setEventId("eventID");
        assertEquals("eventID",serviceRecord.getEventId());

        serviceRecord.setFormSubmissionId("formSubmissionId");
        assertEquals("formSubmissionId",serviceRecord.getFormSubmissionId());

        serviceRecord.setUpdatedAt(0l);
        assertEquals(0l,serviceRecord.getUpdatedAt().longValue());

        serviceRecord.setProgramClientId("programClientID");
        assertEquals("programClientID",serviceRecord.getProgramClientId());

        HashMap<String, String> identifiers = new HashMap<>();
        identifiers.put("ZEIR_ID","programClientID");
        assertEquals(identifiers,serviceRecord.getIdentifiers());

        serviceRecord.setName("name");
        assertEquals("name",serviceRecord.getName());

        serviceRecord.setType("type");
        assertEquals("type",serviceRecord.getType());
    }





}
