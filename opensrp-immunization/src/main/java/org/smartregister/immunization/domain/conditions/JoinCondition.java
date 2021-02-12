package org.smartregister.immunization.domain.conditions;

import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineCondition;
import org.smartregister.immunization.domain.jsonmapping.Condition;

import java.util.Date;
import java.util.List;

// TODO
public class JoinCondition extends VaccineCondition {
    private Condition conditionData;
    private String vaccineCategory;

    public JoinCondition(VaccineRepo.Vaccine vaccine) {
        super(vaccine);
    }

    public JoinCondition(String vaccineCategory, Condition conditionData) {
        super(null);
        this.conditionData = conditionData;
        this.vaccineCategory = vaccineCategory;
    }

    @Override
    public boolean passes(Date anchorDate, List<Vaccine> issuedVaccines) {
        if (conditionData.conditions != null && conditionData.conditions.size() > 0) {

            boolean isOr = "or".equalsIgnoreCase(conditionData.value);
            boolean passes = false;
            for (Condition condition : conditionData.conditions) {
                VaccineCondition vacCondition = VaccineCondition.init(vaccineCategory,
                        condition);

                passes = vacCondition != null && vacCondition.passes(anchorDate, issuedVaccines);

                // any or condition
                if (isOr && passes)
                    return true;

                // break if and conditions and it fails
                if (!isOr && !passes)
                    return false;
            }

            return passes;

        }

        return false;
    }
}
