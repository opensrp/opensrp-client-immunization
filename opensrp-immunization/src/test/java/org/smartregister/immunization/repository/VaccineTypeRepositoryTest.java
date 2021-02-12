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
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.TestApplication;
import org.smartregister.immunization.domain.VaccineType;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by onaio on 29/08/2017.
 */

public class VaccineTypeRepositoryTest extends BaseUnitTest {

    private final long magicL = 10l;
    private final int magic10 = 10;
    @InjectMocks
    private VaccineTypeRepository vaccineTypeRepository;
    @Mock
    private Repository repository;
    @Mock
    private DrishtiApplication application;
    @Mock
    private CommonFtsObject commonFtsObject;
    @Mock
    private AlertService alertService;
    @Mock
    private SQLiteDatabase sqliteDatabase;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);

        Mockito.when(application.getRepository()).thenReturn(repository);
        TestApplication.setInstance(application);

        org.junit.Assert.assertNotNull(vaccineTypeRepository);
    }

    @Test
    public void assertGetDosesPerVialTest() {
        List<VaccineType> list = new ArrayList<>();
        VaccineType type = new VaccineType(magicL, magic10, "", "", "", "");
        list.add(type);
        //        VaccineTypeRepository vaccineTypeRepository = Mockito.mock(VaccineTypeRepository.class);
        VaccineTypeRepository vaccineTypeRepository = new VaccineTypeRepository(commonFtsObject, alertService);
        //        Mockito.when(vaccineTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        VaccineTypeRepository spy = Mockito.spy(vaccineTypeRepository);
        PowerMockito.doReturn(list).when(spy).findIDByName(org.mockito.ArgumentMatchers.anyString());
        Assert.assertEquals(spy.getDosesPerVial(""), magic10);
    }

    @Test
    public void assertInstantiatesSuccessfullyOnConstructorCall() {
        VaccineTypeRepository vaccineRepository = new VaccineTypeRepository(commonFtsObject, alertService);
        org.junit.Assert.assertNotNull(vaccineRepository);
    }

    @Test
    public void verifyCreateTableCallsExecuteSQLMethod() {
        VaccineTypeRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).execSQL(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() {
        Mockito.when(vaccineTypeRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineType vaccineType = PowerMockito.mock(VaccineType.class);
        Mockito.when(vaccineType.getId()).thenReturn(null);
        vaccineTypeRepository.add(vaccineType, sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .insert(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.any(ContentValues.class));
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() {
        Mockito.when(vaccineTypeRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineType vaccineType = new VaccineType(0l, 0, "", "", "", "");
        vaccineTypeRepository.add(vaccineType, sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() {
        vaccineTypeRepository.add(null, sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .insert(org.mockito.ArgumentMatchers.anyString(), (String) org.mockito.ArgumentMatchers.isNull(),
                        org.mockito.ArgumentMatchers.any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifyFindbyEntityIDcallsDatabaseQueryMethod1Times() {
        Cursor cursor = PowerMockito.mock(Cursor.class);
        Mockito.when(sqliteDatabase
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(vaccineTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineTypeRepository.findIDByName("Name");
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class));
    }

    @Test
    public void verifyFindallVaccineTypcallsDatabaseQueryMethod1Times() {
        //        Cursor cursor = PowerMockito.mock(Cursor.class);
        String[] columns = new String[] {VaccineTypeRepository.ID_COLUMN, VaccineTypeRepository.DOSES, VaccineTypeRepository.NAME, VaccineTypeRepository.OPENMRS_PARENT_ENTITIY_ID, VaccineTypeRepository.OPENMRS_DATE_CONCEPT_ID, VaccineTypeRepository.OPENMRS_DOSE_CONCEPT_ID};
        MatrixCursor cursor = new MatrixCursor(columns);
        cursor.addRow(new Object[] {1l, 1, "", "", "", ""});

        Mockito.when(sqliteDatabase
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class))).thenReturn(cursor);
        Mockito.when(vaccineTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        vaccineTypeRepository.getAllVaccineTypes(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .query(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String[].class),
                        org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class),
                        org.mockito.ArgumentMatchers.isNull(String.class));
    }

}
