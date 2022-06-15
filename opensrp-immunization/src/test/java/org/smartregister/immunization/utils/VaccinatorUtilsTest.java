package org.smartregister.immunization.utils;

import android.content.res.Resources;

import com.google.common.collect.ImmutableList;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.domain.ANM;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.BuildConfig;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.ServiceData;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.jsonmapping.Due;
import org.smartregister.immunization.domain.jsonmapping.Expiry;
import org.smartregister.immunization.domain.jsonmapping.Schedule;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.IMDatabaseUtils;
import org.smartregister.immunization.util.JsonFormUtils;
import org.smartregister.immunization.util.RecurringServiceUtils;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.repository.AllSettings;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.service.ANMService;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.Utils;
import org.smartregister.view.controller.ANMController;
import org.smartregister.view.controller.ANMLocationController;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;

/**
 * Created by onaio on 29/08/2017.
 */
 
public class VaccinatorUtilsTest extends BaseUnitTest {

    private final int magicColor = 255;
    private final String magicOPV0 = "OPV 0";
    private final String magicNULL = "NULL";
    private final String UNTRANSLATED = "translate";
    private final String TRANSLATED = "tafsiri";
    private final int RESOURCE_ID = 123456;
    private final String UNTRANSLATED_GROUP_NAME = "6 weeks";
    private final String TRANSLATED_GROUP_NAME = "6 weeks";

    @Mock
    private ImmunizationLibrary immunizationLibrary;
    @Mock
    private Context context;
    @Mock
    private CommonRepository commonRepository;
    @Mock
    private Resources resources;

    @Test
    public void verifygetwastedcallssqlmethodonce() {
        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        Mockito.when(ImmunizationLibrary.getInstance().context().commonrepository(ArgumentMatchers.anyString()))
                .thenReturn(commonRepository);
        VaccinatorUtils.getWasted("", "", "");
        VaccinatorUtils.getWasted("", "", "", "");
        Mockito.verify(commonRepository, Mockito.times(1))
                .rawQuery(org.mockito.ArgumentMatchers.anyString(), eq(new String[]{}));
    }

