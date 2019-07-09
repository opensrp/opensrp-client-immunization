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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.util.IMConstants;

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

    @Mock
    private List<ServiceRecord> serviceRecordList;

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

        Mockito.when(recurringServiceRecordRepository.findUnSyncedBeforeTime(IMConstants.VACCINE_SYNC_TIME))
                .thenReturn(serviceRecordList);
        Assert.assertNotNull(serviceRecordList);

        Assert.assertNotNull(recurringIntentService);
        Whitebox.setInternalState(recurringIntentService, "recurringServiceRecordRepository",
                recurringServiceRecordRepository);
        recurringIntentService.onHandleIntent(intent);
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
