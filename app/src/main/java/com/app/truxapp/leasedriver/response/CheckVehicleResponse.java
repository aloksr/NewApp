package com.app.truxapp.leasedriver.response;

import com.app.truxapp.leasedriver.Model.CheckVehicleModel;



public class CheckVehicleResponse extends BaseResponce {
    private CheckVehicleModel data;

    public CheckVehicleModel getData() {
        return data;
    }

    public void setData(CheckVehicleModel data) {
        this.data = data;
    }



}
