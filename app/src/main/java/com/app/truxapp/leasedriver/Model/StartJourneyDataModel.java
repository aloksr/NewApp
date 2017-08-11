package com.app.truxapp.leasedriver.Model;

import java.io.Serializable;


public class StartJourneyDataModel implements Serializable {
    private AllowedFeatureModel [] allowedFeatures;
    private  String bookingId;
    private  String loginStatus;
    private  String driverStatus;
    private  String dstatus;

    public String getDstatus() {
        return dstatus;
    }

    public void setDstatus(String dstatus) {
        this.dstatus = dstatus;
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

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    //"bookingId"


    public AllowedFeatureModel[] getAllowedFeatures() {
        return allowedFeatures;
    }

    public void setAllowedFeatures(AllowedFeatureModel[] allowedFeatures) {
        this.allowedFeatures = allowedFeatures;
    }
}
