package org.smartregister.immunization.service.intent;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.jsonmapping.Condition;
import org.smartregister.immunization.domain.jsonmapping.Due;
import org.smartregister.immunization.domain.jsonmapping.Expiry;
import org.smartregister.immunization.domain.jsonmapping.OpenMRSCalculation;
import org.smartregister.immunization.domain.jsonmapping.OpenMRSDate;
import org.smartregister.immunization.domain.jsonmapping.Schedule;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.VaccinatorUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

/**
 * Created by onaio on 30/08/2017.
 */
@PrepareForTest({ImmunizationLibrary.class, VaccinatorUtils.class})
public class VaccineIntentServiceTest extends BaseUnitTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private VaccineRepository vaccineRepository;

    @Spy
    private List<Vaccine> vaccineList = new ArrayList<>();

    @Mock
    private List<VaccineGroup> availableVaccines;

    @Mock
    private List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialJsonMappingVaccines;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        Assert.assertNotNull(new VaccineIntentService());
    }

    @Test
    public void onHandleIntentTest() {
        Application application = RuntimeEnvironment.application;
        Intent intent = new Intent(application, VaccineIntentService.class);

        VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
        Assert.assertNotNull(vaccineIntentService);

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.mockStatic(VaccinatorUtils.class);

        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Assert.assertNotNull(immunizationLibrary);

        PowerMockito.when(ImmunizationLibrary.getInstance().vaccineRepository()).thenReturn(vaccineRepository);
        Assert.assertNotNull(vaccineRepository);

        PowerMockito.when(VaccinatorUtils.getSupportedVaccines(any(Context.class))).thenReturn(availableVaccines);
        PowerMockito.when(VaccinatorUtils.getSpecialVaccines(any(Context.class))).thenReturn(specialJsonMappingVaccines);
        Assert.assertNotNull(availableVaccines);
        Assert.assertNotNull(specialJsonMappingVaccines);

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.NAME, 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        Mockito.when(vaccineRepository.findUnSyncedBeforeTime(IMConstants.VACCINE_SYNC_TIME))
                .thenReturn(vaccineList);
        vaccineList.add(vaccine);
        Assert.assertNotNull(vaccineList);

        Whitebox.setInternalState(vaccineIntentService, "vaccineRepository",
                vaccineRepository);
        vaccineIntentService.onHandleIntent(intent);
    }

    @Test
    public void getEventTypeTest() {
        VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
        Assert.assertNotNull(vaccineIntentService);

        String eventType = vaccineIntentService.getEventType();
        Assert.assertNotNull(eventType);
        Assert.assertEquals(eventType, "Vaccination");
    }

    @Test
    public void getEntityTypeTest() {
        VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
        Assert.assertNotNull(vaccineIntentService);

        String entityType = vaccineIntentService.getEntityType();
        Assert.assertNotNull(entityType);
        Assert.assertEquals(entityType, "vaccination");
    }

    @Test
    public void getEventTypeOutOfCatchmentTest() {
        VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
        Assert.assertNotNull(vaccineIntentService);

        String eventTypeOutOfCatchment = vaccineIntentService.getEventTypeOutOfCatchment();
        Assert.assertNotNull(eventTypeOutOfCatchment);
        Assert.assertEquals(eventTypeOutOfCatchment, "Out of Area Service - Vaccination");
    }

    @Test
    public void getParentIdAvailableVaccinesTest() {
        try {
            OpenMRSDate openMRSDate = getOpenMRSDate();
            OpenMRSCalculation openMRSCalculation = getOpenMRSCalculation();

            Due due = getDue();
            List<Due> dueList = new ArrayList<>();
            dueList.add(due);

            Expiry expiry = getExpiry();
            List<Expiry> expiryList = new ArrayList<>();
            expiryList.add(expiry);

            Condition condition = getCondition();
            List<Condition> conditionList = new ArrayList<>();
            conditionList.add(condition);

            Schedule schedule = getSchedule(dueList, expiryList, conditionList);
            Map<String, Schedule> stringScheduleMap = new HashMap<>();
            stringScheduleMap.put("schedule", schedule);

            org.smartregister.immunization.domain.jsonmapping.Vaccine jsonMappingVaccine = getVaccine(openMRSDate,
                    openMRSCalculation, schedule, stringScheduleMap);
            List<org.smartregister.immunization.domain.jsonmapping.Vaccine> vaccineList = new ArrayList<>();
            vaccineList.add(jsonMappingVaccine);

            VaccineGroup vaccineGroup = getVaccineGroup(vaccineList);
            List<VaccineGroup> vaccineGroups = new ArrayList<>();
            vaccineGroups.add(vaccineGroup);

            VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
            Assert.assertNotNull(vaccineIntentService);

            Whitebox.setInternalState(vaccineIntentService, "availableVaccines", vaccineGroups);
            Whitebox.setInternalState(vaccineIntentService, "specialVaccines", new ArrayList<>());
            String parentId = Whitebox.invokeMethod(vaccineIntentService, "getParentId", "name");
            Assert.assertEquals("QEUIYN327647857657657657656576576", parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @NotNull
    private OpenMRSDate getOpenMRSDate() {
        OpenMRSDate openMRSDate = new OpenMRSDate();
        openMRSDate.entity = "AA000000000000000000000000344";
        openMRSDate.entity_id = "HFGHJD234444444444444";
        openMRSDate.parent_entity = "QEUIYN327647857657657657656576576";
        return openMRSDate;
    }

    @NotNull
    private OpenMRSCalculation getOpenMRSCalculation() {
        OpenMRSCalculation openMRSCalculation = new OpenMRSCalculation();
        openMRSCalculation.calculation = 1;
        return openMRSCalculation;
    }

    @NotNull
    private Due getDue() {
        Due due = new Due();
        due.offset = "3d";
        due.prerequisite = null;
        due.window = "18d";
        return due;
    }

    @NotNull
    private Expiry getExpiry() {
        Expiry expiry = new Expiry();
        expiry.offset = "3d";
        expiry.reference = null;
        return expiry;
    }

    @NotNull
    private Condition getCondition() {
        Condition condition = new Condition();
        condition.comparison = "";
        condition.type = "";
        condition.vaccine = "";
        condition.value = "";
        return condition;
    }

    @NotNull
    private Schedule getSchedule(List<Due> dueList, List<Expiry> expiryList, List<Condition> conditionList) {
        Schedule schedule = new Schedule();
        schedule.due = dueList;
        schedule.expiry = expiryList;
        schedule.conditions = conditionList;
        return schedule;
    }

    @NotNull
    private org.smartregister.immunization.domain.jsonmapping.Vaccine getVaccine(OpenMRSDate openMRSDate,
                                                                                 OpenMRSCalculation openMRSCalculation,
                                                                                 Schedule schedule,
                                                                                 Map<String, Schedule> stringScheduleMap) {
        org.smartregister.immunization.domain.jsonmapping.Vaccine jsonMappingVaccine = new org.smartregister.immunization.domain.jsonmapping.Vaccine();
        jsonMappingVaccine.setName("name");
        jsonMappingVaccine.setType("vaccine_type");
        jsonMappingVaccine.setOpenmrsDate(openMRSDate);
        jsonMappingVaccine.setOpenmrsCalculate(openMRSCalculation);
        jsonMappingVaccine.setSchedule(schedule);
        jsonMappingVaccine.setSchedules(stringScheduleMap);
        jsonMappingVaccine.setVaccineSeparator("/");
        return jsonMappingVaccine;
    }

    @NotNull
    private VaccineGroup getVaccineGroup(List<org.smartregister.immunization.domain.jsonmapping.Vaccine> vaccineList) {
        VaccineGroup vaccineGroup = new VaccineGroup();
        vaccineGroup.id = "AHFF0000000000000032432543";
        vaccineGroup.name = "VaccineGropu";
        vaccineGroup.days_after_birth_due = 3;
        vaccineGroup.vaccines = vaccineList;
        return vaccineGroup;
    }

    @Test
    public void getParentIdWithSpecialVaccinesTest() {
        try {
            OpenMRSDate openMRSDate = getOpenMRSDate();
            OpenMRSCalculation openMRSCalculation = getOpenMRSCalculation();

            Due due = getDue();
            List<Due> dueList = new ArrayList<>();
            dueList.add(due);

            Expiry expiry = getExpiry();
            List<Expiry> expiryList = new ArrayList<>();
            expiryList.add(expiry);

            Condition condition = getCondition();
            List<Condition> conditionList = new ArrayList<>();
            conditionList.add(condition);

            Schedule schedule = getSchedule(dueList, expiryList, conditionList);
            Map<String, Schedule> stringScheduleMap = new HashMap<>();
            stringScheduleMap.put("schedule", schedule);

            org.smartregister.immunization.domain.jsonmapping.Vaccine jsonMappingVaccine = getVaccine(openMRSDate,
                    openMRSCalculation, schedule, stringScheduleMap);
            List<org.smartregister.immunization.domain.jsonmapping.Vaccine> vaccineList = new ArrayList<>();
            vaccineList.add(jsonMappingVaccine);

            VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
            Assert.assertNotNull(vaccineIntentService);

            Whitebox.setInternalState(vaccineIntentService, "availableVaccines", new ArrayList<>());
            Whitebox.setInternalState(vaccineIntentService, "specialVaccines", vaccineList);
            String parentId = Whitebox.invokeMethod(vaccineIntentService, "getParentId", "name");
            Assert.assertEquals("QEUIYN327647857657657657656576576", parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getParentIdWithTwoNamesTest() {
        try {
            OpenMRSDate openMRSDate = getOpenMRSDate();
            OpenMRSCalculation openMRSCalculation = getOpenMRSCalculation();

            Due due = getDue();
            List<Due> dueList = new ArrayList<>();
            dueList.add(due);

            Expiry expiry = getExpiry();
            List<Expiry> expiryList = new ArrayList<>();
            expiryList.add(expiry);

            Condition condition = getCondition();
            List<Condition> conditionList = new ArrayList<>();
            conditionList.add(condition);

            Schedule schedule = getSchedule(dueList, expiryList, conditionList);
            Map<String, Schedule> stringScheduleMap = new HashMap<>();
            stringScheduleMap.put("schedule", schedule);

            org.smartregister.immunization.domain.jsonmapping.Vaccine jsonMappingVaccine = getVaccine(openMRSDate,
                    openMRSCalculation, schedule, stringScheduleMap);
            List<org.smartregister.immunization.domain.jsonmapping.Vaccine> vaccineList = new ArrayList<>();
            vaccineList.add(jsonMappingVaccine);

            VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
            Assert.assertNotNull(vaccineIntentService);

            Whitebox.setInternalState(vaccineIntentService, "availableVaccines", new ArrayList<>());
            Whitebox.setInternalState(vaccineIntentService, "specialVaccines", vaccineList);
            String parentId = Whitebox.invokeMethod(vaccineIntentService, "getParentId", "name name");
            Assert.assertEquals("QEUIYN327647857657657657656576576", parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getParentIdWithSlashOnParentEntityAndMeaslesTest() {
        try {
            OpenMRSDate openMRSDate = getOpenMRSDate();
            OpenMRSCalculation openMRSCalculation = getOpenMRSCalculation();

            Due due = getDue();
            List<Due> dueList = new ArrayList<>();
            dueList.add(due);

            Expiry expiry = getExpiry();
            List<Expiry> expiryList = new ArrayList<>();
            expiryList.add(expiry);

            Condition condition = getCondition();
            List<Condition> conditionList = new ArrayList<>();
            conditionList.add(condition);

            Schedule schedule = getSchedule(dueList, expiryList, conditionList);
            Map<String, Schedule> stringScheduleMap = new HashMap<>();
            stringScheduleMap.put("schedule", schedule);

            openMRSDate.parent_entity = "measles / measles";
            org.smartregister.immunization.domain.jsonmapping.Vaccine jsonMappingVaccine = getVaccine(openMRSDate,
                    openMRSCalculation, schedule, stringScheduleMap);
            List<org.smartregister.immunization.domain.jsonmapping.Vaccine> vaccineList = new ArrayList<>();
            vaccineList.add(jsonMappingVaccine);

            VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
            Assert.assertNotNull(vaccineIntentService);

            Whitebox.setInternalState(vaccineIntentService, "availableVaccines", new ArrayList<>());
            Whitebox.setInternalState(vaccineIntentService, "specialVaccines", vaccineList);
            String parentId = Whitebox.invokeMethod(vaccineIntentService, "getParentId", "name name");
            Assert.assertEquals("measles / measles", parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getParentIdWithSlashOnParentEntityAndMrTest() {
        try {
            OpenMRSDate openMRSDate = getOpenMRSDate();
            OpenMRSCalculation openMRSCalculation = getOpenMRSCalculation();

            Due due = getDue();
            List<Due> dueList = new ArrayList<>();
            dueList.add(due);

            Expiry expiry = getExpiry();
            List<Expiry> expiryList = new ArrayList<>();
            expiryList.add(expiry);

            Condition condition = getCondition();
            List<Condition> conditionList = new ArrayList<>();
            conditionList.add(condition);

            Schedule schedule = getSchedule(dueList, expiryList, conditionList);
            Map<String, Schedule> stringScheduleMap = new HashMap<>();
            stringScheduleMap.put("schedule", schedule);

            openMRSDate.parent_entity = "measles / mr";
            org.smartregister.immunization.domain.jsonmapping.Vaccine jsonMappingVaccine = getVaccine(openMRSDate,
                    openMRSCalculation, schedule, stringScheduleMap);
            List<org.smartregister.immunization.domain.jsonmapping.Vaccine> vaccineList = new ArrayList<>();
            vaccineList.add(jsonMappingVaccine);

            VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
            Assert.assertNotNull(vaccineIntentService);

            Whitebox.setInternalState(vaccineIntentService, "availableVaccines", new ArrayList<>());
            Whitebox.setInternalState(vaccineIntentService, "specialVaccines", vaccineList);
            String parentId = Whitebox.invokeMethod(vaccineIntentService, "getParentId", "name name");
            Assert.assertEquals("measles / mr", parentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
