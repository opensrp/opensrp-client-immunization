package org.smartregister.immunization.domain;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineTypeTest extends BaseUnitTest {

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        assertNotNull(new VaccineType(0l, 0, "", "", "", ""));
    }

    @Test
    public void assertgetIDwillreturnID() {
        VaccineType vaccineType = new VaccineType(0l, 0, "", "", "", "");
        assertEquals(vaccineType.getId().longValue(), 0l);
        vaccineType.setId(1l);
        assertEquals(vaccineType.getId().longValue(), 1l);
    }

    @Test
    public void assertgetDosewillreturnDose() {
        VaccineType vaccineType = new VaccineType(0l, 0, "", "", "", "");
        assertEquals(vaccineType.getDoses(), 0);
        vaccineType.setDoses(20);
        assertEquals(vaccineType.getDoses(), 20);
    }

    @Test
    public void assertgetNamewillreturnName() {
        VaccineType vaccineType = new VaccineType(0l, 0, "BCG", "", "", "");
        assertEquals(vaccineType.getName(), "BCG");
        vaccineType.setName("TT");
        assertEquals(vaccineType.getName(), "TT");
    }

    @Test
    public void assertgetOpenMrsParentEntityIDwillreturnOpenMrsParentEntityID() {
        VaccineType vaccineType = new VaccineType(0l, 0, "BCG", "018AAAAAAAA", "", "");
        assertEquals(vaccineType.getOpenmrs_parent_entity_id(), "018AAAAAAAA");
        vaccineType.setOpenmrs_parent_entity_id("01841AAAAAAAA");
        assertEquals(vaccineType.getOpenmrs_parent_entity_id(), "01841AAAAAAAA");
    }

    @Test
    public void assertgetOpenMrsDateConceptIDwillreturnOpenMrsDateConceptID() {
        VaccineType vaccineType = new VaccineType(0l, 0, "BCG", "018AAAAAAAA", "0AAAPAAAAAA", "");
        assertEquals(vaccineType.getOpenmrs_date_concept_id(), "0AAAPAAAAAA");
        vaccineType.setOpenmrs_date_concept_id("0AAA111PAAAAAA");
        assertEquals(vaccineType.getOpenmrs_date_concept_id(), "0AAA111PAAAAAA");
    }

    @Test
    public void assertgetOpenMrsDoseConceptIDwillreturnOpenMrsDoseConceptID() {
        VaccineType vaccineType = new VaccineType(0l, 0, "BCG", "018AAAAAAAA", "0AAAPAAAAAA", "0FF41AAAAAA");
        assertEquals(vaccineType.getOpenmrs_dose_concept_id(), "0FF41AAAAAA");
        vaccineType.setOpenmrs_dose_concept_id("0FF41AAAAAA");
        assertEquals(vaccineType.getOpenmrs_dose_concept_id(), "0FF41AAAAAA");
    }
}
