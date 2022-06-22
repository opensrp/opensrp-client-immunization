package org.smartregister.immunization.repository;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.smartregister.immunization.BaseUnitTest;

public class VaccineOverdueCountRepositoryTest extends BaseUnitTest {

    @Spy
    private VaccineOverdueCountRepository overdueCountRepository;

    @Mock
    private SQLiteDatabase sqliteDatabase;

    @Mock
    private Cursor cursor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTableExecutesCorrectSQLQuery() {

        Mockito.doNothing().when(sqliteDatabase).execSQL(ArgumentMatchers.anyString());

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        VaccineOverdueCountRepository.createTable(sqliteDatabase);

        Mockito.verify(sqliteDatabase).execSQL(stringArgumentCaptor.capture());

        String sQLExecuted = stringArgumentCaptor.getValue();

        Assert.assertNotNull(sQLExecuted);
        Assert.assertEquals(VaccineOverdueCountRepository.CREATE_TABLE_SQL, sQLExecuted);

    }

    @Test
    public void testGetOverdueCountReturnsFirstCursorIndexValue() {

        Mockito.doReturn(1).when(cursor).getCount();
        Mockito.doReturn(true).when(cursor).moveToFirst();
        Mockito.doReturn(5).when(cursor).getInt(ArgumentMatchers.anyInt());

        Mockito.doReturn(sqliteDatabase).when(overdueCountRepository).getReadableDatabase();

        Mockito.doReturn(cursor).when(sqliteDatabase).rawQuery(ArgumentMatchers.anyString(), ArgumentMatchers.isNull());

        String countQuerySQL = "SELECT COUNT(ROWID) FROM alerts WHERE status = 'urgent' ";

        overdueCountRepository.getOverdueCount(countQuerySQL);

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sqliteDatabase).rawQuery(stringArgumentCaptor.capture(), ArgumentMatchers.isNull());

        String queryFired = stringArgumentCaptor.getValue();
        Assert.assertNotNull(queryFired);
        Assert.assertEquals(countQuerySQL, queryFired);

        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.verify(cursor).getInt(integerArgumentCaptor.capture());

        Integer index = integerArgumentCaptor.getValue();
        Assert.assertNotNull(index);
        Assert.assertEquals(0, index.intValue());
    }

    @Test
    public void testGetOverdueCountReturnsZeroIfCursorNull() {

        Mockito.doReturn(sqliteDatabase).when(overdueCountRepository).getReadableDatabase();

        Mockito.doReturn(null).when(sqliteDatabase).rawQuery(ArgumentMatchers.anyString(), ArgumentMatchers.isNull());

        String countQuerySQL = "SELECT COUNT(ROWID) FROM alerts WHERE status = 'urgent' ";

        int result = overdueCountRepository.getOverdueCount(countQuerySQL);

        Assert.assertEquals(0, result);
    }

    @Test
    public void testUpsertExecuteCorrectUpdateAndInsertQuery() {
        String TEST_BASE_ENTIY_ID = "test-id";

        Mockito.doNothing().when(sqliteDatabase).execSQL(ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class));
        Mockito.doReturn(sqliteDatabase).when(overdueCountRepository).getWritableDatabase();

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String[]> stringArrayArgumentCaptor = ArgumentCaptor.forClass(String[].class);

        overdueCountRepository.upsert(TEST_BASE_ENTIY_ID);

        Mockito.verify(sqliteDatabase).execSQL(stringArgumentCaptor.capture(), stringArrayArgumentCaptor.capture());

        String sQLExecuted = stringArgumentCaptor.getValue();

        Assert.assertNotNull(sQLExecuted);
        Assert.assertEquals(VaccineOverdueCountRepository.UPDATE_QUERY_SQL, sQLExecuted);

        String[] resultParameters = stringArrayArgumentCaptor.getValue();
        Assert.assertNotNull(resultParameters);
        Assert.assertEquals(1, resultParameters.length);
        Assert.assertEquals(TEST_BASE_ENTIY_ID, resultParameters[0]);
    }

    @Test
    public void testDeleteExecutesDBDeleteWithCorrectParameters() {

        String TEST_BASE_ENTIY_ID = "test-id";

        Mockito.doReturn(0).when(sqliteDatabase).delete(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(String[].class));
        Mockito.doReturn(sqliteDatabase).when(overdueCountRepository).getWritableDatabase();

        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> stringArgumentCaptor2 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String[]> stringArrayArgumentCaptor = ArgumentCaptor.forClass(String[].class);

        overdueCountRepository.delete(TEST_BASE_ENTIY_ID);

        Mockito.verify(sqliteDatabase).delete(stringArgumentCaptor.capture(), stringArgumentCaptor2.capture(), stringArrayArgumentCaptor.capture());

        String tableName = stringArgumentCaptor.getValue();

        Assert.assertNotNull(tableName);
        Assert.assertEquals(VaccineOverdueCountRepository.TABLE_NAME, tableName);

        String sQLQueryFilter = stringArgumentCaptor2.getValue();

        Assert.assertNotNull(sQLQueryFilter);
        Assert.assertEquals("base_entity_id = ?", sQLQueryFilter);

        String[] resultParameters = stringArrayArgumentCaptor.getValue();
        Assert.assertNotNull(resultParameters);
        Assert.assertEquals(1, resultParameters.length);
        Assert.assertEquals(TEST_BASE_ENTIY_ID, resultParameters[0]);
    }
}