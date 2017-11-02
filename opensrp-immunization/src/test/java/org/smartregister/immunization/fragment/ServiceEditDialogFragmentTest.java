package org.smartregister.immunization.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.smartregister.domain.Alert;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.BuildConfig;
import org.smartregister.immunization.R;
import org.smartregister.immunization.customshadows.FontTextViewShadow;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceRecordTest;
import org.smartregister.immunization.domain.ServiceType;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.domain.ServiceWrapperTest;
import org.smartregister.immunization.fragment.mock.FragmentUtilActivityUsingServiceActionListener;
import org.smartregister.immunization.util.ImageUtils;
import org.smartregister.util.OpenSRPImageListener;
import org.smartregister.util.OpenSRPImageLoader;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.util.FragmentTestUtil.startFragment;
/**
 * Created by onaio on 30/08/2017.
 */

@Config(shadows = {FontTextViewShadow.class})
public class ServiceEditDialogFragmentTest extends BaseUnitTest {


//    @Rule
//    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception {
//        view = org.powermock.api.mockito.PowerMockito.mock(LinearLayout.class);
        initMocks(this);
    }

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        View view = Mockito.mock(View.class);
        junit.framework.Assert.assertNotNull(ServiceEditDialogFragment.newInstance(Collections.EMPTY_LIST, new ServiceWrapper(), view));
        junit.framework.Assert.assertNotNull(ServiceEditDialogFragment.newInstance(new DateTime(),Collections.EMPTY_LIST, new ServiceWrapper(), view,true));
    }

    @Test
    public void onCreateViewTest() throws Exception {

        DateTime datetime = new DateTime();
        Alert alert = Mockito.mock(Alert.class);
        List<ServiceRecord> issuedServices = new ArrayList<ServiceRecord>();
        ServiceRecord serviceRecord = new ServiceRecord(0l, ServiceRecordTest.BASEENTITYID, ServiceRecordTest.PROGRAMCLIENTID, 0l, ServiceRecordTest.VALUE, new Date(), ServiceRecordTest.ANMID, ServiceRecordTest.LOCATIONID, ServiceRecordTest.SYNCED, ServiceRecordTest.EVENTID, ServiceRecordTest.FORMSUBMISSIONID, 0l);

        issuedServices.add(serviceRecord);
        ServiceWrapper tag = new ServiceWrapper();
//        tag.setId(ServiceWrapperTest.ID);
        tag.setDbKey(0l);
        tag.setStatus(ServiceWrapperTest.STATUS);
        tag.setVaccineDate(datetime);
        tag.setAlert(alert);
        tag.setDefaultName(ServiceWrapperTest.DEFAULTNAME);
        tag.setPreviousVaccine(ServiceWrapperTest.ID);
        tag.setColor(ServiceWrapperTest.COLOR);
        tag.setDob(datetime);
        ServiceType serviceType = new ServiceType();
        serviceType.setUnits("units");
        serviceType.setType("type");
        serviceType.setId(0l);
        tag.setServiceType(serviceType);
        serviceType.setName(ServiceWrapperTest.NAME);
        tag.setValue(ServiceWrapperTest.VALUE);
        tag.setPatientName(ServiceWrapperTest.PATIENTNAME);
        tag.setUpdatedVaccineDate(datetime, true);
        tag.setPatientNumber(ServiceWrapperTest.NUMBER);
        Photo photo = Mockito.mock(Photo.class);
        tag.setPhoto(photo);
        tag.setGender(ServiceWrapperTest.GENDER);
        tag.setSynced(true);


//        PowerMockito.doNothing().when(Mockito.spy(serviceEditDialogFragment)).onAttach(any(Activity.class));
        //startFragment(serviceEditDialogFragment);
//        Assert.assertNotNull(serviceEditDialogFragment);
        FragmentUtilActivityUsingServiceActionListener activity = Robolectric.buildActivity( FragmentUtilActivityUsingServiceActionListener.class )
                .create()
                .start()
                .resume()
                .get();
        View view = new LinearLayout(activity);
        ServiceEditDialogFragment fragment = ServiceEditDialogFragment.newInstance(new DateTime(),issuedServices, tag, view,true);
        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();
    }

    public static void startFragment( Fragment fragment )
    {

    }




}
