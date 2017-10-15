package org.smartregister.immunization.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.view.ServiceGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceCardAdapterTest extends BaseUnitTest {

    @Mock
    private Context context;

    @Mock
    private ServiceCardAdapter serviceCardAdapter;

    @Mock
    private ServiceGroup serviceGroup;

    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;

    @Mock
    protected View convertView;

    @Mock
    protected ViewGroup parentView;

    @Before
    public void setUp() {
        serviceCardAdapter = Mockito.mock(ServiceCardAdapter.class);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws JSONException {
        org.junit.Assert.assertNotNull(new ServiceCardAdapter(context, serviceGroup));
    }

    @Test
    public void assertGetCountReturnsTheCorrectNumberOfItems() throws Exception {
        HashMap<String ,List<ServiceType>> vaccineData = new HashMap<String, List<ServiceType>>() ;
        ArrayList<ServiceType> list1 = new ArrayList<ServiceType>();
        list1.add(new ServiceType());
        ArrayList<ServiceType> list2 = new ArrayList<ServiceType>();
        list2.add(new ServiceType());
        ArrayList<ServiceType> list3 = new ArrayList<ServiceType>();
        list3.add(new ServiceType());

        vaccineData.put("servicetype1", list1);
        vaccineData.put("servicetype2", list2);
        vaccineData.put("servicetype3", list3);


        List<Alert> alerts = new ArrayList<>();



        serviceGroup.setData(commonPersonObjectClient, vaccineData, Collections.EMPTY_LIST, alerts);
        Mockito.when(serviceGroup.getServiceTypeKeys()).thenReturn(getServiceTypeKeys(vaccineData));
        serviceCardAdapter = new ServiceCardAdapter(context, serviceGroup);
        org.junit.Assert.assertNotNull(serviceCardAdapter);
        org.junit.Assert.assertEquals(vaccineData.size(), serviceCardAdapter.getCount());
    }

    public static List<String> getServiceTypeKeys(HashMap<String, List<ServiceType>> vaccineData) {
        List<String> keys = new ArrayList<>();
        if (vaccineData == null || vaccineData.isEmpty()) {
            return keys;
        }
        for (String key : vaccineData.keySet()) {
            keys.add(key);
        }
        return keys;
    }


}
