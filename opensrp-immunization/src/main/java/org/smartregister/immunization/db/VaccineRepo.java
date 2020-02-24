package org.smartregister.immunization.db;

import org.smartregister.immunization.ImmunizationLibrary;

import java.util.ArrayList;

public class VaccineRepo {

    public static ArrayList<Vaccine> getVaccines(String category) {
        return getVaccines(category, false);
    }

    public static ArrayList<Vaccine> getVaccines(String category, boolean useDefault) {
        ArrayList<Vaccine> vl = new ArrayList<>();
        Vaccine[] vaccines = useDefault ? Vaccine.values() : ImmunizationLibrary.getInstance().getVaccines();
        for (Vaccine v : vaccines) {
            if (v.category().equalsIgnoreCase(category.trim())) {
                vl.add(v);
            }
        }
        return vl;
    }

    public static Vaccine getVaccine(String name, String category) {
        Vaccine[] vaccines = ImmunizationLibrary.getInstance().getVaccines();
        for (Vaccine curVaccine : vaccines) {
            if (curVaccine.display.equalsIgnoreCase(name)
                    && curVaccine.category.equalsIgnoreCase(category)) {
                return curVaccine;
            }
        }

        return null;
    }

    public static ArrayList<Vaccine> nextVaccines(String vaccine) {
        ArrayList<Vaccine> vl = new ArrayList<>();
        for (Vaccine v : Vaccine.values()) {
            if (v.prerequisite != null && v.prerequisite().name().equalsIgnoreCase(vaccine.trim())) {
                vl.add(v);
            }
        }
        return vl;
    }

    public static VaccineRepo.Vaccine getVaccineEnumFromValue(String value) {
        for (VaccineRepo.Vaccine vaccine : VaccineRepo.Vaccine.values()) {
            if (vaccine.name().equalsIgnoreCase(cleanVaccineLabel(value))) return vaccine;
        }

        throw new IllegalArgumentException(String.format(" No enum constant %1$s.%2$s", Vaccine.class.getCanonicalName(), value));
    }

    public static String cleanVaccineLabel(String vaccineLabel) {
        return vaccineLabel.replaceAll(",", "").replaceAll("-", "").replaceAll(" ", "");
    }

    public enum Vaccine {
        bcg("BCG", null, 366, 0, 0, "child"),
        HepB("HepB", null, 366, 0, 0, "child"),
        opv0("OPV 0", null, 13, 0, 0, "child"),

        opv1("OPV 1", null, 1768, 42, 0, "child"),
        penta1("PENTA 1", null, 1768, 42, 0, "child"),
        pcv1("PCV 1", null, 1768, 42, 0, "child"),
        rota1("ROTA 1", null, 213, 42, 0, "child"),

        opv2("OPV 2", opv1, 1799, 70, 28, "child"),
        penta2("PENTA 2", penta1, 1799, 70, 28, "child"),
        pcv2("PCV 2", pcv1, 1799, 70, 28, "child"),
        rota2("ROTA 2", rota1, 244, 70, 28, "child"),

        rtss1("RTSS 1", null, 304, 183, 0, "child"),
        rtss2("RTSS 2", rtss1, 608, 213, 0, "child"),
        rtss3("RTSS 3", rtss2, 639, 274, 0, "child"),
        rtss4("RTSS 4", rtss3, 1095, 730, 0, "child"),

        mv1("MV 1", null, 304, 152, 0, "child"),
        mv2("MV 2", null, 608, 183, 0, "child"),
        mv3("MV 3", null, 639, 213, 0, "child"),
        mv4("MV 4", null, 1095, 669, 0, "child"),

        opv3("OPV 3", opv2, 1830, 98, 28, "child"),
        penta3("PENTA 3", penta2, 1830, 98, 28, "child"),
        pcv3("PCV 3", pcv2, 1830, 98, 28, "child"),
        rota3("ROTA 3", rota2, 1830, 98, 28, "child"),
        ipv("IPV", null, 1830, 98, 0, "child"),

        mrce("MR - CE", null, -1, 183, 0, "child"),
        measles1("MEASLES 1", null, -1, 274, 0, "child"),
        mr1("MR 1", null, -1, 274, 0, "child"),
        opv4("OPV 4", null, 1830, 274, 28, "child"),
        mcv1("MCV 1", null, -1, 274, 0, "child"),
        rubella1("Rubella 1", null, 1830, 274, 0, "child"),
        yellowfever("Yellow Fever", null, 548, 274, 0, "child"),
        menA("MenA", null, 1830, 274, 0, "child"),
        meningococcal("Meningococcal", null, 548, 274, 28, "child"),
        typhoid("Typhoid", null, 1830, 274, 0, "child"),


        mcv2("MCV 2", mcv1, -1, 456, 183, "child"),
        rubella2("Rubella 2", null, -1, 456, 183, "child"),

        measles2("MEASLES 2", measles1, -1, 548, 274, "child"),
        mr2("MR 2", mr1, -1, 548, 274, "child"),


        bcg2("BCG 2", null, 366, 0, 84, "child"),

        tt1("TT 1", null, 0, 0, 0, "woman"),
        tt2("TT 2", tt1, 366, 0, 28, "woman"),
        tt3("TT 3", tt2, 366, 0, 26 * 7, "woman"),
        tt4("TT 4", tt3, 366, 0, 52 * 7, "woman"),
        tt5("TT 5", tt4, 1830, 0, 52 * 7, "woman"),
        ;

        private String display;
        private Vaccine prerequisite;
        private int expiryDays;
        private int milestoneGapDays;
        private int prerequisiteGapDays;
        private String category;

        Vaccine(String display, Vaccine prerequisite, int expiryDays,
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

        public Vaccine prerequisite() {
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

        public void setDisplay(String display) {
            this.display = display;
        }

        public void setPrerequisite(Vaccine prerequisite) {
            this.prerequisite = prerequisite;
        }

        public void setExpiryDays(int expiryDays) {
            this.expiryDays = expiryDays;
        }

        public void setMilestoneGapDays(int milestoneGapDays) {
            this.milestoneGapDays = milestoneGapDays;
        }

        public void setPrerequisiteGapDays(int prerequisiteGapDays) {
            this.prerequisiteGapDays = prerequisiteGapDays;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }
}
