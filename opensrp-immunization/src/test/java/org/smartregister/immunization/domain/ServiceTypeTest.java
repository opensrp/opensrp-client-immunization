package org.smartregister.immunization.domain;

import org.junit.Test;
import org.smartregister.immunization.BaseUnitTest;

import static org.junit.Assert.assertEquals;

/**
 * Created by onaio on 30/08/2017.
 */

public class ServiceTypeTest extends BaseUnitTest {
    public static final String TYPE = "type";
    public static final String PREOFFSET = "PreOffset";
    public static final String EXPIRYOFFSET = "ExpiryOffset";
    public static final String MILESTONEOFFSET = "MilestoneOffset";
    public static final String NAME = "name";
    public static final String SERVICE_GROUP = "service_group";
    public static final String SERVICENAMEENTITY = "serviceNameEntity";
    public static final String SERVICENAMEENTITYID = "serviceNameEntityId";
    public static final String DATEENTITY = "dateEntity";
    public static final String DATEENTITYID = "dateEntityId";
    public static final String UNITS = "units";
    public static final String SERVICELOGIC = "serviceLogic";
    public static final String PREREQUISITE = "prerequisite";


    @Test
    public void assertBuilderInitializesAllFields() {
        Long id = 0L;
        String type = "type";
        String name = "name";
        String serviceGroup = "serviceGroup";
        String serviceNameEntity = "serviceNameEntity";
        String serviceNameEntityId = "serviceNameEntityId";
        String dateEntity = "dateEntity";
        String dateEntityId = "dateEntityId";
        String units = "units";
        String serviceLogic = "serviceLogic";
        String prerequisite = "prerequisite";
        String preOffset = "preOffset";
        String expiryOffset = "expiryOffset";
        String milestoneOffset = "milestoneOffset";
        Long updatedAt = 11L;

        ServiceType serviceType = new ServiceType.Builder(id, type, name)
                .withServiceGroup(serviceGroup)
                .withServiceNameEntity(serviceNameEntity)
                .withServiceNameEntityId(serviceNameEntityId)
                .withDateEntity(dateEntity)
                .withDateEntityId(dateEntityId)
                .withUnits(units)
                .withServiceLogic(serviceLogic)
                .withPrerequisite(prerequisite)
                .withPreOffset(preOffset)
                .withExpiryOffset(expiryOffset)
                .withMilestoneOffset(milestoneOffset)
                .withUpdatedAt(updatedAt).build();

        assertEquals(serviceType.getId(), id);
        assertEquals(serviceType.getType(), type);
        assertEquals(serviceType.getName(), name);
        assertEquals(serviceType.getServiceGroup(), serviceGroup);
        assertEquals(serviceType.getServiceNameEntity(), serviceNameEntity);
        assertEquals(serviceType.getServiceNameEntityId(), serviceNameEntityId);
        assertEquals(serviceType.getDateEntity(), dateEntity);
        assertEquals(serviceType.getDateEntityId(), dateEntityId);
        assertEquals(serviceType.getUnits(), units);
        assertEquals(serviceType.getServiceLogic(), serviceLogic);
        assertEquals(serviceType.getPrerequisite(), prerequisite);
        assertEquals(serviceType.getPreOffset(), preOffset);
        assertEquals(serviceType.getExpiryOffset(), expiryOffset);
        assertEquals(serviceType.getMilestoneOffset(), milestoneOffset);
        assertEquals(serviceType.getUpdatedAt(), updatedAt);
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
