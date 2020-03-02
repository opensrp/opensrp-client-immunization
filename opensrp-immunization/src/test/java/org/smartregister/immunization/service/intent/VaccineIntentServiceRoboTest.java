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
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.repository.VaccineRepository;
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
public class VaccineIntentServiceRoboTest {


    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Test
    public void onHandleIntentShouldGenerateValidJsonFieldsForEventCreation() {
        VaccineIntentService vaccineIntentService = Robolectric.setupService(VaccineIntentService.class);

        org.smartregister.Context context = Mockito.mock(org.smartregister.Context.class);
        Mockito.doReturn(new AppProperties()).when(context).getAppProperties();
        ImmunizationLibrary.init(context, Mockito.mock(Repository.class), Mockito.mock(CommonFtsObject.class), 1, 1);

        VaccineRepository vaccineRepository = Mockito.mock(VaccineRepository.class);
        ReflectionHelpers.setField(vaccineIntentService, "vaccineRepository", vaccineRepository);

        Date date = Calendar.getInstance().getTime();

        String baseEntityId = "9080s8dfdsc";
        String anmId = "demo";
        String locationId = "093209d-sdfds909";
        Vaccine opv1 = new Vaccine(3L, baseEntityId, "", "opv", 1, date, anmId, locationId, BaseRepository.TYPE_Unsynced, "", date.getTime(), "eventId1", "formSubmissionId1", 0, date);
        Vaccine penta3 = new Vaccine(4L, baseEntityId, "", "penta", 3, date, anmId, locationId, BaseRepository.TYPE_Unsynced, "", date.getTime(), "eventId2", "formSubmissionId2", 0, date);

        List<Vaccine> vaccineList = new ArrayList<>();
        vaccineList.add(opv1);
        vaccineList.add(penta3);

        Mockito.doReturn(vaccineList).when(vaccineRepository).findUnSyncedBeforeTime(12 * 60);


        vaccineIntentService.onHandleIntent(null);

        HashMap<String, HashMap<Integer, ArrayList<Object>>> methodCalls = ShadowJsonFormUtils.getMethodCalls();
        Assert.assertEquals(2, methodCalls.get("createVaccineEvent(Context, Vaccine, String, String, JSONArray)").size());
    }
}
