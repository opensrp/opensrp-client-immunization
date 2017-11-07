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
        org.mockito.MockitoAnnotations.initMocks(this);
        org.junit.Assert.assertNotNull(vaccineNameRepository);
    }

    @Test
    public void assertInstantiatesSuccessfullyOnConstructorCall() throws Exception {
        VaccineNameRepository vaccineNameRepository = new VaccineNameRepository(repository, commonFtsObject, alertService);
        org.junit.Assert.assertNotNull(vaccineNameRepository);
    }

    @Test
    public void verifyCreateTableCallsExecuteSQLMethod() throws Exception {
        vaccineNameRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).execSQL(org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() throws Exception {
        Mockito.when(vaccineNameRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineName vaccineName = PowerMockito.mock(VaccineName.class);
        Mockito.when(vaccineName.getId()).thenReturn(null);
        vaccineNameRepository.add(vaccineName);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).insert(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.isNull(String.class), org.mockito.ArgumentMatchers.any(ContentValues.class));
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() throws Exception {
        Mockito.when(vaccineNameRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineName vaccineName = new VaccineName(0l, "", "", "", "", "", "");
        vaccineNameRepository.add(vaccineName);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() throws Exception {
        vaccineNameRepository.add(null);
        Mockito.verify(sqliteDatabase, Mockito.times(0)).insert(org.mockito.ArgumentMatchers.anyString(), (String) org.mockito.ArgumentMatchers.isNull(), org.mockito.ArgumentMatchers.any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0)).update(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(ContentValues.class), org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.any(String[].class));
    }

}
