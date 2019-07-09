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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OpenMRSDate getOpenmrs_date() {
        return openmrs_date;
    }

    public void setOpenmrs_date(OpenMRSDate openmrs_date) {
        this.openmrs_date = openmrs_date;
    }

    public OpenMRSCalculation getOpenmrs_calculate() {
        return openmrs_calculate;
    }

    public void setOpenmrs_calculate(OpenMRSCalculation openmrs_calculate) {
        this.openmrs_calculate = openmrs_calculate;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Map<String, Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(Map<String, Schedule> schedules) {
        this.schedules = schedules;
    }

    public String getVaccine_separator() {
        return vaccine_separator;
    }

    public void setVaccine_separator(String vaccine_separator) {
        this.vaccine_separator = vaccine_separator;
    }
}
