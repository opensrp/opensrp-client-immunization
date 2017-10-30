package org.smartregister.immunization.repository;

import android.content.ContentValues;

import junit.framework.Assert;

import net.sqlcipher.Cursor;
import net.sqlcipher.MatrixCursor;
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

/**
 * Created by onaio on 29/08/2017.
 */

public class RecurringServiceRecordRepositoryTest extends BaseUnitTest {

    public static final String EVENTID = "EventID";
    private static final String FORMSUBMISSIONID = "formsubmissionID";

    @InjectMocks
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Mock
    private Repository repository;

    @Mock
    private SQLiteDatabase sqliteDatabase;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
        org.junit.Assert.assertNotNull(recurringServiceRecordRepository);
    }

    @Test
    public void instantiatesSuccessfullyOnConstructorCall() throws Exception {
        RecurringServiceRecordRepository vaccineNameRepository = new RecurringServiceRecordRepository(repository);
        org.junit.Assert.assertNotNull(vaccineNameRepository);
    }

    @Test
    public void createTableCallsExecuteSQLMethod() throws Exception {
        recurringServiceRecordRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(6)).execSQL(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() throws Exception {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.add(new ServiceRecord());
        Mockito.verify(sqliteDatabase, Mockito.times(1)).insert(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.any(ContentValues.class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() throws Exception {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setId(0l);
        recurringServiceRecordRepository.add(serviceRecord);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() throws Exception {
        recurringServiceRecordRepository.add(null);
        Mockito.verify(sqliteDatabase, Mockito.times(0)).insert(org.mockito.ArgumentMatchers.anyString(), (String) org.mockito.ArgumentMatchers.isNull(), org.mockito.ArgumentMatchers.any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void findbyEntityIDcallsDatabaseQueryMethod1Times() throws Exception {
        RecurringServiceRecordRepository recurringServiceRecordRepositoryspy = PowerMockito.spy(recurringServiceRecordRepository);
        String[] columns = new String[] {RecurringServiceRecordRepository.ID_COLUMN,RecurringServiceRecordRepository.BASE_ENTITY_ID,RecurringServiceRecordRepository.PROGRAM_CLIENT_ID,RecurringServiceRecordRepository.RECURRING_SERVICE_ID,RecurringServiceRecordRepository.VALUE,RecurringServiceRecordRepository.DATE,RecurringServiceRecordRepository.ANMID,RecurringServiceRecordRepository.LOCATIONID,RecurringServiceRecordRepository.SYNC_STATUS,RecurringServiceRecordRepository.EVENT_ID,RecurringServiceRecordRepository.FORMSUBMISSION_ID,RecurringServiceRecordRepository.UPDATED_AT_COLUMN,RecurringServiceTypeRepository.TYPE,RecurringServiceTypeRepository.NAME};
        MatrixCursor cursor= new MatrixCursor(columns);
        cursor.addRow(new Object[] {1l,"","",1l,"", 1509347806l,"","","","","",1l,"TYPE","NAME"});
        cursor.addRow(new Object[] {1l,"","",1l,"", 1509347806l,"","","","","",1l,"",""});

        Mockito.when(sqliteDatabase.rawQuery(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class))).thenReturn(cursor);
        Mockito.when(recurringServiceRecordRepositoryspy.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepositoryspy.findByEntityId("entityID");
        Mockito.verify(sqliteDatabase, Mockito.times(1)).rawQuery(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void findbyUniqueIDcallsDatabaseQueryMethod1Times() throws Exception {
        String[] columns = new String[] {RecurringServiceRecordRepository.ID_COLUMN,RecurringServiceRecordRepository.BASE_ENTITY_ID,RecurringServiceRecordRepository.PROGRAM_CLIENT_ID,RecurringServiceRecordRepository.RECURRING_SERVICE_ID,RecurringServiceRecordRepository.VALUE,RecurringServiceRecordRepository.DATE,RecurringServiceRecordRepository.ANMID,RecurringServiceRecordRepository.LOCATIONID,RecurringServiceRecordRepository.SYNC_STATUS,RecurringServiceRecordRepository.EVENT_ID,RecurringServiceRecordRepository.FORMSUBMISSION_ID,RecurringServiceRecordRepository.UPDATED_AT_COLUMN,RecurringServiceTypeRepository.TYPE,RecurringServiceTypeRepository.NAME};
        MatrixCursor cursor= new MatrixCursor(columns);
        cursor.addRow(new Object[] {1l,"","",1l,"", 1509347806l,"","","","","",1l,"TYPE","NAME"});

        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setFormSubmissionId(FORMSUBMISSIONID);
        serviceRecord.setEventId(EVENTID);
        recurringServiceRecordRepository.findUnique(null, serviceRecord);
        Mockito.when(sqliteDatabase.query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.findUnique(sqliteDatabase, serviceRecord);

        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class));

        recurringServiceRecordRepository.findUnique(sqliteDatabase, null);
    }

    @Test
    public void findbyCaseIDcallsDatabaseQueryMethod1Times() throws Exception {
        String[] columns = new String[] {RecurringServiceRecordRepository.ID_COLUMN,RecurringServiceRecordRepository.BASE_ENTITY_ID,RecurringServiceRecordRepository.PROGRAM_CLIENT_ID,RecurringServiceRecordRepository.RECURRING_SERVICE_ID,RecurringServiceRecordRepository.VALUE,RecurringServiceRecordRepository.DATE,RecurringServiceRecordRepository.ANMID,RecurringServiceRecordRepository.LOCATIONID,RecurringServiceRecordRepository.SYNC_STATUS,RecurringServiceRecordRepository.EVENT_ID,RecurringServiceRecordRepository.FORMSUBMISSION_ID,RecurringServiceRecordRepository.UPDATED_AT_COLUMN,RecurringServiceTypeRepository.TYPE,RecurringServiceTypeRepository.NAME};
        MatrixCursor cursor= new MatrixCursor(columns);
        cursor.addRow(new Object[] {1l,"","",1l,"", 1509347806l,"","","","","",1l,"TYPE","NAME"});
        Assert.assertNull(recurringServiceRecordRepository.find(0l));
        Mockito.when(sqliteDatabase.query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.find(0l);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class));
    }

    @Test
    public void updateCallsDatabaseUpdateMethod1Times() throws Exception {
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setId(0l);
        recurringServiceRecordRepository.update(null, serviceRecord);
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.update(sqliteDatabase, null);
        recurringServiceRecordRepository.update(null, serviceRecord);
//        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void deletecallsDatabaseDeleteMethod1Times() throws Exception {
        //throwsException

        RecurringServiceRecordRepository recurringServiceRecordRepositoryspy = Mockito.spy(recurringServiceRecordRepository);
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setBaseEntityId("baseEntityID");
        serviceRecord.setName("name");
        serviceRecord.setFormSubmissionId(FORMSUBMISSIONID);
        serviceRecord.setEventId(EVENTID);
        Mockito.doReturn(serviceRecord).when(recurringServiceRecordRepositoryspy).find(0l);
        recurringServiceRecordRepository.deleteServiceRecord(0l);
        Mockito.when(recurringServiceRecordRepositoryspy.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepositoryspy.deleteServiceRecord(0l);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).delete(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void closeMethodCallsInternalMethodsWithCorrectParams() throws Exception {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.close(5l);
        Mockito.verify(recurringServiceRecordRepository.getWritableDatabase(), Mockito.times(1)).update(org.mockito.ArgumentMatchers.eq(recurringServiceRecordRepository.TABLE_NAME), (ContentValues) org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(new String[]{"5"}));
    }

    @Test
    public void closeMethodFailsSilentlyWithNullParams() throws Exception {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.close(null);
        Mockito.verify(recurringServiceRecordRepository.getWritableDatabase(), Mockito.times(0)).update(org.mockito.ArgumentMatchers.anyString(), (ContentValues) org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(new String[]{"5"}));
    }

    @Test
    public void removeHyphenMethodRemoveHyphenFromString() throws Exception {
        String testString = recurringServiceRecordRepository.removeHyphen("test_string");
        org.junit.Assert.assertNotNull(testString);
        junit.framework.Assert.assertFalse(testString.contains("-"));
    }

    @Test
    public void findUnSyncedBeforeTimeTest() throws Exception {
        String[] columns = new String[] {RecurringServiceRecordRepository.ID_COLUMN,RecurringServiceRecordRepository.BASE_ENTITY_ID,RecurringServiceRecordRepository.PROGRAM_CLIENT_ID,RecurringServiceRecordRepository.RECURRING_SERVICE_ID,RecurringServiceRecordRepository.VALUE,RecurringServiceRecordRepository.DATE,RecurringServiceRecordRepository.ANMID,RecurringServiceRecordRepository.LOCATIONID,RecurringServiceRecordRepository.SYNC_STATUS,RecurringServiceRecordRepository.EVENT_ID,RecurringServiceRecordRepository.FORMSUBMISSION_ID,RecurringServiceRecordRepository.UPDATED_AT_COLUMN,RecurringServiceTypeRepository.TYPE,RecurringServiceTypeRepository.NAME};
        MatrixCursor cursor= new MatrixCursor(columns);
        cursor.addRow(new Object[] {1l,"","",1l,"", 1509347806l,"","","","","",1l,"TYPE","NAME"});
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        Mockito.when(sqliteDatabase.query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Assert.assertNotNull(recurringServiceRecordRepository.findUnSyncedBeforeTime(10));
    }
}
