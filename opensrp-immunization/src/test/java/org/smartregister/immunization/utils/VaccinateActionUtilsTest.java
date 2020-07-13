package org.smartregister.immunization.utils;

import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.AllConstants;
import org.smartregister.Context;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.domain.SyncStatus;
import org.smartregister.domain.form.FormSubmission;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.repository.AlertRepository;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;
import org.smartregister.util.FormUtils;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by real on 31/10/17.
 */

@PrepareForTest({FormUtils.class, VaccinatorUtils.class, ImmunizationLibrary.class})
public class VaccinateActionUtilsTest extends BaseUnitTest {

    public static final String WOMAN = "woman";
    private final String magicData = "data";
    private final String magicChild = "child";
    private final String magicNULL = "NULL";
    private final String magicBCG = "BCG";
    private final int magic400 = 400;
    private final String magicID = "uselessentityID";
    private final int magic2 = 2;
    private final int magic12 = 12;

    @Rule
    public PowerMockRule rule = new PowerMockRule();
    @Mock
    private VaccinateActionUtils vaccinateActionUtils;
    @Mock
    private FormUtils formUtils;

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
        Assert.assertNotNull(vaccinateActionUtils);

        mockImmunizationLibrary(immunizationLibrary, context, vaccineRepository, alertService, appProperties);
        Mockito.doReturn(VaccineRepo.Vaccine.values()).when(immunizationLibrary).getVaccines();
    }

    @Test
    public void assertFormDataTestWithTestData() throws Exception {
        android.content.Context context = Mockito.mock(android.content.Context.class);
        PowerMockito.mockStatic(FormUtils.class);
        Assert.assertNull(VaccinateActionUtils.formData(context, "", "", ""));
        PowerMockito.when(FormUtils.getInstance(org.mockito.ArgumentMatchers.any(android.content.Context.class)))
                .thenReturn(formUtils);
        PowerMockito.when(formUtils.generateXMLInputForFormWithEntityId(org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString())).thenReturn(magicData);
        Assert.assertEquals(VaccinateActionUtils.formData(context, "", "", ""), magicData);

    }

    @Test
    public void assertAddDialogHookCustomFilterTestForDifferentAgeAndVaccines() {
        VaccineWrapper vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("36");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.opv1);
        Assert.assertTrue(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper));

        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("64");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.opv2);
        Assert.assertTrue(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper));

        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("92");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.opv3);
        Assert.assertTrue(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper));

        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("251");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.measles1);
        Assert.assertTrue(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper));

        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("351");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.measles2);
        Assert.assertTrue(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper));

        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("0");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.bcg);
        Assert.assertTrue(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper));

    }

    @Test
    public void assertFindRowTestReturnsTableRow() {
        String tag = "TAG";
        String wrong_tag = "WRONG TAG";
        Set<TableLayout> tables = new HashSet<>();
        TableLayout tableLayout = new TableLayout(RuntimeEnvironment.application);
        TableRow row = new TableRow(RuntimeEnvironment.application);
        row.setTag(tag);
        tableLayout.addView(row);
        tables.add(tableLayout);
        Assert.assertNotNull(VaccinateActionUtils.findRow(tables, tag));
        Assert.assertNull(VaccinateActionUtils.findRow(tables, wrong_tag));
        Assert.assertNotNull(VaccinateActionUtils.findRow(tableLayout, tag));
        Assert.assertNull(VaccinateActionUtils.findRow(tableLayout, wrong_tag));
    }

    @Test
    public void assertRetrieveFieldOveridesTestReturnsFieldOverieds() {
        String s = "{\"fieldOverrides\":\"{}\"}";
        Assert.assertNotNull(VaccinateActionUtils.retrieveFieldOverides(s));
        //throws Exception
        Assert.assertNotNull(VaccinateActionUtils.retrieveFieldOverides("{}"));
    }

    @Test
    public void assertAllAlertNamesTestReturnsAlertForACategory() {

        List<ServiceType> typeList = new ArrayList<>();
        HashMap<String, List<ServiceType>> collection = new HashMap<>();
        ServiceType st = new ServiceType();
        st.setName("SERVICE NAME");
        collection.put("1", typeList);
        collection.put("2", null);
        Assert.assertNotNull(VaccinateActionUtils.allAlertNames(collection.values()));

        String[] childVaccines = VaccinateActionUtils.allAlertNames(magicChild);
        String[] womanVaccines = VaccinateActionUtils.allAlertNames(WOMAN);

        Assert.assertNotNull(childVaccines);
        Assert.assertEquals(childVaccines.length, 88);

        Assert.assertNotNull(womanVaccines);
        Assert.assertEquals(womanVaccines.length, 10);

        Assert.assertNull(VaccinateActionUtils.allAlertNames(magicNULL));
    }

    @Test
    public void assertUpdateJsonAndFindTestReturnsJsonObject() throws Exception {
        JSONObject object = new JSONObject();
        String field = "FIELD";
        String value = "value";
        object.put(field, new JSONObject());
        //adds value to the new object
        VaccinateActionUtils.updateJson(object, field, value);
        object = new JSONObject();
        object.put(field, "");
        //throws Exception
        VaccinateActionUtils.updateJson(object, field, value);

        object = new JSONObject();

        object.put(field, new JSONObject());
        //finds value to the new object, should not be null
        Assert.assertNotNull(VaccinateActionUtils.find(object, field));
        object = new JSONObject();
        object.put(field, "");
        //throws Exception
        VaccinateActionUtils.find(object, field);
        object = new JSONObject();
        //return null
        Assert.assertNull(VaccinateActionUtils.find(object, field));

    }

    @Test
    public void assertHyphenTestReturnsHyphenatedString() {
        Assert.assertNotNull(VaccinateActionUtils.addHyphen(""));
        Assert.assertNotNull(VaccinateActionUtils.addHyphen("a b"));
        Assert.assertNotNull(VaccinateActionUtils.removeHyphen(""));
        Assert.assertNotNull(VaccinateActionUtils.removeHyphen("a_b"));
    }

    @Test
    public void assertAddBcg2SpecialVaccineTestReturnsSpecialVaccinesOnHasVaccineAndGetVaccineMethods() {
        List<Vaccine> list = new ArrayList<>();
        Vaccine v = new Vaccine();
        v.setName("BCG 2");
        list.add(v);
        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);
        PowerMockito.mockStatic(VaccinatorUtils.class);

        listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = JsonFormUtils.gson
                .fromJson(VaccineData.special_vacines, listType);

        PowerMockito
                .when(VaccinatorUtils.getSpecialVaccines(org.mockito.ArgumentMatchers.any(android.content.Context.class)))
                .thenReturn(specialVaccines);
        VaccinateActionUtils.addBcg2SpecialVaccine(Mockito.mock(android.content.Context.class), vaccines.get(0), list);

        PowerMockito
                .when(VaccinatorUtils.getSpecialVaccines(org.mockito.ArgumentMatchers.any(android.content.Context.class)))
                .thenReturn(null);
        VaccinateActionUtils.addBcg2SpecialVaccine(Mockito.mock(android.content.Context.class), vaccines.get(0), list);

        //choto related methods

        //hasvaccines
        Assert.assertEquals(VaccinateActionUtils.hasVaccine(null, null), false);
        Assert.assertEquals(VaccinateActionUtils.hasVaccine(list, VaccineRepo.Vaccine.opv0), false);
        //getvaccines
        Assert.assertNull(VaccinateActionUtils.getVaccine(null, null));
        Assert.assertNull(VaccinateActionUtils.getVaccine(list, VaccineRepo.Vaccine.opv0));
        Assert.assertNotNull(VaccinateActionUtils.getVaccine(list, VaccineRepo.Vaccine.bcg2));

    }

    @Test
    public void assertPopulateDefaultAlertsTestReturnsAlerts() {
        VaccinateActionUtils.populateDefaultAlerts(null, null, null, null, null, null);
        List<Vaccine> vlist = new ArrayList<>();
        Vaccine v = new Vaccine();
        v.setName(magicBCG);
        vlist.add(v);
        List<Alert> alist = new ArrayList<Alert>();
        Alert a = new Alert("caseID", magicBCG, magicBCG, AlertStatus.normal, new Date().toString(), new Date().toString());
        alist.add(a);
        VaccineRepo.Vaccine[] vaccine = {VaccineRepo.Vaccine.bcg};
        AlertService alertService = new AlertService(Mockito.mock(AlertRepository.class));
        VaccinateActionUtils.populateDefaultAlerts(alertService, vlist, alist, magicID, new DateTime(), vaccine);
        VaccineRepo.Vaccine[] vaccine2 = {VaccineRepo.Vaccine.bcg2};
        VaccinateActionUtils.populateDefaultAlerts(alertService, vlist, alist, magicID, new DateTime(), vaccine2);


        Assert.assertEquals(VaccinateActionUtils.hasAlert(alist, VaccineRepo.Vaccine.bcg2), false);
        Assert.assertEquals(VaccinateActionUtils.hasAlert(alist, VaccineRepo.Vaccine.bcg), true);
        Assert.assertEquals(VaccinateActionUtils.hasAlert(null, VaccineRepo.Vaccine.bcg), false);

        Assert.assertNull(VaccinateActionUtils.getAlert(alist, VaccineRepo.Vaccine.bcg2));
        Assert.assertNotNull(VaccinateActionUtils.getAlert(alist, VaccineRepo.Vaccine.bcg));
        Assert.assertNull(VaccinateActionUtils.getAlert(null, VaccineRepo.Vaccine.bcg));


    }

    @Test
    public void assertCreateDefaultAlertTestReturnsAlert() {
        DateTime dateTime = new DateTime();

        Assert.assertNotNull(VaccinateActionUtils.createDefaultAlert(VaccineRepo.Vaccine.opv0, "", dateTime));
        dateTime = new DateTime();
        dateTime = dateTime.plusDays(magic2);
        Assert.assertNotNull(VaccinateActionUtils.createDefaultAlert(VaccineRepo.Vaccine.opv0, "", dateTime));
        dateTime = new DateTime();
        dateTime = dateTime.minusDays(magic2);
        Assert.assertNotNull(VaccinateActionUtils.createDefaultAlert(VaccineRepo.Vaccine.opv0, "", dateTime));
        dateTime = new DateTime();
        dateTime = dateTime.plusDays(magic400);
        Assert.assertNotNull(VaccinateActionUtils.createDefaultAlert(VaccineRepo.Vaccine.opv0, "", dateTime));
        dateTime = new DateTime();
        dateTime = dateTime.minusDays(magic400);
        Assert.assertNotNull(VaccinateActionUtils.createDefaultAlert(VaccineRepo.Vaccine.opv0, "", dateTime));
        dateTime = new DateTime();
        dateTime = dateTime.minusDays(magic12);
        Assert.assertNotNull(VaccinateActionUtils.createDefaultAlert(VaccineRepo.Vaccine.opv0, "", dateTime));

    }


    @Test
    public void assertMoreThanThreeMonthsReturnsTrueForDatesMoreThanThreeMonths() {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusMonths(magic2);

        Assert.assertFalse(VaccinateActionUtils.moreThanThreeMonths(new Date(dateTime.getMillis())));


        Assert.assertFalse(VaccinateActionUtils.moreThanThreeMonths(new Date(dateTime.getMillis())));

        dateTime = new DateTime();
        dateTime = dateTime.minusMonths(3).minusHours(1);

        Assert.assertTrue(VaccinateActionUtils.moreThanThreeMonths(new Date(dateTime.getMillis())));

        dateTime = new DateTime();
        dateTime = dateTime.minusMonths(3);
        dateTime = dateTime.minusWeeks(magic2);

        Assert.assertTrue(VaccinateActionUtils.moreThanThreeMonths(new Date(dateTime.getMillis())));

    }

    @Test
    public void assertLessThanThreeMonthsReturnsTrueForCreatedAtVaccineDatesLessThanThreeMonths() {

        Vaccine vaccine = null;
        Assert.assertTrue(VaccinateActionUtils.lessThanThreeMonths(vaccine));

        vaccine = new Vaccine();
        Assert.assertTrue(VaccinateActionUtils.lessThanThreeMonths(vaccine));

        vaccine = new Vaccine();
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusMonths(magic2);
        vaccine.setCreatedAt(new Date(dateTime.getMillis()));
        Assert.assertTrue(VaccinateActionUtils.lessThanThreeMonths(vaccine));

        vaccine = new Vaccine();
        dateTime = new DateTime();
        dateTime = dateTime.minusMonths(5);
        vaccine.setCreatedAt(new Date(dateTime.getMillis()));
        Assert.assertFalse(VaccinateActionUtils.lessThanThreeMonths(vaccine));


    }

    @Test
    public void assertLessThanThreeMonthsReturnsTrueForCreatedAtServiceRecordDatesLessThanThreeMonths() {

        ServiceRecord serviceRecord = null;
        Assert.assertTrue(VaccinateActionUtils.lessThanThreeMonths(serviceRecord));

        serviceRecord = new ServiceRecord();
        Assert.assertTrue(VaccinateActionUtils.lessThanThreeMonths(serviceRecord));

        serviceRecord = new ServiceRecord();
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusMonths(magic2);
        serviceRecord.setCreatedAt(new Date(dateTime.getMillis()));
        Assert.assertTrue(VaccinateActionUtils.lessThanThreeMonths(serviceRecord));

        serviceRecord = new ServiceRecord();
        dateTime = new DateTime();
        dateTime = dateTime.minusMonths(5);
        serviceRecord.setCreatedAt(new Date(dateTime.getMillis()));
        Assert.assertFalse(VaccinateActionUtils.lessThanThreeMonths(serviceRecord));
    }

    @Test
    public void getParamsShouldProvideJsonStringWith5properties() throws JSONException {
        FormSubmission formSubmission = new FormSubmission("instance-id", "entity-id", "form-name", "{}", "client-version", SyncStatus.SYNCED, "synced");
        String jsonString = ReflectionHelpers.callStaticMethod(VaccinateActionUtils.class, "getParams", ReflectionHelpers.ClassParameter.from(FormSubmission.class, formSubmission));

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray keys = jsonObject.names();

        Assert.assertEquals(5, keys.length());
        Assert.assertEquals("instance-id", jsonObject.getString(AllConstants.INSTANCE_ID_PARAM));
        Assert.assertEquals("entity-id", jsonObject.getString(AllConstants.ENTITY_ID_PARAM));
        Assert.assertEquals("form-name", jsonObject.getString(AllConstants.FORM_NAME_PARAM));
        Assert.assertEquals("client-version", jsonObject.getString(AllConstants.VERSION_PARAM));
        Assert.assertEquals(SyncStatus.PENDING.value(), jsonObject.getString(AllConstants.SYNC_STATUS));
    }

}
