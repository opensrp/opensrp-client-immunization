package org.smartregister.immunization.utils;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import org.joda.time.DateTime;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.listener.VaccinationActionListener;
import org.smartregister.immunization.util.VaccinatorUtils;

import java.util.ArrayList;

/**
 * Created by kaderchowdhury on 14/12/17.
 */

public class VaccinatorUtilsTestMockActivity extends AppCompatActivity implements VaccinationActionListener {
    VaccinatorUtils utils;

    @Override
    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme); //we need this here
        super.onCreate(bundle);
        LinearLayout linearLayout;
        linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        utils = new VaccinatorUtils();
    }

    public int addVaccineDetail() {
        TableLayout tableLayout = new TableLayout(this);
        Alert alert = new Alert("", "", "", AlertStatus.normal, "", "");
        VaccineWrapper wrapper = new VaccineWrapper();
        wrapper.setVaccineDate(new DateTime(0l));
        wrapper.setVaccine(VaccineRepo.Vaccine.opv0);
        wrapper.setStatus("due");
        wrapper.setAlert(alert);
        VaccinatorUtils.addVaccineDetail(this, tableLayout, wrapper);

        wrapper.setAlert(null);
        VaccinatorUtils.addVaccineDetail(this, tableLayout, wrapper);

        wrapper.setStatus("done");
        VaccinatorUtils.addVaccineDetail(this, tableLayout, wrapper);

        wrapper.setStatus("expired");
        VaccinatorUtils.addVaccineDetail(this, tableLayout, wrapper);

        tableLayout.findViewById(R.id.undo).performClick();
        return tableLayout.getChildCount();
    }

    @Override
    public void onVaccinateToday(ArrayList<VaccineWrapper> tags, View view) {

    }

    @Override
    public void onVaccinateEarlier(ArrayList<VaccineWrapper> tags, View view) {

    }

    @Override
    public void onUndoVaccination(VaccineWrapper tag, View view) {

    }
}
