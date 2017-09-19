package org.smartregister.immunization.domain;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineNameTest extends BaseUnitTest {

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        assertNotNull(new VaccineName(0l, "", "", "", "", "", ""));
    }

    @Test
    public void assertgetIDwillreturnID(){
        VaccineName vaccineName = new VaccineName(0l, "", "", "", "", "", "");
        assertEquals(vaccineName.getId(). longValue(), 0l);
        vaccineName.setId(1l);
        assertEquals(vaccineName.getId().longValue(), 1l);
    }

    @Test
    public void assertgetNamewillreturnName(){
        VaccineName vaccineName = new VaccineName(0l, "BCG", "", "", "", "", "");
        assertEquals(vaccineName.getName(), "BCG");
        vaccineName.setName("TT");
        assertEquals(vaccineName.getName(), "TT");
    }

    @Test
    public void assertgetTypeIDwillreturnTypeID(){
        VaccineName vaccineName = new VaccineName(0l, "TT1", "TT", "", "", "", "");
        assertEquals(vaccineName.getVaccine_type_id(), "TT");
        vaccineName.setVaccine_type_id("BCG");
        assertEquals(vaccineName.getVaccine_type_id(), "BCG");
    }

    @Test
    public void assertgetDueDayswillreturnDueDays(){
        VaccineName vaccineName = new VaccineName(0l, "TT1", "TT", "5", "", "", "");
        assertEquals(vaccineName.getDue_days(), "5");
        vaccineName.setDue_days("10");
        assertEquals(vaccineName.getDue_days(), "10");
    }

    @Test
    public void assertgetReferenceVaccineIDwillreturnReferenceVaccineID(){
        VaccineName vaccineName = new VaccineName(0l, "TT2", "TT", "5", "TT1", "", "");
        assertEquals(vaccineName.getReference_vaccine_id(), "TT1");
        vaccineName.setReference_vaccine_id("TT3");
        assertEquals(vaccineName.getReference_vaccine_id(), "TT3");
    }

    @Test
    public void assertgetClientTypewillreturnClientType(){
        VaccineName vaccineName = new VaccineName(0l, "TT2", "TT", "5", "TT1", "Mother", "");
        assertEquals(vaccineName.getClient_type(), "Mother");
        vaccineName.setClient_type("Child");
        assertEquals(vaccineName.getClient_type(), "Child");
    }

    @Test
    public void assertgetDoseNowillreturnDoseNo(){
        VaccineName vaccineName = new VaccineName(0l, "TT2", "TT", "5", "TT1", "Mother", "5");
        assertEquals(vaccineName.getDose_no(), "5");
        vaccineName.setDose_no("10");
        assertEquals(vaccineName.getDose_no(), "10");
    }

}
