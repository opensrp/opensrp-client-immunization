package org.smartregister.immunization.domain;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import java.util.Date;
import java.util.HashMap;


/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceRecordTest extends BaseUnitTest {

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        junit.framework.Assert.assertNotNull(new ServiceRecord());
        junit.framework.Assert.assertNotNull(new ServiceRecord(0l, "baseEntityId", 0l, "value", new Date(), "anmId", "locationId", "syncStatus", "eventId", "formSubmissionId", 0l));
        junit.framework.Assert.assertNotNull(new ServiceRecord(0l, "baseEntityId", "programClientId", 0l, "value", new Date(), "anmId", "locationId", "syncStatus", "eventId", "formSubmissionId", 0l));
    }

    @Test
    public void assetTestallgettersandsetters(){
        ServiceRecord serviceRecord = new ServiceRecord();

        serviceRecord.setId(0l);
        junit.framework.Assert.assertEquals(0l,serviceRecord.getId().longValue());

        serviceRecord.setBaseEntityId( "baseEntityId");
        junit.framework.Assert.assertEquals( "baseEntityId", serviceRecord.getBaseEntityId());

        serviceRecord.setRecurringServiceId(0l);
        junit.framework.Assert.assertEquals(0l, serviceRecord.getRecurringServiceId().longValue());

        serviceRecord.setValue("value");
        junit.framework.Assert.assertEquals("value", serviceRecord.getValue());

        Date date = new Date();
        serviceRecord.setDate(date);
        junit.framework.Assert.assertEquals(date, serviceRecord.getDate());

        serviceRecord.setAnmId("anmId");
        junit.framework.Assert.assertEquals("anmId", serviceRecord.getAnmId());

        serviceRecord.setLocationId("locationID");
        junit.framework.Assert.assertEquals("locationID", serviceRecord.getLocationId());

        serviceRecord.setSyncStatus("synced");
        junit.framework.Assert.assertEquals("synced", serviceRecord.getSyncStatus());

        serviceRecord.setEventId("eventID");
        junit.framework.Assert.assertEquals("eventID", serviceRecord.getEventId());

        serviceRecord.setFormSubmissionId("formSubmissionId");
        junit.framework.Assert.assertEquals("formSubmissionId", serviceRecord.getFormSubmissionId());

        serviceRecord.setUpdatedAt(0l);
        junit.framework.Assert.assertEquals(0l, serviceRecord.getUpdatedAt().longValue());

        serviceRecord.setProgramClientId("programClientID");
        junit.framework.Assert.assertEquals("programClientID", serviceRecord.getProgramClientId());

        HashMap<String, String> identifiers = new HashMap<>();
        identifiers.put("ZEIR_ID", "programClientID");
        junit.framework.Assert.assertEquals(identifiers, serviceRecord.getIdentifiers());

        serviceRecord.setName("name");
        junit.framework.Assert.assertEquals("name", serviceRecord.getName());

        serviceRecord.setType("type");
        junit.framework.Assert.assertEquals("type", serviceRecord.getType());
    }





}
