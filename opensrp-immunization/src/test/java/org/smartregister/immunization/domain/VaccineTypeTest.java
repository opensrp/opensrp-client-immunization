package org.smartregister.immunization.domain;

import org.junit.Assert;
import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

/**
 * Created by onaio on 30/08/NUMBEROFDOSES17.
 */

public class VaccineTypeTest extends BaseUnitTest {

    public static final String PARENTENTITYID = "018AAAAAAAA";
    public static final String PARENTENTITYID2 = "01841AAAAAAAA";
    public static final String DATECONCEPTID = "0AAAPAAAAAA";
    public static final String DATECONCEPTID1 = "0AAA111PAAAAAA";
    public static final String DOSECONCEPTID = "0FF41AAAAAA";
    public static final String DOSECONCEPTID2 = "0FF42AAAAAA";
    public static final String BCG = "BCG";
    public static final String TT = "TT";
    public static final int NUMBEROFDOSES = 20;

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        Assert.assertNotNull(new VaccineType(0l, 0, "", "", "", ""));
    }

    @Test
    public void assertgetIDwillreturnID() {
        VaccineType vaccineType = new VaccineType(0l, 0, "", "", "", "");
        Assert.assertEquals(vaccineType.getId().longValue(), 0l);
        vaccineType.setId(1l);
        Assert.assertEquals(vaccineType.getId().longValue(), 1l);
    }

    @Test
    public void assertgetDosewillreturnDose() {
        VaccineType vaccineType = new VaccineType(0l, 0, "", "", "", "");
        Assert.assertEquals(vaccineType.getDoses(), 0);
        vaccineType.setDoses(NUMBEROFDOSES);
        Assert.assertEquals(vaccineType.getDoses(), NUMBEROFDOSES);
    }

    @Test
    public void assertgetNamewillreturnName() {
        VaccineType vaccineType = new VaccineType(0l, 0, BCG, "", "", "");
        Assert.assertEquals(vaccineType.getName(), BCG);
        vaccineType.setName(TT);
        Assert.assertEquals(vaccineType.getName(), TT);
    }

    @Test
    public void assertgetOpenMrsParentEntityIDwillreturnOpenMrsParentEntityID() {
        VaccineType vaccineType = new VaccineType(0l, 0, BCG, PARENTENTITYID, "", "");
        Assert.assertEquals(vaccineType.getOpenmrs_parent_entity_id(), PARENTENTITYID);
        vaccineType.setOpenmrs_parent_entity_id(PARENTENTITYID2);
        Assert.assertEquals(vaccineType.getOpenmrs_parent_entity_id(), PARENTENTITYID2);
    }

    @Test
    public void assertgetOpenMrsDateConceptIDwillreturnOpenMrsDateConceptID() {
        VaccineType vaccineType = new VaccineType(0l, 0, BCG, PARENTENTITYID, DATECONCEPTID, "");
        Assert.assertEquals(vaccineType.getOpenmrs_date_concept_id(), DATECONCEPTID);
        vaccineType.setOpenmrs_date_concept_id(DATECONCEPTID1);
        Assert.assertEquals(vaccineType.getOpenmrs_date_concept_id(), DATECONCEPTID1);
    }

    @Test
    public void assertgetOpenMrsDoseConceptIDwillreturnOpenMrsDoseConceptID() {
        VaccineType vaccineType = new VaccineType(0l, 0, BCG, PARENTENTITYID, DATECONCEPTID, DOSECONCEPTID);
        Assert.assertEquals(vaccineType.getOpenmrs_dose_concept_id(), DOSECONCEPTID);
        vaccineType.setOpenmrs_dose_concept_id(DOSECONCEPTID2);
        Assert.assertEquals(vaccineType.getOpenmrs_dose_concept_id(), DOSECONCEPTID2);
    }
}
