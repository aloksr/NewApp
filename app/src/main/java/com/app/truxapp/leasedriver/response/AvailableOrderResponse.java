package com.app.truxapp.leasedriver.response;

import com.app.truxapp.leasedriver.Model.AvailableModel;

/**
 * Created by ravi on 9/8/17.
 */
public class AvailableOrderResponse extends BaseResponce {

    private AvailableModel data ;

    public AvailableModel getData() {
        return data;
    }

    public void setData(AvailableModel data) {
        this.data = data;
    }
}
