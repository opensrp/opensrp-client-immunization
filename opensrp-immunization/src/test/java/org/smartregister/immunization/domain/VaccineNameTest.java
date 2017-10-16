package org.smartregister.immunization.domain;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineNameTest extends BaseUnitTest {

    public static final String BCG = "BCG";
    public static final String TT = "TT";
    public static final String TT1 = "TT1";
    public static final String TT2 = "TT2";
    public static final String TT3 = "TT3";
    public static final String MOTHER = "Mother";
    public static final String CHILD = "Child";

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        junit.framework.Assert.assertNotNull(new VaccineName(0l, "", "", "", "", "", ""));
    }

    @Test
    public void assertgetIDwillreturnID() {
        VaccineName vaccineName = new VaccineName(0l, "", "", "", "", "", "");
        junit.framework.Assert.assertEquals(vaccineName.getId().longValue(), 0l);
        vaccineName.setId(1l);
        junit.framework.Assert.assertEquals(vaccineName.getId().longValue(), 1l);
    }

    @Test
    public void assertgetNamewillreturnName() {
        VaccineName vaccineName = new VaccineName(0l, BCG, "", "", "", "", "");
        junit.framework.Assert.assertEquals(vaccineName.getName(), BCG);
        vaccineName.setName(TT);
        junit.framework.Assert.assertEquals(vaccineName.getName(), TT);
    }

    @Test
    public void assertgetTypeIDwillreturnTypeID() {
        VaccineName vaccineName = new VaccineName(0l, TT1, TT, "", "", "", "");
        junit.framework.Assert.assertEquals(vaccineName.getVaccine_type_id(), TT);
        vaccineName.setVaccine_type_id(BCG);
        junit.framework.Assert.assertEquals(vaccineName.getVaccine_type_id(), BCG);
    }

    @Test
    public void assertgetDueDayswillreturnDueDays() {
        VaccineName vaccineName = new VaccineName(0l, TT1, TT, "5", "", "", "");
        junit.framework.Assert.assertEquals(vaccineName.getDue_days(), "5");
        vaccineName.setDue_days("10");
        junit.framework.Assert.assertEquals(vaccineName.getDue_days(), "10");
    }

    @Test
    public void assertgetReferenceVaccineIDwillreturnReferenceVaccineID() {
        VaccineName vaccineName = new VaccineName(0l, TT2, TT, "5", TT1, "", "");
        junit.framework.Assert.assertEquals(vaccineName.getReference_vaccine_id(), TT1);
        vaccineName.setReference_vaccine_id(TT3);
        junit.framework.Assert.assertEquals(vaccineName.getReference_vaccine_id(), TT3);
    }

    @Test
    public void assertgetClientTypewillreturnClientType() {
        VaccineName vaccineName = new VaccineName(0l, TT2, TT, "5", TT1, MOTHER, "");
        junit.framework.Assert.assertEquals(vaccineName.getClient_type(), MOTHER);
        vaccineName.setClient_type(CHILD);
        junit.framework.Assert.assertEquals(vaccineName.getClient_type(), CHILD);
    }

    @Test
    public void assertgetDoseNowillreturnDoseNo() {
        VaccineName vaccineName = new VaccineName(0l, TT2, TT, "5", TT1, MOTHER, "5");
        junit.framework.Assert.assertEquals(vaccineName.getDose_no(), "5");
        vaccineName.setDose_no("10");
        junit.framework.Assert.assertEquals(vaccineName.getDose_no(), "10");
    }

}
