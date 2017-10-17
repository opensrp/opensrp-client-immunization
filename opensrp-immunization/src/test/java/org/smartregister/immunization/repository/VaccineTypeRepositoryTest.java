package org.smartregister.immunization.repository;

import android.content.ContentValues;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.VaccineType;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;

/**
 * Created by onaio on 29/08/2017.
 */

public class VaccineTypeRepositoryTest extends BaseUnitTest {

    @InjectMocks
    private VaccineTypeRepository vaccineTypeRepository;

    @Mock
    private Repository repository;

    @Mock
    private CommonFtsObject commonFtsObject;

    @Mock
    private AlertService alertService;

    @Mock
    private SQLiteDatabase sqliteDatabase;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
        org.junit.Assert.assertNotNull(vaccineTypeRepository);
    }

    @Test
    public void instantiatesSuccessfullyOnConstructorCall() throws Exception {
        VaccineTypeRepository vaccineRepository = new VaccineTypeRepository(repository, commonFtsObject, alertService);
        org.junit.Assert.assertNotNull(vaccineRepository);
    }

    @Test
    public void createTableCallsExecuteSQLMethod() throws Exception {
        vaccineTypeRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).execSQL(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() throws Exception {
        Mockito.when(vaccineTypeRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineType vaccineType = PowerMockito.mock(VaccineType.class);
        Mockito.when(vaccineType.getId()).thenReturn(null);
        vaccineTypeRepository.add(vaccineType, sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).insert(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.any(ContentValues.class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() throws Exception {
        Mockito.when(vaccineTypeRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineType vaccineType = new VaccineType(0l, 0, "", "", "", "");
        vaccineTypeRepository.add(vaccineType, sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() throws Exception {
        vaccineTypeRepository.add(null, sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(0)).insert(org.mockito.ArgumentMatchers.anyString(), (String) org.mockito.ArgumentMatchers.isNull(), org.mockito.ArgumentMatchers.any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void findbyEntityIDcallsDatabaseQueryMethod1Times() throws Exception {
        Cursor cursor = PowerMockito.mock(Cursor.class);
        Mockito.when(sqliteDatabase.query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(vaccineTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineTypeRepository.findIDByName("Name");
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class));
    }

    @Test
    public void findallVaccineTypcallsDatabaseQueryMethod1Times() throws Exception {
        Cursor cursor = PowerMockito.mock(Cursor.class);
        Mockito.when(sqliteDatabase.query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(vaccineTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineTypeRepository.getAllVaccineTypes(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class));
    }

}
