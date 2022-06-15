package org.smartregister.immunization;

import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.smartregister.Context;
import org.smartregister.CoreLibrary;
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
@Config(shadows = {FontTextViewShadow.class,}, application = TestApplication.class, sdk = Build.VERSION_CODES.S)
public abstract class BaseUnitTest {
    public final static String BASEENTITYID = "baseEntityId";
    public final static String LOCATIONID = "locationID";
    public final static String SYNCED = "synced";
    public final static String EVENTID = "eventID";
    public final static String PROGRAMCLIENTID = "programClientID";
    public final static String NAME = "name";
    public final static String TYPE = "type";
    public static final String ANMID = "anmId";
    public static final String FORMSUBMISSIONID = "formSubmissionId";
    public static final String VALUE = "value";
    @Mock
    protected ImmunizationLibrary immunizationLibrary;
    @Mock
    protected VaccineRepository vaccineRepository;
    @Mock
    protected Context context;
    @Mock
    protected AlertService alertService;
    @Mock
    protected AppProperties appProperties;
    private AutoCloseable autoCloseable;

    @Before
    public void setUpSuper() {
        try {
            autoCloseable = MockitoAnnotations.openMocks(this);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        Mockito.doReturn(context).when(immunizationLibrary).context();
        Mockito.doReturn(vaccineRepository).when(immunizationLibrary).vaccineRepository();
        Mockito.doReturn(null).when(vaccineRepository).findByEntityId(org.mockito.ArgumentMatchers.anyString());
        Mockito.doReturn(VaccineRepo.Vaccine.values()).when(immunizationLibrary).getVaccines(IMConstants.VACCINE_TYPE.CHILD);
        Mockito.doReturn(VaccineRepo.Vaccine.values()).when(immunizationLibrary).getVaccines(IMConstants.VACCINE_TYPE.WOMAN);
        Mockito.doReturn(alertService).when(context).alertService();
        Mockito.doReturn(appProperties).when(immunizationLibrary).getProperties();
    }

    @After
    public void tearDownSuper() throws Exception {
        if (autoCloseable != null)
            autoCloseable.close();
        Context.destroyInstance();
        CoreLibrary.destroyInstance();
        try {
            Mockito.validateMockitoUsage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
