package org.smartregister.immunization.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.jsonmapping.Due;
import org.smartregister.util.JsonFormUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by onaio on 30/08/2017.
 */

@PrepareForTest({ImmunizationLibrary.class})
public class VaccineTriggerTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    public static final String CHILD = "child";
    public static final String stringdata1 = "{ \"reference\": \"dob\", \"offset\": \"+0d\"}";
    public static final String stringdata2 = "{\"reference\": \"prerequisite\", \"prerequisite\": \"OPV 1\", \"offset\": \"+28d\"}";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertInitReturnsNonNullTriggers() {
        Due data1 = JsonFormUtils.gson.fromJson(stringdata1, Due.class);
        Due data2 = JsonFormUtils.gson.fromJson(stringdata2, Due.class);
        Assert.assertNotNull(VaccineTrigger.init(CHILD, data1));

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getVaccines()).thenReturn(new VaccineRepo.Vaccine[]{VaccineRepo.Vaccine.opv0, VaccineRepo.Vaccine.opv1, VaccineRepo.Vaccine.bcg});
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
        vaccineTrigger = new VaccineTrigger("", "win", VaccineRepo.Vaccine.opv0);
        Assert.assertNotNull(vaccineTrigger.getWindow());
    }

}