    @Test
    public void verifyGetUsedcallssqlmethodonce() {
        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        Mockito.when(ImmunizationLibrary.getInstance().context().commonrepository(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(commonRepository);
        VaccinatorUtils.getUsed("", "", "", "", "");
        Mockito.verify(commonRepository, Mockito.times(1))
                .rawQuery(org.mockito.ArgumentMatchers.anyString(), eq(new String[]{}));
    }

    @Test
    public void assertVaccinatorUtilsTest() {

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(immunizationLibrary.getLocale()).thenReturn(new Locale("en"));
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        Mockito.when(ImmunizationLibrary.getInstance().context().commonrepository(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(commonRepository);

        android.content.Context ctx = Mockito.mock(android.content.Context.class);
        Resources resources = Mockito.mock(Resources.class);
        Mockito.when(ctx.getResources()).thenReturn(resources);
        Mockito.when(resources.getColor(org.mockito.ArgumentMatchers.anyInt())).thenReturn(magicColor);

        //get file name with prefix
        Assert.assertEquals(VaccinatorUtils.getFileName("vaccines.json", "tz"), "tz_vaccines.json");

        //get file name without prefix
        Assert.assertEquals(VaccinatorUtils.getFileName("vaccines.json", null), "vaccines.json");


        //get color test for different status
        Assert.assertNotNull(VaccinatorUtils.getColorValue(ctx, AlertStatus.upcoming));
        Assert.assertNotNull(VaccinatorUtils.getColorValue(ctx, AlertStatus.normal));
        Assert.assertNotNull(VaccinatorUtils.getColorValue(ctx, AlertStatus.urgent));
        Assert.assertNotNull(VaccinatorUtils.getColorValue(ctx, AlertStatus.expired));
        //get getServiceExpiryDate null test
        Assert.assertNull(VaccinatorUtils.getServiceExpiryDate(null, null));
        Assert.assertNotNull(new IMConstants());
        Assert.assertNotNull(new JsonFormUtils());
        Assert.assertNotNull(new RecurringServiceUtils());
        Assert.assertNotNull(new IMDatabaseUtils());
        Assert.assertNotNull(new VaccinateActionUtils());
        Assert.assertNotNull(new VaccinatorUtils());
        Assert.assertEquals(IMConstants.VACCINE_SYNC_TIME, BuildConfig.VACCINE_SYNC_TIME);
        //        VaccinatorUtils powerspy = Mockito.spy(vaccinatorUtils);
        //        Mockito.doReturn(new HashMap<String, Object>()).when(VaccinatorUtils.class, "createVaccineMap", mock(String.class), mock(Alert.class), mock(DateTime.class), mock(ServiceType.class));
    }

    @Test
    public void assertGetVaccineDisplayNameTestReturnsDisplayName() {
        android.content.Context context = Mockito.mock(android.content.Context.class);
        Mockito.mockStatic(ImmunizationLibrary.class);

        Mockito.when(immunizationLibrary.getLocale()).thenReturn(new Locale("en"));
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);

        Class<List<VaccineGroup>> clazz = (Class) List.class;
        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();

        List<VaccineGroup> vaccineGroups = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);

        Map<String, Object> jsonMap = new HashMap<>();
        Mockito.when(ImmunizationLibrary.assetJsonToJava(jsonMap, context, "vaccines.json", clazz, listType)).thenReturn(vaccineGroups);

        Assert.assertNotNull(VaccinatorUtils.getVaccineDisplayName(context, "Birth"));
        Assert.assertNotNull(VaccinatorUtils.getVaccineDisplayName(context, magicOPV0));

    }

    @Test
    public void assertReceivedServicesTestReturnsService() {
        List<ServiceRecord> serviceRecordList = new ArrayList<ServiceRecord>();
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setName(magicNULL);
        serviceRecord.setDate(new Date());
        serviceRecordList.add(serviceRecord);
        Assert.assertNotNull(VaccinatorUtils.receivedServices(serviceRecordList));
    }

    @Test
    public void assertReceivedVaccinesTestReturnsVaccine() {
        List<Vaccine> vaccines = new ArrayList<Vaccine>();
        Vaccine v = new Vaccine();
        v.setName(magicNULL);
        v.setDate(new Date());
        vaccines.add(v);
        Assert.assertNotNull(VaccinatorUtils.receivedVaccines(vaccines));
    }

    @Test
    public void assertGetVaccineCalculationTestReturnsCalculation() throws Exception {
        android.content.Context context = Mockito.mock(android.content.Context.class);
        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.mockStatic(Utils.class);

        Mockito.when(immunizationLibrary.getLocale()).thenReturn(new Locale("en"));
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);

        Class<List<VaccineGroup>> clazz = (Class) List.class;
        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();

        Map<String, Object> jsonMap = new HashMap<>();

        List<VaccineGroup> vaccineGroups = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);
        Mockito.when(ImmunizationLibrary.assetJsonToJava(jsonMap, context, "vaccines.json", clazz, listType)).thenReturn(vaccineGroups);

        Assert.assertEquals(VaccinatorUtils.getVaccineCalculation(context, magicOPV0), 0);
        Assert.assertEquals(VaccinatorUtils.getVaccineCalculation(context, magicNULL), -1);

        //readspecialvaccines
        Class<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>> clazz2 = (Class) List.class;
        Type listType2 = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>>() {
        }.getType();

        List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = JsonFormUtils.gson
                .fromJson(VaccineData.special_vacines, listType2);

        Map<String, Object> jsonMap2 = new HashMap<>();

        Mockito.when(ImmunizationLibrary.assetJsonToJava(jsonMap2, context, "special_vaccines.json", clazz2, listType2)).thenReturn(specialVaccines);

        JSONAssert.assertEquals(VaccineData.special_vacines,
                JsonFormUtils.gson.toJson(VaccinatorUtils.getSpecialVaccines(context), listType2),
                JSONCompareMode.NON_EXTENSIBLE);
        //readrecurringservices
        Mockito.when(Utils.readAssetContents(org.mockito.ArgumentMatchers.any(android.content.Context.class),
                org.mockito.ArgumentMatchers.anyString())).thenReturn(ServiceData.recurringservice);
        Assert.assertEquals(VaccinatorUtils.getSupportedRecurringServices(context), ServiceData.recurringservice);
    }

