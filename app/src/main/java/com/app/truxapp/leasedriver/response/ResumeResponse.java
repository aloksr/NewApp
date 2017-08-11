package com.app.truxapp.leasedriver.response;

import com.app.truxapp.leasedriver.Model.ResumeJourneyDataModel;

/**
 * Created by ravi on 26/7/17.
 */
public class ResumeResponse extends BaseResponce {
    private ResumeJourneyDataModel data;
    public ResumeJourneyDataModel getData() {
        return data;
    }

    public void setData(ResumeJourneyDataModel data) {
        this.data = data;
    }
}
