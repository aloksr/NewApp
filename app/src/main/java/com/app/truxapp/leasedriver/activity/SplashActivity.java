package com.app.truxapp.leasedriver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.amazonaws.com.google.gson.Gson;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.truxapp.leasedriver.Fragments.AvailableFragment;
import com.app.truxapp.leasedriver.Model.AllowedFeatureModel;
import com.app.truxapp.leasedriver.Model.AvailableModel;
import com.app.truxapp.leasedriver.Model.CheckVehicleModel;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.response.AvailableOrderResponse;
import com.app.truxapp.leasedriver.response.CheckVehicleResponse;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.AppPreference;
import com.app.truxapp.leasedriver.utility.CommonUtils;
import com.app.truxapp.leasedriver.utility.Constants;
import com.app.truxapp.leasedriver.utility.DialogUtils;
import com.app.truxapp.leasedriver.utility.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;

public class SplashActivity extends BaseActivity {
    private int Interval2 = 2000;
    private Realm realm;
    PostAttendanceRealmLogin loginRealmcommondata;

    PostAttendanceRealmLogin postAttendanceRealmLogin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        realm = Realm.getDefaultInstance();
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        postAttendanceRealmLogin = RealmController.with(this).getPostAttendanceRealmLoginDetail();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(postAttendanceRealmLogin!=null){
                    checkIsLoging();
                }else {
                    Intent intent=new Intent(SplashActivity.this,LanguageSelector.class);
                            startActivity(intent);
                    finish();
                        }
            }
        }, Interval2);
    }
    public void setSelected(String slectedLanguage) {
        Locale srcLanguage = CommonUtils.getInstance().getLocale(slectedLanguage);
        CommonUtils.getInstance().setLocale(srcLanguage.toString(), getApplicationContext(), prefs);
    }
    public void checkIsLoging() {
        String language = prefs.getStringValueForTag(Constants.SELECT_LANGUAGE);
        if (language != null && language.length() > 0) {
            setSelected(CommonUtils.getInstance().getLanguage(language));
            goToSendVehicleNo();
        }
    }
    private void goToSendVehicleNo() {
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("vehicleNumber",postAttendanceRealmLogin.getVehicleNumber());
        }catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        String url = UrlService.BASE_URL + UrlService.CHECK_VEHICLE_EXIST;
        BaseActivity.showLog("" + url);
        //  MyProgressDialog.showProgress(this);
        if (checkInternateConnection(findViewById(R.id.show_error), this)){
            goToSendVehicleWEbCall(mJSONObject,url);
        }
    }

    private void goToSendVehicleWEbCall(JSONObject mJSONObject, String url){
        DialogUtils.showCustomProgressDialog(this, getResources().getString(R.string.loading), null);
        String URL = url;
        final RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL,mJSONObject,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                DialogUtils.removeCustomProgressDialog();
                String resultObj = resultJson.toString();
                MyProgressDialog.dismissProgress();
                try {
                    CheckVehicleResponse rsp = new Gson().fromJson(resultObj,CheckVehicleResponse.class);
                    if (rsp.getErrorCode().equals("100")) {
                        CheckVehicleModel checkVehicleModel   =  rsp.getData();
                        AllowedFeatureModel[] allowedFeatures = checkVehicleModel.getAllowedFeatures();
                        saveToRealmDataBase(checkVehicleModel, allowedFeatures);
                        checkValidPilot(checkVehicleModel,allowedFeatures);
                        showSnackBar(findViewById(R.id.show_error), SplashActivity.this,rsp.getErrorMesaage());
                    }else if (rsp.getErrorCode().equalsIgnoreCase("D101")) {
                        showSnackBar(findViewById(R.id.show_error), SplashActivity.this,rsp.getErrorMesaage());

                    }else if (rsp.getErrorCode().equals("101")){
                        Intent intent = new Intent(SplashActivity.this, LoginScreen.class);
                        startActivity(intent);
                        finish();
                    }else {
                        showSnackBar(findViewById(R.id.show_error), SplashActivity.this,rsp.getErrorMesaage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogUtils.removeCustomProgressDialog();
                System.out.println(error.toString());
            }
        });
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);
    }

    private void checkValidPilot(CheckVehicleModel checkVehicleModel,AllowedFeatureModel []allowedFeatures) {
        String driverStatus=checkVehicleModel.getDriverStatus();
        String loginStatus=checkVehicleModel.getLoginStatus();
        String availableorder=checkVehicleModel.getDstatus();
        if(driverStatus !=null  && loginStatus!=null){
            if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("0")){
                Intent intent = new Intent(SplashActivity.this, RegistrationActivity.class);
                intent.putExtra("data",checkVehicleModel);
                startActivity(intent);
                finish();
            }
            else if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if(driverStatus.equalsIgnoreCase("1")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(SplashActivity.this, StopJourningActivity.class);
                intent.putExtra("allowedfeaturemodel",allowedFeatures);
                startActivity(intent);
                finish();
            }
            else if(availableorder.equalsIgnoreCase("Free")) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                }

            }
        }

    private void saveToRealmDataBase(CheckVehicleModel checkVehicleModel, AllowedFeatureModel[] allowedFeatures) {
        RealmController.with(this).clearAllPostAttendanceRealmLogin();
        PostAttendanceRealmLogin postAttendanceRealmLogin = new PostAttendanceRealmLogin();
        postAttendanceRealmLogin.setId(checkVehicleModel.getId());
        postAttendanceRealmLogin.setDriverId(checkVehicleModel.getDriverId());
        postAttendanceRealmLogin.setDstatus(checkVehicleModel.getDstatus());
        postAttendanceRealmLogin.setVehicleId(checkVehicleModel.getVehicleId());
        postAttendanceRealmLogin.setDriverPhoneNumber(checkVehicleModel.getDriverPhoneNumber());
        postAttendanceRealmLogin.setDeviceUUID(checkVehicleModel.getDeviceUUID());
        postAttendanceRealmLogin.setVehicleNumber(checkVehicleModel.getVehicleNumber());
        postAttendanceRealmLogin.setDriverName(checkVehicleModel.getDriverName());
        postAttendanceRealmLogin.setVehicleType(checkVehicleModel.getVehicleType());
        postAttendanceRealmLogin.setLoginStatus(checkVehicleModel.getLoginStatus());
        postAttendanceRealmLogin.setDriverStatus(checkVehicleModel.getDriverStatus());
        postAttendanceRealmLogin.setLastLoginTime(checkVehicleModel.getLastLoginTime());
        postAttendanceRealmLogin.setLastLogoutTime(checkVehicleModel.getLastLogoutTime());
        postAttendanceRealmLogin.setLoginId(checkVehicleModel.getLoginId());
        postAttendanceRealmLogin.setBookingId(checkVehicleModel.getBookingId());
        postAttendanceRealmLogin.setSubClientId(checkVehicleModel.getSubClientId());
        postAttendanceRealmLogin.setTrackingDeviceId(checkVehicleModel.getTrackingDeviceId());
        postAttendanceRealmLogin.setToken(checkVehicleModel.getToken());
        postAttendanceRealmLogin.setDriverImage(checkVehicleModel.getDriverImage());
        postAttendanceRealmLogin.setDriverClientName(checkVehicleModel.getDriverClientName());
        postAttendanceRealmLogin.setDriverClientId(checkVehicleModel.getDriverClientId());
        postAttendanceRealmLogin.setLoginOpeningKM(checkVehicleModel.getLoginOpeningKM());
        postAttendanceRealmLogin.setLoginClosingKM(checkVehicleModel.getLoginClosingKM());
        postAttendanceRealmLogin.setLastLoginOpeningKM(checkVehicleModel.getLastLoginOpeningKM());
        postAttendanceRealmLogin.setLastLoginClosingKM(checkVehicleModel.getLastLoginClosingKM());
        postAttendanceRealmLogin.setCurrentServerTime(checkVehicleModel.getCurrentServerTime());
        postAttendanceRealmLogin.setSubClientName(checkVehicleModel.getSubClientName());
        postAttendanceRealmLogin.setSubClientId(checkVehicleModel.getSubClientId());
        postAttendanceRealmLogin.setJourneyStartTime(checkVehicleModel.getJourneyStartTime());
        postAttendanceRealmLogin.setPrefillDriverName(checkVehicleModel.getPrefillDriverName());
        postAttendanceRealmLogin.setPrefillDriverId(checkVehicleModel.getPrefillDriverId());
        realm.beginTransaction();
        realm.copyToRealm(postAttendanceRealmLogin);
        realm.commitTransaction();
        AppPreference.with(this).setPreLoad(true);
    }
}