package org.smartregister.immunization.utils;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.repository.AllSettings;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by onaio on 29/08/2017.
 */

@PrepareForTest({ImmunizationLibrary.class})
public class VaccinatorUtilsTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private Context context;

    @Mock
    private CommonRepository commonRepository;

    @Mock
    private AllSettings allSettings;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void assertgetwastedcallssqlmethodonce(){
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().commonrepository(anyString())).thenReturn(commonRepository);
        VaccinatorUtils.getWasted("", "", "");
        Mockito.verify(commonRepository, Mockito.times(1)).rawQuery(anyString());
    }

    @Test
    public void assertgetUsedcallssqlmethodonce(){
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().commonrepository(anyString())).thenReturn(commonRepository);
        VaccinatorUtils.getUsed("", "", "",new String[]{"", ""});
        Mockito.verify(commonRepository, Mockito.times(1)).rawQuery(anyString());
    }





}