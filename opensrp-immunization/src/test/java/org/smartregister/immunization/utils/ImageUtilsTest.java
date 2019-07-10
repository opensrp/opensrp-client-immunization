package org.smartregister.immunization.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Photo;
import org.smartregister.domain.ProfileImage;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.repository.ImageRepository;

import java.util.Collections;

/**
 * Created by onaio on 29/08/2017.
 */

@PrepareForTest ({ImmunizationLibrary.class})
public class ImageUtilsTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private CommonPersonObjectClient commonPersonObjectClient;

    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Mock
    private Context context;

    @Mock
    private ImageRepository imageRepository;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void assertProfileImageResourceByGenderWithEmptyStringParameterReturnsDefaultResource() {
        org.junit.Assert.assertEquals(ImageUtils.profileImageResourceByGender(""), R.drawable.child_boy_infant);
    }

    @Test
    public void assertProfileImageResourceByGenderWithMaleParameterReturnsMaleResource() {
        org.junit.Assert.assertEquals(ImageUtils.profileImageResourceByGender("male"), R.drawable.child_boy_infant);
    }

    @Test
    public void assertProfileImageResourceByGenderWithFemaleParameterReturnsFemaleResource() {
        org.junit.Assert.assertEquals(ImageUtils.profileImageResourceByGender("female"), R.drawable.child_girl_infant);
    }

    @Test
    public void assertProfileImageResourceByGenderWithTransgenderParameterReturnsTransgenderResource() {
        org.junit.Assert
                .assertEquals(ImageUtils.profileImageResourceByGender("transgender"), R.drawable.child_transgender_inflant);
    }

    @Test
    public void assertProfileImageResourceByGenderObjectWithMaleGenderParameterReturnsMaleResource() {
        org.junit.Assert.assertEquals(ImageUtils.profileImageResourceByGender(org.opensrp.api.constants.Gender.MALE),
                R.drawable.child_boy_infant);
    }

    @Test
    public void assertProfileImageResourceByGenderWithFemaleObjectReturnsFemaleResource() {
        org.junit.Assert.assertEquals(ImageUtils.profileImageResourceByGender(org.opensrp.api.constants.Gender.FEMALE),
                R.drawable.child_girl_infant);
    }

    @Test
    public void assertProfileImageResourceByGenderWithNullObjectParameterReturnsTransgenderResource() {
        org.opensrp.api.constants.Gender gender = null;
        org.junit.Assert.assertEquals(ImageUtils.profileImageResourceByGender(gender), R.drawable.child_transgender_inflant);
    }

    @Test
    public void assertImageUtilsClassConstructorReturnsNonNullObjectOnInstantiation() {
        org.junit.Assert.assertNotNull(new ImageUtils());
    }

    @Test
    public void assertProfilePhotoByClientReturnsDefaultInfantBoyPhoto() {
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().imageRepository()).thenReturn(imageRepository);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().imageRepository()
                .findByEntityId(org.mockito.ArgumentMatchers.anyString())).thenReturn(null);
        Photo photo = ImageUtils.profilePhotoByClient(commonPersonObjectClient);
        org.junit.Assert.assertNotNull(photo);
        org.junit.Assert.assertEquals(photo.getResourceId(), R.drawable.child_boy_infant);
    }

    @Test
    public void assertProfilePhotoByClientReturnsCorrectPhotoFilePathForCorrespondingClient() {
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().imageRepository()).thenReturn(imageRepository);
        ProfileImage profileImage = new ProfileImage();
        String imagePath = "/dummy/test/path/image.png";
        String dummyCaseId = "4400";
        profileImage.setFilepath(imagePath);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().imageRepository().findByEntityId(dummyCaseId))
                .thenReturn(profileImage);
        commonPersonObjectClient = new CommonPersonObjectClient(dummyCaseId, Collections.<String, String>emptyMap(),
                "Test Name");
        commonPersonObjectClient.setCaseId(dummyCaseId);
        Photo photo = ImageUtils.profilePhotoByClient(commonPersonObjectClient);
        org.junit.Assert.assertNotNull(photo);
        org.junit.Assert.assertEquals(imagePath, photo.getFilePath());
    }

}
