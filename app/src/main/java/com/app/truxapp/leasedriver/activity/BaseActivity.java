package com.app.truxapp.leasedriver.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;



import android.location.Location;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.androidadvance.topsnackbar.TSnackbar;
import com.app.truxapp.leasedriver.MainControllerApplication;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.services.LocationService;
import com.app.truxapp.leasedriver.utility.AppPreference;
import com.app.truxapp.leasedriver.utility.Constants;
import com.app.truxapp.leasedriver.utility.DialogUtils;
import com.app.truxapp.leasedriver.utility.GlobalData.ConstantData;
import com.app.truxapp.leasedriver.utility.MyProgressDialog;
import com.app.truxapp.leasedriver.utility.NetworkStateReceiver;

import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

import static com.app.truxapp.leasedriver.utility.GlobalData.ConstantData.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS;

import com.app.truxapp.leasedriver.R;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;


public class BaseActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    public MainControllerApplication mMainControllerApplication;
    public LocationSettingsRequest mLocationSettingsRequest;
    public Location mCurrentLocation;
    public AppPreference prefs;
    public RequestQueue queue;
    public LocationRequest mLocationRequest;
    public int width;
    public Map<String, String> params = new ArrayMap<>();
    public Display display;
    public DisplayMetrics metrics;
    public String directoryName;
    public GoogleApiClient mGoogleApiClient;
    private Realm realm;

    private Timer timer;
    private int counter;


    public static int getColor(Context context, int id) {
        return ContextCompat.getColor(context, id);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();

        prefs = new AppPreference(this);
        mMainControllerApplication = MainControllerApplication.getInstance();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels;
        prefs = new AppPreference(this);

    }

    public boolean checkInternateConnection(View view ,Context context)
    {
        if (!NetworkStateReceiver.isOnline(this)) {
            showSnackBar(view,context, getResources().getString(R.string.enable_internet));
        }
        return  NetworkStateReceiver.isOnline(this);
    }

   public static void showSnackBar(View view,Context mContext,String message) {
        try {
            TSnackbar snackbar = TSnackbar.make(view, message, TSnackbar.LENGTH_LONG)
                    .setAction("Done", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Log.d("CLICKED Action", "CLIDKED Action");
                        }
                    });
            snackbar.setActionTextColor(Color.WHITE);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(MainActivity.getColor(mContext, R.color.colorPrimary));
            TextView textView = (TextView) snackBarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);

            snackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case ConstantData.REQUEST_CODE:
               // checkIsLoging();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                try {
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            ) {
                        if(checkDrawOverlayPermission()){
                           // checkIsLoging();
                        }

                    } else {
                        finish();
                       // showSnackBar(getString(R.string.allow_all_permission), this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public static void showLog(String message)
    {
        Log.e("ERROR: ",message);
    }



    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23&& ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false;
        }
            return true;

    }
    public void checkIsLoging() {

    }
    public void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<>();
                final List<String> permissionsList = new ArrayList<>();
                if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
                    permissionsNeeded.add(getString(R.string.gps_permission));
                if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
                    permissionsNeeded.add(getString(R.string.calling_permission));
                if (!addPermission(permissionsList, Manifest.permission.CAMERA))
                    permissionsNeeded.add(getString(R.string.camera_permission));
                if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    permissionsNeeded.add(getString(R.string.storage_permission));
                if (permissionsList.size() > 0) {
                    if (permissionsNeeded.size() > 0) {
                        String message = getString(R.string.grant_access) + permissionsNeeded.get(0);
                        for (int i = 1; i < permissionsNeeded.size(); i++)
                            message = message + ", " + permissionsNeeded.get(i);
                        showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(BaseActivity.this, permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                        return;
                    }
                    ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }else {
                  //  checkIsLoging();
                }

    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), okListener)
                .setNegativeButton(getString(R.string.cancel), null)
                .create()
                .show();
    }
    private boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23&&!Settings.canDrawOverlays(getApplicationContext())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, ConstantData.REQUEST_CODE);
            return false;
        }else
            return true;
    }
    public void exitDialog( String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.confimation_));
        alertDialog.setMessage(getResources().getString(R.string.are_you_sure_you_want_to) + " " + message);
        alertDialog.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public  static void errordilog_code101(Context context,String string){
      final  Dialog  dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dilog_d101error);
        Button button = dialog.findViewById(R.id.ok_btn);
        TextView error_text = dialog.findViewById(R.id.error_text);
        error_text.setText(string);
        ImageView cross = dialog.findViewById(R.id.cross_error);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ConstantData.REQUEST_COARSE_LOCATION);
        } else {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            startLocationUpdate();
            buildLocationSettingsRequest();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        BaseActivity.showLog("   service  " + mCurrentLocation);


}


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
       // Log.i(Config.TAG, "Connection suspended");

    }
    private void startLocationUpdate() {
        if (mGoogleApiClient.isConnected())
        {
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)

            {
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
        }

    }

    public void startTimer() {
        DialogUtils.showCustomProgressDialog(this, getResources().getString(R.string.loading), null);
        isLocationUpdated();
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 1000, 1000);
    }
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mCurrentLocation == null) {
                counter = counter + 1;
            } else if (isLocationUpdated() &&mCurrentLocation != null && mCurrentLocation.getLatitude() != 0.0 && mCurrentLocation.getLongitude() != 0.0) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
               runOnUiThread(new Runnable() {
                    public void run() {
                      DialogUtils.removeCustomProgressDialog();
                        getLatLong(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
                    }
                });
            } else {
                counter = counter + 1;
            }
            BaseActivity.showLog("counter  " + counter);
            if (counter == 30) {
                DialogUtils.removeCustomProgressDialog();
                if (timer != null) {
                    timer.cancel();
                }
                counter = 0;
                runOnUiThread(new Runnable() {
                    public void run() {
                        DialogUtils.removeCustomProgressDialog();
                        showLocationNotGeting();
                    }
                });
            }
        }
    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }
    public synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        try {
            mGoogleApiClient.connect();
        } catch (Exception e) {

            e.printStackTrace();
        }
        createLocationRequest();
    }
    public void checkForValidLocation() {
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();

        } else if (!mGoogleApiClient.isConnected()) {
            buildGoogleApiClient();

        } else if (mGoogleApiClient.isConnected() && mCurrentLocation == null) {
            mGoogleApiClient.disconnect();
            buildGoogleApiClient();

        } else {
            getLatLong(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        }

    }

    public void getLatLong(double latitude, double longitude) {

    }


    public void showLocationNotGeting() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getString(R.string.not_getting_location));
        alertDialog.setMessage(getString(R.string.message_getlocation));

        alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              //  getMapLocation();

            }
        });
        if (!isFinishing())
            alertDialog.show();
    }
    public boolean isLocationUpdated() {
        LocationService locationService = new LocationService(this);
        Location location = locationService.getLocation();
        if (location == null)
            return true;
        else {
            if (mCurrentLocation == null) {
                mCurrentLocation = location;
                return true;
            } else {
                int accuracyDelta = (int) (mCurrentLocation.getAccuracy());
                int newLocation = (int) (location.getAccuracy());
                if (accuracyDelta < newLocation)
                    return true;
                else {
                    mCurrentLocation = location;
                    return true;
                }
            }
        }
    }
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        builder.setAlwaysShow(true);
        final PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                BaseActivity.showLog(" service   " + status.getStatusCode());
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startTimer();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // try {
                        //status.startResolutionForResult(BaseActivity.this, Config.REQUEST_LOCATION);
                        startTimer();
                        //} catch (IntentSender.SendIntentException e) {
                        //  e.printStackTrace();
                        //}
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }


        });
    }

}




