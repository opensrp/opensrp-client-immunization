package org.smartregister.immunization.domain;

import org.junit.Assert;
import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineTest extends BaseUnitTest {

    public static final String BASEENTITYID = "baseEntityId";
    public static final String BASEENTITYID2 = "baseEntityId2";
    public static final String NAME = "name";
    public static final String NAME2 = "name2";
    public static final String ANMID = "anmId";
    public static final String ANMID1 = "anmId1";
    public static final String LOCATIONID = "locationId";
    public static final String LOCATIONID1 = "locationId1";
    public static final String SYNCSTATUS = "syncStatus";
    public static final String SYNCSTATUS1 = "syncStatus1";
    public static final String HIA2STATUS = "hia2Status";
    public static final String HIA2STATUS1 = "hia2Status1";
    public static final String EVENTID = "eventId";
    public static final String EVENTID1 = "eventId1";
    public static final String FORMSUBMISSIONID = "formSubmissionId";
    public static final String FORMSUBMISSIONID1 = "formSubmissionId1";
    public static final String PROGRAMCLIENTID = "programClientid";

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        Assert.assertNotNull(new Vaccine());
        Assert.assertNotNull(new Vaccine(0l, BASEENTITYID, NAME, 0, new Date(),
                ANMID, LOCATIONID, SYNCSTATUS, HIA2STATUS, 0l, EVENTID, FORMSUBMISSIONID));
        Assert.assertNotNull(new Vaccine(0l, BASEENTITYID, NAME, 0, new Date(),
                ANMID, LOCATIONID, SYNCSTATUS, HIA2STATUS, 0l, EVENTID, FORMSUBMISSIONID, 0));
        Assert.assertNotNull(new Vaccine(0l, BASEENTITYID, PROGRAMCLIENTID, NAME, 0, new Date(),
                ANMID, LOCATIONID, SYNCSTATUS, HIA2STATUS, 0l, EVENTID, FORMSUBMISSIONID, 0, new Date(), 1));

        Assert.assertNotNull(new Vaccine(0l, BASEENTITYID, PROGRAMCLIENTID, NAME, 0, new Date(),
                ANMID, LOCATIONID, SYNCSTATUS, HIA2STATUS, 0l, EVENTID, FORMSUBMISSIONID, 0, new Date(), 1,1));
    }

    @Test
    public void assetGetIDwillReturnID() {
        Vaccine vaccine = new Vaccine(0l, BASEENTITYID, NAME, 0, new Date(),
                ANMID, LOCATIONID, SYNCSTATUS, HIA2STATUS, 0l, EVENTID, FORMSUBMISSIONID, 0);
        Assert.assertEquals(0l, vaccine.getId().longValue());
        vaccine.setId(1l);
        Assert.assertEquals(1l, vaccine.getId().longValue());
    }

    @Test
    public void assetGetBaseEntityIDwillReturnBaseEntityID() {
        Vaccine vaccine = new Vaccine(0l, BASEENTITYID, NAME, 0, new Date(),
                ANMID, LOCATIONID, SYNCSTATUS, HIA2STATUS, 0l, EVENTID, FORMSUBMISSIONID, 0);
        Assert.assertEquals(BASEENTITYID, vaccine.getBaseEntityId());
        vaccine.setBaseEntityId(BASEENTITYID2);
        Assert.assertEquals(BASEENTITYID2, vaccine.getBaseEntityId());
    }

    @Test
    public void assetGetNamewillReturnName() {
        Vaccine vaccine = new Vaccine(0l, BASEENTITYID, NAME, 0, new Date(),
                ANMID, LOCATIONID, SYNCSTATUS, HIA2STATUS, 0l, EVENTID, FORMSUBMISSIONID, 0);
        Assert.assertEquals(NAME, vaccine.getName());
        vaccine.setName(NAME2);
        Assert.assertEquals(NAME2, vaccine.getName());
    }

    @Test
    public void assetGetDatewillReturnDate() {
        Date date = new Date();
        Vaccine vaccine = new Vaccine(0l, BASEENTITYID, NAME, 0, date,
                ANMID, LOCATIONID, SYNCSTATUS, HIA2STATUS, 0l, EVENTID, FORMSUBMISSIONID, 0);
        Assert.assertEquals(date, vaccine.getDate());
        Date date2 = new Date();
        vaccine.setDate(date2);
        Assert.assertEquals(date2, vaccine.getDate());
    }

    @Test
    public void assetTestallgettersandsetters() {
        Date date = new Date();
        Vaccine vaccine = new Vaccine(0l, BASEENTITYID, NAME, 0, date,
                ANMID, LOCATIONID, SYNCSTATUS, HIA2STATUS, 0l, EVENTID, FORMSUBMISSIONID, 0);
        Assert.assertEquals(0, vaccine.getCalculation().intValue());
        Assert.assertEquals(ANMID, vaccine.getAnmId());
        Assert.assertEquals(LOCATIONID, vaccine.getLocationId());
        Assert.assertEquals(SYNCSTATUS, vaccine.getSyncStatus());
        Assert.assertEquals(HIA2STATUS, vaccine.getHia2Status());
        Assert.assertEquals(0l, vaccine.getUpdatedAt().longValue());
        Assert.assertEquals(EVENTID, vaccine.getEventId());
        Assert.assertEquals(FORMSUBMISSIONID, vaccine.getFormSubmissionId());
        Assert.assertEquals(0, vaccine.getOutOfCatchment().intValue());

        vaccine.setCalculation(1);
        Assert.assertEquals(1, vaccine.getCalculation().intValue());

        vaccine.setAnmId(ANMID1);
        Assert.assertEquals(ANMID1, vaccine.getAnmId());

        vaccine.setLocationId(LOCATIONID1);
        Assert.assertEquals(LOCATIONID1, vaccine.getLocationId());

        vaccine.setSyncStatus(SYNCSTATUS1);
        Assert.assertEquals(SYNCSTATUS1, vaccine.getSyncStatus());

        vaccine.setHia2Status(HIA2STATUS1);
        Assert.assertEquals(HIA2STATUS1, vaccine.getHia2Status());

        vaccine.setUpdatedAt(1l);
        Assert.assertEquals(1l, vaccine.getUpdatedAt().longValue());

        vaccine.setEventId(EVENTID1);
        Assert.assertEquals(EVENTID1, vaccine.getEventId());

        vaccine.setFormSubmissionId(FORMSUBMISSIONID1);
        Assert.assertEquals(FORMSUBMISSIONID1, vaccine.getFormSubmissionId());

        vaccine.setOutOfCatchment(1);
        Assert.assertEquals(1, vaccine.getOutOfCatchment().intValue());

        vaccine.setOutreach(1);
        Assert.assertEquals(1,vaccine.getOutreach().intValue());
        vaccine.setProgramClientId(PROGRAMCLIENTID);
        Assert.assertEquals(PROGRAMCLIENTID, vaccine.getProgramClientId());
    }

    @Test
    public void assetIdentifiersFromCLientID() {
        Vaccine vaccine = new Vaccine();
        vaccine.setProgramClientId(PROGRAMCLIENTID);
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("ZEIR_ID", PROGRAMCLIENTID);
        Assert.assertEquals(hashMap, vaccine.getIdentifiers());
    }

}
