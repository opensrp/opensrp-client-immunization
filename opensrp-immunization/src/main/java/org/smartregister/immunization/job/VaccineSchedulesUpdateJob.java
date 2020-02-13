package org.smartregister.immunization.job;

import android.content.Intent;
import android.support.annotation.NonNull;

import org.smartregister.AllConstants;
import org.smartregister.immunization.service.intent.VaccineSchedulesUpdateIntentService;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.job.BaseJob;

/**
 * This job starts a service which recalculates the alerts periodically(daily) or ad-hoc
 *
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2020-02-13
 */

public abstract class VaccineSchedulesUpdateJob extends BaseJob {

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Intent intent = new Intent(getApplicationContext(), VaccineSchedulesUpdateIntentService.class);
        intent.putExtra(IMConstants.IntentKey.VaccineScheduleUpdateIntentService.CLIENT_TABLE_NAME, getClientTableName());

        getApplicationContext().startService(intent);
        return params != null && params.getExtras().getBoolean(AllConstants.INTENT_KEY.TO_RESCHEDULE, false)
                ? Result.RESCHEDULE: Result.SUCCESS;
    }

    @NonNull
    protected abstract String getClientTableName();
}
