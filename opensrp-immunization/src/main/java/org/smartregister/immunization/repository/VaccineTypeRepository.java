package org.smartregister.immunization.repository;

import android.content.ContentValues;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.domain.VaccineType;
import org.smartregister.repository.BaseRepository;
import org.smartregister.service.AlertService;

import java.util.ArrayList;
import java.util.List;

public class VaccineTypeRepository extends BaseRepository {
    public static final String VACCINE_Types_TABLE_NAME = "vaccine_types";
    public static final String ID_COLUMN = "_id";
    public static final String DOSES = "doses";
    public static final String NAME = "name";
    public static final String OPENMRS_PARENT_ENTITIY_ID = "openmrs_parent_entity_id";
    public static final String OPENMRS_DATE_CONCEPT_ID = "openmrs_date_concept_id";
    public static final String OPENMRS_DOSE_CONCEPT_ID = "openmrs_dose_concept_id";
    public static final String[] VACCINE_Types_TABLE_COLUMNS = {ID_COLUMN, DOSES, NAME, OPENMRS_PARENT_ENTITIY_ID, OPENMRS_DATE_CONCEPT_ID, OPENMRS_DOSE_CONCEPT_ID};
    private static final String TAG = VaccineNameRepository.class.getCanonicalName();
    private static final String VACCINE_Types_SQL = "CREATE TABLE vaccine_types (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,doses INTEGER,name VARCHAR NOT NULL,openmrs_parent_entity_id VARCHAR NULL,openmrs_date_concept_id VARCHAR NULL,openmrs_dose_concept_id VARCHAR)";
    private CommonFtsObject commonFtsObject;
    private AlertService alertService;

    public VaccineTypeRepository(CommonFtsObject commonFtsObject, AlertService alertService) {
        this.commonFtsObject = commonFtsObject;
        this.alertService = alertService;
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(VACCINE_Types_SQL);
    }

    public void add(VaccineType vaccineType, SQLiteDatabase database_) {
        SQLiteDatabase database = database_;
        if (vaccineType == null) {
            return;
        }

        if (database == null) {
            database = getWritableDatabase();
        }

        if (vaccineType.getId() == null) {
            vaccineType.setId(database.insert(VACCINE_Types_TABLE_NAME, null, createValuesFor(vaccineType)));
        } else {
            //mark the vaccine as unsynced for processing as an updated event

            String idSelection = ID_COLUMN + " = ?";
            database.update(VACCINE_Types_TABLE_NAME, createValuesFor(vaccineType), idSelection,
                    new String[] {vaccineType.getId().toString()});
        }
    }

    private ContentValues createValuesFor(VaccineType vaccineType) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, vaccineType.getId());
        values.put(NAME, vaccineType.getName());
        values.put(DOSES, vaccineType.getDoses());
        values.put(OPENMRS_DATE_CONCEPT_ID, vaccineType.getOpenmrs_date_concept_id());
        values.put(OPENMRS_DOSE_CONCEPT_ID, vaccineType.getOpenmrs_dose_concept_id());
        values.put(OPENMRS_PARENT_ENTITIY_ID, vaccineType.getOpenmrs_parent_entity_id());
        return values;
    }

    public List<VaccineType> getAllVaccineTypes(SQLiteDatabase database_) {
        SQLiteDatabase database = database_;
        if (database == null) {
            database = getReadableDatabase();
        }

        Cursor cursor = database
                .query(VACCINE_Types_TABLE_NAME, VACCINE_Types_TABLE_COLUMNS, null, null, null, null, null, null);
        return readAllVaccines(cursor);
    }

    private List<VaccineType> readAllVaccines(Cursor cursor) {
        List<VaccineType> vaccines = new ArrayList<>();

        try {

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    vaccines.add(
                            new VaccineType(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                                    cursor.getInt(cursor.getColumnIndex(DOSES)),
                                    cursor.getString(cursor.getColumnIndex(NAME)),
                                    cursor.getString(cursor.getColumnIndex(OPENMRS_PARENT_ENTITIY_ID)),
                                    cursor.getString(cursor.getColumnIndex(OPENMRS_DATE_CONCEPT_ID)),
                                    cursor.getString(cursor.getColumnIndex(OPENMRS_DOSE_CONCEPT_ID))
                            )
                    );

                    cursor.moveToNext();
                }
            }
        } catch (Exception e) {

        } finally {
            cursor.close();
        }
        return vaccines;
    }


    //    public void deleteVaccine(Long caseId) {
    //        Vaccine vaccine = find(caseId);
    //        if(vaccine != null) {
    //            getWritableDatabase().delete(VACCINE_TABLE_NAME, ID_COLUMN + "= ?", new String[]{caseId.toString()});
    //
    //            updateFtsSearch(vaccine.getBaseEntityId(), vaccine.getName());
    //        }
    //    }

    //    public void close(Long caseId) {
    //        ContentValues values = new ContentValues();
    //        values.put(SYNC_STATUS, TYPE_Synced);
    //        getWritableDatabase().update(VACCINE_TABLE_NAME, values, ID_COLUMN + " = ?", new String[]{caseId.toString()});
    //    }

    public int getDosesPerVial(String name) {
        int dosespervaccine = 1;
        ArrayList<VaccineType> vaccineTypes = (ArrayList<VaccineType>) findIDByName(name);
        for (int i = 0; i < vaccineTypes.size(); i++) {
            dosespervaccine = vaccineTypes.get(0).getDoses();
        }
        return dosespervaccine;
    }

    public List<VaccineType> findIDByName(String Name) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database
                .query(VACCINE_Types_TABLE_NAME, VACCINE_Types_TABLE_COLUMNS, NAME + " = ? ", new String[] {Name}, null,
                        null, null, null);
        return readAllVaccines(cursor);
    }
}
