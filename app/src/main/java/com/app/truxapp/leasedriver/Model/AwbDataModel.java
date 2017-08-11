package com.app.truxapp.leasedriver.Model;

import java.io.Serializable;
public class AwbDataModel implements Serializable {
    private String id;
    private String awbNumber;
    private String orderNumber;
    private String consigneeName;
    private String consigneeContact;
    private String awbDate;
    private String awbAmount;
    private String ndr;
    private String createdDateTime;

    public String getConsigneeContact() {
        return consigneeContact;
    }

    public void setConsigneeContact(String consigneeContact) {
        this.consigneeContact = consigneeContact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getAwbDate() {
        return awbDate;
    }

    public void setAwbDate(String awbDate) {
        this.awbDate = awbDate;
    }

    public String getAwbAmount() {
        return awbAmount;
    }

    public void setAwbAmount(String awbAmount) {
        this.awbAmount = awbAmount;
    }

    public String getNdr() {
        return ndr;
    }

    public void setNdr(String ndr) {
        this.ndr = ndr;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }



}
