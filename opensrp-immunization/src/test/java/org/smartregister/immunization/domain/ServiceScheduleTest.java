package org.smartregister.immunization.domain;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.Context;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by real on 24/10/17.
 */
public class ServiceScheduleTest extends BaseUnitTest {

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private Context context;

    @Mock
    private AlertService alertService;

    @Mock
    private RecurringServiceTypeRepository recurringServiceTypeRepository;

    @Mock
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private AppProperties appProperties;

    @Before
    public void setUp() {
        mockImmunizationLibrary(immunizationLibrary, context, vaccineRepository, alertService, appProperties);
    }

    @Test
    public void assertUpdateOfflineAlertsReturnsAlertFromGetOflineAlerts() {

        List<String> types = new ArrayList<String>();
        String magicString = "TT";
        types.add(magicString);

        ServiceType serviceType = new ServiceType.Builder(0l, ServiceTypeTest.TYPE, ServiceTypeTest.NAME)
                .withServiceGroup(ServiceTypeTest.SERVICE_GROUP)
                .withServiceNameEntity(ServiceTypeTest.SERVICENAMEENTITY)
                .withServiceNameEntityId(ServiceTypeTest.SERVICENAMEENTITYID)
                .withDateEntity(ServiceTypeTest.DATEENTITY)
                .withDateEntityId(ServiceTypeTest.DATEENTITYID)
                .withUnits(ServiceTypeTest.UNITS)
                .withServiceLogic(ServiceTypeTest.SERVICELOGIC)
                .withPrerequisite(ServiceTypeTest.PREREQUISITE)
                .withPreOffset("preOffset")
                .withExpiryOffset("expiryOffset")
                .withMilestoneOffset("milestoneOffset")
                .withUpdatedAt(0L).build();


        List<ServiceType> serviceTypeList = new ArrayList<ServiceType>();
        serviceTypeList.add(serviceType);

        Mockito.when(immunizationLibrary.recurringServiceTypeRepository()).thenReturn(recurringServiceTypeRepository);
        Mockito.when(immunizationLibrary.recurringServiceRecordRepository()).thenReturn(recurringServiceRecordRepository);
        Mockito.when(recurringServiceTypeRepository.fetchTypes()).thenReturn(types);
        Mockito.when(immunizationLibrary.context()).thenReturn(context);
        Mockito.when(context.alertService()).thenReturn(alertService);
        Mockito.when(recurringServiceTypeRepository.findByType(Mockito.any(String.class))).thenReturn(serviceTypeList);

        ServiceSchedule.updateOfflineAlerts(VaccineTest.BASEENTITYID, new DateTime());
        ServiceSchedule.updateOfflineAlerts(magicString, VaccineTest.BASEENTITYID, null);
        ServiceSchedule.updateOfflineAlerts(magicString, VaccineTest.BASEENTITYID, new DateTime());
        ServiceRecord serviceRecord = new ServiceRecord(0l, ServiceRecordTest.BASEENTITYID,
                ServiceRecordTest.PROGRAMCLIENTID, 0l, ServiceRecordTest.VALUE, new Date(), ServiceRecordTest.ANMID,
                ServiceRecordTest.LOCATIONID, ServiceRecordTest.SYNCED, ServiceRecordTest.EVENTID,
                ServiceRecordTest.FORMSUBMISSIONID, 0l, new Date());
        serviceRecord.setDate(new Date());
        serviceRecord.setName(ServiceWrapperTest.DEFAULTNAME);
        serviceRecord.setEventId("1");
        ArrayList<ServiceRecord> issuedServices = new ArrayList<ServiceRecord>();
        issuedServices.add(serviceRecord);
        Assert.assertNotNull(
                ServiceSchedule.getOfflineAlert(serviceType, issuedServices, VaccineTest.BASEENTITYID, new DateTime()));
    }

    @Test
    public void assertAddOffsetToCalanderReturnsDateTime() {
        List<String> offsets = new ArrayList<String>();
        offsets.add("+10d");
        offsets.add("+10m");
        offsets.add("+10y");
        offsets.add("-10d");
        offsets.add("+xxy");
        Assert.assertNotNull(ServiceSchedule.addOffsetToDateTime(new DateTime(), offsets));
    }

    @Test
    public void assertGetServiceScheduleTestWithTestJSONData() throws Exception {
        JSONArray array = new JSONArray(ServiceData.recurringservice);
        JSONArray services = array.getJSONObject(0).getJSONArray("services");
        ServiceSchedule serviceSchedule = ServiceSchedule.getServiceSchedule(services.getJSONObject(0).getJSONObject(
                "schedule"));
        Assert.assertNotNull(serviceSchedule);
        Assert.assertNull(ServiceSchedule.standardiseDateTime(null));
        Assert.assertNotNull(serviceSchedule.getDueTrigger());
        Assert.assertNotNull(serviceSchedule.getExpiryTrigger());
        ServiceSchedule.standardiseCalendarDate(java.util.Calendar.getInstance());

    }

    @Test
    public void assertConstructorInitiatedTest() {
        Assert.assertNotNull(new ServiceSchedule(null, null));
    }

    @Test
    public void isServiceIssuedShouldReturnTrueWhenServiceIsWithinServiceRecords() {
        ArrayList<ServiceRecord> serviceRecords = new ArrayList<>();

        String[] vaccines = new String[]{"bcg2", "opv1", "opv2", "ipv", "penta"};

        for (int i = 0; i < 4; i++) {
            ServiceRecord dummyServiceRecord = new ServiceRecord();
            dummyServiceRecord.setName(vaccines[i]);
            serviceRecords.add(dummyServiceRecord);
        }

        Assert.assertTrue(ServiceSchedule.isServiceIssued("bcg2", serviceRecords));
    }

}
