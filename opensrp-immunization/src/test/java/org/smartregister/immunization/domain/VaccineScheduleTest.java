package org.smartregister.immunization.domain;

import android.test.mock.MockContext;

import net.sqlcipher.database.SQLiteDatabase;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.junit.Test;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Created by real on 23/10/17.
 */
@PrepareForTest({ImmunizationLibrary.class})
public class VaccineScheduleTest extends BaseUnitTest{

    @Rule
    public PowerMockRule rule = new PowerMockRule();

//    @Mock
//    private SQLiteDatabase sqliteDatabase;

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private Context context;

    @Mock
    private AlertService alertService;

    @Mock
    VaccineSchedule vaccineSchedule;
    Vaccine newVaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.PROGRAMCLIENTID, "OPV", 0, new Date(),
            VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void updateOfflineAlertsTest() throws Exception{

        VaccineSchedule vaccineSchedule = new VaccineSchedule(null,null,null,null);
        vaccineSchedule.init(new JSONArray(VaccineData.vaccines),new JSONArray(VaccineData.special_vacines),"child");


        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().vaccineRepository()).thenReturn(vaccineRepository);
        PowerMockito.when(ImmunizationLibrary.getInstance().vaccineRepository().findByEntityId(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().alertService()).thenReturn(alertService);

        Assert.assertNotNull(vaccineSchedule.updateOfflineAlerts(VaccineTest.BASEENTITYID,new DateTime(),"child"));
//        VaccineRepository vaccineRepositoryspy = Mockito.mock(VaccineRepository.class);
//       Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
//        Mockito.when(immunizationLibrary.vaccineRepository()).thenReturn(vaccineRepositoryspy);
//
////       Mockito.when(ImmunizationLibrary.getInstance().vaccineRepository()).thenReturn(vaccineRepositoryspy);
////        ImmunizationLibrary.init(Mockito.mock(Context.class),Mockito.mock(Repository.class), Mockito.mock(CommonFtsObject.class));
//        Mockito.when(vaccineRepositoryspy.findByEntityId(Mockito.any(String.class))).thenReturn(null);
//        Mockito.when(vaccineRepositoryspy.getReadableDatabase()).thenReturn(sqliteDatabase);
//        Assert.assertNotNull(vaccineSchedule.updateOfflineAlerts(VaccineTest.BASEENTITYID,new DateTime(),"child"));

    }


    @Test
    public void constructorTest() throws Exception{
        Assert.assertNotNull(new VaccineSchedule(null,null,null,null));
    }

    @Test
    public void initAndInitVaccineTest()throws Exception{
        VaccineSchedule vaccineSchedule = new VaccineSchedule(null,null,null,null);
//        VaccineSchedule vaccineSchedule = Mockito.spy(this.vaccineSchedule);
        vaccineSchedule.init(new JSONArray(VaccineData.vaccines),new JSONArray(VaccineData.special_vacines),"child");
        vaccineSchedule.init(new JSONArray(VaccineData.vaccines),new JSONArray(VaccineData.special_vacines),"");
        Assert.assertNotNull(vaccineSchedule.getVaccineSchedule("child","OPV 0"));
        Assert.assertNull(vaccineSchedule.getVaccineSchedule("",""));

    }

}
