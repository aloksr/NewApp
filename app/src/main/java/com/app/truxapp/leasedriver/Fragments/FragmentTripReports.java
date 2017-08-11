package com.app.truxapp.leasedriver.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.MainControllerApplication;
import com.app.truxapp.leasedriver.Model.ResponseCode;
import com.app.truxapp.leasedriver.Model.TripData;
import com.app.truxapp.leasedriver.Model.TripDocUrl;
import com.app.truxapp.leasedriver.Model.TripDropDetailsData;
//import com.app.truxapp.leasedriver.R;
//import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.activity.BaseActivity;
import com.app.truxapp.leasedriver.activity.MainActivity;
import com.app.truxapp.leasedriver.adapters.DropDetailAdapter;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.NetworkStateReceiver;
import com.app.truxapp.leasedriver.utility.TouchImageView;
import com.app.truxapp.leasedriver.utility.wheel.OnWheelChangedListener;
import com.app.truxapp.leasedriver.utility.wheel.WheelView;
import com.app.truxapp.leasedriver.utility.wheel.adapter.ArrayWheelAdapter;
import com.app.truxapp.leasedriver.utility.wheel.adapter.NumericWheelAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import io.realm.Realm;


public class FragmentTripReports extends FragmentBase implements  View.OnClickListener{
    public MainControllerApplication mMainControllerApplication;
    private int width;
    PostAttendanceRealmLogin postAttendanceRealmLogin;

    private RelativeLayout mLayoutDetail;
    private LinearLayout layoutdate,layouttrip,layoutChangeDate;
    private TextView textTripId;
    private TextView textTotalKMRun;
    private TextView textSTTime;
    private TextView textETTime;
    private TextView textClick;
    private TextView textNoDetail;
    private TextView textNoDocument;
    private ListView mListView;
    private WheelView day,month,year,trip;
    private Calendar calendar;
    private int TripcurrentItem;
    private ProgressDialog progressDialog;
    private JSONObject Trip;
    public static ArrayList<TripData> TripdListData = new ArrayList<>();
    public static ArrayList<TripDropDetailsData> TripdDropDetailsListData = new ArrayList<>();
    public static ArrayList<TripDocUrl> TripDocUrlListData = new ArrayList<>();
    boolean showdate;
    private ProgressBar imageprogressBar;
    Realm realm;
    private String vehcle_id;

