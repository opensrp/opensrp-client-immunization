package org.smartregister.immunization.utils;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import androidx.test.core.app.ApplicationProvider;
import org.smartregister.immunization.BaseUnitTest;
import org.smartregister.immunization.ImmunizationLibrary;
import org.smartregister.immunization.R;
import org.smartregister.immunization.domain.ServiceRecord;
import org.smartregister.immunization.domain.ServiceWrapper;
import org.smartregister.immunization.repository.RecurringServiceRecordRepository;
import org.smartregister.immunization.util.RecurringServiceUtils;
import org.smartregister.immunization.view.ServiceGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ndegwamartin on 2020-03-23.
 */

@PrepareForTest({ImmunizationLibrary.class})
public class RecurringServiceUtilsTest extends BaseUnitTest {

    private static final String SG_BETA = "SG Beta";

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private RecurringServiceRecordRepository recurringServiceRecordRepository;
    @Mock
    private ImmunizationLibrary immunizationLibrary;

    @Before
    public void setUp() {
        org.mockito.MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetLastOpenedServiceViewReturnsCorrectView() {
        List<ServiceGroup> serviceGroups = new ArrayList<>();

        ServiceGroup serviceGroup = new ServiceGroup(ApplicationProvider.getApplicationContext());
        serviceGroup.setModalOpen(false);
        serviceGroup.setTag(R.id.key, "SG Alpha");
        serviceGroups.add(serviceGroup);

        serviceGroup = new ServiceGroup(ApplicationProvider.getApplicationContext());
        serviceGroup.setModalOpen(true);
        serviceGroup.setTag(R.id.key, SG_BETA);
        serviceGroups.add(serviceGroup);

        serviceGroup = new ServiceGroup(ApplicationProvider.getApplicationContext());
        serviceGroup.setModalOpen(false);
        serviceGroup.setTag(R.id.key, "SG Gamma");
        serviceGroups.add(serviceGroup);

        ServiceGroup lastOpenedServiceView = RecurringServiceUtils.getLastOpenedServiceView(serviceGroups);

        Assert.assertNotNull(lastOpenedServiceView);

        Assert.assertEquals(SG_BETA, lastOpenedServiceView.getTag(R.id.key));

    }

    @Test
    public void testCanSaveService() {
        PowerMockito.mockStatic(ImmunizationLibrary.class);
        PowerMockito.when(ImmunizationLibrary.getInstance()).thenReturn(immunizationLibrary);
        PowerMockito.when(immunizationLibrary.recurringServiceRecordRepository()).thenReturn(recurringServiceRecordRepository);
        ServiceWrapper tag = new ServiceWrapper();
        tag.setUpdatedVaccineDate(new DateTime(), true);
        String baseEntityId = "test-entity-id";
        String providerId = "testProvider";
        String locationId = "test-location-id";
        RecurringServiceUtils.saveService(tag, baseEntityId, providerId, locationId,
               "testTeam", "testTeamId", "testChildLocation");
        Mockito.verify(recurringServiceRecordRepository, Mockito.times(1)).add((ServiceRecord) ArgumentMatchers.any());
    }
}

