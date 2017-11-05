package org.smartregister.immunization.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;

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
import org.smartregister.immunization.domain.Vaccine;
import org.smartregister.immunization.domain.VaccineData;
import org.smartregister.immunization.domain.VaccineTest;
import org.smartregister.immunization.fragment.mock.ServiceDialogFragmentTestActivity;
import org.smartregister.immunization.view.mock.VaccineGroupTestActivity;
import org.smartregister.util.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Created by onaio on 30/08/2017.
 */

public class VaccineGroupTest extends BaseUnitTest {

    @Mock
    private VaccineGroup vaccineGroup;

    @Mock
    private Context context;

    private ActivityController<VaccineGroupTestActivity> controller;

    @InjectMocks
    private VaccineGroupTestActivity activity;

    @Mock
    private org.smartregister.Context context_;

    @Before
    public void setUp() throws Exception {
        org.mockito.MockitoAnnotations.initMocks(this);
        Intent intent = new Intent(RuntimeEnvironment.application, VaccineGroupTestActivity.class);
        controller = Robolectric.buildActivity(VaccineGroupTestActivity.class, intent);
        activity = controller.start().resume().get();
        CoreLibrary.init(context_);
        controller.setup();

    }

    @Test
    public void testActivity(){
        Assert.assertNotNull(activity);
    }

    @Test
    public void testVaccineGroup() throws Exception{
        VaccineGroup v = activity.getInstance();
        Assert.assertNull(v.getChildDetails());
        Assert.assertNull(v.getVaccineData());
        Assert.assertNull(v.getVaccineList());
        Assert.assertNull(v.getAlertList());
        v.setAlertList(new ArrayList<Alert>());
        Assert.assertNotNull(v.getAlertList());
        v.setVaccineList(new ArrayList<Vaccine>());
        Assert.assertNotNull(v.getVaccineList());

        v.setData(null,null,null,null);
        v.setOnVaccineUndoClickListener(null);
        v.updateViews();
        JSONArray vaccineArray = new JSONArray(VaccineData.vaccines);
        JSONObject vaccineData = vaccineArray.getJSONObject(0);
        HashMap<String,String>detail = new HashMap<String,String>();
        detail.put("dob","1985-07-24T00:00:00.000Z");

        CommonPersonObjectClient childdetails = new CommonPersonObjectClient("1",detail,"NME");
        childdetails.setColumnmaps(detail);

        Vaccine vaccine = new Vaccine(0l, VaccineTest.BASEENTITYID, VaccineTest.NAME, 0, new Date(),
                VaccineTest.ANMID, VaccineTest.LOCATIONID, VaccineTest.SYNCSTATUS, VaccineTest.HIA2STATUS, 0l, VaccineTest.EVENTID, VaccineTest.FORMSUBMISSIONID, 0);
        Alert alert = new Alert("","","", AlertStatus.complete,"","");
        ArrayList<Vaccine>vaccinelist= new ArrayList<Vaccine>();
        vaccinelist.add(vaccine);
        ArrayList<Alert>alertlist =  new ArrayList<Alert>();
        alertlist.add(alert);

        v.setData(vaccineData,childdetails,vaccinelist,alertlist);


    }
    @Test
    public void testConstructors(){
        Assert.assertNotNull(activity.getInstance());
        Assert.assertNotNull(activity.getInstance1());
        Assert.assertNotNull(activity.getInstance2());
        Assert.assertNotNull(activity.getInstance3());
    }

    @After
    public void tearDown() {
        destroyController();
        activity = null;
        controller = null;

    }
    private void destroyController() {
        try {
            activity.finish();
            controller.pause().stop().destroy(); //destroy controller if we can

        } catch (Exception e) {
            Log.e(getClass().getCanonicalName(), e.getMessage());
        }

        System.gc();
    }

}
