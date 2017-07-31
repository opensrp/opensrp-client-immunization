package org.smartregister.immunization.listener;

import android.view.View;

import org.smartregister.immunization.domain.VaccineWrapper;

import java.util.ArrayList;

/**
 * Created by keyman on 22/11/2016.
 */
public interface VaccinationActionListener {

    public void onVaccinateToday(ArrayList<VaccineWrapper> tags, View view);

    public void onVaccinateEarlier(ArrayList<VaccineWrapper> tags, View view);

    public void onUndoVaccination(VaccineWrapper tag, View view);
}
