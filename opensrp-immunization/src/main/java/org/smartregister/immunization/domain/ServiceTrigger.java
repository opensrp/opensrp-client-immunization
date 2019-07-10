package org.smartregister.immunization.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Keyman on 26/05/2017.
 */

public class ServiceTrigger {
    private final Reference reference;
    private final String offset;
    private final String prerequisite;
    private final Multiple multiple;
    public ServiceTrigger(String offset) {
        reference = Reference.DOB;
        this.offset = offset;
        prerequisite = null;
        multiple = null;
    }

    public ServiceTrigger(String offset, String prerequisite) {
        reference = Reference.PREREQUISITE;
        this.offset = offset;
        this.prerequisite = prerequisite;
        multiple = null;
    }

    public ServiceTrigger(String offset, Multiple multiple) {
        reference = Reference.MULTIPLE;
        this.offset = offset;
        prerequisite = null;
        this.multiple = multiple;
    }

    public static ServiceTrigger init(JSONObject data) throws JSONException {
        if (data != null) {
            if (data.getString("reference").equalsIgnoreCase(Reference.DOB.name())) {
                return new ServiceTrigger(data.getString("offset"));
            } else if (data.getString("reference").equalsIgnoreCase(Reference.PREREQUISITE.name())) {
                String prerequisite = data.getString("prerequisite");
                if (prerequisite != null) {
                    return new ServiceTrigger(data.getString("offset"), prerequisite);
                }
            } else if (data.getString("reference").equalsIgnoreCase(Reference.MULTIPLE.name())) {
                JSONObject multipleJson = data.getJSONObject("multiple");
                String condition = null;
                List<String> prerequisites = new ArrayList<>();
                if (multipleJson != null) {
                    if (multipleJson.has("condition")) {
                        condition = multipleJson.getString("condition");
                    }
                    if (multipleJson.has("prerequisites")) {
                        JSONArray jsonArray = multipleJson.getJSONArray("prerequisites");
                        if (jsonArray != null)
                            for (int i = 0; i < jsonArray.length(); i++) {
                                prerequisites.add(jsonArray.getString(i));
                            }
                    }
                }

                return new ServiceTrigger(data.getString("offset"), new Multiple(condition, prerequisites));
            }
        }

        return null;
    }

    public Reference getReference() {
        return reference;
    }

    public Multiple getMultiple() {
        return multiple;
    }

    public String getOffset() {
        return offset;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public enum Reference {
        DOB,
        PREREQUISITE,
        MULTIPLE
    }

    public static class Multiple {
        private String condition;
        private List<String> prerequisites;

        public Multiple(String condition, List<String> prerequisites) {
            this.condition = condition;
            this.prerequisites = prerequisites;
        }

        public String getCondition() {
            return condition;
        }

        public List<String> getPrerequisites() {
            return prerequisites;
        }
    }


}
