package org.smartregister.immunization.domain;

import org.joda.time.DateTime;
import org.smartregister.domain.Alert;
import org.smartregister.domain.Photo;
import org.smartregister.immunization.db.VaccineRepo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by keyman on 16/11/2016.
 */
public class VaccineWrapper implements Serializable {
    private String id;
    private Long dbKey;
    private Photo photo;
    private String name;
    private String defaultName;
    private String gender;
    private String status;
    private VaccineRepo.Vaccine vaccine;
    private DateTime vaccineDate;
    private Alert alert;
    private String previousVaccineId;
    private String notGivenCondition;
    private boolean compact;

    private String color;
    private String formattedVaccineDate;
    private String existingAge;

    private String patientName;
    private String patientNumber;

    private DateTime updatedVaccineDate;

    private boolean today;
    private boolean synced;

    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDbKey() {
        return dbKey;
    }

    public void setDbKey(Long dbKey) {
        this.dbKey = dbKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VaccineRepo.Vaccine getVaccine() {
        return vaccine;
    }

    public void setVaccine(VaccineRepo.Vaccine vaccine) {
        this.vaccine = vaccine;
    }

    public DateTime getVaccineDate() {
        return vaccineDate;
    }

    public void setVaccineDate(DateTime vaccineDate) {
        this.vaccineDate = vaccineDate;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreviousVaccineId() {
        return previousVaccineId;
    }

    public void setPreviousVaccine(String previousVaccineId) {
        this.previousVaccineId = previousVaccineId;
    }

    public String getNotGivenCondition() {
        return notGivenCondition;
    }

    public void setNotGivenCondition(String notGivenCondition) {
        this.notGivenCondition = notGivenCondition;
    }

    public boolean isCompact() {
        return compact;
    }

    public void setCompact(boolean compact) {
        this.compact = compact;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFormattedVaccineDate() {
        return formattedVaccineDate;
    }

    public void setFormattedVaccineDate(String formattedVaccineDate) {
        this.formattedVaccineDate = formattedVaccineDate;
    }

    public String getExistingAge() {
        return existingAge;
    }

    public void setExistingAge(String existingAge) {
        this.existingAge = existingAge;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public DateTime getUpdatedVaccineDate() {
        return updatedVaccineDate;
    }

    public void setUpdatedVaccineDate(DateTime updatedVaccineDate, boolean today) {
        this.today = today;
        this.updatedVaccineDate = updatedVaccineDate;
    }

    public boolean isToday() {
        return today;
    }

    public String getVaccineDateAsString() {
        return vaccineDate != null ? vaccineDate.toString("yyyy-MM-dd") : "";
    }

    public String getUpdatedVaccineDateAsString() {
        return updatedVaccineDate != null ? updatedVaccineDate.toString("yyyy-MM-dd") : "";
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public void setDefaultName(String defaultName) {
        this.defaultName = defaultName;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
