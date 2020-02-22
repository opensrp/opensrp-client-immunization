package org.smartregister.immunization;

import android.support.annotation.NonNull;

import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineNameRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.repository.VaccineTypeRepository;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.Utils;
import org.smartregister.immunization.util.VaccineCache;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;
import org.smartregister.util.AppProperties;
import org.smartregister.util.AssetHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private static boolean allowExpiredVaccineEntry;
    private static Map<String, VaccineCache> vaccineCacheMap = new HashMap<>();

    public static List<String> COMBINED_VACCINES = new ArrayList<>();
    public static Map<String, String> COMBINED_VACCINES_MAP = new HashMap<>();

    private long vaccineSyncTime = -1;

    private ImmunizationLibrary(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        this.repository = repository;
        this.context = context;
        this.commonFtsObject = commonFtsObject;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
    }

    public static void init(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new ImmunizationLibrary(context, repository, commonFtsObject, applicationVersion, databaseVersion);

            allowExpiredVaccineEntry = instance.getProperties().hasProperty(IMConstants.APP_PROPERTIES.VACCINE_EXPIRED_ENTRY_ALLOW) && instance.getProperties().getPropertyBoolean(IMConstants.APP_PROPERTIES.VACCINE_EXPIRED_ENTRY_ALLOW);

            Utils.processVaccineCache(context.applicationContext(), IMConstants.VACCINE_TYPE.CHILD);
            //Utils.processVaccineCache(context.applicationContext(), IMConstants.VACCINE_TYPE.WOMAN);
        }
    }

    public <T> T assetJsonToJava(String fileName, Class<T> clazz, Type type) {
        return AssetHandler.assetJsonToJava(jsonMap, context.applicationContext(), fileName, clazz, type);
    }

    public static <T> T assetJsonToJava(Map<String, Object> jsonMap, android.content.Context context, String fileName, Class<T> clazz, Type type) {
        return AssetHandler.assetJsonToJava(jsonMap, context, fileName, clazz, type);
    }

    public EventClientRepository eventClientRepository() {
        if (eventClientRepository == null) {
            eventClientRepository = new EventClientRepository();
        }

        return eventClientRepository;
    }

    public Repository getRepository() {
        return repository;
    }

    public VaccineRepository vaccineRepository() {
        if (vaccineRepository == null) {
            vaccineRepository = new VaccineRepository(commonFtsObject(), context.alertService());
        }
        return vaccineRepository;
    }

    private CommonFtsObject commonFtsObject() {
        return commonFtsObject;
    }

    public RecurringServiceTypeRepository recurringServiceTypeRepository() {
        if (recurringServiceTypeRepository == null) {
            recurringServiceTypeRepository = new RecurringServiceTypeRepository();
        }
        return recurringServiceTypeRepository;
    }

    public RecurringServiceRecordRepository recurringServiceRecordRepository() {
        if (recurringServiceRecordRepository == null) {
            recurringServiceRecordRepository = new RecurringServiceRecordRepository();
        }
        return recurringServiceRecordRepository;
    }

    public VaccineTypeRepository vaccineTypeRepository() {
        if (vaccineTypeRepository == null) {
            vaccineTypeRepository = new VaccineTypeRepository(commonFtsObject(), context().alertService());
        }
        return vaccineTypeRepository;
    }

    public Context context() {
        return context;
    }

    public VaccineNameRepository vaccineNameRepository() {
        if (vaccineNameRepository == null) {
            vaccineNameRepository = new VaccineNameRepository(commonFtsObject(), context().alertService());
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

    public AppProperties getProperties() {
        return ImmunizationLibrary.getInstance().context().getAppProperties();
    }

    public void setVaccines(VaccineRepo.Vaccine[] vaccines) {
        this.vaccineCacheMap.get(IMConstants.VACCINE_TYPE.CHILD).vaccines = vaccines;
    }

    public VaccineRepo.Vaccine[] getVaccines() {
        return this.vaccineCacheMap.get(IMConstants.VACCINE_TYPE.CHILD).vaccines;
    }

    public boolean isAllowExpiredVaccineEntry() {
        return allowExpiredVaccineEntry;
    }

    public static Map<String, VaccineCache> getVaccineCacheMap() {
        return vaccineCacheMap;
    }

    public boolean isExpiredVaccineCardRed() {
        return getProperties().hasProperty(IMConstants.APP_PROPERTIES.EXPIRED_CARD_AS_RED) && instance.getProperties().getPropertyBoolean(IMConstants.APP_PROPERTIES.EXPIRED_CARD_AS_RED);
    }

    public long getVaccineSyncTime() {
        if (vaccineSyncTime == -1) {
            setVaccineSyncTime(BuildConfig.VACCINE_SYNC_TIME);
        }

        return vaccineSyncTime;
    }

    public void setVaccineSyncTime(int vaccineSyncTime) {
        setGrowthMonitoringSyncTime(vaccineSyncTime, TimeUnit.HOURS);
    }

    public void setGrowthMonitoringSyncTime(int growthMonitoringSyncTime, @NonNull TimeUnit timeUnit) {
        this.vaccineSyncTime = timeUnit.toMinutes(growthMonitoringSyncTime);
    }

}