    public FragmentTripReports() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trip_reports, container, false);
        ((MainActivity)getActivity()).mAddTitle(getString(R.string.trip_report));
        realm = Realm.getDefaultInstance();
        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        postAttendanceRealmLogin = RealmController.with(getActivity()).getPostAttendanceRealmLoginDetail();
       vehcle_id= postAttendanceRealmLogin.getVehicleId();
        calendar = Calendar.getInstance();
        month = (WheelView)  rootView.findViewById(R.id.month);
        year = (WheelView)  rootView.findViewById(R.id.year);
        day = (WheelView)  rootView.findViewById(R.id.day);
        trip = (WheelView)  rootView.findViewById(R.id.trip);
        mLayoutDetail = (RelativeLayout) rootView.findViewById(R.id.mLayoutDetail);
        layoutdate = (LinearLayout) rootView.findViewById(R.id.layoutdate);
        layouttrip = (LinearLayout) rootView.findViewById(R.id.layouttrip);
        layoutChangeDate = (LinearLayout) rootView.findViewById(R.id.layoutChangeDate);
        mListView = (ListView) rootView.findViewById(R.id.mListView);
        textTripId = (TextView) rootView.findViewById(R.id.textTripId);
        textTotalKMRun = (TextView) rootView.findViewById(R.id.textTotalKMRun);
        textSTTime = (TextView) rootView.findViewById(R.id.textSTTime);
        textETTime = (TextView) rootView.findViewById(R.id.textETTime);
        textNoDetail = (TextView) rootView.findViewById(R.id.textNoDetail);
        textNoDocument = (TextView) rootView.findViewById(R.id.textNoDocument);
        textClick = (TextView) rootView.findViewById(R.id.textClick);
        Button btnChangeDate = (Button) rootView.findViewById(R.id.btnChangeDate);
        Button btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        textClick.setOnClickListener(this);
        btnChangeDate.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month, day);
            }
        };
        // month
        int curMonth = calendar.get(Calendar.MONTH);
        month.setViewAdapter(new DateArrayAdapter(getActivity(),getResources().getStringArray(R.array.month), curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);
        // year
        int curYear = calendar.get(Calendar.YEAR);
        year.setViewAdapter(new DateNumericAdapter(getActivity(), curYear, curYear, 0));
        year.setCurrentItem(curYear);
        year.addChangingListener(listener);
        //day
        updateDays(year, month, day);
        day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        day.addChangingListener(listener);
//
        return rootView;
    }
    private void init(){
        mLayoutDetail.invalidate();
        SpannableString(textTripId, getResources().getString(R.string.trip_id), String.valueOf(prefs.getIntValueForTag("TripId")));
        SpannableString(textTotalKMRun, getResources().getString(R.string.total_km_run), prefs.getStringValueForTag("totalDistance"));
        SpannableString(textSTTime, getResources().getString(R.string.start_time), prefs.getStringValueForTag("TripStartTime"));
        SpannableString(textETTime, getResources().getString(R.string.end_time), prefs.getStringValueForTag("TripEndTime"));
        if(TripdDropDetailsListData.size()>0) {
            DropDetailAdapter customAdapter = new DropDetailAdapter(getActivity(), TripdDropDetailsListData);
            mListView.setAdapter(customAdapter);
        }
    }
    private void SpannableString(TextView mTextView,String value1,String value2){
        SpannableString mSpannable1 = new SpannableString(value1);
        mSpannable1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, mSpannable1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.setText(mSpannable1);
        SpannableString mSpannable2 = new SpannableString(value2);
        mSpannable2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_button)), 0, mSpannable2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.append(mSpannable2);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (!NetworkStateReceiver.isOnline(getActivity().getApplicationContext())) {
                    mMainControllerApplication.showError(getResources().getString(R.string.enable_internet),R.drawable.repeating_bg_error);
                }else {
                    if (!showdate) {
                        if (day.getCurrentItem() + 1 > Calendar.getInstance().get(Calendar.DAY_OF_MONTH) || month.getCurrentItem() + 1 > Calendar.getInstance().get(Calendar.MONTH) + 1 || calendar.get(Calendar.YEAR) > Calendar.getInstance().get(Calendar.YEAR)) {
                           // mMainControllerApplication.showError(getString(R.string.error_date), R.drawable.repeating_bg_error);
                            BaseActivity.showSnackBar(getActivity().findViewById(R.id.show_error),getActivity(),getString(R.string.error_date));
                        } else {
                            String Date =  String.valueOf(calendar.get(Calendar.YEAR) + year.getCurrentItem())+ "-" + String.valueOf(month.getCurrentItem() + 1) + "-" +String.valueOf(day.getCurrentItem() + 1 );
                            getTripFromServer(vehcle_id, Date, UrlService.FIND_ALL_TRIP, ResponseCode.FIND_ALL_TRIP);
                        }
                    } else {
                        TripData Item = TripdListData.get(trip.getCurrentItem());
                        getBookingDetailFromServer("clientRequestId="+Item.getBookingLeaseId()+"&isBookingId=true", UrlService.BOOKING_REPORT, ResponseCode.BOOKING_REPORT);
                    }
                }
                break;
            case R.id.btnChangeDate:
                layoutdate.setVisibility(View.VISIBLE);
                layouttrip.setVisibility(View.GONE);
                layoutChangeDate.setVisibility(View.GONE);
                showdate = false;
                init();
                break;
            case R.id.textClick:
                mOpenTripStheetDilog();
                break;
        }
    }

    private void mOpenTripStheetDilog() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setIcon(R.drawable.app_launcher_icon);
        builderSingle.setTitle(getString(R.string.select_document));
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.select_dialog_singlechoice);
        for(int i=0;i<TripDocUrlListData.size();i++) {
            TripDocUrl docUrlItem = TripDocUrlListData.get(i);
            arrayAdapter.add(docUrlItem.getDocName());
        }
        builderSingle.setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, final int position) {
                String strName = arrayAdapter.getItem(position);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                builderInner.setMessage(strName);
                builderInner.setTitle(getString(R.string.your_selected_item));
                builderInner.setPositiveButton(getString(R.string.ok),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        showImage(TripDocUrlListData.get(position).getDocUrl());
                        dialog.dismiss();
                    }
                });
                builderInner.setNegativeButton(getString(R.string.cancel),new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }
    private void showImage(String docUrl) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dailog_zoom_image, null, false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        if(dialog.getWindow()==null)
            return;
        dialog.getWindow().setLayout(width - width / 8, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TouchImageView imgDisplay = (TouchImageView) dialog.findViewById(R.id.imgDisplay);
        imageprogressBar = (ProgressBar) dialog.findViewById(R.id.imageprogressBar);
        imageprogressBar.setVisibility(View.VISIBLE);
        if(!docUrl.equals("") || !docUrl.equals("null")) {
            Picasso.with(getActivity()).load(docUrl)
                    .error(getResources().getDrawable(R.drawable.app_launcher_icon))
                    .fit()
                    .centerCrop()// optional
                    .noFade()
                    .into(imgDisplay, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    imageprogressBar.setVisibility(View.GONE);
                }
                @Override
                public void onError() {
                    imageprogressBar.setVisibility(View.GONE);
                }
            });

        }
        Button btnCancel = (Button) dialog.findViewById(R.id.btncancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (! getActivity().isFinishing()) {
            dialog.show();
        }
    }

    /**
     * Updates day wheel. Sets max days according to selected month and year
     */
    void updateDays(WheelView year, WheelView month, WheelView day) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        day.setViewAdapter(new DateNumericAdapter(getActivity(), 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
        int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
        day.setCurrentItem(curDay - 1, true);
    }



    private void TripValue(WheelView trip, String mTripRecord[],int tripcurrentItem) {
        OnWheelChangedListener mTripListener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                updateTrip(TripRecord,TripcurrentItem);
            }
        };
        trip.setViewAdapter(new TripArrayAdapter(getActivity(), mTripRecord, tripcurrentItem));
        trip.setCurrentItem(TripcurrentItem);
        trip.addChangingListener(mTripListener);
        trip.setVisibleItems(3);
    }

