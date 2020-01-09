package org.smartregister.immunization.domain.jsonmapping;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class VaccineTest {

    @Test
    public void assetTestallgettersandsetters() {
        Vaccine vaccine = new Vaccine();

        String name = "name";
        vaccine.setName(name);
        Assert.assertEquals(name, vaccine.getName());

        String type = "type";
        vaccine.setType(type);
        Assert.assertEquals(type, vaccine.getType());

        OpenMRSDate openmrs_date = new OpenMRSDate();
        vaccine.setOpenmrsDate(openmrs_date);
        Assert.assertEquals(openmrs_date, vaccine.getOpenmrsDate());

        OpenMRSCalculation openmrs_calculate = new OpenMRSCalculation();
        vaccine.setOpenmrsCalculate(openmrs_calculate);
        Assert.assertEquals(openmrs_calculate, vaccine.getOpenmrsCalculate());

        Schedule schedule = new Schedule();
        vaccine.setSchedule(schedule);
        Assert.assertEquals(schedule, vaccine.getSchedule());

        Map<String, Schedule> schedules = new HashMap<>();
        vaccine.setSchedules(schedules);
        Assert.assertEquals(schedules, vaccine.getSchedules());

        String vaccine_separator = "vaccine_separator";
        vaccine.setVaccineSeparator(vaccine_separator);
        Assert.assertEquals(vaccine_separator, vaccine.getVaccineSeparator());
    }

    @Test
    public void testCloning() {
        Vaccine vaccine = new Vaccine();
        vaccine.setName("asd");

        Vaccine vaccine1 = vaccine.clone();

        Assert.assertEquals(vaccine1.getName(), vaccine.getName());
        Assert.assertNotEquals(vaccine1, vaccine);
    }
}
