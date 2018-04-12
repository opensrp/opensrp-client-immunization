package org.smartregister.immunization.util;

import android.app.Activity;
import android.content.res.Configuration;

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
}