    @Test
    public void testTranslateNameInvokesCorrectApiMethods() {

        android.content.Context context = Mockito.mock(android.content.Context.class);
        Mockito.mockStatic(Utils.class);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(RESOURCE_ID)).thenReturn(TRANSLATED);
        Mockito.when(resources.getIdentifier(UNTRANSLATED, "string", context.getPackageName())).thenReturn(RESOURCE_ID);

        String translated = VaccinatorUtils.translate(context, UNTRANSLATED);

        Assert.assertEquals(TRANSLATED, translated);
    }

    @Test
    public void testGetTranslatedVaccineName() {

        android.content.Context context = Mockito.mock(android.content.Context.class);
        Mockito.mockStatic(Utils.class);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getString(123)).thenReturn(TRANSLATED);
        Mockito.when(resources.getIdentifier(UNTRANSLATED, "string", context.getPackageName())).thenReturn(123);

        String translated = VaccinatorUtils.getTranslatedVaccineName(context, UNTRANSLATED);
        Assert.assertEquals(TRANSLATED, translated);

        //Combo Vaccines
        translated = VaccinatorUtils.getTranslatedVaccineName(context, UNTRANSLATED + " / " + UNTRANSLATED);
        Assert.assertEquals(TRANSLATED + " / " + TRANSLATED, translated);

    }

    @Test
    public void testGetTranslatedGroupName() {

        android.content.Context context = Mockito.mock(android.content.Context.class);
        Mockito.mockStatic(Utils.class);
        Mockito.when(context.getResources()).thenReturn(resources);
        Mockito.when(resources.getIdentifier(UNTRANSLATED, "string", context.getPackageName())).thenReturn(RESOURCE_ID);

        //Test Group names
        String translated = VaccinatorUtils.translate(context, UNTRANSLATED_GROUP_NAME);

        Assert.assertEquals(TRANSLATED_GROUP_NAME, translated);
    }

    @Test
    public void testCreateIdentifierWorksCorrectly() {

        String translated = VaccinatorUtils.createIdentifier("4 Weeks");

        Assert.assertEquals("_4_weeks", translated);
    }

    @Test
    public void testGetPrerequisiteVaccineReturnsCorrectVaccine() {

        org.smartregister.immunization.domain.jsonmapping.Vaccine vaccine = new org.smartregister.immunization.domain.jsonmapping.Vaccine();
        Schedule schedule = new Schedule();
        Due due = new Due();
        due.prerequisite = "opv1";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        VaccineRepo.Vaccine repoVaccine = VaccinatorUtils.getPrerequisiteVaccine(vaccine);
        Assert.assertEquals(VaccineRepo.Vaccine.opv1, repoVaccine);

        schedule = new Schedule();
        due = new Due();
        due.prerequisite = "penta1";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        repoVaccine = VaccinatorUtils.getPrerequisiteVaccine(vaccine);
        Assert.assertEquals(VaccineRepo.Vaccine.penta1, repoVaccine);

        schedule = new Schedule();
        due = new Due();
        due.prerequisite = "rtss1";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        repoVaccine = VaccinatorUtils.getPrerequisiteVaccine(vaccine);
        Assert.assertEquals(VaccineRepo.Vaccine.rtss1, repoVaccine);

        schedule = new Schedule();
        due = new Due();
        due.prerequisite = "tt1";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        repoVaccine = VaccinatorUtils.getPrerequisiteVaccine(vaccine);
        Assert.assertEquals(VaccineRepo.Vaccine.tt1, repoVaccine);

        schedule = new Schedule();
        due = new Due();
        due.prerequisite = "mrce";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        repoVaccine = VaccinatorUtils.getPrerequisiteVaccine(vaccine);
        Assert.assertEquals(VaccineRepo.Vaccine.mrce, repoVaccine);

        schedule = new Schedule();
        due = new Due();
        due.prerequisite = "measles1";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        repoVaccine = VaccinatorUtils.getPrerequisiteVaccine(vaccine);
        Assert.assertEquals(VaccineRepo.Vaccine.measles1, repoVaccine);
    }

    @Test
    public void testGetPrerequisiteGapDaysReturnsCorrectValue() {

        org.smartregister.immunization.domain.jsonmapping.Vaccine vaccine = new org.smartregister.immunization.domain.jsonmapping.Vaccine();
        Schedule schedule = new Schedule();
        Due due = new Due();
        due.offset = "+20d";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        int days = VaccinatorUtils.getPrerequisiteGapDays(vaccine);
        Assert.assertEquals(20, days);

        schedule = new Schedule();
        due = new Due();
        due.offset = "+5m";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getPrerequisiteGapDays(vaccine);
        Assert.assertEquals(152, days);

        schedule = new Schedule();
        due = new Due();
        due.offset = "+3y";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getPrerequisiteGapDays(vaccine);
        Assert.assertEquals(1098, days);

        schedule = new Schedule();
        due = new Due();
        due.offset = "+3m,10d";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getPrerequisiteGapDays(vaccine);
        Assert.assertEquals(101, days);

        schedule = new Schedule();
        due = new Due();
        due.offset = "+2y,9m";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getPrerequisiteGapDays(vaccine);
        Assert.assertEquals(1006, days);


        schedule = new Schedule();
        due = new Due();
        due.offset = "+1y,9m,9d";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getPrerequisiteGapDays(vaccine);
        Assert.assertEquals(649, days);

        schedule = new Schedule();
        due = new Due();
        due.offset = "+2y,8d";
        schedule.due = ImmutableList.of(due);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getPrerequisiteGapDays(vaccine);
        Assert.assertEquals(740, days);
    }

    @Test
    public void testGetExpiryDaysReturnsCorrectValue() {

        org.smartregister.immunization.domain.jsonmapping.Vaccine vaccine = new org.smartregister.immunization.domain.jsonmapping.Vaccine();
        Schedule schedule = new Schedule();
        Expiry expiry = new Expiry();
        expiry.offset = "+10d";
        schedule.expiry = ImmutableList.of(expiry);
        vaccine.setSchedule(schedule);
        int days = VaccinatorUtils.getExpiryDays(vaccine);
        Assert.assertEquals(10, days);

        schedule = new Schedule();
        expiry = new Expiry();
        expiry.offset = "+4m";
        schedule.expiry = ImmutableList.of(expiry);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getExpiryDays(vaccine);
        Assert.assertEquals(122, days);

        schedule = new Schedule();
        expiry = new Expiry();
        expiry.offset = "+2y";
        schedule.expiry = ImmutableList.of(expiry);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getExpiryDays(vaccine);
        Assert.assertEquals(732, days);

        schedule = new Schedule();
        expiry = new Expiry();
        expiry.offset = "+4m,10d";
        schedule.expiry = ImmutableList.of(expiry);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getExpiryDays(vaccine);
        Assert.assertEquals(132, days);

        schedule = new Schedule();
        expiry = new Expiry();
        expiry.offset = "+1y,9m";
        schedule.expiry = ImmutableList.of(expiry);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getExpiryDays(vaccine);
        Assert.assertEquals(640, days);

        schedule = new Schedule();
        expiry = new Expiry();
        expiry.offset = "+1y,9m,3d";
        schedule.expiry = ImmutableList.of(expiry);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getExpiryDays(vaccine);
        Assert.assertEquals(643, days);

        schedule = new Schedule();
        expiry = new Expiry();
        expiry.offset = "+1y,4d";
        schedule.expiry = ImmutableList.of(expiry);
        vaccine.setSchedule(schedule);
        days = VaccinatorUtils.getExpiryDays(vaccine);
        Assert.assertEquals(370, days);
    }


    @Test
    public void testCleanVaccineNameProcessesInputsCorrectly() {

        String normalVaccine = VaccinatorUtils.cleanVaccineName("BCG 1");

        Assert.assertNotNull(normalVaccine);
        Assert.assertEquals("bcg1", normalVaccine);

        String rss1 = VaccinatorUtils.cleanVaccineName("RSS,S1");

        Assert.assertNotNull(rss1);
        Assert.assertEquals("rsss1", rss1);

        String mrce = VaccinatorUtils.cleanVaccineName("MR - CE");

        Assert.assertNotNull(mrce);
        Assert.assertEquals("mrce", mrce);
    }

    @Test
    public void testNextVaccineDueUsingLastVisit() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, 11, 2);
        final DateTime someDate = new DateTime(calendar.getTime());
        calendar.set(2019, 10, 2);
        final DateTime someDate2 = new DateTime(calendar.getTime());
        HashMap<String, Object> vaccineSchedule = new HashMap<String, Object>() {{
            put("status", "due");
            put("vaccine", VaccineRepo.Vaccine.bcg2);
            put("date", someDate);
        }};
        HashMap<String, Object> vaccineSchedule2 = new HashMap<String, Object>() {{
            put("status", "due");
            put("vaccine", VaccineRepo.Vaccine.bcg2);
            put("date", someDate2);
        }};
        List<Map<String, Object>> schedules = new ArrayList<>();
        schedules.add(vaccineSchedule);
        schedules.add(vaccineSchedule2);
        calendar.set(2020, 5, 2);
        Date lastVisit = calendar.getTime();
        Map<String, Object> stringObjectMap = VaccinatorUtils.nextServiceDue(schedules, lastVisit);
        Assert.assertNotNull(stringObjectMap);
        Assert.assertEquals("due", stringObjectMap.get("status"));
    }

    @Test
    public void testNextVaccineDueUsingServiceRecord() {

        final ServiceType serviceType = new ServiceType();
        serviceType.setName("Some Service Name");
        serviceType.setType("Some Type");
        HashMap<String, Object> vaccineSchedule = new HashMap<String, Object>() {{
            put("service", serviceType);
        }};

        List<Map<String, Object>> schedules = new ArrayList<>();
        schedules.add(vaccineSchedule);
        ServiceRecord serviceRecord = new ServiceRecord();
        Assert.assertNull(VaccinatorUtils.nextServiceDue(schedules, serviceRecord));
        serviceRecord.setSyncStatus(RecurringServiceRecordRepository.TYPE_Synced);
        Assert.assertNull(VaccinatorUtils.nextServiceDue(schedules, serviceRecord));
        serviceRecord.setType("Some Type");
        serviceRecord.setName("Some Service Name");
        serviceRecord.setSyncStatus(RecurringServiceRecordRepository.TYPE_Unsynced);
        Map<String, Object> stringObjectMap = VaccinatorUtils.nextServiceDue(schedules, serviceRecord);
        Assert.assertNotNull(stringObjectMap);
        Assert.assertEquals(stringObjectMap, vaccineSchedule);
    }

    @Test
    public void testNextVaccineDueFromLastVisit() {
        List<Map<String, Object>> schedules = new ArrayList<>();

        Map<String, Object> scheduleItem = new HashMap<>();
        scheduleItem.put("status", "due");
        scheduleItem.put("vaccine", VaccineRepo.Vaccine.opv1);
        scheduleItem.put("date", DateTime.parse("2020-07-13T07:21:01Z"));

        schedules.add(scheduleItem);

        scheduleItem = new HashMap<>();
        scheduleItem.put("status", "due");
        scheduleItem.put("vaccine", VaccineRepo.Vaccine.HepB);
        scheduleItem.put("date", DateTime.parse("2020-01-16T07:32:01Z"));

        schedules.add(scheduleItem);

        scheduleItem = new HashMap<>();
        scheduleItem.put("status", "due");
        scheduleItem.put("vaccine", VaccineRepo.Vaccine.bcg2);
        scheduleItem.put("date", DateTime.parse("2020-01-16T07:32:01Z"));

        schedules.add(scheduleItem);

        scheduleItem = new HashMap<>();
        scheduleItem.put("status", "due");
        scheduleItem.put("vaccine", VaccineRepo.Vaccine.penta1);
        scheduleItem.put("date", DateTime.parse("2020-05-12T07:32:01Z"));

        schedules.add(scheduleItem);

        Date lastVisitDate = java.sql.Date.valueOf(LocalDate.of(2020, 02, 03).toString());
        Map<String, Object> nextVaccineDue = VaccinatorUtils.nextVaccineDue(schedules, lastVisitDate);

        Assert.assertNotNull(nextVaccineDue);
        Assert.assertEquals(3, nextVaccineDue.size());
        Assert.assertEquals("due", nextVaccineDue.get("status"));
        Assert.assertEquals(VaccineRepo.Vaccine.penta1, nextVaccineDue.get("vaccine"));

        DateTime vaccineDate = (DateTime) nextVaccineDue.get("date");
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        String vaccineDateString = vaccineDate.toString(fmt);
        Assert.assertEquals("2020-05-12", vaccineDateString);

    }

    @Test
    public void testIsSkippableVaccineReturnsTrueForSkippableVaccines() {

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.doReturn(Arrays.asList(VaccineRepo.Vaccine.bcg2)).when(immunizationLibrary).getSkippableVaccines();

        Boolean result = VaccinatorUtils.isSkippableVaccine("BCG 2");
        Assert.assertNotNull(result);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testIsSkippableVaccineReturnsFalseForNonSkippableVaccines() {

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);

        Boolean result = VaccinatorUtils.isSkippableVaccine("OPV 1");
        Assert.assertNotNull(result);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testReadProviderDetails() {
        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);

        Mockito.doReturn(context).when(immunizationLibrary).context();

        Mockito.doReturn(Mockito.mock(ANMController.class)).when(context).anmController();
        Mockito.doReturn(Mockito.mock(AllSettings.class)).when(context).allSettings();

        AllSharedPreferences allSharedPreferences = Mockito.mock(AllSharedPreferences.class);
        Mockito.doReturn(allSharedPreferences).when(context).allSharedPreferences();

        String team = "{\"person\": {\"display\": \"\"},\"identifier\": \"\",\"team\": {\"teamName\": \"\"}}";
        Mockito.doReturn(team).when(allSharedPreferences).getPreference("team");

        ANMLocationController anmLocationController = Mockito.mock(ANMLocationController.class);
        Mockito.doReturn(anmLocationController).when(context).anmLocationController();
        String info = "{}";
        Mockito.doReturn(info).when(anmLocationController).get();

        ANMService anmService = Mockito.mock(ANMService.class);
        Mockito.doReturn(Mockito.mock(ANM.class)).when(anmService).fetchDetails();
        Mockito.doReturn(anmService).when(context).anmService();


        HashMap<String, String> result = VaccinatorUtils.providerDetails();
        Assert.assertEquals(result.size(), 10);
    }
}
