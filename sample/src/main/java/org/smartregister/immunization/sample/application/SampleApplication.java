package org.smartregister.immunization.sample.application;

import android.util.Log;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.VaccineSchedule;
import org.smartregister.immunization.domain.jsonmapping.Vaccine;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.sample.BuildConfig;
import org.smartregister.immunization.sample.repository.SampleRepository;
import org.smartregister.immunization.sample.util.VaccineDuplicate;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.HashMap;
import java.util.List;

import static org.smartregister.util.Log.logError;

/**
 * Created by keyman on 01/08/2017.
 */
public class SampleApplication extends DrishtiApplication {

    public static final String TAG = SampleApplication.class.getCanonicalName();

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = Context.getInstance();

        context.updateApplicationContext(getApplicationContext());

        //Initialize Modules
        CoreLibrary.init(context);
        ImmunizationLibrary.init(context, getRepository(), null, BuildConfig.VERSION_CODE, BuildConfig.DATABASE_VERSION);

        // This method updates some of the hardcoded vaccine definitions in VaccineRepo.Vaccine enum
        updateHardcodedVaccineDefinition();

        initOfflineSchedules();
    }

    private void updateHardcodedVaccineDefinition() {
        VaccineRepo.Vaccine[] vaccines = ImmunizationLibrary.getInstance().getVaccines(IMConstants.VACCINE_TYPE.CHILD);

        HashMap<String, VaccineDuplicate> replacementVaccines = new HashMap<>();
        replacementVaccines.put("MR 2", new VaccineDuplicate("MR 2", VaccineRepo.Vaccine.mr1, -1, 548, 183, "child"));

        for (VaccineRepo.Vaccine vaccine: vaccines) {
            if (replacementVaccines.containsKey(vaccine.display())) {
                VaccineDuplicate vaccineDuplicate = replacementVaccines.get(vaccine.display());

                vaccine.setCategory(vaccineDuplicate.category());
                vaccine.setExpiryDays(vaccineDuplicate.expiryDays());
                vaccine.setMilestoneGapDays(vaccineDuplicate.milestoneGapDays());
                vaccine.setPrerequisite(vaccineDuplicate.prerequisite());
                vaccine.setPrerequisiteGapDays(vaccineDuplicate.prerequisiteGapDays());
            }
        }

        ImmunizationLibrary.getInstance().setVaccines(vaccines,IMConstants.VACCINE_TYPE.CHILD);
    }

    public static synchronized SampleApplication getInstance() {
        return (SampleApplication) mInstance;
    }

    @Override
    public Repository getRepository() {
        try {
            if (repository == null) {
                repository = new SampleRepository(getInstance().getApplicationContext(), context);
            }
        } catch (UnsatisfiedLinkError e) {
            logError("Error on getRepository: " + e);

        }
        return repository;
    }


    @Override
    public void logoutCurrentUser() {

    }

    private void initOfflineSchedules() {
        try {
            List<VaccineGroup> childVaccines = VaccinatorUtils.getSupportedVaccines(this);
            List<Vaccine> specialVaccines = VaccinatorUtils.getSpecialVaccines(this);
            List<Vaccine> vaccineList = VaccinatorUtils.getVaccineFromVaccineConfigFile(this.getApplicationContext(), VaccinatorUtils.special_vaccines_file);
            VaccineSchedule.init(childVaccines, specialVaccines, "child");

            List<VaccineGroup> womanVaccines = VaccinatorUtils.getSupportedWomanVaccines(this);
            VaccineSchedule.init(womanVaccines, null, "woman");
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }
}
