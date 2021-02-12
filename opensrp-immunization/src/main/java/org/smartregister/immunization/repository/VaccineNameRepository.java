package org.smartregister.immunization.repository;

import android.content.ContentValues;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.domain.VaccineName;
import org.smartregister.repository.BaseRepository;
import org.smartregister.service.AlertService;

import java.util.ArrayList;
import java.util.List;

public class VaccineNameRepository extends BaseRepository {
    public static final String VACCINE_Names_TABLE_NAME = "Vaccines_names";
    public static final String ID_COLUMN = "_id";
    public static final String NAME = "name";
    public static final String VACCINE_TYPE_ID = "vaccine_type_id";
    public static final String REFERENCE_VACCINE_ID = "reference_vaccine_id";
    public static final String DUE_DAYS = "due_days";
    public static final String CLIENT_TYPE = "Client_type";
    public static final String DOSE_NO = "Dose_no";
    public static final String[] VACCINE_Names_TABLE_COLUMNS = {ID_COLUMN, NAME, VACCINE_TYPE_ID, REFERENCE_VACCINE_ID, DUE_DAYS, CLIENT_TYPE, DOSE_NO};
    private static final String TAG = VaccineTypeRepository.class.getCanonicalName();
    private static final String VACCINE_Names_SQL = "CREATE TABLE Vaccines_names (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,name VARCHAR NOT NULL,vaccine_type_id VARCHAR NULL,reference_vaccine_id VARCHAR NULL,due_days VARCHAR ,Client_type VARCHAR ,Dose_no VARCHAR)";
    private CommonFtsObject commonFtsObject;
    private AlertService alertService;

    public VaccineNameRepository(CommonFtsObject commonFtsObject, AlertService alertService) {
        this.commonFtsObject = commonFtsObject;
        this.alertService = alertService;
    }

    public static void createTable(SQLiteDatabase database) {
        database.execSQL(VACCINE_Names_SQL);
    }

    public void add(VaccineName vaccineName) {
        if (vaccineName == null) {
            return;
        }


        SQLiteDatabase database = getWritableDatabase();
        if (vaccineName.getId() == null) {
            vaccineName.setId(database.insert(VACCINE_Names_TABLE_NAME, null, createValuesFor(vaccineName)));
        } else {
            //mark the vaccine as unsynced for processing as an updated event

            String idSelection = ID_COLUMN + " = ?";
            database.update(VACCINE_Names_SQL, createValuesFor(vaccineName), idSelection,
                    new String[] {vaccineName.getId().toString()});
        }
    }


    //    public List<Vaccine> findByEntityId(String entityId) {
    //        SQLiteDatabase database = getReadableDatabase();
    //        Cursor cursor = database.query(VACCINE_TABLE_NAME, VACCINE_TABLE_COLUMNS, BASE_ENTITY_ID + " = ? ORDER BY " + UPDATED_AT_COLUMN, new String[]{entityId}, null, null, null, null);
    //        return readAllVaccines(cursor);
    //    }


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

    private ContentValues createValuesFor(VaccineName vaccineName) {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, vaccineName.getId());
        values.put(NAME, vaccineName.getName());
        values.put(VACCINE_TYPE_ID, vaccineName.getVaccine_type_id());
        values.put(REFERENCE_VACCINE_ID, vaccineName.getReference_vaccine_id());
        values.put(DUE_DAYS, vaccineName.getDue_days());
        values.put(CLIENT_TYPE, vaccineName.getClient_type());
        values.put(DUE_DAYS, vaccineName.getDue_days());
        values.put(DOSE_NO, vaccineName.getDose_no());
        return values;
    }

    private List<VaccineName> readAllVaccines(Cursor cursor) {
        List<VaccineName> vaccines = new ArrayList<>();

        try {

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    vaccines.add(
                            new VaccineName(cursor.getLong(cursor.getColumnIndex(ID_COLUMN)),
                                    cursor.getString(cursor.getColumnIndex(NAME)),
                                    cursor.getString(cursor.getColumnIndex(VACCINE_TYPE_ID)),
                                    cursor.getString(cursor.getColumnIndex(DUE_DAYS)),
                                    cursor.getString(cursor.getColumnIndex(REFERENCE_VACCINE_ID)),
                                    cursor.getString(cursor.getColumnIndex(CLIENT_TYPE)),
                                    cursor.getString(cursor.getColumnIndex(DOSE_NO))
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


}
