package org.smartregister.immunization;

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

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by keyman on 31/07/17.
 */
public class ImmunizationLibrary {

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

    private static ImmunizationLibrary instance;

    private Map<String, Object> jsonMap = new HashMap<>();

    public static void init(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new ImmunizationLibrary(context, repository, commonFtsObject, applicationVersion, databaseVersion);
        }
    }

    public static ImmunizationLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call " + ImmunizationLibrary.class.getName() + ".init method in the onCreate method of your Application class ");
        }
        return instance;
    }

    private ImmunizationLibrary(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        this.repository = repository;
        this.context = context;
        this.commonFtsObject = commonFtsObject;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
    }

    public <T> T assetJsonToJava(String fileName, Class<T> clazz, Type type) {
        return AssetHandler.assetJsonToJava(jsonMap, context.applicationContext(), fileName, clazz, type);
    }

    public Repository getRepository() {
        return repository;
    }


    public EventClientRepository eventClientRepository() {
        if (eventClientRepository == null) {
            eventClientRepository = new EventClientRepository(getRepository());
        }

        return eventClientRepository;
    }

    public VaccineRepository vaccineRepository() {
        if (vaccineRepository == null) {
            vaccineRepository = new VaccineRepository(getRepository(), commonFtsObject(), context.alertService());
        }
        return vaccineRepository;
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

    public VaccineNameRepository vaccineNameRepository() {
        if (vaccineNameRepository == null) {
            vaccineNameRepository = new VaccineNameRepository(getRepository(), commonFtsObject(), context().alertService());
        }
        return vaccineNameRepository;
    }

    public Context context() {
        return context;
    }

    private CommonFtsObject commonFtsObject() {
        return commonFtsObject;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }
}
