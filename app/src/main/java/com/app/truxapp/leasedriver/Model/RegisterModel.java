package com.app.truxapp.leasedriver.Model;

import java.io.Serializable;

/**
 * Created by sharadsingh on 18/07/17.
 */

public class RegisterModel implements Serializable {
    private  String prefillDriverNumber;
    private  String prefillDriverName;
    private  String prefillDriverId;

    public String getPrefillDriverNumber() {
        return prefillDriverNumber;
    }

    public void setPrefillDriverNumber(String prefillDriverNumber) {
        this.prefillDriverNumber = prefillDriverNumber;
    }

    public String getPrefillDriverName() {
        return prefillDriverName;
    }

    public void setPrefillDriverName(String prefillDriverName) {
        this.prefillDriverName = prefillDriverName;
    }

    public String getPrefillDriverId() {
        return prefillDriverId;
    }

    public void setPrefillDriverId(String prefillDriverId) {
        this.prefillDriverId = prefillDriverId;
    }



}
