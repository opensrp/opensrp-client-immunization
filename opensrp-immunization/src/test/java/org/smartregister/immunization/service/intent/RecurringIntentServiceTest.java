package org.smartregister.immunization.service.intent;

import android.app.Application;
import android.content.Intent;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.util.IMConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */
@PrepareForTest ({ImmunizationLibrary.class})
public class RecurringIntentServiceTest extends BaseUnitTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private RecurringServiceTypeRepository recurringServiceTypeRepository;

    @Mock
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onHandleIntentTest() {
        Application application = RuntimeEnvironment.application;
        Intent intent = new Intent(application, RecurringIntentService.class);
        //List<ServiceRecord> spyServiceRecordList = Mockito.spy(serviceRecordList);

        RecurringIntentService recurringIntentService = Mockito.spy(new RecurringIntentService());
        PowerMockito.mockStatic(ImmunizationLibrary.class);

        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Assert.assertNotNull(immunizationLibrary);

        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceTypeRepository())
                .thenReturn(recurringServiceTypeRepository);
        Assert.assertNotNull(recurringServiceTypeRepository);

        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceRecordRepository())
                .thenReturn(recurringServiceRecordRepository);
        Assert.assertNotNull(recurringServiceRecordRepository);
        ServiceRecord serviceRecord = new ServiceRecord(0L, BASEENTITYID, 0L, VALUE, new Date(), ANMID, LOCATIONID, SYNCED,
                EVENTID,
                FORMSUBMISSIONID, 0L);

        Mockito.when(recurringServiceRecordRepository.findUnSyncedBeforeTime(IMConstants.VACCINE_SYNC_TIME))
                .thenReturn(serviceRecordList);
        serviceRecordList.add(serviceRecord);
        Assert.assertNotNull(serviceRecordList);

        Mockito.when(recurringServiceTypeRepository.find(serviceRecordList.get(0).getRecurringServiceId()))
                .thenReturn(serviceType);
        getServiceType();
        Assert.assertNotNull(serviceType);

        Assert.assertNotNull(recurringIntentService);
        Whitebox.setInternalState(recurringIntentService, "recurringServiceRecordRepository",
                recurringServiceRecordRepository);
        Whitebox.setInternalState(recurringIntentService, "recurringServiceTypeRepository",
                recurringServiceTypeRepository);
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
            String spinnerJson = "{\"key\":\"protected_at_birth\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"concept\"," +
                    "\"openmrs_entity_id\":\"164826AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"type\":\"spinner\"}";
            JSONObject spinnerObject = new JSONObject(spinnerJson);
            RecurringIntentService recurringIntentService = Mockito.spy(new RecurringIntentService());
            Whitebox.invokeMethod(recurringIntentService, "addYesNoChoices", spinnerObject);

            Assert.assertNotNull(spinnerObject.getJSONArray("values"));
            Assert.assertNotNull(spinnerObject.getJSONObject("openmrs_choice_ids"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
