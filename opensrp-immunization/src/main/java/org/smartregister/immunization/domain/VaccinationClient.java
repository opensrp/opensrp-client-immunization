package org.smartregister.immunization.domain;

import org.joda.time.DateTime;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2020-02-13
 */

public class VaccinationClient {

    private String baseEntityId;
    private DateTime birthDateTime;

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public DateTime getBirthDateTime() {
        return birthDateTime;
    }

    public void setBirthDateTime(DateTime birthDateTime) {
        this.birthDateTime = birthDateTime;
    }
}
