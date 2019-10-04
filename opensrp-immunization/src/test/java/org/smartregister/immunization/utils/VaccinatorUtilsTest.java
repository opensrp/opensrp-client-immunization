package org.smartregister.immunization.utils;

import android.content.res.Resources;

import com.google.gson.reflect.TypeToken;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.BuildConfig;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceData;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.IMDatabaseUtils;
import org.smartregister.immunization.util.JsonFormUtils;
import org.smartregister.immunization.util.RecurringServiceUtils;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.eq;

/**
 * Created by onaio on 29/08/2017.
 */

@PrepareForTest({ImmunizationLibrary.class, Utils.class, AssetHandler.class})
public class VaccinatorUtilsTest extends BaseUnitTest {

    private final int magicColor = 255;
    private final String magicOPV0 = "OPV 0";
    private final String magicNULL = "NULL";
    private final String UNTRANSLATED = "translate";
    private final String TRANSLATED = "tafsiri";
    private final int RESOURCE_ID = 123456;
    private final String UNTRANSLATED_GROUP_NAME = "6 weeks";
    private final String TRANSLATED_GROUP_NAME = "6 weeks";
    @Rule
    public PowerMockRule rule = new PowerMockRule();
    @Mock
    private ImmunizationLibrary immunizationLibrary;
    @Mock
    private Context context;
    @Mock
    private CommonRepository commonRepository;
    @Mock
    private Resources resources;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verifygetwastedcallssqlmethodonce() {
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito
                .when(ImmunizationLibrary.getInstance().context().commonrepository(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(commonRepository);
        VaccinatorUtils.getWasted("", "", "");
        VaccinatorUtils.getWasted("", "", "", "");
        Mockito.verify(commonRepository, Mockito.times(1))
                .rawQuery(org.mockito.ArgumentMatchers.anyString(), eq(new String[]{}));
    }

    @Test
    public void verifyGetUsedcallssqlmethodonce() {
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito
                .when(ImmunizationLibrary.getInstance().context().commonrepository(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(commonRepository);
        VaccinatorUtils.getUsed("", "", "", "", "");
        Mockito.verify(commonRepository, Mockito.times(1))
                .rawQuery(org.mockito.ArgumentMatchers.anyString(), eq(new String[]{}));
    }

    @Test
    public void assertVaccinatorUtilsTest() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(immunizationLibrary.getLocale()).thenReturn(new Locale("en"));
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito
                .when(ImmunizationLibrary.getInstance().context().commonrepository(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(commonRepository);

        android.content.Context ctx = Mockito.mock(android.content.Context.class);
        Resources resources = Mockito.mock(Resources.class);
        PowerMockito.when(ctx.getResources()).thenReturn(resources);
        PowerMockito.when(resources.getColor(org.mockito.ArgumentMatchers.anyInt())).thenReturn(magicColor);

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
        //        VaccinatorUtils powerspy = PowerMockito.spy(vaccinatorUtils);
        //        PowerMockito.doReturn(new HashMap<String, Object>()).when(VaccinatorUtils.class, "createVaccineMap", mock(String.class), mock(Alert.class), mock(DateTime.class), mock(ServiceType.class));
    }

    @Test
    public void assertGetVaccineDisplayNameTestReturnsDisplayName() {
        android.content.Context context = Mockito.mock(android.content.Context.class);
        PowerMockito.mockStatic(ImmunizationLibrary.class);

        PowerMockito.when(immunizationLibrary.getLocale()).thenReturn(new Locale("en"));
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);

        Class<List<VaccineGroup>> clazz = (Class) List.class;
        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();

        List<VaccineGroup> vaccineGroups = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);

        PowerMockito.when(immunizationLibrary.assetJsonToJava("vaccines.json", clazz, listType)).thenReturn(vaccineGroups);

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
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.mockStatic(Utils.class);

        PowerMockito.when(immunizationLibrary.getLocale()).thenReturn(new Locale("en"));
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);

        Class<List<VaccineGroup>> clazz = (Class) List.class;
        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();

        List<VaccineGroup> vaccineGroups = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);

        PowerMockito.when(immunizationLibrary.assetJsonToJava("vaccines.json", clazz, listType)).thenReturn(vaccineGroups);

        Assert.assertEquals(VaccinatorUtils.getVaccineCalculation(context, magicOPV0), 0);
        Assert.assertEquals(VaccinatorUtils.getVaccineCalculation(context, magicNULL), -1);

        //readspecialvaccines
        Class<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>> clazz2 = (Class) List.class;
        Type listType2 = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>>() {
        }.getType();

        List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = JsonFormUtils.gson
                .fromJson(VaccineData.special_vacines, listType2);

        PowerMockito.when(immunizationLibrary.assetJsonToJava("special_vaccines.json", clazz2, listType2))
                .thenReturn(specialVaccines);
        JSONAssert.assertEquals(VaccineData.special_vacines,
                JsonFormUtils.gson.toJson(VaccinatorUtils.getSpecialVaccines(context), listType2),
                JSONCompareMode.NON_EXTENSIBLE);
        //readrecurringservices
        PowerMockito.when(Utils.readAssetContents(org.mockito.ArgumentMatchers.any(android.content.Context.class),
                org.mockito.ArgumentMatchers.anyString())).thenReturn(ServiceData.recurringservice);
        Assert.assertEquals(VaccinatorUtils.getSupportedRecurringServices(context), ServiceData.recurringservice);
    }

    @Test
    public void testTranslateNameInvokesCorrectApiMethods() {

        android.content.Context context = Mockito.mock(android.content.Context.class);
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.when(context.getResources()).thenReturn(resources);
        PowerMockito.when(resources.getString(RESOURCE_ID)).thenReturn(TRANSLATED);
        PowerMockito.when(resources.getIdentifier(UNTRANSLATED, "string", context.getPackageName())).thenReturn(RESOURCE_ID);

        String translated = VaccinatorUtils.translate(context, UNTRANSLATED);

        Assert.assertEquals(TRANSLATED, translated);
    }

    @Test
    public void testGetTranslatedVaccineName() {

        android.content.Context context = Mockito.mock(android.content.Context.class);
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.when(context.getResources()).thenReturn(resources);
        PowerMockito.when(resources.getString(123)).thenReturn(TRANSLATED);
        PowerMockito.when(resources.getIdentifier(UNTRANSLATED, "string", context.getPackageName())).thenReturn(123);

        String translated = VaccinatorUtils.getTranslatedVaccineName(context, UNTRANSLATED);
        Assert.assertEquals(TRANSLATED, translated);

        //Combo Vaccines
        translated = VaccinatorUtils.getTranslatedVaccineName(context, UNTRANSLATED + " / " + UNTRANSLATED);
        Assert.assertEquals(TRANSLATED + " / " + TRANSLATED, translated);

    }

    @Test
    public void testGetTranslatedGroupName() {

        android.content.Context context = Mockito.mock(android.content.Context.class);
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.when(context.getResources()).thenReturn(resources);
        PowerMockito.when(resources.getIdentifier(UNTRANSLATED, "string", context.getPackageName())).thenReturn(RESOURCE_ID);

        //Test Group names
        String translated = VaccinatorUtils.translate(context, UNTRANSLATED_GROUP_NAME);

        Assert.assertEquals(TRANSLATED_GROUP_NAME, translated);
    }

    @Test
    public void testCreateIdentifierWorksCorrectly() {

        String translated = VaccinatorUtils.createIdentifier("4 Weeks");

        Assert.assertEquals("_4_weeks", translated);
    }

}
