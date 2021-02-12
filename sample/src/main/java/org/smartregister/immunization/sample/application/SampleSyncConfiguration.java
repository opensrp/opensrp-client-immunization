package org.smartregister.immunization.sample.application;

import org.smartregister.SyncConfiguration;
import org.smartregister.SyncFilter;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.activity.BaseLoginActivity;

import java.util.Collections;
import java.util.List;

/**
 * Created by ndegwamartin on 04/03/2019.
 */
public class SampleSyncConfiguration extends SyncConfiguration {
    @Override
    public int getSyncMaxRetries() {
        return 5;
    }

    @Override
    public SyncFilter getSyncFilterParam() {
        return SyncFilter.LOCATION;
    }

    @Override
    public String getSyncFilterValue() {
        AllSharedPreferences sharedPreferences = SampleApplication.getInstance().context().userService()
                .getAllSharedPreferences();
        return sharedPreferences.fetchDefaultTeamId(sharedPreferences.fetchRegisteredANM());
    }

    @Override
    public int getUniqueIdSource() {
        return 2;
    }

    @Override
    public int getUniqueIdBatchSize() {
        return 11;
    }

    @Override
    public int getUniqueIdInitialBatchSize() {
        return 10;
    }

    @Override
    public boolean isSyncSettings() {
        return false;
    }

    @Override
    public SyncFilter getEncryptionParam() {
        return SyncFilter.LOCATION;
    }

    @Override
    public boolean updateClientDetailsTable() {
        return true;
    }

    @Override
    public List<String> getSynchronizedLocationTags() {
        return Collections.emptyList();
    }

    @Override
    public String getTopAllowedLocationLevel() {
        return "";
    }

    @Override
    public String getOauthClientId() {
        return "";
    }

    @Override
    public String getOauthClientSecret() {
        return "";
    }

    @Override
    public Class<? extends BaseLoginActivity> getAuthenticationActivity() {
        return null;
    }

}
