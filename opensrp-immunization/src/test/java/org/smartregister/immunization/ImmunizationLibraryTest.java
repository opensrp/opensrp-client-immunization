package org.smartregister.immunization;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.repository.Repository;

/**
 * Created by onaio on 30/08/2017.
 */

public class ImmunizationLibraryTest extends BaseUnitTest {

    @Before
    public void setUp() {
        Repository repository = Mockito.mock(Repository.class);
        Context context = Mockito.mock(Context.class);
        CommonFtsObject commonFtsObject = Mockito.mock(CommonFtsObject.class);
        ImmunizationLibrary.init(context, repository, commonFtsObject,0,0);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        junit.framework.Assert.assertNotNull(ImmunizationLibrary.getInstance());
    }

    @Test
    public void assertRepositoryReturnsNonNUllObject() {
        junit.framework.Assert.assertNotNull(ImmunizationLibrary.getInstance().getRepository());
    }

    @Test
    public void assertRecurringServiceRecordRepositoryReturnsNonNUllObject() {
        junit.framework.Assert.assertNotNull(ImmunizationLibrary.getInstance().recurringServiceRecordRepository());
    }

    @Test
    public void assertRecurringServiceTypeRepositoryReturnsNonNUllObject() {
        junit.framework.Assert.assertNotNull(ImmunizationLibrary.getInstance().recurringServiceTypeRepository());
    }

    @Test
    public void assertEventClientRepositoryReturnsNonNUllObject() {
        junit.framework.Assert.assertNotNull(ImmunizationLibrary.getInstance().eventClientRepository());
    }

    @Test
    public void assertVaccineRepositoryReturnsNonNUllObject() {
        junit.framework.Assert.assertNotNull(ImmunizationLibrary.getInstance().vaccineRepository());
    }

    @Test
    public void assertVaccineNameRepositoryReturnsNonNUllObject() {
        junit.framework.Assert.assertNotNull(ImmunizationLibrary.getInstance().vaccineNameRepository());
    }

    @Test
    public void assertVaccineTypeRepositoryReturnsNonNUllObject() {
        junit.framework.Assert.assertNotNull(ImmunizationLibrary.getInstance().vaccineTypeRepository());
    }

    @Test
    public void assertContextReturnsNonNUllObject() {
        junit.framework.Assert.assertNotNull(ImmunizationLibrary.getInstance().context());
    }

}
