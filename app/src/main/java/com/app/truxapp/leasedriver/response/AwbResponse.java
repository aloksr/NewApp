package com.app.truxapp.leasedriver.response;

import com.app.truxapp.leasedriver.Model.AwbDataModel;

/**
 * Created by ravi on 25/7/17.
 */
public class AwbResponse extends BaseResponce {
    private AwbDataModel data;
    public AwbDataModel getData() {
        return data;
    }

    public void setData(AwbDataModel data) {
        this.data = data;
    }


}
