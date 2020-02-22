package org.smartregister.immunization.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.ei.drishti.dto.AlertStatus;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.domain.Alert;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.repository.BaseRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.service.AlertService;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class VaccineRepository extends BaseRepository {
    public static final String VACCINE_TABLE_NAME = "vaccines";
    public static final String ID_COLUMN = "_id";
    public static final String BASE_ENTITY_ID = "base_entity_id";
    public static final String EVENT_ID = "event_id";
    public static final String FORMSUBMISSION_ID = "formSubmissionId";
    public static final String PROGRAM_CLIENT_ID = "program_client_id";
    public static final String NAME = "name";
    public static final String CALCULATION = "calculation";
    public static final String DATE = "date";
    public static final String ANMID = "anmid";
    public static final String LOCATION_ID = "location_id";
    public static final String CHILD_LOCATION_ID = "child_location_id";
    public static final String SYNC_STATUS = "sync_status";
    public static final String HIA2_STATUS = "hia2_status";
    public static final String UPDATED_AT_COLUMN = "updated_at";
    public static final String OUT_OF_AREA = "out_of_area";
    public static final String CREATED_AT = "created_at";
    public static final String TEAM_ID = "team_id";
    public static final String TEAM = "team";
    public static final String[] VACCINE_TABLE_COLUMNS = {ID_COLUMN, BASE_ENTITY_ID, PROGRAM_CLIENT_ID, NAME, CALCULATION, DATE, ANMID, LOCATION_ID, CHILD_LOCATION_ID, TEAM, TEAM_ID, SYNC_STATUS, HIA2_STATUS, UPDATED_AT_COLUMN, EVENT_ID, FORMSUBMISSION_ID, OUT_OF_AREA, CREATED_AT};
    public static final String UPDATE_TABLE_ADD_EVENT_ID_COL = "ALTER TABLE " + VACCINE_TABLE_NAME + " ADD COLUMN " + EVENT_ID + " VARCHAR;";
    public static final String EVENT_ID_INDEX = "CREATE INDEX " + VACCINE_TABLE_NAME + "_" + EVENT_ID + "_index ON " + VACCINE_TABLE_NAME + "(" + EVENT_ID + " COLLATE NOCASE);";
    public static final String UPDATE_TABLE_ADD_FORMSUBMISSION_ID_COL = "ALTER TABLE " + VACCINE_TABLE_NAME + " ADD COLUMN " + FORMSUBMISSION_ID + " VARCHAR;";
    public static final String FORMSUBMISSION_INDEX = "CREATE INDEX " + VACCINE_TABLE_NAME + "_" + FORMSUBMISSION_ID + "_index ON " + VACCINE_TABLE_NAME + "(" + FORMSUBMISSION_ID + " COLLATE NOCASE);";
    public static final String UPDATE_TABLE_ADD_OUT_OF_AREA_COL = "ALTER TABLE " + VACCINE_TABLE_NAME + " ADD COLUMN " + OUT_OF_AREA + " INTEGER;";
    public static final String UPDATE_TABLE_ADD_OUT_OF_AREA_COL_INDEX = "CREATE INDEX " + VACCINE_TABLE_NAME + "_" + OUT_OF_AREA + "_index ON " + VACCINE_TABLE_NAME + "(" + OUT_OF_AREA + " COLLATE NOCASE);";
    public static final String UPDATE_TABLE_ADD_HIA2_STATUS_COL = "ALTER TABLE " + VACCINE_TABLE_NAME + " ADD COLUMN " + HIA2_STATUS + " VARCHAR;";
    public static final String ALTER_ADD_CREATED_AT_COLUMN = "ALTER TABLE " + VACCINE_TABLE_NAME + " ADD COLUMN " + CREATED_AT + " DATETIME NULL ";
    public static final String UPDATE_TABLE_ADD_TEAM_COL = "ALTER TABLE " + VACCINE_TABLE_NAME + " ADD COLUMN " + TEAM + " VARCHAR;";
    public static final String UPDATE_TABLE_ADD_TEAM_ID_COL = "ALTER TABLE " + VACCINE_TABLE_NAME + " ADD COLUMN " + TEAM_ID + " VARCHAR;";
    public static final String UPDATE_TABLE_ADD_CHILD_LOCATION_ID_COL = "ALTER TABLE " + VACCINE_TABLE_NAME + " ADD COLUMN " + CHILD_LOCATION_ID + " VARCHAR;";
    private static final String TAG = VaccineRepository.class.getCanonicalName();
    private static final String VACCINE_SQL = "CREATE TABLE vaccines (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,base_entity_id VARCHAR NOT NULL,program_client_id VARCHAR NULL,name VARCHAR NOT NULL,calculation INTEGER,date DATETIME NOT NULL,anmid VARCHAR NULL,location_id VARCHAR NULL,sync_status VARCHAR, updated_at INTEGER NULL, UNIQUE(base_entity_id, program_client_id, name) ON CONFLICT IGNORE)";
    private static final String BASE_ENTITY_ID_INDEX = "CREATE INDEX " + VACCINE_TABLE_NAME + "_" + BASE_ENTITY_ID + "_index ON " + VACCINE_TABLE_NAME + "(" + BASE_ENTITY_ID + " COLLATE NOCASE);";
    private static final String UPDATED_AT_INDEX = "CREATE INDEX " + VACCINE_TABLE_NAME + "_" + UPDATED_AT_COLUMN + "_index ON " + VACCINE_TABLE_NAME + "(" + UPDATED_AT_COLUMN + ");";
    public static String HIA2_Within = "Within";
    public static String HIA2_Overdue = "Overdue";

    private CommonFtsObject commonFtsObject;
    private AlertService alertService;

    public VaccineRepository(CommonFtsObject commonFtsObject, AlertService alertService) {
        this.commonFtsObject = commonFtsObject;
        this.alertService = alertService;
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(VACCINE_SQL);
        database.execSQL(BASE_ENTITY_ID_INDEX);
        database.execSQL(UPDATED_AT_INDEX);
    }

    public static void migrateCreatedAt(SQLiteDatabase database) {
        try {
            String sql = "UPDATE " + VACCINE_TABLE_NAME +
                    " SET " + CREATED_AT + " = " +
                    " ( SELECT " + EventClientRepository.event_column.dateCreated.name() +
                    "   FROM " + EventClientRepository.Table.event.name() +
                    "   WHERE " + EventClientRepository.event_column.eventId
                    .name() + " = " + VACCINE_TABLE_NAME + "." + EVENT_ID +
                    "   OR " + EventClientRepository.event_column.formSubmissionId
                    .name() + " = " + VACCINE_TABLE_NAME + "." + FORMSUBMISSION_ID +
                    " ) " +
                    " WHERE " + CREATED_AT + " is null ";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public void add(Vaccine vaccine) {
        if (vaccine == null) {
            return;
        }

        try {

            vaccine.setHia2Status(null);

            if (StringUtils.isBlank(vaccine.getSyncStatus())) {
                vaccine.setSyncStatus(TYPE_Unsynced);
            }
            if (StringUtils.isBlank(vaccine.getFormSubmissionId())) {
                vaccine.setFormSubmissionId(generateRandomUUIDString());
            }

            if (vaccine.getUpdatedAt() == null) {
                vaccine.setUpdatedAt(Calendar.getInstance().getTimeInMillis());
            }

            SQLiteDatabase database = getWritableDatabase();
            if (vaccine.getId() == null) {
                Vaccine sameVaccine = findUnique(database, vaccine);
                if (sameVaccine != null) {
                    vaccine.setUpdatedAt(sameVaccine.getUpdatedAt());
                    vaccine.setId(sameVaccine.getId());
                    update(database, vaccine);
                } else {
                    if (vaccine.getCreatedAt() == null) {
                        vaccine.setCreatedAt(new Date());
                    }
                    vaccine.setId(database.insert(VACCINE_TABLE_NAME, null, createValuesFor(vaccine)));
                }
            } else {
                //mark the vaccine as unsynced for processing as an updated event
                vaccine.setSyncStatus(TYPE_Unsynced);
                update(database, vaccine);
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        updateFtsSearch(vaccine);
    }

    public void updateHia2Status(Vaccine vaccine, String hai2Status) {
        if (vaccine == null || vaccine.getId() == null) {
            return;
        }

        vaccine.setHia2Status(hai2Status);
        SQLiteDatabase database = getWritableDatabase();
        update(database, vaccine);
    }

    public void update(SQLiteDatabase database, Vaccine vaccine) {
        if (vaccine == null || vaccine.getId() == null) {
            return;
        }

        try {
            String idSelection = ID_COLUMN + " = ?";
            database.update(VACCINE_TABLE_NAME, createValuesFor(vaccine), idSelection,
                    new String[] {vaccine.getId().toString()});
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private ContentValues createValuesFor(Vaccine vaccine) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, vaccine.getId());
        values.put(BASE_ENTITY_ID, vaccine.getBaseEntityId());
        values.put(PROGRAM_CLIENT_ID, vaccine.getProgramClientId());
        values.put(NAME, vaccine.getName() != null ? addHyphen(vaccine.getName().toLowerCase()) : null);
        values.put(CALCULATION, vaccine.getCalculation());
        values.put(DATE, vaccine.getDate() != null ? vaccine.getDate().getTime() : null);
        values.put(ANMID, vaccine.getAnmId());
        values.put(LOCATION_ID, vaccine.getLocationId());
        values.put(TEAM, vaccine.getTeam());
        values.put(TEAM_ID, vaccine.getTeamId());
        values.put(CHILD_LOCATION_ID, vaccine.getChildLocationId());
        values.put(SYNC_STATUS, vaccine.getSyncStatus());
        values.put(HIA2_STATUS, vaccine.getHia2Status());
        values.put(UPDATED_AT_COLUMN, vaccine.getUpdatedAt());
        values.put(EVENT_ID, vaccine.getEventId());
        values.put(FORMSUBMISSION_ID, vaccine.getFormSubmissionId());
        values.put(OUT_OF_AREA, vaccine.getOutOfCatchment());
        values.put(CREATED_AT,
                vaccine.getCreatedAt() != null ? EventClientRepository.dateFormat.format(vaccine.getCreatedAt()) : null);
        return values;
    }

    public static String addHyphen(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replace(" ", "_");
        }
        return s;
    }

    public List<Vaccine> findUnSyncedBeforeTime(int minutes) {
        List<Vaccine> vaccines = new ArrayList<>();
        Cursor cursor = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, -minutes);

            Long time = calendar.getTimeInMillis();

            cursor = getReadableDatabase().query(VACCINE_TABLE_NAME, VACCINE_TABLE_COLUMNS,
                    UPDATED_AT_COLUMN + " < ? AND " + SYNC_STATUS + " = ? ", new String[] {time.toString(), TYPE_Unsynced},
                    null, null, null, null);
            vaccines = readAllVaccines(cursor);
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return vaccines;
    }

    private List<Vaccine> readAllVaccines(Cursor cursor) {
        List<Vaccine> vaccines = new ArrayList<>();

        try {

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String vaccineName = cursor.getString(cursor.getColumnIndex(NAME));
                    if (vaccineName != null) {
                        vaccineName = removeHyphen(vaccineName);
                    }

                    Date createdAt = null;
                    String dateCreatedString = cursor.getString(cursor.getColumnIndex(CREATED_AT));
                    if (StringUtils.isNotBlank(dateCreatedString)) {
                        try {
                            createdAt = EventClientRepository.dateFormat.parse(dateCreatedString);
                        } catch (ParseException e) {
                            Log.e(TAG, Log.getStackTraceString(e));
                        }
                    }
                    Vaccine vaccine = new Vaccine(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                            cursor.getString(cursor.getColumnIndex(BASE_ENTITY_ID)),
                            cursor.getString(cursor.getColumnIndex(PROGRAM_CLIENT_ID)),
                            vaccineName,
                            cursor.getInt(cursor.getColumnIndex(CALCULATION)),
                            new Date(cursor.getLong(cursor.getColumnIndex(DATE))),
                            cursor.getString(cursor.getColumnIndex(ANMID)),
                            cursor.getString(cursor.getColumnIndex(LOCATION_ID)),
                            cursor.getString(cursor.getColumnIndex(SYNC_STATUS)),
                            cursor.getString(cursor.getColumnIndex(HIA2_STATUS)),
                            cursor.getLong(cursor.getColumnIndex(UPDATED_AT_COLUMN)),
                            cursor.getString(cursor.getColumnIndex(EVENT_ID)),
                            cursor.getString(cursor.getColumnIndex(FORMSUBMISSION_ID)),
                            cursor.getInt(cursor.getColumnIndex(OUT_OF_AREA)),
                            createdAt
                    );

                    vaccine.setTeam(cursor.getString(cursor.getColumnIndex(TEAM)));
                    vaccine.setTeamId(cursor.getString(cursor.getColumnIndex(TEAM_ID)));
                    vaccine.setChildLocationId(cursor.getString(cursor.getColumnIndex(CHILD_LOCATION_ID)));

                    vaccines.add(vaccine);

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {

        } finally {
            cursor.close();
        }
        return vaccines;
    }

    public static String removeHyphen(String s) {
        if (StringUtils.isNotBlank(s)) {
            return s.replace("_", " ");
        }
        return s;
    }

    public List<Vaccine> findByEntityId(String entityId) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(VACCINE_TABLE_NAME, VACCINE_TABLE_COLUMNS,
                BASE_ENTITY_ID + " = ? " + COLLATE_NOCASE + " ORDER BY " + UPDATED_AT_COLUMN, new String[] {entityId}, null,
                null, null, null);
        return readAllVaccines(cursor);
    }

    public List<Vaccine> findLatestTwentyFourHoursByEntityId(String entityId) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.query(VACCINE_TABLE_NAME, VACCINE_TABLE_COLUMNS,
                BASE_ENTITY_ID + " = ? and (" + UPDATED_AT_COLUMN + "/1000 < strftime('%s',datetime('now','-1 day')))",
                new String[] {entityId}, null, null, null, null);
        return readAllVaccines(cursor);
    }

    public Vaccine findUnique(SQLiteDatabase database, Vaccine vaccine) {
        if (vaccine == null || (StringUtils.isBlank(vaccine.getFormSubmissionId()) && StringUtils
                .isBlank(vaccine.getEventId()))) {
            return null;
        }

        try {
            if (database == null) {
                database = getReadableDatabase();
            }

            String selection = null;
            String[] selectionArgs = null;
            if (StringUtils.isNotBlank(vaccine.getFormSubmissionId()) && StringUtils.isNotBlank(vaccine.getEventId())) {
                selection = FORMSUBMISSION_ID + " = ? " + COLLATE_NOCASE + " OR " + EVENT_ID + " = ? " + COLLATE_NOCASE;
                selectionArgs = new String[] {vaccine.getFormSubmissionId(), vaccine.getEventId()};
            } else if (StringUtils.isNotBlank(vaccine.getEventId())) {
                selection = EVENT_ID + " = ? " + COLLATE_NOCASE;
                selectionArgs = new String[] {vaccine.getEventId()};
            } else if (StringUtils.isNotBlank(vaccine.getFormSubmissionId())) {
                selection = FORMSUBMISSION_ID + " = ? " + COLLATE_NOCASE;
                selectionArgs = new String[] {vaccine.getFormSubmissionId()};
            }

            Cursor cursor = database.query(VACCINE_TABLE_NAME, VACCINE_TABLE_COLUMNS, selection, selectionArgs, null, null,
                    ID_COLUMN + " DESC ", null);
            List<Vaccine> vaccines = readAllVaccines(cursor);
            if (!vaccines.isEmpty()) {
                return vaccines.get(0);
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return null;

    }

    public List<Vaccine> findWithNullHia2Status() {
        List<Vaccine> vaccines = new ArrayList<>();
        Cursor cursor = null;
        try {

            cursor = getReadableDatabase()
                    .query(VACCINE_TABLE_NAME, VACCINE_TABLE_COLUMNS, HIA2_STATUS + " is null", null, null, null, null,
                            null);
            vaccines = readAllVaccines(cursor);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return vaccines;
    }

    public void deleteVaccine(Long caseId) {
        try {
            Vaccine vaccine = find(caseId);
            if (vaccine != null) {
                getWritableDatabase().delete(VACCINE_TABLE_NAME, ID_COLUMN + "= ?", new String[] {caseId.toString()});

                updateFtsSearch(vaccine.getBaseEntityId(), vaccine.getName());
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public Vaccine find(Long caseId) {
        Vaccine vaccine = null;
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase()
                    .query(VACCINE_TABLE_NAME, VACCINE_TABLE_COLUMNS, ID_COLUMN + " = ?", new String[] {caseId.toString()},
                            null, null, null, null);
            List<Vaccine> vaccines = readAllVaccines(cursor);
            if (!vaccines.isEmpty()) {
                vaccine = vaccines.get(0);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return vaccine;
    }

    public void updateFtsSearch(String entityId, String vaccineName_) {
        String vaccineName = vaccineName_;
        try {
            if (commonFtsObject != null && alertService() != null) {
                if (vaccineName != null) {
                    vaccineName = removeHyphen(vaccineName);
                }

                String scheduleName = commonFtsObject.getAlertScheduleName(vaccineName);
                if (StringUtils.isNotBlank(entityId) && StringUtils.isNotBlank(scheduleName)) {
                    Alert alert = alertService().findByEntityIdAndScheduleName(entityId, scheduleName);
                    alertService().updateFtsSearch(alert, true);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public AlertService alertService() {
        if (alertService == null) {
            alertService = ImmunizationLibrary.getInstance().context().alertService();
        }
        return alertService;
    }

    public void close(Long caseId) {
        try {
            ContentValues values = new ContentValues();
            values.put(SYNC_STATUS, TYPE_Synced);
            getWritableDatabase().update(VACCINE_TABLE_NAME, values, ID_COLUMN + " = ?", new String[] {caseId.toString()});
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    //-----------------------
    // FTS methods
    public void updateFtsSearch(Vaccine vaccine) {
        try {
            if (commonFtsObject != null && alertService() != null) {
                String entityId = vaccine.getBaseEntityId();
                String vaccineName = vaccine.getName();
                if (vaccineName != null) {
                    vaccineName = removeHyphen(vaccineName);
                }
                String scheduleName = commonFtsObject.getAlertScheduleName(vaccineName);

                String bindType = commonFtsObject.getAlertBindType(scheduleName);

                if (StringUtils.isNotBlank(bindType) && StringUtils.isNotBlank(scheduleName) && StringUtils
                        .isNotBlank(entityId)) {
                    String field = addHyphen(scheduleName);
                    // update vaccine status
                    alertService().updateFtsSearchInACR(bindType, entityId, field, AlertStatus.complete.value());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

    }
}
