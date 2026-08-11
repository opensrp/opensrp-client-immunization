package org.smartregister.immunization.utils;

import com.google.common.collect.ImmutableMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Photo;
import org.smartregister.domain.ProfileImage;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.IMConstants;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.repository.ImageRepository;
import org.smartregister.service.AlertService;
import org.smartregister.util.AppProperties;

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
    private VaccineRepository vaccineRepository;

    @Mock
    private AlertService alertService;

    @Mock
    private AppProperties appProperties;

    @Mock
    private ImageRepository imageRepository;

    @Before
    public void setUp() {
        mockImmunizationLibrary(immunizationLibrary, context, vaccineRepository, alertService, appProperties);
        Mockito.when(context.imageRepository()).thenReturn(imageRepository);
        Mockito.when(context.alertService()).thenReturn(alertService);
        Mockito.when(imageRepository.findByEntityId(ArgumentMatchers.anyString())).thenReturn(null);
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
        Mockito.doReturn("test-base-entity-id").when(commonPersonObjectClient).entityId();
        Photo photo = ImageUtils.profilePhotoByClient(commonPersonObjectClient);
        org.junit.Assert.assertNotNull(photo);
        org.junit.Assert.assertEquals(photo.getResourceId(), R.drawable.child_boy_infant);
    }

    @Test
    public void assertProfilePhotoByClientReturnsCorrectPhotoFilePathForCorrespondingClient() {
        ProfileImage profileImage = new ProfileImage();
        String imagePath = "/dummy/test/path/image.png";
        String dummyCaseId = "4400";
        profileImage.setFilepath(imagePath);
        Mockito.when(imageRepository.findByEntityId(dummyCaseId)).thenReturn(profileImage);
        commonPersonObjectClient = new CommonPersonObjectClient(dummyCaseId, Collections.emptyMap(),
                "Test Name");
        commonPersonObjectClient.setCaseId(dummyCaseId);
        Photo photo = ImageUtils.profilePhotoByClient(commonPersonObjectClient);
        org.junit.Assert.assertNotNull(photo);
        org.junit.Assert.assertEquals(imagePath, photo.getFilePath());
    }


    @Test
    public void assertProfilePhotoByClientReturnsGirlPhotoForFemaleGender() {

        Map<String, String> childDetails = ImmutableMap.of(IMConstants.KEY.GENDER, "female");

        Photo photo = ImageUtils.profilePhotoByClient(childDetails);
        org.junit.Assert.assertNotNull(photo);
        org.junit.Assert.assertEquals(photo.getResourceId(), R.drawable.child_girl_infant);
    }

}
