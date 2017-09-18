package org.smartregister.immunization;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.service.intent.RecurringIntentService;
import org.smartregister.immunization.view.ImmunizationRowCard;
import org.smartregister.immunization.view.ImmunizationRowGroup;
import org.smartregister.repository.Repository;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by onaio on 30/08/2017.
 */


public class ImmunizationLibraryTest extends BaseUnitTest {

    @Before
    public void setUp() {
        Repository repository = Mockito.mock(Repository.class);
        Context context = Mockito.mock(Context.class);
        CommonFtsObject commonFtsObject = Mockito.mock(CommonFtsObject.class);
        ImmunizationLibrary.init(context,repository,commonFtsObject);
        ImmunizationLibrary immunizationLibrary = ImmunizationLibrary.getInstance();
        initMocks(this);
    }

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        assertNotNull(ImmunizationLibrary.getInstance());
    }

    @Test
    public void assertRepositoryReturnsNonNUllObject() {
        assertNotNull(ImmunizationLibrary.getInstance().getRepository());
    }

    @Test
    public void assertRecurringServiceRecordRepositoryReturnsNonNUllObject() {
        assertNotNull(ImmunizationLibrary.getInstance().recurringServiceRecordRepository());
    }

    @Test
    public void assertRecurringServiceTypeRepositoryReturnsNonNUllObject() {
        assertNotNull(ImmunizationLibrary.getInstance().recurringServiceTypeRepository());
    }

    @Test
    public void assertEventClientRepositoryReturnsNonNUllObject() {
        assertNotNull(ImmunizationLibrary.getInstance().eventClientRepository());
    }

    @Test
    public void assertVaccineRepositoryReturnsNonNUllObject() {
        assertNotNull(ImmunizationLibrary.getInstance().vaccineRepository());
    }

    @Test
    public void assertVaccineNameRepositoryReturnsNonNUllObject() {
        assertNotNull(ImmunizationLibrary.getInstance().vaccineNameRepository());
    }

    @Test
    public void assertVaccineTypeRepositoryReturnsNonNUllObject() {
        assertNotNull(ImmunizationLibrary.getInstance().vaccineTypeRepository());
    }

    @Test
    public void assertContextReturnsNonNUllObject() {
        assertNotNull(ImmunizationLibrary.getInstance().context());
    }

}
