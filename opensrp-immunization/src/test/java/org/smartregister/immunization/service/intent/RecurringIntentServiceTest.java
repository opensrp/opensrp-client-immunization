package org.smartregister.immunization.service.intent;

import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.Context;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

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
    private Context context;

    @Mock
    private RecurringServiceTypeRepository recurringServiceTypeRepository;

    @Mock
    private RecurringServiceRecordRepository recurringServiceRecordRepository;


    @Mock
    private RecurringIntentService recurringIntentService;
    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        junit.framework.Assert.assertNotNull(new RecurringIntentService());
    }

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onStartCommandTest() {

//        Activity activity = Robolectric.setupActivity(Activity.class);
//        Intent intent = new Intent(activity,RecurringIntentService.class);
        RecurringIntentService recurringIntentService = new RecurringIntentService();
        recurringIntentService.onHandleIntent(new Intent());

//        IntentService service = Mockito.mock(IntentService.class);
//        RecurringIntentService recurringIntentService = new RecurringIntentService();
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceTypeRepository()).thenReturn(recurringServiceTypeRepository);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceRecordRepository()).thenReturn(recurringServiceRecordRepository);
//        Mockito.when(IntentService.onStartCommand(intent, Service.START_FLAG_RETRY,1)).thenReturn(1);
//        recurringIntentService = Mockito.spy(recurringIntentService);
//        RecurringIntentService spy = Mockito.spy(recurringIntentService);
//        Intent i = new Intent();
//        i = Mockito.spy(Intent.class);
//        Mockito.doReturn(1).when(((IntentService)spy)).onStartCommand(i, Service.START_FLAG_REDELIVERY,IntentService.START_STICKY);
//        Mockito.when(spy.onStartCommand(i, Service.START_FLAG_REDELIVERY,IntentService.START_STICKY)).thenReturn(1);
//        Mockito.when(spy.onStartCommand(i, Service.START_FLAG_REDELIVERY,IntentService.START_STICKY)).thenCallRealMethod();
//        spy.startService(i);
//        Assert.assertEquals(spy.onStartCommand(i, Service.START_FLAG_REDELIVERY,IntentService.START_STICKY),1);

    }




}
