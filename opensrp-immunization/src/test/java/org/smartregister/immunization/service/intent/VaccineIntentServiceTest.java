package org.smartregister.immunization.service.intent;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
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
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onaio on 30/08/2017.
 */
public class VaccineIntentServiceTest extends BaseUnitTest {

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private org.smartregister.Context drishtiContext;

    @Mock
    private AlertService alertService;

    @Mock
    private AppProperties appProperties;

    @Spy
    private List<Vaccine> vaccineList = new ArrayList<>();

    @Mock
    private List<VaccineGroup> availableVaccines;

    @Mock
    private List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialJsonMappingVaccines;

    @Before
    public void setUp() {
        mockImmunizationLibrary(immunizationLibrary, drishtiContext, vaccineRepository, alertService, appProperties);
    }

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        Assert.assertNotNull(new VaccineIntentService());
    }

    @Test
    public void onHandleIntentTest() throws Exception {
        Application application = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(application, VaccineIntentService.class);

        VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
        Assert.assertNotNull(vaccineIntentService);

        try (MockedStatic<VaccinatorUtils> vaccinatorUtils = Mockito.mockStatic(VaccinatorUtils.class)) {
            vaccinatorUtils.when(() -> VaccinatorUtils.getSupportedVaccines(ArgumentMatchers.any(Context.class))).thenReturn(availableVaccines);
            vaccinatorUtils.when(() -> VaccinatorUtils.getSpecialVaccines(ArgumentMatchers.any(Context.class))).thenReturn(specialJsonMappingVaccines);

            Mockito.when(immunizationLibrary.vaccineRepository()).thenReturn(vaccineRepository);
            Mockito.when(immunizationLibrary.context()).thenReturn(drishtiContext);
            Mockito.when(immunizationLibrary.allowSyncImmediately()).thenReturn(false);
            Mockito.when(immunizationLibrary.getVaccineSyncTime()).thenReturn(1L);

            Vaccine vaccine = new Vaccine(0L, VaccineTest.BASEENTITYID, VaccineTest.NAME, 0, new Date(),
                    VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0L,
                    VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
            Mockito.when(vaccineRepository.findUnSyncedBeforeTime(IMConstants.VACCINE_SYNC_TIME))
                    .thenReturn(vaccineList);
            vaccineList.add(vaccine);
            Assert.assertNotNull(vaccineList);

            setField(vaccineIntentService, "vaccineRepository", vaccineRepository);
            setField(vaccineIntentService, "immunizationLibrary", immunizationLibrary);
            vaccineIntentService.onHandleIntent(intent);
        }
    }

    @Test
    public void getEventTypeTest() {
        VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
        Assert.assertNotNull(vaccineIntentService);

        String eventType = vaccineIntentService.getEventType();
        Assert.assertNotNull(eventType);
        Assert.assertEquals("Vaccination", eventType);
    }

    @Test
    public void getEntityTypeTest() {
        VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
        Assert.assertNotNull(vaccineIntentService);

        String entityType = vaccineIntentService.getEntityType();
        Assert.assertNotNull(entityType);
        Assert.assertEquals("vaccination", entityType);
    }

    @Test
    public void getEventTypeOutOfCatchmentTest() {
        VaccineIntentService vaccineIntentService = Mockito.spy(new VaccineIntentService());
        Assert.assertNotNull(vaccineIntentService);

        String eventTypeOutOfCatchment = vaccineIntentService.getEventTypeOutOfCatchment();
        Assert.assertNotNull(eventTypeOutOfCatchment);
        Assert.assertEquals("Out of Area Service - Vaccination", eventTypeOutOfCatchment);
    }

    @Test
    public void getParentIdAvailableVaccinesTest() throws Exception {
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

        setField(vaccineIntentService, "availableVaccines", vaccineGroups);
        setField(vaccineIntentService, "specialVaccines", new ArrayList<>());
        String parentId = invokeGetParentId(vaccineIntentService, "name");
        Assert.assertEquals("QEUIYN327647857657657657656576576", parentId);
    }

    @Test
    public void getParentIdWithSpecialVaccinesTest() throws Exception {
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

        setField(vaccineIntentService, "availableVaccines", new ArrayList<>());
        setField(vaccineIntentService, "specialVaccines", vaccineList);
        String parentId = invokeGetParentId(vaccineIntentService, "name");
        Assert.assertEquals("QEUIYN327647857657657657656576576", parentId);
    }

    @Test
    public void getParentIdWithTwoNamesTest() throws Exception {
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

        setField(vaccineIntentService, "availableVaccines", new ArrayList<>());
        setField(vaccineIntentService, "specialVaccines", vaccineList);
        String parentId = invokeGetParentId(vaccineIntentService, "name name-two");
        Assert.assertEquals("QEUIYN327647857657657657656576576", parentId);
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

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private String invokeGetParentId(VaccineIntentService service, String name) throws Exception {
        Method method = VaccineIntentService.class.getDeclaredMethod("getParentId", String.class);
        method.setAccessible(true);
        return (String) method.invoke(service, name);
    }
}
