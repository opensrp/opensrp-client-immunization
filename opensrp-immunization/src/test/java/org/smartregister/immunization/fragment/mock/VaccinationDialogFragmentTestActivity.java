package org.smartregister.immunization.fragment.mock;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import org.smartregister.immunization.listener.VaccinationActionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by real on 05/11/17.
 */

public class VaccinationDialogFragmentTestActivity extends FragmentActivity implements VaccinationActionListener {

    @Override
    public void onCreate(Bundle bundle) {
        setTheme(R.style.AppTheme); //we need this here
        super.onCreate(bundle);
        LinearLayout linearLayout;
        linearLayout = new LinearLayout(this);
        setContentView(linearLayout);
        startFragment();
        startFragmentWithVaccineWrapper();
        startFragmentWithTagList();
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
        vaccineWrapper.setId(VaccineWrapperTest.ID);
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

        //        VaccinationDialogFragmentMock fragment = VaccinationDialogFragmentMock.newInstance(new java.util.Date(), null, null);
        initializeFragment(issuedVaccines, taglist);

    }

    public void startFragmentWithVaccineWrapper() {
        DateTime datetime = new DateTime();
        Alert alert = Mockito.mock(Alert.class);

        List<Vaccine> issuedVaccines = new ArrayList<Vaccine>();

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.NAME, 0, new java.util.Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        issuedVaccines.add(vaccine);

        VaccineWrapper tag = new VaccineWrapper();
        tag.setId(VaccineWrapperTest.ID);
        tag.setDbKey(0l);
        tag.setStatus(VaccineWrapperTest.STATUS);
        tag.setVaccine(null);
        tag.setVaccineDate(datetime);
        tag.setAlert(alert);
        tag.setName(VaccineRepo.Vaccine.opv0 + "/" + VaccineRepo.Vaccine.bcg);
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

        //        VaccinationDialogFragmentMock fragment = VaccinationDialogFragmentMock.newInstance(new java.util.Date(), null, null);
        initializeFragment(issuedVaccines, taglist);

    }

    public void startFragmentWithTagList() {
        DateTime datetime = new DateTime();
        Alert alert = Mockito.mock(Alert.class);

        List<Vaccine> issuedVaccines = new ArrayList<Vaccine>();

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.NAME, 0, new java.util.Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l,
                VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        issuedVaccines.add(vaccine);

        VaccineWrapper vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setId(VaccineWrapperTest.ID);
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

        ArrayList<VaccineWrapper> arrayList = new ArrayList<VaccineWrapper>();
        arrayList.add(vaccineWrapper);
        vaccineWrapper = new VaccineWrapper();
        vaccineWrapper.setId(VaccineWrapperTest.ID);
        vaccineWrapper.setDbKey(0l);
        vaccineWrapper.setStatus(VaccineWrapperTest.STATUS);
        vaccineWrapper.setVaccine(null);
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
        vaccineWrapper.setPhoto(photo);
        vaccineWrapper.setGender(VaccineWrapperTest.GENDER);
        vaccineWrapper.setDefaultName(VaccineWrapperTest.DEFAULTNAME);
        vaccineWrapper.setSynced(true);
        arrayList.add(vaccineWrapper);

        //        VaccinationDialogFragmentMock fragment = VaccinationDialogFragmentMock.newInstance(new java.util.Date(), null, null);
        initializeFragment(issuedVaccines, arrayList);

    }

    private void initializeFragment(List<Vaccine> issuedVaccines, ArrayList<VaccineWrapper> taglist) {
        VaccinationDialogFragmentMock fragment = VaccinationDialogFragmentMock
                .newInstance(new java.util.Date(), issuedVaccines, taglist);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();
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
    public void onVaccinateToday(ArrayList<VaccineWrapper> tags, View view) {

    }

    @Override
    public void onVaccinateEarlier(ArrayList<VaccineWrapper> tags, View view) {

    }

    @Override
    public void onUndoVaccination(VaccineWrapper tag, View view) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
