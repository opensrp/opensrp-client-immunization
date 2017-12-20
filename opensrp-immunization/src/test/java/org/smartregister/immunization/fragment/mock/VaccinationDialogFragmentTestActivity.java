package org.smartregister.immunization.fragment.mock;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
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

public class VaccinationDialogFragmentTestActivity extends Activity implements VaccinationActionListener {

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

    public void startFragmentWithTagList() {
        DateTime datetime = new DateTime();
        Alert alert = Mockito.mock(Alert.class);

        List<Vaccine> issuedVaccines = new ArrayList<Vaccine>();

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.NAME, 0, new java.util.Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        issuedVaccines.add(vaccine);

        VaccineWrapper tag = new VaccineWrapper();
        tag.setId(VaccineWrapperTest.ID);
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

//        VaccinationDialogFragmentMock fragment = VaccinationDialogFragmentMock.newInstance(new java.util.Date(), null, null);
        VaccinationDialogFragmentMock fragment = VaccinationDialogFragmentMock.newInstance(new java.util.Date(), issuedVaccines, taglist);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

    }

    public void startFragment() {
        DateTime datetime = new DateTime();
        Alert alert = Mockito.mock(Alert.class);

        List<Vaccine> issuedVaccines = new ArrayList<Vaccine>();

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.NAME, 0, new java.util.Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        issuedVaccines.add(vaccine);

        VaccineWrapper tag = new VaccineWrapper();
        tag.setId(VaccineWrapperTest.ID);
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

//        VaccinationDialogFragmentMock fragment = VaccinationDialogFragmentMock.newInstance(new java.util.Date(), null, null);
        VaccinationDialogFragmentMock fragment = VaccinationDialogFragmentMock.newInstance(new java.util.Date(), issuedVaccines, taglist);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

    }

    public void startFragmentWithVaccineWrapper() {
        DateTime datetime = new DateTime();
        Alert alert = Mockito.mock(Alert.class);

        List<Vaccine> issuedVaccines = new ArrayList<Vaccine>();

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.NAME, 0, new java.util.Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
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
        VaccinationDialogFragmentMock fragment = VaccinationDialogFragmentMock.newInstance(new java.util.Date(), issuedVaccines, taglist);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
