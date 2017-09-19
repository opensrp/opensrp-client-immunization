package org.smartregister.immunization.repository;


import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.domain.VaccineName;
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

public class VaccineNameRepositoryTest extends BaseUnitTest {

    @InjectMocks
    private VaccineNameRepository vaccineNameRepository;

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
        initMocks(this);
        assertNotNull(vaccineNameRepository);
    }


    @Test
    public void instantiatesSuccessfullyOnConstructorCall() throws Exception {
        VaccineNameRepository vaccineNameRepository = new VaccineNameRepository(repository, commonFtsObject, alertService);
        assertNotNull(vaccineNameRepository);
    }

    @Test
    public void createTableCallsExecuteSQLMethod() throws Exception {
        vaccineNameRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).execSQL(anyString());
    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() throws Exception {
        when(vaccineNameRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineName vaccineName = PowerMockito.mock(VaccineName.class);
        when(vaccineName.getId()).thenReturn(null);
        vaccineNameRepository.add(vaccineName);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).insert(anyString(), isNull(String.class), any(ContentValues.class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() throws Exception {
        when(vaccineNameRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineName vaccineName = new VaccineName(0l, "", "", "", "", "", "");
        vaccineNameRepository.add(vaccineName);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(anyString(), any(ContentValues.class), anyString(), any(String [].class));
    }

    @Test
    public void addCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() throws Exception {
        vaccineNameRepository.add(null);
        Mockito.verify(sqliteDatabase, Mockito.times(0)).insert(anyString(), (String)isNull(), any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0)).update(anyString(), any(ContentValues.class), anyString(), any(String [].class));
    }

}