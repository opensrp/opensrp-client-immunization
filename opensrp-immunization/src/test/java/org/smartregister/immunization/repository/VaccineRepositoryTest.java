package org.smartregister.immunization.repository;


import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by onaio on 29/08/2017.
 */

public class VaccineRepositoryTest extends BaseUnitTest {

    @InjectMocks
    VaccineRepository vaccineRepository;

    @Mock
    Repository repository;

    @Mock
    CommonFtsObject commonFtsObject;

    @Mock
    AlertService alertService;

    @Mock
    ImmunizationLibrary immunizationLibrary;

    @Mock
    Context context;

    @Mock
    SQLiteDatabase sqliteDatabase;

    @Before
    public void setUp() {

        initMocks(this);
        assertNotNull(vaccineRepository);
    }

    @Test
    public void addHyphenMethodAddsUnderscoreToString() throws Exception {
        String testString = vaccineRepository.addHyphen("test string");
        assertNotNull(testString);
        assertTrue(testString.contains("_"));
    }

    @Test
    public void removeHyphenMethodRemoveHyphenFromString() throws Exception {

        String testString = vaccineRepository.removeHyphen("test_string");
        assertNotNull(testString);
        assertFalse(testString.contains("-"));
    }

    @Test
    public void addHyphenMethodWithBlankParamReturnsBlankString() throws Exception {
        String testString = vaccineRepository.addHyphen("");
        assertNotNull(testString);
        assertTrue(testString.isEmpty());
    }

    @Test
    public void removeHyphenMethodWithBlankParamReturnsBlankString() throws Exception {
        String testString = vaccineRepository.removeHyphen("");
        assertNotNull(testString);
        assertTrue(testString.isEmpty());
    }

    @Test
    public void alertServiceDoesNotReturnNull() throws Exception {
        assertNotNull(vaccineRepository.alertService());
    }

    @Test
    public void instantiatesSuccessfullyOnConstructorCall() throws Exception {

        VaccineRepository vaccineRepository = new VaccineRepository(repository, commonFtsObject, alertService);
        assertNotNull(vaccineRepository);
    }

    @Test
    public void createTableCallsExecuteSQLMethod3Times() throws Exception {
        vaccineRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(3)).execSQL(anyString());

    }

    @Test
    public void closeMethodCallsInternalMethodsWithCorrectParams() throws Exception {

        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.close(5l);
        Mockito.verify(vaccineRepository.getWritableDatabase(), Mockito.times(1)).update(eq(VaccineRepository.VACCINE_TABLE_NAME), (ContentValues) any(), anyString(), eq(new String[]{"5"}));

    }

    @Test
    public void closeMethodFailsSilentlyWithNullParams() throws Exception {

        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.close(null);
        Mockito.verify(vaccineRepository.getWritableDatabase(), Mockito.times(0)).update(anyString(), (ContentValues) any(), anyString(), eq(new String[]{"5"}));

    }

}