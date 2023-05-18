package org.smartregister.immunization.domain;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

public class VaccinationClientTest extends BaseUnitTest {


    @Test
    public void testSetBaseEntityIdSetsTheCorrectValue() {
        VaccinationClient client = new VaccinationClient();
        client.setBaseEntityId("baseEntityId");
        Assert.assertEquals("baseEntityId", client.getBaseEntityId());
    }

    @Test
    public void testSetBirthDateTimeSetsTheCorrectValue() {
        VaccinationClient client = new VaccinationClient();
        DateTime dateTime = new DateTime();
        client.setBirthDateTime(dateTime);
        Assert.assertEquals(dateTime, client.getBirthDateTime());
    }

}
