package org.smartregister.immunization.utils;

import android.os.Build;

import com.google.gson.reflect.TypeToken;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.customshadows.ShadowAssetHandler;
import org.smartregister.immunization.domain.jsonmapping.Vaccine;
import org.smartregister.immunization.util.VaccinatorUtils;
import org.smartregister.util.AssetHandler;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2020-02-05
 */

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.O_MR1}, shadows = {ShadowAssetHandler.class})
public class VaccinatorUtilsRobolectricTest {

    @Rule
    public MockitoRule mockitoRule  = MockitoJUnit.rule();

    @Test
    public void getVaccineFromVaccineConfigFileShouldReturnSpecialVaccinesAndAddThemToConfigJsonMapping() {
        ImmunizationLibrary immunizationLibrary = Mockito.mock(ImmunizationLibrary.class);
        ReflectionHelpers.setStaticField(ImmunizationLibrary.class, "instance", immunizationLibrary);

        Map<String, Object> jsonMap = new HashMap<>();
        Mockito.doReturn(jsonMap).when(immunizationLibrary).getVaccinesConfigJsonMap();

        // Mock calls to AssetHandler.readFileFromAssetsFolder(String filename, Context context)
        List<Vaccine> specialVaccines = VaccinatorUtils.getVaccineFromVaccineConfigFile(RuntimeEnvironment.application, "special_vaccines.json");

        Assert.assertEquals(1, specialVaccines.size());

        // Verify that the jsonMap required to read from, for BCG2 to be available, always has an extra item added
        // That is supposed to be the BCG2 vaccine in the special_vaccines.json file
        Assert.assertEquals(1, jsonMap.size());
    }

    @Test
    public void testThatSpecialVaccinesFileIsAvailableAndCanBeRead() {
        Class<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>> classType = (Class) List.class;
        Type listType = new TypeToken<List<Vaccine>>() {}.getType();

        Map<String, Object> jsonMap = new HashMap<>();
        List<Vaccine> specialVaccinesList = AssetHandler.assetJsonToJava(jsonMap, RuntimeEnvironment.application, "special_vaccines.json", classType,listType);

        Assert.assertEquals(1, specialVaccinesList.size());
    }
}
