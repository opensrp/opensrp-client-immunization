package org.smartregister.immunization.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.domain.Alert;
import org.smartregister.domain.AlertStatus;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.R;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.domain.VaccineWrapper;
import org.smartregister.immunization.fragment.mock.ServiceDialogFragmentTestActivity;
import org.smartregister.immunization.repository.VaccineRepository;
import org.smartregister.immunization.view.mock.VaccineGroupTestActivity;
import org.smartregister.immunization.view.mock.ViewAttributes;
import org.smartregister.util.Utils;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineGroupTest extends BaseUnitTest {

    private VaccineGroup view;

    @Mock
    private Context context;

    @Mock
    private org.smartregister.Context context_;

    private JSONObject vaccineData;
    private CommonPersonObjectClient childdetails;
    private ArrayList<Vaccine> vaccinelist;
    private ArrayList<Alert> alertlist;
    private VaccineWrapper wrapper;
    private ArrayList<VaccineWrapper>wrappers;
    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        view = new VaccineGroup(RuntimeEnvironment.application);
    }
    @Test
    public void assertGetAlertListNotNull() throws Exception {
        view.setAlertList(new ArrayList<Alert>());
        Assert.assertNotNull(view.getAlertList());
    }
    @Test
    public void assertGetVaccineListNotNull() throws Exception {
        view.setVaccineList(new ArrayList<Vaccine>());
        Assert.assertNotNull(view.getVaccineList());
    }

    @Test
    public void assertEqualsVaccineData() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        Assert.assertEquals(view.getVaccineData(),vaccineData);
    }
    @Test
    public void assertEqualsChildDetails() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        Assert.assertEquals(view.getChildDetails(),childdetails);
    }
    @Test
    public void assertEqualsVaccineList() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        Assert.assertEquals(view.getVaccineList(),vaccinelist);
    }
    @Test
    public void assertEqualsAlertList()throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        Assert.assertEquals(view.getAlertList(),alertlist);
    }
    @Test
    public void assertUpdateViewsWithDifferentTimeWillSetVaccineAdapter() throws Exception {
        Assert.assertEquals(view.getDueVaccines().size(),0);
        Assert.assertEquals(view.getAllVaccineWrappers().size(),0);

        setDataForTest("1985-07-24T00:00:00.000Z");
        view.updateViews();
        view.updateViews(wrappers);
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        setDataForTest(format.format(new Date())+"T00:00:00.000Z");
        view.updateViews(wrappers);
        setDataForTest("2018-01-01T00:00:00.000Z");
        view.updateViews(wrappers);
        Assert.assertNotNull(view.getDueVaccines());
        Assert.assertNotNull(view.getAllVaccineWrappers());
    }
    @Test
    public void verifyOnClickCallsOnRecordAllClickListenerAndOnVaccineClickedListener() throws Exception {

        setDataForTest("1985-07-24T00:00:00.000Z");
        view.updateViews();
        view.updateViews(wrappers);
        VaccineGroup.OnRecordAllClickListener onRecordAllClickListener = Mockito.mock(VaccineGroup.OnRecordAllClickListener.class);
        view.setOnRecordAllClickListener(onRecordAllClickListener);

        view.onClick((android.widget.TextView) view.findViewById(R.id.record_all_tv));
        Mockito.verify(onRecordAllClickListener).onClick(any(VaccineGroup.class),any(ArrayList.class));

        VaccineGroup.OnVaccineClickedListener onVaccineClickListener = Mockito.mock(VaccineGroup.OnVaccineClickedListener.class);
        view.setOnVaccineClickedListener(onVaccineClickListener);
        VaccineCard vaccineCard = new VaccineCard(RuntimeEnvironment.application);
        wrapper= new VaccineWrapper();
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg);
        vaccineCard.setVaccineWrapper(wrapper);

        view.onClick(vaccineCard);
        Mockito.verify(onVaccineClickListener).onClick(any(VaccineGroup.class),any(VaccineWrapper.class));

        VaccineGroup.OnVaccineUndoClickListener onVaccineUndoClickListener = Mockito.mock(VaccineGroup.OnVaccineUndoClickListener.class);
        view.setOnVaccineUndoClickListener(onVaccineUndoClickListener);
        View v = new View(RuntimeEnvironment.application);
        v.setId(R.id.undo_b);
        ViewGroup parent = new LinearLayout(RuntimeEnvironment.application);
        parent.addView(v);
        vaccineCard.addView(parent);

        view.onClick(v);
        Mockito.verify(onVaccineUndoClickListener).onUndoClick(any(VaccineGroup.class),any(VaccineWrapper.class));
    }

    @Test
    public void assertUpdateWrapperStatusCallsUpdateWrapperStatus() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        view.updateWrapperStatus(wrappers);
        wrapper= new VaccineWrapper();
        wrapper.setName(VaccineRepo.Vaccine.bcg2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg2);
        view.updateWrapper(wrapper);
        wrapper= new VaccineWrapper();
        wrapper.setName(VaccineRepo.Vaccine.bcg2.display()+"/:D");
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg2);
        view.updateWrapper(wrapper);
        Assert.assertNotNull(view.getAllVaccineWrappers());
    }

    @Test
    public void assertIsModalOpenReturnsBoolean() throws Exception {
        view.setModalOpen(true);
        Assert.assertEquals(view.isModalOpen(),true);
        view.setModalOpen(false);
        Assert.assertEquals(view.isModalOpen(),false);
    }


    @Test
    public void assertOnStateChangedCallsUpdateViews() throws Exception {
        setDataForTest("1985-07-24T00:00:00.000Z");
        view.onStateChanged(VaccineCard.State.DONE_CAN_BE_UNDONE);
        //calls updateViews which sets the adapter, we can check the the adapter is not null
        Assert.assertNotNull(view.getAllVaccineWrappers());
    }

    public void setDataForTest(String dateTimeString) throws Exception {
        wrappers = new ArrayList<VaccineWrapper>();
        wrapper= new VaccineWrapper();
        wrapper.setName(VaccineRepo.Vaccine.bcg2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.bcg2);
        wrappers.add(wrapper);
        wrapper= new VaccineWrapper();
        wrapper.setVaccine(VaccineRepo.Vaccine.opv1);
        wrapper.setName(VaccineRepo.Vaccine.opv1.display());
        wrappers.add(wrapper);
        wrapper= new VaccineWrapper();
        wrapper.setName(VaccineRepo.Vaccine.measles2.display());
        wrapper.setVaccine(VaccineRepo.Vaccine.measles2);
        wrappers.add(wrapper);
        JSONArray vaccineArray = new JSONArray(VaccineData.vaccines);
        vaccineData = vaccineArray.getJSONObject(0);
        HashMap<String,String>detail = new HashMap<String,String>();
        detail.put("dob",dateTimeString);
        childdetails = new CommonPersonObjectClient("1",detail,"NME");
        childdetails.setColumnmaps(detail);
        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.measles2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        Alert alert = new Alert("","","", AlertStatus.complete,"","");
        vaccinelist= new ArrayList<Vaccine>();
        vaccinelist.add(vaccine);
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.bcg2.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinelist.add(vaccine);
        vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineRepo.Vaccine.opv1.display(), 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineRepository.TYPE_Synced, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        vaccinelist.add(vaccine);
        alertlist =  new ArrayList<Alert>();
        alertlist.add(alert);
        view.setData(vaccineData,childdetails,vaccinelist,alertlist);
    }

    @Test
    public void asertConstructorsNotNull(){
        Assert.assertNotNull(new VaccineGroup(RuntimeEnvironment.application));
        Assert.assertNotNull(new VaccineGroup(RuntimeEnvironment.application, ViewAttributes.attrs));
        Assert.assertNotNull(new VaccineGroup(RuntimeEnvironment.application, ViewAttributes.attrs,0));
        Assert.assertNotNull(new VaccineGroup(RuntimeEnvironment.application, ViewAttributes.attrs,0,0));
    }

}
