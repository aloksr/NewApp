package com.app.truxapp.leasedriver.Model;

import java.io.Serializable;


public class TripDropDetailsData implements Serializable {

    private String awbNumber;
    private int dropedBoxes;
    private String dropLocationReachTime;
    private String afterDropStartTime;
    private String dropLocation;

    public String getAwbNumber() {
        return awbNumber;
    }

    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public int getDropedBoxes() {
        return dropedBoxes;
    }

    public void setDropedBoxes(int dropedBoxes) {
        this.dropedBoxes = dropedBoxes;
    }

    public String getDropLocationReachTime() {
        return dropLocationReachTime;
    }

    public void setDropLocationReachTime(String dropLocationReachTime) {
        this.dropLocationReachTime = dropLocationReachTime;
    }

    public String getAfterDropStartTime() {
        return afterDropStartTime;
    }

    public void setAfterDropStartTime(String afterDropStartTime) {
        this.afterDropStartTime = afterDropStartTime;
    }

    public String getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(String dropLocation) {
        this.dropLocation = dropLocation;
    }
}
