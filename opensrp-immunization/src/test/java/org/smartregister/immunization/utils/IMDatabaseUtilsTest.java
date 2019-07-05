package org.smartregister.immunization.utils;

import android.content.Context;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceData;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineType;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineTypeRepository;
import org.smartregister.immunization.util.IMDatabaseUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.util.Utils;

import java.util.ArrayList;

/**
 * Created by real on 29/10/17.
 */
@PrepareForTest ({VaccinatorUtils.class, ImmunizationLibrary.class, Utils.class})
public class IMDatabaseUtilsTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();
    @InjectMocks
    private IMDatabaseUtils imDatabaseUtils;
    @Mock
    private Context context;
    @Mock
    private ImmunizationLibrary immunizationLibrary;
    @Mock
    private VaccineTypeRepository vaccineTypeRepository;
    @Mock
    private RecurringServiceTypeRepository recurringServiceTypeRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        org.junit.Assert.assertNotNull(imDatabaseUtils);
    }

    @Test
    public void assertPopulateRecurringServices() throws Exception {
        PowerMockito.mockStatic(VaccinatorUtils.class);
        PowerMockito.when(VaccinatorUtils.getSupportedRecurringServices(context)).thenReturn(ServiceData.recurringservice);
        IMDatabaseUtils.populateRecurringServices(context, null, recurringServiceTypeRepository);
        org.junit.Assert.assertNotNull(imDatabaseUtils);
    }

    @Test
    public void accessAssetsAndFillDataBaseForVaccineTypesTest() throws Exception {

        PowerMockito.mockStatic(Utils.class);
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.vaccineTypeRepository()).thenReturn(vaccineTypeRepository);
        PowerMockito.when(vaccineTypeRepository.getAllVaccineTypes(null)).thenReturn(new ArrayList<VaccineType>());
        PowerMockito.when(Utils.readAssetContents(org.mockito.ArgumentMatchers.any(Context.class),
                org.mockito.ArgumentMatchers.any(String.class))).thenReturn(VaccineData.vaccine_type);
        IMDatabaseUtils.accessAssetsAndFillDataBaseForVaccineTypes(context, null);
        org.junit.Assert.assertNotNull(imDatabaseUtils);
    }

}
