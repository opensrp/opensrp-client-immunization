package org.smartregister.immunization.domain;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.smartregister.immunization.BaseUnitTest;

/**
 * Created by real on 24/10/17.
 */

public class ServiceScheduleTest extends BaseUnitTest {

    @InjectMocks
    ServiceSchedule serviceSchedule;
    @Before
    public void setup(){
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getServiceScheduleTest() throws Exception{
        JSONArray array = new JSONArray(ServiceData.recurringservice);
        JSONArray services = array.getJSONObject(0).getJSONArray("services");
        Assert.assertNotNull(serviceSchedule.getServiceSchedule(services.getJSONObject(0).getJSONObject("schedule")));
    }

    @Test
    public void constructorTest(){
        Assert.assertNotNull(new ServiceSchedule(null,null));
    }

}
