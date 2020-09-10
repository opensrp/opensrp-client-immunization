package org.smartregister.immunization.domain.conditions;

import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineCondition;
import org.smartregister.immunization.domain.VaccineSchedule;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GivenCondition extends VaccineCondition {
    private final Comparison comparison;
    private final String value;

    public GivenCondition(VaccineRepo.Vaccine vaccine, String value, Comparison comparison) {
        super(vaccine);
        this.value = value;
        this.comparison = comparison;
    }

    public static Comparison getComparison(String name) {
        for (Comparison curComparison : Comparison.values()) {
            if (curComparison.name.equalsIgnoreCase(name)) {
                return curComparison;
            }
        }
        return null;
    }

    @Override
    public boolean passes(Date anchorDate, List<Vaccine> issuedVaccines) {
        boolean result = false;

        // Check if vaccine was given at all
        Vaccine comparisonVaccine = null;
        for (Vaccine curVaccine : issuedVaccines) {
            if (curVaccine.getName().equalsIgnoreCase(vaccine.display())) {
                comparisonVaccine = curVaccine;
                break;
            }
        }

        if (comparisonVaccine != null) {
            Calendar comparisonDate = Calendar.getInstance();
            VaccineSchedule.standardiseCalendarDate(comparisonDate);
            comparisonDate = VaccineSchedule.addOffsetToCalendar(comparisonDate, value);

            Calendar vaccinationDate = Calendar.getInstance();
            vaccinationDate.setTime(comparisonVaccine.getDate());
            VaccineSchedule.standardiseCalendarDate(vaccinationDate);

            switch (comparison) {
                case EXACTLY:
                    result = comparisonDate.getTimeInMillis() == vaccinationDate.getTimeInMillis();
                    break;
                case AT_LEAST:
                    result = comparisonDate.getTimeInMillis() >= vaccinationDate.getTimeInMillis();
                    break;
                case AT_MOST:
                    result = comparisonDate.getTimeInMillis() <= vaccinationDate.getTimeInMillis();
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    public enum Comparison {
        EXACTLY("exactly"),
        AT_LEAST("at_least"),
        AT_MOST("at_most");

        private final String name;

        Comparison(String name) {
            this.name = name;
        }
    }
}
