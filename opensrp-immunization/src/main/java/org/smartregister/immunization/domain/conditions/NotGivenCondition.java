package org.smartregister.immunization.domain.conditions;

import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineCondition;

import java.util.Date;
import java.util.List;

public class NotGivenCondition extends VaccineCondition {

    public NotGivenCondition(VaccineRepo.Vaccine vaccine) {
        super(vaccine);
    }

    @Override
    public boolean passes(Date anchorDate, List<Vaccine> issuedVaccines) {
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
