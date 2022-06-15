package org.smartregister.immunization.utils;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
public class IMDatabaseUtilsTest extends BaseUnitTest {
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
        Mockito.mockStatic(VaccinatorUtils.class);
        Mockito.when(VaccinatorUtils.getSupportedRecurringServices(context)).thenReturn(ServiceData.recurringservice);
        IMDatabaseUtils.populateRecurringServices(context, null, recurringServiceTypeRepository);
        org.junit.Assert.assertNotNull(imDatabaseUtils);
    }

    @Test
    public void accessAssetsAndFillDataBaseForVaccineTypesTest() throws Exception {

        Mockito.mockStatic(Utils.class);
        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(immunizationLibrary.vaccineTypeRepository()).thenReturn(vaccineTypeRepository);
        Mockito.when(vaccineTypeRepository.getAllVaccineTypes(null)).thenReturn(new ArrayList<VaccineType>());
        Mockito.when(Utils.readAssetContents(org.mockito.ArgumentMatchers.any(Context.class),
                org.mockito.ArgumentMatchers.any(String.class))).thenReturn(VaccineData.vaccine_type);
        IMDatabaseUtils.accessAssetsAndFillDataBaseForVaccineTypes(context, null);
        org.junit.Assert.assertNotNull(imDatabaseUtils);
    }

}
