package org.smartregister.immunization.domain.jsonmapping;

import java.util.List;

/**
 * Created by samuelgithengi on 2/27/18.
 */

public class VaccineGroup {

    public String name;

    public String id;

    public Integer days_after_birth_due;

    public List<Vaccine> vaccines;

}
