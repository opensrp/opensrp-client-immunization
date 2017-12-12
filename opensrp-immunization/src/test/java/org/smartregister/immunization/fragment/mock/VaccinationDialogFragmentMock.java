package org.smartregister.immunization.fragment.mock;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineSchedule;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.fragment.VaccinationDialogFragment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kaderchowdhury on 12/12/17.
 */

public class VaccinationDialogFragmentMock extends VaccinationDialogFragment {
    public VaccinationDialogFragmentMock() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth) {
        super.setDateOfBirth(dateOfBirth);
    }

    @Override
    public void setIssuedVaccines(List<Vaccine> issuedVaccines) {
        super.setIssuedVaccines(issuedVaccines);
    }

    public static VaccinationDialogFragmentMock newInstance(Date dateOfBirth,
                                                            List<Vaccine> issuedVaccines,
                                                            ArrayList<VaccineWrapper> tags) {
        VaccinationDialogFragmentMock vaccinationDialogFragment = new VaccinationDialogFragmentMock();

        Bundle args = new Bundle();
        args.putSerializable(WRAPPER_TAG, tags);
        vaccinationDialogFragment.setArguments(args);
        vaccinationDialogFragment.setDateOfBirth(dateOfBirth);
        vaccinationDialogFragment.setIssuedVaccines(issuedVaccines);
        vaccinationDialogFragment.setDisableConstraints(false);

        return vaccinationDialogFragment;
    }

    @Override
    public void setDisableConstraints(boolean disableConstraints) {
        super.setDisableConstraints(disableConstraints);
    }

    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = Mockito.spy(inflater);
        LinearLayout vaccineNameLayout = new LinearLayout(RuntimeEnvironment.application);
        CheckBox checkBox = new CheckBox(RuntimeEnvironment.application);
        checkBox.setId(R.id.select);
        RadioButton radioButton = new RadioButton(RuntimeEnvironment.application);
        radioButton.setId(R.id.radio);
        TextView textView = new TextView(RuntimeEnvironment.application);
        textView.setId(R.id.vaccine);
        vaccineNameLayout.addView(checkBox);
        vaccineNameLayout.addView(radioButton);
        vaccineNameLayout.addView(textView);
        Mockito.doReturn(vaccineNameLayout).when(this.inflater).inflate(R.layout.vaccination_name, null);
        try{
            VaccineSchedule.init(new JSONArray(VaccineData.vaccines), new JSONArray(VaccineData.special_vacines), "child");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return super.onCreateView(this.inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
    }
}
