package org.smartregister.immunization.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
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
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.BuildConfig;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.VaccineSchedule;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.IMDatabaseUtils;
import org.smartregister.immunization.util.JsonFormUtils;
import org.smartregister.immunization.util.RecurringServiceUtils;
import org.smartregister.immunization.util.VaccinateActionUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.immunization.util.VaccineScheduleUtils;
import org.smartregister.repository.Repository;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
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
    public void addStatusTagTest() throws Exception {

//        Activity activity = new Activity();
//        TableLayout tableLayout = Mockito.spy(new TableLayout(activity));


//        PowerMockito.doNothing().when(activity).vaccinatorUtilsTest(vaccinatorUtils);
//        VaccinatorUtils.addStatusTag(activity,new TableLayout(activity),"",true);
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
    public void assertVaccinatorUtilsTest() throws Exception{
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().commonrepository(org.mockito.ArgumentMatchers.anyString())).thenReturn(commonRepository);

        android.content.Context ctx = mock(android.content.Context.class);
        Resources resources = mock(Resources.class);
        PowerMockito.when(ctx.getResources()).thenReturn(resources);
        PowerMockito.when(resources.getColor(anyInt())).thenReturn(255);
        //get color test for different status
        Assert.assertNotNull(vaccinatorUtils.getColorValue(ctx, AlertStatus.upcoming));
        Assert.assertNotNull(vaccinatorUtils.getColorValue(ctx, AlertStatus.normal));
        Assert.assertNotNull(vaccinatorUtils.getColorValue(ctx, AlertStatus.urgent));
        Assert.assertNotNull(vaccinatorUtils.getColorValue(ctx, AlertStatus.expired));

        //get getServiceExpiryDate null test

        Assert.assertNull(vaccinatorUtils.getServiceExpiryDate(null,null));
        Assert.assertNotNull(new IMConstants());
        Assert.assertNotNull(new VaccineScheduleUtils());
        Assert.assertNotNull(new JsonFormUtils());
        Assert.assertNotNull(new RecurringServiceUtils());
        Assert.assertNotNull(new IMDatabaseUtils());
        Assert.assertNotNull(new VaccinateActionUtils());
        Assert.assertNotNull(new VaccinatorUtils());
        Assert.assertEquals(IMConstants.VACCINE_SYNC_TIME, BuildConfig.VACCINE_SYNC_TIME);

//        VaccinatorUtils powerspy = PowerMockito.spy(vaccinatorUtils);
//        PowerMockito.doReturn(new HashMap<String,Object>()).when(VaccinatorUtils.class,"createVaccineMap",mock(String.class),mock(Alert.class),mock(DateTime.class),mock(ServiceType.class));
    }

}
