package org.smartregister.immunization;

import android.content.res.AssetManager;
import android.util.Log;

import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineNameRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.repository.VaccineTypeRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.Utils;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Created by keyman on 31/07/17.
 */
public class ImmunizationLibrary {

    private static ImmunizationLibrary instance;
    private final Repository repository;
    private final Context context;
    private EventClientRepository eventClientRepository;
    private VaccineRepository vaccineRepository;
    private RecurringServiceRecordRepository recurringServiceRecordRepository;
    private RecurringServiceTypeRepository recurringServiceTypeRepository;
    private VaccineTypeRepository vaccineTypeRepository;
    private VaccineNameRepository vaccineNameRepository;
    private CommonFtsObject commonFtsObject;
    private int applicationVersion;
    private int databaseVersion;
    private Map<String, Object> jsonMap = new HashMap<>();
    public Properties properties;

    private ImmunizationLibrary(Context context, Repository repository, CommonFtsObject commonFtsObject,
                                int applicationVersion, int databaseVersion) {
        this.repository = repository;
        this.context = context;
        this.commonFtsObject = commonFtsObject;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;

        properties = new Properties();
        try {
            AssetManager assetManager = context.applicationContext().getAssets();
            InputStream inputStream = assetManager.open("app.properties");
            properties.load(inputStream);
        } catch (Exception e) {
            Log.e(Utils.class.getCanonicalName(), e.getMessage(), e);
        }
    }

    public static void init(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion,
                            int databaseVersion) {
        if (instance == null) {
            instance = new ImmunizationLibrary(context, repository, commonFtsObject, applicationVersion, databaseVersion);
        }
    }

    public <T> T assetJsonToJava(String fileName, Class<T> clazz, Type type) {
        return AssetHandler.assetJsonToJava(jsonMap, context.applicationContext(), fileName, clazz, type);
    }

    public EventClientRepository eventClientRepository() {
        if (eventClientRepository == null) {
            eventClientRepository = new EventClientRepository(getRepository());
        }

        return eventClientRepository;
    }

    public Repository getRepository() {
        return repository;
    }

    public VaccineRepository vaccineRepository() {
        if (vaccineRepository == null) {
            vaccineRepository = new VaccineRepository(getRepository(), commonFtsObject(), context.alertService());
        }
        return vaccineRepository;
    }

    private CommonFtsObject commonFtsObject() {
        return commonFtsObject;
    }

    public RecurringServiceTypeRepository recurringServiceTypeRepository() {
        if (recurringServiceTypeRepository == null) {
            recurringServiceTypeRepository = new RecurringServiceTypeRepository(getRepository());
        }
        return recurringServiceTypeRepository;
    }

    public RecurringServiceRecordRepository recurringServiceRecordRepository() {
        if (recurringServiceRecordRepository == null) {
            recurringServiceRecordRepository = new RecurringServiceRecordRepository(getRepository());
        }
        return recurringServiceRecordRepository;
    }

    public VaccineTypeRepository vaccineTypeRepository() {
        if (vaccineTypeRepository == null) {
            vaccineTypeRepository = new VaccineTypeRepository(getRepository(), commonFtsObject(), context().alertService());
        }
        return vaccineTypeRepository;
    }

    public Context context() {
        return context;
    }

    public VaccineNameRepository vaccineNameRepository() {
        if (vaccineNameRepository == null) {
            vaccineNameRepository = new VaccineNameRepository(getRepository(), commonFtsObject(), context().alertService());
        }
        return vaccineNameRepository;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public Locale getLocale() {
        return ImmunizationLibrary.getInstance().context().applicationContext().getResources().getConfiguration().locale;
    }

    public static ImmunizationLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call " + ImmunizationLibrary.class
                    .getName() + ".init method in the onCreate method of your Application class ");
        }
        return instance;
    }

    public Map<String, Object> getVaccinesConfigJsonMap() {
        return jsonMap;
    }
}
