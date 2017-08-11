package com.app.truxapp.leasedriver.Model;

import java.io.Serializable;

/**
 * Created by sharadsingh on 18/07/17.
 */

public class CheckVehicleModel implements Serializable {
    private   String id;
    private   String driverId;
    private   String vehicleId;
    private   String driverPhoneNumber;
    private   String deviceUUID;
    private   String vehicleNumber;
    private   String driverName;
    private   String vehicleType;
    private   String loginStatus;
    private   String driverStatus;
    private   String lastLoginTime;
    private   String lastLogoutTime;
    private   String loginId;
    private   String bookingId;
    private   String subClientId;
    private   String trackingDeviceId;
    private   String token;
    private   String driverImage;
    private   String driverClientName;
    private   String driverClientId;
    private   String loginOpeningKM;
    private   String loginClosingKM;
    private   String lastLoginOpeningKM;
    private   String lastLoginClosingKM;
    private   String currentServerTime;
    private   String subClientName;
    private   String journeyStartTime;
    private   String prefillDriverName;
    private   int prefillDriverId;
    private  String prefillDriverNumber;
    private  String dstatus;

    public String getDstatus() {
        return dstatus;
    }

    public void setDstatus(String dstatus) {
        this.dstatus = dstatus;
    }

    public AllowedFeatureModel[] getAllowedFeatures() {
        return allowedFeatures;
    }

    public void setAllowedFeatures(AllowedFeatureModel[] allowedFeatures) {
        this.allowedFeatures = allowedFeatures;
    }

    private  AllowedFeatureModel [] allowedFeatures;


    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

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

    public String getDeviceUUID() {
        return deviceUUID;
    }

    public void setDeviceUUID(String deviceUUID) {
        this.deviceUUID = deviceUUID;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
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

    public String getTrackingDeviceId() {
        return trackingDeviceId;
    }

    public void setTrackingDeviceId(String trackingDeviceId) {
        this.trackingDeviceId = trackingDeviceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getDriverClientId() {
        return driverClientId;
    }

    public void setDriverClientId(String driverClientId) {
        this.driverClientId = driverClientId;
    }

    public String getDriverClientName() {
        return driverClientName;
    }

    public void setDriverClientName(String driverClientName) {
        this.driverClientName = driverClientName;
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

    public String getCurrentServerTime() {
        return currentServerTime;
    }

    public void setCurrentServerTime(String currentServerTime) {
        this.currentServerTime = currentServerTime;
    }

    public String getSubClientName() {
        return subClientName;
    }

    public void setSubClientName(String subClientName) {
        this.subClientName = subClientName;
    }

    public String getJourneyStartTime() {
        return journeyStartTime;
    }

    public void setJourneyStartTime(String journeyStartTime) {
        this.journeyStartTime = journeyStartTime;
    }

    public String getPrefillDriverName() {
        return prefillDriverName;
    }

    public void setPrefillDriverName(String prefillDriverName) {
        this.prefillDriverName = prefillDriverName;
    }

    public int getPrefillDriverId() {
        return prefillDriverId;
    }

    public void setPrefillDriverId(int prefillDriverId) {
        this.prefillDriverId = prefillDriverId;
    }

    public String getPrefillDriverNumber() {
        return prefillDriverNumber;
    }

    public void setPrefillDriverNumber(String prefillDriverNumber) {
        this.prefillDriverNumber = prefillDriverNumber;
    }




}
