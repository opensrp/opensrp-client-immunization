package org.smartregister.immunization.utils;

import androidx.test.core.app.ApplicationProvider;

import com.google.gson.reflect.TypeToken;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.Context;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.jsonmapping.Vaccine;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.Utils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.util.VaccineCache;
import org.smartregister.util.AppProperties;
import org.smartregister.util.JsonFormUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ndegwamartin on 2019-07-23.
 */
public class UtilsTest extends BaseUnitTest {

    private static String OFFSET_days = "+2d";
    private static String OFFSET_days_negative = "-2d";
    private static String OFFSET_month_days = "+4m,3d";
    private static String OFFSET_months = "+6m";
    private static String OFFSET_years = "-5y";
    private static String RELAXATION_DAYS = "2";
    @Mock
    private ImmunizationLibrary immunizationLibrary;
    @Mock
    private AppProperties properties;
    @Mock
    private android.content.Context androidContext;

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForDaysDefault() {
        String result = Utils.updateRelaxationDays(OFFSET_days);
        Assert.assertEquals(OFFSET_days, result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForDays() {

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        Mockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_days);
        Assert.assertEquals("+0d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForDaysNegative() {

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        Mockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_days_negative);
        Assert.assertEquals("-2d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForMonths() {

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        Mockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_months);
        Assert.assertEquals("+6m,-2d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForYears() {

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        Mockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_years);
        Assert.assertEquals("-5y,-2d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForMonthsDays() {

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        Mockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_month_days);
        Assert.assertEquals("+4m,1d", result);
    }

    @Test
    public void testProcessVaccineCacheFunctionsCorrectly() {

        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();
        Type vaccineListType = new TypeToken<List<Vaccine>>() {
        }.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);
        List<Vaccine> specialVaccines = JsonFormUtils.gson.fromJson(VaccineData.special_vacines, vaccineListType);

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);

        Context context = Mockito.mock(Context.class);
        android.content.Context androidContext = Mockito.mock(android.content.Context.class);

        Mockito.doReturn(context).when(immunizationLibrary).context();
        Mockito.doReturn(androidContext).when(context).applicationContext();
        Mockito.when(VaccinatorUtils.getSpecialVaccines(androidContext)).thenReturn(specialVaccines);

        Mockito.spy(VaccinatorUtils.class);
        Mockito.when(VaccinatorUtils.getSupportedVaccinesByCategory(ApplicationProvider.getApplicationContext(), "child")).thenReturn(vaccines);

        Map<String, VaccineCache> vaccineCacheMap = new HashMap<>();
        Mockito.when(ImmunizationLibrary.getVaccineCacheMap()).thenReturn(vaccineCacheMap);

        Utils.processVaccineCache(ApplicationProvider.getApplicationContext(), "child");

        VaccineCache childVaccineCache = vaccineCacheMap.get("child");


        //Test a few random entries (order is important)
        //Vaccines
        Assert.assertNotNull(childVaccineCache);
        Assert.assertNotNull(childVaccineCache.vaccines);
        Assert.assertTrue(childVaccineCache.vaccines.length > 0);

        Assert.assertEquals(VaccineRepo.Vaccine.opv0, childVaccineCache.vaccines[0]);
        Assert.assertEquals(VaccineRepo.Vaccine.bcg, childVaccineCache.vaccines[1]);
        Assert.assertEquals(VaccineRepo.Vaccine.opv1, childVaccineCache.vaccines[3]);
        Assert.assertEquals(VaccineRepo.Vaccine.penta1, childVaccineCache.vaccines[4]);

        Assert.assertEquals(VaccineRepo.Vaccine.opv4, childVaccineCache.vaccines[16]);
        Assert.assertEquals(VaccineRepo.Vaccine.measles2, childVaccineCache.vaccines[17]);
        Assert.assertEquals(VaccineRepo.Vaccine.mr2, childVaccineCache.vaccines[18]);


        //Reverse look up map

        Assert.assertNotNull(childVaccineCache.reverseLookupGroupMap);
        Assert.assertTrue(childVaccineCache.reverseLookupGroupMap.size() > 0);

        Assert.assertEquals("Birth", childVaccineCache.reverseLookupGroupMap.get("bcg"));
        Assert.assertEquals("6 Weeks", childVaccineCache.reverseLookupGroupMap.get("pcv1"));
        Assert.assertEquals("10 Weeks", childVaccineCache.reverseLookupGroupMap.get("opv2"));
        Assert.assertEquals("10 Weeks", childVaccineCache.reverseLookupGroupMap.get("rota2"));
        Assert.assertEquals("18 Months", childVaccineCache.reverseLookupGroupMap.get("measles2"));
        Assert.assertEquals("18 Months", childVaccineCache.reverseLookupGroupMap.get("mr2"));

