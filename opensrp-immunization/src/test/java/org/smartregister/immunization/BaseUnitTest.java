package org.smartregister.immunization;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.smartregister.immunization.customshadows.CheckBoxShadow;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.customshadows.RadioButtonShadow;

/**
 * Created by onaio on 29/08/2017.
 */

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {FontTextViewShadow.class , RadioButtonShadow.class, CheckBoxShadow.class})
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public abstract class BaseUnitTest {

}
