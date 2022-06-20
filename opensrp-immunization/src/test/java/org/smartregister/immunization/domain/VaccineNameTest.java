package org.smartregister.immunization.domain;

import org.junit.Assert;
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
    public static final String DOSENO5 = "5";
    public static final String DOSENO10 = "10";
    public static final String DUEDAYS5 = "5";
    public static final String DUEDAYS10 = "10";

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        Assert.assertNotNull(new VaccineName(0l, "", "", "", "", "", ""));
    }

    @Test
    public void assertgetIDwillreturnID() {
        VaccineName vaccineName = new VaccineName(0l, "", "", "", "", "", "");
        Assert.assertEquals(vaccineName.getId().longValue(), 0l);
        vaccineName.setId(1l);
        Assert.assertEquals(vaccineName.getId().longValue(), 1l);
    }

    @Test
    public void assertgetNamewillreturnName() {
        VaccineName vaccineName = new VaccineName(0l, BCG, "", "", "", "", "");
        Assert.assertEquals(vaccineName.getName(), BCG);
        vaccineName.setName(TT);
        Assert.assertEquals(vaccineName.getName(), TT);
    }

    @Test
    public void assertgetTypeIDwillreturnTypeID() {
        VaccineName vaccineName = new VaccineName(0l, TT1, TT, "", "", "", "");
        Assert.assertEquals(vaccineName.getVaccine_type_id(), TT);
        vaccineName.setVaccine_type_id(BCG);
        Assert.assertEquals(vaccineName.getVaccine_type_id(), BCG);
    }

    @Test
    public void assertgetDueDayswillreturnDueDays() {
        VaccineName vaccineName = new VaccineName(0l, TT1, TT, DUEDAYS5, "", "", "");
        Assert.assertEquals(vaccineName.getDue_days(), DUEDAYS5);
        vaccineName.setDue_days(DUEDAYS10);
        Assert.assertEquals(vaccineName.getDue_days(), DUEDAYS10);
    }

    @Test
    public void assertgetReferenceVaccineIDwillreturnReferenceVaccineID() {
        VaccineName vaccineName = new VaccineName(0l, TT2, TT, DUEDAYS5, TT1, "", "");
        Assert.assertEquals(vaccineName.getReference_vaccine_id(), TT1);
        vaccineName.setReference_vaccine_id(TT3);
        Assert.assertEquals(vaccineName.getReference_vaccine_id(), TT3);
    }

    @Test
    public void assertgetClientTypewillreturnClientType() {
        VaccineName vaccineName = new VaccineName(0l, TT2, TT, DUEDAYS5, TT1, MOTHER, "");
        Assert.assertEquals(vaccineName.getClient_type(), MOTHER);
        vaccineName.setClient_type(CHILD);
        Assert.assertEquals(vaccineName.getClient_type(), CHILD);
    }

    @Test
    public void assertgetDoseNowillreturnDoseNo() {
        VaccineName vaccineName = new VaccineName(0l, TT2, TT, DUEDAYS5, TT1, MOTHER, DOSENO5);
        Assert.assertEquals(vaccineName.getDose_no(), DOSENO5);
        vaccineName.setDose_no(DOSENO10);
        Assert.assertEquals(vaccineName.getDose_no(), DOSENO10);
    }

}
