package org.smartregister.immunization.sample.util;

import org.smartregister.immunization.db.VaccineRepo;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-08-29
 */

public class VaccineDuplicate {

    private String display;
    private VaccineRepo.Vaccine prerequisite;
    private int expiryDays;
    private int milestoneGapDays;
    private int prerequisiteGapDays;
    private String category;

    public VaccineDuplicate(String display, VaccineRepo.Vaccine prerequisite, int expiryDays,
                            int milestoneGapDays, int prerequisiteGapDays, String category) {
        this.display = display;
        this.prerequisite = prerequisite;
        this.expiryDays = expiryDays;
        this.milestoneGapDays = milestoneGapDays;
        this.prerequisiteGapDays = prerequisiteGapDays;
        this.category = category;
    }

    public String display() {
        return display;
    }

    public VaccineRepo.Vaccine prerequisite() {
        return prerequisite;
    }

    public int expiryDays() {
        return expiryDays;
    }

    public int milestoneGapDays() {
        return milestoneGapDays;
    }

    public int prerequisiteGapDays() {
        return prerequisiteGapDays;
    }

    public String category() {
        return category;
    }

}