package com.app.truxapp.leasedriver.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.com.google.gson.Gson;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
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
import com.app.truxapp.leasedriver.Model.CheckVehicleModel;
import com.app.truxapp.leasedriver.Model.ResumeJourneyDataModel;
import com.app.truxapp.leasedriver.Model.UploadImage;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.adapters.CommonAutoCompleteAdapter;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.response.AwbResponse;
import com.app.truxapp.leasedriver.response.ResumeResponse;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.CommonUtils;
import com.app.truxapp.leasedriver.utility.Constants;
import com.app.truxapp.leasedriver.utility.DialogUtils;
import com.app.truxapp.leasedriver.utility.GeneratedId;
import com.app.truxapp.leasedriver.utility.MyProgressDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class ResumeJourneyActivity extends BaseActivity {
    ArrayList<AllowedFeatureModel> list;
    AllowedFeatureModel[] allowedFeatureModel;
    CheckVehicleModel model_data;
    private Bitmap bitmap = null;
    public  Bitmap mBitmapforImages;
    public static Uri outputFileUri;
    public String[] path;
    private static final int cameraData = 0;
    private static final int SELECTED_IMAGE = 1;
    EditText input_boxno;
    TextView awb_no;
    private static final int CAMERA_DATA = 0;
    private Realm realm;
    private PostAttendanceRealmLogin loginRealmcommondata;
    private String token;
    String selectede_values ="";
    public static boolean check = false;
    LinearLayout ndrlist_layout,uploadDocumentlayout,tempretureLayout,Signature_on_drop,Pod_layout;
    Button uploadDocument_button,Signature_on_drop_button,Pod_button;
    private AlertDialog.Builder builder;
    public UploadImage uploadImage;
    public  String imagePath_doc=null;
    public  String imagePath_pod=null;
    public  String imagePath_drop=null;
    public static int mIndexforImages;
    ImageView image_doc ,image_pod,image_drop;
    Button scaningagain_button;
    Dialog dialog;
    private IntentIntegrator qrScan;
    private String driver_id;
    private String booking_id;
    private String vehicle_no;
    String box_no;
    String adb_no;
    ArrayList<String>  autolist;
    LinearLayout l2,l_scan;
    private boolean checkFC02=false;
    private boolean checkFC03=false;
    private boolean checkFC04=false;
    private boolean checkFC05=false;
    private boolean checkFC06=false;
    private boolean checkFC07=false;
    EditText input_temperature;
    String input_temperaturevalue;
    int upload_image=0;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_resumejrny);
        l2=(LinearLayout)findViewById(R.id.l2) ;
        l_scan=(LinearLayout)findViewById(R.id.l_scan) ;
        qrScan = new IntentIntegrator(this);
        realm = Realm.getDefaultInstance();
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        loginRealmcommondata = RealmController.with(this).getPostAttendanceRealmLoginDetail();
        token = loginRealmcommondata.getToken();
        booking_id= loginRealmcommondata.getBookingId();
        vehicle_no= loginRealmcommondata.getVehicleNumber();
        driver_id=loginRealmcommondata.getDriverId();

        if(token!=null){
            getAwsCredential();
        }

        Intent intent=getIntent();
        ImageView backarrow=(ImageView)findViewById(R.id.back_Arrow);
        TextView headertitle=(TextView)findViewById(R.id.header_title);
        Button resume_button=(Button)findViewById(R.id.resume_button);
         scaningagain_button=(Button)findViewById(R.id.scaningagain_button);
        awb_no=(TextView)findViewById(R.id.awb_no);
        input_boxno=(EditText)findViewById(R.id.input_boxno);
        image_doc = (ImageView) findViewById(R.id.image_doc);
        image_pod = (ImageView) findViewById(R.id.image_pod);
        image_drop = (ImageView) findViewById(R.id.image_drop);
        input_temperature=(EditText)findViewById(R.id.input_temperature);




        ndrlist_layout = (LinearLayout) findViewById(R.id.ndrlist_layout);
        uploadDocumentlayout = (LinearLayout) findViewById(R.id.uploadDocumentlayout);
        tempretureLayout = (LinearLayout) findViewById(R.id.tempretureLayout);
        Signature_on_drop = (LinearLayout) findViewById(R.id.Signature_on_drop);
        Pod_layout = (LinearLayout) findViewById(R.id.Pod_layout);

        uploadDocument_button=(Button)findViewById(R.id.uploadDocument_button);
        Signature_on_drop_button=(Button)findViewById(R.id.Signature_on_drop_button);
        Pod_button=(Button)findViewById(R.id.Pod_button);


        final AutoCompleteTextView ndrlist=(AutoCompleteTextView)findViewById(R.id.ndrlist);
        ndrlist.setClickable(true);
        ndrlist.setFocusable(false);
        List<String> myList =Arrays.asList(getResources().getStringArray(R.array.ndr_responce_new));
        autolist  =  new ArrayList<String>((myList));
        CommonAutoCompleteAdapter auto_adapter  = new CommonAutoCompleteAdapter(this,R.layout.spinner_rows,autolist);
        ndrlist.setAdapter(auto_adapter);
        headertitle.setText(getString(R.string.resume_delivery));
        uploadDocument_button.setOnClickListener(this);
        Signature_on_drop_button.setOnClickListener(this);
        Pod_button.setOnClickListener(this);
        backarrow.setVisibility(View.GONE);
        resume_button.setOnClickListener(this);
        scaningagain_button.setOnClickListener(this);
        if(intent !=null){
            String awbno=intent.getStringExtra("awbno");
            if(awbno !=null){
                awb_no.setText(awbno);
            }
            allowedFeatureModel = (AllowedFeatureModel[]) intent.getSerializableExtra("allowedfeaturemodel");
            if(allowedFeatureModel !=null){
                list=new ArrayList<AllowedFeatureModel>(Arrays.asList(allowedFeatureModel));
                checkStartDEl(list);
            }
        }
        ndrlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ndrlist.showDropDown();
            }
        });
        ndrlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectede_values = autolist.get(i);
            }
        });
    }

    private void checkStartDEl(ArrayList<AllowedFeatureModel> list) {
        for(AllowedFeatureModel p: list){
            if(p.getFeatureCode().equalsIgnoreCase("FC03")){
                if(p.isEnabled()){
                    ndrlist_layout.setVisibility(View.VISIBLE);
                    checkFC03 = true;
                }else {
                    ndrlist_layout.setVisibility(View.GONE);

                }
            }
            if(p.getFeatureCode().equalsIgnoreCase("FC04")){
                if(p.isEnabled()){
                    uploadDocumentlayout.setVisibility(View.VISIBLE);
                    checkFC04 = true;
                }else {
                    uploadDocumentlayout.setVisibility(View.GONE);

                }
            }
            if(p.getFeatureCode().equalsIgnoreCase("FC05")){
                if(p.isEnabled()){
                    tempretureLayout.setVisibility(View.VISIBLE);
                    checkFC05 = true;
                }else {
                   tempretureLayout.setVisibility(View.GONE);

                }
            }
            if(p.getFeatureCode().equalsIgnoreCase("FC06")){
                if(p.isEnabled()){
                    Signature_on_drop.setVisibility(View.VISIBLE);
                    checkFC06 = true;
                }else {
                    Signature_on_drop.setVisibility(View.GONE);

                }
            }
            if(p.getFeatureCode().equalsIgnoreCase("FC07")){
                if(p.isEnabled()){
                   Pod_layout.setVisibility(View.VISIBLE);
                    checkFC07 = true;
                }else {
                   Pod_layout.setVisibility(View.GONE);

                }
            }
            if(p.getFeatureCode().equalsIgnoreCase("FC02")){
                if(p.isEnabled()){
                    l_scan.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.VISIBLE);
                    checkFC02 = true;

                }else {
                    l_scan.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);

                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.uploadDocument_button:
                upload_image=1;
                onPickImage(view);
                break;
            case R.id.Signature_on_drop_button:
                upload_image=2;
                onPickImage(view);
                break;
            case R.id.Pod_button:
                upload_image=3;
                onPickImage(view);
                break;
            case R.id.scaningagain_button:
                mScanDialog(getString(R.string.scan_pocket));
                break;
            case R.id.resume_button:
                box_no=input_boxno.getText().toString();
                 adb_no =awb_no.getText().toString();
                input_temperaturevalue=input_temperature.getText().toString();
                if(isValidateForFourpreCall()){
                    checkForValidLocation();
                }
                break;
        }
    }


    @Override
    public void getLatLong(double latitude, double longitude) {
        super.getLatLong(latitude, longitude);
        resumeWebCall(latitude,longitude);
    }

    private boolean isValidateForFourpreCall() {
         box_no = input_boxno.getText().toString();
         adb_no = awb_no.getText().toString();
        input_temperaturevalue=input_temperature.getText().toString();
        if (box_no.equals("")) {
            showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this, getString(R.string.enter_number_of_boxes));
            return false;
        }
        if (checkFC02) {
            if (adb_no.equals("")) {
                showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this, getString(R.string.enter_awb_number));
                return false;
            }
        }
         if(checkFC03) {
            if (selectede_values.equalsIgnoreCase("") || selectede_values.equalsIgnoreCase("Select Option")) {
                showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this, getString(R.string.please_provide_delivery_status));
                return false;
            }
        }
         if(checkFC04) {
            if (imagePath_doc == null) {
                showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this, getString(R.string.no_document_found));
                return false;
            }
        }
         if(checkFC05) {
            if (input_temperaturevalue.equals("")) {
                showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this, getString(R.string.enter_temperature));
                return false;
            }
        }
         if(checkFC06) {
            if (imagePath_drop == null) {
                showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this, getString(R.string.no_sig_found));
                return false;
            }
        }
         if(checkFC07) {
            if (imagePath_pod == null) {
                showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this, getString(R.string.no_pod_found));
                return false;
            }
        }
        else {

            return true;
        }

        return true;
    }

    private void resumeWebCall(double latitude,double longitude) {
        String state_name = getLocatiopnNameFromLatLong(latitude,longitude);
        JSONObject  jsonObject   = new JSONObject();
        try {
            jsonObject.put("bookingLeaseId",booking_id);
            jsonObject.put("dropLocationReachTime",prefs.getStringValueForTag(Constants.TIME_STAMP_START_DELIVERY));
            jsonObject.put("afterDropStartTime",getCurrentTimeStamp());
            jsonObject.put("stopLat",latitude);
            jsonObject.put("stopLong",longitude);
            jsonObject.put("dropLocation",state_name);
            jsonObject.put("droped_boxes",box_no);
            jsonObject.put("vehicleNumber",vehicle_no);
            if(checkFC02) {
                jsonObject.put("awbNumber", adb_no);
            }
            if(checkFC03){
                if(selectede_values !=null && selectede_values.length()>0){
                    jsonObject.put("ndrResponce",selectede_values);
                }
            }
            if(checkFC04){
                jsonObject.put("stopDocUrl",imagePath_doc);
            }
            if(checkFC05){
                 jsonObject.put("currentTemp",input_temperaturevalue);
            }
            if(checkFC06){
                jsonObject.put("signatureImage",imagePath_drop);
            }
            if(checkFC07){
                  jsonObject.put("dropPOD",imagePath_pod);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = UrlService.BASE_URL + UrlService.ADD_RESUME_URL;
        GotoPostAttendancewebCall(url,jsonObject);
    }
    private  String  getCurrentTimeStamp() {
        String current_times = null;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Date date = null;
        try {
            date = df.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        current_times   = String.valueOf(date.getTime());
        return current_times;
    }


    private void GotoPostAttendancewebCall(String url, JSONObject obj) {
        DialogUtils.showCustomProgressDialog(this, getResources().getString(R.string.loading), null);
        String URL = url;
        final RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL,obj,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                DialogUtils.removeCustomProgressDialog();
                try {
                    ResumeResponse rsp = new Gson().fromJson(resultJson.toString(), ResumeResponse.class);
                    if (rsp.getErrorCode().equals("100")) {
                        ResumeJourneyDataModel resumeJourneyDataModel=rsp.getData();
                        AllowedFeatureModel[] allowedFeatures = resumeJourneyDataModel.getAllowedFeatures();
                        updateDatabase(resumeJourneyDataModel);
                        checkStatusPagetowitch(resumeJourneyDataModel,allowedFeatures);
                    } else if (rsp.getErrorCode().equalsIgnoreCase("D101")){
                        BaseActivity.errordilog_code101( ResumeJourneyActivity.this,rsp.getErrorMesaage());
                    }else if (rsp.getErrorCode().equalsIgnoreCase("P101")) {
                        ResumeJourneyDataModel resumeJourneyDataModel=rsp.getData();
                        AllowedFeatureModel[] allowedFeatures = resumeJourneyDataModel.getAllowedFeatures();
                        updateDatabase(resumeJourneyDataModel);
                        checkStatusPagetowitch(resumeJourneyDataModel,allowedFeatures);
                    }else if (rsp.getErrorCode().equals("101")){
                        showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this,rsp.getErrorMesaage());

                    }
                    else {
                        showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this,rsp.getErrorMesaage());
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
    private void checkStatusPagetowitch(ResumeJourneyDataModel resumeJourneyDataModel,AllowedFeatureModel [] allowedFeatures) {
        String driverStatus=resumeJourneyDataModel.getDriverStatus();
        String loginStatus=resumeJourneyDataModel.getLoginStatus();
        if(driverStatus !=null  && loginStatus!=null){
            if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("0")){
                Intent intent = new Intent(ResumeJourneyActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
            else if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(ResumeJourneyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if(driverStatus.equalsIgnoreCase("1")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(ResumeJourneyActivity.this, StopJourningActivity.class);
                intent.putExtra("allowedfeaturemodel",allowedFeatures);
                startActivity(intent);
                finish();
            }
    }}

    public  String getLocatiopnNameFromLatLong(double lat , double log){
        String str = null;
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation((lat),(log), 1);
            if(addresses.size()<=0)
                return "";
            String addres = addresses.get(0).getSubLocality();
            String cityName = addresses.get(0).getLocality();
            String stateName = addresses.get(0).getAdminArea();
            StringBuffer hrffhd = new StringBuffer();
            if(addres !=null && addres.length()>0){
                hrffhd.append(addres+",");
            }if(cityName !=null && cityName.length()>0) {
                hrffhd.append(cityName+",");
             }if(stateName !=null && stateName.length()>0){
                hrffhd.append(stateName);
            }
            str = hrffhd.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    public void onPickImage(View v) {
        captureImageDialog(0);
    }
    private void captureImageDialog(int index) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("select_image");
        builder.setItems(
                new CharSequence[]{"CAMERA", "GALLERY", "REMOVE"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                captureImage();
                                builder.setCancelable(true);
                                break;
                            case 1:
                                Intent j = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(j, SELECTED_IMAGE);
                                builder.setCancelable(true);
                            case 2:
                                if(upload_image==1){
                                   // imagePath_doc = null;
                                    Picasso.with(ResumeJourneyActivity.this).load(R.mipmap.ic_uplaod)
                                            .fit()
                                            .into(image_doc);
                                    builder.create().cancel();
                                }else if(upload_image==2){
                                   // imagePath_drop = null;
                                    Picasso.with(ResumeJourneyActivity.this).load(R.mipmap.ic_uplaod)
                                            .fit()
                                            .into(image_drop);
                                    builder.create().cancel();
                                }else if(upload_image==3){
                                   // imagePath_pod = null;
                                    Picasso.with(ResumeJourneyActivity.this).load(R.mipmap.ic_uplaod)
                                            .fit()
                                            .into(image_pod);
                                    builder.create().cancel();
                                }

                        }
                    }
                });
        builder.create().show();
        mIndexforImages = index;
    }
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            outputFileUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, cameraData);
        } else {
            Toast.makeText(this, "error_no_camera", Toast.LENGTH_LONG).show();
        }
    }

    public void getPathByUri(Uri uri, Context context, boolean isTab, String directoryName) {
        if (uri != null) {
            executeS3(context, uri, isTab, directoryName);
        }
    }
    public void executeS3(Context context, Uri uri, boolean isTab, String directoryName) {
        try {

            BaseActivity.showLog("executeS3   ");
            if (bitmap != null)
                bitmap.recycle();
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            bitmap = rotateImageIfRequired(context, bitmap, uri);
            path = createDirectoryAndSaveFile(bitmap, context, isTab);
            bitmap.recycle();
            bitmap = null;
            this.directoryName = directoryName;
//            if () {
//                ((BaseActivity) context).getAWSKEY(new SharedPreferencesField(context).getString("id", null));
//            } else {
            callUploadImage(context);
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void callUploadImage(Context context) {
        UploadToS3Bucket upload = new UploadToS3Bucket(context, false, directoryName, CommonUtils.aws_key, CommonUtils.aws_password);
        upload.execute(path[0]);

    }
    public String[] createDirectoryAndSaveFile(Bitmap bmpPic, Context context, boolean isTab) throws IOException {
        File file = null;
        Fragment fragment = null;
        File thumbFile = null;
        FileOutputStream outThumb = null;
        String[] paths = new String[2];
        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"
                + context.getResources().getString(R.string.app_name).replace("\n", "").replace(" ", "_").trim());// +
        if (direct.exists()) {
            direct.delete();
        }
        try {
            direct.mkdirs();
            file = new File(direct, "cropped_image.JPEG");
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            int outWidth;
            int outHeight;
            int inWidth = bmpPic.getWidth();
            int inHeight = bmpPic.getHeight();
            if (inWidth > inHeight) {
                outWidth = 1000;
                outHeight = (inHeight * 1000) / inWidth;
            } else {
                outHeight = 1000;
                outWidth = (inWidth * 1000) / inHeight;
            }
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmpPic, outWidth, outHeight, false);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out);
            if (!isTab && fragment != null && outThumb != null) {
                Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(file.getAbsolutePath()), 100, 140);
                thumbImage.compress(Bitmap.CompressFormat.JPEG, 85, outThumb);

            }
            out.flush();
            out.close();
        } catch (OutOfMemoryError E) {

            E.printStackTrace();
            //mContext.showSnackBar(mContext.getString(R.string.plz_upload_image_again), mContext);
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null)
            paths[0] = file.getAbsolutePath();
        if (file != null && thumbFile != null)
            paths[1] = thumbFile.getAbsolutePath();
        return paths;
        //return file;
    }

    public class UploadToS3Bucket extends AsyncTask<String, Void, UploadImage> {
        boolean isThumb;
        private Context context;
        private String directoryName;
        private String key;
        private String pasword;

        private UploadToS3Bucket(Context context, boolean isThumb, String directoryName, String key, String pasword) {
            this.context = context;
            this.isThumb = isThumb;
            this.directoryName = directoryName;
            this.key = key;
            this.pasword = pasword;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MyProgressDialog.showProgress(context);
        }

        @Override
        protected UploadImage doInBackground(String... urls) {
            String ACCESS_KEY = key, SECRET_KEY = pasword,
                    MY_BUCKET = "truxs3/" + directoryName;
            byte[] result = null;
            try {
                SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
                String randomNum = Integer.toString(prng.nextInt());
                MessageDigest sha = MessageDigest.getInstance("SHA-1");
                result = sha.digest(randomNum.getBytes());
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }
            String type = urls[0].substring(urls[0].lastIndexOf(".") + 1);
            AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);
            AmazonS3 s3 = new AmazonS3Client(credentials);
            java.security.Security.setProperty("networkaddress.cache.ttl", "60");
            s3.setEndpoint("https://s3-ap-southeast-1.amazonaws.com/");
            TransferUtility transferUtility = new TransferUtility(s3, context);
            final File UPLOADING_IMAGE = new File(urls[0]);
            final String imageUrl = urls[0];
            String key;
            if (isThumb)
                key = GeneratedId.hexEncode(result) + "_thumb" + "." + type;
            else
                key = GeneratedId.hexEncode(result) + "." + type;
            final String s3bucketurl = "https://s3-ap-southeast-1.amazonaws.com/" + MY_BUCKET + "/"
                    + key;
            BaseActivity.showLog("s3bucketurl " + s3bucketurl);
            TransferObserver observer = transferUtility.upload(MY_BUCKET, key, UPLOADING_IMAGE);
            if (!isThumb) {
                uploadImage = new UploadImage();
                uploadImage.observer = observer;
                uploadImage.imageUrl = s3bucketurl;
                uploadImage.UPLOADING_IMAGE = UPLOADING_IMAGE;
                uploadImage.imagePath = imageUrl;
                uploadImage.isThumb = isThumb;
            } else {
                uploadImage.observer = observer;
                uploadImage.thumbImageUrl = s3bucketurl;
                uploadImage.UPLOADING_THUMB_IMAGE = UPLOADING_IMAGE;
                uploadImage.imageThumbPath = imageUrl;
                uploadImage.isThumb = isThumb;
            }
            uploadImage.directoryName = directoryName;
            uploadImage.key = key;
            uploadImage.pasword = pasword;
            return uploadImage;
        }

        @Override
        protected void onPostExecute(UploadImage uploadImage) {
            BaseActivity.showLog("observer   " + uploadImage.observer);
            BaseActivity.showLog("observer   " + uploadImage.imageUrl);
            BaseActivity.showLog(" uploadImage.observer.getState() :" + uploadImage.observer.getState());
            if (TransferState.COMPLETED.equals(uploadImage.observer.getState())) {
                MyProgressDialog.dismissProgress();
                if (!isThumb && path[1] != null) {
                    UploadToS3Bucket upload = new UploadToS3Bucket(context, true, uploadImage.directoryName, uploadImage.key, uploadImage.pasword);
                    upload.execute(path[1]);
                } else {
                    uploadToS3(uploadImage);
                }
            } else {
                uploadImage.observer.setTransferListener(new UploadListener(context, uploadImage.directoryName, uploadImage.key, uploadImage.pasword));
            }

        }

    }

    private class UploadListener implements TransferListener {
        private Context context;
        private String directoryName;
        private String key;
        private String password;

        UploadListener(Context context, String directoryName, String key, String password) {
            this.context = context;
            this.directoryName = directoryName;
            this.key = key;
            this.password = password;
        }

        @Override
        public void onError(int id, Exception e) {
            BaseActivity.showLog("  onError :" + e);
            MyProgressDialog.dismissProgress();
            showImageFailedDialog();
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            BaseActivity.showLog(" onProgressChanged :" + bytesCurrent);
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            BaseActivity.showLog("  TransferState newState :" + newState);
            if (TransferState.COMPLETED.equals(uploadImage.observer.getState())) {
                BaseActivity.showLog(" observer   " + uploadImage.imageUrl);
                MyProgressDialog.dismissProgress();
                if (!uploadImage.isThumb && path[1] != null) {
                    UploadToS3Bucket upload = new UploadToS3Bucket(context, true, directoryName, key, password);
                    upload.execute(path[1]);
                } else {
                    uploadToS3(uploadImage);
                }


            } else if (TransferState.FAILED.equals(uploadImage.observer.getState()) || TransferState.CANCELED.equals(uploadImage.observer.getState())) {
                MyProgressDialog.dismissProgress();
                showImageFailedDialog();
            }

        }

    }
    public void uploadToS3(UploadImage uploadImage) {
        Log.e("Image path ", uploadImage.imagePath);
        mBitmapforImages = BitmapFactory.decodeFile(uploadImage.imagePath);
        Log.e("Image path ", "" + mBitmapforImages);
        if(upload_image==1){
            imagePath_doc = uploadImage.imageUrl;
        }else if(upload_image==2){
            imagePath_drop = uploadImage.imageUrl;
        }
        else if(upload_image==3){
            imagePath_pod = uploadImage.imageUrl;
        }

        Matrix matrix = new Matrix();
        if (mBitmapforImages.getWidth() > mBitmapforImages.getHeight()) {
            matrix.setRotate(180);
            mBitmapforImages = Bitmap.createBitmap(mBitmapforImages, 0, 0, mBitmapforImages.getWidth(), mBitmapforImages.getHeight(),
                    matrix, true);
        }
        try {
            if(upload_image==1){
                String imageurl = CommonUtils.s3UrGenerator(imagePath_doc);
                Picasso.with(this).load(imageurl)
                        .into(image_doc);
            }else if(upload_image==2){
                String imageurl = CommonUtils.s3UrGenerator(imagePath_drop);
                Picasso.with(this).load(imageurl)
                        .into(image_drop);

            }else if(upload_image==3){
                String imageurl = CommonUtils.s3UrGenerator(imagePath_pod);
                Picasso.with(this).load(imageurl)
                        .into(image_pod);
            }

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {
        //try {
        ExifInterface ei = getPictureData(context, selectedImage);
        if (ei == null)
            return img;
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }



    }

    public static ExifInterface getPictureData(Context context, Uri uri) throws IOException {
        String[] uriParts = uri.toString().split(":");
        String path;

        if (uriParts[0].equals("content")) {
            String col = MediaStore.Images.ImageColumns.DATA;
            Cursor c = context.getContentResolver().query(uri,
                    new String[]{col},
                    null, null, null);
            if (c != null && c.moveToFirst()) {
                path = c.getString(c.getColumnIndex(col));
                c.close();
                return new ExifInterface(path);
            }

        } else if (uriParts[0].equals("file")) {
            path = uri.getEncodedPath();
            return new ExifInterface(path);
        }
        return null;
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
    public void showImageFailedDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.uploading_failed);
        alertDialog.setMessage(R.string.image_uploading_failed);
        alertDialog.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    public void getAwsCredential() {
        String url = UrlService.BASE_URL + UrlService.AWS_KEY + driver_id + "&appName=lvt_driver";
        BaseActivity.showLog("" + url);
        if (checkInternateConnection(findViewById(R.id.show_error), this)){
            goToWebCallOfAwsCred(url);
        }

    }
    private void goToWebCallOfAwsCred(String url){
        DialogUtils.showCustomProgressDialog(this, getResources().getString(R.string.loading), null);
        String URL = url;
        final RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                DialogUtils.removeCustomProgressDialog();
                try {
                        JSONObject data = new JSONObject(resultJson.getString("data"));
                        if (resultJson.getString("errorCode").equals("100")) {
                            CommonUtils.aws_key = data.getString("aws_key");
                            CommonUtils.aws_password = data.getString("aws_password");
                        }
                    } catch (JSONException e) {
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
                Map<String, String> params = new HashMap< >();
                if(token !=null)
                    params.put("authKey",token);
                return params;
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);
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
                    showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this, getString(R.string.enter_awb_number));
                } else {
                    String new_scan=edtAwbNumber.getText().toString();
                    awb_no.setText(new_scan);
                    dialog.dismiss();

                }
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Uri uri;
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                try {
                    String values =null;
                    values = result.getContents();
                     goToWebCall(values);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        switch (requestCode) {
            case CAMERA_DATA:
                if (resultCode == Activity.RESULT_OK) {
                    uri = ResumeJourneyActivity.outputFileUri;
                    getPathByUri(uri, this, true,"trip");
                }
                break;
            case SELECTED_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    uri = data.getData();
                    getPathByUri(uri, this, true,"trip");
                }
                break;

        }

    }


    private void goToWebCall(String edtAwbNumber) {
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
                        awb_no.setText(awbDataModel.getAwbNumber());

                    } else if (rsp.getErrorCode().equalsIgnoreCase("D101")){
                        BaseActivity.errordilog_code101( ResumeJourneyActivity.this,rsp.getErrorMesaage());
                    }else {
                        showSnackBar(findViewById(R.id.show_error), ResumeJourneyActivity.this,rsp.getErrorMesaage());
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
    private void updateDatabase(ResumeJourneyDataModel allowedFeatureModel) {
        final RealmResults<PostAttendanceRealmLogin> booksUser = RealmController.with(this).getPostAttendanceRealmLoginDataAll();
        realm.beginTransaction();
        booksUser.get(0).setBookingId(allowedFeatureModel.getBookingId());
        realm.commitTransaction();


    }
    @Override
    public void onBackPressed() {

    }
}
