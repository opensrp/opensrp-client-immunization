package org.smartregister.immunization.domain;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.immunization.BaseUnitTest;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineTriggerTest extends BaseUnitTest {

    public static final String CHILD = "child";
    public static final String stringdata1 = "{ \"reference\": \"dob\",\"offset\": \"+0d\"}";
    public static final String stringdata2 = "{\"reference\": \"prerequisite\",\"prerequisite\": \"OPV 1\",\"offset\": \"+28d\"}";

    @Test
    public void assertInitReturnsNonNullTriggers() throws JSONException {
        JSONObject data1 = new JSONObject(stringdata1);
        JSONObject data2 = new JSONObject(stringdata2);
        junit.framework.Assert.assertNotNull(VaccineTrigger.init(CHILD, data1));
        junit.framework.Assert.assertNotNull(VaccineTrigger.init(CHILD, data2));
    }

    @Test
    public void assertGetMethodsReturnsCorrectValues() throws JSONException {
        JSONObject data1 = new JSONObject(stringdata1);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        VaccineSchedule.standardiseCalendarDate(calendar);
        VaccineTrigger.init(CHILD, data1);
        junit.framework.Assert.assertEquals(calendar.getTime(), VaccineTrigger.init(CHILD, data1).getFireDate(Collections.EMPTY_LIST, date));
    }

    @Test
    public void assertGetWindowTestReturnsCurrentWindow() throws JSONException {
        JSONObject data1 = new JSONObject(stringdata1);
        VaccineTrigger vaccineTrigger = Mockito.mock(VaccineTrigger.class);
        vaccineTrigger.init(CHILD, data1);
        String notNull = vaccineTrigger.getWindow();
        Mockito.verify(vaccineTrigger,Mockito.times(1)).getWindow();
        junit.framework.Assert.assertNull(notNull);
        vaccineTrigger = new VaccineTrigger("","win");
        junit.framework.Assert.assertNotNull(vaccineTrigger.getWindow());
    }

}
