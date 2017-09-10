package org.smartregister.immunization.db;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.Vaccine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineRepoTest extends BaseUnitTest {

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {

    }

    @Test
    public void assertGetVaccinesWillReturnAccordingToCategoryWoman() {
        ArrayList<VaccineRepo.Vaccine> vaccines = new ArrayList<VaccineRepo.Vaccine>();
        vaccines.add(VaccineRepo.Vaccine.tt1);
        vaccines.add(VaccineRepo.Vaccine.tt2);
        vaccines.add(VaccineRepo.Vaccine.tt3);
        vaccines.add(VaccineRepo.Vaccine.tt4);
        vaccines.add(VaccineRepo.Vaccine.tt5);
        assertEquals(vaccines,VaccineRepo.getVaccines("woman"));
        assertEquals(vaccines.get(0),VaccineRepo.getVaccine("TT 1","woman"));
    }

//    @Test
//    public void assertGetNextVaccinesWillReturnAccordingToCategoryWoman() {
//        ArrayList<VaccineRepo.Vaccine> vaccines = new ArrayList<VaccineRepo.Vaccine>();
//        vaccines.add(VaccineRepo.Vaccine.tt2);
//        assertEquals(vaccines,VaccineRepo.nextVaccines("tt1"));
//    }



}
