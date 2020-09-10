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
import org.smartregister.immunization.domain.conditions.GivenCondition;
import org.smartregister.immunization.domain.conditions.JoinCondition;
import org.smartregister.immunization.domain.conditions.NotGivenCondition;
import org.smartregister.immunization.domain.jsonmapping.Condition;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

        NotGivenCondition notgiven = new NotGivenCondition(VaccineRepo.Vaccine.opv0);
        List<Vaccine> list = new ArrayList<Vaccine>();
        list.add(newVaccine);
        Assert.assertNotNull(notgiven.passes(null, list));

        String magic10d = "+10d";
        GivenCondition given = new GivenCondition(VaccineRepo.Vaccine.opv0, magic10d,
                GivenCondition.Comparison.AT_LEAST, null);
        Assert.assertNull(GivenCondition.getComparison(""));
        Assert.assertNotNull(given.passes(null, list));

        given = new GivenCondition(VaccineRepo.Vaccine.opv0, magic10d,
                GivenCondition.Comparison.AT_MOST, null);
        Assert.assertNotNull(given.passes(null, list));
        given = new GivenCondition(VaccineRepo.Vaccine.opv0, magic10d,
                GivenCondition.Comparison.EXACTLY, null);
        Assert.assertNotNull(given.passes(null, list));

    }

    @Test
    public void testVaccineOrCondition() throws ParseException {
        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);

        listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = JsonFormUtils.gson
                .fromJson(VaccineData.special_vacines, listType);

        mockImmunizationLibrary();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        VaccineSchedule.init(vaccines, specialVaccines, "child");
        VaccineSchedule.init(vaccines, specialVaccines, "");

        List<Vaccine> list = new ArrayList<>();
        list.add(new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.PROGRAMCLIENTID, "MCV 1", 0,
                sdf.parse("2020-01-28"),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0, new Date()));

        Condition object = new Condition();
        object.type = "join";
        object.value = "or";
        object.conditions = new ArrayList<>();

        Condition given1 = new Condition();
        given1.vaccine = "MCV 1";
        given1.type = "given";
        given1.age = new HashMap<>();
        given1.age.put("from", "+0d");
        given1.age.put("to", "+365d");

        given1.comparison = "at_least";
        given1.value = "+28d";
        object.conditions.add(given1);

        Condition given2 = new Condition();
        given2.vaccine = "MCV 1";
        given2.type = "given";
        given2.comparison = "exactly";
        given2.value = "+0d";
        object.conditions.add(given2);

        Condition given3 = new Condition();
        given3.vaccine = "MCV 1";
        given3.type = "not_given";
        object.conditions.add(given3);


        JoinCondition joinCondition = new JoinCondition("child", object);
        Assert.assertTrue(joinCondition.passes(sdf.parse("2020-01-01"), list));
    }

}
