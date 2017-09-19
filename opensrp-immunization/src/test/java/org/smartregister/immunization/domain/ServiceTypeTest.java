package org.smartregister.immunization.domain;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceTypeTest extends BaseUnitTest {

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        assertNotNull(new ServiceType());
        assertNotNull(new ServiceType(0l, "type", "name", "serviceNameEntity", "serviceNameEntityId", "dateEntity", "dateEntityId", "units", "serviceLogic", "prerequisite", "preOffset", "expiryOffset", "milestoneOffset", 0l));
    }

    @Test
    public void assertCheckGettersandSetters() {
        ServiceType serviceType = new ServiceType();

        serviceType.setId(0l);
        assertEquals(0l, serviceType.getId().longValue());

        serviceType.setType("type");
        assertEquals("type", serviceType.getType());

        serviceType.setName("name");
        assertEquals("name", serviceType.getName());

        serviceType.setServiceNameEntity("serviceNameEntity");
        assertEquals("serviceNameEntity", serviceType.getServiceNameEntity());

        serviceType.setServiceNameEntityId("serviceNameEntityId");
        assertEquals("serviceNameEntityId", serviceType.getServiceNameEntityId());

        serviceType.setDateEntity("dateEntity");
        assertEquals("dateEntity", serviceType.getDateEntity());

        serviceType.setDateEntityId("dateEntityId");
        assertEquals("dateEntityId", serviceType.getDateEntityId());

        serviceType.setUnits("units");
        assertEquals("units", serviceType.getUnits());

        serviceType.setServiceLogic("serviceLogic");
        assertEquals("serviceLogic", serviceType.getServiceLogic());

        serviceType.setPrerequisite("prerequisite");
        assertEquals("prerequisite", serviceType.getPrerequisite());

        serviceType.setPreOffset("PreOffset");
        assertEquals("PreOffset", serviceType.getPreOffset());

        serviceType.setExpiryOffset("ExpiryOffset");
        assertEquals("ExpiryOffset", serviceType.getExpiryOffset());

        serviceType.setMilestoneOffset("MilestoneOffset");
        assertEquals("MilestoneOffset", serviceType.getMilestoneOffset());

        serviceType.setUpdatedAt(0l);
        assertEquals(0l, serviceType.getUpdatedAt().longValue());

    }
}
