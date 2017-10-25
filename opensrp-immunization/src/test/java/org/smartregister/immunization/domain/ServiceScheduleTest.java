package org.smartregister.immunization.domain;

import junit.framework.Assert;

import net.sqlcipher.database.SQLiteDatabase;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.Context;
import org.smartregister.clientandeventmodel.DateUtil;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.service.AlertService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by real on 24/10/17.
 */
@PrepareForTest({ImmunizationLibrary.class})
public class ServiceScheduleTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private Context context;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private ServiceSchedule serviceSchedule;

//    @Mock
//    private SQLiteDatabase sqLiteDatabase;
    @Mock
    private ServiceTrigger dueTrigger;
    @Mock
    private ServiceTrigger expTrigger;
    @Mock
    private RecurringServiceTypeRepository recurringServiceTypeRepository;
    @Mock
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Before
    public void setup(){
        org.mockito.MockitoAnnotations.initMocks(this);
    }
    @Test
    public void updateOfflineAlertsTest() throws Exception{

        ServiceSchedule serviceSchedule = new ServiceSchedule(dueTrigger,expTrigger);

        List<String> types = new ArrayList<String>();
        types.add("TT");

        ServiceType serviceType = new ServiceType(0l, ServiceTypeTest.TYPE, ServiceTypeTest.NAME, ServiceTypeTest.SERVICENAMEENTITY, ServiceTypeTest.SERVICENAMEENTITYID, ServiceTypeTest.DATEENTITY, ServiceTypeTest.DATEENTITYID, ServiceTypeTest.UNITS, ServiceTypeTest.SERVICELOGIC, ServiceTypeTest.PREREQUISITE, "preOffset", "expiryOffset", "milestoneOffset", 0l);
        List<ServiceType>serviceTypeList = new ArrayList<ServiceType>();
        serviceTypeList.add(serviceType);

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceTypeRepository()).thenReturn(recurringServiceTypeRepository);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceRecordRepository()).thenReturn(recurringServiceRecordRepository);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceTypeRepository().fetchTypes()).thenReturn(types);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().alertService()).thenReturn(alertService);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceTypeRepository().findByType(Mockito.any(String.class))).thenReturn(serviceTypeList);
        Alert alert = new Alert(VaccineTest.BASEENTITYID, VaccineTest.NAME, VaccineTest.NAME.toLowerCase().replace(" ", ""),
                AlertStatus.complete, DateUtil.yyyyMMdd.format(new Date()), DateUtil.yyyyMMdd.format(new Date()), true);

        //Mockito.when(serviceSchedule.getOfflineAlert(Mockito.any(ServiceType.class),Mockito.any(List.class),Mockito.any(String.class),Mockito.any(DateTime.class))).thenReturn(Mockito.mock(Alert.class));
        serviceSchedule.updateOfflineAlerts(VaccineTest.BASEENTITYID,new DateTime());
        serviceSchedule.updateOfflineAlerts("TT",VaccineTest.BASEENTITYID,null);
        serviceSchedule.updateOfflineAlerts("TT",VaccineTest.BASEENTITYID,new DateTime());

        List<String>offsets = new ArrayList<String>();
        offsets.add("+10d");
        offsets.add("+10m");
        offsets.add("+10y");
        offsets.add("-10d");
        offsets.add("+xxy");//shouldthrowexception
        Assert.assertNotNull(serviceSchedule.addOffsetToDateTime(new DateTime(),offsets));

    }

    @Test
    public void getServiceScheduleTest() throws Exception{
        JSONArray array = new JSONArray(ServiceData.recurringservice);
        JSONArray services = array.getJSONObject(0).getJSONArray("services");
        Assert.assertNotNull(serviceSchedule.getServiceSchedule(services.getJSONObject(0).getJSONObject("schedule")));
        Assert.assertNull(serviceSchedule.standardiseDateTime(null));
        Assert.assertNotNull(serviceSchedule.getDueTrigger());
        Assert.assertNotNull(serviceSchedule.getExpiryTrigger());
        serviceSchedule.standardiseCalendarDate(java.util.Calendar.getInstance());

    }

    @Test
    public void constructorTest(){
        Assert.assertNotNull(new ServiceSchedule(null,null));
    }

}
