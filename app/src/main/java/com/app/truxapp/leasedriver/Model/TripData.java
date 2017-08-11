package com.app.truxapp.leasedriver.Model;

import java.io.Serializable;


public class TripData implements Serializable {

    private int bookingLeaseId;
    private String bookingStartDate;
    private String bookingStartTime;
    private String bookingEndDate;
    private String bookingEndTime;

    public int getBookingLeaseId() {
        return bookingLeaseId;
    }

    public void setBookingLeaseId(int bookingLeaseId) {
        this.bookingLeaseId = bookingLeaseId;
    }

    public String getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(String bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public String getBookingStartTime() {
        return bookingStartTime;
    }

    public void setBookingStartTime(String bookingStartTime) {
        this.bookingStartTime = bookingStartTime;
    }

    public String getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(String bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    public String getBookingEndTime() {
        return bookingEndTime;
    }

    public void setBookingEndTime(String bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }



}
