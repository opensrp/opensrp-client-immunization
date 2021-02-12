package org.smartregister.immunization.fragment.mock;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineSchedule;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.jsonmapping.VaccineGroup;
import org.smartregister.immunization.fragment.VaccinationDialogFragment;
import org.smartregister.util.JsonFormUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kaderchowdhury on 12/12/17.
 */

public class VaccinationDialogFragmentMock extends VaccinationDialogFragment {
    private LayoutInflater inflater;

    public VaccinationDialogFragmentMock() {
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
    public void setDateOfBirth(Date dateOfBirth) {
        super.setDateOfBirth(dateOfBirth);
    }

    @Override
    public void setIssuedVaccines(List<Vaccine> issuedVaccines) {
        super.setIssuedVaccines(issuedVaccines);
    }

    @Override
    public void setDisableConstraints(boolean disableConstraints) {
        super.setDisableConstraints(disableConstraints);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = Mockito.spy(inflater);
        ViewGroup dialogViewMock = (ViewGroup) inflater.inflate(R.layout.vaccination_dialog_view, container, false);
        ViewGroup dialogView = Mockito.spy((ViewGroup) inflater.inflate(R.layout.vaccination_dialog_view, container, false));
        Mockito.doReturn(dialogView).when(this.inflater).inflate(R.layout.vaccination_dialog_view, container, false);
        LinearLayout vaccinationNameLayout = Mockito
                .spy((LinearLayout) dialogView.findViewById(R.id.vaccination_name_layout));
        Mockito.doReturn(vaccinationNameLayout).when(dialogView).findViewById(R.id.vaccination_name_layout);

        LinearLayout vaccineName = Mockito.spy(new LinearLayout(RuntimeEnvironment.application));
        CheckBoxMock checkBox = Mockito.mock(CheckBoxMock.class);
        RadioButtonMock radioButton = Mockito
                .mock(RadioButtonMock.class);//new RadioButtonMock(RuntimeEnvironment.application);
        TextView textView = new TextView(RuntimeEnvironment.application);
        Mockito.doReturn(vaccineName).when(this.inflater).inflate(R.layout.vaccination_name, null);
        Mockito.doReturn(radioButton).when(vaccineName).findViewById(R.id.radio);
        Mockito.doReturn(textView).when(vaccineName).findViewById(R.id.vaccine);
        Mockito.doReturn(checkBox).when(vaccineName).findViewById(R.id.select);
        Mockito.doNothing().when(vaccinationNameLayout).addView(vaccineName);
        Mockito.doReturn(1).when(vaccinationNameLayout).getChildCount();
        Mockito.doReturn(vaccineName).when(vaccinationNameLayout).getChildAt(0);

        Type listType = new TypeToken<List<VaccineGroup>>() {
        }.getType();
        List<VaccineGroup> vaccines = JsonFormUtils.gson.fromJson(VaccineData.vaccines, listType);

        listType = new TypeToken<List<org.smartregister.immunization.domain.jsonmapping.Vaccine>>() {
        }.getType();
        List<org.smartregister.immunization.domain.jsonmapping.Vaccine> specialVaccines = JsonFormUtils.gson
                .fromJson(VaccineData.special_vacines, listType);

        VaccineSchedule.init(vaccines, specialVaccines, "child");

        super.onCreateView(this.inflater, container, savedInstanceState);

        dialogView.findViewById(R.id.set).performClick();
        dialogView.findViewById(R.id.vaccinate_today).performClick();
        dialogView.findViewById(R.id.vaccinate_earlier).performClick();
        dialogViewMock.setFilterTouchesWhenObscured(dialogView.getFilterTouchesWhenObscured());//Set to same state
        return dialogViewMock;
    }

    @Override
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        super.setOnDismissListener(onDismissListener);
    }

    @Override
    public void dismiss() {
        //        super.dismiss();
    }
}
