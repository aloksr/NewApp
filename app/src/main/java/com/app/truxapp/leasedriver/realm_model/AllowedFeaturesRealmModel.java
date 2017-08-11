package com.app.truxapp.leasedriver.realm_model;


import io.realm.RealmObject;

public class AllowedFeaturesRealmModel extends RealmObject{
     private int id;
    private String client_id;
    private boolean FC01;
    private boolean FC02;
    private boolean FC03;
    private boolean FC04;
    private boolean FC05;
    private boolean FC06;
    private boolean FC07;
    private boolean FC08;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public boolean isFC03() {
        return FC03;
    }

    public void setFC03(boolean FC03) {
        this.FC03 = FC03;
    }

    public boolean isFC04() {
        return FC04;
    }

    public void setFC04(boolean FC04) {
        this.FC04 = FC04;
    }

    public boolean isFC05() {
        return FC05;
    }

    public void setFC05(boolean FC05) {
        this.FC05 = FC05;
    }

    public boolean isFC06() {
        return FC06;
    }

    public void setFC06(boolean FC06) {
        this.FC06 = FC06;
    }

    public boolean isFC07() {
        return FC07;
    }

    public void setFC07(boolean FC07) {
        this.FC07 = FC07;
    }

    public boolean isFC08() {
        return FC08;
    }

    public void setFC08(boolean FC08) {
        this.FC08 = FC08;
    }

    public boolean isFC01() {
        return FC01;
    }

    public void setFC01(boolean FC01) {
        this.FC01 = FC01;
    }

    public boolean isFC02() {
        return FC02;
    }

    public void setFC02(boolean FC02) {
        this.FC02 = FC02;
    }



}
