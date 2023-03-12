package org.smartregister.immunization.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecurringServiceRecordRepository extends BaseRepository {
    public static final String TABLE_NAME = "recurring_service_records";
    public static final String ID_COLUMN = "_id";
    public static final String BASE_ENTITY_ID = "base_entity_id";
    public static final String VALUE = "value";
    public static final String EVENT_ID = "event_id";
    public static final String FORMSUBMISSION_ID = "formSubmissionId";
    public static final String PROGRAM_CLIENT_ID = "program_client_id";
    public static final String RECURRING_SERVICE_ID = "recurring_service_id";
    public static final String DATE = "date";
    public static final String ANMID = "anmid";
    public static final String LOCATION_ID = "location_id";
    public static final String SYNC_STATUS = "sync_status";
    public static final String UPDATED_AT_COLUMN = "updated_at";
    public static final String CREATED_AT = "created_at";
    public static final String TEAM_ID = "team_id";
    public static final String TEAM = "team";
    public static final String CHILD_LOCATION_ID = "child_location_id";
    public static final String[] TABLE_COLUMNS = {ID_COLUMN, BASE_ENTITY_ID, PROGRAM_CLIENT_ID, RECURRING_SERVICE_ID, VALUE, DATE, ANMID, LOCATION_ID, CHILD_LOCATION_ID, TEAM, TEAM_ID, SYNC_STATUS, EVENT_ID, FORMSUBMISSION_ID, UPDATED_AT_COLUMN, CREATED_AT};
    public static final String RECURRING_SERVICE_ID_INDEX = "CREATE INDEX " + TABLE_NAME + "_" + RECURRING_SERVICE_ID + "_index ON " + TABLE_NAME + "(" + RECURRING_SERVICE_ID + ");";
    public static final String EVENT_ID_INDEX = "CREATE INDEX " + TABLE_NAME + "_" + EVENT_ID + "_index ON " + TABLE_NAME + "(" + EVENT_ID + " COLLATE NOCASE);";
    public static final String FORMSUBMISSION_INDEX = "CREATE INDEX " + TABLE_NAME + "_" + FORMSUBMISSION_ID + "_index ON " + TABLE_NAME + "(" + FORMSUBMISSION_ID + " COLLATE NOCASE);";
    public static final String ALTER_ADD_CREATED_AT_COLUMN = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + CREATED_AT + " DATETIME NULL ";
    public static final String UPDATE_TABLE_ADD_TEAM_COL = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + TEAM + " VARCHAR;";
    public static final String UPDATE_TABLE_ADD_TEAM_ID_COL = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + TEAM_ID + " VARCHAR;";
    public static final String UPDATE_TABLE_ADD_CHILD_LOCATION_ID_COL = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + CHILD_LOCATION_ID + " VARCHAR;";
    private static final String TAG = RecurringServiceRecordRepository.class.getCanonicalName();
    private static final String CREATE_TABLE_SQL = "CREATE TABLE recurring_service_records (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,base_entity_id VARCHAR NOT NULL,program_client_id VARCHAR NULL,recurring_service_id INTERGER NOT NULL,value VARCHAR, date DATETIME NOT NULL,anmid VARCHAR NULL,location_id VARCHAR NULL,sync_status VARCHAR, event_id VARCHAR, formSubmissionId VARCHAR, updated_at INTEGER NULL, UNIQUE(base_entity_id, recurring_service_id) ON CONFLICT IGNORE)";
    private static final String BASE_ENTITY_ID_INDEX = "CREATE INDEX " + TABLE_NAME + "_" + BASE_ENTITY_ID + "_index ON " + TABLE_NAME + "(" + BASE_ENTITY_ID + " COLLATE NOCASE);";
    private static final String UPDATED_AT_INDEX = "CREATE INDEX " + TABLE_NAME + "_" + UPDATED_AT_COLUMN + "_index ON " + TABLE_NAME + "(" + UPDATED_AT_COLUMN + ");";

    public RecurringServiceRecordRepository(Repository repository) {
        super(repository);
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_SQL);
        database.execSQL(BASE_ENTITY_ID_INDEX);
        database.execSQL(RECURRING_SERVICE_ID_INDEX);
        database.execSQL(EVENT_ID_INDEX);
        database.execSQL(FORMSUBMISSION_INDEX);
        database.execSQL(UPDATED_AT_INDEX);
    }

    public static void migrateCreatedAt(SQLiteDatabase database) {
        try {
            String sql = "UPDATE " + TABLE_NAME +
                    " SET " + CREATED_AT + " = " +
                    " ( SELECT " + EventClientRepository.event_column.dateCreated.name() +
                    "   FROM " + EventClientRepository.Table.event.name() +
                    "   WHERE " + EventClientRepository.event_column.eventId.name() + " = " + TABLE_NAME + "." + EVENT_ID +
                    "   OR " + EventClientRepository.event_column.formSubmissionId
                    .name() + " = " + TABLE_NAME + "." + FORMSUBMISSION_ID +
                    " ) " +
                    " WHERE " + CREATED_AT + " is null ";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void add(ServiceRecord serviceRecord) {
        if (serviceRecord == null) {
            return;
        }

        try {
            if (StringUtils.isBlank(serviceRecord.getSyncStatus())) {
                serviceRecord.setSyncStatus(TYPE_Unsynced);
            }
            if (StringUtils.isBlank(serviceRecord.getFormSubmissionId())) {
                serviceRecord.setFormSubmissionId(generateRandomUUIDString());
            }

            if (serviceRecord.getUpdatedAt() == null) {
                serviceRecord.setUpdatedAt(Calendar.getInstance().getTimeInMillis());
            }

            SQLiteDatabase database = getWritableDatabase();
            if (serviceRecord.getId() == null) {
                ServiceRecord sameServiceRecord = findUnique(database, serviceRecord);
                if (sameServiceRecord != null) {
                    serviceRecord.setUpdatedAt(sameServiceRecord.getUpdatedAt());
                    serviceRecord.setId(sameServiceRecord.getId());
                    update(database, serviceRecord);
                } else {
                    if (serviceRecord.getCreatedAt() == null) {
                        serviceRecord.setCreatedAt(new Date());
                    }
                    serviceRecord.setId(database.insert(TABLE_NAME, null, createValuesFor(serviceRecord)));
                }
            } else {
                //mark the recurring service as unsynced for processing as an updated event
                serviceRecord.setSyncStatus(TYPE_Unsynced);
                update(database, serviceRecord);
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public ServiceRecord findUnique(SQLiteDatabase database_, ServiceRecord serviceRecord) {
        SQLiteDatabase database = database_;
        if (serviceRecord == null || (StringUtils.isBlank(serviceRecord.getFormSubmissionId()) && StringUtils
                .isBlank(serviceRecord.getEventId()))) {
            return null;
        }

        try {
            if (database == null) {
                database = getReadableDatabase();
            }

            String selection = null;
            String[] selectionArgs = null;
            if (StringUtils.isNotBlank(serviceRecord.getFormSubmissionId()) && StringUtils
                    .isNotBlank(serviceRecord.getEventId())) {
                selection = FORMSUBMISSION_ID + " = ? " + COLLATE_NOCASE + " OR " + EVENT_ID + " = ? " + COLLATE_NOCASE;
                selectionArgs = new String[] {serviceRecord.getFormSubmissionId(), serviceRecord.getEventId()};
            } else if (StringUtils.isNotBlank(serviceRecord.getEventId())) {
                selection = EVENT_ID + " = ? " + COLLATE_NOCASE;
                selectionArgs = new String[] {serviceRecord.getEventId()};
            } else if (StringUtils.isNotBlank(serviceRecord.getFormSubmissionId())) {
                selection = FORMSUBMISSION_ID + " = ? " + COLLATE_NOCASE;
                selectionArgs = new String[] {serviceRecord.getFormSubmissionId()};
            }

            Cursor cursor = database
                    .query(TABLE_NAME, TABLE_COLUMNS, selection, selectionArgs, null, null, ID_COLUMN + " DESC ", null);
            List<ServiceRecord> serviceRecordList = readAllServiceRecords(cursor);
            if (!serviceRecordList.isEmpty()) {
                return serviceRecordList.get(0);
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        return null;
    }

    public void update(SQLiteDatabase database_, ServiceRecord serviceRecord) {
        SQLiteDatabase database = database_;
        if (serviceRecord == null || serviceRecord.getId() == null) {
            return;
        }

        if (database == null) {
            database = getWritableDatabase();
        }

        try {
            String idSelection = ID_COLUMN + " = ?";
            database.update(TABLE_NAME, createValuesFor(serviceRecord), idSelection,
                    new String[] {serviceRecord.getId().toString()});
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private ContentValues createValuesFor(ServiceRecord serviceRecord) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, serviceRecord.getId());
        values.put(BASE_ENTITY_ID, serviceRecord.getBaseEntityId());
        values.put(PROGRAM_CLIENT_ID, serviceRecord.getProgramClientId());
        values.put(RECURRING_SERVICE_ID, serviceRecord.getRecurringServiceId());
        values.put(VALUE, serviceRecord.getValue());
        values.put(DATE, serviceRecord.getDate() != null ? serviceRecord.getDate().getTime() : null);
        values.put(ANMID, serviceRecord.getAnmId());
        values.put(LOCATION_ID, serviceRecord.getLocationId());
        values.put(TEAM, serviceRecord.getTeam());
        values.put(TEAM_ID, serviceRecord.getTeamId());
        values.put(CHILD_LOCATION_ID, serviceRecord.getChildLocationId());
        values.put(SYNC_STATUS, serviceRecord.getSyncStatus());
        values.put(EVENT_ID, serviceRecord.getEventId());
        values.put(FORMSUBMISSION_ID,
                serviceRecord.getFormSubmissionId());
        values.put(UPDATED_AT_COLUMN, serviceRecord.getUpdatedAt());
        values.put(CREATED_AT, serviceRecord.getCreatedAt() != null ? EventClientRepository.dateFormat
                .format(serviceRecord.getCreatedAt()) : null);
        return values;
    }

    private List<ServiceRecord> readAllServiceRecords(Cursor cursor) {
        List<ServiceRecord> serviceRecords = new ArrayList<>();

        try {

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    Date createdAt = null;
                    String dateCreatedString = cursor.getString(cursor.getColumnIndex(CREATED_AT));
                    if (StringUtils.isNotBlank(dateCreatedString)) {
                        try {
                            createdAt = EventClientRepository.dateFormat.parse(dateCreatedString);
                        } catch (ParseException e) {
                            Log.e(TAG, Log.getStackTraceString(e));
                        }
                    }
                    ServiceRecord serviceRecord = new ServiceRecord(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                            cursor.getString(cursor.getColumnIndex(BASE_ENTITY_ID)),
                            cursor.getString(cursor.getColumnIndex(PROGRAM_CLIENT_ID)),
                            cursor.getLong(cursor.getColumnIndex(RECURRING_SERVICE_ID)),
                            cursor.getString(cursor.getColumnIndex(VALUE)),
                            new Date(cursor.getLong(cursor.getColumnIndex(DATE))),
                            cursor.getString(cursor.getColumnIndex(ANMID)),
                            cursor.getString(cursor.getColumnIndex(LOCATION_ID)),
                            cursor.getString(cursor.getColumnIndex(SYNC_STATUS)),
                            cursor.getString(cursor.getColumnIndex(EVENT_ID)),
                            cursor.getString(cursor.getColumnIndex(FORMSUBMISSION_ID)),
                            cursor.getLong(cursor.getColumnIndex(UPDATED_AT_COLUMN)),
                            createdAt);


                    if (cursor.getColumnIndex(RecurringServiceTypeRepository.TYPE) > -1) {
                        String type = cursor.getString(cursor.getColumnIndex(RecurringServiceTypeRepository.TYPE));
                        if (type != null) {
                            type = removeHyphen(type);
                            serviceRecord.setType(type);
                        }
                    }

                    if (cursor.getColumnIndex(RecurringServiceTypeRepository.NAME) > -1) {
                        String name = cursor.getString(cursor.getColumnIndex(RecurringServiceTypeRepository.NAME));
                        if (name != null) {
                            name = removeHyphen(name);
                            serviceRecord.setName(name);
                        }
                    }

                    serviceRecord.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                    serviceRecord.setTeamId(cursor.getString(cursor.getColumnIndex(TEAM_ID)));
                    serviceRecord.setChildLocationId(cursor.getString(cursor.getColumnIndex(CHILD_LOCATION_ID)));
                    serviceRecords.add(serviceRecord);

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceRecords;
    }

    public static String removeHyphen(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replace("_", " ");
        }
        return s;
    }

    public List<ServiceRecord> findUnSyncedBeforeTime(int hours) {
        List<ServiceRecord> serviceRecords = new ArrayList<>();
        Cursor cursor = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR_OF_DAY, -hours);

            Long time = calendar.getTimeInMillis();

            cursor = getReadableDatabase()
                    .query(TABLE_NAME, TABLE_COLUMNS, UPDATED_AT_COLUMN + " < ? AND " + SYNC_STATUS + " = ?",
                            new String[] {time.toString(), TYPE_Unsynced}, null, null, null, null);
            serviceRecords = readAllServiceRecords(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceRecords;
    }

    public List<ServiceRecord> findByEntityId(String entityId) {
        SQLiteDatabase database = getReadableDatabase();
        String sql = " SELECT " + TABLE_NAME + ".*, " + RecurringServiceTypeRepository.TABLE_NAME + ".name, " + RecurringServiceTypeRepository.TABLE_NAME + ".type FROM " + TABLE_NAME + " LEFT JOIN " + RecurringServiceTypeRepository.TABLE_NAME +
                " ON " + TABLE_NAME + "." + RECURRING_SERVICE_ID + " = " + RecurringServiceTypeRepository.TABLE_NAME + "." + RecurringServiceTypeRepository.ID_COLUMN +
                " WHERE " + TABLE_NAME + "." + BASE_ENTITY_ID + " = ? " + COLLATE_NOCASE + " ORDER BY " + TABLE_NAME + "." + UPDATED_AT_COLUMN;
        Cursor cursor = database.rawQuery(sql, new String[] {entityId});
        return readAllServiceRecords(cursor);
    }

    public void deleteServiceRecord(Long caseId) {
        try {
            ServiceRecord serviceRecord = find(caseId);
            if (serviceRecord != null) {
                getWritableDatabase().delete(TABLE_NAME, ID_COLUMN + "= ?", new String[] {caseId.toString()});
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public ServiceRecord find(Long caseId) {
        ServiceRecord serviceRecord = null;
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase()
                    .query(TABLE_NAME, TABLE_COLUMNS, ID_COLUMN + " = ?", new String[] {caseId.toString()}, null, null, null,
                            null);
            List<ServiceRecord> serviceRecords = readAllServiceRecords(cursor);
            if (!serviceRecords.isEmpty()) {
                serviceRecord = serviceRecords.get(0);
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceRecord;
    }

    public void close(Long caseId) {
        try {
            ContentValues values = new ContentValues();
            values.put(SYNC_STATUS, TYPE_Synced);
            getWritableDatabase().update(TABLE_NAME, values, ID_COLUMN + " = ?", new String[] {caseId.toString()});
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}
