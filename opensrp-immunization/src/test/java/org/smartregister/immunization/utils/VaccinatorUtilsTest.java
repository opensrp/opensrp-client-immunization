package org.smartregister.immunization.utils;

import android.app.Activity;
import android.content.res.Resources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.repository.Repository;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.powermock.configuration.ConfigurationType.PowerMock;

/**
 * Created by onaio on 29/08/2017.
 */

@PrepareForTest({ImmunizationLibrary.class})
public class VaccinatorUtilsTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private Context context;

    @Mock
    private CommonRepository commonRepository;
    @Mock
    private VaccinatorUtils vaccinatorUtils;
    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);

    }

    @Test
    public void assertgetwastedcallssqlmethodonce() {
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().commonrepository(org.mockito.ArgumentMatchers.anyString())).thenReturn(commonRepository);
        VaccinatorUtils.getWasted("", "", "");
        VaccinatorUtils.getWasted("", "", "","");
        Mockito.verify(commonRepository, Mockito.times(1)).rawQuery(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    public void assertgetUsedcallssqlmethodonce() {
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().commonrepository(org.mockito.ArgumentMatchers.anyString())).thenReturn(commonRepository);
        VaccinatorUtils.getUsed("", "", "", new String[]{"", ""});
        Mockito.verify(commonRepository, Mockito.times(1)).rawQuery(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    @Config(packageName = "org.smartregister.immunization")
    public void assertVaccinatorUtilsTest(){
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().commonrepository(org.mockito.ArgumentMatchers.anyString())).thenReturn(commonRepository);
//        Activity activity = Robolectric.setupActivity(Activity.class);
//        android.content.Context ctx = RuntimeEnvironment.application;
        android.content.Context ctx = Mockito.mock(android.content.Context.class);
        Resources resources = Mockito.mock(Resources.class);
        PowerMockito.when(ctx.getResources()).thenReturn(resources);
        PowerMockito.when(resources.getColor(anyInt())).thenReturn(255);
//        PowerMockito.when(ctx.getResources().getColor(R.color.alert_normal)).thenReturn(255);
//        PowerMockito.when(ctx.getResources().getColor(R.color.alert_urgent)).thenReturn(255);
//        PowerMockito.when(ctx.getResources().getColor(R.color.alert_na)).thenReturn(255);
        Assert.assertNotNull(vaccinatorUtils.getColorValue(ctx, AlertStatus.upcoming));
        Assert.assertNotNull(vaccinatorUtils.getColorValue(ctx, AlertStatus.normal));
        Assert.assertNotNull(vaccinatorUtils.getColorValue(ctx, AlertStatus.urgent));
        Assert.assertNotNull(vaccinatorUtils.getColorValue(ctx, AlertStatus.expired));
    }

}
