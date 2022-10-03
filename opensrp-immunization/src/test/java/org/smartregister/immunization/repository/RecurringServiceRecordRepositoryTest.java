package org.smartregister.immunization.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.BASE_ENTITY_ID;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.CHILD_LOCATION_ID;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.CREATED_AT;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.DATE;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.EVENT_ID;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.FORMSUBMISSION_ID;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.ID_COLUMN;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.LOCATION_ID;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.PROGRAM_CLIENT_ID;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.RECURRING_SERVICE_ID;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.SYNC_STATUS;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.TABLE_NAME;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.TEAM;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.TEAM_ID;
import static org.smartregister.immunization.repository.RecurringServiceRecordRepository.UPDATED_AT_COLUMN;

import android.content.ContentValues;

import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
    private final String magicDate = "1985-07-24 00:00:00";

    @InjectMocks
    private RecurringServiceRecordRepository recurringServiceRecordRepository;

    @Mock
    private Repository repository;

    @Mock
    private DrishtiApplication application;

    @Mock
    private SQLiteDatabase sqliteDatabase;

    private final String[] columns = new String[]{
            ID_COLUMN,
            BASE_ENTITY_ID,
            PROGRAM_CLIENT_ID,
            RECURRING_SERVICE_ID,
            VALUE,
            DATE,
            ANMID,
            LOCATION_ID,
            CHILD_LOCATION_ID,
            TEAM,
            TEAM_ID,
            SYNC_STATUS,
            EVENT_ID,
            FORMSUBMISSION_ID,
            UPDATED_AT_COLUMN,
            CREATED_AT
    };

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
        Mockito.verify(sqliteDatabase, Mockito.times(8)).execSQL(anyString());
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.add(new ServiceRecord());
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .insert(anyString(), isNull(), any(ContentValues.class));
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setId(0l);
        recurringServiceRecordRepository.add(serviceRecord);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .update(anyString(), any(ContentValues.class),
                        anyString(), any(String[].class));
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() {
        recurringServiceRecordRepository.add(null);
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .insert(anyString(), (String) isNull(),
                        any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .update(anyString(), any(ContentValues.class),
                        anyString(), any(String[].class));
    }

    @Test
    public void verifyFindbyEntityIDcallsDatabaseQueryMethod1Times() {
        RecurringServiceRecordRepository recurringServiceRecordRepositoryspy = PowerMockito
                .spy(recurringServiceRecordRepository);
        String[] columns = new String[]{ID_COLUMN, BASE_ENTITY_ID, PROGRAM_CLIENT_ID, RECURRING_SERVICE_ID, RecurringServiceRecordRepository.VALUE, DATE, RecurringServiceRecordRepository.ANMID, LOCATION_ID, SYNC_STATUS, EVENT_ID, FORMSUBMISSION_ID, UPDATED_AT_COLUMN, RecurringServiceTypeRepository.TYPE, RecurringServiceTypeRepository.NAME};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", 1l, "", magicNumber, "", "", "", "", "", 1l, magicType, magicNAME});
        cursor.addRow(new Object[]{1l, "", "", 1l, "", magicNumber, "", "", "", "", "", 1l, "", ""});

        Mockito.when(sqliteDatabase
                        .rawQuery(anyString(), any(String[].class)))
                .thenReturn(cursor);
        Mockito.when(recurringServiceRecordRepositoryspy.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepositoryspy.findByEntityId("entityID");
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .rawQuery(anyString(), any(String[].class));
    }

    @Test
    public void verifyFindbyUniqueIDcallsDatabaseQueryMethod1Times() {
        String[] columns = new String[]{
                CREATED_AT,
                ID_COLUMN,
                BASE_ENTITY_ID,
                PROGRAM_CLIENT_ID,
                RECURRING_SERVICE_ID,
                RecurringServiceRecordRepository.VALUE,
                DATE,
                RecurringServiceRecordRepository.ANMID,
                LOCATION_ID,
                SYNC_STATUS, EVENT_ID, FORMSUBMISSION_ID,
                UPDATED_AT_COLUMN, RecurringServiceTypeRepository.TYPE, RecurringServiceTypeRepository.NAME,
                TEAM, TEAM_ID, CHILD_LOCATION_ID};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{"2020-01-01", 1l, "", "", 1l, "", magicNumber, "", "", "", "", "", 1l, magicType, magicNAME, "", "", ""});

        ServiceRecord serviceRecord = new ServiceRecord();
        serviceRecord.setFormSubmissionId(FORMSUBMISSIONID);
        serviceRecord.setEventId(EVENTID);
        recurringServiceRecordRepository.findUnique(null, serviceRecord);
        Mockito.when(sqliteDatabase
                        .query(anyString(), any(String[].class),
                                anyString(), any(String[].class),
                                isNull(), isNull(), anyString(), isNull()))
                .thenReturn(cursor);
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.findUnique(sqliteDatabase, serviceRecord);

        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .query(anyString(), any(String[].class),
                        anyString(), any(String[].class),
                        isNull(), isNull(), anyString(), isNull());

        recurringServiceRecordRepository.findUnique(sqliteDatabase, null);
    }

    @Test
    public void assertAndVerifyFindbyCaseIDcallsDatabaseQueryMethod1Times() {
        String[] columns = new String[]{ID_COLUMN, BASE_ENTITY_ID, PROGRAM_CLIENT_ID, RECURRING_SERVICE_ID, RecurringServiceRecordRepository.VALUE, DATE, RecurringServiceRecordRepository.ANMID, LOCATION_ID, SYNC_STATUS, EVENT_ID, FORMSUBMISSION_ID, UPDATED_AT_COLUMN, RecurringServiceTypeRepository.TYPE, RecurringServiceTypeRepository.NAME};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", 1l, "", magicNumber, "", "", "", "", "", 1l, magicType, magicNAME});
        Assert.assertNull(recurringServiceRecordRepository.find(0l));
        Mockito.when(sqliteDatabase
                .query(anyString(), any(String[].class),
                        anyString(), any(String[].class),
                        isNull(), isNull(), isNull(),
                        isNull())).thenReturn(cursor);
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.find(0l);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .query(anyString(), any(String[].class),
                        anyString(), any(String[].class),
                        isNull(), isNull(), isNull(), isNull());
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
                .update(anyString(), any(ContentValues.class), anyString(), any(String[].class));
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
                .delete(anyString(), anyString(), any(String[].class));
    }

    @Test
    public void verifycloseMethodCallsInternalMethodsWithCorrectParams() {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.close(5l);
        Mockito.verify(recurringServiceRecordRepository.getWritableDatabase(), Mockito.times(1))
                .update(eq(TABLE_NAME), (ContentValues) any(), anyString(), eq(new String[]{"5"}));
    }

    @Test
    public void verifyCloseMethodFailsSilentlyWithNullParams() {
        Mockito.when(recurringServiceRecordRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceRecordRepository.close(null);
        Mockito.verify(recurringServiceRecordRepository.getWritableDatabase(), Mockito.times(0))
                .update(anyString(), (ContentValues) any(),
                        anyString(), eq(new String[]{"5"}));
    }

    @Test
    public void assertRemoveHyphenMethodRemoveHyphenFromString() {
        String testString = RecurringServiceRecordRepository.removeHyphen("test_string");
        org.junit.Assert.assertNotNull(testString);
        org.junit.Assert.assertFalse(testString.contains("-"));
    }

    @Test
    public void assertFindUnSyncedBeforeTimeTest() {
        String[] columns = new String[]{ID_COLUMN, BASE_ENTITY_ID, PROGRAM_CLIENT_ID, RECURRING_SERVICE_ID, RecurringServiceRecordRepository.VALUE, DATE, RecurringServiceRecordRepository.ANMID, LOCATION_ID, SYNC_STATUS, EVENT_ID, FORMSUBMISSION_ID, UPDATED_AT_COLUMN, RecurringServiceTypeRepository.TYPE, RecurringServiceTypeRepository.NAME};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", 1l, "", magicNumber, "", "", "", "", "", 1l, magicType, magicNAME});
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        Mockito.when(sqliteDatabase
                .query(anyString(), any(String[].class),
                        anyString(), any(String[].class),
                        isNull(), isNull(), isNull(),
                        isNull())).thenReturn(cursor);
        org.junit.Assert.assertNotNull(recurringServiceRecordRepository.findUnSyncedBeforeTime(magicNumber10));
    }

    @Test
    public void testFindByBaseEntityIdAndRecurringServiceIdInvokesDBQueryAndReturnsCorrectVaccine() {
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1L, "base-entity-id", "", 1L, "", magicNumber, "", "", "", "", "", "", "", "", 1L, magicDate});

        Mockito.when(
                sqliteDatabase.query(
                        anyString(), any(String[].class), anyString(), any(String[].class),
                        isNull(), isNull(), isNull(), isNull()
                )
        ).thenReturn(cursor);
        Mockito.when(recurringServiceRecordRepository.getReadableDatabase()).thenReturn(sqliteDatabase);

        ServiceRecord serviceRecord = recurringServiceRecordRepository.findByBaseEntityIdAndRecurringServiceId("base-entity-id", 1L);

        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .query(anyString(), any(String[].class), anyString(), any(String[].class),
                        isNull(), isNull(), isNull(), isNull());

        org.junit.Assert.assertNotNull(serviceRecord);
        org.junit.Assert.assertEquals(Long.valueOf(1), serviceRecord.getRecurringServiceId());
        org.junit.Assert.assertEquals("base-entity-id", serviceRecord.getBaseEntityId());
    }

    @Test
    public void testFindByBaseEntityIdAndRecurringServiceIdReturnsNullVaccineWhenExceptionThrown() {
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1L, "base-entity-id", "", 1L, "", magicNumber, "", "", "", "", "", "", "", "", 1L, magicDate});

        Mockito.when(sqliteDatabase.query(
                anyString(), any(String[].class), anyString(), any(String[].class),
                isNull(), isNull(), isNull(), isNull())
        ).thenThrow(new RuntimeException());

        ServiceRecord serviceRecord = recurringServiceRecordRepository.findByBaseEntityIdAndRecurringServiceId("base-entity-id", 1L);
        org.junit.Assert.assertNull(serviceRecord);
    }
}
