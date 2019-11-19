package org.smartregister.immunization;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.jsonmapping.Vaccine;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.repository.RecurringServiceTypeRepository;
import org.smartregister.immunization.repository.VaccineNameRepository;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.repository.VaccineTypeRepository;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.Repository;
import org.smartregister.util.AppProperties;
import org.smartregister.util.AssetHandler;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    private VaccineRepo.Vaccine[] vaccines = VaccineRepo.Vaccine.values();

    private HashMap<String, String> vaccineGrouping = new HashMap<>();

    private ImmunizationLibrary(Context context, Repository repository, CommonFtsObject commonFtsObject,
                                int applicationVersion, int databaseVersion) {
        this.repository = repository;
        this.context = context;
        this.commonFtsObject = commonFtsObject;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
    }

    public static void init(Context context, Repository repository, CommonFtsObject commonFtsObject, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new ImmunizationLibrary(context, repository, commonFtsObject, applicationVersion, databaseVersion);
            allowExpiredVaccineEntry = ImmunizationLibrary.getInstance().getProperties().hasProperty(IMConstants.APP_PROPERTIES.VACCINE_EXPIRED_ENTRY_ALLOW) && ImmunizationLibrary.getInstance().getProperties().getPropertyBoolean(IMConstants.APP_PROPERTIES.VACCINE_EXPIRED_ENTRY_ALLOW);

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

    public AppProperties getProperties() {
        return ImmunizationLibrary.getInstance().context().getAppProperties();
    }

    public void setVaccines(VaccineRepo.Vaccine[] vaccines) {
        this.vaccines = vaccines;
    }

    public VaccineRepo.Vaccine[] getVaccines() {
        return vaccines;
    }

    @NonNull
    public HashMap<String, String> getVaccineGroupings(@NonNull android.content.Context context) {
        if (vaccineGrouping.isEmpty()) {
            List<VaccineGroup> vaccinesJsonMapping = VaccinatorUtils.getSupportedVaccines(context);

            if (vaccinesJsonMapping != null && vaccinesJsonMapping.size() > 0) {
                for (VaccineGroup vaccineGroup : vaccinesJsonMapping) {
                    String groupName = vaccineGroup.name;

                    for (Vaccine vaccine : vaccineGroup.vaccines) {
                        String shortVaccineName = vaccine.getName()
                                .trim()
                                .replace(" ", "")
                                .toLowerCase();

                        if (!TextUtils.isEmpty(shortVaccineName)) {
                            if (vaccine.getVaccineSeparator() != null && shortVaccineName.contains(vaccine.getVaccineSeparator().trim())) {
                                String[] individualVaccines = shortVaccineName.split(vaccine.getVaccineSeparator().trim());

                                for (String individualVaccine : individualVaccines) {
                                    if (!TextUtils.isEmpty(individualVaccine)) {
                                        vaccineGrouping.put(individualVaccine, groupName);
                                    }
                                }
                            } else {
                                vaccineGrouping.put(shortVaccineName, groupName);
                            }
                        }
                    }
                }
            }
        }

        return vaccineGrouping;
    }

    public boolean isAllowExpiredVaccineEntry() {
        return allowExpiredVaccineEntry;
    }
}
