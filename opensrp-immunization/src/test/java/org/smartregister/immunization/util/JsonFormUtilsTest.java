package org.smartregister.immunization.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.smartregister.AllConstants;
import org.smartregister.Context;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.service.intent.RecurringIntentService;
import org.smartregister.immunization.service.intent.VaccineIntentService;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;
import org.smartregister.util.AppProperties;

import java.util.Locale;


@RunWith(RobolectricTestRunner.class)
public class JsonFormUtilsTest {

    protected static final String TEST_BASE_ENTITY_ID = "test-base-entity-id";
    protected static final String TEST_STRING = "test-string-param";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private EventClientRepository eventClientRepository;

    @Mock
    private AllSharedPreferences allSharedPreferences;

    @Mock
    private Context openSRPContext;

    @Mock
    private AppProperties appProperties;

    @Before
    public void setUp() {
        ImmunizationLibrary.destroy();//Clear any static instance

        Mockito.doReturn(appProperties).when(openSRPContext).getAppProperties();
        ImmunizationLibrary.init(openSRPContext, Mockito.mock(Repository.class), Mockito.mock(CommonFtsObject.class), 4, "4.0.0", 2);
    }

    @After
    public void tearDown() {
        ImmunizationLibrary.destroy();
    }

    @Test
    public void testCreateVaccineEventAddsCorrectEventToRepository() throws JSONException {

        Mockito.doReturn(eventClientRepository).when(openSRPContext).getEventClientRepository();
        Mockito.doReturn(allSharedPreferences).when(openSRPContext).allSharedPreferences();
        Mockito.doReturn(AllConstants.DATA_CAPTURE_STRATEGY.NORMAL).when(allSharedPreferences).fetchCurrentDataStrategy();

        Vaccine vaccine = new Vaccine();
        vaccine.setAnmId("demo");
        vaccine.setName(VaccineRepo.Vaccine.bcg.name().toLowerCase(Locale.ENGLISH));
        vaccine.setBaseEntityId(TEST_BASE_ENTITY_ID);
        vaccine.setChildLocationId(TEST_STRING);
        vaccine.setTeam(TEST_STRING);
        vaccine.setTeamId(TEST_STRING);
        vaccine.setLocationId(TEST_STRING);

        JSONArray jsonArray = new JSONArray();

        String entityId = "1410AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        String dateDataType = "date";
        String concept = "concept";

        JSONObject vaccineJsonObject = new JSONObject();
        vaccineJsonObject.put(JsonFormUtils.KEY, vaccine.getName());
        vaccineJsonObject.put(JsonFormUtils.OPENMRS_ENTITY, concept);
        vaccineJsonObject.put(JsonFormUtils.OPENMRS_ENTITY_ID, entityId);
        vaccineJsonObject.put(JsonFormUtils.OPENMRS_ENTITY_PARENT, TEST_STRING);
        vaccineJsonObject.put(JsonFormUtils.OPENMRS_DATA_TYPE, dateDataType);
        vaccineJsonObject.put(JsonFormUtils.VALUE, "2022-07-10");
        jsonArray.put(vaccineJsonObject);

        JsonFormUtils.createVaccineEvent(vaccine, VaccineIntentService.EVENT_TYPE, VaccineIntentService.ENTITY_TYPE, jsonArray, openSRPContext);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JSONObject> jsonObjectArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);

        Mockito.verify(eventClientRepository).addEvent(stringArgumentCaptor.capture(), jsonObjectArgumentCaptor.capture());

        Assert.assertNotNull(stringArgumentCaptor.getValue());
        Assert.assertEquals(TEST_BASE_ENTITY_ID, stringArgumentCaptor.getValue());
        JSONObject jsonObject = jsonObjectArgumentCaptor.getValue();
        Assert.assertNotNull(jsonObject);

