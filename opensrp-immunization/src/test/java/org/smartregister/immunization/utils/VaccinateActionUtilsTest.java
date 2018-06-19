package org.smartregister.immunization.utils;

import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.gson.reflect.TypeToken;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.json.JSONObject;
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
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.repository.AlertRepository;
import org.smartregister.service.AlertService;
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

@PrepareForTest({FormUtils.class, VaccinatorUtils.class})
public class VaccinateActionUtilsTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private VaccinateActionUtils vaccinateActionUtils;

    @Mock
    private FormUtils formUtils;

    private final String magicData = "data";
    private final String magicChild = "child";
    private final String magicNULL = "NULL";
    private final String magicBCG = "BCG";
    private final int magic400 = 400;
    private final String magicID = "uselessentityID";
    private final int magic2 = 2;
    private final int magic12 = 12;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Assert.assertNotNull(vaccinateActionUtils);
    }

    @Test
    public void assertFormDataTestWithTestData() throws Exception {
        android.content.Context context = Mockito.mock(android.content.Context.class);
        PowerMockito.mockStatic(FormUtils.class);
        Assert.assertNull(VaccinateActionUtils.formData(context, "", "", ""));
        PowerMockito.when(FormUtils.getInstance(org.mockito.ArgumentMatchers.any(android.content.Context.class))).thenReturn(formUtils);
        PowerMockito.when(formUtils.generateXMLInputForFormWithEntityId(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString())).thenReturn(magicData);
        Assert.assertEquals(VaccinateActionUtils.formData(context, "", "", ""), magicData);

    }

