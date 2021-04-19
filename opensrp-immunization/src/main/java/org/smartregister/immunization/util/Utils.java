package org.smartregister.immunization.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.CoreLibrary;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.GroupVaccineCount;
import org.smartregister.immunization.domain.jsonmapping.Vaccine;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

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
                        int offsetMinusRelaxationDays = Integer.parseInt(mid) - Integer.parseInt(relaxationsDays);
                        if (offsetMinusRelaxationDays > 0) { //Do not use relaxation days on vaccines due at birth (0d offset)
                            tokens[i] = offsetMinusRelaxationDays + suffix;
                        }
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
            Timber.e(e);
        }
        return newOffset;
    }

    @NonNull
    public static String getGroupName(VaccineRepo.Vaccine vaccine, String category) {
        VaccineCache vaccineCache = ImmunizationLibrary.getVaccineCacheMap().get(category);
        if (vaccine != null && vaccineCache != null) {
            String groupName = vaccineCache.reverseLookupGroupMap.get(VaccinatorUtils.cleanVaccineName(vaccine.display()).toLowerCase(Locale.ENGLISH));
            if (groupName != null) {
                return groupName;
            }
        }

        return "";
    }

    public static void processVaccineCache(@NonNull android.content.Context context, String category) {
        try {
            if ( ImmunizationLibrary.getVaccineCacheMap().get(category) == null) {
                ImmunizationLibrary.getVaccineCacheMap().put(category, new VaccineCache());

                List<VaccineGroup> vaccinesJsonMapping = VaccinatorUtils.getSupportedVaccinesByCategory(context, category);

                processVaccineCore(category, vaccinesJsonMapping);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private static void processVaccineCore(String category, List<VaccineGroup> vaccinesJsonMapping) {
        if (vaccinesJsonMapping != null && vaccinesJsonMapping.size() > 0) {
            for (VaccineGroup vaccineGroup : vaccinesJsonMapping) {
                processVaccineGroup(vaccineGroup, category);
            }

            //Set vaccines Array
            VaccineCache vaccineCache = ImmunizationLibrary.getVaccineCacheMap().get(category);
            if (vaccineCache != null)
                vaccineCache.vaccines = vaccineCache.vaccineRepo.toArray(new VaccineRepo.Vaccine[]{});//Reset globally

        } else {
            //Eject from map if no values found. Will allow reprocessing if invoked later
            ImmunizationLibrary.getVaccineCacheMap().remove(category);
            Timber.e("No such vaccine configuration file found for category");
        }
    }

    public static void processVaccineCache(@NonNull android.content.Context context) {
        try {

            List<String> files = VaccinatorUtils.getVaccineFiles(context);
            String category;

            String fileName;
            for (int i = 0; i < files.size(); i++) {
                fileName = files.get(i);
                category = fileName.substring(0, fileName.lastIndexOf('_'));


                if ( ImmunizationLibrary.getVaccineCacheMap().get(category) == null) {
                    ImmunizationLibrary.getVaccineCacheMap().put(category, new VaccineCache());

                    List<VaccineGroup> vaccinesJsonMapping = VaccinatorUtils.getVaccineGroupsFromVaccineConfigFile(context, VaccinatorUtils.vaccines_folder + File.separator + fileName);

                    processVaccineCore(category, vaccinesJsonMapping);
                }

            }


        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private static void processVaccineGroup(VaccineGroup vaccineGroup, String category) {

        // Add BCG 2 and other special vaccines to the birth group
        if (vaccineGroup.id.equals("Birth") && category.equals(IMConstants.VACCINE_TYPE.CHILD)) {
            List<Vaccine> vaccinesList = VaccinatorUtils.getSpecialVaccines(ImmunizationLibrary.getInstance().context().applicationContext());

            if (vaccinesList != null)
                for (Vaccine vaccine : vaccinesList) {
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
        VaccineCache vaccineCache = ImmunizationLibrary.getVaccineCacheMap().get(category);

        if (vaccineName != null && vaccineCache != null) {
            if (vaccine.getVaccineSeparator() != null && vaccineName.contains(vaccine.getVaccineSeparator().trim())) {

                processCombinedVaccines(vaccine);

                String[] individualVaccines = vaccineName.split(vaccine.getVaccineSeparator().trim());

                for (String individualVaccine : individualVaccines) {
                    vaccine.setName(individualVaccine);
                    processVaccineGroupCore(vaccineGroup, vaccine, category);


                }

            } else {

                vaccineCache.reverseLookupGroupMap.put(vaccineName, vaccineGroup.name);

                VaccineRepo.Vaccine repoVaccine = VaccineRepo.getVaccineEnumFromValue(vaccineName);

                repoVaccine.setCategory(category);
                repoVaccine.setExpiryDays(VaccinatorUtils.getExpiryDays(vaccine));
                repoVaccine.setMilestoneGapDays(vaccineGroup.days_after_birth_due);
                repoVaccine.setPrerequisite(VaccinatorUtils.getPrerequisiteVaccine(vaccine));
                repoVaccine.setPrerequisiteGapDays(VaccinatorUtils.getPrerequisiteGapDays(vaccine));

                vaccineCache.vaccineRepo.add(repoVaccine);

                processDefaultVaccineGroupCount(repoVaccine, category);
            }
        }

    }

    private static void processCombinedVaccines(Vaccine vaccine) {
        String[] individualVaccines = vaccine.getName().split(vaccine.getVaccineSeparator().trim());

        for (String individualVaccine : individualVaccines) {
            ImmunizationLibrary.COMBINED_VACCINES.add(individualVaccine.trim());
            ImmunizationLibrary.COMBINED_VACCINES_MAP.put(individualVaccine.trim(), vaccine.getName().trim());

        }
    }

    private static void processDefaultVaccineGroupCount(VaccineRepo.Vaccine repoVaccine, String category) {
        String repoGroup = Utils.getGroupName(repoVaccine, category);
        VaccineCache vaccineCache = ImmunizationLibrary.getVaccineCacheMap().get(category);
        if (!TextUtils.isEmpty(repoGroup) && vaccineCache != null) {


            GroupVaccineCount groupVaccineCount = vaccineCache.groupVaccineCountMap.get(repoGroup);
            if (groupVaccineCount == null) {
                groupVaccineCount = new GroupVaccineCount(0, 0);
            }

            groupVaccineCount.setGiven(groupVaccineCount.getGiven() + 1);
            groupVaccineCount.setRemaining(groupVaccineCount.getRemaining() + 1);

            vaccineCache.groupVaccineCountMap.put(repoGroup, groupVaccineCount);
        }
    }

    /**
     * Returns boolean for boolean values configured in the app.properties file
     **/
    public static boolean isPropertyTrue(String key) {

        return CoreLibrary.getInstance().context().getAppProperties().hasProperty(key) && CoreLibrary.getInstance().context().getAppProperties().getPropertyBoolean(key);
    }
}
