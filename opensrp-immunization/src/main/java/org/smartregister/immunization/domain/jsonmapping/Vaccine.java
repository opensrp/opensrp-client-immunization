package org.smartregister.immunization.domain.jsonmapping;

import android.util.Log;

import java.util.Map;

/**
 * Created by samuelgithengi on 2/27/18.
 */

public class Vaccine implements Cloneable{

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

    public OpenMRSDate getOpenmrsDate() {
        return openmrs_date;
    }

    public void setOpenmrsDate(OpenMRSDate openmrs_date) {
        this.openmrs_date = openmrs_date;
    }

    public OpenMRSCalculation getOpenmrsCalculate() {
        return openmrs_calculate;
    }

    public void setOpenmrsCalculate(OpenMRSCalculation openmrs_calculate) {
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

    public String getVaccineSeparator() {
        return vaccine_separator;
    }

    public void setVaccineSeparator(String vaccine_separator) {
        this.vaccine_separator = vaccine_separator;
    }

    @Override
    public Vaccine clone() {
        try {
            return (Vaccine) super.clone();
        } catch (CloneNotSupportedException e) {
            Log.e(Vaccine.class.getCanonicalName(), e.getMessage());
            return null;
        }
    }
}
