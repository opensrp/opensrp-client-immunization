package org.smartregister.immunization.job;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2020-02-13
 */
class DailyExecutionWindow {

    final long startMs;
    final long endMs;

    /**
     * Holds the start end time in ms for a job.
     * Will wrap around to next day if currentHour < targetHour.
     * If the current time is exactly now it will be forced to 60 seconds in the future.
     *
     * @param currentHour - current currentHour
     * @param currentMinute - current currentMinute
     * @param targetHour - currentHour we want to start
     * @param targetMinute - currentMinute we want to start
     * @param windowLengthInMinutes - number of minutes for the execution window
     */
    DailyExecutionWindow(int currentHour, int currentMinute, long targetHour, long targetMinute, long windowLengthInMinutes) {
        long hourOffset;
        long minuteOffset;

        if (targetHour == currentHour && targetMinute < currentMinute) {
            hourOffset = TimeUnit.HOURS.toMillis(23);
        } else if (targetHour - currentHour == 1) { // if we are less then an hour ahead, but into the next hour
            // move forward to 0 minute of next hour
            hourOffset = TimeUnit.MINUTES.toMillis(60 - currentMinute);
            currentMinute = 0;
        } else if (targetHour >= currentHour) {
            hourOffset = TimeUnit.HOURS.toMillis(targetHour - currentHour);
        } else {
            hourOffset = TimeUnit.HOURS.toMillis((24 + targetHour) - currentHour);
        }

        if (targetMinute >= currentMinute) {
            minuteOffset = TimeUnit.MINUTES.toMillis(targetMinute - currentMinute);
        } else {
            minuteOffset = TimeUnit.MINUTES.toMillis((60 + targetMinute) - currentMinute);
        }

        this.startMs = Math.max(hourOffset + minuteOffset, 60000);
        this.endMs = this.startMs + TimeUnit.MINUTES.toMillis(windowLengthInMinutes);

    }
}