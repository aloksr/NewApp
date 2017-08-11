package com.app.truxapp.leasedriver.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import com.app.truxapp.leasedriver.realm_model.LoginRealmBookingLeaseData;
import com.app.truxapp.leasedriver.realm_model.LoginRealmCommonData;
import com.app.truxapp.leasedriver.realm_model.LoginRealmDriverAttendanceData;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {
    private static RealmController instance;
    private final Realm realm;
    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }
    public static RealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }
    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }
    public static RealmController getInstance() {
        return instance;
    }
    public Realm getRealm() {
        return realm;
    }
    public void refresh() {
        realm.refresh();
    }

  //User Detail Information Data to save realm start
    public void clearAllUserData() {
        realm.beginTransaction();
        realm.clear(LoginRealmCommonData.class);
        realm.clear(LoginRealmBookingLeaseData.class);
        realm.clear(LoginRealmDriverAttendanceData.class);
        realm.commitTransaction();
    }
     public LoginRealmCommonData getLoginRealmCommonDataDetail() {
        return realm.where(LoginRealmCommonData.class).findFirst();
    }


    public LoginRealmBookingLeaseData getLoginRealmBookingLeaseDataDetail() {
        return realm.where(LoginRealmBookingLeaseData.class).findFirst();
    }

    public LoginRealmDriverAttendanceData getLoginRealmDriverAttendanceDataDetail() {
        return realm.where(LoginRealmDriverAttendanceData.class).findFirst();
    }


    public RealmResults<LoginRealmCommonData> getLoginRealmCommonDataAll() {
        return realm.where(LoginRealmCommonData.class).findAll();
    }


    public RealmResults<LoginRealmDriverAttendanceData> getLoginRealmDriverAttendanceDataAll() {
        return realm.where(LoginRealmDriverAttendanceData.class).findAll();
    }

    public RealmResults<LoginRealmBookingLeaseData> getLoginRealmBookingLeaseDataAll() {
        return realm.where(LoginRealmBookingLeaseData.class).findAll();
    }

    public boolean hasLoginRealmCommonData() {
        return !realm.allObjects(LoginRealmCommonData.class).isEmpty();
    }
    public boolean hasLoginRealmBookingLeaseData() {
        return !realm.allObjects(LoginRealmBookingLeaseData.class).isEmpty();
    }

    public boolean hasLoginRealmDriverAttendanceData() {
        return !realm.allObjects(LoginRealmDriverAttendanceData.class).isEmpty();
    }

    /*public UserDetailModel getUserDetailThroughId(String id) {
        return realm.where(UserDetailModel.class).equalTo("id", id).findFirst();
    }
    public boolean hasUserData() {
        return !realm.allObjects(UserDetailModel.class).isEmpty();
    }
    public RealmResults<UserDetailModel> getUserDetailWithQuery() {
        return realm.where(UserDetailModel.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();
    }*/
    //User Detail Information Data to save realm end
     ///////  registration database PostAttendanceRealmLogin  ///////////////
     public boolean hasAllPostAttendanceRealmLogin() {
         return !realm.allObjects(PostAttendanceRealmLogin.class).isEmpty();
     }

    public RealmResults<PostAttendanceRealmLogin> getPostAttendanceRealmLoginDataAll() {
        return realm.where(PostAttendanceRealmLogin.class).findAll();
    }

    public void clearAllPostAttendanceRealmLogin() {
        realm.beginTransaction();
        realm.clear(PostAttendanceRealmLogin.class);
        realm.commitTransaction();
    }



    public PostAttendanceRealmLogin getPostAttendanceRealmLoginDetail() {
        return realm.where(PostAttendanceRealmLogin.class).findFirst();
    }
     ///////  registration database PostAttendanceRealmLogin  ///////////////



}
