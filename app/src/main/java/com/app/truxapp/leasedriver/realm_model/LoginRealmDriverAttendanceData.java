package com.app.truxapp.leasedriver.realm_model;


import io.realm.RealmObject;

public class LoginRealmDriverAttendanceData extends RealmObject {
    private String id;
    private String driverId;
    private String companyId;
    private String punchIn;
    private String punchOut;
    private String createdDate;
    private String createdBy;
    private String modifiedDate;
    private String modifiedBy;
    private String openingKilometer;
    private String closingKilometer;
    private String tolltax;
    private String noofboxes;
    private String clientSubId;
    private String fromApp;
    private String vehicleNumber;
    private String driverAPKVersion;
    private String clientRequestDeploymentId;
    private String deviceUUID;
    private String checkPending;
    private String statusMessage;
    private String punchIngStatus;
    private String attendanceDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getPunchIn() {
        return punchIn;
    }

    public void setPunchIn(String punchIn) {
        this.punchIn = punchIn;
    }

    public String getPunchOut() {
        return punchOut;
    }

    public void setPunchOut(String punchOut) {
        this.punchOut = punchOut;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getOpeningKilometer() {
        return openingKilometer;
    }

    public void setOpeningKilometer(String openingKilometer) {
        this.openingKilometer = openingKilometer;
    }

    public String getClosingKilometer() {
        return closingKilometer;
    }

    public void setClosingKilometer(String closingKilometer) {
        this.closingKilometer = closingKilometer;
    }

    public String getTolltax() {
        return tolltax;
    }

    public void setTolltax(String tolltax) {
        this.tolltax = tolltax;
    }

    public String getNoofboxes() {
        return noofboxes;
    }

    public void setNoofboxes(String noofboxes) {
        this.noofboxes = noofboxes;
    }

    public String getClientSubId() {
        return clientSubId;
    }

    public void setClientSubId(String clientSubId) {
        this.clientSubId = clientSubId;
    }

    public String getFromApp() {
        return fromApp;
    }

    public void setFromApp(String fromApp) {
        this.fromApp = fromApp;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getDriverAPKVersion() {
        return driverAPKVersion;
    }

    public void setDriverAPKVersion(String driverAPKVersion) {
        this.driverAPKVersion = driverAPKVersion;
    }

    public String getClientRequestDeploymentId() {
        return clientRequestDeploymentId;
    }

    public void setClientRequestDeploymentId(String clientRequestDeploymentId) {
        this.clientRequestDeploymentId = clientRequestDeploymentId;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getCheckPending() {
        return checkPending;
    }

    public void setCheckPending(String checkPending) {
        this.checkPending = checkPending;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getPunchIngStatus() {
        return punchIngStatus;
    }

    public void setPunchIngStatus(String punchIngStatus) {
        this.punchIngStatus = punchIngStatus;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }



}
