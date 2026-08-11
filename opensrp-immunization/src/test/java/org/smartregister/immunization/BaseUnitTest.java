package org.smartregister.immunization;

import androidx.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;

/**
 * Created by onaio on 29/08/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {FontTextViewShadow.class})
public abstract class BaseUnitTest {
    public static final String BASEENTITYID = "baseEntityId";
    public static final String LOCATIONID = "locationID";
    public static final String SYNCED = "synced";
    public static final String EVENTID = "eventID";
    public static final String PROGRAMCLIENTID = "programClientID";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String ANMID = "anmId";
    public static final String FORMSUBMISSIONID = "formSubmissionId";
    public static final String VALUE = "value";

    private AutoCloseable closeableMocks;

    @Before
    public void baseSetup() {
        closeableMocks = MockitoAnnotations.openMocks(this);
        ImmunizationLibrary.destroy();
    }

    @After
    public void baseTearDown() throws Exception {
        if (closeableMocks != null) {
            closeableMocks.close();
        }
        ImmunizationLibrary.destroy();
    }

    public void mockImmunizationLibrary(@NonNull ImmunizationLibrary immunizationLibrary,
                                        @NonNull Context context,
                                        @NonNull VaccineRepository vaccineRepository,
                                        @NonNull AlertService alertService,
                                        @NonNull AppProperties appProperties) {
        ReflectionHelpers.setStaticField(ImmunizationLibrary.class, "instance", immunizationLibrary);
        Mockito.when(immunizationLibrary.context()).thenReturn(context);
        Mockito.when(immunizationLibrary.vaccineRepository()).thenReturn(vaccineRepository);
        Mockito.when(immunizationLibrary.getVaccines(IMConstants.VACCINE_TYPE.CHILD)).thenReturn(VaccineRepo.Vaccine.values());
        Mockito.when(immunizationLibrary.getVaccines(IMConstants.VACCINE_TYPE.WOMAN)).thenReturn(VaccineRepo.Vaccine.values());
        Mockito.when(immunizationLibrary.vaccineRepository().findByEntityId(ArgumentMatchers.anyString())).thenReturn(null);
        Mockito.when(immunizationLibrary.context().alertService()).thenReturn(alertService);
        Mockito.when(immunizationLibrary.getProperties()).thenReturn(appProperties);
    }
}
