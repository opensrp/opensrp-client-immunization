package org.smartregister.immunization.service.intent;

import android.app.Application;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */
public class RecurringIntentServiceTest extends BaseUnitTest {

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private RecurringServiceTypeRepository recurringServiceTypeRepository;

    @Mock
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Mock
    private org.smartregister.Context drishtiContext;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private AlertService alertService;

    @Mock
    private AppProperties appProperties;

    @Spy
    private List<ServiceRecord> serviceRecordList = new ArrayList<>();

    @Spy
    private ServiceType serviceType = new ServiceType();

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        Assert.assertNotNull(new RecurringIntentService());
    }

    @Before
    public void setUp() {
        mockImmunizationLibrary(immunizationLibrary, drishtiContext, vaccineRepository, alertService, appProperties);
    }

    @Test
    public void onHandleIntentTest() throws Exception {
        Application application = ApplicationProvider.getApplicationContext();
        Intent intent = new Intent(application, RecurringIntentService.class);

        RecurringIntentService recurringIntentService = Mockito.spy(new RecurringIntentService());

        Mockito.when(immunizationLibrary.recurringServiceTypeRepository()).thenReturn(recurringServiceTypeRepository);
        Mockito.when(immunizationLibrary.recurringServiceRecordRepository()).thenReturn(recurringServiceRecordRepository);

        ServiceRecord serviceRecord = new ServiceRecord(0L, BASEENTITYID, 0L, VALUE, new Date(), ANMID, LOCATIONID, SYNCED,
                EVENTID, FORMSUBMISSIONID, 0L);

        Mockito.when(recurringServiceRecordRepository.findUnSyncedBeforeTime(IMConstants.VACCINE_SYNC_TIME))
                .thenReturn(serviceRecordList);
        serviceRecordList.add(serviceRecord);

        Mockito.when(recurringServiceTypeRepository.find(serviceRecordList.get(0).getRecurringServiceId()))
                .thenReturn(serviceType);
        getServiceType();

        setField(recurringIntentService, "recurringServiceRecordRepository", recurringServiceRecordRepository);
        setField(recurringIntentService, "recurringServiceTypeRepository", recurringServiceTypeRepository);
        setField(recurringIntentService, "immunizationLibrary", immunizationLibrary);

        recurringIntentService.onHandleIntent(intent);
    }

    private void getServiceType() {
        serviceType.setId(0L);
        serviceType.setType("Service");
        serviceType.setName("Custom Service");
        serviceType.setServiceNameEntity("Custom Service Entity");
        serviceType.setServiceNameEntityId("ahsafd-35ndfyu-893467598-kjdfhsj");
        serviceType.setDateEntity("12-12-2019");
        serviceType.setDateEntityId("dasdsf-5465fdds-fdgfs55-455dfd");
        serviceType.setUnits("34");
        serviceType.setServiceLogic("Custom Service");
        serviceType.setPrerequisite(null);
        serviceType.setPreOffset(null);
        serviceType.setExpiryOffset(null);
        serviceType.setMilestoneOffset(null);
        serviceType.setUpdatedAt(345656437L);
    }

    @Test
    public void addYesNoChoicesTest() {
        try {
            String spinnerJson = "{\"key\":\"protected_at_birth\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\",\"openmrs_entity_id\":\"164826AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"type\":\"spinner\"}";
            JSONObject spinnerObject = new JSONObject(spinnerJson);
            RecurringIntentService recurringIntentService = Mockito.spy(new RecurringIntentService());
            invokeAddYesNoChoices(recurringIntentService, spinnerObject);

            Assert.assertNotNull(spinnerObject.getJSONArray("values"));
            Assert.assertNotNull(spinnerObject.getJSONObject("openmrs_choice_ids"));
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void invokeAddYesNoChoices(RecurringIntentService service, JSONObject spinnerObject) throws Exception {
        java.lang.reflect.Method method = RecurringIntentService.class.getDeclaredMethod("addYesNoChoices", JSONObject.class);
        method.setAccessible(true);
        method.invoke(service, spinnerObject);
    }
}
