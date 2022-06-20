package org.smartregister.immunization.domain;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceTriggerTest extends BaseUnitTest {

    public static final String stringdata1 = "{\"reference\": \"dob\",\"offset\": \"+0d\"}";
    public static final String stringdata2 = "{\"reference\": \"multiple\",\"multiple\": {\"condition\": \"OR\",\"prerequisites\": [ \"Vit A IFC 2\", \"dob\"]},\"offset\": \"+6m\" }";
    public static final String stringdata3 = "{\"reference\": \"prerequisite\",\"prerequisite\": \"Vit A 2\",\"offset\": \"+6m\"}";

    @Test
    public void assertinitConstructorWithTestData() throws JSONException {
        JSONObject data1 = new JSONObject(stringdata1);
        JSONObject data2 = new JSONObject(stringdata2);
        JSONObject data3 = new JSONObject(stringdata3);
        Assert.assertNotNull(ServiceTrigger.init(data1));
        Assert.assertNotNull(ServiceTrigger.init(data2));
        Assert.assertNotNull(ServiceTrigger.init(data3));
        Assert.assertNull(ServiceTrigger.init(null));
    }

    @Test
    public void assertGetMethodsReturnsCorrectValues() throws JSONException {
        JSONObject data1 = new JSONObject(stringdata1);
        JSONObject data2 = new JSONObject(stringdata2);
        JSONObject data3 = new JSONObject(stringdata3);

        String condition = "OR";
        List<String> prerequisites = new ArrayList<>();
        prerequisites.add("Vit A IFC 2");
        prerequisites.add("dob");

        Assert.assertEquals(ServiceTrigger.init(data2).getMultiple().getCondition(), condition);
        Assert.assertEquals(ServiceTrigger.init(data2).getMultiple().getPrerequisites(), prerequisites);
        Assert.assertEquals(ServiceTrigger.init(data2).getReference(), ServiceTrigger.Reference.MULTIPLE);
        Assert.assertEquals(ServiceTrigger.init(data1).getOffset(), "+0d");
        Assert.assertEquals(ServiceTrigger.init(data3).getPrerequisite(), "Vit A 2");

    }

}
