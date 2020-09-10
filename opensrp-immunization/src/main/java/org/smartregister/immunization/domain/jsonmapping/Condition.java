package org.smartregister.immunization.domain.jsonmapping;


import java.util.List;
import java.util.Map;

/**
 * Created by samuelgithengi on 2/27/18.
 */

public class Condition {

    public String type;

    public String vaccine;

    public Map<String, String> age;

    public String comparison;

    public String value;

    public List<Condition> conditions;

}
