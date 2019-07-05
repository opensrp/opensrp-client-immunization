package org.smartregister.immunization.listener;

import android.view.View;

import org.smartregister.immunization.domain.VaccineWrapper;

import java.util.ArrayList;

/**
 * Created by keyman on 22/11/2016.
 */
public interface VaccinationActionListener {

    void onVaccinateToday(ArrayList<VaccineWrapper> tags, View view);

    void onVaccinateEarlier(ArrayList<VaccineWrapper> tags, View view);

    void onUndoVaccination(VaccineWrapper tag, View view);
}
