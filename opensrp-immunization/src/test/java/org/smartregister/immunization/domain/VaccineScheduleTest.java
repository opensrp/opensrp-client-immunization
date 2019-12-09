package org.smartregister.immunization.domain;

import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.Context;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.jsonmapping.Condition;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by real on 23/10/17.
 */
@PrepareForTest({ImmunizationLibrary.class})
public class VaccineScheduleTest extends BaseUnitTest {

    private final String magicChild = "child";
    private final String magicOPV0 = "OPV 0";
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

    private Vaccine newVaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.PROGRAMCLIENTID, magicOPV0, 0,
            new Date(),
            VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l,
            VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0, new Date());

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Mockito.doReturn(VaccineRepo.Vaccine.values()).when(immunizationLibrary).getVaccines();
    }

    @Test
    public void assertUpdateOfflineAlertsTestReturnsAlert() {

        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);

        listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = JsonFormUtils.gson
                .fromJson(VaccineData.special_vacines, listType);
        mockImmunizationLibrary();

        VaccineSchedule.init(vaccines, specialVaccines, magicChild);

        Assert.assertNotNull(VaccineSchedule.updateOfflineAlerts(VaccineTest.BASEENTITYID, new DateTime(), magicChild));
    }

    private void mockImmunizationLibrary() {
        mockImmunizationLibrary(immunizationLibrary, context, vaccineRepository, alertService, appProperties);
    }

    @Test
    public void assertConstructorInitializationTest() {
        Assert.assertNotNull(new VaccineSchedule(null, null, null, null));
    }

    @Test
    public void assertInitAndInitVaccineWithTestData() {
        //        VaccineSchedule vaccineSchedule = Mockito.spy(this.vaccineSchedule);
        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);

        listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = JsonFormUtils.gson
                .fromJson(VaccineData.special_vacines, listType);

        mockImmunizationLibrary();

        VaccineSchedule.init(vaccines, specialVaccines, magicChild);
        VaccineSchedule.init(vaccines, specialVaccines, "");
        Assert.assertNotNull(VaccineSchedule.getVaccineSchedule(magicChild, magicOPV0));
        Assert.assertNull(VaccineSchedule.getVaccineSchedule("", ""));
        //vaccine cnodition test
        Condition object = new Condition();
        object.type = "";
        Assert.assertNull(VaccineCondition.init("", object));

        VaccineCondition.NotGivenCondition notgiven = new VaccineCondition.NotGivenCondition(VaccineRepo.Vaccine.opv0);
        List<Vaccine> list = new ArrayList<Vaccine>();
        list.add(newVaccine);
        Assert.assertNotNull(notgiven.passes(list));

        String magic10d = "+10d";
        VaccineCondition.GivenCondition given = new VaccineCondition.GivenCondition(VaccineRepo.Vaccine.opv0, magic10d,
                VaccineCondition.GivenCondition.Comparison.AT_LEAST);
        Assert.assertNull(VaccineCondition.GivenCondition.getComparison(""));
        Assert.assertNotNull(given.passes(list));

        given = new VaccineCondition.GivenCondition(VaccineRepo.Vaccine.opv0, magic10d,
                VaccineCondition.GivenCondition.Comparison.AT_MOST);
        Assert.assertNotNull(given.passes(list));
        given = new VaccineCondition.GivenCondition(VaccineRepo.Vaccine.opv0, magic10d,
                VaccineCondition.GivenCondition.Comparison.EXACTLY);
        Assert.assertNotNull(given.passes(list));

    }

}
