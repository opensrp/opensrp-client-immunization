package org.smartregister.immunization.domain;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.json.JSONArray;
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
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
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
    private Context context;

    @Mock
    private AlertService alertService;

    @InjectMocks
    private ServiceSchedule serviceSchedule;

    @Mock
    private ServiceTrigger dueTrigger;

    @Mock
    private ServiceTrigger expTrigger;

    private final String magicString = "TT";

    @Mock
    private RecurringServiceTypeRepository recurringServiceTypeRepository;

    @Mock
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertUpdateOfflineAlertsReturnsAlertFromGetOflineAlerts() throws Exception {

        ServiceSchedule serviceSchedule = new ServiceSchedule(dueTrigger, expTrigger);

        List<String> types = new ArrayList<String>();
        types.add(magicString);

        ServiceType serviceType = new ServiceType(0l, ServiceTypeTest.TYPE, ServiceTypeTest.NAME, ServiceTypeTest.SERVICENAMEENTITY, ServiceTypeTest.SERVICENAMEENTITYID, ServiceTypeTest.DATEENTITY, ServiceTypeTest.DATEENTITYID, ServiceTypeTest.UNITS, ServiceTypeTest.SERVICELOGIC, ServiceTypeTest.PREREQUISITE, "preOffset", "expiryOffset", "milestoneOffset", 0l);
        List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
        serviceTypeList.add(serviceType);

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceTypeRepository()).thenReturn(recurringServiceTypeRepository);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceRecordRepository()).thenReturn(recurringServiceRecordRepository);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceTypeRepository().fetchTypes()).thenReturn(types);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().alertService()).thenReturn(alertService);
        PowerMockito.when(ImmunizationLibrary.getInstance().recurringServiceTypeRepository().findByType(Mockito.any(String.class))).thenReturn(serviceTypeList);

        serviceSchedule.updateOfflineAlerts(VaccineTest.BASEENTITYID, new DateTime());
        serviceSchedule.updateOfflineAlerts(magicString, VaccineTest.BASEENTITYID, null);
        serviceSchedule.updateOfflineAlerts(magicString, VaccineTest.BASEENTITYID, new DateTime());
        ServiceRecord serviceRecord = new ServiceRecord(0l, ServiceRecordTest.BASEENTITYID, ServiceRecordTest.PROGRAMCLIENTID, 0l, ServiceRecordTest.VALUE, new Date(), ServiceRecordTest.ANMID, ServiceRecordTest.LOCATIONID, ServiceRecordTest.SYNCED, ServiceRecordTest.EVENTID, ServiceRecordTest.FORMSUBMISSIONID, 0l, new Date());
        serviceRecord.setDate(new Date());
        serviceRecord.setName(ServiceWrapperTest.DEFAULTNAME);
        serviceRecord.setEventId("1");
        ArrayList<ServiceRecord> issuedServices = new ArrayList<ServiceRecord>();
        issuedServices.add(serviceRecord);
        Assert.assertNotNull(serviceSchedule.getOfflineAlert(serviceType, issuedServices, VaccineTest.BASEENTITYID, new DateTime()));
    }

    @Test
    public void assertAddOffsetToCalanderReturnsDateTime() throws Exception {
        List<String> offsets = new ArrayList<String>();
        offsets.add("+10d");
        offsets.add("+10m");
        offsets.add("+10y");
        offsets.add("-10d");
        offsets.add("+xxy");
        Assert.assertNotNull(serviceSchedule.addOffsetToDateTime(new DateTime(), offsets));
    }

    @Test
    public void assertGetServiceScheduleTestWithTestJSONData() throws Exception {
        JSONArray array = new JSONArray(ServiceData.recurringservice);
        JSONArray services = array.getJSONObject(0).getJSONArray("services");
        Assert.assertNotNull(serviceSchedule.getServiceSchedule(services.getJSONObject(0).getJSONObject("schedule")));
        Assert.assertNull(serviceSchedule.standardiseDateTime(null));
        Assert.assertNotNull(serviceSchedule.getDueTrigger());
        Assert.assertNotNull(serviceSchedule.getExpiryTrigger());
        serviceSchedule.standardiseCalendarDate(java.util.Calendar.getInstance());

    }

    @Test
    public void assertConstructorInitiatedTest() {
        Assert.assertNotNull(new ServiceSchedule(null, null));
    }

}
