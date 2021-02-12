package org.smartregister.immunization.job;

import android.content.Intent;
import androidx.annotation.NonNull;

import org.smartregister.AllConstants;
import org.smartregister.immunization.service.intent.VaccineIntentService;
import org.smartregister.job.BaseJob;

public class VaccineServiceJob extends BaseJob {
    public static final String TAG = "VaccineServiceJob";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Intent intent = new Intent(getApplicationContext(), VaccineIntentService.class);
        getApplicationContext().startService(intent);
        return params != null && params.getExtras()
                .getBoolean(AllConstants.INTENT_KEY.TO_RESCHEDULE, false) ? Result.RESCHEDULE : Result.SUCCESS;
    }
}
