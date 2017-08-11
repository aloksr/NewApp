package com.app.truxapp.leasedriver.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.app.truxapp.leasedriver.Model.CheckVehicleModel;
import com.app.truxapp.leasedriver.Model.RegisterModel;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.Model.AllowedFeatures;
import com.app.truxapp.leasedriver.Model.PostAttendanceLOginLogin;
import com.app.truxapp.leasedriver.Model.UploadImage;

import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.response.PostAttendanceLOginResponse;
import com.app.truxapp.leasedriver.response.RegisterResponse;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.AppPreference;
import com.app.truxapp.leasedriver.utility.CommonUtils;
import com.app.truxapp.leasedriver.utility.DialogUtils;
import com.app.truxapp.leasedriver.utility.GeneratedId;
import com.app.truxapp.leasedriver.utility.MyProgressDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
public class RegistrationActivity extends BaseActivity {
    EditText driver_mobileno, input_km, input_drivername;
    ImageView change_mobileno;
    Button top_submit, bottom_submit;
    TextView lastclosing_km;
    LinearLayout linearTop, linearBottom,drivername_layout;
    Button upload_image;
    ImageView image;
    public static boolean check = false;
    public  int mIndexforImages;
    public  String imagePath;
    private Bitmap bitmap = null;
    public  Bitmap mBitmapforImages;
    public static Uri outputFileUri;
    private static final int cameraData = 0;
    private AlertDialog.Builder builder;
    public String[] path;
    public UploadImage uploadImage;
    private static final int SELECTED_IMAGE = 1;
    private static Context context;
    private static final int CAMERA_DATA = 0;
    String driver_phoneno,lastloginclosing_km,vehcle_no,driver_name,token,driver_id;
    int pre_call = 1;
    String VersionName;
    PostAttendanceRealmLogin postAttendanceRealmLogin;
    private Realm realm;
    String input_kelomite,exist_kelomiter;
    TextView drivername_fromserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        realm = Realm.getDefaultInstance();
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        postAttendanceRealmLogin = RealmController.with(this).getPostAttendanceRealmLoginDetail();
        try {
            VersionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        driver_mobileno = (EditText) findViewById(R.id.driver_mobileno);
        input_km = (EditText) findViewById(R.id.input_km);
        input_drivername = (EditText) findViewById(R.id.input_drivername);
        lastclosing_km = (TextView) findViewById(R.id.lastclosing_km);
        drivername_fromserver = (TextView) findViewById(R.id.drivername_fromserver);
        change_mobileno = (ImageView) findViewById(R.id.edit_no);
        top_submit = (Button) findViewById(R.id.top_submit);
        bottom_submit = (Button) findViewById(R.id.bottom_submit);
         linearTop = (LinearLayout) findViewById(R.id.linearTop);
         linearBottom = (LinearLayout) findViewById(R.id.linearBottom);
        drivername_layout = (LinearLayout) findViewById(R.id.drivername);
         upload_image = (Button) findViewById(R.id.upload_image);
         image = (ImageView) findViewById(R.id.image);
         change_mobileno.setOnClickListener(this);
         top_submit.setOnClickListener(this);
         bottom_submit.setOnClickListener(this);
         upload_image.setOnClickListener(this);
         upload_image.setTag(image);
         driver_phoneno = postAttendanceRealmLogin.getDriverPhoneNumber();

         lastloginclosing_km = postAttendanceRealmLogin.getLoginClosingKM();
         vehcle_no = postAttendanceRealmLogin.getVehicleNumber();
         driver_id = postAttendanceRealmLogin.getDriverId();
         driver_name = postAttendanceRealmLogin.getDriverName();
         token = postAttendanceRealmLogin.getToken();
         lastclosing_km.setText(lastloginclosing_km);
         driver_mobileno.setText(driver_phoneno);
        if(!driver_name.equals("")){
            drivername_layout.setVisibility(View.VISIBLE);
            drivername_fromserver.setText(driver_name);

        }else {
            drivername_layout.setVisibility(View.GONE);


        }

        if(token!=null){
            getAwsCredential();
         }
        mRequstFocus(driver_mobileno);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_submit:
                if (isValidateForThreepreCall()) {
                    checkForValidLocation();
                  //  postAttendanceLoginNewWebCall();
                }
                break;
            case R.id.edit_no:
                String driver_number= driver_mobileno.getText().toString();
                if(driver_number!=null&&driver_number.length()==10){
                    goToWebCall(driver_number);
                }

                break;
            case R.id.bottom_submit:
                pre_call = 4;
                if (isValidateForFourpreCall()) {
                   // postAttendanceLoginNewWebCall();
                    checkForValidLocation();
                }
                break;
            case R.id.upload_image:
                onPickImage(view);
                break;
        }
    }

    @Override
    public void getLatLong(double latitude, double longitude) {
        super.getLatLong(latitude, longitude);
        postAttendanceLoginNewWebCall(latitude,longitude);
    }

    private void mRequstFocus(final EditText Three) {
        Three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                linearBottom.setVisibility(View.GONE);
                linearTop.setVisibility(View.GONE);
                if(editable.length()>=10){
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            }
        });
    }

    private boolean isValidateForThreepreCall() {
        String  input_kelomiter = input_km.getText().toString();
        String  driver_no = driver_mobileno.getText().toString();
         exist_kelomiter = lastclosing_km.getText().toString();
        if (input_kelomiter .equalsIgnoreCase("")){
            showSnackBar(findViewById(R.id.show_error), RegistrationActivity.this, getString(R.string.valid_km));
            check = false;
        }
        else if (driver_no.equalsIgnoreCase("")) {
            showSnackBar(findViewById(R.id.show_error), RegistrationActivity.this, getString(R.string.valid_driverno));
            check = false;
        } else {
            check = true;
        }
        return check;
    }
    private boolean isValidateForFourpreCall() {
        String input_kelomiter = input_km.getText().toString();
        String driver_no = driver_mobileno.getText().toString();
        String exist_kelomiter = lastclosing_km.getText().toString();
        String drivername = input_drivername.getText().toString();
        if (input_kelomiter .equalsIgnoreCase("")){
            showSnackBar(findViewById(R.id.show_error), RegistrationActivity.this,getString(R.string.valid_km));
            check = false;
        }
        else if (driver_no.equalsIgnoreCase("")) {
            showSnackBar(findViewById(R.id.show_error), RegistrationActivity.this, getString(R.string.valid_driverno));
            check = false;
        }
        else if (drivername.equalsIgnoreCase("")) {
            showSnackBar(findViewById(R.id.show_error), RegistrationActivity.this, getString(R.string.valid_drivername));
            check = false;
        }
        else if(imagePath==null){
            showSnackBar(findViewById(R.id.show_error), RegistrationActivity.this, getString(R.string.valid_image));
            check = false;
        }
        else {
            check = true;
        }
        return check;
    }
    private void postAttendanceLoginNewWebCall(double latitude,double longitude) {
        String url = UrlService.BASE_URL + UrlService.POST_ADDENDANCE_LOGIN_NEW_URL;
        JSONObject obj = new JSONObject();
        String input_kelomiter = input_km.getText().toString();
        try {
            obj.put("punchIn", "1");
            obj.put("vehicleNo", vehcle_no);
            obj.put("deviceId", CommonUtils.getInstance().getIMEI(this));
            obj.put("lgLat", latitude);
            obj.put("lgLong", longitude);
            obj.put("apkVersion", VersionName);
            obj.put("openingKm", input_kelomiter);
            obj.put("driverId", driver_id);
            obj.put("driverMobile", driver_mobileno.getText().toString());

            if (pre_call == 4) {
                obj.put("isNewDriver", "1");
                obj.put("driverName", input_drivername.getText().toString());
                if(imagePath !=null){
                    obj.put("driverImage", imagePath);
                }
            } else {
                obj.put("driverName", driver_name);
                if (driver_phoneno.equalsIgnoreCase(driver_mobileno.getText().toString())) {
                    obj.put("isNewDriver", "0");
                } else {
                    obj.put("isNewDriver", "2");

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseActivity.showLog("" + url);

        if (checkInternateConnection(findViewById(R.id.show_error), this)){
            GotoPostAttendancewebCall(url,obj);
        }
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
                    PostAttendanceLOginResponse rsp = new Gson().fromJson(resultJson.toString(), PostAttendanceLOginResponse.class);
                    if (rsp.getErrorCode().equals("100")) {
                        PostAttendanceLOginLogin checkVehicleModel = rsp.getData();
                        AllowedFeatures[] allowedFeatures = checkVehicleModel.getAllowedFeatures();
                        saveToRealmDataBase(checkVehicleModel, allowedFeatures);
                        checkValidPilotforRegistration(checkVehicleModel,allowedFeatures);

                    } else if (rsp.getErrorCode().equalsIgnoreCase("D101")){
                        BaseActivity.errordilog_code101( RegistrationActivity.this,rsp.getErrorMesaage());
                    }else if (rsp.getErrorCode().equals("101")){
                        showSnackBar(findViewById(R.id.show_error), RegistrationActivity.this,rsp.getErrorMesaage());

                    }
                    else {
                        showSnackBar(findViewById(R.id.show_error), RegistrationActivity.this,rsp.getErrorMesaage());
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

    public void onPickImage(View v) {
        captureImageDialog(0);

    }
    private void checkValidPilotforRegistration(PostAttendanceLOginLogin checkVehicleModel,  AllowedFeatures[] allowedFeatures) {
        String driverStatus=checkVehicleModel.getDriverStatus();
        String loginStatus=checkVehicleModel.getLoginStatus();
        String availableorder=checkVehicleModel.getDstatus();
        if(driverStatus !=null  && loginStatus!=null){
            if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("0")){
                Intent intent = new Intent(RegistrationActivity.this, RegistrationActivity.class);
                intent.putExtra("data",checkVehicleModel);
                startActivity(intent);
                finish();
            }
            else if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if(driverStatus.equalsIgnoreCase("1")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(RegistrationActivity.this, StopJourningActivity.class);
                intent.putExtra("allowedfeaturemodel",allowedFeatures);
                startActivity(intent);
                finish();
            }
            else if(availableorder.equalsIgnoreCase("Free")){
                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
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
                if (pre_call == 1) {
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
                    params.put("authKey", token);
                return params;
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsObjRequest);
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
                                Intent j = new Intent(
                                        Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(j, SELECTED_IMAGE);
                                builder.setCancelable(true);

                            case 2:
                                imagePath = null;
                                Picasso.with(RegistrationActivity.this).load(R.mipmap.ic_uplaod)
                                        .fit()
                                        .into(image);
                                builder.create().cancel();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        switch (requestCode) {
            case CAMERA_DATA:
                if (resultCode == Activity.RESULT_OK) {
                    uri = RegistrationActivity.outputFileUri;
                    getPathByUri(uri, this, true,"driver");
                }
                break;
            case SELECTED_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    uri = data.getData();
                    getPathByUri(uri, this, true,"driver");
                }
                break;
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
//            mSPLF = new SharedPreferencesField(context);
//            int maxSize = mSPLF.getInt("image_ratio", 1000);
//            if (!isTab) {
//                fragment = ((BaseActivity) context).getFragmentInstance();
//                if (fragment instanceof FragmentRegistrationDriverDocument) {
//                    maxSize = 300;
//                    if (mIndexforImages == 0) {
//                        thumbFile = new File(direct, "thumb_image.JPEG");
//                        outThumb = new FileOutputStream(thumbFile);
//                    }
//                }
//            }
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
        imagePath = uploadImage.imageUrl;
        Matrix matrix = new Matrix();
        if (mBitmapforImages.getWidth() > mBitmapforImages.getHeight()) {
            matrix.setRotate(180);
            mBitmapforImages = Bitmap.createBitmap(mBitmapforImages, 0, 0, mBitmapforImages.getWidth(), mBitmapforImages.getHeight(),
                    matrix, true);
        }
        try {
            String imageurl = CommonUtils.s3UrGenerator(imagePath);
            Picasso.with(this).load(imageurl)
                    .into(image);
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
    private void goToWebCall(String driver_no) {
        String url = UrlService.BASE_URL + UrlService.CHECK_IS_DRIVER_FREE_WHILE_PUNCH_IN + driver_no;
        BaseActivity.showLog("" + url);
        if (checkInternateConnection(findViewById(R.id.show_error), this)){
            goToDriverWebCall(url);
        }

    }

    private void goToDriverWebCall(String url) {
        DialogUtils.showCustomProgressDialog(this, getResources().getString(R.string.loading), null);
        String URL = url;
        final RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                DialogUtils.removeCustomProgressDialog();
                    try {
                        RegisterResponse rsp = new Gson().fromJson(resultJson.toString(), RegisterResponse.class);
                        if (rsp.getErrorCode().equals("100")) {
                            RegisterModel registerModel=rsp.getData();
                            linearTop.setVisibility(View.VISIBLE);
                            drivername_layout.setVisibility(View.VISIBLE);
                            drivername_fromserver.setText(registerModel.getPrefillDriverName());

                        }
                        else if (rsp.getErrorCode().equals("ND101")) {
                             linearBottom.setVisibility(View.VISIBLE);
                             linearTop.setVisibility(View.GONE);
                            drivername_layout.setVisibility(View.GONE);

                        }else if (rsp.getErrorCode().equalsIgnoreCase("D101")){
                               linearBottom.setVisibility(View.GONE);
                               linearTop.setVisibility(View.GONE);
                            drivername_layout.setVisibility(View.GONE);
                            BaseActivity.errordilog_code101( RegistrationActivity.this,rsp.getErrorMesaage());
                        }else if (rsp.getErrorCode().equals("101")){
                            showSnackBar(findViewById(R.id.show_error), RegistrationActivity.this,rsp.getErrorMesaage());

                        }
                        else {
                            showSnackBar(findViewById(R.id.show_error), RegistrationActivity.this,rsp.getErrorMesaage());
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

    private void saveToRealmDataBase(PostAttendanceLOginLogin checkVehicleModel, AllowedFeatures[] allowedFeatures) {
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

    }


}
