package org.smartregister.immunization.service.intent;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

/**
 * Created by onaio on 30/08/2017.
 */

public class RecurringIntentServiceTest extends BaseUnitTest {

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        junit.framework.Assert.assertNotNull(new RecurringIntentService());
    }

}