//    @Test
//    public void vaccinateTodayTest()throws Exception {
//        ActivityUtils activity = Robolectric.setupActivity(ActivityUtils.class);
//        TableRow row = (TableRow) LayoutInflater
//                .from(activity)
//                .inflate(R.layout.vaccinate_row_view, null);
////        View v = new View(RuntimeEnvironment.application);
////        TableRow row = new TableRow(RuntimeEnvironment.application);
////        PowerMockito.doReturn(v).when(row).findViewById(R.id.date);
//        VaccinateActionUtils.vaccinateToday(row, null);
//    }

    @Test
    public void assertAddDialogHookCustomFilterTestForDifferentAgeAndVaccines() throws Exception {
        VaccineWrapper vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("36");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.opv1);
        Assert.assertEquals(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper), true);

        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("64");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.opv2);
        Assert.assertEquals(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper), true);

        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("92");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.opv3);
        Assert.assertEquals(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper), true);

        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("251");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.measles1);
        Assert.assertEquals(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper), true);

        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("351");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.measles2);
        Assert.assertEquals(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper), true);

        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setExistingAge("0");
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.bcg);
        Assert.assertEquals(VaccinateActionUtils.addDialogHookCustomFilter(vaccineWrapper), true);

    }

    @Test
    public void assertFindRowTestReturnsTableRow() throws Exception {
        String tag = "TAG";
        String wrong_tag = "WRONG TAG";
        Set<TableLayout> tables = new HashSet<TableLayout>();
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
    public void assertRetrieveFieldOveridesTestReturnsFieldOverieds() throws Exception {
        String s = "{\"fieldOverrides\":\"{}\"}";
        Assert.assertNotNull(VaccinateActionUtils.retrieveFieldOverides(s));
        //throws Exception
        Assert.assertNotNull(VaccinateActionUtils.retrieveFieldOverides("{}"));
    }

    @Test
    public void assertAllAlertNamesTestReturnsAlertForACategory() throws Exception {

        List<ServiceType> typeList = new ArrayList<ServiceType>();
        HashMap<String, List<ServiceType>> collection = new HashMap<String, List<ServiceType>>();
        ServiceType st = new ServiceType();
        st.setName("SERVICE NAME");
        collection.put("1", typeList);
        collection.put("2", null);
        Assert.assertNotNull(VaccinateActionUtils.allAlertNames(collection.values()));

        Assert.assertNotNull(VaccinateActionUtils.allAlertNames(magicChild));
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
    public void assertPreviousStateKeyTestWithVariousVaccineNames() throws Exception {
        Assert.assertNull(VaccinateActionUtils.previousStateKey(null, null));
        Vaccine v = new Vaccine();
        v.setName(magicBCG);
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey(magicChild, v));
        v.setName(magicNULL);
        Assert.assertNull(VaccinateActionUtils.previousStateKey(magicChild, v));
        v.setName("OPV 0");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey(magicChild, v));
        v.setName("OPV 1");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey(magicChild, v));
        v.setName("OPV 2");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey(magicChild, v));
        v.setName("OPV 3");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey(magicChild, v));
        v.setName("OPV 4");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey(magicChild, v));
        v.setName("MR 1");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey(magicChild, v));
        v.setName("MR 2");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey(magicChild, v));
        v.setName("IPV");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey(magicChild, v));
        v.setName("TT 1");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey("woman", v));
    }

    @Test
    public void assertHyphenTestReturnsHyphenatedString() throws Exception {
        Assert.assertNotNull(VaccinateActionUtils.addHyphen(""));
        Assert.assertNotNull(VaccinateActionUtils.addHyphen("a b"));
        Assert.assertNotNull(VaccinateActionUtils.removeHyphen(""));
        Assert.assertNotNull(VaccinateActionUtils.removeHyphen("a_b"));
    }

    @Test
    public void assertAddBcg2SpecialVaccineTestReturnsSpecialVaccinesOnHasVaccineAndGetVaccineMethods() throws Exception {
        List<Vaccine> list = new ArrayList<Vaccine>();
        Vaccine v = new Vaccine();
        v.setName("BCG 2");
        list.add(v);
        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);
        PowerMockito.mockStatic(VaccinatorUtils.class);

        listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = JsonFormUtils.gson.fromJson(VaccineData.special_vacines, listType);

        PowerMockito.when(VaccinatorUtils.getSpecialVaccines(org.mockito.ArgumentMatchers.any(android.content.Context.class))).thenReturn(specialVaccines);
        VaccinateActionUtils.addBcg2SpecialVaccine(Mockito.mock(android.content.Context.class), vaccines.get(0), list);

        PowerMockito.when(VaccinatorUtils.getSpecialVaccines(org.mockito.ArgumentMatchers.any(android.content.Context.class))).thenReturn(null);
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
    public void assertPopulateDefaultAlertsTestReturnsAlerts() throws Exception {
        VaccinateActionUtils.populateDefaultAlerts(null, null, null, null, null, null);
        List<Vaccine> vlist = new ArrayList<Vaccine>();
        Vaccine v = new Vaccine();
        v.setName(magicBCG);
        vlist.add(v);
        List<Alert> alist = new ArrayList<Alert>();
        Alert a = new Alert("caseID", magicBCG, magicBCG, AlertStatus.normal, new Date().toString(), new Date().toString());
        alist.add(a);
        VaccineRepo.Vaccine vaccine[] = {VaccineRepo.Vaccine.bcg};
        AlertService alertService = new AlertService(Mockito.mock(AlertRepository.class));
        VaccinateActionUtils.populateDefaultAlerts(alertService, vlist, alist, magicID, new DateTime(), vaccine);
        VaccineRepo.Vaccine vaccine2[] = {VaccineRepo.Vaccine.bcg2};
        VaccinateActionUtils.populateDefaultAlerts(alertService, vlist, alist, magicID, new DateTime(), vaccine2);


        Assert.assertEquals(VaccinateActionUtils.hasAlert(alist, VaccineRepo.Vaccine.bcg2), false);
        Assert.assertEquals(VaccinateActionUtils.hasAlert(alist, VaccineRepo.Vaccine.bcg), true);
        Assert.assertEquals(VaccinateActionUtils.hasAlert(null, VaccineRepo.Vaccine.bcg), false);

        Assert.assertNull(VaccinateActionUtils.getAlert(alist, VaccineRepo.Vaccine.bcg2));
        Assert.assertNotNull(VaccinateActionUtils.getAlert(alist, VaccineRepo.Vaccine.bcg));
        Assert.assertNull(VaccinateActionUtils.getAlert(null, VaccineRepo.Vaccine.bcg));


    }

    @Test
    public void assertCreateDefaultAlertTestReturnsAlert() throws Exception {
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
    public void assertMoreThanThreeMonthsReturnsTrueForDatesMoreThanThreeMonths() throws Exception {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusMonths(magic2);

        Assert.assertFalse(vaccinateActionUtils.moreThanThreeMonths(new Date(dateTime.getMillis())));


        Assert.assertFalse(vaccinateActionUtils.moreThanThreeMonths(new Date(dateTime.getMillis())));

        dateTime = new DateTime();
        dateTime = dateTime.minusMonths(3);

        Assert.assertTrue(vaccinateActionUtils.moreThanThreeMonths(new Date(dateTime.getMillis())));

        dateTime = new DateTime();
        dateTime = dateTime.minusMonths(3);
        dateTime = dateTime.minusWeeks(magic2);

        Assert.assertTrue(vaccinateActionUtils.moreThanThreeMonths(new Date(dateTime.getMillis())));

    }

    @Test
    public void assertLessThanThreeMonthsReturnsTrueForCreatedAtVaccineDatesLessThanThreeMonths() throws Exception {

        Vaccine vaccine = null;
        Assert.assertTrue(vaccinateActionUtils.lessThanThreeMonths(vaccine));

        vaccine = new Vaccine();
        Assert.assertTrue(vaccinateActionUtils.lessThanThreeMonths(vaccine));

        vaccine = new Vaccine();
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusMonths(magic2);
        vaccine.setCreatedAt(new Date(dateTime.getMillis()));
        Assert.assertTrue(vaccinateActionUtils.lessThanThreeMonths(vaccine));

        vaccine = new Vaccine();
        dateTime = new DateTime();
        dateTime = dateTime.minusMonths(5);
        vaccine.setCreatedAt(new Date(dateTime.getMillis()));
        Assert.assertFalse(vaccinateActionUtils.lessThanThreeMonths(vaccine));


    }

    @Test
    public void assertLessThanThreeMonthsReturnsTrueForCreatedAtServiceRecordDatesLessThanThreeMonths() throws Exception {

        ServiceRecord serviceRecord = null;
        Assert.assertTrue(vaccinateActionUtils.lessThanThreeMonths(serviceRecord));

        serviceRecord = new ServiceRecord();
        Assert.assertTrue(vaccinateActionUtils.lessThanThreeMonths(serviceRecord));

        serviceRecord = new ServiceRecord();
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusMonths(magic2);
        serviceRecord.setCreatedAt(new Date(dateTime.getMillis()));
        Assert.assertTrue(vaccinateActionUtils.lessThanThreeMonths(serviceRecord));

        serviceRecord = new ServiceRecord();
        dateTime = new DateTime();
        dateTime = dateTime.minusMonths(5);
        serviceRecord.setCreatedAt(new Date(dateTime.getMillis()));
        Assert.assertFalse(vaccinateActionUtils.lessThanThreeMonths(serviceRecord));


    }
}
