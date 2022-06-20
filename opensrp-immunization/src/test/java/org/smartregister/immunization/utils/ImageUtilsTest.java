package org.smartregister.immunization.utils;

import com.google.common.collect.ImmutableMap;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Photo;
import org.smartregister.domain.ProfileImage;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.repository.ImageRepository;

import java.util.Collections;
import java.util.Map;

/**
 * Created by onaio on 29/08/2017.
 */

public class ImageUtilsTest extends BaseUnitTest {

    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private Context context;

    @Mock
    private ImageRepository imageRepository;

    @Test
    public void assertProfileImageResourceByGenderWithEmptyStringParameterReturnsDefaultResource() {
        Assert.assertEquals(ImageUtils.profileImageResourceByGender(""), R.drawable.child_boy_infant);
    }

    @Test
    public void assertProfileImageResourceByGenderWithMaleParameterReturnsMaleResource() {
        Assert.assertEquals(ImageUtils.profileImageResourceByGender("male"), R.drawable.child_boy_infant);
    }

    @Test
    public void assertProfileImageResourceByGenderWithFemaleParameterReturnsFemaleResource() {
        Assert.assertEquals(ImageUtils.profileImageResourceByGender("female"), R.drawable.child_girl_infant);
    }

    @Test
    public void assertProfileImageResourceByGenderWithTransgenderParameterReturnsTransgenderResource() {
        org.junit.Assert
                .assertEquals(ImageUtils.profileImageResourceByGender("transgender"), R.drawable.child_transgender_inflant);
    }

    @Test
    public void assertProfileImageResourceByGenderObjectWithMaleGenderParameterReturnsMaleResource() {
        Assert.assertEquals(ImageUtils.profileImageResourceByGender(org.opensrp.api.constants.Gender.MALE),
                R.drawable.child_boy_infant);
    }

    @Test
    public void assertProfileImageResourceByGenderWithFemaleObjectReturnsFemaleResource() {
        Assert.assertEquals(ImageUtils.profileImageResourceByGender(org.opensrp.api.constants.Gender.FEMALE),
                R.drawable.child_girl_infant);
    }

    @Test
    public void assertProfileImageResourceByGenderWithNullObjectParameterReturnsTransgenderResource() {
        org.opensrp.api.constants.Gender gender = null;
        Assert.assertEquals(ImageUtils.profileImageResourceByGender(gender), R.drawable.child_transgender_inflant);
    }

    @Test
    public void assertImageUtilsClassConstructorReturnsNonNullObjectOnInstantiation() {
        Assert.assertNotNull(new ImageUtils());
    }

    @Test
    public void assertProfilePhotoByClientReturnsDefaultInfantBoyPhoto() {
        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        Mockito.when(ImmunizationLibrary.getInstance().context().imageRepository()).thenReturn(imageRepository);
        Mockito.when(ImmunizationLibrary.getInstance().context().imageRepository().findByEntityId(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);
        Mockito.doReturn("test-base-entity-id").when(commonPersonObjectClient).entityId();
        Photo photo = ImageUtils.profilePhotoByClient(commonPersonObjectClient);
        Assert.assertNotNull(photo);
        Assert.assertEquals(photo.getResourceId(), R.drawable.child_boy_infant);
    }

    @Test
    public void assertProfilePhotoByClientReturnsCorrectPhotoFilePathForCorrespondingClient() {
        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        Mockito.when(ImmunizationLibrary.getInstance().context().imageRepository()).thenReturn(imageRepository);
        ProfileImage profileImage = new ProfileImage();
        String imagePath = "/dummy/test/path/image.png";
        String dummyCaseId = "4400";
        profileImage.setFilepath(imagePath);
        Mockito.when(ImmunizationLibrary.getInstance().context().imageRepository().findByEntityId(dummyCaseId))
                .thenReturn(profileImage);
        commonPersonObjectClient = new CommonPersonObjectClient(dummyCaseId, Collections.emptyMap(),
                "Test Name");
        commonPersonObjectClient.setCaseId(dummyCaseId);
        Photo photo = ImageUtils.profilePhotoByClient(commonPersonObjectClient);
        Assert.assertNotNull(photo);
        Assert.assertEquals(imagePath, photo.getFilePath());
    }


    @Test
    public void assertProfilePhotoByClientReturnsGirlPhotoForFemaleGender() {
        Mockito.mockStatic(ImmunizationLibrary.class);
        Mockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        Mockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        Mockito.when(ImmunizationLibrary.getInstance().context().imageRepository()).thenReturn(imageRepository);
        Mockito.when(ImmunizationLibrary.getInstance().context().imageRepository().findByEntityId(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);

        Map<String, String> childDetails = ImmutableMap.of(IMConstants.KEY.GENDER, "female");

        Photo photo = ImageUtils.profilePhotoByClient(childDetails);
        Assert.assertNotNull(photo);
        Assert.assertEquals(photo.getResourceId(), R.drawable.child_girl_infant);
    }

}
