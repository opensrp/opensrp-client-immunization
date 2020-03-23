package org.smartregister.immunization.utils;

import org.junit.Assert;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.R;
import org.smartregister.immunization.util.RecurringServiceUtils;
import org.smartregister.immunization.view.ServiceGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ndegwamartin on 2020-03-23.
 */
public class RecurringServiceUtilsTest extends BaseUnitTest {

    private static final String SG_BETA = "SG Beta";

    @Test
    public void testGetLastOpenedServiceViewReturnsCorrectView() {

        List<ServiceGroup> serviceGroups = new ArrayList<>();

        ServiceGroup serviceGroup = new ServiceGroup(RuntimeEnvironment.application);
        serviceGroup.setModalOpen(false);
        serviceGroup.setTag(R.id.key, "SG Alpha");
        serviceGroups.add(serviceGroup);

        serviceGroup = new ServiceGroup(RuntimeEnvironment.application);
        serviceGroup.setModalOpen(true);
        serviceGroup.setTag(R.id.key, SG_BETA);
        serviceGroups.add(serviceGroup);

        serviceGroup = new ServiceGroup(RuntimeEnvironment.application);
        serviceGroup.setModalOpen(false);
        serviceGroup.setTag(R.id.key, "SG Gamma");
        serviceGroups.add(serviceGroup);

        ServiceGroup lastOpenedServiceView = RecurringServiceUtils.getLastOpenedServiceView(serviceGroups);

        Assert.assertNotNull(lastOpenedServiceView);

        Assert.assertEquals(SG_BETA, lastOpenedServiceView.getTag(R.id.key));

    }
}

