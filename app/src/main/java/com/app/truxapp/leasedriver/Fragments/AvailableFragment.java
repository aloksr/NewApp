package com.app.truxapp.leasedriver.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.app.truxapp.leasedriver.Model.AvailableModel;
import com.app.truxapp.leasedriver.Model.StartJourneyDataModel;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.activity.BaseActivity;
import com.app.truxapp.leasedriver.activity.MainActivity;
import com.app.truxapp.leasedriver.activity.StopJourningActivity;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.response.AvailableOrderResponse;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;


public class AvailableFragment extends FragmentBase {
    View view;
    Button accept_button;
    PostAttendanceRealmLogin postAttendanceRealmLogin;
    Realm realm;
    private String Vhecle_number;
    String token;
    LinearLayout available_details,button_layout,error_layout;
    TextView order_id, branch_address, source, destination, order_date, ti_name, ti_no,error_txt;
    String error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        postAttendanceRealmLogin = RealmController.with(getActivity()).getPostAttendanceRealmLoginDetail();
        token = postAttendanceRealmLogin.getToken();
        Vhecle_number = postAttendanceRealmLogin.getVehicleNumber();
        webcall();


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_available, container, false);
        ((MainActivity)getActivity()).mAddTitle(getString(R.string.menu_available));
        accept_button = (Button) view.findViewById(R.id.accept_button);
        available_details = (LinearLayout) view.findViewById(R.id.available_details);
        button_layout = (LinearLayout) view.findViewById(R.id.button_layout);
        error_layout = (LinearLayout) view.findViewById(R.id.error_layout);
        order_id = (TextView) view.findViewById(R.id.order_id);
        branch_address = (TextView) view.findViewById(R.id.branch_address);
        source = (TextView) view.findViewById(R.id.source);
        destination = (TextView) view.findViewById(R.id.destination);
        ti_name = (TextView) view.findViewById(R.id.ti_name);
        ti_no = (TextView) view.findViewById(R.id.ti_no);
        error_txt = (TextView) view.findViewById(R.id.error_txt);
        order_date = (TextView) view.findViewById(R.id.order_date);

        accept_button.setOnClickListener(this);
        return view;
    }
    private void webcall() {
        String url = UrlService.BASE_URL + UrlService.AVAILABLE_URL;
        JSONObject obj = new JSONObject();
        try {
            obj.put("vehicleNumber",Vhecle_number);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseActivity.showLog("" + url);

        if (checkInternateConnection(getActivity().findViewById(R.id.show_error), getActivity())) {
            availableOrderCall(url, obj);
        }
    }
    private void availableOrderCall(String url, JSONObject obj) {
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
                    AvailableOrderResponse rsp = new Gson().fromJson(resultJson.toString(), AvailableOrderResponse.class);
                    if (rsp.getErrorCode().equals("100")) {
                        AvailableModel available =rsp.getData();
                        setView(available);
                    }else if (rsp.getErrorCode().equals("101")) {
                        setErrorMessage(rsp.getErrorMesaage());
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

    private void setErrorMessage(String errorMesaage) {
        if (errorMesaage != null) {
            available_details.setVisibility(View.GONE);
            button_layout.setVisibility(View.GONE);
            error_txt.setText(errorMesaage);

        }
    }

    private void setView(AvailableModel availableModel) {
        if (availableModel != null) {
            available_details.setVisibility(View.VISIBLE);
            error_layout.setVisibility(View.GONE);
            order_id.setText(getString(R.string.order_id)+"-" +availableModel.getOrderId());
            branch_address.setText(getString(R.string.order_addrs)+" "+availableModel.getBranchAddress());
            source.setText(availableModel.getSource());
            destination.setText(availableModel.getDestination());
            order_date.setText(availableModel.getOrderDate());
            ti_name.setText(availableModel.getTiFirstName() +" "+ availableModel.getTiLastName());
            ti_no.setText(availableModel.getTiContactNumber());
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.accept_button:
                 HomeFragment home = new HomeFragment();
                ((MainActivity) getActivity()).addFragment(home, getString(R.string.menu_home));

        }
    }

}