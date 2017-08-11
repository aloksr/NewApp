package com.app.truxapp.leasedriver.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.com.google.gson.Gson;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.truxapp.leasedriver.Model.AllowedFeatureModel;
import com.app.truxapp.leasedriver.Model.StartJourneyDataModel;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.response.StartJournyResponse;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.CommonUtils;
import com.app.truxapp.leasedriver.utility.Constants;
import com.app.truxapp.leasedriver.utility.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class StopJourningActivity extends BaseActivity{
    Button startDelvry,stopjrny;
    Realm realm;
    PostAttendanceRealmLogin postAttendanceRealmLogin;
    private String token;
    ArrayList<AllowedFeatureModel> list;
    AllowedFeatureModel[] allowedFeatureModel;
    private String vehcle_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stop_journing);
        Intent intent = getIntent();
        realm = Realm.getDefaultInstance();
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        postAttendanceRealmLogin = RealmController.with(this).getPostAttendanceRealmLoginDetail();
        token = postAttendanceRealmLogin.getToken();
        
        vehcle_no=postAttendanceRealmLogin.getVehicleNumber();
        startDelvry=(Button)findViewById(R.id.startdelvry_button);
        stopjrny=(Button)findViewById(R.id.stopjrny_button);
        ImageView backarrow=(ImageView)findViewById(R.id.back_Arrow);
        TextView headertitle=(TextView)findViewById(R.id.header_title);
        headertitle.setText(getString(R.string.stop_journey));
        startDelvry.setOnClickListener(this);
        stopjrny.setOnClickListener(this);
        backarrow.setVisibility(View.GONE);



        //  backarrow.setOnClickListener(this);
        if(intent !=null){
             allowedFeatureModel = (AllowedFeatureModel[]) intent.getSerializableExtra("allowedfeaturemodel");
            if(allowedFeatureModel !=null){
                list=new ArrayList<AllowedFeatureModel>(Arrays.asList(allowedFeatureModel));
                checkStartDEl(list);
            }
           }
    }
    private void checkStartDEl(ArrayList<AllowedFeatureModel> list) {
        for(AllowedFeatureModel p : list){
            if(p.getFeatureCode().equalsIgnoreCase("FC01")){
                if(p.isEnabled()){
                    startDelvry.setVisibility(View.VISIBLE);
                  }else {
                    startDelvry.setVisibility(View.GONE);
                }
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startdelvry_button:
                   getCurrentTimeStamp();
                   check_start_delivery_of_button();
                break;
            case R.id.stopjrny_button:
                checkForValidLocation();
                break;
        }
    }

    private void getCurrentTimeStamp() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Date date = null;
        try {
            date = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String  sadcsa  = String.valueOf(date.getTime());
        prefs.setStringValueForTag(Constants.TIME_STAMP_START_DELIVERY,sadcsa);

    }

    private void check_start_delivery_of_button() {
        if(list !=null && list.size()>0){
            for(AllowedFeatureModel p : list){
                if(p.getFeatureCode().equalsIgnoreCase("FC02")){
                    if(p.isEnabled()){
                        Intent intent=new Intent(this, ScanBoxesActivity.class);
                        intent.putExtra("allowedfeaturemodel",allowedFeatureModel);
                        startActivity(intent);

                    }else {
                        Intent intent=new Intent(this, ResumeJourneyActivity.class);
                        intent.putExtra("allowedfeaturemodel",allowedFeatureModel);
                        startActivity(intent);


                    }
                }
            }

        }

    }

    @Override
    public void getLatLong(double latitude, double longitude) {
        super.getLatLong(latitude, longitude);
        goToWebCall(latitude,longitude);
    }

    private void goToWebCall(double latitude, double longitude) {
        String url = UrlService.BASE_URL + UrlService.STOP_JOURNEY_URL;
        JSONObject obj = new JSONObject();
        try {
            obj.put("vehicleNo",vehcle_no);
            obj.put("byId", "0");
            obj.put("timestamp", "100");
            obj.put("jrnStatus", "0");
            obj.put("deviceBookingLeasesId", "0");
            obj.put("bookingEndLatitude", latitude);
            obj.put("bookingEndLogitude", longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseActivity.showLog("" + url);

        if (checkInternateConnection(this.findViewById(R.id.show_error), this)) {
            stopjourneyCall(url, obj);
        }
    }
    private void stopjourneyCall(String url, JSONObject obj) {
        DialogUtils.showCustomProgressDialog(this, getResources().getString(R.string.loading), null);
        String URL = url;
        final RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                DialogUtils.removeCustomProgressDialog();
                try {
                    StartJournyResponse rsp = new Gson().fromJson(resultJson.toString(), StartJournyResponse.class);
                    if (rsp.getErrorCode().equals("100")) {
                        StartJourneyDataModel  startJourneyDataModel =rsp.getData();
                        AllowedFeatureModel [] allowedFeatureModel=startJourneyDataModel.getAllowedFeatures();
                        Intent intent=new Intent(StopJourningActivity.this,MainActivity.class);
                        startActivity(intent);

                    } else if (rsp.getErrorCode().equalsIgnoreCase("D101")){
                        BaseActivity.errordilog_code101( StopJourningActivity.this,rsp.getErrorMesaage());

                    }else if (rsp.getErrorCode().equalsIgnoreCase("P101")){
                        StartJourneyDataModel  startJourneyDataModel =rsp.getData();
                        updateDatabase(startJourneyDataModel);
                        checkValidPilot(startJourneyDataModel,allowedFeatureModel);
                    }
                    else if (rsp.getErrorCode().equals("101")){
                        showSnackBar(findViewById(R.id.show_error), StopJourningActivity.this,rsp.getErrorMesaage());
                    }

                    else {
                        showSnackBar(findViewById(R.id.show_error), StopJourningActivity.this,rsp.getErrorMesaage());
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError, NullPointerException {
                Map<String, String> params = new HashMap<>();
                if (token != null)
                    params.put("authKey", token);
                return params;
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);

    }

    @Override
    public void onBackPressed() {


    }
  
    private void checkValidPilot(StartJourneyDataModel checkVehicleModel, AllowedFeatureModel []allowedFeatures) {
        String driverStatus = checkVehicleModel.getDriverStatus();
        String loginStatus = checkVehicleModel.getLoginStatus();
        String availableorder=checkVehicleModel.getDstatus();
        if (driverStatus != null && loginStatus != null) {
            if (driverStatus.equalsIgnoreCase("0") && loginStatus.equalsIgnoreCase("0")) {
                Intent intent = new Intent(this, RegistrationActivity.class);
                intent.putExtra("data", checkVehicleModel);
                startActivity(intent);

            } else if (driverStatus.equalsIgnoreCase("0") && loginStatus.equalsIgnoreCase("1")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            } else if (driverStatus.equalsIgnoreCase("1") && loginStatus.equalsIgnoreCase("1")) {
                Intent intent = new Intent(this, StopJourningActivity.class);
                intent.putExtra("allowedfeaturemodel", allowedFeatures);
                startActivity(intent);

            } else if(availableorder.equalsIgnoreCase("Free")){
                Intent intent = new Intent(StopJourningActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
    private void updateDatabase(StartJourneyDataModel allowedFeatureModel) {
        final RealmResults<PostAttendanceRealmLogin> booksUser = RealmController.with(this).getPostAttendanceRealmLoginDataAll();
        realm.beginTransaction();
        booksUser.get(0).setBookingId(allowedFeatureModel.getBookingId());
        realm.commitTransaction();

    }

}