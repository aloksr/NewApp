package com.app.truxapp.leasedriver.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.app.truxapp.leasedriver.Model.AwbDataModel;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.response.AwbResponse;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.DialogUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class ScanBoxesActivity extends BaseActivity {
    Button scanbutton,cancel_button;
    Dialog dialog;
    AllowedFeatureModel[] allowedFeatureModel;
    private IntentIntegrator qrScan;
    Realm realm;
    PostAttendanceRealmLogin postAttendanceRealmLogin;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        postAttendanceRealmLogin = RealmController.with(this).getPostAttendanceRealmLoginDetail();
        token = postAttendanceRealmLogin.getToken();
        qrScan = new IntentIntegrator(this);
        setContentView(R.layout.scan_box);
        TextView headertitle=(TextView)findViewById(R.id.header_title);
        headertitle.setText(getString(R.string.scan_pocket));
        ImageView backarrow=(ImageView)findViewById(R.id.back_Arrow);
        scanbutton=(Button)findViewById(R.id.scan_button);
        cancel_button=(Button)findViewById(R.id.cancel_button);
        scanbutton.setOnClickListener(this);
        backarrow.setOnClickListener(this);
        cancel_button.setOnClickListener(this);
        Intent intent=getIntent();
        if(intent !=null){
            allowedFeatureModel = (AllowedFeatureModel[]) intent.getSerializableExtra("allowedfeaturemodel");

        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.scan_button:
                mScanDialog(getString(R.string.scan_pocket));
                break;
            case R.id.back_Arrow:
                finish();
                break;
            case R.id.cancel_button:
                finish();
                break;

        }
    }
    private void mScanDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.confimation_));
        alertDialog.setMessage(getResources().getString(R.string.are_you_sure_you_want_to) + " " + message);
        alertDialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        qrScan.initiateScan();


                    }
                });

        alertDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        enterAwbNumber();
                    }
                });
        alertDialog.show();
    }
    public void enterAwbNumber() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.inputawbno_dilog, null, false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(width - width / 8, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        final EditText edtAwbNumber = (EditText) dialog.findViewById(R.id.input_awbentry);
        Button btnSubmit = (Button) dialog.findViewById(R.id.awbno_button);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.cross_awb);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAwbNumber.getText().toString().isEmpty()) {
                    showSnackBar(findViewById(R.id.show_error), ScanBoxesActivity.this, getString(R.string.enter_awb_number));
                } else {
                    goToWebCall(edtAwbNumber.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    private void goToWebCall(String edtAwbNumber) {
       // dialog.dismiss();
        String url = UrlService.BASE_URL + UrlService.SCAN_AWB+edtAwbNumber;
        BaseActivity.showLog("" + url);
        if (checkInternateConnection(this.findViewById(R.id.show_error), this)) {
            scanWebCall(url);
        }
    }
    private void scanWebCall(String url) {
        DialogUtils.showCustomProgressDialog(this, getResources().getString(R.string.loading), null);
        String URL = url;
        final RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                DialogUtils.removeCustomProgressDialog();
                try {
                    AwbResponse rsp = new Gson().fromJson(resultJson.toString(), AwbResponse.class);
                    AwbDataModel awbDataModel =  rsp.getData();
                    if (rsp.getErrorCode().equals("100")) {
                        Intent intent=new Intent(ScanBoxesActivity.this,ResumeJourneyActivity.class);
                        intent.putExtra("awbno",awbDataModel.getAwbNumber());
                        intent.putExtra("allowedfeaturemodel",allowedFeatureModel);
                        startActivity(intent);
                        finish();

                    } else if (rsp.getErrorCode().equalsIgnoreCase("D101")){
                        BaseActivity.errordilog_code101( ScanBoxesActivity.this,rsp.getErrorMesaage());
                    }
                    else if (rsp.getErrorCode().equals("101")){
                        showSnackBar(findViewById(R.id.show_error), ScanBoxesActivity.this,rsp.getErrorMesaage());

                    }

                    else {
                        showSnackBar(findViewById(R.id.show_error), ScanBoxesActivity.this,rsp.getErrorMesaage());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {
                    String values =null;
                    values = result.getContents();
                    Log.e("scan",values);

                    goToWebCall(values);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onBackPressed() {

    }

}