        // Special vaccines
        Assert.assertEquals("Birth", childVaccineCache.reverseLookupGroupMap.get("bcg2"));

        //Vaccine Repo list
        Assert.assertNotNull(childVaccineCache.vaccineRepo);
        Assert.assertEquals(19, childVaccineCache.vaccineRepo.size());

        Assert.assertEquals(VaccineRepo.Vaccine.opv0, childVaccineCache.vaccineRepo.get(0));
        Assert.assertEquals(VaccineRepo.Vaccine.bcg, childVaccineCache.vaccineRepo.get(1));
        Assert.assertEquals(VaccineRepo.Vaccine.opv1, childVaccineCache.vaccineRepo.get(3));
        Assert.assertEquals(VaccineRepo.Vaccine.penta1, childVaccineCache.vaccineRepo.get(4));

        //Group vaccine count
        Assert.assertNotNull(childVaccineCache.groupVaccineCountMap);
        Assert.assertTrue(childVaccineCache.groupVaccineCountMap.size() > 0);
        Assert.assertTrue(childVaccineCache.groupVaccineCountMap.containsKey("Birth"));
        Assert.assertTrue(childVaccineCache.groupVaccineCountMap.containsKey("6 Weeks"));
        Assert.assertTrue(childVaccineCache.groupVaccineCountMap.containsKey("10 Weeks"));


        Assert.assertEquals(3, childVaccineCache.groupVaccineCountMap.get("Birth").getRemaining());
        Assert.assertEquals(4, childVaccineCache.groupVaccineCountMap.get("6 Weeks").getRemaining());
        Assert.assertEquals(3, childVaccineCache.groupVaccineCountMap.get("14 Weeks").getRemaining());
    }


    @Test
    public void testGenericProcessVaccineCache() throws IOException {
        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();

        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.girls_over_5_vaccines, listType);

        Context context = Mockito.mock(Context.class);
        Mockito.doReturn(context).when(immunizationLibrary).context();

        Mockito.mockStatic(VaccinatorUtils.class);
        Mockito.when(VaccinatorUtils.cleanVaccineName("HPV 1")).thenReturn("hpv1");
        Mockito.when(VaccinatorUtils.cleanVaccineName("HPV 2")).thenReturn("hpv2");

        Mockito.when(VaccinatorUtils.getVaccineFiles(androidContext)).thenReturn(Arrays.asList(new String[]{"girls_over_5_vaccines.json"}));
        Mockito.when(VaccinatorUtils.getVaccineGroupsFromVaccineConfigFile(androidContext, "vaccines/girls_over_5_vaccines.json")).thenReturn(vaccines);

        Map<String, VaccineCache> vaccineCacheMap = new HashMap<>();

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getVaccineCacheMap()).thenReturn(vaccineCacheMap);

        Utils.processVaccineCache(androidContext);

        VaccineCache childVaccineCache = vaccineCacheMap.get("girls_over_5");

        //Test a few random entries (order is important)
        //Vaccines
        Assert.assertNotNull(childVaccineCache);
        Assert.assertNotNull(childVaccineCache.vaccines);
        Assert.assertEquals(2, childVaccineCache.vaccines.length);

        Assert.assertEquals(VaccineRepo.Vaccine.hpv1, childVaccineCache.vaccines[0]);
        Assert.assertEquals(VaccineRepo.Vaccine.hpv2, childVaccineCache.vaccines[1]);

        //Reverse look up map

        Assert.assertNotNull(childVaccineCache.reverseLookupGroupMap);
        Assert.assertEquals(2, childVaccineCache.reverseLookupGroupMap.size());

        Assert.assertEquals("108 Months", childVaccineCache.reverseLookupGroupMap.get("hpv1"));
        Assert.assertEquals("114 Months", childVaccineCache.reverseLookupGroupMap.get("hpv2"));

        //Vaccine Repo list
        Assert.assertNotNull(childVaccineCache.vaccineRepo);
        Assert.assertEquals(2, childVaccineCache.vaccineRepo.size());

        Assert.assertEquals(VaccineRepo.Vaccine.hpv1, childVaccineCache.vaccineRepo.get(0));
        Assert.assertEquals(VaccineRepo.Vaccine.hpv2, childVaccineCache.vaccineRepo.get(1));

        //Group vaccine count
        Assert.assertNotNull(childVaccineCache.groupVaccineCountMap);
        Assert.assertEquals(2, childVaccineCache.groupVaccineCountMap.size());
        Assert.assertTrue(childVaccineCache.groupVaccineCountMap.containsKey("108 Months"));
        Assert.assertTrue(childVaccineCache.groupVaccineCountMap.containsKey("114 Months"));

        Assert.assertEquals(1, childVaccineCache.groupVaccineCountMap.get("108 Months").getRemaining());
        Assert.assertEquals(1, childVaccineCache.groupVaccineCountMap.get("114 Months").getRemaining());

    }
}
