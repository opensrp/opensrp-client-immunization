package org.smartregister.immunization.domain.jsonmapping;

import java.util.Map;

/**
 * Created by samuelgithengi on 2/27/18.
 */

public class Vaccine {

    public String name;

    public String type;

    public OpenMRSDate openmrs_date;

    public OpenMRSCalculation openmrs_calculate;

    public Schedule schedule;

    public Map<String, Schedule> schedules;

    public String vaccine_separator;

}
