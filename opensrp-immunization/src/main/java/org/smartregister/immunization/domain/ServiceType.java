package org.smartregister.immunization.domain;

/**
 * Created by keyman on 3/1/17.
 */
public class ServiceType {
    private Long id;
    private String type;
    private String name;
    private String serviceGroup;
    private String serviceNameEntity;
    private String serviceNameEntityId;
    private String dateEntity;
    private String dateEntityId;
    private String units;
    private String serviceLogic;
    private String prerequisite;
    private String preOffset;
    private String expiryOffset;
    private String milestoneOffset;
    private Long updatedAt;

    public ServiceType() {
    }

/*
    public ServiceType(Long id, String type, String name, String serviceGroup, String serviceNameEntity, String
            serviceNameEntityId, String dateEntity, String dateEntityId, String units, String
                               serviceLogic, String prerequisite, String preOffset, String expiryOffset, String
                               milestoneOffset, Long updatedAt) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.serviceGroup = serviceGroup;
        this.serviceNameEntity = serviceNameEntity;
        this.serviceNameEntityId = serviceNameEntityId;
        this.dateEntity = dateEntity;
        this.dateEntityId = dateEntityId;
        this.units = units;
        this.serviceLogic = serviceLogic;
        this.prerequisite = prerequisite;
        this.preOffset = preOffset;
        this.expiryOffset = expiryOffset;
        this.milestoneOffset = milestoneOffset;
        this.updatedAt = updatedAt;
    }
*/

    private ServiceType(Builder builder) {
        this.id = builder.id;
        this.type = builder.type;
        this.name = builder.name;
        this.serviceGroup = builder.serviceGroup;
        this.serviceNameEntity = builder.serviceNameEntity;
        this.serviceNameEntityId = builder.serviceNameEntityId;
        this.dateEntity = builder.dateEntity;
        this.dateEntityId = builder.dateEntityId;
        this.units = builder.units;
        this.serviceLogic = builder.serviceLogic;
        this.prerequisite = builder.prerequisite;
        this.preOffset = builder.preOffset;
        this.expiryOffset = builder.expiryOffset;
        this.milestoneOffset = builder.milestoneOffset;
        this.updatedAt = builder.updatedAt;
    }

    public static class Builder {
        private Long id;
        private String type;
        private String name;
        private String serviceGroup;
        private String serviceNameEntity;
        private String serviceNameEntityId;
        private String dateEntity;
        private String dateEntityId;
        private String units;
        private String serviceLogic;
        private String prerequisite;
        private String preOffset;
        private String expiryOffset;
        private String milestoneOffset;
        private Long updatedAt;

        public Builder(Long id, String type, String name) {
            this.id = id;
            this.type = type;
            this.name = name;
        }

        public Builder withServiceGroup(String serviceGroup) {
            this.serviceGroup = serviceGroup;
            return this;
        }

        public Builder withServiceNameEntity(String serviceNameEntity) {
            this.serviceNameEntity = serviceNameEntity;
            return this;
        }

        public Builder withServiceNameEntityId(String serviceNameEntityId) {
            this.serviceNameEntityId = serviceNameEntityId;
            return this;
        }

        public Builder withDateEntity(String dateEntity) {
            this.dateEntity = dateEntity;
            return this;
        }

        public Builder withDateEntityId(String dateEntityId) {
            this.dateEntityId = dateEntityId;
            return this;
        }

        public Builder withUnits(String units) {
            this.units = units;
            return this;
        }

        public Builder withServiceLogic(String serviceLogic) {
            this.serviceLogic = serviceLogic;
            return this;
        }

        public Builder withPrerequisite(String prerequisite) {
            this.prerequisite = prerequisite;
            return this;
        }

        public Builder withPreOffset(String preOffset) {
            this.preOffset = preOffset;
            return this;
        }

        public Builder withExpiryOffset(String expiryOffset) {
            this.expiryOffset = expiryOffset;
            return this;
        }

        public Builder withMilestoneOffset(String milestoneOffset) {
            this.milestoneOffset = milestoneOffset;
            return this;
        }

        public Builder withUpdatedAt(Long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ServiceType build() {
            return new ServiceType(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public String getServiceNameEntity() {
        return serviceNameEntity;
    }

    public void setServiceNameEntity(String serviceNameEntity) {
        this.serviceNameEntity = serviceNameEntity;
    }

    public String getServiceNameEntityId() {
        return serviceNameEntityId;
    }

    public void setServiceNameEntityId(String serviceNameEntityId) {
        this.serviceNameEntityId = serviceNameEntityId;
    }

    public String getDateEntity() {
        return dateEntity;
    }

    public void setDateEntity(String dateEntity) {
        this.dateEntity = dateEntity;
    }

    public String getDateEntityId() {
        return dateEntityId;
    }

    public void setDateEntityId(String dateEntityId) {
        this.dateEntityId = dateEntityId;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getServiceLogic() {
        return serviceLogic;
    }

    public void setServiceLogic(String serviceLogic) {
        this.serviceLogic = serviceLogic;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getPreOffset() {
        return preOffset;
    }

    public void setPreOffset(String preOffset) {
        this.preOffset = preOffset;
    }

    public String getExpiryOffset() {
        return expiryOffset;
    }

    public void setExpiryOffset(String expiryOffset) {
        this.expiryOffset = expiryOffset;
    }

    public String getMilestoneOffset() {
        return milestoneOffset;
    }

    public void setMilestoneOffset(String milestoneOffset) {
        this.milestoneOffset = milestoneOffset;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

}
