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
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.VaccineType;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by onaio on 29/08/2017.
 */

public class RecurringServiceTypeRepositoryTest extends BaseUnitTest {

    @InjectMocks
    private RecurringServiceTypeRepository recurringServiceTypeRepository;

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
        assertNotNull(recurringServiceTypeRepository);
    }


    @Test
    public void instantiatesSuccessfullyOnConstructorCall() throws Exception {

        RecurringServiceTypeRepository vaccineNameRepository = new RecurringServiceTypeRepository(repository);
        assertNotNull(vaccineNameRepository);
    }

    @Test
    public void createTableCallsExecuteSQLMethod() throws Exception {
        recurringServiceTypeRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(4)).execSQL(anyString());
    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() throws Exception {
        when(recurringServiceTypeRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        ServiceType serviceType = PowerMockito.mock(ServiceType.class);
        when(serviceType.getId()).thenReturn(null);
        recurringServiceTypeRepository.add(serviceType,sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).insert(anyString(),isNull(String.class),any(ContentValues.class));
    }

//    @Test
//    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() throws Exception {
//        when(recurringServiceTypeRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
//        ServiceType serviceType = new ServiceType();
//        serviceType.setId(0l);
//        recurringServiceTypeRepository.add(serviceType,sqliteDatabase);
//        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(anyString(),any(ContentValues.class),anyString(),any(String [].class));
//    }
    @Test
    public void searchByNamecallsDatabaseQueryMethod1Times() throws Exception {
        RecurringServiceTypeRepository recurringServiceTypeRepositoryspy = PowerMockito.spy(recurringServiceTypeRepository);
        Cursor cursor = PowerMockito.mock(Cursor.class);
        when(sqliteDatabase.query(anyString(),any(String[].class),anyString(),any(String[].class),isNull(String.class),isNull(String.class), isNull(String.class),isNull(String.class))).thenReturn(cursor);
        when(recurringServiceTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceTypeRepository.searchByName("Name");
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(anyString(),any(String[].class),anyString(),any(String[].class),isNull(String.class),isNull(String.class), isNull(String.class), isNull(String.class));
    }

    @Test
    public void searchByTypecallsDatabaseQueryMethod1Times() throws Exception {
        RecurringServiceTypeRepository recurringServiceTypeRepositoryspy = PowerMockito.spy(recurringServiceTypeRepository);
        Cursor cursor = PowerMockito.mock(Cursor.class);
        when(sqliteDatabase.query(anyString(),any(String[].class),anyString(),any(String[].class),isNull(String.class),isNull(String.class), isNull(String.class),isNull(String.class))).thenReturn(cursor);
        when(recurringServiceTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceTypeRepository.findByType("Type");
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(anyString(),any(String[].class),anyString(),any(String[].class),isNull(String.class),isNull(String.class), isNull(String.class), isNull(String.class));
    }

    @Test
    public void findcallsDatabaseQueryMethod1Times() throws Exception {
        RecurringServiceTypeRepository recurringServiceTypeRepositoryspy = PowerMockito.spy(recurringServiceTypeRepository);
        Cursor cursor = PowerMockito.mock(Cursor.class);
        when(sqliteDatabase.query(anyString(),any(String[].class),anyString(),any(String[].class),isNull(String.class),isNull(String.class), isNull(String.class),isNull(String.class))).thenReturn(cursor);
        when(recurringServiceTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceTypeRepository.find(0l,sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(anyString(),any(String[].class),anyString(),any(String[].class),isNull(String.class),isNull(String.class), isNull(String.class), isNull(String.class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() throws Exception {
        recurringServiceTypeRepository.add(null,sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(0)).insert(anyString(),(String)isNull(),any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0)).update(anyString(),any(ContentValues.class),anyString(),any(String [].class));
    }

    @Test
    public void fetchAllTypecallsDatabaseQueryMethod1Times() throws Exception {
        RecurringServiceTypeRepository recurringServiceTypeRepositoryspy = PowerMockito.spy(recurringServiceTypeRepository);
        Cursor cursor = PowerMockito.mock(Cursor.class);
        when(sqliteDatabase.query(anyString(),any(String[].class),isNull(String.class),isNull(String[].class),isNull(String.class),isNull(String.class),anyString())).thenReturn(cursor);
        when(recurringServiceTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceTypeRepository.fetchAll();
        Mockito.verify(sqliteDatabase, Mockito.times(1)).query(anyString(),any(String[].class),isNull(String.class),isNull(String[].class),isNull(String.class),isNull(String.class),anyString());
    }

    @Test
    public void fetchTypescallsDatabaseQueryMethod1Times() throws Exception {
        RecurringServiceTypeRepository recurringServiceTypeRepositoryspy = PowerMockito.spy(recurringServiceTypeRepository);
        Cursor cursor = PowerMockito.mock(Cursor.class);
        when(sqliteDatabase.rawQuery(anyString(),isNull(String[].class))).thenReturn(cursor);
        when(recurringServiceTypeRepository.getReadableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceTypeRepository.fetchTypes();
        Mockito.verify(sqliteDatabase, Mockito.times(1)).rawQuery(anyString(),isNull(String[].class));
    }


    @Test
    public void deletecallsDatabaseDeleteMethod1Times() throws Exception {
        RecurringServiceTypeRepository recurringServiceTypeRepositoryspy = Mockito.spy(recurringServiceTypeRepository);
        ServiceType serviceType = new ServiceType();
        Mockito.doReturn(serviceType).when(recurringServiceTypeRepositoryspy).find(0l,null);
        when(recurringServiceTypeRepositoryspy.getWritableDatabase()).thenReturn(sqliteDatabase);
        recurringServiceTypeRepositoryspy.deleteServiceType(0l);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).delete(anyString(),anyString(),any(String[].class));
    }

    @Test
    public void addHyphenMethodWithBlankParamReturnsBlankString() throws Exception {
        String testString = recurringServiceTypeRepository.addHyphen("");
        assertNotNull(testString);
        assertTrue(testString.isEmpty());
    }

    @Test
    public void removeHyphenMethodWithBlankParamReturnsBlankString() throws Exception {
        String testString = recurringServiceTypeRepository.removeHyphen("");
        assertNotNull(testString);
        assertTrue(testString.isEmpty());
    }

}