        Event event = org.smartregister.util.JsonFormUtils.gson.fromJson(jsonObject.toString(), Event.class);
        Assert.assertNotNull(event);
        Assert.assertEquals(TEST_BASE_ENTITY_ID, event.getBaseEntityId());
        Assert.assertEquals(TEST_STRING, event.getChildLocationId());
        Assert.assertEquals(TEST_STRING, event.getLocationId());
        Assert.assertEquals(VaccineIntentService.EVENT_TYPE, event.getEventType());
        Assert.assertEquals("demo", event.getProviderId());
        Assert.assertEquals(VaccineIntentService.ENTITY_TYPE, event.getEntityType());
        Assert.assertEquals(4, event.getClientApplicationVersion().intValue());
        Assert.assertEquals("4.0.0", event.getClientApplicationVersionName());
        Assert.assertEquals(2, event.getClientDatabaseVersion().intValue());
        Assert.assertEquals(AllConstants.DATA_STRATEGY, event.getObs().get(0).getFormSubmissionField());
        Assert.assertEquals(AllConstants.DATA_CAPTURE_STRATEGY.NORMAL, event.getObs().get(0).getValue());
        Assert.assertEquals(1, event.getDetails().size());
        Assert.assertTrue(event.getDetails().containsKey(AllConstants.DATA_STRATEGY));
        Assert.assertEquals(AllConstants.DATA_CAPTURE_STRATEGY.NORMAL, event.getDetails().get(AllConstants.DATA_STRATEGY));
        Assert.assertEquals("bcg", event.getObs().get(1).getFormSubmissionField());
        Assert.assertEquals("2022-07-10", event.getObs().get(1).getValue());

    }

    @Test
    public void testCreateServiceEventAddsCorrectEventToRepository() throws JSONException {
        Mockito.doReturn(eventClientRepository).when(openSRPContext).getEventClientRepository();
        Mockito.doReturn(allSharedPreferences).when(openSRPContext).allSharedPreferences();
        Mockito.doReturn(AllConstants.DATA_CAPTURE_STRATEGY.ADVANCED).when(allSharedPreferences).fetchCurrentDataStrategy();

        ServiceRecord service = new ServiceRecord();
        service.setAnmId("demo");
        service.setName("VitaminA");
        service.setBaseEntityId(TEST_BASE_ENTITY_ID);
        service.setChildLocationId(TEST_STRING);
        service.setTeam(TEST_STRING);
        service.setTeamId(TEST_STRING);
        service.setLocationId(TEST_STRING);

        JSONArray jsonArray = new JSONArray();

        String entityId = "1639AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        String dateDataType = "date";
        String concept = "concept";

        JSONObject serviceJsonObject = new JSONObject();
        serviceJsonObject.put(JsonFormUtils.KEY, service.getName());
        serviceJsonObject.put(JsonFormUtils.OPENMRS_ENTITY, concept);
        serviceJsonObject.put(JsonFormUtils.OPENMRS_ENTITY_ID, entityId);
        serviceJsonObject.put(JsonFormUtils.OPENMRS_ENTITY_PARENT, TEST_STRING);
        serviceJsonObject.put(JsonFormUtils.OPENMRS_DATA_TYPE, dateDataType);
        serviceJsonObject.put(JsonFormUtils.VALUE, "2022-05-10");
        jsonArray.put(serviceJsonObject);

        JsonFormUtils.createServiceEvent(service, RecurringIntentService.EVENT_TYPE, RecurringIntentService.ENTITY_TYPE, jsonArray, openSRPContext);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<JSONObject> jsonObjectArgumentCaptor = ArgumentCaptor.forClass(JSONObject.class);

        Mockito.verify(eventClientRepository).addEvent(stringArgumentCaptor.capture(), jsonObjectArgumentCaptor.capture());

        Assert.assertNotNull(stringArgumentCaptor.getValue());
        Assert.assertEquals(TEST_BASE_ENTITY_ID, stringArgumentCaptor.getValue());
        JSONObject jsonObject = jsonObjectArgumentCaptor.getValue();
        Assert.assertNotNull(jsonObject);

        Event event = org.smartregister.util.JsonFormUtils.gson.fromJson(jsonObject.toString(), Event.class);
        Assert.assertNotNull(event);
        Assert.assertEquals(TEST_BASE_ENTITY_ID, event.getBaseEntityId());
        Assert.assertEquals(TEST_STRING, event.getChildLocationId());
        Assert.assertEquals(TEST_STRING, event.getLocationId());
        Assert.assertEquals(RecurringIntentService.EVENT_TYPE, event.getEventType());
        Assert.assertEquals("demo", event.getProviderId());
        Assert.assertEquals(RecurringIntentService.ENTITY_TYPE, event.getEntityType());
        Assert.assertEquals(4, event.getClientApplicationVersion().intValue());
        Assert.assertEquals("4.0.0", event.getClientApplicationVersionName());
        Assert.assertEquals(2, event.getClientDatabaseVersion().intValue());
        Assert.assertEquals(AllConstants.DATA_STRATEGY, event.getObs().get(0).getFormSubmissionField());
        Assert.assertEquals(AllConstants.DATA_CAPTURE_STRATEGY.ADVANCED, event.getObs().get(0).getValue());
        Assert.assertEquals(1, event.getDetails().size());
        Assert.assertTrue(event.getDetails().containsKey(AllConstants.DATA_STRATEGY));
        Assert.assertEquals(AllConstants.DATA_CAPTURE_STRATEGY.ADVANCED, event.getDetails().get(AllConstants.DATA_STRATEGY));
        Assert.assertEquals("VitaminA", event.getObs().get(1).getFormSubmissionField());
        Assert.assertEquals("2022-05-10", event.getObs().get(1).getValue());
    }
}