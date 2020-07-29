package org.smartregister.immunization.util;

import org.smartregister.immunization.BuildConfig;

/**
 * Created by keyman on 26/07/2017.
 */
public class IMConstants {

    public static final int VACCINE_SYNC_TIME = BuildConfig.VACCINE_SYNC_TIME;

    public static final int INACTIVE_WIDGET_ALPHA = 120;
    public static final int ACTIVE_WIDGET_ALPHA = 255;

    public class APP_PROPERTIES {
        public static final String VACCINE_RELAXATION_DAYS = "vaccine.relaxation.days";
        public static final String VACCINE_EXPIRED_ENTRY_ALLOW = "vaccine.expired.entry.allow";
        public static final String VACCINE_REQUISITE_DATE_CONSTRAINT_ENABLED = "vaccine.requisite.date.constraint.enabled";
        public static final String EXPIRED_CARD_AS_RED = "vaccine.expired.red";
    }

    public class VACCINE_TYPE {
        public static final String CHILD = "child";
        public static final String WOMAN = "woman";
    }

    public interface BroadcastAction {

        interface VaccineScheduleUpdate {
            String SERVICE_STARTED = "vaccine-schedule-update-intent-service-started";
            String SERVICE_FINISHED = "vaccine-schedule-update-intent-service-finished";
        }
    }

    public interface IntentKey {
        interface VaccineScheduleUpdateIntentService {
            String CLIENT_TABLE_NAME = "client-table-name";
        }
    }

    public interface Preference {
        String VACCINE_SCHEDULE_UPDATE_LAST_TIME_RUN = "vaccine-schedule-update-last-time-run";
    }

}
