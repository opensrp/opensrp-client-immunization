package org.smartregister.immunization;

import android.os.Build;
import android.support.annotation.NonNull;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.smartregister.Context;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;

/**
 * Created by onaio on 29/08/2017.
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {FontTextViewShadow.class}, sdk = Build.VERSION_CODES.O_MR1)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*", "javax.xml.*", "org.xml.sax.*"
        , "org.w3c.dom.*", "org.springframework.context.*", "org.apache.log4j.*", "com.android.internal.policy.*"
        , "org.xmlpull.v1.*"})
public abstract class BaseUnitTest {
    public final static String BASEENTITYID = "baseEntityId";
    public final static String LOCATIONID = "locationID";
    public final static String SYNCED = "synced";
    public final static String EVENTID = "eventID";
    public final static String PROGRAMCLIENTID = "programClientID";
    public final static String NAME = "name";
    public final static String TYPE = "type";
    public static final String ANMID = "anmId";
    public static final String FORMSUBMISSIONID = "formSubmissionId";
    public static final String VALUE = "value";

    public void mockImmunizationLibrary(@NonNull ImmunizationLibrary immunizationLibrary, @NonNull Context context, @NonNull VaccineRepository vaccineRepository, @NonNull AlertService alertService, @NonNull AppProperties appProperties) {
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().vaccineRepository()).thenReturn(vaccineRepository);
        PowerMockito.when(ImmunizationLibrary.getInstance().vaccineRepository()
                .findByEntityId(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().alertService()).thenReturn(alertService);
        PowerMockito.when(ImmunizationLibrary.getInstance().getProperties()).thenReturn(appProperties);
    }
}
