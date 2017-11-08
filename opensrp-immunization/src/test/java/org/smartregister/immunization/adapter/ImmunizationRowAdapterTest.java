package org.smartregister.immunization.adapter;

import android.content.Context;
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
import org.smartregister.immunization.view.ImmunizationRowGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */

public class ImmunizationRowAdapterTest extends BaseUnitTest {

    @Mock
    private Context context;

    @Mock
    private ImmunizationRowGroup vaccineGroup;

    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;

    private final int magicNumber = 231231;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertConstructorsCreateNonNullObjectsOnInstantiation() throws JSONException {
        junit.framework.Assert.assertNotNull(new ImmunizationRowAdapter(context, vaccineGroup, true));
    }

    @Test
    public void assertGetCountTestReturnsCount() throws Exception {
        JSONObject vaccineData = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("testvalue1");
        jsonArray.put("testvalue2");
        jsonArray.put("testvalue3");
        vaccineData.put("vaccines", jsonArray);
        List<Vaccine> vaccineList = new ArrayList<>();
        List<Alert> alerts = new ArrayList<>();
        vaccineGroup.setData(vaccineData, commonPersonObjectClient, vaccineList, alerts);
        Mockito.when(vaccineGroup.getVaccineData()).thenReturn(vaccineData);
        ImmunizationRowAdapter immunizationRowAdapter = new ImmunizationRowAdapter(context, vaccineGroup, true);
        junit.framework.Assert.assertNotNull(immunizationRowAdapter);
        junit.framework.Assert.assertEquals(jsonArray.length(), immunizationRowAdapter.getCount());

        //returns 0 when throwsexception
        JSONObject errData = new JSONObject();
        errData.put("err", jsonArray);
        vaccineGroup.setData(errData, commonPersonObjectClient, vaccineList, alerts);
        Mockito.when(vaccineGroup.getVaccineData()).thenReturn(errData);
        junit.framework.Assert.assertEquals(0, immunizationRowAdapter.getCount());

        //should return null
        junit.framework.Assert.assertNull(immunizationRowAdapter.getItem(0));

        junit.framework.Assert.assertEquals(immunizationRowAdapter.getItemId(0), magicNumber);

    }

}
