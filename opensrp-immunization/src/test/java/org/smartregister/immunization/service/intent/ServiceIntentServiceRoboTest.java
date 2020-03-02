package org.smartregister.immunization.service.intent;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BuildConfig;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.customshadows.ShadowJsonFormUtils;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.Repository;
import org.smartregister.util.AppProperties;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowJsonFormUtils.class}, sdk = 27, constants= BuildConfig.class)
public class ServiceIntentServiceRoboTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Test
    public void onHandleIntentShouldGenerateValidJsonFieldsForEventCreation() {
        RecurringIntentService recurringIntentService = Robolectric.setupService(RecurringIntentService.class);

        org.smartregister.Context context = Mockito.mock(org.smartregister.Context.class);
        Mockito.doReturn(new AppProperties()).when(context).getAppProperties();
        ImmunizationLibrary.init(context, Mockito.mock(Repository.class), Mockito.mock(CommonFtsObject.class), 1, 1);

        RecurringServiceRecordRepository recurringServiceRecordRepository = Mockito.mock(RecurringServiceRecordRepository.class);
        RecurringServiceTypeRepository recurringServiceTypeRepository = Mockito.mock(RecurringServiceTypeRepository.class);

        ReflectionHelpers.setField(recurringIntentService, "recurringServiceRecordRepository", recurringServiceRecordRepository);
        ReflectionHelpers.setField(recurringIntentService, "recurringServiceTypeRepository", recurringServiceTypeRepository);
        Date date = Calendar.getInstance().getTime();

        String baseEntityId = "9080s8dfdsc";
        String anmId = "demo";
        String locationId = "093209d-sdfds909";
        ServiceRecord deworming = new ServiceRecord(3L, baseEntityId, "", 1L, "opv", date, anmId, locationId, BaseRepository.TYPE_Unsynced, "eventId1", "formSubmissionId1", date.getTime(), date);
        ServiceRecord itn = new ServiceRecord(4L, baseEntityId, "", 3L, "penta", date, anmId, locationId, BaseRepository.TYPE_Unsynced, "eventId2", "formSubmissionId2", date.getTime(), date);
        ServiceType dewormingServiceType = new ServiceType();
        dewormingServiceType.setType("Deworming");
        dewormingServiceType.setName("Deworming");
        dewormingServiceType.setDateEntity("entity");
        dewormingServiceType.setDateEntityId("entity_id");
        dewormingServiceType.setId(1L);

        ServiceType itnServiceType = new ServiceType();
        itnServiceType.setType("ITN");
        itnServiceType.setName("ITN");
        itnServiceType.setDateEntity("entity");
        itnServiceType.setDateEntityId("entity_id");
        itnServiceType.setId(3L);

        List<ServiceRecord> vaccineList = new ArrayList<>();
        vaccineList.add(deworming);
        vaccineList.add(itn);

        Mockito.doReturn(vaccineList).when(recurringServiceRecordRepository).findUnSyncedBeforeTime(12 * 60);
        Mockito.doReturn(dewormingServiceType).when(recurringServiceTypeRepository).find(1L);
        Mockito.doReturn(itnServiceType).when(recurringServiceTypeRepository).find(3L);

        recurringIntentService.onHandleIntent(null);

        HashMap<String, HashMap<Integer, ArrayList<Object>>> methodCalls = ShadowJsonFormUtils.getMethodCalls();
        Assert.assertEquals(2, methodCalls.get("createServiceEvent(Context, ServiceRecord, String, String, JSONArray)").size());
    }
}
