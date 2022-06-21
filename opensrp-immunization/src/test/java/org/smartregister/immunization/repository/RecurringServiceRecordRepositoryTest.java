package org.smartregister.immunization.repository;

import android.content.ContentValues;

import org.junit.Assert;

import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.TestApplication;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;

/**
 * Created by onaio on 29/08/2017.
 */

public class RecurringServiceRecordRepositoryTest extends BaseUnitTest {

    public static final String EVENTID = "EventID";
    private static final String FORMSUBMISSIONID = "formsubmissionID";
    private final long magicNumber = 1509347806l;
    private final String magicType = "TYPE";
    private final String magicNAME = "NAME";
    private final int magicNumber10 = 10;

    @InjectMocks
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Mock
    private Repository repository;

    @Mock
    private DrishtiApplication application;

    @Mock
    private SQLiteDatabase sqliteDatabase;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);

        Mockito.when(application.getRepository()).thenReturn(repository);
        TestApplication.setInstance(application);

        org.junit.Assert.assertNotNull(recurringServiceRecordRepository);
    }

    @Test
    public void testMigrateCreatedAt() {
        String sql = "UPDATE recurring_service_records SET created_at =  ( SELECT dateCreated   FROM event   WHERE eventId = recurring_service_records.event_id   OR formSubmissionId = recurring_service_records.formSubmissionId )  WHERE created_at is null ";
        RecurringServiceRecordRepository.migrateCreatedAt(sqliteDatabase);
        Mockito.verify(sqliteDatabase).execSQL(sql);
    }

    @Test
    public void assertInstantiatesSuccessfullyOnConstructorCall() {
        RecurringServiceRecordRepository vaccineNameRepository = new RecurringServiceRecordRepository();
        org.junit.Assert.assertNotNull(vaccineNameRepository);
    }

    @Test
    public void verifyCreateTableCallsExecuteSQLMethod() {
        RecurringServiceRecordRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(8)).execSQL(ArgumentMatchers.anyString());
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.add(new ServiceRecord());
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .insert(ArgumentMatchers.anyString(), ArgumentMatchers.isNull(),
                        ArgumentMatchers.any(ContentValues.class));
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setId(0l);
        recurringServiceRecordRepository.add(serviceRecord);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .update(ArgumentMatchers.anyString(), ArgumentMatchers.any(ContentValues.class),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() {
        recurringServiceRecordRepository.add(null);
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .insert(ArgumentMatchers.anyString(), (String) ArgumentMatchers.isNull(),
                        ArgumentMatchers.any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .update(ArgumentMatchers.anyString(), ArgumentMatchers.any(ContentValues.class),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifyFindbyEntityIDcallsDatabaseQueryMethod1Times() {
        RecurringServiceRecordRepository recurringServiceRecordRepositoryspy = PowerMockito
                .spy(recurringServiceRecordRepository);
        String[] columns = new String[]{RecurringServiceRecordRepository.ID_COLUMN, RecurringServiceRecordRepository.BASE_ENTITY_ID, RecurringServiceRecordRepository.PROGRAM_CLIENT_ID, RecurringServiceRecordRepository.RECURRING_SERVICE_ID, RecurringServiceRecordRepository.VALUE, RecurringServiceRecordRepository.DATE, RecurringServiceRecordRepository.ANMID, RecurringServiceRecordRepository.LOCATION_ID, RecurringServiceRecordRepository.SYNC_STATUS, RecurringServiceRecordRepository.EVENT_ID, RecurringServiceRecordRepository.FORMSUBMISSION_ID, RecurringServiceRecordRepository.UPDATED_AT_COLUMN, RecurringServiceTypeRepository.TYPE, RecurringServiceTypeRepository.NAME};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", 1l, "", magicNumber, "", "", "", "", "", 1l, magicType, magicNAME});
        cursor.addRow(new Object[]{1l, "", "", 1l, "", magicNumber, "", "", "", "", "", 1l, "", ""});

        Mockito.when(sqliteDatabase
                        .rawQuery(ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class)))
                .thenReturn(cursor);
        Mockito.when(recurringServiceRecordRepositoryspy.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepositoryspy.findByEntityId("entityID");
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .rawQuery(ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifyFindbyUniqueIDcallsDatabaseQueryMethod1Times() {
        String[] columns = new String[]{
                RecurringServiceRecordRepository.CREATED_AT,
                RecurringServiceRecordRepository.ID_COLUMN,
                RecurringServiceRecordRepository.BASE_ENTITY_ID,
                RecurringServiceRecordRepository.PROGRAM_CLIENT_ID,
                RecurringServiceRecordRepository.RECURRING_SERVICE_ID,
                RecurringServiceRecordRepository.VALUE,
                RecurringServiceRecordRepository.DATE,
                RecurringServiceRecordRepository.ANMID,
                RecurringServiceRecordRepository.LOCATION_ID,
                RecurringServiceRecordRepository.SYNC_STATUS, RecurringServiceRecordRepository.EVENT_ID, RecurringServiceRecordRepository.FORMSUBMISSION_ID,
                RecurringServiceRecordRepository.UPDATED_AT_COLUMN, RecurringServiceTypeRepository.TYPE, RecurringServiceTypeRepository.NAME,
                RecurringServiceRecordRepository.TEAM, RecurringServiceRecordRepository.TEAM_ID, RecurringServiceRecordRepository.CHILD_LOCATION_ID};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{"2020-01-01", 1l, "", "", 1l, "", magicNumber, "", "", "", "", "", 1l, magicType, magicNAME, "", "", ""});

        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setFormSubmissionId(FORMSUBMISSIONID);
        serviceRecord.setEventId(EVENTID);
        recurringServiceRecordRepository.findUnique(null, serviceRecord);
        Mockito.when(sqliteDatabase
                        .query(ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                                ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                                ArgumentMatchers.isNull(), ArgumentMatchers.isNull(),
                                ArgumentMatchers.anyString(), ArgumentMatchers.isNull()))
                .thenReturn(cursor);
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.findUnique(sqliteDatabase, serviceRecord);

        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .query(ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                        ArgumentMatchers.isNull(), ArgumentMatchers.isNull(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.isNull());

        recurringServiceRecordRepository.findUnique(sqliteDatabase, null);
    }

    @Test
    public void assertAndVerifyFindbyCaseIDcallsDatabaseQueryMethod1Times() {
        String[] columns = new String[]{RecurringServiceRecordRepository.ID_COLUMN, RecurringServiceRecordRepository.BASE_ENTITY_ID, RecurringServiceRecordRepository.PROGRAM_CLIENT_ID, RecurringServiceRecordRepository.RECURRING_SERVICE_ID, RecurringServiceRecordRepository.VALUE, RecurringServiceRecordRepository.DATE, RecurringServiceRecordRepository.ANMID, RecurringServiceRecordRepository.LOCATION_ID, RecurringServiceRecordRepository.SYNC_STATUS, RecurringServiceRecordRepository.EVENT_ID, RecurringServiceRecordRepository.FORMSUBMISSION_ID, RecurringServiceRecordRepository.UPDATED_AT_COLUMN, RecurringServiceTypeRepository.TYPE, RecurringServiceTypeRepository.NAME};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", 1l, "", magicNumber, "", "", "", "", "", 1l, magicType, magicNAME});
        Assert.assertNull(recurringServiceRecordRepository.find(0l));
        Mockito.when(sqliteDatabase
                .query(ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                        ArgumentMatchers.isNull(), ArgumentMatchers.isNull(),
                        ArgumentMatchers.isNull(),
                        ArgumentMatchers.isNull())).thenReturn(cursor);
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.find(0l);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .query(ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                        ArgumentMatchers.isNull(), ArgumentMatchers.isNull(),
                        ArgumentMatchers.isNull(),
                        ArgumentMatchers.isNull());
    }

    @Test
    public void verifyUpdateCallsDatabaseUpdateMethod1Times() {
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setId(0l);
        recurringServiceRecordRepository.update(null, serviceRecord);
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.update(sqliteDatabase, null);
        recurringServiceRecordRepository.update(null, serviceRecord);
        recurringServiceRecordRepository.update(sqliteDatabase, serviceRecord);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .update(ArgumentMatchers.anyString(), ArgumentMatchers.any(ContentValues.class),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifyDeletecallsDatabaseDeleteMethod1Times() {

        RecurringServiceRecordRepository recurringServiceRecordRepositoryspy = Mockito.spy(recurringServiceRecordRepository);
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setBaseEntityId("baseEntityID");
        serviceRecord.setName(magicNAME);
        serviceRecord.setFormSubmissionId(FORMSUBMISSIONID);
        serviceRecord.setEventId(EVENTID);
        Mockito.doReturn(serviceRecord).when(recurringServiceRecordRepositoryspy).find(0l);
        recurringServiceRecordRepository.deleteServiceRecord(0l);
        Mockito.when(recurringServiceRecordRepositoryspy.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepositoryspy.deleteServiceRecord(0l);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .delete(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                        ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifycloseMethodCallsInternalMethodsWithCorrectParams() {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.close(5l);
        Mockito.verify(recurringServiceRecordRepository.getWritableDatabase(), Mockito.times(1))
                .update(ArgumentMatchers.eq(RecurringServiceRecordRepository.TABLE_NAME),
                        (ContentValues) ArgumentMatchers.any(), ArgumentMatchers.anyString(),
                        ArgumentMatchers.eq(new String[]{"5"}));
    }

    @Test
    public void verifyCloseMethodFailsSilentlyWithNullParams() {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.close(null);
        Mockito.verify(recurringServiceRecordRepository.getWritableDatabase(), Mockito.times(0))
                .update(ArgumentMatchers.anyString(), (ContentValues) ArgumentMatchers.any(),
                        ArgumentMatchers.anyString(), ArgumentMatchers.eq(new String[]{"5"}));
    }

    @Test
    public void assertRemoveHyphenMethodRemoveHyphenFromString() {
        String testString = RecurringServiceRecordRepository.removeHyphen("test_string");
        org.junit.Assert.assertNotNull(testString);
        junit.framework.Assert.assertFalse(testString.contains("-"));
    }

    @Test
    public void assertFindUnSyncedBeforeTimeTest() {
        String[] columns = new String[]{RecurringServiceRecordRepository.ID_COLUMN, RecurringServiceRecordRepository.BASE_ENTITY_ID, RecurringServiceRecordRepository.PROGRAM_CLIENT_ID, RecurringServiceRecordRepository.RECURRING_SERVICE_ID, RecurringServiceRecordRepository.VALUE, RecurringServiceRecordRepository.DATE, RecurringServiceRecordRepository.ANMID, RecurringServiceRecordRepository.LOCATION_ID, RecurringServiceRecordRepository.SYNC_STATUS, RecurringServiceRecordRepository.EVENT_ID, RecurringServiceRecordRepository.FORMSUBMISSION_ID, RecurringServiceRecordRepository.UPDATED_AT_COLUMN, RecurringServiceTypeRepository.TYPE, RecurringServiceTypeRepository.NAME};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", 1l, "", magicNumber, "", "", "", "", "", 1l, magicType, magicNAME});
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        Mockito.when(sqliteDatabase
                .query(ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class),
                        ArgumentMatchers.isNull(), ArgumentMatchers.isNull(),
                        ArgumentMatchers.isNull(),
                        ArgumentMatchers.isNull())).thenReturn(cursor);
        Assert.assertNotNull(recurringServiceRecordRepository.findUnSyncedBeforeTime(magicNumber10));
    }
}
