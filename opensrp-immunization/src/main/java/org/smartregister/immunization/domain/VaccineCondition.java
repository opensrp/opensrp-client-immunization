package org.smartregister.immunization.domain;

import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.jsonmapping.Condition;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Jason Rogena - jrogena@ona.io on 19/05/2017.
 */

public abstract class VaccineCondition {
    public static final String TYPE_NOT_GIVEN = "not_given";
    private static final String TYPE_GIVEN = "given";
    protected final VaccineRepo.Vaccine vaccine;

    public VaccineCondition(VaccineRepo.Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public static VaccineCondition init(String vaccineCategory, Condition conditionData) {
        if (conditionData.type.equals(TYPE_GIVEN)) {
            GivenCondition.Comparison comparison = GivenCondition.getComparison(conditionData.comparison);
            VaccineRepo.Vaccine vaccine = VaccineRepo.getVaccine(conditionData.vaccine,
                    vaccineCategory);

            if (comparison != null && vaccine != null) {
                return new GivenCondition(vaccine, conditionData.value, comparison);
            }
        } else if (conditionData.type.equals(TYPE_NOT_GIVEN)) {
            VaccineRepo.Vaccine vaccine = VaccineRepo.getVaccine(conditionData.vaccine,
                    vaccineCategory);

            if (vaccine != null) {
                return new NotGivenCondition(vaccine);
            }
        }

        return null;
    }

    public abstract boolean passes(List<Vaccine> issuedVaccines);

    public static class NotGivenCondition extends VaccineCondition {

        public NotGivenCondition(VaccineRepo.Vaccine vaccine) {
            super(vaccine);
        }

        @Override
        public boolean passes(List<Vaccine> issuedVaccines) {
            // Check if vaccine was not given
            boolean given = false;

            // TODO: Check if name used in VaccineRepo.Vaccine is the same as the one in Vaccine
            for (Vaccine curVaccine : issuedVaccines) {
                if (curVaccine.getName().equalsIgnoreCase(vaccine.display())) {
                    given = true;
                    break;
                }
            }

            return !given;
        }
    }

    public static class GivenCondition extends VaccineCondition {
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
        public boolean passes(List<Vaccine> issuedVaccines) {
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
}
