package org.smartregister.immunization.repository;

import android.content.ContentValues;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.repository.BaseRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import timber.log.Timber;

public class RecurringServiceTypeRepository extends BaseRepository {
    public static final String TABLE_NAME = "recurring_service_types";
    public static final String ID_COLUMN = "_id";
    public static final String TYPE = "type";
    public static final String NAME = "name";
    public static final String SERVICE_GROUP = "service_group";
    public static final String SERVICE_NAME_ENTITY = "service_name_entity";
    public static final String SERVICE_NAME_ENTITY_ID = "service_name_entity_id";
    public static final String DATE_ENTITY = "date_entity";
    public static final String DATE_ENTITY_ID = "date_entity_id";
    public static final String UNITS = "units";
    public static final String SERVICE_LOGIC = "service_logic";
    public static final String PREREQUISITE = "prerequisite";
    public static final String PRE_OFFSET = "pre_offset";
    public static final String EXPIRY_OFFSET = "expiry_offset";
    public static final String MILESTONE_OFFSET = "milestone_offset";
    public static final String UPDATED_AT_COLUMN = "updated_at";
    public static final String[] TABLE_COLUMNS = {ID_COLUMN, TYPE, NAME, SERVICE_GROUP, SERVICE_NAME_ENTITY, SERVICE_NAME_ENTITY_ID, DATE_ENTITY, DATE_ENTITY_ID, UNITS, SERVICE_LOGIC, PREREQUISITE, PRE_OFFSET, EXPIRY_OFFSET, MILESTONE_OFFSET, UPDATED_AT_COLUMN};
    private static final String TAG = RecurringServiceTypeRepository.class.getCanonicalName();
    private static final String CREATE_TABLE_SQL = "CREATE TABLE recurring_service_types (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,type VARCHAR NOT NULL, name VARCHAR NOT NULL, service_group VARCHAR,service_name_entity VARCHAR,service_name_entity_id VARCHAR, date_entity VARCHAR, date_entity_id VARCHAR, units VARCHAR,service_logic VARCHAR NULL,prerequisite VARCHAR NULL,pre_offset VARCHAR NULL,expiry_offset VARCHAR NULL, milestone_offset VARCHAR NULL, updated_at INTEGER NULL, UNIQUE(type, name) ON CONFLICT IGNORE)";
    private static final String TYPE_INDEX = "CREATE INDEX " + TABLE_NAME + "_" + TYPE + "_index ON " + TABLE_NAME + "(" + TYPE + " COLLATE NOCASE);";
    private static final String NAME_INDEX = "CREATE INDEX " + TABLE_NAME + "_" + NAME + "_index ON " + TABLE_NAME + "(" + NAME + " COLLATE NOCASE);";
    private static final String UPDATED_AT_INDEX = "CREATE INDEX " + TABLE_NAME + "_" + UPDATED_AT_COLUMN + "_index ON " + TABLE_NAME + "(" + UPDATED_AT_COLUMN + ");";


