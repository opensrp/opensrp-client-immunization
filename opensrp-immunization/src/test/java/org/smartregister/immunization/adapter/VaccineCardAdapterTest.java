package org.smartregister.immunization.adapter;

import android.content.Context;
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
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.view.VaccineGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineCardAdapterTest extends BaseUnitTest {

    @Mock
    private Context context;

    @Mock
    private VaccineCardAdapter vaccineCardAdapter;

    @Mock
    private VaccineGroup vaccineGroup;

    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;

    @Mock
    protected View convertView;

    @Mock
    protected ViewGroup parentView;

    private final int magicNumber = 231231;

    @Before
    public void setUp() {
        vaccineCardAdapter = Mockito.mock(VaccineCardAdapter.class);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws JSONException {
        org.junit.Assert.assertNotNull(new VaccineCardAdapter(context, vaccineGroup ,""));
    }

    @Test
    public void assertGetCountReturnsTheCorrectNumberOfItems() throws Exception {
        JSONObject vaccineData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("testvalue1");
        jsonArray.put("testvalue2");
        jsonArray.put("testvalue3");
        vaccineData.put("vaccines", jsonArray);

        List<Vaccine> vaccineList = new ArrayList<>();
        List<Alert> alerts = new ArrayList<>();
        vaccineGroup.setData(vaccineData, commonPersonObjectClient, vaccineList, alerts , "");
        Mockito.when(vaccineGroup.getVaccineData()).thenReturn(vaccineData);
        vaccineCardAdapter = new VaccineCardAdapter(context, vaccineGroup ,"");
        org.junit.Assert.assertNotNull(vaccineCardAdapter);
        org.junit.Assert.assertEquals(jsonArray.length(), vaccineCardAdapter.getCount());

        //returns 0 when throwsexception
        JSONObject errData = new JSONObject();
        errData.put("err", jsonArray);
        vaccineGroup.setData(errData, commonPersonObjectClient, vaccineList, alerts,"mother");
        Mockito.when(vaccineGroup.getVaccineData()).thenReturn(errData);
        junit.framework.Assert.assertEquals(0, vaccineCardAdapter.getCount());

        //should return null
        junit.framework.Assert.assertNull(vaccineCardAdapter.getItem(0));

        junit.framework.Assert.assertEquals(vaccineCardAdapter.getItemId(0), magicNumber);

        vaccineGroup.setData(errData, commonPersonObjectClient, vaccineList, alerts,"child");
        Mockito.when(vaccineGroup.getVaccineData()).thenReturn(errData);
        junit.framework.Assert.assertEquals(0, vaccineCardAdapter.getCount());

        //should return null
        junit.framework.Assert.assertNull(vaccineCardAdapter.getItem(0));

        junit.framework.Assert.assertEquals(vaccineCardAdapter.getItemId(0), magicNumber);
    }
}
