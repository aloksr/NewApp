package com.app.truxapp.leasedriver.realm_model;

import io.realm.RealmObject;

public class LoginRealmCommonData extends RealmObject {

    private String id;
    private String driverId;
    private String vehicleId;
    private String deviceId;
    private String driverPhoneNumber;
    private String deviceUUID;
    private String vehicleNumber;
    private String vehicleType;
    private String driverName;
    private int loginStatus;
    private int driverStatus;
    private String lastLoginTime;
    private String lastLogoutTime;
    private String driver_apk_version;
    private String documentuploadurl;
    private String loginId;
    private String bookingId;
    private String subClientId;
    private String dstatus;
    private String trackingDeviceId;
    private String channelFrom;
    private String modefrom;
    private String vtsClient;
    private String isValidDriver;
    private String token;
    private String driverMessage;
    private String driverLoginDate;
    private String driverLoginTime;
    private String driverLogoutDate;
    private String driverLogoutTime;
    private String driverLoginDurationTime;
    private String latestApkVersion;
    private String apkUrl;
    private String driverImage;
    private String driverClientName;
    private String driverClientId;
    private String loginOpeningKM;
    private String loginClosingKM;
    private String lastLoginOpeningKM;
    private String lastLoginClosingKM;
    private int isBarCodeIssued;
    private String currentServerTime;
    private String bodyType;
    private String bookingClientRequestId;
    private String help_url;
    private String insuranceImgUrl;
    private String pollutionImgUrl;
    private String rcImgUrl;
    private String subClientName;
    private boolean docsRequiredAtJourneyStart;
    private int showAssociateMenu;
    private String journeyStartTime;
    private String destination;
    private boolean isReadyToUnload;
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

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }

    public int getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(int driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(String lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public String getDriver_apk_version() {
        return driver_apk_version;
    }

    public void setDriver_apk_version(String driver_apk_version) {
        this.driver_apk_version = driver_apk_version;
    }

    public String getDocumentuploadurl() {
        return documentuploadurl;
    }

    public void setDocumentuploadurl(String documentuploadurl) {
        this.documentuploadurl = documentuploadurl;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getSubClientId() {
        return subClientId;
    }

    public void setSubClientId(String subClientId) {
        this.subClientId = subClientId;
    }

    public String getDstatus() {
        return dstatus;
    }

    public void setDstatus(String dstatus) {
        this.dstatus = dstatus;
    }

    public String getTrackingDeviceId() {
        return trackingDeviceId;
    }

    public void setTrackingDeviceId(String trackingDeviceId) {
        this.trackingDeviceId = trackingDeviceId;
    }

    public String getChannelFrom() {
        return channelFrom;
    }

    public void setChannelFrom(String channelFrom) {
        this.channelFrom = channelFrom;
    }

    public String getModefrom() {
        return modefrom;
    }

    public void setModefrom(String modefrom) {
        this.modefrom = modefrom;
    }

    public String getVtsClient() {
        return vtsClient;
    }

    public void setVtsClient(String vtsClient) {
        this.vtsClient = vtsClient;
    }

    public String getIsValidDriver() {
        return isValidDriver;
    }

    public void setIsValidDriver(String isValidDriver) {
        this.isValidDriver = isValidDriver;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDriverMessage() {
        return driverMessage;
    }

    public void setDriverMessage(String driverMessage) {
        this.driverMessage = driverMessage;
    }

    public String getDriverLoginDate() {
        return driverLoginDate;
    }

    public void setDriverLoginDate(String driverLoginDate) {
        this.driverLoginDate = driverLoginDate;
    }

    public String getDriverLoginTime() {
        return driverLoginTime;
    }

    public void setDriverLoginTime(String driverLoginTime) {
        this.driverLoginTime = driverLoginTime;
    }

    public String getDriverLogoutDate() {
        return driverLogoutDate;
    }

    public void setDriverLogoutDate(String driverLogoutDate) {
        this.driverLogoutDate = driverLogoutDate;
    }

    public String getDriverLogoutTime() {
        return driverLogoutTime;
    }

    public void setDriverLogoutTime(String driverLogoutTime) {
        this.driverLogoutTime = driverLogoutTime;
    }

    public String getDriverLoginDurationTime() {
        return driverLoginDurationTime;
    }

    public void setDriverLoginDurationTime(String driverLoginDurationTime) {
        this.driverLoginDurationTime = driverLoginDurationTime;
    }

    public String getLatestApkVersion() {
        return latestApkVersion;
    }

    public void setLatestApkVersion(String latestApkVersion) {
        this.latestApkVersion = latestApkVersion;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getDriverClientName() {
        return driverClientName;
    }

    public void setDriverClientName(String driverClientName) {
        this.driverClientName = driverClientName;
    }

    public String getDriverClientId() {
        return driverClientId;
    }

    public void setDriverClientId(String driverClientId) {
        this.driverClientId = driverClientId;
    }

    public String getLoginOpeningKM() {
        return loginOpeningKM;
    }

    public void setLoginOpeningKM(String loginOpeningKM) {
        this.loginOpeningKM = loginOpeningKM;
    }

    public String getLoginClosingKM() {
        return loginClosingKM;
    }

    public void setLoginClosingKM(String loginClosingKM) {
        this.loginClosingKM = loginClosingKM;
    }

    public String getLastLoginOpeningKM() {
        return lastLoginOpeningKM;
    }

    public void setLastLoginOpeningKM(String lastLoginOpeningKM) {
        this.lastLoginOpeningKM = lastLoginOpeningKM;
    }

    public String getLastLoginClosingKM() {
        return lastLoginClosingKM;
    }

    public void setLastLoginClosingKM(String lastLoginClosingKM) {
        this.lastLoginClosingKM = lastLoginClosingKM;
    }

    public int getIsBarCodeIssued() {
        return isBarCodeIssued;
    }

    public void setIsBarCodeIssued(int isBarCodeIssued) {
        this.isBarCodeIssued = isBarCodeIssued;
    }

    public String getCurrentServerTime() {
        return currentServerTime;
    }

    public void setCurrentServerTime(String currentServerTime) {
        this.currentServerTime = currentServerTime;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getBookingClientRequestId() {
        return bookingClientRequestId;
    }

    public void setBookingClientRequestId(String bookingClientRequestId) {
        this.bookingClientRequestId = bookingClientRequestId;
    }

    public String getHelp_url() {
        return help_url;
    }

    public void setHelp_url(String help_url) {
        this.help_url = help_url;
    }

    public String getInsuranceImgUrl() {
        return insuranceImgUrl;
    }

    public void setInsuranceImgUrl(String insuranceImgUrl) {
        this.insuranceImgUrl = insuranceImgUrl;
    }

    public String getPollutionImgUrl() {
        return pollutionImgUrl;
    }

    public void setPollutionImgUrl(String pollutionImgUrl) {
        this.pollutionImgUrl = pollutionImgUrl;
    }

    public String getRcImgUrl() {
        return rcImgUrl;
    }

    public void setRcImgUrl(String rcImgUrl) {
        this.rcImgUrl = rcImgUrl;
    }

    public String getSubClientName() {
        return subClientName;
    }

    public void setSubClientName(String subClientName) {
        this.subClientName = subClientName;
    }

    public boolean isDocsRequiredAtJourneyStart() {
        return docsRequiredAtJourneyStart;
    }

    public void setDocsRequiredAtJourneyStart(boolean docsRequiredAtJourneyStart) {
        this.docsRequiredAtJourneyStart = docsRequiredAtJourneyStart;
    }

    public int getShowAssociateMenu() {
        return showAssociateMenu;
    }

    public void setShowAssociateMenu(int showAssociateMenu) {
        this.showAssociateMenu = showAssociateMenu;
    }

    public String getJourneyStartTime() {
        return journeyStartTime;
    }

    public void setJourneyStartTime(String journeyStartTime) {
        this.journeyStartTime = journeyStartTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isReadyToUnload() {
        return isReadyToUnload;
    }

    public void setReadyToUnload(boolean readyToUnload) {
        isReadyToUnload = readyToUnload;
    }

}
