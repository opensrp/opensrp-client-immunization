package org.smartregister.immunization.service.intent;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;

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
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;

/**
 * Created by onaio on 30/08/2017.
 */
@PrepareForTest({ImmunizationLibrary.class})
public class RecurringIntentServiceTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private RecurringServiceTypeRepository recurringServiceTypeRepository;

    @Mock
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        junit.framework.Assert.assertNotNull(new RecurringIntentService());
    }

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    public void onStartCommandTest() {
        Application application = RuntimeEnvironment.application;

        Intent intent = new Intent(application, RecurringIntentService.class);

        RecurringIntentService recurringIntentService = Mockito.spy(new RecurringIntentService());
        recurringIntentService.onHandleIntent(new Intent());

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceTypeRepository()).thenReturn(recurringServiceTypeRepository);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceRecordRepository()).thenReturn(recurringServiceRecordRepository);
        PowerMockito.doReturn(1).when((IntentService) recurringIntentService).onStartCommand(intent, IntentService.START_FLAG_REDELIVERY, 1);
//        recurringIntentService.onStartCommand(intent,IntentService.START_FLAG_REDELIVERY,1);
    }

}
