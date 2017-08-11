package com.app.truxapp.leasedriver.activity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.com.google.gson.Gson;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.truxapp.leasedriver.Model.AllowedFeatureModel;
import com.app.truxapp.leasedriver.Model.CheckVehicleModel;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.response.CheckVehicleResponse;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.AppPreference;
import com.app.truxapp.leasedriver.utility.DialogUtils;
import com.app.truxapp.leasedriver.utility.MyProgressDialog;

import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class LoginScreen extends BaseActivity {
    EditText vehicle_no;
    Button login_button;
    private Realm realm;
    TextInputLayout input_layout_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        realm = Realm.getDefaultInstance();
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        input_layout_name=(TextInputLayout)findViewById(R.id.input_layout_name);
        vehicle_no = (EditText) findViewById(R.id.input_vehicleno);
        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(this);
        vehicle_no.setFilters(new InputFilter[] {new InputFilter.AllCaps(),new InputFilter.LengthFilter(10)});
        vehicle_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                if(arg0.length()>=10){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            @Override
            public void afterTextChanged(Editable et) {

            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                String input=vehicle_no.getText().toString();
                if(input!=null && input.length()>0){
                    goToCheckVehicleNo(input);
                }else {
                    showSnackBar(findViewById(R.id.show_error), LoginScreen.this,getString(R.string.enter_vehicleno));
                }
        }
    }
    private void goToCheckVehicleNo(String vehicle_no) {
        JSONObject mJSONObject = new JSONObject();
        try {
            mJSONObject.put("vehicleNumber",vehicle_no.toUpperCase());
        }catch (Exception e){
            e.getMessage();
            e.printStackTrace();
        }
        String url = UrlService.BASE_URL + UrlService.CHECK_VEHICLE_EXIST;
        BaseActivity.showLog("" + url);
      //  MyProgressDialog.showProgress(this);
        if (checkInternateConnection(findViewById(R.id.show_error), this)){
            goToCheckVehicleWEbCall(mJSONObject,url);
        }
    }

    private void goToCheckVehicleWEbCall(JSONObject mJSONObject, String url){
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
                        showSnackBar(findViewById(R.id.show_error), LoginScreen.this,rsp.getErrorMesaage());
                    }else if (rsp.getErrorCode().equalsIgnoreCase("D101")) {
                        BaseActivity.errordilog_code101(LoginScreen.this, rsp.getErrorMesaage());
                    }
                    else if (rsp.getErrorCode().equalsIgnoreCase("P101")) {
                        CheckVehicleModel checkVehicleModel = rsp.getData();
                        AllowedFeatureModel[] allowedFeatures = checkVehicleModel.getAllowedFeatures();
                        updateDatabase(checkVehicleModel);
                        checkValidPilot(checkVehicleModel, allowedFeatures);
                    }else if (rsp.getErrorCode().equals("101")){
                        input_layout_name.setError(rsp.getErrorMesaage());

                        //showSnackBar(findViewById(R.id.show_error), LoginScreen.this,rsp.getErrorMesaage());
                    }else {
                        showSnackBar(findViewById(R.id.show_error), LoginScreen.this,rsp.getErrorMesaage());
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
                Intent intent = new Intent(LoginScreen.this, RegistrationActivity.class);
                intent.putExtra("data",checkVehicleModel);
                startActivity(intent);
                finish();
            }
            else if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
           else if(driverStatus.equalsIgnoreCase("1")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(LoginScreen.this, StopJourningActivity.class);
                intent.putExtra("allowedfeaturemodel",allowedFeatures);
                startActivity(intent);
                finish();
            }
            else if(availableorder.equalsIgnoreCase("Free")){
                Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    private void saveToRealmDataBase(CheckVehicleModel checkVehicleModel, AllowedFeatureModel[] allowedFeatures) {
        RealmController.with(this).clearAllPostAttendanceRealmLogin();
        PostAttendanceRealmLogin postAttendanceRealmLogin = new PostAttendanceRealmLogin();
        postAttendanceRealmLogin.setId(checkVehicleModel.getId());
        postAttendanceRealmLogin.setDstatus(checkVehicleModel.getDstatus());
        postAttendanceRealmLogin.setDriverId(checkVehicleModel.getDriverId());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this,LanguageSelector.class);
        startActivity(intent);
    }
    private void updateDatabase(CheckVehicleModel allowedFeatureModel) {
        final RealmResults<PostAttendanceRealmLogin> booksUser = RealmController.with(this).getPostAttendanceRealmLoginDataAll();
        realm.beginTransaction();
        booksUser.get(0).setBookingId(allowedFeatureModel.getBookingId());
        realm.commitTransaction();

    }
}
