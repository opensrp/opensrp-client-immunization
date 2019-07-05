package org.smartregister.immunization;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by real on 05/11/17.
 */

public class CustomRoboelectricTestRunner extends RobolectricTestRunner {
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file and res
     * directory by default. Use the {@link Config} annotation to configure.
     *
     * @param testClass
     *         the test class to be run
     * @throws InitializationError
     *         if junit says so
     */
    public CustomRoboelectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }
}
