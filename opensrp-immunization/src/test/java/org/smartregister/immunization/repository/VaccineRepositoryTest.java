package org.smartregister.immunization.repository;

import android.content.ContentValues;
import android.content.Context;

import junit.framework.Assert;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.smartregister.immunization.domain.VaccineTest.PROGRAMCLIENTID;

/**
 * Created by onaio on 29/08/2017.
 */

public class VaccineRepositoryTest extends BaseUnitTest {

    public static final String ENTITYID = "entityID";
    public static final String EVENTID = "EventID";
    public static final String FORMSUBMISSIONID = "formsubmissionID";

    @InjectMocks
    private VaccineRepository vaccineRepository;

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
        org.junit.Assert.assertNotNull(vaccineRepository);
    }

    @Test
    public void addHyphenTest() throws Exception {
        String testString = vaccineRepository.addHyphen("");
        org.junit.Assert.assertNotNull(testString);
        org.junit.Assert.assertTrue(testString.isEmpty());
        String testString2 = vaccineRepository.addHyphen("test string");
        org.junit.Assert.assertNotNull(testString2);
        org.junit.Assert.assertTrue(testString2.contains("_"));
    }

    @Test
    public void removeHyphenTest() throws Exception {
        String testString = vaccineRepository.removeHyphen("");
        org.junit.Assert.assertNotNull(testString);
        org.junit.Assert.assertTrue(testString.isEmpty());
        String testString2 = vaccineRepository.removeHyphen("test_string");
        org.junit.Assert.assertNotNull(testString2);
        junit.framework.Assert.assertFalse(testString2.contains("-"));
    }

    @Test
    public void alertServiceTest() throws Exception {
        org.junit.Assert.assertNotNull(vaccineRepository.alertService());
        VaccineRepository vaccineRepository = new VaccineRepository(repository, commonFtsObject, null);
        Repository repository = Mockito.mock(Repository.class);
        org.smartregister.Context context = Mockito.mock(org.smartregister.Context.class);
        CommonFtsObject commonFtsObject = Mockito.mock(CommonFtsObject.class);
        ImmunizationLibrary.init(context, repository, commonFtsObject);
        vaccineRepository.alertService();
    }
    @Test
    public void findUnSyncedBeforeTimeTest(){
        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        List<Vaccine> list = vaccineRepository.findUnSyncedBeforeTime(10);
        org.junit.Assert.assertNotNull(list);
    }

    @Test
    public void constructorTest() throws Exception {
        VaccineRepository vaccineRepository = new VaccineRepository(repository, commonFtsObject, alertService);
        org.junit.Assert.assertNotNull(vaccineRepository);
    }
    @Test
    public void updateHia2StatusTest(){
        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.add(new Vaccine());
        Vaccine vaccine = new Vaccine();
        vaccine.setId(1l);
        vaccineRepository.updateHia2Status(vaccine,"TS");
        vaccineRepository.updateHia2Status(null,"TS");
    }
    @Test
    public void createTableTest() throws Exception {
        vaccineRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(3)).execSQL(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    public void addTest() throws Exception {
        //null database, throws exception
        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(null);
        Vaccine vaccine1 = new Vaccine();
        vaccine1.setId(1l);
        vaccineRepository.add(vaccine1);
        //null vaccine
        vaccineRepository.add(null);
        Mockito.verify(sqliteDatabase, Mockito.times(0)).insert(org.mockito.ArgumentMatchers.anyString(), (String) org.mockito.ArgumentMatchers.isNull(), org.mockito.ArgumentMatchers.any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
        //notnull vaccine
        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        Vaccine vaccine = new Vaccine();
        vaccine.setId(0l);
        vaccineRepository.add(vaccine);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
        //null id
        vaccineRepository.add(new Vaccine());
        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.add(new Vaccine());
        Mockito.verify(sqliteDatabase, Mockito.times(2)).insert(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.any(ContentValues.class));
        Vaccine newVaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.PROGRAMCLIENTID, VaccineTest.NAME, 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);

        VaccineRepository vaccineRepositoryspy = Mockito.spy(vaccineRepository);
        Vaccine vaccineToReturn = Mockito.mock(Vaccine.class);
        Mockito.when(vaccineRepositoryspy.findUnique(org.mockito.ArgumentMatchers.any(SQLiteDatabase.class),org.mockito.ArgumentMatchers.any(Vaccine.class))).thenReturn(vaccineToReturn);
        vaccineRepositoryspy.add(newVaccine);
        Mockito.verify(sqliteDatabase, Mockito.times(2)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void updateTest() throws Exception {
        Vaccine vaccine = new Vaccine();
        vaccine.setId(0l);
        //exception test database null
        vaccineRepository.update(null, vaccine);
        Mockito.verify(sqliteDatabase, Mockito.times(0)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
        //vaccine null test
        vaccineRepository.update(sqliteDatabase, null);
        //vaccine and database not null
        vaccineRepository.update(sqliteDatabase, vaccine);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void findbyEntityIdTest() throws Exception {
        Cursor cursor = PowerMockito.mock(Cursor.class);
        Mockito.when(sqliteDatabase.query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.findByEntityId(ENTITYID);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class));
    }

    @Test
    public void findTest() throws Exception {
        vaccineRepository.find(0l);
        Cursor cursor = PowerMockito.mock(Cursor.class);
        Mockito.when(sqliteDatabase.query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.find(1l);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class));


    }

    @Test
    public void findbyUniqueTest() throws Exception {
        Cursor cursor = PowerMockito.mock(Cursor.class);
        Vaccine vaccine = new Vaccine();
        vaccine.setFormSubmissionId(FORMSUBMISSIONID);
        vaccine.setEventId(EVENTID);
        Mockito.when(sqliteDatabase.query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.findUnique(null, vaccine);
        vaccineRepository.findUnique(sqliteDatabase, null);
        vaccineRepository.findUnique(sqliteDatabase, vaccine);
        Mockito.verify(sqliteDatabase, Mockito.times(2)).query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class));
    }

    @Test
    public void deleteVaccineTest() throws Exception {
        VaccineRepository vaccineRepositoryspy = Mockito.spy(vaccineRepository);
        Vaccine vaccine = new Vaccine();
        vaccine.setBaseEntityId("baseEntityID");
        vaccine.setName("name");
        vaccine.setFormSubmissionId(FORMSUBMISSIONID);
        vaccine.setEventId(EVENTID);
        Mockito.doReturn(vaccine).when(vaccineRepositoryspy).find(0l);
        Mockito.when(vaccineRepositoryspy.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepositoryspy.deleteVaccine(0l);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).delete(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void findWithNullHia2StatusTest() throws Exception{

        Mockito.when(vaccineRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        List<Vaccine>list= vaccineRepository.findWithNullHia2Status();
        org.junit.Assert.assertNotNull(list);
    }

    @Test
    public void closeTest() throws Exception {
        //null parameter
        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.close(null);
        Mockito.verify(vaccineRepository.getWritableDatabase(), Mockito.times(0)).update(org.mockito.ArgumentMatchers.anyString(), (ContentValues) org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(new String[]{"5"}));

        Mockito.when(vaccineRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        vaccineRepository.close(5l);
        Mockito.verify(vaccineRepository.getWritableDatabase(), Mockito.times(1)).update(org.mockito.ArgumentMatchers.eq(VaccineRepository.VACCINE_TABLE_NAME), (ContentValues) org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.eq(new String[]{"5"}));
    }

}
