package com.app.truxapp.leasedriver.Model;

import java.io.Serializable;


public class TripDocUrl implements Serializable {

    private String docName;
    private String docAmount;
    private String docUrl;
    private String docNumber;

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocAmount() {
        return docAmount;
    }

    public void setDocAmount(String docAmount) {
        this.docAmount = docAmount;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(String docNumber) {
        this.docNumber = docNumber;
    }


}
