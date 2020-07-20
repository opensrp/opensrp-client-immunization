package org.smartregister.immunization.util;

import android.app.Activity;
import android.content.res.Configuration;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.CoreLibrary;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.GroupVaccineCount;
import org.smartregister.immunization.domain.jsonmapping.Vaccine;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by vkaruri on 29/03/2018.
 */

public class Utils {
    public static double calculateDialogWidthFactor(Activity activity) {
        double widthFactor = 0.7;
        int screenSize = activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if (screenSize >= Configuration.SCREENLAYOUT_SIZE_NORMAL && screenSize < Configuration.SCREENLAYOUT_SIZE_LARGE) {
            widthFactor = 0.9;
        }
        return widthFactor;
    }

    /**
     * Returns an offset with the relaxation value appended. Offsets can look like: "+5y,3m,2d" : Plus 5 years, 3 months, and 2 days "-2d" : Minus 2 days
     *
     * @param offset original offset value
     * @return the offset with the relaxation appended e.g. +5y with a relaxation of 3days gives +5y,-3d
     */
    public static String updateRelaxationDays(String offset) {
        String newOffset = offset;
        try {
            //Vaccine Relaxation Logic
            String relaxationsDays = ImmunizationLibrary.getInstance().getProperties().getProperty(IMConstants.APP_PROPERTIES.VACCINE_RELAXATION_DAYS);
            if (relaxationsDays != null) {

                String[] tokens = offset.split(",");
                boolean foundDay = false;
                for (int i = 0; i < tokens.length; i++) {

                    char lastCharacter = tokens[i].trim().charAt(tokens[i].length() - 1);
                    if (lastCharacter == 'd') {
                        String suffix = tokens[i].substring(tokens[i].length() - 1);
                        String mid = tokens[i].substring(0, tokens[i].length() - 1);
                        tokens[i] = (Integer.valueOf(mid) - Integer.valueOf(relaxationsDays)) + suffix;
                        foundDay = true;
                    }
                }

                char lastCharacter = offset.trim().charAt(offset.length() - 1);
                if (foundDay) {
                    newOffset = StringUtils.join(tokens, ",");
                } else if (lastCharacter == 'm' || lastCharacter == 'y') {
                    newOffset = offset + ",-" + relaxationsDays + "d";
                }

                newOffset = (newOffset.charAt(0) != '-' && newOffset.charAt(0) != '+') ? '+' + newOffset : newOffset;
            }
        } catch (Exception e) {
            Log.e(Utils.class.getCanonicalName(), e.getMessage());
        }
        return newOffset;
    }

    @NonNull
    public static String getGroupName(VaccineRepo.Vaccine vaccine, String category) {
        if (vaccine != null) {

            String groupName = ImmunizationLibrary.getInstance().getVaccineCacheMap().get(category).reverseLookupGroupMap.get(VaccinatorUtils.cleanVaccineName(vaccine.display()).toLowerCase(Locale.ENGLISH));
            if (groupName != null) {
                return groupName;
            }
        }

        return "";
    }

    public static void processVaccineCache(@NonNull android.content.Context context, String category) {
        try {
            if (ImmunizationLibrary.getInstance().getVaccineCacheMap().get(category) == null) {
                ImmunizationLibrary.getInstance().getVaccineCacheMap().put(category, new VaccineCache());

                List<VaccineGroup> vaccinesJsonMapping = VaccinatorUtils.getSupportedVaccinesByCategory(context, category);

                if (vaccinesJsonMapping != null && vaccinesJsonMapping.size() > 0) {
                    for (VaccineGroup vaccineGroup : vaccinesJsonMapping) {
                        processVaccineGroup(vaccineGroup, category);
                    }

                    //Set vaccines Array
                    ImmunizationLibrary.getInstance().getVaccineCacheMap().get(category).vaccines = ImmunizationLibrary.getInstance().getVaccineCacheMap().get(category).vaccineRepo.toArray(new VaccineRepo.Vaccine[]{});//Reset globally

                } else {
                    //Eject from map if no values found. Will allow reprocessing if invoked later
                    ImmunizationLibrary.getInstance().getVaccineCacheMap().remove(category);
                    Log.e(Utils.class.getCanonicalName(), "No such vaccine configuration file found for category");
                }
            }
        } catch (Exception e) {
            Log.e(Utils.class.getCanonicalName(), e.getMessage());
        }
    }

