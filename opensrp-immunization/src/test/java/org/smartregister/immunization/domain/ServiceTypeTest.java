package org.smartregister.immunization.domain;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceTypeTest extends BaseUnitTest {
    public static final String TYPE = "type";
    public static final String PREOFFSET = "PreOffset";
    public static final String EXPIRYOFFSET = "ExpiryOffset";
    public static final String MILESTONEOFFSET = "MilestoneOffset";
    public static final String NAME = "name";
    public static final String SERVICENAMEENTITY = "serviceNameEntity";
    public static final String SERVICENAMEENTITYID = "serviceNameEntityId";
    public static final String DATEENTITY = "dateEntity";
    public static final String DATEENTITYID = "dateEntityId";
    public static final String UNITS = "units";
    public static final String SERVICELOGIC = "serviceLogic";
    public static final String PREREQUISITE = "prerequisite";

    @Test
    public void assertDefaultConstructorsCreateNonNullObjectOnInstantiation() {
        junit.framework.Assert.assertNotNull(new ServiceType());
        junit.framework.Assert.assertNotNull(new ServiceType(0l, TYPE, NAME, SERVICENAMEENTITY, SERVICENAMEENTITYID, DATEENTITY, DATEENTITYID, UNITS, SERVICELOGIC, PREREQUISITE, "preOffset", "expiryOffset", "milestoneOffset", 0l));
    }

    @Test
    public void assertCheckGettersandSetters() {
        ServiceType serviceType = new ServiceType();

        serviceType.setId(0l);
        junit.framework.Assert.assertEquals(0l, serviceType.getId().longValue());

        serviceType.setType(TYPE);
        junit.framework.Assert.assertEquals(TYPE, serviceType.getType());

        serviceType.setName(NAME);
        junit.framework.Assert.assertEquals(NAME, serviceType.getName());

        serviceType.setServiceNameEntity(SERVICENAMEENTITY);
        junit.framework.Assert.assertEquals(SERVICENAMEENTITY, serviceType.getServiceNameEntity());

        serviceType.setServiceNameEntityId(SERVICENAMEENTITYID);
        junit.framework.Assert.assertEquals(SERVICENAMEENTITYID, serviceType.getServiceNameEntityId());

        serviceType.setDateEntity(DATEENTITY);
        junit.framework.Assert.assertEquals(DATEENTITY, serviceType.getDateEntity());

        serviceType.setDateEntityId(DATEENTITYID);
        junit.framework.Assert.assertEquals(DATEENTITYID, serviceType.getDateEntityId());

        serviceType.setUnits(UNITS);
        junit.framework.Assert.assertEquals(UNITS, serviceType.getUnits());

        serviceType.setServiceLogic(SERVICELOGIC);
        junit.framework.Assert.assertEquals(SERVICELOGIC, serviceType.getServiceLogic());

        serviceType.setPrerequisite(PREREQUISITE);
        junit.framework.Assert.assertEquals(PREREQUISITE, serviceType.getPrerequisite());

        serviceType.setPreOffset(PREOFFSET);
        junit.framework.Assert.assertEquals(PREOFFSET, serviceType.getPreOffset());

        serviceType.setExpiryOffset(EXPIRYOFFSET);
        junit.framework.Assert.assertEquals(EXPIRYOFFSET, serviceType.getExpiryOffset());

        serviceType.setMilestoneOffset(MILESTONEOFFSET);
        junit.framework.Assert.assertEquals(MILESTONEOFFSET, serviceType.getMilestoneOffset());

        serviceType.setUpdatedAt(0l);
        junit.framework.Assert.assertEquals(0l, serviceType.getUpdatedAt().longValue());
    }
}
