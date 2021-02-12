package org.smartregister.immunization.util;

import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.GroupVaccineCount;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ndegwamartin on 2019-12-08.
 */
public class VaccineCache {

    public VaccineRepo.Vaccine[] vaccines;
    public Map<String, String> reverseLookupGroupMap = new LinkedHashMap<>();
    public List<VaccineRepo.Vaccine> vaccineRepo = new ArrayList<>();
    public Map<String, GroupVaccineCount> groupVaccineCountMap = new LinkedHashMap<>();
}
