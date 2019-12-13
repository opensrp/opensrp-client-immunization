package org.smartregister.immunization.domain;

/**
 * Created by ndegwamartin on 2019-07-19.
 */
public class GroupVaccineCount {

    private int remaining;
    private int given;

    public GroupVaccineCount(int remaining, int given) {
        this.remaining = remaining;
        this.given = given;
    }

    public int getRemaining() {
        return remaining;
    }

    public int getGiven() {
        return given;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public void setGiven(int given) {
        this.given = given;
    }

}
