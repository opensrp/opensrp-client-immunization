package org.smartregister.immunization.repository;

import android.content.ContentValues;

import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.TestApplication;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.Date;
import java.util.List;

/**
 * Created by onaio on 29/08/2017.
 */

public class VaccineRepositoryTest extends BaseUnitTest {

    public static final String ENTITYID = "entityID";
    public static final String EVENTID = "EventID";
    public static final String FORMSUBMISSIONID = "formsubmissionID";
    private final int magic10 = 10;
    private final long magicTime = 1509347804l;
    private final String magicName = "NAME";
    private final String magicTS = "TS";
    @Mock
    public SQLiteDatabase sqliteDatabase;
    @InjectMocks
    private VaccineRepository vaccineRepository;
    @Mock
    private Repository repository;
    @Mock
    private DrishtiApplication application;
    @Mock
    private CommonFtsObject commonFtsObject;
    @Mock
    private AlertService alertService;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);

        Mockito.when(application.getRepository()).thenReturn(repository);
        TestApplication.setInstance(application);

        org.junit.Assert.assertNotNull(vaccineRepository);
    }

    public SQLiteDatabase getMockDatabase() {
        return sqliteDatabase == null ? Mockito.mock(SQLiteDatabase.class) : sqliteDatabase;
    }

    @Test
    public void assertAddHyphenTest() {
        String testString = VaccineRepository.addHyphen("");
        org.junit.Assert.assertNotNull(testString);
        org.junit.Assert.assertTrue(testString.isEmpty());
        String testString2 = VaccineRepository.addHyphen("test string");
        org.junit.Assert.assertNotNull(testString2);
        org.junit.Assert.assertTrue(testString2.contains("_"));
    }

    @Test
    public void assertRemoveHyphenTest() {
        String testString = VaccineRepository.removeHyphen("");
        org.junit.Assert.assertNotNull(testString);
        org.junit.Assert.assertTrue(testString.isEmpty());
        String testString2 = VaccineRepository.removeHyphen("test_string");
        org.junit.Assert.assertNotNull(testString2);
        Assert.assertFalse(testString2.contains("-"));
    }

    @Test
    public void assertFindUnSyncedBeforeTimeTest() {
        String[] columns = new String[]{VaccineRepository.ID_COLUMN, VaccineRepository.BASE_ENTITY_ID, VaccineRepository.PROGRAM_CLIENT_ID, VaccineRepository.NAME, VaccineRepository.CALCULATION, VaccineRepository.DATE, VaccineRepository.ANMID, VaccineRepository.LOCATION_ID, VaccineRepository.SYNC_STATUS, VaccineRepository.HIA2_STATUS, VaccineRepository.UPDATED_AT_COLUMN, VaccineRepository.EVENT_ID, VaccineRepository.FORMSUBMISSION_ID, VaccineRepository.OUT_OF_AREA};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", magicName, magic10, magicTime, "", "", "", "", 1l, "", "", 1});
        org.junit.Assert.assertNotNull(vaccineRepository.findUnSyncedBeforeTime(magic10));
        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        Mockito.when(sqliteDatabase
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        org.junit.Assert.assertNotNull(vaccineRepository.findUnSyncedBeforeTime(magic10));
    }

    @Test
    public void assertConstructorTest() {
        VaccineRepository vaccineRepository = new VaccineRepository(commonFtsObject, alertService);
        org.junit.Assert.assertNotNull(vaccineRepository);
    }

    @Test
    public void verifyUpdateHia2StatusTest() {
        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.add(new Vaccine());
        Vaccine vaccine = new Vaccine();
        vaccine.setId(1l);
        vaccineRepository.updateHia2Status(vaccine, magicTS);
        vaccineRepository.updateHia2Status(null, magicTS);
    }

    @Test
    public void verifyCreateTableTest() {
        VaccineRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(3)).execSQL(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    public void verifyaddTestShouldCallInsertAndUpdate1Time() {
        //null database, throws exception
        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(null);
        Vaccine vaccine1 = new Vaccine();
        vaccine1.setId(1l);
        vaccineRepository.add(vaccine1);
        //null vaccine
        vaccineRepository.add(null);
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .insert(org.mockito.ArgumentMatchers.anyString(), (String) org.mockito.ArgumentMatchers.isNull(),
                        org.mockito.ArgumentMatchers.any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
        //notnull vaccine
        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        Vaccine vaccine = new Vaccine();
        vaccine.setId(0l);
        vaccineRepository.add(vaccine);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
        //null id
        vaccineRepository.add(new Vaccine());
        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.add(new Vaccine());
        Mockito.verify(sqliteDatabase, Mockito.times(2))
                .insert(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.any(ContentValues.class));
        Vaccine newVaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.PROGRAMCLIENTID, VaccineTest.NAME, 0,
                new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0, new Date());

        VaccineRepository vaccineRepositoryspy = Mockito.spy(vaccineRepository);
        Vaccine vaccineToReturn = Mockito.mock(Vaccine.class);
        Mockito.when(vaccineRepositoryspy.findUnique(org.mockito.ArgumentMatchers.any(SQLiteDatabase.class),
                org.mockito.ArgumentMatchers.any(Vaccine.class))).thenReturn(vaccineToReturn);
        vaccineRepositoryspy.add(newVaccine);
        Mockito.verify(sqliteDatabase, Mockito.times(2))
                .update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifyUpdateTestCallsDatabaseUpdate1Time() {
        Vaccine vaccine = new Vaccine();
        vaccine.setId(0l);
        //exception test database null
        vaccineRepository.update(null, vaccine);
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
        //vaccine null test
        vaccineRepository.update(sqliteDatabase, null);
        //vaccine and database not null
        vaccineRepository.update(sqliteDatabase, vaccine);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifyFindbyEntityIdTestCallsDatabaseQuery1Time() {
        String[] columns = new String[]{VaccineRepository.ID_COLUMN, VaccineRepository.BASE_ENTITY_ID, VaccineRepository.PROGRAM_CLIENT_ID, VaccineRepository.NAME, VaccineRepository.CALCULATION, VaccineRepository.DATE, VaccineRepository.ANMID, VaccineRepository.LOCATION_ID, VaccineRepository.SYNC_STATUS, VaccineRepository.HIA2_STATUS, VaccineRepository.UPDATED_AT_COLUMN, VaccineRepository.EVENT_ID, VaccineRepository.FORMSUBMISSION_ID, VaccineRepository.OUT_OF_AREA};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", magicName, magic10, magicTime, "", "", "", "", 1l, "", "", 1});

        Mockito.when(sqliteDatabase
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.findByEntityId(ENTITYID);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class));
    }

    @Test
    public void verifyFindTwentyFourTestCallsDatabaseQuery1Time() {
        String[] columns = new String[]{VaccineRepository.ID_COLUMN, VaccineRepository.BASE_ENTITY_ID, VaccineRepository.PROGRAM_CLIENT_ID, VaccineRepository.NAME, VaccineRepository.CALCULATION, VaccineRepository.DATE, VaccineRepository.ANMID, VaccineRepository.LOCATION_ID, VaccineRepository.SYNC_STATUS, VaccineRepository.HIA2_STATUS, VaccineRepository.UPDATED_AT_COLUMN, VaccineRepository.EVENT_ID, VaccineRepository.FORMSUBMISSION_ID, VaccineRepository.OUT_OF_AREA};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", magicName, magic10, magicTime, "", "", "", "", 1l, "", "", 1});

        Mockito.when(sqliteDatabase
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.findLatestTwentyFourHoursByEntityId(ENTITYID);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class));
    }

    @Test
    public void verifyFindTestCallsDatabaseQuery1Time() {
        vaccineRepository.find(0l);
        String[] columns = new String[]{VaccineRepository.ID_COLUMN, VaccineRepository.BASE_ENTITY_ID, VaccineRepository.PROGRAM_CLIENT_ID, VaccineRepository.NAME, VaccineRepository.CALCULATION, VaccineRepository.DATE, VaccineRepository.ANMID, VaccineRepository.LOCATION_ID, VaccineRepository.SYNC_STATUS, VaccineRepository.HIA2_STATUS, VaccineRepository.UPDATED_AT_COLUMN, VaccineRepository.EVENT_ID, VaccineRepository.FORMSUBMISSION_ID, VaccineRepository.OUT_OF_AREA};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", magicName, magic10, magicTime, "", "", "", "", 1l, "", "", 1});

        Mockito.when(sqliteDatabase
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.find(1l);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class));


    }

    @Test
    public void findbyUniqueTest() {
        String[] columns = new String[]{VaccineRepository.ID_COLUMN, VaccineRepository.BASE_ENTITY_ID, VaccineRepository.PROGRAM_CLIENT_ID, VaccineRepository.NAME, VaccineRepository.CALCULATION, VaccineRepository.DATE, VaccineRepository.ANMID, VaccineRepository.LOCATION_ID, VaccineRepository.SYNC_STATUS, VaccineRepository.HIA2_STATUS, VaccineRepository.UPDATED_AT_COLUMN, VaccineRepository.EVENT_ID, VaccineRepository.FORMSUBMISSION_ID, VaccineRepository.OUT_OF_AREA};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[]{1l, "", "", magicName, magic10, magicTime, "", "", "", "", 1l, "", "", 1});

        Vaccine vaccine = new Vaccine();
        vaccine.setFormSubmissionId(FORMSUBMISSIONID);
        vaccine.setEventId(EVENTID);
        Mockito.when(sqliteDatabase
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class)))
                .thenReturn(cursor);
        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.findUnique(null, vaccine);
        vaccineRepository.findUnique(sqliteDatabase, null);
        vaccineRepository.findUnique(sqliteDatabase, vaccine);
        Mockito.verify(sqliteDatabase, Mockito.times(2))
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class));
    }

    @Test
    public void deleteVaccineTest() {
        VaccineRepository vaccineRepositoryspy = Mockito.spy(vaccineRepository);
        Vaccine vaccine = new Vaccine();
        vaccine.setBaseEntityId("baseEntityID");
        vaccine.setName(magicName);
        vaccine.setFormSubmissionId(FORMSUBMISSIONID);
        vaccine.setEventId(EVENTID);
        Mockito.doReturn(vaccine).when(vaccineRepositoryspy).find(0l);
        Mockito.when(vaccineRepositoryspy.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepositoryspy.deleteVaccine(0l);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .delete(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void findWithNullHia2StatusTest() {

        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        List<Vaccine> list = vaccineRepository.findWithNullHia2Status();
        org.junit.Assert.assertNotNull(list);
    }

    @Test
    public void closeTest() {
        //null parameter
        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.close(null);
        Mockito.verify(vaccineRepository.getWritableDatabase(), Mockito.times(0))
                .update(org.mockito.ArgumentMatchers.anyString(), (ContentValues) org.mockito.ArgumentMatchers.any(),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(new String[]{"5"}));

        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.close(5l);
        Mockito.verify(vaccineRepository.getWritableDatabase(), Mockito.times(1))
                .update(org.mockito.ArgumentMatchers.eq(VaccineRepository.VACCINE_TABLE_NAME),
                        (ContentValues) org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers.eq(new String[]{"5"}));
    }

}
