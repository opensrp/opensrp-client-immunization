package org.smartregister.immunization.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import junit.framework.Assert;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.view.ServiceRowGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceRowAdapterTest extends BaseUnitTest {

    @Mock
    private Context context;

    @Mock
    private ServiceRowAdapter serviceRowAdapter;

    @Mock
    private ServiceRowGroup serviceRowGroup;

    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;

    @Mock
    protected View convertView;

    @Mock
    protected ViewGroup parentView;

    @Before
    public void setUp() {
        serviceRowAdapter = Mockito.mock(ServiceRowAdapter.class);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws JSONException {
        org.junit.Assert.assertNotNull(new ServiceRowAdapter(context, serviceRowGroup, true));
    }

    @Test
    public void assertGetCountReturnsTheCorrectNumberOfItems() throws Exception {
        ArrayList<ServiceType> list1 = new ArrayList<ServiceType>();
        list1.add(new ServiceType());
        list1.add(new ServiceType());
        list1.add(new ServiceType());
        List<Alert> alerts = new ArrayList<>();
        serviceRowGroup.setData(commonPersonObjectClient, list1, Collections.EMPTY_LIST, alerts);
        Mockito.when(serviceRowGroup.getServiceTypes()).thenReturn(list1);
        serviceRowAdapter = new ServiceRowAdapter(context, serviceRowGroup, true);
        org.junit.Assert.assertNotNull(serviceRowAdapter);
        junit.framework.Assert.assertEquals(list1.size(), serviceRowAdapter.getCount());

        //should return null
        Assert.assertNull(serviceRowAdapter.getItem(0));

        Assert.assertEquals(serviceRowAdapter.getItemId(0),231231);
    }

}
