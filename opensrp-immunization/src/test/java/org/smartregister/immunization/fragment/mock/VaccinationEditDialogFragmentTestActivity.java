package org.smartregister.immunization.fragment.mock;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import org.joda.time.DateTime;
import org.mockito.Mockito;
import org.smartregister.domain.Alert;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.domain.VaccineWrapperTest;
import org.smartregister.immunization.fragment.VaccinationEditDialogFragment;
import org.smartregister.immunization.listener.VaccinationActionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by real on 05/11/17.
 */

public class VaccinationEditDialogFragmentTestActivity extends FragmentActivity implements VaccinationActionListener {

    private LinearLayout linearLayout;
    private VaccinationEditDialogFragment fragment;
    private int fragmentId = 0;

    @Override
    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme); //we need this here
        super.onCreate(bundle);
        linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        startFragment();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

    }

    public void startFragment() {
        DateTime datetime = new DateTime();
        Alert alert = Mockito.mock(Alert.class);

        List<Vaccine> issuedVaccines = new ArrayList<Vaccine>();

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.NAME, 0, new java.util.Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        issuedVaccines.add(vaccine);

        VaccineWrapper vaccineWrapper = new VaccineWrapper();
        //  vaccineWrapper.setId(VaccineWrapperTest.ID);
        vaccineWrapper.setId(null);
        vaccineWrapper.setDbKey(0l);
        vaccineWrapper.setStatus(VaccineWrapperTest.STATUS);
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.bcg);
        vaccineWrapper.setVaccineDate(datetime);
        vaccineWrapper.setAlert(alert);
        vaccineWrapper.setName(VaccineWrapperTest.NAME);
        vaccineWrapper.setPreviousVaccine(VaccineWrapperTest.ID);
        vaccineWrapper.setCompact(true);
        vaccineWrapper.setColor(VaccineWrapperTest.COLOR);
        vaccineWrapper.setFormattedVaccineDate(VaccineWrapperTest.DD_MM_YYYY);
        vaccineWrapper.setExistingAge(VaccineWrapperTest.AGE);
        vaccineWrapper.setPatientName(VaccineWrapperTest.PATIENTNAME);
        vaccineWrapper.setUpdatedVaccineDate(datetime, true);
        vaccineWrapper.setPatientNumber(VaccineWrapperTest.NUMBER);
        Photo photo = Mockito.mock(Photo.class);
        vaccineWrapper.setPhoto(photo);
        vaccineWrapper.setGender(VaccineWrapperTest.GENDER);
        vaccineWrapper.setDefaultName(VaccineWrapperTest.DEFAULTNAME);
        vaccineWrapper.setSynced(true);

        ArrayList<VaccineWrapper> taglist = new ArrayList<VaccineWrapper>();
        taglist.add(vaccineWrapper);


        vaccineWrapper = new VaccineWrapper();
        // vaccineWrapper.setId(VaccineWrapperTest.ID);
        vaccineWrapper.setId(null);
        vaccineWrapper.setDbKey(0l);
        vaccineWrapper.setStatus(VaccineWrapperTest.STATUS);
        vaccineWrapper.setVaccine(VaccineRepo.Vaccine.bcg);
        vaccineWrapper.setVaccineDate(datetime);
        vaccineWrapper.setAlert(alert);
        vaccineWrapper.setName(VaccineWrapperTest.NAME);
        vaccineWrapper.setPreviousVaccine(VaccineWrapperTest.ID);
        vaccineWrapper.setCompact(true);
        vaccineWrapper.setColor(VaccineWrapperTest.COLOR);
        vaccineWrapper.setFormattedVaccineDate(VaccineWrapperTest.DD_MM_YYYY);
        vaccineWrapper.setExistingAge(VaccineWrapperTest.AGE);
        vaccineWrapper.setPatientName(VaccineWrapperTest.PATIENTNAME);
        vaccineWrapper.setUpdatedVaccineDate(datetime, true);
        vaccineWrapper.setPatientNumber(VaccineWrapperTest.NUMBER);
        photo = Mockito.mock(Photo.class);
        vaccineWrapper.setPhoto(photo);
        vaccineWrapper.setGender(VaccineWrapperTest.GENDER);
        vaccineWrapper.setDefaultName(VaccineWrapperTest.DEFAULTNAME);
        vaccineWrapper.setSynced(true);


        taglist.add(vaccineWrapper);

        //        VaccinationDialogFragment fragment = VaccinationDialogFragment.newInstance(new java.util.Date(), issuedVaccines, taglist);
        initializeFragment(issuedVaccines, taglist);

    }

    private void initializeFragment(List<Vaccine> issuedVaccines, ArrayList<VaccineWrapper> taglist) {
        fragment = VaccinationEditDialogFragment
                .newInstance(this, new java.util.Date(), issuedVaccines, taglist, linearLayout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        View view = fragment.getView();
        if (fragmentId == 0) {
            view.findViewById(R.id.set).performClick();//need to mock CheckBox
            view.findViewById(R.id.vaccinate_earlier).performClick();//need to mock CheckBox
        }
        view.findViewById(R.id.vaccinate_today).performClick();
        fragmentId = 1;

        startFragmentWithTagList();
    }

    public void startFragmentWithTagList() {
        DateTime datetime = new DateTime();
        Alert alert = Mockito.mock(Alert.class);

        List<Vaccine> issuedVaccines = new ArrayList<Vaccine>();

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.NAME, 0, new java.util.Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        issuedVaccines.add(vaccine);

        VaccineWrapper tag = new VaccineWrapper();
        tag.setId(VaccineWrapperTest.ID);
        //        tag.setId(null);
        tag.setDbKey(0l);
        tag.setStatus(VaccineWrapperTest.STATUS);
        tag.setVaccine(VaccineRepo.Vaccine.bcg);
        tag.setVaccineDate(datetime);
        tag.setAlert(alert);
        tag.setName(VaccineWrapperTest.NAME);
        tag.setPreviousVaccine(VaccineWrapperTest.ID);
        tag.setCompact(true);
        tag.setColor(VaccineWrapperTest.COLOR);
        tag.setFormattedVaccineDate(VaccineWrapperTest.DD_MM_YYYY);
        tag.setExistingAge(VaccineWrapperTest.AGE);
        tag.setPatientName(VaccineWrapperTest.PATIENTNAME);
        tag.setUpdatedVaccineDate(datetime, true);
        tag.setPatientNumber(VaccineWrapperTest.NUMBER);
        Photo photo = Mockito.mock(Photo.class);
        tag.setPhoto(photo);
        tag.setGender(VaccineWrapperTest.GENDER);
        tag.setDefaultName(VaccineWrapperTest.DEFAULTNAME);
        tag.setSynced(true);

        ArrayList<VaccineWrapper> taglist = new ArrayList<VaccineWrapper>();
        taglist.add(tag);
        tag = new VaccineWrapper();
        tag.setId(VaccineWrapperTest.ID);
        //        tag.setId(null);
        tag.setDbKey(0l);
        tag.setStatus(VaccineWrapperTest.STATUS);
        tag.setVaccine(null);
        tag.setVaccineDate(datetime);
        tag.setAlert(alert);
        tag.setName(VaccineWrapperTest.NAME);
        tag.setPreviousVaccine(VaccineWrapperTest.ID);
        tag.setCompact(true);
        tag.setColor(VaccineWrapperTest.COLOR);
        tag.setFormattedVaccineDate(VaccineWrapperTest.DD_MM_YYYY);
        tag.setExistingAge(VaccineWrapperTest.AGE);
        tag.setPatientName(VaccineWrapperTest.PATIENTNAME);
        tag.setUpdatedVaccineDate(datetime, true);
        tag.setPatientNumber(VaccineWrapperTest.NUMBER);
        tag.setPhoto(photo);
        tag.setGender(VaccineWrapperTest.GENDER);
        tag.setDefaultName(VaccineWrapperTest.DEFAULTNAME);
        tag.setSynced(true);
        taglist.add(tag);
        //        VaccinationDialogFragment fragment = VaccinationDialogFragment.newInstance(new java.util.Date(), issuedVaccines, taglist);
        initializeFragment(issuedVaccines, taglist);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public VaccinationEditDialogFragment getFragment() {
        return fragment;
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
