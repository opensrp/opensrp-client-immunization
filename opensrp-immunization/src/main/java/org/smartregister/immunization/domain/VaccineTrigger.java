package org.smartregister.immunization.domain;

import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.jsonmapping.Due;
import org.smartregister.immunization.domain.jsonmapping.Expiry;
import org.smartregister.immunization.util.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jason Rogena - jrogena@ona.io on 19/05/2017.
 */

public class VaccineTrigger {
    private final Reference reference;
    private final String offset;
    private final String window;
    private final VaccineRepo.Vaccine prerequisite;

    public VaccineTrigger(String offset, String window, Reference reference) {
        this.reference = reference;
        this.offset = offset;
        prerequisite = null;
        this.window = window;
    }

    public VaccineTrigger(String offset, String window, VaccineRepo.Vaccine prerequisite) {
        reference = Reference.PREREQUISITE;
        this.offset = offset;
        this.prerequisite = prerequisite;
        this.window = window;
    }

    public static VaccineTrigger init(String vaccineCategory, Due data) {
        if (data != null) {
            if (data.reference.equalsIgnoreCase(Reference.DOB.name())) {
                return new VaccineTrigger(data.offset, data.window, Reference.DOB);
            } else if (data.reference.equalsIgnoreCase(Reference.LMP.name())) {
                return new VaccineTrigger(data.offset, data.window, Reference.LMP);
            } else if (data.reference.equalsIgnoreCase(Reference.PREREQUISITE.name())) {
                VaccineRepo.Vaccine prerequisite = VaccineRepo.getVaccine(data.prerequisite, vaccineCategory);
                if (prerequisite != null) {

                    //Vaccine Relaxation Logic
                    data.offset = Utils.updateRelaxationDays(data.offset);
                    return new VaccineTrigger(data.offset, data.window, prerequisite);
                }
            }
        }

        return null;
    }

    public static VaccineTrigger init(Expiry data) {
        if (data != null) {
            if (data.reference.equalsIgnoreCase(Reference.DOB.name())) {
                return new VaccineTrigger(data.offset, null, Reference.DOB);
            } else if (data.reference.equalsIgnoreCase(Reference.LMP.name())) {
                return new VaccineTrigger(data.offset, null, Reference.LMP);
            }
        }
        return null;
    }

    /**
     * Get the date the trigger will fire
     *
     * @return {@link Date} if able to get trigger date, or {@code null} if prerequisite hasn't been administered yet
     */
    public Date getFireDate(List<Vaccine> issuedVaccines, Date dob) {
        if (reference.equals(Reference.DOB)) {
            if (dob != null) {
                Calendar dobCalendar = Calendar.getInstance();
                dobCalendar.setTime(dob);
                VaccineSchedule.standardiseCalendarDate(dobCalendar);

                dobCalendar = VaccineSchedule.addOffsetToCalendar(dobCalendar, offset);
                return dobCalendar.getTime();
            }
        } else if (reference.equals(Reference.LMP)) {
            if (dob != null) {
                Calendar dobCalendar = Calendar.getInstance();
                dobCalendar.setTime(dob);
                VaccineSchedule.standardiseCalendarDate(dobCalendar);

                dobCalendar = VaccineSchedule.addOffsetToCalendar(dobCalendar, offset);
                return dobCalendar.getTime();
            }
        } else if (reference.equals(Reference.PREREQUISITE)) {
            // Check if prerequisite is in the list of issued vaccines
            Vaccine issuedPrerequisite = null;
            for (Vaccine curVaccine : issuedVaccines) {
                if (curVaccine.getName().equalsIgnoreCase(prerequisite.display())) {
                    issuedPrerequisite = curVaccine;
                    break;
                }
            }

            if (issuedPrerequisite != null) {
                // Check if the date given is in the past
                Calendar issuedDate = Calendar.getInstance();
                issuedDate.setTime(issuedPrerequisite.getDate());
                VaccineSchedule.standardiseCalendarDate(issuedDate);

                issuedDate = VaccineSchedule.addOffsetToCalendar(issuedDate, offset);
                return issuedDate.getTime();
            }
        }

        return null;
    }

    public String getWindow() {
        return window;
    }

    private enum Reference {
        DOB,
        PREREQUISITE,
        LMP
    }
}