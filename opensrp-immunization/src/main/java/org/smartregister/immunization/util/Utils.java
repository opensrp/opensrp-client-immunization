package org.smartregister.immunization.util;

import android.app.Activity;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.immunization.ImmunizationLibrary;

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
                    newOffset = StringUtils.join(tokens,",");
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
}
