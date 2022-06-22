package org.smartregister.immunization.repository;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.repository.BaseRepository;

import timber.log.Timber;

public class VaccineOverdueCountRepository extends BaseRepository {

    public static final String TABLE_NAME = "vaccine_overdue_count";

    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME + "("
            + VaccineRepository.BASE_ENTITY_ID + " VARCHAR NOT NULL,"
            + "UNIQUE(" + VaccineRepository.BASE_ENTITY_ID + ") ON CONFLICT REPLACE)";

    @VisibleForTesting
    protected static final String UPDATE_QUERY_SQL = "INSERT INTO " + TABLE_NAME + "(base_entity_id) values ( ? ) ON conflict(base_entity_id) do nothing;";

    public static void createTable(@NonNull SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_SQL);
    }

    /**
     * Gets the count of overdue (urgent) entries in the vaccines table and uses the query filter @link #COUNT_QUERY
     */
    public int getOverdueCount(String countQuerySQL) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuerySQL, null);
        return cursor != null && cursor.getCount() > 0 && cursor.moveToFirst() ? cursor.getInt(0) : 0;
    }

    /**
     * Inserts a new record into the vaccine overdue count table by baseEntityId or nothing if one already exists
     * This table only tracks at least one entry of the corresponding baseEntityId. Removal/Deletion from the table is via the {@link #delete(String baseEntityId)}
     *
     * @param baseEntityId the entity id of client
     */
    public void upsert(String baseEntityId) {
        try {
            getWritableDatabase().execSQL(UPDATE_QUERY_SQL, new String[]{baseEntityId});
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    /**
     * Deletes an entry in the table by base entity id
     *
     * @return boolean true if any records were deleted
     */
    public boolean delete(String baseEntityId) {
        SQLiteDatabase database = getWritableDatabase();
        int affectedRows = database.delete(TABLE_NAME, VaccineRepository.BASE_ENTITY_ID + " = ?", new String[]{baseEntityId});
        return affectedRows > 0;
    }

}
