package org.smartregister.immunization.customshadows;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ephraim Kigamba - nek.eam@gmail.com on 28-02-2020.
 */
@Implements(JsonFormUtils.class)
public class ShadowJsonFormUtils {

    private static HashMap<String, HashMap<Integer, ArrayList<Object>>> methodCalls = new HashMap<>();

    @Implementation
    public static void createVaccineEvent(Context context, Vaccine vaccine, String eventType, String entityType, JSONArray fields) {
        // TODO: Make this dynamic and reusable for other instances
        String methodName = "createVaccineEvent(Context, Vaccine, String, String, JSONArray)";

        int count = 0;

        HashMap<Integer, ArrayList<Object>> methodCall = methodCalls.get(methodName);
        if (methodCall != null) {
            count = methodCall.size() + 1;
        } else {
            methodCall = new HashMap<>();
        }
        ArrayList<Object> params = new ArrayList<>();
        params.add(context);
        params.add(vaccine);
        params.add(eventType);
        params.add(entityType);
        params.add(fields);

        methodCall.put(count, params);
        methodCalls.put(methodName, methodCall);
    }

    @Implementation
    public static void createServiceEvent(Context context, ServiceRecord serviceRecord, String eventType, String entityType, JSONArray fields) {
        // TODO: Make this dynamic and reusable for other instances
        String methodName = "createServiceEvent(Context, ServiceRecord, String, String, JSONArray)";

        int count = 0;

        HashMap<Integer, ArrayList<Object>> methodCall = methodCalls.get(methodName);
        if (methodCall != null) {
            count = methodCall.size() + 1;
        } else {
            methodCall = new HashMap<>();
        }
        ArrayList<Object> params = new ArrayList<>();
        params.add(context);
        params.add(serviceRecord);
        params.add(eventType);
        params.add(entityType);
        params.add(fields);

        methodCall.put(count, params);
        methodCalls.put(methodName, methodCall);
    }

    public static HashMap<String, HashMap<Integer, ArrayList<Object>>> getMethodCalls() {
        return methodCalls;
    }

    public static boolean isCalled(@NonNull String methodWithShortSignature) {
        return methodCalls.containsKey(methodWithShortSignature);
    }

    @Nullable
    public static ArrayList<Object> getMethodParams(@NonNull String methodWithShortSignature, int methodHitIndex) {
        return isCalled(methodWithShortSignature) ? methodCalls.get(methodWithShortSignature).get(methodHitIndex) : null;
    }
}
