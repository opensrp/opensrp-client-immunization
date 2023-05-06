package org.smartregister.immunization.util;

import org.smartregister.immunization.BuildConfig;

import java.util.HashMap;

/**
 * Created by keyman on 26/07/2017.
 */
public class IMConstants {

    public static final int VACCINE_SYNC_TIME = BuildConfig.VACCINE_SYNC_TIME;
    public  static HashMap<String,Boolean> isInvalidVaccineMap = new HashMap<>();
    public static final int INACTIVE_WIDGET_ALPHA = 120;
    public static final int ACTIVE_WIDGET_ALPHA = 255;

    public class APP_PROPERTIES {
        public static final String VACCINE_RELAXATION_DAYS = "vaccine.relaxation.days";
        public static final String VACCINE_EXPIRED_ENTRY_ALLOW = "vaccine.expired.entry.allow";
    }

    public class VACCINE_TYPE {
        public static final String CHILD = "child";
        public static final String WOMAN = "woman";
    }

}
