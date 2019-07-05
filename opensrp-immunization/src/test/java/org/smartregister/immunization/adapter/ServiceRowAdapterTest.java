package org.smartregister.immunization.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceRecordTest;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceTypeTest;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.ServiceWrapperTest;
import org.smartregister.immunization.view.ServiceRowGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by onaio on 30/08/2017.
 */
@Config (shadows = {FontTextViewShadow.class, ImageUtilsShadow.class, ServiceRowCardShadow.class})
public class ServiceRowAdapterTest extends BaseUnitTest {

    private final int magicNumber = 231231;
    private final String magicDate = "1985-07-24T00:00:00.000Z";
    @Mock
    protected View convertView;
    @Mock
    protected ViewGroup parentView;
    @Mock
    private Context context;
    private ServiceRowAdapter serviceRowAdapter;
    private ServiceRowGroup view;
    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;
    private ServiceWrapper wrapper;
    private String type = "SERVICETYPE";
    private List<ServiceType> serviceTypeList = new ArrayList<>();
    private List<ServiceRecord> serviceRecordList = new ArrayList<>();
    private List<Alert> alertList = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        view = new ServiceRowGroup(RuntimeEnvironment.application, true);
        setDataForTest(magicDate);
        serviceRowAdapter = new ServiceRowAdapter(RuntimeEnvironment.application, view, true, serviceTypeList,
                serviceRecordList, alertList);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    public void setDataForTest(String dateTimeString) {
        ArrayList<ServiceWrapper> wrappers = new ArrayList<>();
        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);

        wrappers.add(wrapper);
        wrapper = new ServiceWrapper();

        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
        wrappers.add(wrapper);
        wrapper = new ServiceWrapper();
        wrapper.setDefaultName(ServiceWrapperTest.DEFAULTNAME);

        wrappers.add(wrapper);

        HashMap<String, String> detail = new HashMap<>();
        detail.put("dob", dateTimeString);
        detail.put("gender", "male");
        detail.put("zeir_id", "1");
        detail.put("first_name", "");
        detail.put("last_name", "");
        CommonPersonObjectClient childdetails = new CommonPersonObjectClient("1", detail, "NME");
        childdetails.setColumnmaps(detail);
        Alert alert = new Alert("", "", "", AlertStatus.complete, "", "");

        ArrayList<Alert> alertlist = new ArrayList<Alert>();
        alertlist.add(alert);
        Map<String, List<ServiceType>> serviceTypeMap = new HashMap<>();
        ServiceType serviceType = new ServiceType();
        serviceType.setId(0l);
        serviceType.setType(ServiceTypeTest.TYPE);
        serviceType.setName(ServiceTypeTest.NAME);
        serviceType.setServiceNameEntity(ServiceTypeTest.SERVICENAMEENTITY);
        serviceType.setServiceNameEntityId(ServiceTypeTest.SERVICENAMEENTITYID);
        serviceType.setDateEntity(ServiceTypeTest.DATEENTITY);
        serviceType.setDateEntityId(ServiceTypeTest.DATEENTITYID);
        serviceType.setUnits(ServiceTypeTest.UNITS);
        serviceType.setServiceLogic(ServiceTypeTest.SERVICELOGIC);
        serviceType.setPrerequisite(ServiceTypeTest.PREREQUISITE);
        serviceType.setPreOffset(ServiceTypeTest.PREOFFSET);
        serviceType.setExpiryOffset(ServiceTypeTest.EXPIRYOFFSET);
        serviceType.setMilestoneOffset(ServiceTypeTest.MILESTONEOFFSET);
        serviceType.setUpdatedAt(0l);
        ArrayList<ServiceType> serviceTypes = new ArrayList<>();
        serviceTypes.add(serviceType);
        serviceTypeMap.put(type, serviceTypes);
        ArrayList<ServiceRecord> servcServiceRecords = new ArrayList<>();
        ServiceRecord serviceRecord = new ServiceRecord(0l, ServiceRecordTest.BASEENTITYID,
                ServiceRecordTest.PROGRAMCLIENTID, 0l, ServiceRecordTest.VALUE, new Date(), ServiceRecordTest.ANMID,
                ServiceRecordTest.LOCATIONID, ServiceRecordTest.SYNCED, ServiceRecordTest.EVENTID,
                ServiceRecordTest.FORMSUBMISSIONID, 0l, new Date());
        serviceRecord.setDate(new Date());
        serviceRecord.setName(ServiceWrapperTest.DEFAULTNAME);
        servcServiceRecords.add(serviceRecord);
        view.setData(childdetails, serviceTypes, servcServiceRecords, alertlist);
    }

    @Test
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() {
        org.junit.Assert
                .assertNotNull(new ServiceRowAdapter(context, view, true, serviceTypeList, serviceRecordList, alertList));
    }

    @Test
    public void assertGetCountReturnsTheCorrectNumberOfItems() {

        junit.framework.Assert.assertEquals(1, serviceRowAdapter.getCount());

        //should return null
        Assert.assertNull(serviceRowAdapter.getItem(0));

        Assert.assertEquals(serviceRowAdapter.getItemId(0), magicNumber);
    }

    @Test
    public void assertGetViewReturnsServiceRowView() {
        Assert.assertEquals(serviceRowAdapter.getView(0, null, null) != null, true);
    }

}
