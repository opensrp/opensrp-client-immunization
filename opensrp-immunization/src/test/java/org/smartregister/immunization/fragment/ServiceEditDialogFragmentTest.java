package org.smartregister.immunization.fragment;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import junit.framework.Assert;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceEditDialogFragmentTest extends BaseUnitTest {
    private View view;

    @Before
    public void setUp() throws Exception {
        view = org.powermock.api.mockito.PowerMockito.mock(LinearLayout.class);
    }

    @Test
    public void assertThatCallToNewInstanceCreatesAFragment() {
        junit.framework.Assert.assertNotNull(ServiceEditDialogFragment.newInstance(Collections.EMPTY_LIST, new ServiceWrapper(), view));
        junit.framework.Assert.assertNotNull(ServiceEditDialogFragment.newInstance(new DateTime(),Collections.EMPTY_LIST, new ServiceWrapper(), view,true));
    }

    @Test
    public void updateDateRangesTest() throws Exception {
        List<ServiceRecord> issuedServices = new ArrayList<ServiceRecord>();
        ServiceRecord serviceRecord = new ServiceRecord();
        ServiceWrapper tag = new ServiceWrapper();

        View view = new View(RuntimeEnvironment.application);
        ServiceEditDialogFragment serviceEditDialogFragment = ServiceEditDialogFragment.newInstance(new DateTime(),issuedServices, null, view,true);
        Assert.assertNull(serviceEditDialogFragment.onCreateView(null,null,null));
        serviceEditDialogFragment = ServiceEditDialogFragment.newInstance(new DateTime(),issuedServices, tag, view,true);
        LinearLayout viewgroup = new LinearLayout(RuntimeEnvironment.application);
        LayoutInflater inflater = Mockito.mock(LayoutInflater.class);
//        Mockito.when(inflater.inflate(anyInt(),any(ViewGroup.class),anyBoolean())).thenReturn(viewgroup);
//        serviceEditDialogFragment.onCreateView(inflater,Mockito.mock(LinearLayout.class),Mockito.mock(Bundle.class));


    }


}
