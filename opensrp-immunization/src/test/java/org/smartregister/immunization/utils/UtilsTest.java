package org.smartregister.immunization.utils;

import com.google.gson.reflect.TypeToken;

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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ndegwamartin on 2019-07-23.
 */
@PrepareForTest({ImmunizationLibrary.class, VaccinatorUtils.class})
public class UtilsTest extends BaseUnitTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();
    @Mock
    private ImmunizationLibrary immunizationLibrary;
    @Mock
    private AppProperties properties;

    private static String OFFSET_days = "+2d";
    private static String OFFSET_days_negative = "-2d";
    private static String OFFSET_month_days = "+4m,3d";
    private static String OFFSET_months = "+6m";
    private static String OFFSET_years = "-5y";
    private static String RELAXATION_DAYS = "2";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForDaysDefault() {
        String result = Utils.updateRelaxationDays(OFFSET_days);
        Assert.assertEquals(OFFSET_days, result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForDays() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        PowerMockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_days);
        Assert.assertEquals("+0d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForDaysNegative() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        PowerMockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_days_negative);
        Assert.assertEquals("-4d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForMonths() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        PowerMockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_months);
        Assert.assertEquals("+6m,-2d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForYears() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        PowerMockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_years);
        Assert.assertEquals("-5y,-2d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForMonthsDays() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        PowerMockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_month_days);
        Assert.assertEquals("+4m,1d", result);
    }

    @Test
    public void testProcessVaccineCacheFunctionsCorrectly() {

        Type listType = new TypeToken<List<VaccineGroup>>() {}.getType();
        Type vaccineListType = new TypeToken<List<Vaccine>>() {}.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);
        List<Vaccine> specialVaccines = JsonFormUtils.gson.fromJson(VaccineData.special_vacines, vaccineListType);

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);

        Context context = Mockito.mock(Context.class);
        android.content.Context androidContext = Mockito.mock(android.content.Context.class);

        Mockito.doReturn(context).when(immunizationLibrary).context();
        Mockito.doReturn(androidContext).when(context).applicationContext();
        PowerMockito.when(VaccinatorUtils.getSpecialVaccines(androidContext)).thenReturn(specialVaccines);

        PowerMockito.spy(VaccinatorUtils.class);
        PowerMockito.when(VaccinatorUtils.getSupportedVaccinesByCategory(RuntimeEnvironment.application, "child")).thenReturn(vaccines);
        PowerMockito.when(VaccinatorUtils.getSupportedVaccinesByCategory(RuntimeEnvironment.application, "child")).thenReturn(vaccines);

        Map<String, VaccineCache> vaccineCacheMap = new HashMap<>();
        PowerMockito.when(ImmunizationLibrary.getInstance().getVaccineCacheMap()).thenReturn(vaccineCacheMap);

        Utils.processVaccineCache(RuntimeEnvironment.application, "child");

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


        Assert.assertEquals(3,childVaccineCache.groupVaccineCountMap.get("Birth").getRemaining());
        Assert.assertEquals(4,childVaccineCache.groupVaccineCountMap.get("6 Weeks").getRemaining());
        Assert.assertEquals(3,childVaccineCache.groupVaccineCountMap.get("14 Weeks").getRemaining());
    }
}
