package org.smartregister.immunization;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.repository.Repository;
import org.smartregister.util.AppProperties;

/**
 * Created by onaio on 30/08/2017.
 */

public class ImmunizationLibraryTest extends BaseUnitTest {

    @Before
    public void setUp() {
        Repository repository = Mockito.mock(Repository.class);
        Context context = Mockito.mock(Context.class);
        AppProperties properties = Mockito.mock(AppProperties.class);
        Mockito.doReturn(properties).when(context).getAppProperties();
        CommonFtsObject commonFtsObject = Mockito.mock(CommonFtsObject.class);
        ImmunizationLibrary.init(context, repository, commonFtsObject, 0, 0);
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        Assert.assertNotNull(ImmunizationLibrary.getInstance());
    }

    @Test
    public void assertRepositoryReturnsNonNUllObject() {
        Assert.assertNotNull(ImmunizationLibrary.getInstance().getRepository());
    }

    @Test
    public void assertRecurringServiceRecordRepositoryReturnsNonNUllObject() {
        Assert.assertNotNull(ImmunizationLibrary.getInstance().recurringServiceRecordRepository());
    }

    @Test
    public void assertRecurringServiceTypeRepositoryReturnsNonNUllObject() {
        Assert.assertNotNull(ImmunizationLibrary.getInstance().recurringServiceTypeRepository());
    }

    @Test
    public void assertEventClientRepositoryReturnsNonNUllObject() {
        Assert.assertNotNull(ImmunizationLibrary.getInstance().eventClientRepository());
    }

    @Test
    public void assertVaccineRepositoryReturnsNonNUllObject() {
        Assert.assertNotNull(ImmunizationLibrary.getInstance().vaccineRepository());
    }

    @Test
    public void assertVaccineNameRepositoryReturnsNonNUllObject() {
        Assert.assertNotNull(ImmunizationLibrary.getInstance().vaccineNameRepository());
    }

    @Test
    public void assertVaccineTypeRepositoryReturnsNonNUllObject() {
        Assert.assertNotNull(ImmunizationLibrary.getInstance().vaccineTypeRepository());
    }

    @Test
    public void assertContextReturnsNonNUllObject() {
        Assert.assertNotNull(ImmunizationLibrary.getInstance().context());
    }

}
