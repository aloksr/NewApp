package com.app.truxapp.leasedriver.Model;

import java.io.Serializable;


public class ResumeJourneyDataModel implements Serializable {
    private String loginStatus;
    private String driverStatus;
    private String bookingId;
    AllowedFeatureModel [] allowedFeatures;

    public AllowedFeatureModel[] getAllowedFeatures() {
        return allowedFeatures;
    }

    public void setAllowedFeatures(AllowedFeatureModel[] allowedFeatures) {
        this.allowedFeatures = allowedFeatures;
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
}
