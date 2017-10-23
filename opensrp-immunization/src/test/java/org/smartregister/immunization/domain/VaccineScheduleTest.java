package org.smartregister.immunization.domain;

import android.test.mock.MockContext;

import net.sqlcipher.database.SQLiteDatabase;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BaseUnitTest;
import org.junit.Test;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by real on 23/10/17.
 */

public class VaccineScheduleTest extends BaseUnitTest{

    @InjectMocks
    VaccineSchedule vaccineSchedule;

    @Before
    public void setup(){

        vaccineSchedule = Mockito.mock(VaccineSchedule.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void updateOfflineAlertsTest() throws Exception{
//        Repository repository = Mockito.mock(Repository.class);
//        Context context = Mockito.mock(Context.class);
//        CommonFtsObject commonFtsObject = Mockito.mock(CommonFtsObject.class);
//
//        ImmunizationLibrary mocked = Mockito.mock(ImmunizationLibrary.class);
//        mocked.init(context, repository, commonFtsObject);
//        Mockito.when(mocked.vaccineRepository()).thenReturn(Mockito.mock(VaccineRepository.class));
//        Mockito.when(mocked.context()).thenReturn(context);
//        Mockito.when(mocked.context().alertService()).thenReturn(Mockito.mock(AlertService.class));
//        Assert.assertNotNull(VaccineSchedule.updateOfflineAlerts(VaccineTest.BASEENTITYID,new DateTime(),"OPV"));

    }
    @Test
    public void getOfflineAlertTest(){
        Vaccine newVaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.PROGRAMCLIENTID, VaccineTest.NAME, 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        List<Vaccine> list = new ArrayList<Vaccine>();
        list.add(newVaccine);

        vaccineSchedule.getOfflineAlert(VaccineTest.BASEENTITYID,new Date(),list);
    }
    @Test
    public void constructorTest(){
        Assert.assertNotNull(new VaccineSchedule(null,null,null,null));
    }
    @Test
    public void initAndInitVaccineTest()throws Exception{

        //VaccineSchedule vaccineScheduleSpy = Mockito.spy(vaccineSchedule);
        vaccineSchedule.init(new JSONArray(VaccineData.vaccines),new JSONArray(VaccineData.special_vacines),"OPV");
        //Mockito.verify(vaccineScheduleSpy,times(1)).init(org.mockito.ArgumentMatchers.any(JSONArray.class),org.mockito.ArgumentMatchers.any(JSONArray.class),org.mockito.ArgumentMatchers.any(String.class));


    }

}
