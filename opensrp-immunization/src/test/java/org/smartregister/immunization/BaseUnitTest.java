package org.smartregister.immunization;

import android.os.Build;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.smartregister.immunization.customshadows.FontTextViewShadow;

/**
 * Created by onaio on 29/08/2017.
 */

@RunWith (PowerMockRunner.class)
@PowerMockRunnerDelegate (RobolectricTestRunner.class)
@Config (constants = BuildConfig.class, shadows = {FontTextViewShadow.class}, sdk = Build.VERSION_CODES.O_MR1)
@PowerMockIgnore ({"org.mockito.*", "org.robolectric.*", "android.*"})
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
}
