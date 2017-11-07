package org.smartregister.immunization.utils;

import android.widget.TableLayout;
import android.widget.TableRow;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.json.JSONArray;
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
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.repository.AlertRepository;
import org.smartregister.service.AlertService;
import org.smartregister.util.FormUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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
    @Mock
    private VaccineRepo vaccineRepo;
    @Mock
    private VaccinatorUtils vaccinatorUtils;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Assert.assertNotNull(vaccinateActionUtils);
    }

    @Test
    public void assertFormDataTestWithTestData() throws Exception {
        VaccinateActionUtils vaccinateActionUtils = new VaccinateActionUtils();
        android.content.Context context = Mockito.mock(android.content.Context.class);
        PowerMockito.mockStatic(FormUtils.class);
        Assert.assertNull(VaccinateActionUtils.formData(context, "", "", ""));
        PowerMockito.when(FormUtils.getInstance(any(android.content.Context.class))).thenReturn(formUtils);
        PowerMockito.when(formUtils.generateXMLInputForFormWithEntityId(anyString(), anyString(), anyString())).thenReturn("data");
        Assert.assertEquals(VaccinateActionUtils.formData(context, "", "", ""), "data");

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
        String tag= "TAG";

        Set<TableLayout>tables = new HashSet<TableLayout>();
        TableLayout tableLayout = new TableLayout(RuntimeEnvironment.application);
        TableRow row = new TableRow(RuntimeEnvironment.application);
        row.setTag(tag);
        tableLayout.addView(row);
        tables.add(tableLayout);
        Assert.assertNotNull(VaccinateActionUtils.findRow(tables, tag));
        Assert.assertNull(VaccinateActionUtils.findRow(tables, "WRONG TAG"));
        Assert.assertNotNull(VaccinateActionUtils.findRow(tableLayout, tag));
        Assert.assertNull(VaccinateActionUtils.findRow(tableLayout, "WRONG TAG"));
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

        Assert.assertNotNull(VaccinateActionUtils.allAlertNames("child"));
        Assert.assertNull(VaccinateActionUtils.allAlertNames("NULL"));
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
        v.setName("BCG");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey("child", v));
        v.setName("NULL");
        Assert.assertNull(VaccinateActionUtils.previousStateKey("child", v));
        v.setName("OPV 0");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey("child", v));
        v.setName("OPV 1");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey("child", v));
        v.setName("OPV 2");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey("child", v));
        v.setName("OPV 3");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey("child", v));
        v.setName("OPV 4");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey("child", v));
        v.setName("MR 1");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey("child", v));
        v.setName("MR 2");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey("child", v));
        v.setName("IPV");
        Assert.assertNotNull(VaccinateActionUtils.previousStateKey("child", v));
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
        List<Vaccine>list = new ArrayList<Vaccine>();
        Vaccine v = new Vaccine();
        v.setName("BCG 2");
        list.add(v);
        JSONArray vaccines = new JSONArray(VaccineData.vaccines);
        PowerMockito.mockStatic(VaccinatorUtils.class);
        PowerMockito.when(VaccinatorUtils.getSpecialVaccines(any(android.content.Context.class))).thenReturn(VaccineData.special_vacines);
        VaccinateActionUtils.addBcg2SpecialVaccine(Mockito.mock(android.content.Context.class), vaccines.getJSONObject(0), list);


        PowerMockito.when(VaccinatorUtils.getSpecialVaccines(any(android.content.Context.class))).thenReturn("NULL");
        VaccinateActionUtils.addBcg2SpecialVaccine(Mockito.mock(android.content.Context.class), vaccines.getJSONObject(0), list);

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
        List<Vaccine>vlist = new ArrayList<Vaccine>();
        Vaccine v = new Vaccine();
        v.setName("BCG");
        vlist.add(v);
        List<Alert>alist = new ArrayList<Alert>();
        Alert a = new Alert("caseID", "BCG", "BCG", AlertStatus.normal, new Date().toString(), new Date().toString());
        alist.add(a);
        VaccineRepo.Vaccine vaccine [] = {VaccineRepo.Vaccine.bcg};
        AlertService alertService = new AlertService(Mockito.mock(AlertRepository.class));
        VaccinateActionUtils.populateDefaultAlerts(alertService, vlist, alist, "uselessentityID", new DateTime(), vaccine);
        VaccineRepo.Vaccine vaccine2 [] = {VaccineRepo.Vaccine.bcg2};
        VaccinateActionUtils.populateDefaultAlerts(alertService, vlist, alist, "uselessentityID", new DateTime(), vaccine2);


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
        dateTime = dateTime.plusDays(2);
        Assert.assertNotNull(VaccinateActionUtils.createDefaultAlert(VaccineRepo.Vaccine.opv0, "", dateTime));
        dateTime = new DateTime();
        dateTime = dateTime.minusDays(2);
        Assert.assertNotNull(VaccinateActionUtils.createDefaultAlert(VaccineRepo.Vaccine.opv0, "", dateTime));
        dateTime = new DateTime();
        dateTime = dateTime.plusDays(400);
        Assert.assertNotNull(VaccinateActionUtils.createDefaultAlert(VaccineRepo.Vaccine.opv0, "", dateTime));
        dateTime = new DateTime();
        dateTime = dateTime.minusDays(400);
        Assert.assertNotNull(VaccinateActionUtils.createDefaultAlert(VaccineRepo.Vaccine.opv0, "", dateTime));

    }
}
