package org.smartregister.immunization.domain;

import org.junit.Assert;
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

        Assert.assertEquals(serviceType.getId(), id);
        Assert.assertEquals(serviceType.getType(), type);
        Assert.assertEquals(serviceType.getName(), name);
        Assert.assertEquals(serviceType.getServiceGroup(), serviceGroup);
        Assert.assertEquals(serviceType.getServiceNameEntity(), serviceNameEntity);
        Assert.assertEquals(serviceType.getServiceNameEntityId(), serviceNameEntityId);
        Assert.assertEquals(serviceType.getDateEntity(), dateEntity);
        Assert.assertEquals(serviceType.getDateEntityId(), dateEntityId);
        Assert.assertEquals(serviceType.getUnits(), units);
        Assert.assertEquals(serviceType.getServiceLogic(), serviceLogic);
        Assert.assertEquals(serviceType.getPrerequisite(), prerequisite);
        Assert.assertEquals(serviceType.getPreOffset(), preOffset);
        Assert.assertEquals(serviceType.getExpiryOffset(), expiryOffset);
        Assert.assertEquals(serviceType.getMilestoneOffset(), milestoneOffset);
        Assert.assertEquals(serviceType.getUpdatedAt(), updatedAt);
    }

    @Test
    public void assertCheckGettersandSetters() {
        ServiceType serviceType = new ServiceType();

        serviceType.setId(0l);
        Assert.assertEquals(0l, serviceType.getId().longValue());

        serviceType.setType(TYPE);
        Assert.assertEquals(TYPE, serviceType.getType());

        serviceType.setName(NAME);
        Assert.assertEquals(NAME, serviceType.getName());

        serviceType.setServiceNameEntity(SERVICENAMEENTITY);
        Assert.assertEquals(SERVICENAMEENTITY, serviceType.getServiceNameEntity());

        serviceType.setServiceNameEntityId(SERVICENAMEENTITYID);
        Assert.assertEquals(SERVICENAMEENTITYID, serviceType.getServiceNameEntityId());

        serviceType.setDateEntity(DATEENTITY);
        Assert.assertEquals(DATEENTITY, serviceType.getDateEntity());

        serviceType.setDateEntityId(DATEENTITYID);
        Assert.assertEquals(DATEENTITYID, serviceType.getDateEntityId());

        serviceType.setUnits(UNITS);
        Assert.assertEquals(UNITS, serviceType.getUnits());

        serviceType.setServiceLogic(SERVICELOGIC);
        Assert.assertEquals(SERVICELOGIC, serviceType.getServiceLogic());

        serviceType.setPrerequisite(PREREQUISITE);
        Assert.assertEquals(PREREQUISITE, serviceType.getPrerequisite());

        serviceType.setPreOffset(PREOFFSET);
        Assert.assertEquals(PREOFFSET, serviceType.getPreOffset());

        serviceType.setExpiryOffset(EXPIRYOFFSET);
        Assert.assertEquals(EXPIRYOFFSET, serviceType.getExpiryOffset());

        serviceType.setMilestoneOffset(MILESTONEOFFSET);
        Assert.assertEquals(MILESTONEOFFSET, serviceType.getMilestoneOffset());

        serviceType.setUpdatedAt(0l);
        Assert.assertEquals(0l, serviceType.getUpdatedAt().longValue());
    }
}
