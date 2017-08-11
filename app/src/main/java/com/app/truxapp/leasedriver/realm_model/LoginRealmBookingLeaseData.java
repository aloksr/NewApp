package com.app.truxapp.leasedriver.realm_model;


import io.realm.RealmObject;

public class LoginRealmBookingLeaseData extends RealmObject {
    private String bookingLeaseId;
    private String driverId;
    private String vehicleId;
    private String companyId;
    private String journeyStartDate;
    private String journeyEndDate;
    private double fromJrLat;
    private double fromJrLong;
    private String  fromJrLocation;
    private double toJrLat;
    private double toJrLong;
    private String toJrLocation;
    private String totalDistance;
    private String totalDuration;
    private String bookingLsStatus;
    private String createdDateTime;
    private String createdBy;
    private String updatedDateTime;
    private String modifiedBy;
    private String driverMobile;
    private String deviceBookingLeasesId;
    private String clientOrderNumber;
    private String clientOrderDocUrl;
    private String clientOrderDocUrl2;
    private String clientOrderDocUrl3;
    private String clientOrderDocUrl4;
    private String clientRequestId;
    private String boxToBeDelivered;
    private String totalBoxes;
    private String successfulBoxDelivery;
    private String rejectedBoxDelivery;
    private String attemptedBoxDelivery;
    private String summaryReportImage;
    private String associateId;
    private String regionId;
    private String journeyStartDateConvert;
    private String journeyEndDateConvert;

    public String getBookingLeaseId() {
        return bookingLeaseId;
    }

    public void setBookingLeaseId(String bookingLeaseId) {
        this.bookingLeaseId = bookingLeaseId;
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


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getJourneyStartDate() {
        return journeyStartDate;
    }

    public void setJourneyStartDate(String journeyStartDate) {
        this.journeyStartDate = journeyStartDate;
    }

    public String getJourneyEndDate() {
        return journeyEndDate;
    }

    public void setJourneyEndDate(String journeyEndDate) {
        this.journeyEndDate = journeyEndDate;
    }

    public double getFromJrLat() {
        return fromJrLat;
    }

    public void setFromJrLat(double fromJrLat) {
        this.fromJrLat = fromJrLat;
    }

    public double getFromJrLong() {
        return fromJrLong;
    }

    public void setFromJrLong(double fromJrLong) {
        this.fromJrLong = fromJrLong;
    }

    public String getFromJrLocation() {
        return fromJrLocation;
    }

    public void setFromJrLocation(String fromJrLocation) {
        this.fromJrLocation = fromJrLocation;
    }

    public double getToJrLat() {
        return toJrLat;
    }

    public void setToJrLat(double toJrLat) {
        this.toJrLat = toJrLat;
    }

    public double getToJrLong() {
        return toJrLong;
    }

    public void setToJrLong(double toJrLong) {
        this.toJrLong = toJrLong;
    }

    public String getToJrLocation() {
        return toJrLocation;
    }

    public void setToJrLocation(String toJrLocation) {
        this.toJrLocation = toJrLocation;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getBookingLsStatus() {
        return bookingLsStatus;
    }

    public void setBookingLsStatus(String bookingLsStatus) {
        this.bookingLsStatus = bookingLsStatus;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(String updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public String getDeviceBookingLeasesId() {
        return deviceBookingLeasesId;
    }

    public void setDeviceBookingLeasesId(String deviceBookingLeasesId) {
        this.deviceBookingLeasesId = deviceBookingLeasesId;
    }

    public String getClientOrderNumber() {
        return clientOrderNumber;
    }

    public void setClientOrderNumber(String clientOrderNumber) {
        this.clientOrderNumber = clientOrderNumber;
    }

    public String getClientOrderDocUrl() {
        return clientOrderDocUrl;
    }

    public void setClientOrderDocUrl(String clientOrderDocUrl) {
        this.clientOrderDocUrl = clientOrderDocUrl;
    }

    public String getClientOrderDocUrl2() {
        return clientOrderDocUrl2;
    }

    public void setClientOrderDocUrl2(String clientOrderDocUrl2) {
        this.clientOrderDocUrl2 = clientOrderDocUrl2;
    }

    public String getClientOrderDocUrl3() {
        return clientOrderDocUrl3;
    }

    public void setClientOrderDocUrl3(String clientOrderDocUrl3) {
        this.clientOrderDocUrl3 = clientOrderDocUrl3;
    }

    public String getClientOrderDocUrl4() {
        return clientOrderDocUrl4;
    }

    public void setClientOrderDocUrl4(String clientOrderDocUrl4) {
        this.clientOrderDocUrl4 = clientOrderDocUrl4;
    }

    public String getClientRequestId() {
        return clientRequestId;
    }

    public void setClientRequestId(String clientRequestId) {
        this.clientRequestId = clientRequestId;
    }

    public String getBoxToBeDelivered() {
        return boxToBeDelivered;
    }

    public void setBoxToBeDelivered(String boxToBeDelivered) {
        this.boxToBeDelivered = boxToBeDelivered;
    }

    public String getTotalBoxes() {
        return totalBoxes;
    }

    public void setTotalBoxes(String totalBoxes) {
        this.totalBoxes = totalBoxes;
    }

    public String getSuccessfulBoxDelivery() {
        return successfulBoxDelivery;
    }

    public void setSuccessfulBoxDelivery(String successfulBoxDelivery) {
        this.successfulBoxDelivery = successfulBoxDelivery;
    }

    public String getRejectedBoxDelivery() {
        return rejectedBoxDelivery;
    }

    public void setRejectedBoxDelivery(String rejectedBoxDelivery) {
        this.rejectedBoxDelivery = rejectedBoxDelivery;
    }

    public String getAttemptedBoxDelivery() {
        return attemptedBoxDelivery;
    }

    public void setAttemptedBoxDelivery(String attemptedBoxDelivery) {
        this.attemptedBoxDelivery = attemptedBoxDelivery;
    }

    public String getSummaryReportImage() {
        return summaryReportImage;
    }

    public void setSummaryReportImage(String summaryReportImage) {
        this.summaryReportImage = summaryReportImage;
    }

    public String getAssociateId() {
        return associateId;
    }

    public void setAssociateId(String associateId) {
        this.associateId = associateId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getJourneyStartDateConvert() {
        return journeyStartDateConvert;
    }

    public void setJourneyStartDateConvert(String journeyStartDateConvert) {
        this.journeyStartDateConvert = journeyStartDateConvert;
    }

    public String getJourneyEndDateConvert() {
        return journeyEndDateConvert;
    }

    public void setJourneyEndDateConvert(String journeyEndDateConvert) {
        this.journeyEndDateConvert = journeyEndDateConvert;
    }


}
