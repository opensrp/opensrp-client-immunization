package org.smartregister.immunization.fragment.mock;

import android.content.Context;

import org.mockito.Mockito;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.repository.Repository;
import org.smartregister.util.OpenSRPImageLoader;
import org.smartregister.view.activity.DrishtiApplication;

/**
 * Created by kaderchowdhury on 12/12/17.
 */
@Implements(DrishtiApplication.class)
public class DrishtiApplicationShadow extends Shadow {
    Repository repository;
    private static OpenSRPImageLoader openSRPImageLoader;

    public DrishtiApplicationShadow() {
        super();
        repository = Mockito.mock(Repository.class);
    }

    @Implementation
    public static OpenSRPImageLoader getCachedImageLoaderInstance() {
        openSRPImageLoader = Mockito.mock(OpenSRPImageLoader.class);
//        Mockito.doNothing().when(openSRPImageLoader).getImageByClientId(Mockito.anyString(),Mockito.any(OpenSRPImageListener.class));


        return openSRPImageLoader;
    }

    @Implementation
    public void onCreate() {

    }

    @Implementation
    protected void attachBaseContext(Context base) {

    }

    @Implementation
    public Repository getRepository() {
        return repository;
    }

    @Implementation
    public String getPassword() {
        return "";
    }

    @Implementation
    public void setPassword(String password) {

    }

    @Implementation
    public void logoutCurrentUser() {

    }

}
