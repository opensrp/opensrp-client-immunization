package org.smartregister.immunization.sample.util;

import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/**
 * Created by keyman on 01/08/2017.
 */
public class SampleUtil {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault().toString().startsWith("ar") ? Locale.ENGLISH : Locale.getDefault()
    );

    public static final String ENTITY_ID = "1";
    public static final String DOB_STRING = "2018-06-01T00:00:00.000Z";
    public static final String GENDER = (new Random()).nextBoolean() ? "male" : "female";

    public static CommonPersonObjectClient dummyDetatils() {
        HashMap<String, String> columnMap = new HashMap<String, String>();
        columnMap.put("first_name", "Test");
        columnMap.put("last_name", "Doe");
        columnMap.put("zeir_id", "1");
        columnMap.put("dob", DOB_STRING);
        columnMap.put("gender", GENDER);


        CommonPersonObjectClient personDetails = new CommonPersonObjectClient(ENTITY_ID, columnMap, "Test");
        personDetails.setColumnmaps(columnMap);

        return personDetails;
    }


}