    public static final String ADD_SERVICE_GROUP_COLUMN = "ALTER TABLE " + TABLE_NAME + " ADD " + SERVICE_GROUP + " VARCHAR;";

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_SQL);
        database.execSQL(TYPE_INDEX);
        database.execSQL(NAME_INDEX);
        database.execSQL(UPDATED_AT_INDEX);
    }

    public void add(ServiceType serviceType) {
        add(serviceType, null);
    }

    public void add(ServiceType serviceType, SQLiteDatabase database_) {
        try {
            SQLiteDatabase database = database_;
            if (serviceType == null) {
                return;
            }

            if (serviceType.getUpdatedAt() == null) {
                serviceType.setUpdatedAt(Calendar.getInstance().getTimeInMillis());
            }

            ServiceType currentServiceType = null;
            if (serviceType.getId() != null) {
                currentServiceType = find(serviceType.getId(), database);
            }

            if (database == null) {
                database = getWritableDatabase();
            }
            if (currentServiceType == null) {
                serviceType.setId(database.insert(TABLE_NAME, null, createValuesFor(serviceType)));
            } else {
                String idSelection = ID_COLUMN + " = ?";
                database.update(TABLE_NAME, createValuesFor(serviceType), idSelection,
                        new String[]{serviceType.getId().toString()});
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public ServiceType find(Long caseId, SQLiteDatabase database_) {
        SQLiteDatabase database = database_;
        ServiceType serviceType = null;
        Cursor cursor = null;
        try {
            if (database == null) {
                database = getReadableDatabase();
            }
            cursor = database
                    .query(TABLE_NAME, TABLE_COLUMNS, ID_COLUMN + " = ?", new String[]{caseId.toString()}, null, null, null,
                            null);
            List<ServiceType> serviceTypes = readAllServiceTypes(cursor);
            if (!serviceTypes.isEmpty()) {
                serviceType = serviceTypes.get(0);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceType;
    }

    private ContentValues createValuesFor(ServiceType serviceType) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, serviceType.getId());
        values.put(TYPE, serviceType.getType() != null ? addHyphen(serviceType.getType()) : null);
        values.put(NAME, serviceType.getName() != null ? addHyphen(serviceType.getName()) : null);
        values.put(SERVICE_GROUP, serviceType.getServiceGroup());
        values.put(SERVICE_NAME_ENTITY, serviceType.getServiceNameEntity());
        values.put(SERVICE_NAME_ENTITY_ID, serviceType.getServiceNameEntityId());
        values.put(DATE_ENTITY, serviceType.getDateEntity());
        values.put(DATE_ENTITY_ID, serviceType.getDateEntityId());
        values.put(UNITS, serviceType.getUnits());
        values.put(SERVICE_LOGIC, serviceType.getServiceLogic());
        values.put(PREREQUISITE, serviceType.getPrerequisite());
        values.put(PRE_OFFSET, serviceType.getPreOffset());
        values.put(EXPIRY_OFFSET, serviceType.getExpiryOffset());
        values.put(MILESTONE_OFFSET, serviceType.getMilestoneOffset());
        values.put(UPDATED_AT_COLUMN, serviceType.getUpdatedAt());
        return values;
    }

    private List<ServiceType> readAllServiceTypes(Cursor cursor) {
        List<ServiceType> serviceTypes = new ArrayList<>();

        try {

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String name = cursor.getString(cursor.getColumnIndex(NAME));
                    if (name != null) {
                        name = removeHyphen(name);
                    }

                    String type = cursor.getString(cursor.getColumnIndex(TYPE));
                    if (type != null) {
                        type = removeHyphen(type);
                    }


                    serviceTypes.add(

                            new ServiceType.Builder(
                                    cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                                    type,
                                    name
                            ).withServiceGroup(cursor.getString(cursor.getColumnIndex(SERVICE_GROUP)))
                                    .withServiceNameEntity(cursor.getString(cursor.getColumnIndex(SERVICE_NAME_ENTITY)))
                                    .withServiceNameEntityId(cursor.getString(cursor.getColumnIndex(SERVICE_NAME_ENTITY_ID)))
                                    .withDateEntity(cursor.getString(cursor.getColumnIndex(DATE_ENTITY)))
                                    .withDateEntityId(cursor.getString(cursor.getColumnIndex(DATE_ENTITY_ID)))
                                    .withUnits(cursor.getString(cursor.getColumnIndex(UNITS)))
                                    .withServiceLogic(cursor.getString(cursor.getColumnIndex(SERVICE_LOGIC)))
                                    .withPrerequisite(cursor.getString(cursor.getColumnIndex(PREREQUISITE)))
                                    .withPreOffset(cursor.getString(cursor.getColumnIndex(PRE_OFFSET)))
                                    .withExpiryOffset(cursor.getString(cursor.getColumnIndex(EXPIRY_OFFSET)))
                                    .withMilestoneOffset(cursor.getString(cursor.getColumnIndex(MILESTONE_OFFSET)))
                                    .withUpdatedAt(cursor.getLong(cursor.getColumnIndex(UPDATED_AT_COLUMN))).build()
                    );

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceTypes;
    }

    public static String addHyphen(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replace(" ", "_");
        }
        return s;
    }

    public static String removeHyphen(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replace("_", " ");
        }
        return s;
    }

    public List<ServiceType> findByType(String type) {
        if (StringUtils.isBlank(type)) {
            return new ArrayList<>();
        }
        type = addHyphen(type);

        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database
                .query(TABLE_NAME, TABLE_COLUMNS, TYPE + " = ? " + COLLATE_NOCASE + " ORDER BY " + UPDATED_AT_COLUMN,
                        new String[]{type}, null, null, null, null);
        return readAllServiceTypes(cursor);
    }

    public List<ServiceType> searchByName(String name_) {
        String name = name_;
        if (StringUtils.isBlank(name)) {
            return new ArrayList<>();
        }
        name = addHyphen(name);


        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database
                .query(TABLE_NAME, TABLE_COLUMNS, NAME + " LIKE ? " + COLLATE_NOCASE + " ORDER BY " + UPDATED_AT_COLUMN,
                        new String[]{"%" + name + "%"}, null, null, null, null);
        return readAllServiceTypes(cursor);
    }

    @Nullable
    public ServiceType getByName(String name_) {
        if (StringUtils.isBlank(name_)) {
            return null;
        }
        String name = addHyphen(name_);

        SQLiteDatabase database = getReadableDatabase();
        ServiceType serviceType = null;
        Cursor cursor = null;
        try {
            cursor = database
                    .query(TABLE_NAME, TABLE_COLUMNS, NAME + " = ?", new String[]{name}, null, null, null,
                            null);
            List<ServiceType> serviceTypes = readAllServiceTypes(cursor);
            if (!serviceTypes.isEmpty()) {
                serviceType = serviceTypes.get(0);
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return serviceType;
    }

    public ServiceType find(Long caseId) {
        return find(caseId, null);
    }

    public void deleteServiceType(Long caseId) {
        ServiceType serviceType = find(caseId, null);
        if (serviceType != null) {
            getWritableDatabase().delete(TABLE_NAME, ID_COLUMN + "= ?", new String[]{caseId.toString()});
        }
    }

    public List<ServiceType> fetchAll() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, TABLE_COLUMNS, null, null, null, null, UPDATED_AT_COLUMN);
        return readAllServiceTypes(cursor);
    }

    /***
     * Extract the list of service types that belong to a specific group
     * @param group
     * @return
     */
    public List<String> fetchTypes(String group) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME, new String[]{TYPE}, SERVICE_GROUP + " = ? ", new String[]{group}, TYPE, null, UPDATED_AT_COLUMN);
        return extractCursorTypes(cursor);
    }

    /**
     * Extracts all service types
     *
     * @return
     */
    public List<String> fetchTypes() {
        String sql = " SELECT " + TYPE + " FROM " + TABLE_NAME + " GROUP BY " + TYPE + " ORDER BY " + UPDATED_AT_COLUMN;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, null);
        return extractCursorTypes(cursor);
    }

    public List<String> extractCursorTypes(Cursor cursor) {
        List<String> types = new ArrayList<>();
        try {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String type = cursor.getString(cursor.getColumnIndex(TYPE));
                    if (type != null) {
                        type = removeHyphen(type);
                    }
                    types.add(type);

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return types;
    }
}
