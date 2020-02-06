package org.smartregister.immunization.customshadows;

import android.content.Context;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.smartregister.util.AssetHandler;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2020-02-06
 */

@Implements(AssetHandler.class)
public class ShadowAssetHandler {

    @Implementation
    public static String readFileFromAssetsFolder(String fileName, Context context) {
        if (fileName.equals("special_vaccines.json")) {
            return "[\n" +
                    "  {\n" +
                    "    \"name\": \"BCG 2\",\n" +
                    "    \"type\": \"BCG\",\n" +
                    "    \"openmrs_date\": {\n" +
                    "      \"parent_entity\": \"886AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\n" +
                    "      \"entity\": \"concept\",\n" +
                    "      \"entity_id\": \"1410AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
                    "    },\n" +
                    "    \"openmrs_calculate\": {\n" +
                    "      \"parent_entity\": \"886AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\n" +
                    "      \"entity\": \"concept\",\n" +
                    "      \"entity_id\": \"1418AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\n" +
                    "      \"calculation\": 2\n" +
                    "    },\n" +
                    "    \"schedule\": {\n" +
                    "      \"due\": [\n" +
                    "        {\n" +
                    "          \"reference\": \"prerequisite\",\n" +
                    "          \"prerequisite\": \"BCG\",\n" +
                    "          \"offset\": \"+84d\"\n" +
                    "        }\n" +
                    "      ],\n" +
                    "      \"expiry\": [\n" +
                    "        {\n" +
                    "          \"reference\": \"dob\",\n" +
                    "          \"offset\": \"+1y\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  }\n" +
                    "]";
        }

        return "";
    }
}
