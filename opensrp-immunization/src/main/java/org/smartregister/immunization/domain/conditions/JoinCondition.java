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

    public JoinCondition(VaccineRepo.Vaccine vaccine, Condition conditionData) {
        super(vaccine);
        this.conditionData = conditionData;
    }

    public JoinCondition() {
        super(null);
    }

    @Override
    public boolean passes(Date anchorDate, List<Vaccine> issuedVaccines) {
        if(conditionData.conditions != null && conditionData.conditions.size() > 0){

        }

        return false;
    }
}
