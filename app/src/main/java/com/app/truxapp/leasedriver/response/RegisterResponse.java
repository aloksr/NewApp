package com.app.truxapp.leasedriver.response;

import com.app.truxapp.leasedriver.Model.RegisterModel;

/**
 * Created by sharadsingh on 18/07/17.
 */

public class RegisterResponse extends BaseResponce {
    private RegisterModel data;
    public RegisterModel getData() {
        return data;
    }

    public void setData(RegisterModel data) {
        this.data = data;
    }



}
