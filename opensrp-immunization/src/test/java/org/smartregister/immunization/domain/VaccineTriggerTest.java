package org.smartregister.immunization.domain;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.jsonmapping.Due;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;
import org.smartregister.util.JsonFormUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineTriggerTest extends BaseUnitTest {

    public static final String CHILD = "child";
    public static final String stringdata1 = "{ \"reference\": \"dob\", \"offset\": \"+0d\"}";
    public static final String stringdata2 = "{\"reference\": \"prerequisite\", \"prerequisite\": \"OPV 1\", \"offset\": \"+28d\"}";
    @Mock
    private ImmunizationLibrary immunizationLibrary;
    @Mock
    private VaccineRepository vaccineRepository;
    @Mock
    private Context context;
    @Mock
    private AlertService alertService;
    @Mock
    private AppProperties appProperties;

    @Test
    public void assertInitReturnsNonNullTriggers() {
        Due data1 = JsonFormUtils.gson.fromJson(stringdata1, Due.class);
        Due data2 = JsonFormUtils.gson.fromJson(stringdata2, Due.class);
        Assert.assertNotNull(VaccineTrigger.init(CHILD, data1));

        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(immunizationLibrary.getVaccines(IMConstants.VACCINE_TYPE.CHILD)).thenReturn(new VaccineRepo.Vaccine[]{VaccineRepo.Vaccine.opv0, VaccineRepo.Vaccine.opv1, VaccineRepo.Vaccine.bcg});
        VaccineTrigger vaccineTrigger = VaccineTrigger.init(CHILD, data2);
        Assert.assertNotNull(vaccineTrigger);
    }

    @Test
    public void assertGetMethodsReturnsCorrectValues() {
        Due data1 = JsonFormUtils.gson.fromJson(stringdata1, Due.class);

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        VaccineSchedule.standardiseCalendarDate(calendar);
        VaccineTrigger.init(CHILD, data1);
        Assert.assertEquals(calendar.getTime(),
                VaccineTrigger.init(CHILD, data1).getFireDate(Collections.EMPTY_LIST, date));
    }

    @Test
    public void assertGetWindowTestReturnsCurrentWindow() {
        Due data1 = JsonFormUtils.gson.fromJson(stringdata1, Due.class);
        VaccineTrigger vaccineTrigger = Mockito.mock(VaccineTrigger.class);
        VaccineTrigger.init(CHILD, data1);
        String notNull = vaccineTrigger.getWindow();
        Mockito.verify(vaccineTrigger, Mockito.times(1)).getWindow();
        Assert.assertNull(notNull);

        Due due = new Due();
        due.reference = "prerequisite";
        due.prerequisite = "OPV 0";
        due.offset = "";
        due.window = "win";

        vaccineTrigger = VaccineTrigger.init(CHILD, due);
        Assert.assertNotNull(vaccineTrigger.getWindow());
        Assert.assertEquals("win", vaccineTrigger.getWindow());

    }

    @Test
    public void testInitCreatesValidVaccineTriggerForExpiryConstructorParamWithLMP() {

        Due expiry = new Due();
        expiry.reference = "LMP";
        expiry.offset = "+1y, +3m, -1d";

        VaccineTrigger vaccineTrigger = VaccineTrigger.init(expiry);
        Assert.assertNotNull(vaccineTrigger);

        Assert.assertEquals("LMP", ReflectionHelpers.getField(vaccineTrigger, "reference").toString());
        Assert.assertEquals("+1y, +3m, -1d", ReflectionHelpers.getField(vaccineTrigger, "offset"));

    }

    @Test
    public void testInitCreatesValidVaccineTriggerForExpiryConstructorParamWithDOB() {

        Due expiry = new Due();
        expiry.reference = "DOB";
        expiry.offset = "+2m,+2d";

        VaccineTrigger vaccineTrigger = VaccineTrigger.init(expiry);
        Assert.assertNotNull(vaccineTrigger);

        Assert.assertEquals("DOB", ReflectionHelpers.getField(vaccineTrigger, "reference").toString());
        Assert.assertEquals("+2m,+2d", ReflectionHelpers.getField(vaccineTrigger, "offset"));

    }

    @Test
    public void testInitReturnsNullForNullExpiryConstructorParam() {
        Due nullDueParam = null;
        VaccineTrigger vaccineTrigger = VaccineTrigger.init(nullDueParam);
        Assert.assertNull(vaccineTrigger);

    }
}
