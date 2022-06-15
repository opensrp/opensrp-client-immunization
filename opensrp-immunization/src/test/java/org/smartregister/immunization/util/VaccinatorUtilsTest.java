package org.smartregister.immunization.util;

import android.content.Context;
import android.content.res.AssetManager;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ndegwamartin on 28/06/2021.
 */
public class VaccinatorUtilsTest {

    @Mock
    private Context context;

    @Mock
    private AssetManager assetManager;


    @Test
    public void testGetVaccineFilesReturnsListOfFilesFromVaccinesFolder() throws IOException {

        Mockito.doReturn(assetManager).when(context).getAssets();
        Mockito.doReturn(new String[]{VaccinatorUtils.vaccines_file, VaccinatorUtils.special_vaccines_file, VaccinatorUtils.mother_vaccines_file}).when(assetManager).list(VaccinatorUtils.vaccines_folder);

        List<String> resultList = VaccinatorUtils.getVaccineFiles(context);
        Assert.assertNotNull(resultList);
        Assert.assertEquals(VaccinatorUtils.vaccines_file, resultList.get(0));
        Assert.assertEquals(VaccinatorUtils.special_vaccines_file, resultList.get(1));
        Assert.assertEquals(VaccinatorUtils.mother_vaccines_file, resultList.get(2));

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(assetManager).list(stringArgumentCaptor.capture());
        String folderName = stringArgumentCaptor.getValue();
        Assert.assertEquals(VaccinatorUtils.vaccines_folder, folderName);

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