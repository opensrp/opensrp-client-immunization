package org.smartregister.immunization.job;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import android.text.TextUtils;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobRequest;

import org.smartregister.CoreLibrary;
import org.smartregister.immunization.service.intent.VaccineSchedulesUpdateIntentService;
import org.smartregister.immunization.util.IMConstants;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * This job starts a service which recalculates the alerts periodically(daily) or ad-hoc
 *
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2020-02-13
 */

public abstract class VaccineSchedulesUpdateJob extends DailyJob {

    public static final String TAG = "VaccineSchedulesUpdateJob";
    public static final String SCHEDULE_ADHOC_TAG = "VaccineSchedulesUpdateAdhocJob";

    public static void scheduleEverydayAt(@NonNull String jobTag, int hour, int minute) {
        JobRequest.Builder jobRequest = new JobRequest.Builder(jobTag);
        long startTime = TimeUnit.HOURS.toMillis(hour) + TimeUnit.MINUTES.toMillis(minute);
        schedule(jobRequest, startTime, startTime + TimeUnit.MINUTES.toMillis(45));
    }

    /**
     * For jobs that need to be started immediately
     */
    public static void scheduleJobImmediately() {
        int jobId = startNowOnce(new JobRequest.Builder(SCHEDULE_ADHOC_TAG));
        Timber.d("Scheduling job with name " + SCHEDULE_ADHOC_TAG + " immediately with JOB ID " + jobId);
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        Intent intent = new Intent(getContext(), VaccineSchedulesUpdateIntentService.class);
        intent.putExtra(IMConstants.IntentKey.VaccineScheduleUpdateIntentService.CLIENT_TABLE_NAME, getClientTableName());

        getContext().startService(intent);
        updateLastTimeRun();

        return DailyJobResult.SUCCESS;
    }

    @VisibleForTesting
    protected void updateLastTimeRun() {
        CoreLibrary.getInstance().context().allSharedPreferences().savePreference(IMConstants.Preference.VACCINE_SCHEDULE_UPDATE_LAST_TIME_RUN, String.valueOf(System.currentTimeMillis()));
    }

    public static long getLastTimeRun() {
        String timeString = CoreLibrary.getInstance().context().allSharedPreferences().getPreference(IMConstants.Preference.VACCINE_SCHEDULE_UPDATE_LAST_TIME_RUN);

        if (TextUtils.isEmpty(timeString)) {
            return 0L;
        } else {
            try {
                return Long.parseLong(timeString);
            } catch (NumberFormatException ex) {
                Timber.e(ex);
                return 0L;
            }
        }
    }

    public static boolean isLastTimeRunLongerThan(long hours) {
        long lastTimeMillis = getLastTimeRun();

        return ((System.currentTimeMillis() - lastTimeMillis)/TimeUnit.HOURS.toMillis(1)) > hours;
    }


    @NonNull
    protected abstract String getClientTableName();
}