    private static void processVaccineGroup(VaccineGroup vaccineGroup, String category) {

        // Add BCG 2 and other special vaccines to the birth group
        if (vaccineGroup.id.equals("Birth") && category.equals(IMConstants.VACCINE_TYPE.CHILD)) {
            List<Vaccine> vaccinesList = VaccinatorUtils.getSpecialVaccines(ImmunizationLibrary.getInstance().context().applicationContext());

            for (Vaccine vaccine: vaccinesList) {
                if (vaccineGroup.vaccines == null) {
                    vaccineGroup.vaccines = new ArrayList<>();
                }

                vaccineGroup.vaccines.add(vaccine);
            }
        }

        for (Vaccine vaccine : vaccineGroup.vaccines) {

            processVaccineGroupCore(vaccineGroup, vaccine, category);


        }
    }

    private static void processVaccineGroupCore(VaccineGroup vaccineGroup, Vaccine vaccine, String category) {
        String vaccineName = VaccinatorUtils.cleanVaccineName(vaccine.getName());

        if (vaccine.getVaccineSeparator() != null && vaccineName.contains(vaccine.getVaccineSeparator().trim())) {

            processCombinedVaccines(vaccine);

            String[] individualVaccines = vaccineName.split(vaccine.getVaccineSeparator().trim());

            for (String individualVaccine : individualVaccines) {
                vaccine.setName(individualVaccine);
                processVaccineGroupCore(vaccineGroup, vaccine, category);


            }

        } else {

            ImmunizationLibrary.getInstance().getVaccineCacheMap().get(category).reverseLookupGroupMap.put(vaccineName, vaccineGroup.name);

            VaccineRepo.Vaccine repoVaccine = VaccineRepo.getVaccineEnumFromValue(vaccineName);

            repoVaccine.setCategory(category);
            repoVaccine.setExpiryDays(VaccinatorUtils.getExpiryDays(vaccine));
            repoVaccine.setMilestoneGapDays(vaccineGroup.days_after_birth_due);
            repoVaccine.setPrerequisite(VaccinatorUtils.getPrerequisiteVaccine(vaccine));
            repoVaccine.setPrerequisiteGapDays(VaccinatorUtils.getPrerequisiteGapDays(vaccine));

            ImmunizationLibrary.getInstance().getVaccineCacheMap().get(category).vaccineRepo.add(repoVaccine);

            processDefaultVaccineGroupCount(repoVaccine, category);
        }

    }

    private static void processCombinedVaccines(Vaccine vaccine) {
        String[] individualVaccines = vaccine.getName().split(vaccine.getVaccineSeparator().trim());

        for (String individualVaccine : individualVaccines) {
            ImmunizationLibrary.getInstance().COMBINED_VACCINES.add(individualVaccine.trim());
            ImmunizationLibrary.getInstance().COMBINED_VACCINES_MAP.put(individualVaccine.trim(), vaccine.getName().trim());

        }
    }

    private static void processDefaultVaccineGroupCount(VaccineRepo.Vaccine repoVaccine, String category) {
        String repoGroup = Utils.getGroupName(repoVaccine, category);

        if (!TextUtils.isEmpty(repoGroup)) {

            GroupVaccineCount groupVaccineCount = ImmunizationLibrary.getInstance().getVaccineCacheMap().get(category).groupVaccineCountMap.get(repoGroup);
            if (groupVaccineCount == null) {
                groupVaccineCount = new GroupVaccineCount(0, 0);
            }

            groupVaccineCount.setGiven(groupVaccineCount.getGiven() + 1);
            groupVaccineCount.setRemaining(groupVaccineCount.getRemaining() + 1);

            ImmunizationLibrary.getInstance().getVaccineCacheMap().get(category).groupVaccineCountMap.put(repoGroup, groupVaccineCount);
        }
    }

    /**
     * Returns boolean for boolean values configured in the app.properties file
     * **/
    public static boolean isPropertyTrue(String key) {

        return CoreLibrary.getInstance().context().getAppProperties().hasProperty(key) && CoreLibrary.getInstance().context().getAppProperties().getPropertyBoolean(key);
    }
}
