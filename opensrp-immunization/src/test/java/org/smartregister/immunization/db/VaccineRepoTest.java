package org.smartregister.immunization.db;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.Context;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;

import java.util.ArrayList;

/**
 * Created by onaio on 30/08/2017.
 */

@PrepareForTest({ImmunizationLibrary.class})
public class VaccineRepoTest extends BaseUnitTest {
    public static final String WOMAN = "woman";
    public static final String CHILD = "child";

    @Rule
    public PowerMockRule rule = new PowerMockRule();
    @Mock
    private ImmunizationLibrary immunizationLibrary;
    @Mock
    private VaccineRepository vaccineRepository;
    @Mock
    private Context context;
    @Mock
    private AlertService alertService;
    @Mock
    private AppProperties appProperties;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mockImmunizationLibrary(immunizationLibrary, context, vaccineRepository, alertService, appProperties);
    }

    @Test
    public void assertGetVaccinesWillReturnAccordingToCategoryWoman() {
        ArrayList<VaccineRepo.Vaccine> vaccines = new ArrayList<>();
        vaccines.add(VaccineRepo.Vaccine.tt1);
        vaccines.add(VaccineRepo.Vaccine.tt2);
        vaccines.add(VaccineRepo.Vaccine.tt3);
        vaccines.add(VaccineRepo.Vaccine.tt4);
        vaccines.add(VaccineRepo.Vaccine.tt5);
        Assert.assertEquals(vaccines, VaccineRepo.getVaccines(WOMAN));
        Assert.assertEquals(vaccines.get(0), VaccineRepo.getVaccine("TT 1", WOMAN));
        Assert.assertEquals(vaccines.get(1), VaccineRepo.getVaccine("TT 2", WOMAN));
        Assert.assertEquals(vaccines.get(2), VaccineRepo.getVaccine("TT 3", WOMAN));
        Assert.assertEquals(vaccines.get(3), VaccineRepo.getVaccine("TT 4", WOMAN));
        Assert.assertEquals(vaccines.get(4), VaccineRepo.getVaccine("TT 5", WOMAN));
    }

    @Test
    public void assertGetVaccinesWillReturnAccordingToCategoryChild() {

        ArrayList<VaccineRepo.Vaccine> vaccines = new ArrayList<>();
        vaccines.add(VaccineRepo.Vaccine.bcg);
        vaccines.add(VaccineRepo.Vaccine.HepB);
        vaccines.add(VaccineRepo.Vaccine.opv0);
        vaccines.add(VaccineRepo.Vaccine.opv1);
        vaccines.add(VaccineRepo.Vaccine.penta1);
        vaccines.add(VaccineRepo.Vaccine.pcv1);
        vaccines.add(VaccineRepo.Vaccine.rota1);
        vaccines.add(VaccineRepo.Vaccine.opv2);
        vaccines.add(VaccineRepo.Vaccine.penta2);
        vaccines.add(VaccineRepo.Vaccine.pcv2);
        vaccines.add(VaccineRepo.Vaccine.rota2);
        vaccines.add(VaccineRepo.Vaccine.rtss1);
        vaccines.add(VaccineRepo.Vaccine.rtss2);
        vaccines.add(VaccineRepo.Vaccine.rtss3);
        vaccines.add(VaccineRepo.Vaccine.rtss4);
        vaccines.add(VaccineRepo.Vaccine.mv1);
        vaccines.add(VaccineRepo.Vaccine.mv2);
        vaccines.add(VaccineRepo.Vaccine.mv3);
        vaccines.add(VaccineRepo.Vaccine.mv4);
        vaccines.add(VaccineRepo.Vaccine.opv3);
        vaccines.add(VaccineRepo.Vaccine.penta3);
        vaccines.add(VaccineRepo.Vaccine.pcv3);
        vaccines.add(VaccineRepo.Vaccine.pcv4);
        vaccines.add(VaccineRepo.Vaccine.rota3);
        vaccines.add(VaccineRepo.Vaccine.ipv);
        vaccines.add(VaccineRepo.Vaccine.mrce);
        vaccines.add(VaccineRepo.Vaccine.measles1);
        vaccines.add(VaccineRepo.Vaccine.mr1);
        vaccines.add(VaccineRepo.Vaccine.opv4);
        vaccines.add(VaccineRepo.Vaccine.mcv1);
        vaccines.add(VaccineRepo.Vaccine.rubella1);
        vaccines.add(VaccineRepo.Vaccine.yellowfever);
        vaccines.add(VaccineRepo.Vaccine.menA);
        vaccines.add(VaccineRepo.Vaccine.meningococcal);
        vaccines.add(VaccineRepo.Vaccine.typhoid);
        vaccines.add(VaccineRepo.Vaccine.mcv2);
        vaccines.add(VaccineRepo.Vaccine.rubella2);
        vaccines.add(VaccineRepo.Vaccine.measles2);
        vaccines.add(VaccineRepo.Vaccine.mr2);
        vaccines.add(VaccineRepo.Vaccine.bcg2);
        vaccines.add(VaccineRepo.Vaccine.ipv1);
        vaccines.add(VaccineRepo.Vaccine.ipv2);
        vaccines.add(VaccineRepo.Vaccine.hepb0);
        vaccines.add(VaccineRepo.Vaccine.dtp4);
        Assert.assertEquals(vaccines, VaccineRepo.getVaccines(CHILD));
    }

    @Test
    public void assertGetNextVaccinesWillReturnNotNull() {
        Assert.assertNotNull(VaccineRepo.nextVaccines(""));
    }

    @Test
    public void assertGetDisplayWillReturnNotNull() {
        VaccineRepo.Vaccine vaccine = VaccineRepo.Vaccine.opv0;
        Assert.assertNotNull(vaccine.display());
    }

    @Test
    public void assertGetCategoryWillReturnNotNull() {
        VaccineRepo.Vaccine vaccine = VaccineRepo.Vaccine.opv0;
        Assert.assertNotNull(vaccine.category());
    }

    @Test
    public void assertGetPrerequisiteWillReturnNotNull() {
        VaccineRepo.Vaccine vaccine = VaccineRepo.Vaccine.tt4;
        Assert.assertNotNull(vaccine.prerequisite());
    }

    @Test
    public void assertGetPrerequisiteGapDaysWillReturnNotNull() {
        VaccineRepo.Vaccine vaccine = VaccineRepo.Vaccine.rota2;
        Assert.assertNotNull(vaccine.prerequisiteGapDays());
    }

    @Test
    public void assertGetExpiryDaysWillReturnNotNull() {
        VaccineRepo.Vaccine vaccine = VaccineRepo.Vaccine.pcv1;
        Assert.assertNotNull(vaccine.expiryDays());
    }

    @Test
    public void assertGetmilestoneGapDaysWillReturnNotNull() {
        VaccineRepo.Vaccine vaccine = VaccineRepo.Vaccine.opv0;
        Assert.assertNotNull(vaccine.milestoneGapDays());
    }

}
