package org.smartregister.immunization.util;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;

/**
 * Created by ndegwamartin on 28/06/2021.
 */
public class VaccinatorUtilsTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessConfigCalendarOffsetReturnsCorrectCalendarInstanceForAddedMonthAndDays() {
        LocalDate date = LocalDate.parse("2020-03-28");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.toDate());
        VaccinatorUtils.processConfigCalendarOffset(calendar, "+3m,+7d");

        Assert.assertEquals(Calendar.JULY, calendar.get(Calendar.MONTH));
        Assert.assertEquals(5, calendar.get(Calendar.DAY_OF_MONTH));

    }

    @Test
    public void testProcessConfigCalendarOffsetReturnsCorrectCalendarInstanceForAddedMonthAndSubtractedDays() {
        LocalDate date = LocalDate.parse("2020-04-28");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date.toDate());
        VaccinatorUtils.processConfigCalendarOffset(calendar, "+2m,-7d");

        Assert.assertEquals(Calendar.JUNE, calendar.get(Calendar.MONTH));
        Assert.assertEquals(21, calendar.get(Calendar.DAY_OF_MONTH));

    }

    @Test
    public void testProcessConfigCalendarOffsetReturnsCorrectDateTimeInstanceForAddedMonthAndDays() {
        DateTime dateTime = new DateTime(2020, 3, 28, 0, 0, 0, 0);
        DateTime resultDateTime = VaccinatorUtils.processConfigDateTimeOffset(dateTime, "+3m,+7d");

        Assert.assertEquals(7, resultDateTime.getMonthOfYear());
        Assert.assertEquals(5, resultDateTime.getDayOfMonth());

    }

    @Test
    public void testProcessConfigCalendarOffsetReturnsCorrectDateTimeInstanceForAddedMonthAndSubtractedDays() {

        DateTime dateTime = new DateTime(2020, 4, 28, 12, 0, 0, 0);
        DateTime resultDateTime = VaccinatorUtils.processConfigDateTimeOffset(dateTime, "+2m,-7d");

        Assert.assertEquals(6, resultDateTime.getMonthOfYear());
        Assert.assertEquals(21, resultDateTime.getDayOfMonth());

    }

    @Test
    public void testProcessOffsetValueInDaysForAddedMonthAndDays() {
        int days = VaccinatorUtils.processOffsetValueInDays("+3m,+7d");
        Assert.assertEquals(98, days);

    }

    @Test
    public void testProcessOffsetValueInDaysForAddedMonthAndSubtractedDays() {

        int days = VaccinatorUtils.processOffsetValueInDays("+2m,-7d");
        Assert.assertEquals(54, days);

    }

    @Test
    public void testProcessOffsetValueInDaysForAddedYearsMonthAndDays() {
        int days = VaccinatorUtils.processOffsetValueInDays("+2y, +3m,+7d");
        Assert.assertEquals(830, days);

    }

    @Test
    public void testProcessOffsetValueInDaysForSubtractedYearsMonthAndDays() {
        int days = VaccinatorUtils.processOffsetValueInDays("-2y, -3m,-7d");
        Assert.assertEquals(-830, days);

    }
}