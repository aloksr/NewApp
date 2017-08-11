package com.app.truxapp.leasedriver.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.app.truxapp.leasedriver.Model.AvailableModel;
import com.app.truxapp.leasedriver.Model.StartJourneyDataModel;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.activity.BaseActivity;
import com.app.truxapp.leasedriver.activity.MainActivity;
import com.app.truxapp.leasedriver.activity.RegistrationActivity;
import com.app.truxapp.leasedriver.activity.StopJourningActivity;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.response.StartJournyResponse;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.CommonUtils;
import com.app.truxapp.leasedriver.utility.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;


public class HomeFragment extends FragmentBase {
    View view;
    Button startjrny;
    Dialog dialog;
    Realm realm;
    PostAttendanceRealmLogin postAttendanceRealmLogin;
    private String token,tcurrent,tlast;
    private String Vhecle_number;
    String input_box;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        postAttendanceRealmLogin = RealmController.with(getActivity()).getPostAttendanceRealmLoginDetail();
        token = postAttendanceRealmLogin.getToken();
        tcurrent = postAttendanceRealmLogin.getCurrentServerTime();
        tlast = postAttendanceRealmLogin.getLastLoginTime();
        Vhecle_number = postAttendanceRealmLogin.getVehicleNumber();




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity)getActivity()).mAddTitle(getString(R.string.menu_home));
        startjrny=(Button)view.findViewById(R.id.startjrny_button);

        startjrny.setOnClickListener(this);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startjrny_button:
                checkToStartJourney();
        }

    }
    private void checkToStartJourney() {
         if(tcurrent!=null&& tlast!=null) {
            long timeDef = CommonUtils.getNumberOfDay(tcurrent, tlast);
            if (timeDef == 0) {
                show_dialoge();
            } else {
                showPendingJourneyDialog();
            }
        }
    }

    private void showPendingJourneyDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.complte_journey));
        alertDialog.setMessage(getString(R.string.punchout) + " " + getDateByLastLoginTime(tlast) + " " + getString(R.string.punchout_pending));
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }
    public String getDateByLastLoginTime(String lastLoginTime) {
        String date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US);
        try {
            Date date1 = df.parse(lastLoginTime);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            date = dateFormat.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void show_dialoge() {
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.startjourney_dialog);
        Button button = dialog.findViewById(R.id.submitboxes_button);
        ImageView cross = dialog.findViewById(R.id.cross);
       final  EditText input_boxentry = dialog.findViewById(R.id.input_boxentry);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                 input_box  = input_boxentry.getText().toString();
                if(input_box !=null && input_box.length()>0){
                    checkForValidLocation();
                }

            }
        });
        dialog.show();
    }

    @Override
    public void getLatLong(double latitude, double longitude) {
        super.getLatLong(latitude, longitude);
        goToWebCall(latitude,longitude);

    }

    private void goToWebCall(double latitude,double longitude) {
        String url = UrlService.BASE_URL + UrlService.START_JOURNEY_URL;
        JSONObject obj = new JSONObject();
        try {
            obj.put("vehicleNo",Vhecle_number);
            obj.put("byId", "0");
            obj.put("timestamp", "100");
            obj.put("jrnStatus", "1");
            obj.put("deviceBookingLeasesId", "0");
            obj.put("bookingStartLatitude", String.valueOf(latitude));
            obj.put("bookingStartLogitude",String.valueOf(longitude));
            obj.put("boxToBeDelivered", Integer.parseInt(input_box));
            obj.put("bookingTotalDistance", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseActivity.showLog("" + url);

        if (checkInternateConnection(getActivity().findViewById(R.id.show_error), getActivity())) {
            startjourneyCall(url, obj);
        }
    }
    private void startjourneyCall(String url, JSONObject obj) {
        DialogUtils.showCustomProgressDialog(getActivity(), getResources().getString(R.string.loading), null);
        String URL = url;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                DialogUtils.removeCustomProgressDialog();
                try {
                    StartJournyResponse rsp = new Gson().fromJson(resultJson.toString(), StartJournyResponse.class);
                    StartJourneyDataModel  startJourneyDataModel =rsp.getData();
                    AllowedFeatureModel [] allowedFeatureModel=startJourneyDataModel.getAllowedFeatures();

                    updateDatabase(startJourneyDataModel);

                    if (rsp.getErrorCode().equals("100")) {
                        Intent intent = new Intent(getActivity(), StopJourningActivity.class);
                        intent.putExtra("allowedfeaturemodel", allowedFeatureModel);
                        startActivity(intent);

                    }else if (rsp.getErrorCode().equalsIgnoreCase("D101")){
                        BaseActivity.errordilog_code101( getActivity(),rsp.getErrorMesaage());
                    }
                    else if (rsp.getErrorCode().equalsIgnoreCase("P101")){
                        updateDatabase(startJourneyDataModel);

                        checkValidPilot(startJourneyDataModel,allowedFeatureModel);
                    }else if (rsp.getErrorCode().equals("101")){
                       BaseActivity. showSnackBar(getActivity().findViewById(R.id.show_error), getActivity(),rsp.getErrorMesaage());

                    }

                    else {
                        BaseActivity.showSnackBar(getActivity().findViewById(R.id.show_error), getActivity(), rsp.getErrorMesaage());

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


    private void checkValidPilot(StartJourneyDataModel checkVehicleModel, AllowedFeatureModel []allowedFeatures) {
        String driverStatus=checkVehicleModel.getDriverStatus();
        String loginStatus=checkVehicleModel.getLoginStatus();
        if(driverStatus !=null  && loginStatus!=null){
            if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("0")){
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                intent.putExtra("data",checkVehicleModel);
                startActivity(intent);

            }
            else if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
            else if(driverStatus.equalsIgnoreCase("1")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(getActivity(), StopJourningActivity.class);
                intent.putExtra("allowedfeaturemodel",allowedFeatures);
                startActivity(intent);

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
