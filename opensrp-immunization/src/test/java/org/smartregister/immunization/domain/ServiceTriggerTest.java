package org.smartregister.immunization.domain;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceTriggerTest extends BaseUnitTest {

    public static final String stringdata1 = "{\n" +
            "            \"reference\": \"dob\",\n" +
            "            \"offset\": \"+0d\"\n" +
            "          }";
    public static final String stringdata2 = "{\n" +
            "            \"reference\": \"multiple\",\n" +
            "            \"multiple\": {\n" +
            "              \"condition\": \"OR\",\n" +
            "              \"prerequisites\": [\n" +
            "                \"Vit A IFC 2\",\n" +
            "                \"dob\"\n" +
            "              ]\n" +
            "            }" + ",\n" +
            "            \"offset\": \"+6m\"" +
            "}";
    public static final String stringdata3 = "{\n" +
            "            \"reference\": \"prerequisite\",\n" +
            "            \"prerequisite\": \"Vit A 2\",\n" +
            "            \"offset\": \"+6m\"\n" +
            "          }";

    @Test
    public void assertInitReturnsNonNullTriggers() throws JSONException {
        JSONObject data1 = new JSONObject(stringdata1);
        JSONObject data2 = new JSONObject(stringdata2);
        JSONObject data3 = new JSONObject(stringdata3);
        assertNotNull(ServiceTrigger.init(data1));
        assertNotNull(ServiceTrigger.init(data2));
        assertNotNull(ServiceTrigger.init(data3));
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

        assertEquals(ServiceTrigger.init(data2).getMultiple().getCondition(), condition);
        assertEquals(ServiceTrigger.init(data2).getMultiple().getPrerequisites(), prerequisites);
        assertEquals(ServiceTrigger.init(data2).getReference(), ServiceTrigger.Reference.MULTIPLE);
        assertEquals(ServiceTrigger.init(data1).getOffset(), "+0d");
        assertEquals(ServiceTrigger.init(data3).getPrerequisite(), "Vit A 2");

    }

}
