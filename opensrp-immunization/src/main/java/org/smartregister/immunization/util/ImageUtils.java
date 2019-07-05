package org.smartregister.immunization.util;

import org.apache.commons.lang3.StringUtils;
import org.opensrp.api.constants.Gender;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Photo;
import org.smartregister.domain.ProfileImage;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;

import static org.smartregister.util.Utils.getValue;

/**
 * Created by keyman on 22/02/2017.
 */
public class ImageUtils {

    public static int profileImageResourceByGender(Gender gender) {
        if (gender != null) {
            if (gender.equals(Gender.MALE)) {
                return R.drawable.child_boy_infant;
            } else if (gender.equals(Gender.FEMALE)) {
                return R.drawable.child_girl_infant;
            }
        }
        return R.drawable.child_transgender_inflant;
    }

    public static Photo profilePhotoByClient(CommonPersonObjectClient client) {
        Photo photo = new Photo();
        ProfileImage profileImage = ImmunizationLibrary.getInstance().context().imageRepository()
                .findByEntityId(client.entityId());
        if (profileImage != null) {
            photo.setFilePath(profileImage.getFilepath());
        } else {
            String gender = getValue(client, "gender", true);
            photo.setResourceId(profileImageResourceByGender(gender));
        }
        return photo;
    }

    public static int profileImageResourceByGender(String gender) {
        if (StringUtils.isNotBlank(gender)) {
            if (gender.equalsIgnoreCase("male")) {
                return R.drawable.child_boy_infant;
            } else if (gender.equalsIgnoreCase("female")) {
                return R.drawable.child_girl_infant;
            } else if (gender.toLowerCase().contains("trans")) {
                return R.drawable.child_transgender_inflant;
            }
        }
        return R.drawable.child_boy_infant;
    }

}
