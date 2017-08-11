package com.app.truxapp.leasedriver.response;

import com.app.truxapp.leasedriver.Model.PostAttendanceLOginLogin;

/**
 * Created by sharadsingh on 19/07/17.
 */

public class PostAttendanceLOginResponse  extends BaseResponce {

    private PostAttendanceLOginLogin data;

    public PostAttendanceLOginLogin getData() {
        return data;
    }

    public void setData(PostAttendanceLOginLogin data) {
        this.data = data;
    }



}
