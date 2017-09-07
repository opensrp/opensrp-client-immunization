package org.smartregister.immunization.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.view.ServiceGroup;
import org.smartregister.immunization.view.ServiceRowGroup;
import org.smartregister.immunization.view.VaccineCard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceRowAdapterTest extends BaseUnitTest {

    @Mock
    private Context context;

    @Mock
    private ServiceRowAdapter serviceRowAdapter;

    @Mock
    private AttributeSet attributeSet;

    @Mock
    private ServiceRowGroup serviceRowGroup;

    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;

    @Mock
    protected View convertView;

    @Mock
    protected ViewGroup parentView;

    @Mock
    private VaccineCard vaccineCard;

    @Mock
    private VaccineWrapper vaccineWrapper;

    @Before
    public void setUp() {
        serviceRowAdapter = Mockito.mock(ServiceRowAdapter.class);
        initMocks(this);
    }

    @Test
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws JSONException {

        assertNotNull(new ServiceRowAdapter(context, serviceRowGroup,true));
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
        serviceRowAdapter = new ServiceRowAdapter(context, serviceRowGroup,true);
        assertNotNull(serviceRowAdapter);
        assertEquals(3, serviceRowAdapter.getCount());
    }


}
