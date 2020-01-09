package org.smartregister.immunization;

import org.smartregister.view.activity.DrishtiApplication;

import timber.log.Timber;

public class TestApplication extends DrishtiApplication {

    public static void setInstance(DrishtiApplication application) {
        mInstance = application;
    }

    @Override
    public void logoutCurrentUser() {
        Timber.v("logoutCurrentUser");
    }
}

