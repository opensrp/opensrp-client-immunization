package org.smartregister.immunization.domain;

import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.conditions.GivenCondition;
import org.smartregister.immunization.domain.conditions.JoinCondition;
import org.smartregister.immunization.domain.conditions.NotGivenCondition;
import org.smartregister.immunization.domain.jsonmapping.Condition;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jason Rogena - jrogena@ona.io on 19/05/2017.
 */

public abstract class VaccineCondition {
    public static final String TYPE_NOT_GIVEN = "not_given";
    private static final String TYPE_GIVEN = "given";
    private static final String JOIN = "join";
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
                return new GivenCondition(vaccine, conditionData.value, comparison, conditionData);
            }
        } else if (conditionData.type.equals(TYPE_NOT_GIVEN)) {
            VaccineRepo.Vaccine vaccine = VaccineRepo.getVaccine(conditionData.vaccine,
                    vaccineCategory);

            if (vaccine != null) {
                return new NotGivenCondition(vaccine);
            }
        } else if (conditionData.type.equals(JOIN)) {
            return new JoinCondition(vaccineCategory, conditionData);
        }

        return null;
    }

    public abstract boolean passes(Date anchorDate, List<Vaccine> issuedVaccines);


    protected boolean isWithinAge(Date anchorDate, Date vaccineDate, Condition conditionData) {
        if (anchorDate != null && conditionData.age != null) {
            Calendar baseDate = getDate(vaccineDate, "+0d");
            Calendar startDate = getDate(anchorDate, conditionData.age.get("from"));
            Calendar endDate = conditionData.age.containsKey("to") ?
                    getDate(anchorDate, conditionData.age.get("to")) : getDate(new Date(), "+0d");

            return startDate.getTimeInMillis() <= baseDate.getTimeInMillis() && endDate.getTimeInMillis() >= baseDate.getTimeInMillis();
        }
        return true;
    }

    private Calendar getDate(Date anchorDate, String window) {
        Calendar dueDateCalendar = Calendar.getInstance();
        dueDateCalendar.setTime(anchorDate);
        VaccineSchedule.standardiseCalendarDate(dueDateCalendar);
        return VaccineSchedule.addOffsetToCalendar(dueDateCalendar, window);
    }
}
