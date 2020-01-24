package org.smartregister.immunization.domain;

import org.junit.Assert;
import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceRecordTest extends BaseUnitTest {
    public final static String BASEENTITYID = "baseEntityId";
    public final static String LOCATIONID = "locationID";
    public final static String SYNCED = "synced";
    public final static String EVENTID = "eventID";
    public final static String PROGRAMCLIENTID = "programClientID";
    public final static String NAME = "name";
    public final static String TYPE = "type";
    public static final String ANMID = "anmId";
    public static final String FORMSUBMISSIONID = "formSubmissionId";
    public static final String VALUE = "value";

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        Assert.assertNotNull(new ServiceRecord());
        Assert.assertNotNull(
                new ServiceRecord(0l, BASEENTITYID, 0l, VALUE, new Date(), ANMID, LOCATIONID, SYNCED, EVENTID,
                        FORMSUBMISSIONID, 0l));
        Assert.assertNotNull(
                new ServiceRecord(0l, BASEENTITYID, PROGRAMCLIENTID, 0l, VALUE, new Date(), ANMID, LOCATIONID, SYNCED,
                        EVENTID, FORMSUBMISSIONID, 0l, new Date()));
    }

    @Test
    public void assetTestallgettersandsetters() {
        ServiceRecord serviceRecord = new ServiceRecord();

        serviceRecord.setId(0l);
        Assert.assertEquals(0l, serviceRecord.getId().longValue());

        serviceRecord.setBaseEntityId(BASEENTITYID);
        Assert.assertEquals(BASEENTITYID, serviceRecord.getBaseEntityId());

        serviceRecord.setRecurringServiceId(0l);
        Assert.assertEquals(0l, serviceRecord.getRecurringServiceId().longValue());

        serviceRecord.setValue(VALUE);
        Assert.assertEquals(VALUE, serviceRecord.getValue());

        Date date = new Date();
        serviceRecord.setDate(date);
        Assert.assertEquals(date, serviceRecord.getDate());

        serviceRecord.setAnmId(ANMID);
        Assert.assertEquals(ANMID, serviceRecord.getAnmId());

        serviceRecord.setLocationId(LOCATIONID);
        Assert.assertEquals(LOCATIONID, serviceRecord.getLocationId());

        serviceRecord.setSyncStatus(SYNCED);
        Assert.assertEquals(SYNCED, serviceRecord.getSyncStatus());

        serviceRecord.setEventId(EVENTID);
        Assert.assertEquals(EVENTID, serviceRecord.getEventId());

        serviceRecord.setFormSubmissionId(FORMSUBMISSIONID);
        Assert.assertEquals(FORMSUBMISSIONID, serviceRecord.getFormSubmissionId());

        serviceRecord.setUpdatedAt(0l);
        Assert.assertEquals(0l, serviceRecord.getUpdatedAt().longValue());

        serviceRecord.setProgramClientId(PROGRAMCLIENTID);
        Assert.assertEquals(PROGRAMCLIENTID, serviceRecord.getProgramClientId());

        HashMap<String, String> identifiers = new HashMap<>();
        identifiers.put("ZEIR_ID", PROGRAMCLIENTID);
        Assert.assertEquals(identifiers, serviceRecord.getIdentifiers());

        serviceRecord.setName(NAME);
        Assert.assertEquals(NAME, serviceRecord.getName());

        serviceRecord.setType(TYPE);
        Assert.assertEquals(TYPE, serviceRecord.getType());

        Date DATE = new Date();
        serviceRecord.setCreatedAt(DATE);
        Assert.assertEquals(DATE, serviceRecord.getCreatedAt());

        String TEAM = "team";
        serviceRecord.setTeam(TEAM);
        Assert.assertEquals(TEAM, serviceRecord.getTeam());

        String TEAM_ID = "team_id";
        serviceRecord.setTeamId(TEAM_ID);
        Assert.assertEquals(TEAM_ID, serviceRecord.getTeamId());

        String CHILD_LOCATION_ID = "child_location_id";
        serviceRecord.setChildLocationId(CHILD_LOCATION_ID);
        Assert.assertEquals(CHILD_LOCATION_ID, serviceRecord.getChildLocationId());
    }

}
