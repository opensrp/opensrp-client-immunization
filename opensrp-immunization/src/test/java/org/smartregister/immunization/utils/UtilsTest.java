package org.smartregister.immunization.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.Utils;

import java.util.Properties;

/**
 * Created by ndegwamartin on 2019-07-23.
 */
@PrepareForTest({ImmunizationLibrary.class})
public class UtilsTest {
    @Rule
    public PowerMockRule rule = new PowerMockRule();
    @Mock
    private ImmunizationLibrary immunizationLibrary;
    @Mock
    private Properties properties;

    private static String OFFSET_days = "+2d";
    private static String OFFSET_days_negative = "-2d";
    private static String OFFSET_month_days = "+4m,3d";
    private static String OFFSET_months = "+6m";
    private static String OFFSET_years = "-5y";
    private static String RELAXATION_DAYS = "2";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForDaysDefault() {
        String result = Utils.updateRelaxationDays(OFFSET_days);
        Assert.assertEquals(OFFSET_days, result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForDays() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        PowerMockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_days);
        Assert.assertEquals("+0d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForDaysNegative() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        PowerMockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_days_negative);
        Assert.assertEquals("-4d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForMonths() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        PowerMockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_months);
        Assert.assertEquals("+6m,-2d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForYears() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        PowerMockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_years);
        Assert.assertEquals("-5y,-2d", result);
    }

    @Test
    public void testUpdateRelaxationDaysGivesCorrectOffsetForMonthsDays() {

        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.getProperties()).thenReturn(properties);
        PowerMockito.when(properties.getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS)).thenReturn(RELAXATION_DAYS);

        String result = Utils.updateRelaxationDays(OFFSET_month_days);
        Assert.assertEquals("+4m,1d", result);
    }
}
