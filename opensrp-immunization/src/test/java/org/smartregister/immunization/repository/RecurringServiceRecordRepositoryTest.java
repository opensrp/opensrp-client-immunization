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
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.repository.Repository;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by onaio on 29/08/2017.
 */

public class RecurringServiceRecordRepositoryTest extends BaseUnitTest {

    @InjectMocks
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Mock
    private Repository repository;

    @Mock
    private SQLiteDatabase sqliteDatabase;

    @Before
    public void setUp() {
        initMocks(this);
        assertNotNull(recurringServiceRecordRepository);
    }


    @Test
    public void instantiatesSuccessfullyOnConstructorCall() throws Exception {
        RecurringServiceRecordRepository vaccineNameRepository = new RecurringServiceRecordRepository(repository);
        assertNotNull(vaccineNameRepository);
    }

    @Test
    public void createTableCallsExecuteSQLMethod() throws Exception {
        recurringServiceRecordRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(6)).execSQL(anyString());
    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() throws Exception {
        when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.add(new ServiceRecord());
        Mockito.verify(sqliteDatabase, Mockito.times(1)).insert(anyString(),isNull(String.class), any(ContentValues.class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() throws Exception {
        when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setId(0l);
        recurringServiceRecordRepository.add(serviceRecord);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(anyString(), any(ContentValues.class), anyString(), any(String [].class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() throws Exception {
        recurringServiceRecordRepository.add(null);
        Mockito.verify(sqliteDatabase, Mockito.times(0)).insert(anyString(), (String)isNull(), any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0)).update(anyString(), any(ContentValues.class), anyString(), any(String [].class));
    }

    @Test
    public void findbyEntityIDcallsDatabaseQueryMethod1Times() throws Exception {
        RecurringServiceRecordRepository recurringServiceRecordRepositoryspy = PowerMockito.spy(recurringServiceRecordRepository);
        Cursor cursor = PowerMockito.mock(Cursor.class);
        when(sqliteDatabase.rawQuery(anyString(), any(String[].class))).thenReturn(cursor);
        when(recurringServiceRecordRepositoryspy.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepositoryspy.findByEntityId("entityID");
        Mockito.verify(sqliteDatabase, Mockito.times(1)).rawQuery(anyString(), any(String [].class));
    }

    @Test
    public void findbyUniqueIDcallsDatabaseQueryMethod1Times() throws Exception {
        Cursor cursor = PowerMockito.mock(Cursor.class);
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setFormSubmissionId("formsubmissionID");
        serviceRecord.setEventId("EventID");
        when(sqliteDatabase.query(anyString(), any(String[].class), anyString(), any(String[].class), isNull(String.class), isNull(String.class), anyString(), isNull(String.class))).thenReturn(cursor);
        when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.findUnique(sqliteDatabase, serviceRecord);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(anyString(), any(String[].class), anyString(), any(String[].class), isNull(String.class), isNull(String.class), anyString(), isNull(String.class));
    }

    @Test
    public void findbyCaseIDcallsDatabaseQueryMethod1Times() throws Exception {
        Cursor cursor = PowerMockito.mock(Cursor.class);
        when(sqliteDatabase.query(anyString(), any(String[].class), anyString(), any(String[].class), isNull(String.class), isNull(String.class), isNull(String.class), isNull(String.class))).thenReturn(cursor);
        when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.find(0l);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(anyString(), any(String[].class), anyString(), any(String[].class), isNull(String.class), isNull(String.class), isNull(String.class), isNull(String.class));
    }


    @Test
    public void updateCallsDatabaseUpdateMethod1Times() throws Exception {
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setId(0l);
        recurringServiceRecordRepository.update(sqliteDatabase,serviceRecord);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(anyString(), any(ContentValues.class), anyString(), any(String [].class));
    }

    @Test
    public void deletecallsDatabaseDeleteMethod1Times() throws Exception {
        RecurringServiceRecordRepository recurringServiceRecordRepositoryspy = Mockito.spy(recurringServiceRecordRepository);
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setBaseEntityId("baseEntityID");
        serviceRecord.setName("name");
        serviceRecord.setFormSubmissionId("formsubmissionID");
        serviceRecord.setEventId("EventID");
        Mockito.doReturn(serviceRecord).when(recurringServiceRecordRepositoryspy).find(0l);
        when(recurringServiceRecordRepositoryspy.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepositoryspy.deleteServiceRecord(0l);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).delete(anyString(), anyString(), any(String[].class));
    }

    @Test
    public void closeMethodCallsInternalMethodsWithCorrectParams() throws Exception {
        when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.close(5l);
        Mockito.verify(recurringServiceRecordRepository.getWritableDatabase(), Mockito.times(1)).update(eq(recurringServiceRecordRepository.TABLE_NAME), (ContentValues)any(), anyString(), eq(new String[]{"5"}));
    }

    @Test
    public void closeMethodFailsSilentlyWithNullParams() throws Exception {
        when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.close(null);
        Mockito.verify(recurringServiceRecordRepository.getWritableDatabase(), Mockito.times(0)).update(anyString(), (ContentValues)any(), anyString(), eq(new String[]{"5"}));
    }

    @Test
    public void removeHyphenMethodRemoveHyphenFromString() throws Exception {
        String testString = recurringServiceRecordRepository.removeHyphen("test_string");
        assertNotNull(testString);
        assertFalse(testString.contains("-"));
    }



}