//    private void updateTrip(String mTripRecord[],int tripcurrentItem) {
//        trip.setViewAdapter(new TripArrayAdapter(mContext, mTripRecord, tripcurrentItem));
//        trip.setCurrentItem(tripcurrentItem);
//    }

    private class TripArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        /**
         * Constructor
         */
        private TripArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(14);

        }
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            TripcurrentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
    /**
     * Adapter for numeric wheels. Highlights the current value.
     */
    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        /**
         * Constructor
         */
        private DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(14);
        }
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
    /**
     * Adapter for string based wheel. Highlights the current value.
     */
    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;
        /**
         * Constructor
         */
        private DateArrayAdapter(Context context, String[] items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(14);
        }
        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            view.setTypeface(Typeface.SANS_SERIF);
        }
        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }
    private void getBookingDetailFromServer(String VALUE, String SUBURL,  final int what) {

        progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading), true);
        String URL = UrlService.BASE_URL + SUBURL+VALUE;
        //LoginActivity.showLog("URL :" + URL + "mJSONObject " + Trip);
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,
                URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                progressDialog = null;
                String resultObj = resultJson.toString();
                try {
                    if (resultObj.equals(""))
                        return;
                    if (getActivity() != null)
                        mServerResponse(resultObj,what);
                } catch (Exception e) {
                    System.out.println(e.toString());
                  //  BaseActivity.showSnackBar(getActivity().findViewById(R.id.show_error),getActivity(),getResources().getString(R.string.error_service));
                   // mMainControllerApplication.showError(getResources().getString(R.string.error_service), R.drawable.repeating_bg_error);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                progressDialog = null;
                System.out.println(error.toString());
                if (getActivity() != null)
                    if (error instanceof TimeoutError) {
                        BaseActivity.showSnackBar(getActivity().findViewById(R.id.show_error),getActivity(),getResources().getString(R.string.error_timeOut));
                      //  mMainControllerApplication.showError(getString(R.string.error_timeOut), R.drawable.repeating_bg_error);
                    } else if (error instanceof NoConnectionError) {
                        BaseActivity.showSnackBar(getActivity().findViewById(R.id.show_error),getActivity(),getResources().getString(R.string.error_NoConnectionError));

                        // mMainControllerApplication.showError(getString(R.string.error_NoConnectionError), R.drawable.repeating_bg_error);
                    } else if (error instanceof AuthFailureError) {
                        BaseActivity.showSnackBar(getActivity().findViewById(R.id.show_error),getActivity(),getResources().getString(R.string.error_AuthFailureError));


                        //mMainControllerApplication.showError(getString(R.string.error_AuthFailureError), R.drawable.repeating_bg_error);
                    } else if (error instanceof ServerError) {
                        BaseActivity.showSnackBar(getActivity().findViewById(R.id.show_error),getActivity(),getResources().getString(R.string.error_ServerError));


                      //  mMainControllerApplication.showError(getString(R.string.error_ServerError), R.drawable.repeating_bg_error);
                    } else if (error instanceof NetworkError) {

                        BaseActivity.showSnackBar(getActivity().findViewById(R.id.show_error),getActivity(),getResources().getString(R.string.error_internet));
                      //  mMainControllerApplication.showError(getString(R.string.error_internet), R.drawable.repeating_bg_error);
                    }
            }
        });
        queue.add(jsObjRequest);
    }
    private void getTripFromServer(String vehicleId,String journeyStartDate,String SUBURL,final int what) {
        try {
            Trip = new JSONObject();
            Trip.put("vehicleId", vehicleId);
            Trip.put("journeyStartDate", journeyStartDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog = ProgressDialog.show(getActivity(), "", getResources().getString(R.string.loading), true);
        String URL = UrlService.BASE_URL + SUBURL;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,
                URL, Trip, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                    progressDialog = null;
                String resultObj = resultJson.toString();
                try {
                    if (resultObj.isEmpty())
                        return;
                    if (getActivity() != null)
                        mServerResponse(resultObj,what);
                } catch (Exception e) {
                    System.out.println(e.toString());
                    BaseActivity.showSnackBar(getActivity().findViewById(R.id.show_error),getActivity(),getResources().getString(R.string.error_service));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                    progressDialog = null;

            }
        });
        queue.add(jsObjRequest);
    }
    private void mServerResponse(String result, int responseCode) {
        try {
            JSONObject resultJson = new JSONObject(result);
            if (resultJson.getInt("errorCode") == 100 && responseCode == ResponseCode.FIND_ALL_TRIP) {
                TripData mTripData;
                JSONArray mJSONArray = resultJson.getJSONArray("data");
                TripdListData.clear();
                for(int i = 0; i< mJSONArray.length(); i++) {
                    mTripData = new TripData();
                    JSONObject jsObjRequestData = (JSONObject) mJSONArray.get(i);
                    mTripData.setBookingLeaseId(jsObjRequestData.getInt("bookingLeaseId"));
                    mTripData.setBookingStartDate(jsObjRequestData.getString("bookingStartDate"));
                    mTripData.setBookingStartTime(jsObjRequestData.getString("bookingStartTime"));
                    mTripData.setBookingEndDate(jsObjRequestData.getString("bookingEndDate"));
                    mTripData.setBookingEndTime(jsObjRequestData.getString("bookingEndTime"));
                    TripdListData.add(mTripData);
                }
                mfillTripWheel(trip,TripdListData,TripcurrentItem);
            }else if (resultJson.getInt("errorCode") == 100 && responseCode == ResponseCode.BOOKING_REPORT) {
                TripDropDetailsData mTripDropDetailsData;
                TripDocUrl mTripDocUrl;
                TripDocUrlListData.clear();
                TripdDropDetailsListData.clear();
                JSONObject jsonObject = resultJson.getJSONObject("data");
                if(!jsonObject.getString("bookingLease").equals("null")) {
                    JSONObject jsonbookingLease = jsonObject.getJSONObject("bookingLease");
                    prefs.setIntValueForTag("TripId", jsonbookingLease.optInt("bookingLeaseId", 0));
                    prefs.setStringValueForTag("TripStartTime", splitDate(jsonbookingLease.optString("journeyStartDateConvert","0").replace("null","0")));
                    prefs.setStringValueForTag("TripEndTime", splitDate(jsonbookingLease.optString("journeyEndDateConvert","0").replace("null","0")));
                    prefs.setStringValueForTag("totalDistance", String.valueOf(jsonbookingLease.optInt("totalDistance", 0)));
                    if(!jsonObject.getString("bookingLeaseStop").equals("null")) {
                        JSONArray mJSONArrayDropDetails = jsonObject.getJSONArray("bookingLeaseStop");
                        for(int i=0;i<mJSONArrayDropDetails.length();i++) {
                            mTripDropDetailsData = new TripDropDetailsData();
                            JSONObject jsObjRequestData = (JSONObject) mJSONArrayDropDetails.get(i);
                            mTripDropDetailsData.setAwbNumber(jsObjRequestData.optString("awbNumber", "N/A").replace("null","N/A"));
                            mTripDropDetailsData.setDropedBoxes(jsObjRequestData.optInt("dropedBoxes",0));
                            mTripDropDetailsData.setAfterDropStartTime(splitDate(jsObjRequestData.optString("afterDropStartTimeConvert","0").replace("null","0")));
                            mTripDropDetailsData.setDropLocationReachTime(splitDate(jsObjRequestData.optString("dropLocationReachTimeConvert","0").replace("null","0")));
                            mTripDropDetailsData.setDropLocation(jsObjRequestData.optString("dropLocation", "N/A").replace("null","N/A"));
                            TripdDropDetailsListData.add(mTripDropDetailsData);
                        }
                        textNoDetail.setVisibility(View.GONE);
                    }else{
                        textNoDetail.setVisibility(View.VISIBLE);
                    }
                    if(!jsonObject.getString("bookingLeaseDocs").equals("null")) {
                        JSONArray mJSONArrayDocUrl = jsonObject.getJSONArray("bookingLeaseDocs");
                        for(int i=0;i<mJSONArrayDocUrl.length();i++) {
                            mTripDocUrl = new TripDocUrl();
                            JSONObject jsObjRequestData = (JSONObject) mJSONArrayDocUrl.get(i);
                            mTripDocUrl.setDocName(jsObjRequestData.optString("docName", "N/A").replace("null","N/A"));
                            mTripDocUrl.setDocAmount(jsObjRequestData.optString("docAmount", "N/A").replace("null","N/A"));
                            mTripDocUrl.setDocNumber(jsObjRequestData.optString("doc_nNumber", "N/A").replace("null","N/A"));
                            mTripDocUrl.setDocUrl(jsObjRequestData.optString("docUrl", "N/A").replace("null","N/A"));
                            TripDocUrlListData.add(mTripDocUrl);
                        }
                        textNoDocument.setVisibility(View.GONE);
                        textClick.setVisibility(View.VISIBLE);
                    }else{
                        textNoDocument.setVisibility(View.VISIBLE);
                        textClick.setVisibility(View.GONE);
                    }
                    mLayoutDetail.setVisibility(View.VISIBLE);
                    init();
                }else{
                    mMainControllerApplication.showError(getResources().getString(R.string.no_data), R.drawable.repeating_bg_error);
                }
            }
            else{
                mMainControllerApplication.showError(getResources().getString(R.string.no_data), R.drawable.repeating_bg_error);
            }
        } catch (Exception e) {
            mMainControllerApplication.showError(getResources().getString(R.string.error_service), R.drawable.repeating_bg_error);
        }
    }

    private String splitDate(String journeyStartDate) {
        mChangeLocal1();
        String delims = " ";
        System.out.println("StringTokenizer Example: \n");
        StringTokenizer st = new StringTokenizer(journeyStartDate, delims);
        while (st.hasMoreElements()) {
            System.out.println("StringTokenizer Output: " + st.nextElement());
        }
        System.out.println("\n\nSplit Example: \n");
        String[] tokens = journeyStartDate.split(delims);
        for (String token : tokens) {
            System.out.println("Split Output: " + token);
        }
        String time = tokens[2];
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm a");
        String out="";
        try {
            Date date = dateFormat.parse(time);

            out = dateFormat2.format(date);
            Log.e("Time", out);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mChangeLocal2();
        return out;
    }

    private void mfillTripWheel(WheelView trip, ArrayList<TripData> tripdListData, int tripcurrentItem) {
        ArrayList<String> tripRecord = new ArrayList<>();
        String[]  TripArray=null;
        tripRecord.clear();

        for(int i=0;i<tripdListData.size();i++) {
            TripData Item = tripdListData.get(i);
            tripRecord.add("From "+Item.getBookingStartTime()+" To "+Item.getBookingEndTime());
            TripArray = new String[tripRecord.size()];
            tripRecord.toArray(TripArray);
        }
        layoutdate.setVisibility(View.GONE);
        layouttrip.setVisibility(View.VISIBLE);
        layoutChangeDate.setVisibility(View.VISIBLE);
        mLayoutDetail.invalidate();
        showdate = true;
        TripValue(trip, TripArray, TripcurrentItem);
        init();
    }
}



