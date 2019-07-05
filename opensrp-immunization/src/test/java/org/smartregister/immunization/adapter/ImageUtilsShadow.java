package org.smartregister.immunization.adapter;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.util.ImageUtils;

/**
 * Created by kaderchowdhury on 13/12/17.
 */
@Implements (ImageUtils.class)
public class ImageUtilsShadow extends Shadow {

    @Implementation
    public static Photo profilePhotoByClient(CommonPersonObjectClient client) {
        return new Photo();
    }

}
