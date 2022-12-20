package org.smartregister.immunization.repository;

import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.TestApplication;
import org.smartregister.immunization.domain.VaccineName;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;
import org.smartregister.view.activity.DrishtiApplication;

/**
 * Created by onaio on 29/08/2017.
 */

public class VaccineNameRepositoryTest extends BaseUnitTest {

    @InjectMocks
    private VaccineNameRepository vaccineNameRepository;

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

        org.junit.Assert.assertNotNull(vaccineNameRepository);
    }

    @Test
    public void assertInstantiatesSuccessfullyOnConstructorCall() {
        VaccineNameRepository vaccineNameRepository = new VaccineNameRepository(commonFtsObject, alertService);
        org.junit.Assert.assertNotNull(vaccineNameRepository);
    }

    @Test
    public void verifyCreateTableCallsExecuteSQLMethod() {
        VaccineNameRepository.createTable(sqliteDatabase);
        Mockito.verify(sqliteDatabase, Mockito.times(1)).execSQL(ArgumentMatchers.anyString());
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNullID() {
        Mockito.when(vaccineNameRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineName vaccineName = new VaccineName(0l, "", "", "", "", "", "");
        vaccineNameRepository.add(vaccineName);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .insert(ArgumentMatchers.anyString(), ArgumentMatchers.isNull(),
                        ArgumentMatchers.any(ContentValues.class));
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod1TimesInCaseOfNonNullVaccineNotNullID() {
        Mockito.when(vaccineNameRepository.getWritableDatabase()).thenReturn(sqliteDatabase);
        VaccineName vaccineName = new VaccineName(1l, "", "", "", "", "", "");
        vaccineNameRepository.add(vaccineName);
        Mockito.verify(sqliteDatabase, Mockito.times(1))
                .update(ArgumentMatchers.anyString(), ArgumentMatchers.any(ContentValues.class),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class));
    }

    @Test
    public void verifyAddCallsDatabaseDatabaseMethod0TimesInCaseOfNullVaccine() {
        vaccineNameRepository.add(null);
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .insert(ArgumentMatchers.anyString(), (String) ArgumentMatchers.isNull(),
                        ArgumentMatchers.any(ContentValues.class));
        Mockito.verify(sqliteDatabase, Mockito.times(0))
                .update(ArgumentMatchers.anyString(), ArgumentMatchers.any(ContentValues.class),
                        ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class));
    }

}
