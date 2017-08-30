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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by onaio on 29/08/2017.
 */

@PrepareForTest({ImmunizationLibrary.class})
public class ImageUtilsTest extends BaseUnitTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    CommonPersonObjectClient commonPersonObjectClient;

    @Mock
    ImmunizationLibrary immunizationLibrary;

    @Mock
    Context context;

    @Mock
    ImageRepository imageRepository;

    @Before
    public void setUp() {

        initMocks(this);
    }


    @Test
    public void profileImageResourceByGenderWithEmptyStringParameterReturnsDefaultResource() {
        assertEquals(ImageUtils.profileImageResourceByGender(""), R.drawable.child_boy_infant);
    }

    @Test
    public void profileImageResourceByGenderWithMaleParameterReturnsMaleResource() {
        assertEquals(ImageUtils.profileImageResourceByGender("male"), R.drawable.child_boy_infant);
    }

    @Test
    public void profileImageResourceByGenderWithFemaleParameterReturnsFemaleResource() {
        assertEquals(ImageUtils.profileImageResourceByGender("female"), R.drawable.child_girl_infant);
    }

    @Test
    public void profileImageResourceByGenderWithTransgenderParameterReturnsTransgenderResource() {
        assertEquals(ImageUtils.profileImageResourceByGender("transgender"), R.drawable.child_transgender_inflant);
    }

    @Test
    public void profileImageResourceByGenderObjectWithMaleGenderParameterReturnsMaleResource() {
        assertEquals(ImageUtils.profileImageResourceByGender(org.opensrp.api.constants.Gender.MALE), R.drawable.child_boy_infant);
    }

    @Test
    public void profileImageResourceByGenderWithFemaleObjectReturnsFemaleResource() {
        assertEquals(ImageUtils.profileImageResourceByGender(org.opensrp.api.constants.Gender.FEMALE), R.drawable.child_girl_infant);
    }

    @Test
    public void profileImageResourceByGenderWithNullObjectParameterReturnsTransgenderResource() {
        org.opensrp.api.constants.Gender gender = null;
        assertEquals(ImageUtils.profileImageResourceByGender(gender), R.drawable.child_transgender_inflant);
    }

    @Test
    public void imageUtilsClassConstructorReturnsNonNullObjectOnInstantiation() {
        assertNotNull(new ImageUtils());
    }

    @Test
    public void profilePhotoByClientReturnsDefaultInfantBoyPhoto() {
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().imageRepository()).thenReturn(imageRepository);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().imageRepository().findByEntityId(anyString())).thenReturn(null);
        Photo photo = ImageUtils.profilePhotoByClient(commonPersonObjectClient);
        assertNotNull(photo);
        assertEquals(photo.getResourceId(), R.drawable.child_boy_infant);
    }

    @Test
    public void profilePhotoByClientReturnsCorrectPhotoFilePathForCorrespondingClient() {
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(ImmunizationLibrary.getInstance().context()).thenReturn(context);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().imageRepository()).thenReturn(imageRepository);
        ProfileImage profileImage = new ProfileImage();
        String imagePath = "/dummy/test/path/image.png";
        String dummyCaseId = "4400";
        profileImage.setFilepath(imagePath);
        PowerMockito.when(ImmunizationLibrary.getInstance().context().imageRepository().findByEntityId(dummyCaseId)).thenReturn(profileImage);
        commonPersonObjectClient = new CommonPersonObjectClient(dummyCaseId, Collections.<String, String>emptyMap(), "Test Name");
        commonPersonObjectClient.setCaseId(dummyCaseId);
        Photo photo = ImageUtils.profilePhotoByClient(commonPersonObjectClient);
        assertNotNull(photo);
        assertEquals(imagePath, photo.getFilePath());
    }


}