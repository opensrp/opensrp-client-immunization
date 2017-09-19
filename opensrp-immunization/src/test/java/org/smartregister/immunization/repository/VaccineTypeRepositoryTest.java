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
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.VaccineType;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

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
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private Context context;

    @Mock
    private SQLiteDatabase sqliteDatabase;

    @Before
    public void setUp() {

        initMocks(this);
        assertNotNull(vaccineTypeRepository);
    }


    @Test
    public void instantiatesSuccessfullyOnConstructorCall() throws Exception {
        VaccineTypeRepository vaccineRepository = new VaccineTypeRepository(repository, commonFtsObject, alertService);
        assertNotNull(vaccineRepository);
    }

    @Test
    public void createTableCallsExecuteSQLMethod() throws Exception {
        vaccineTypeRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).execSQL(anyString());

    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() throws Exception {
        when(vaccineTypeRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineType vaccineType = PowerMockito.mock(VaccineType.class);
        when(vaccineType.getId()).thenReturn(null);
        vaccineTypeRepository.add(vaccineType, sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).insert(anyString(), isNull(String.class), any(ContentValues.class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() throws Exception {
        when(vaccineTypeRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineType vaccineType = new VaccineType(0l, 0, "", "" , "", "");
        vaccineTypeRepository.add(vaccineType,sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(anyString(), any(ContentValues.class), anyString(), any(String [].class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() throws Exception {
        vaccineTypeRepository.add(null,sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(0)).insert(anyString(), (String)isNull(), any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0)).update(anyString(), any(ContentValues.class), anyString(), any(String [].class));
    }

    @Test
    public void findbyEntityIDcallsDatabaseQueryMethod1Times() throws Exception {
        Cursor cursor = PowerMockito.mock(Cursor.class);
        when(sqliteDatabase.query(anyString(), any(String[].class), anyString(), any(String[].class), isNull(String.class), isNull(String.class), isNull(String.class), isNull(String.class))).thenReturn(cursor);
        when(vaccineTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineTypeRepository.findIDByName("Name");
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(anyString(), any(String[].class), anyString(), any(String[].class), isNull(String.class), isNull(String.class), isNull(String.class), isNull(String.class));
    }

    @Test
    public void findallVaccineTypcallsDatabaseQueryMethod1Times() throws Exception {
        Cursor cursor = PowerMockito.mock(Cursor.class);
        when(sqliteDatabase.query(anyString(), any(String[].class), isNull(String.class), isNull(String[].class), isNull(String.class), isNull(String.class), isNull(String.class), isNull(String.class))).thenReturn(cursor);
        when(vaccineTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineTypeRepository.getAllVaccineTypes(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(anyString(), any(String[].class), isNull(String.class), isNull(String[].class), isNull(String.class), isNull(String.class), isNull(String.class), isNull(String.class));
    }

}