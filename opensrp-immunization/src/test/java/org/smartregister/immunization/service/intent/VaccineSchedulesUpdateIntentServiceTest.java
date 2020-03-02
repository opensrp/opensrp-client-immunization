package org.smartregister.immunization.service.intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.smartregister.immunization.domain.VaccinationClient;
import org.smartregister.immunization.util.IMConstants;

import java.util.ArrayList;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2020-02-13
 */

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 27)
public class VaccineSchedulesUpdateIntentServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    public VaccineSchedulesUpdateIntentService vaccineScheduleUpdateIntentService;

    @Before
    public void setUp() throws Exception {
        vaccineScheduleUpdateIntentService = Mockito.spy(new VaccineSchedulesUpdateIntentService());
    }

    @Test
    public void onHandleIntentShouldSendLocalBroadcastWhenStart() {
        Mockito.doNothing().when(vaccineScheduleUpdateIntentService).sendLocalBroadcast(Mockito.anyString());
        Mockito.doReturn(new ArrayList<VaccinationClient>()).when(vaccineScheduleUpdateIntentService).getClients(ArgumentMatchers.eq("ec_client"), ArgumentMatchers.eq(0));

        vaccineScheduleUpdateIntentService.onHandleIntent(null);

        Mockito.verify(vaccineScheduleUpdateIntentService, Mockito.times(1))
                .sendLocalBroadcast(IMConstants.BroadcastAction.VaccineScheduleUpdate.SERVICE_STARTED);
        Mockito.verify(vaccineScheduleUpdateIntentService, Mockito.times(1))
                .sendLocalBroadcast(IMConstants.BroadcastAction.VaccineScheduleUpdate.SERVICE_FINISHED);
    }

}