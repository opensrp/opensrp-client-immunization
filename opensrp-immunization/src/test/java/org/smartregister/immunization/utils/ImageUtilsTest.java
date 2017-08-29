package org.smartregister.immunization.utils;


import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.repository.Repository;
import org.smartregister.service.AlertService;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by onaio on 29/08/2017.
 */

@PrepareForTest({})
public class ImageUtilsTest extends BaseUnitTest {


    @Before
    public void setUp() {
    }

    @Test
    public void profileImageResourceByGenderWithEmptyStringParameterReturnsDefaultResource() throws Exception {
        assertNotNull(ImageUtils.profileImageResourceByGender(""));
        assertEquals(ImageUtils.profileImageResourceByGender(""), R.drawable.child_boy_infant);
    }




}