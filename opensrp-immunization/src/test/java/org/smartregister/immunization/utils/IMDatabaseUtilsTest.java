package org.smartregister.immunization.utils;

import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineType;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.repository.VaccineTypeRepository;
import org.smartregister.immunization.util.IMDatabaseUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

/**
 * Created by real on 26/10/17.
 */
@PrepareForTest({ImmunizationLibrary.class})
public class IMDatabaseUtilsTest extends BaseUnitTest {

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @InjectMocks
    private VaccineTypeRepository vaccineRepository;
    @Mock
    private android.content.Context context;
    @Mock
    IMDatabaseUtils imDatabaseUtils;

    @Rule
    public PowerMockRule rule = new PowerMockRule();



    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void accessAssetsAndFillDataBaseForVaccineTypesTest() {
        List<VaccineType> list = new ArrayList<VaccineType>();
        SQLiteDatabase sqliteDatabase = PowerMockito.mock(SQLiteDatabase.class);
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);

        PowerMockito.when(immunizationLibrary.vaccineTypeRepository()).thenReturn(vaccineRepository);
//        PowerMockito.when(ImmunizationLibrary.getInstance().vaccineTypeRepository().getReadableDatabase()).thenReturn(sqLiteDatabase);
        Cursor cursor = PowerMockito.mock(Cursor.class);
//        Mockito.when(sqliteDatabase.query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
//        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
//        PowerMockito.when(vaccineRepository.getAllVaccineTypes(any(SQLiteDatabase.class))).thenReturn(Mockito.mock(List.class));

//        imDatabaseUtils.accessAssetsAndFillDataBaseForVaccineTypes(context,sqliteDatabase);
    }
}
