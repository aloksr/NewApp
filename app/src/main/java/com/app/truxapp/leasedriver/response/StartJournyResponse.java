package com.app.truxapp.leasedriver.response;

import com.app.truxapp.leasedriver.Model.StartJourneyDataModel;


public class StartJournyResponse extends BaseResponce {
   private StartJourneyDataModel data;

    public StartJourneyDataModel getData() {
        return data;
    }

    public void setData(StartJourneyDataModel data) {
        this.data = data;
    }
}
