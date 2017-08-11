package com.app.truxapp.leasedriver.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.app.truxapp.leasedriver.Fragments.AvailableFragment;
import com.app.truxapp.leasedriver.Model.AvailableModel;
import com.app.truxapp.leasedriver.response.AvailableOrderResponse;
import com.bumptech.glide.Glide;
import com.app.truxapp.leasedriver.Fragments.FragmentAttendanceReports;
import com.app.truxapp.leasedriver.Fragments.FragmentChengeLanguage;
import com.app.truxapp.leasedriver.Fragments.FragmentTripReports;
import com.app.truxapp.leasedriver.Fragments.HomeFragment;
import com.app.truxapp.leasedriver.Fragments.ReportFragments;
import com.app.truxapp.leasedriver.Model.AllowedFeatureModel;
import com.app.truxapp.leasedriver.Model.CheckVehicleModel;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.response.CheckVehicleResponse;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.CommonUtils;
import com.app.truxapp.leasedriver.utility.Constants;
import com.app.truxapp.leasedriver.utility.DialogUtils;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, TimePickerDialog.OnTimeSetListener {
    public Stack<Fragment> mFrStack;
    public int VisibleFragmentPosition;
    public String VisibleFragmentName;
    public Fragment VisibleFragment;
    ImageView imageView;
    String VersionName;
    TextView driver_name, client_name, vehicle_no;
    int prefill_driverid;
    Realm realm;
    PostAttendanceRealmLogin loginRealmcommondata;
    String token;
    Dialog dialog;
    String dateSelectedString;
   TextView input_logout_date_time;
    String current_servertime,login_openingkm;
    String last_logintime,  driver_id,driver_image,driversx_name,driver_com,driver_no,prefill_id,driver_phoneno;
    String input_km;
    String dateTime;
    String dstatus;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (VisibleFragment instanceof HomeFragment) {
                onBackPressed();
            }
            else if (VisibleFragment instanceof AvailableFragment) {
                onBackPressed();
            }
            else if (VisibleFragment instanceof FragmentChengeLanguage) {
                onBackPressed();
            }
            else if (VisibleFragment instanceof ReportFragments) {
                onBackPressed();
            }
            else if (VisibleFragment instanceof FragmentAttendanceReports || VisibleFragment instanceof FragmentTripReports) {
                addFragment(new ReportFragments(), getString(R.string.menu_reports));

            } else if (VisibleFragment == null) {
                finish();
            } else if (mFrStack.size() == 0) {
                addFragment(new HomeFragment(), getString(R.string.menu_home));
            } else {
                getSupportFragmentManager().popBackStack();
                getCurrentFragment();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            VersionName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mFrStack = new Stack<>();
        realm = Realm.getDefaultInstance();
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        loginRealmcommondata = RealmController.with(this).getPostAttendanceRealmLoginDetail();
        token = loginRealmcommondata.getToken();
        current_servertime= loginRealmcommondata.getCurrentServerTime();
         last_logintime= loginRealmcommondata.getLastLoginTime();
        driver_id=loginRealmcommondata.getDriverId();
        driver_image=loginRealmcommondata.getDriverImage();
        driversx_name =loginRealmcommondata.getDriverName();
         driver_com = loginRealmcommondata.getDriverClientName();
         driver_no = loginRealmcommondata.getVehicleNumber();
        driver_phoneno=loginRealmcommondata.getDriverPhoneNumber();
        login_openingkm=loginRealmcommondata.getLoginOpeningKM();
        prefill_driverid=loginRealmcommondata.getPrefillDriverId();
        dstatus=  loginRealmcommondata.getDstatus();
        if(driver_image!=null && token!=null){
            getAwsCredential();

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        addFragment(new HomeFragment(), getString(R.string.menu_home));
        hideItem();

    }
    private void hideItem() {
        if (dstatus != null) {
            if (dstatus.equalsIgnoreCase("Free")) {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                Menu nav_Menu = navigationView.getMenu();
                nav_Menu.findItem(R.id.available_order).setVisible(true);
                nav_Menu.findItem(R.id.home).setVisible(false);
                addFragment(new AvailableFragment(), getString(R.string.menu_available));
            } else {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                Menu nav_Menu = navigationView.getMenu();
                nav_Menu.findItem(R.id.available_order).setVisible(false);
                nav_Menu.findItem(R.id.home).setVisible(true);
                addFragment(new HomeFragment(), getString(R.string.menu_home));

            }

        }
    }

    public void getAwsCredential() {
        String url = UrlService.BASE_URL + UrlService.AWS_KEY +  driver_id+ "&appName=lvt_driver";
        BaseActivity.showLog("" + url);
        if (checkInternateConnection(findViewById(R.id.show_error), this)) {
            goToWebCallOfAwsCred(url);
        }
    }

    private void goToWebCallOfAwsCred(String url) {
        DialogUtils.showCustomProgressDialog(this, getResources().getString(R.string.loading), null);
        String URL = url;
        final RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                DialogUtils.removeCustomProgressDialog();
                try {
                    JSONObject data = new JSONObject(resultJson.getString("data"));
                    if (resultJson.getString("errorCode").equals("100")) {
                        CommonUtils.aws_key = data.getString("aws_key");
                        CommonUtils.aws_password = data.getString("aws_password");
                        setProfileImage();
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
        }) {
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

    private void setProfileImage() {
        if ( driver_image!= null && driver_image.length() > 0) {
            String imageurl = CommonUtils.s3UrGenerator(driver_image);
            Glide.with(this)
                    .load(imageurl)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .error(R.drawable.app_launcher_icon)
                    .placeholder(R.drawable.app_launcher_icon)
                    .into(imageView);
        }else {
            Glide.with(this)
                    .load(R.drawable.app_launcher_icon)
                    .placeholder(R.drawable.app_launcher_icon)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(imageView);
        }
    }

    public void setHeaderProfile(String driversx_name, String driver_com, String driver_no, String driver_image) {
        try {
            driver_name.setText(driversx_name);
            client_name.setText(driver_com);
            vehicle_no.setText(driver_no);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void addFragment(Fragment fragment, String mTitle) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(mTitle).commit();
        mFrStack.push(fragment);
        mAddTitle(mTitle);
        getCurrentFragment();
    }

    private void getCurrentFragment() {
        VisibleFragmentPosition = mFrStack.size() - 1;
        VisibleFragment = mFrStack.elementAt(mFrStack.size() - 1);
        if (VisibleFragment instanceof HomeFragment) {
            prefs.setIntValueForTag(Constants.DRAWER_ITEM_POSITION, 0);
            VisibleFragmentName = getString(R.string.menu_home);
        } else if (VisibleFragment instanceof ReportFragments) {
            prefs.setIntValueForTag(Constants.DRAWER_ITEM_POSITION, 1);
            VisibleFragmentName = getString(R.string.menu_reports);
        } else if (VisibleFragment instanceof FragmentChengeLanguage) {
            prefs.setIntValueForTag(Constants.DRAWER_ITEM_POSITION, 2);
            VisibleFragmentName = getString(R.string.menu_language);
        }
        mAddTitle(VisibleFragmentName);
    }

    public void mAddTitle(String mTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.custom_actionbar, null), new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.START)
        );
        TextView textViewTitle = (TextView) findViewById(R.id.apptitle);
        textViewTitle.setText(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        imageView = (ImageView) findViewById(R.id.AimageView);
        driver_name = (TextView) findViewById(R.id.AdriverNameTV);
        client_name = (TextView) findViewById(R.id.AclientNameTV);
        vehicle_no = (TextView) findViewById(R.id.AvehicleNumberTV);
        setHeaderProfile(driversx_name, driver_com, driver_no, driver_image);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            prefs.setIntValueForTag(Constants.DRAWER_ITEM_POSITION, 0);
            addFragment(new HomeFragment(), getString(R.string.menu_home));
        } else if (id == R.id.available_order) {
            prefs.setIntValueForTag(Constants.DRAWER_ITEM_POSITION, 1);
            addFragment(new AvailableFragment(), getString(R.string.menu_available));
        } else if (id == R.id.reports) {
            prefs.setIntValueForTag(Constants.DRAWER_ITEM_POSITION, 2);
            addFragment(new ReportFragments(), getString(R.string.menu_reports));
        } else if (id == R.id.language) {
            prefs.setIntValueForTag(Constants.DRAWER_ITEM_POSITION, 3);
            addFragment(new FragmentChengeLanguage(), getString(R.string.menu_language));

        } else if (id == R.id.logout) {
            checkForLogout();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void getLatLong(double latitude, double longitude) {
        super.getLatLong(latitude, longitude);
        goToLogoutWebcall(latitude,  longitude);
    }

    private void goToLogoutWebcall(double latitude, double longitude) {
        String url = UrlService.BASE_URL + UrlService.POST_LOGOUT_URL;
        JSONObject obj = new JSONObject();
        try {
            obj.put("punchIn", 0);
            obj.put("vehicleNo", driver_no);
            obj.put("deviceId", CommonUtils.getInstance().getIMEI(this));
            obj.put("lgLat", latitude);
            obj.put("lgLong", longitude);
            obj.put("apkVersion", VersionName);
            obj.put("closingKm", input_km);
            obj.put("driverId", prefill_driverid);
            obj.put("driverMobile",driver_phoneno );
            obj.put("driverName", driversx_name);
            if(dateTime !=null && !dateTime.equalsIgnoreCase("") ){
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                Date date = null;
                try {
                    date = formatter.parse(dateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                obj.put("lastLogoutTime", date.getTime());

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseActivity.showLog("" + url);
        if (checkInternateConnection(findViewById(R.id.show_error), this)) {
            LogoutCall(url, obj, dialog);
        }
    }

    private void LogoutCall(String url, JSONObject obj, final DialogInterface dialog) {
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
                    CheckVehicleResponse rsp = new Gson().fromJson(resultJson.toString(), CheckVehicleResponse.class);
                    if (rsp.getErrorCode().equals("100")) {
                        dialog.dismiss();
                        Intent intent=new Intent(MainActivity.this,LoginScreen.class);
                        startActivity(intent);

                    } else if (rsp.getErrorCode().equalsIgnoreCase("D101")) {
                        BaseActivity.errordilog_code101(MainActivity.this, rsp.getErrorMesaage());
                    }else if (rsp.getErrorCode().equalsIgnoreCase("P101")) {
                        CheckVehicleModel checkVehicleModel = rsp.getData();
                        AllowedFeatureModel[] allowedFeatures = checkVehicleModel.getAllowedFeatures();
                        updateDatabase(checkVehicleModel);
                        checkValidPilot(checkVehicleModel, allowedFeatures);
                    }
                    else if (rsp.getErrorCode().equals("101")){
                            showSnackBar(findViewById(R.id.show_error), MainActivity.this,rsp.getErrorMesaage());

                    }
                    else {
                        showSnackBar(findViewById(R.id.show_error), MainActivity.this,rsp.getErrorMesaage());
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



    private void show_dialoge(long time_def) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_logoutdilog);
        Button cancle = dialog.findViewById(R.id.cancle);
        Button submit = dialog.findViewById(R.id.submit);
        final TextView closing_kmonlogout = dialog.findViewById(R.id.closing_kmonlogout);
        input_logout_date_time = dialog.findViewById(R.id.input_logout_date_time);
        final ImageView date_time = dialog.findViewById(R.id.date_time);
        LinearLayout linear=dialog.findViewById(R.id.date_layout);
        final EditText input_boxentry = dialog.findViewById(R.id.input_kmonlogout);
         closing_kmonlogout.setText(login_openingkm);
         if(time_def ==0){
            linear.setVisibility(View.GONE);
         }else {
            linear.setVisibility(View.VISIBLE);
         }
        date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDate();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    input_km= input_boxentry.getText().toString();
                    String closingkm= closing_kmonlogout.getText().toString();
                    dateTime = input_logout_date_time.getText().toString();
                    if (input_km != null && closingkm !=null) {
                        if(Integer.parseInt(input_km)>= Integer.parseInt(closingkm)){

                            checkForValidLocation();
                           // goToLogoutWebcall();
                        }
                        else
                        {
                            showSnackBar(findViewById(R.id.show_error), MainActivity.this, getString(R.string.error_difference));
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        });
        dialog.show();
    }

    private void showSelectDate() {
        int mYear;
        int mMonth;
        int mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear = monthOfYear + 1;
                        String day, month;
                        if (dayOfMonth < 10)
                        {
                            day = "0" + dayOfMonth;
                        }
                        else{
                            day = "" + dayOfMonth;
                        }

                        if (monthOfYear < 10){
                            month = "0" + monthOfYear;}
                        else{
                            month = "" + monthOfYear;
                        }

                       dateSelectedString  = day + "-"
                                + month + "-" + year;
                    /*dateTV.setText(day + "-"
                            + month + "-" + year);*/
                        showTimeDialog();
                       // input_logout_date_time.setText(date);


                    }
                }, mYear, mMonth, mDay);
        dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok), dpd);
        dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), dpd);
        dpd.getDatePicker().setMaxDate(new Date().getTime());
        dpd.getDatePicker().updateDate(mYear, mMonth, mDay);
        dpd.show();
    }

    private void showTimeDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog timepickerdialog = TimePickerDialog.newInstance(this,
                now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true);
        timepickerdialog.setThemeDark(false); //Dark Theme?
        timepickerdialog.vibrate(false); //vibrate on choosing time?
        timepickerdialog.dismissOnPause(false); //dismiss the dialog onPause() called?
        timepickerdialog.enableSeconds(true); //show seconds?

        //Handling cancel event
        timepickerdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(MainActivity.this, "Cancel choosing time", Toast.LENGTH_SHORT).show();
            }
        });
        timepickerdialog.show(getFragmentManager(), "Timepickerdialog"); //show time picker dialog
    }

    private void checkForLogout() {
        long timeDef = CommonUtils.getNumberOfDay(current_servertime,last_logintime);
            show_dialoge(timeDef);

    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time =  hourString + ":" + minuteString + ":" + secondString;
        input_logout_date_time.setText(dateSelectedString+" "+time);
    }

    @Override
    public void onBackPressed() {

    }
    private void checkValidPilot(CheckVehicleModel checkVehicleModel,AllowedFeatureModel []allowedFeatures) {
        String driverStatus=checkVehicleModel.getDriverStatus();
        String loginStatus=checkVehicleModel.getLoginStatus();
        if(driverStatus !=null  && loginStatus!=null){
            if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("0")){
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                intent.putExtra("data",checkVehicleModel);
                startActivity(intent);
                finish();
            }
            else if(driverStatus.equalsIgnoreCase("0")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if(driverStatus.equalsIgnoreCase("1")&&loginStatus.equalsIgnoreCase("1")){
                Intent intent = new Intent(MainActivity.this, StopJourningActivity.class);
                intent.putExtra("allowedfeaturemodel",allowedFeatures);
                startActivity(intent);
                finish();
            }
        }
    }
    private void updateDatabase(CheckVehicleModel allowedFeatureModel) {
        final RealmResults<PostAttendanceRealmLogin> booksUser = RealmController.with(this).getPostAttendanceRealmLoginDataAll();
        realm.beginTransaction();
        booksUser.get(0).setBookingId(allowedFeatureModel.getBookingId());
        realm.commitTransaction();

    }

}
