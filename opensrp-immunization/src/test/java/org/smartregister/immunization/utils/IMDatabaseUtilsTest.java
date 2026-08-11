package org.smartregister.immunization.utils;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.ServiceData;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineType;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.repository.VaccineTypeRepository;
import org.smartregister.immunization.util.IMDatabaseUtils;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;
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

    @Mock
    private org.smartregister.Context drishtiContext;

    @Mock
    private VaccineRepository vaccineRepository;

    @Mock
    private AlertService alertService;

    @Mock
    private AppProperties appProperties;

    @Before
    public void setUp() {
        mockImmunizationLibrary(immunizationLibrary, drishtiContext, vaccineRepository, alertService, appProperties);
        org.junit.Assert.assertNotNull(imDatabaseUtils);
    }

    @Test
    public void assertPopulateRecurringServices() throws Exception {
        try (MockedStatic<VaccinatorUtils> vaccinatorUtils = Mockito.mockStatic(VaccinatorUtils.class)) {
            vaccinatorUtils.when(() -> VaccinatorUtils.getSupportedRecurringServices(context)).thenReturn(ServiceData.recurringservice);
            IMDatabaseUtils.populateRecurringServices(context, null, recurringServiceTypeRepository);
            org.junit.Assert.assertNotNull(imDatabaseUtils);
        }
    }

    @Test
    public void accessAssetsAndFillDataBaseForVaccineTypesTest() throws Exception {
        try (MockedStatic<Utils> utils = Mockito.mockStatic(Utils.class)) {
            Mockito.when(immunizationLibrary.vaccineTypeRepository()).thenReturn(vaccineTypeRepository);
            Mockito.when(vaccineTypeRepository.getAllVaccineTypes(null)).thenReturn(new ArrayList<>());
            utils.when(() -> Utils.readAssetContents(ArgumentMatchers.any(Context.class),
                    ArgumentMatchers.any(String.class))).thenReturn(VaccineData.vaccine_type);
            IMDatabaseUtils.accessAssetsAndFillDataBaseForVaccineTypes(context, null);
            org.junit.Assert.assertNotNull(imDatabaseUtils);
        }
    }
}
