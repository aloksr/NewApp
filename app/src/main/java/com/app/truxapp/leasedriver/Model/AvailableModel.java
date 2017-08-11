package com.app.truxapp.leasedriver.Model;

import java.io.Serializable;
public class AvailableModel implements Serializable {
    private String tiContactNumber;
    private String orderId;
    private String tiLastName;
    private String destination;
    private String branchAddress;
    private String source;
    private String tiFirstName;
    private String orderDate;

    public String getTiContactNumber() {
        return tiContactNumber;
    }

    public void setTiContactNumber(String tiContactNumber) {
        this.tiContactNumber = tiContactNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTiLastName() {
        return tiLastName;
    }

    public void setTiLastName(String tiLastName) {
        this.tiLastName = tiLastName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTiFirstName() {
        return tiFirstName;
    }

    public void setTiFirstName(String tiFirstName) {
        this.tiFirstName = tiFirstName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
