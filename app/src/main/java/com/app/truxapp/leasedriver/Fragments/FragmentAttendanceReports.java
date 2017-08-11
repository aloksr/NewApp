package com.app.truxapp.leasedriver.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.truxapp.leasedriver.MainControllerApplication;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.activity.BaseActivity;
import com.app.truxapp.leasedriver.activity.MainActivity;
import com.app.truxapp.leasedriver.realm.RealmController;
import com.app.truxapp.leasedriver.realm_model.PostAttendanceRealmLogin;
import com.app.truxapp.leasedriver.services.UrlService;
import com.app.truxapp.leasedriver.utility.DialogUtils;
import com.app.truxapp.leasedriver.utility.NetworkStateReceiver;
import com.app.truxapp.leasedriver.utility.wheel.OnWheelChangedListener;
import com.app.truxapp.leasedriver.utility.wheel.WheelView;
import com.app.truxapp.leasedriver.utility.wheel.adapter.ArrayWheelAdapter;
import com.app.truxapp.leasedriver.utility.wheel.adapter.NumericWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;


public class FragmentAttendanceReports extends FragmentBase implements View.OnClickListener {
    private static JSONArray mJSONArray;
    public MainControllerApplication mMainControllerApplication;
    private Button btnSubmit;
    private TableLayout mTableLayoutMain, mTableLayoutHeader;
    private RelativeLayout mLayoutTable;
    private TableRow mTableRow1;
    private TableRow.LayoutParams TableRowParams1, TableRowParams2;
    private int background, presentDay;
    private TextView textTotalPresentDay, recyclableTextView, textTotalKM;
    private WheelView month, year;
    private OnWheelChangedListener listener;
    private Calendar calendar;
    private String selectMonth;
    private int maxDays, selectMonthValue, selectYearValue, TKM;
    private ProgressDialog progressDialog;
    Realm realm;
    PostAttendanceRealmLogin loginRealmcommondata;
    String token;
    private String vehcle_no;

    public FragmentAttendanceReports() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        View rootView = inflater.inflate(R.layout.fragment_attendance_reports, container, false);
        ((MainActivity) getActivity()).mAddTitle(getString(R.string.attendance_report));

        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();
        loginRealmcommondata = RealmController.with(this).getPostAttendanceRealmLoginDetail();
        token = loginRealmcommondata.getToken();
        vehcle_no=  loginRealmcommondata.getVehicleNumber();

         calendar = Calendar.getInstance();
        mLayoutTable = (RelativeLayout) rootView.findViewById(R.id.mLayoutTable);
        mTableLayoutMain = (TableLayout) rootView.findViewById(R.id.mTableLayoutMain);
        mTableLayoutHeader = (TableLayout) rootView.findViewById(R.id.mTableLayoutHeader);
        textTotalKM = (TextView) rootView.findViewById(R.id.textTotalKM);
        textTotalPresentDay = (TextView) rootView.findViewById(R.id.textTotalPresentDay);
        month = (WheelView) rootView.findViewById(R.id.month);
        year = (WheelView) rootView.findViewById(R.id.year);
        btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                updateDays(year, month);
            }
        };
        int curMonth = calendar.get(Calendar.MONTH);
        month.setViewAdapter(new DateArrayAdapter(getActivity(), getResources().getStringArray(R.array.month), curMonth));
        month.setCurrentItem(curMonth);
        month.addChangingListener(listener);
        int curYear = calendar.get(Calendar.YEAR);
        year.setViewAdapter(new DateNumericAdapter(getActivity(), curYear, curYear, 0));
        year.setCurrentItem(curYear);
        year.addChangingListener(listener);
        updateDays(year, month);
        return rootView;
    }

    void updateDays(WheelView year, WheelView month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        selectYearValue = calendar.get(Calendar.YEAR) + year.getCurrentItem();
        selectMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
        selectMonthValue = month.getCurrentItem() + 1;
        maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public void init(JSONArray mJSONarray) {
        mTableLayoutHeader.removeAllViews();
        mTableLayoutMain.removeAllViews();
        mLayoutTable.invalidate();
        mLayoutTable.setVisibility(View.VISIBLE);
        TKM = 0;
        presentDay = 0;
        int Sunday = CheckDay(1);
        for (int i = 0; i < mJSONarray.length() + 1; i++) {
            mTableRow1 = new TableRow(getActivity());
            TableRowParams1 = new TableRow.LayoutParams(TableRowParams1.MATCH_PARENT, TableRowParams1.WRAP_CONTENT);
            try {
                if (i == 0) {
                    background = getResources().getColor(R.color.white);
                    mTableRow1.setBackground(getResources().getDrawable(R.drawable.table_row_bg));
                    mTableRow1.addView(makeTableRowWithText(i, selectMonth, getResources().getDrawable(R.drawable.table_cell_bg_first), background));
                    mTableRow1.addView(makeTableRowWithText(i, "Punch-in", getResources().getDrawable(R.drawable.table_cell_bg_inside), background));
                    mTableRow1.addView(makeTableRowWithText(i, "Punch-out", getResources().getDrawable(R.drawable.table_cell_bg_inside), background));
                    mTableRow1.addView(makeTableRowWithText(i, "Start KM", getResources().getDrawable(R.drawable.table_cell_bg_inside), background));
                    mTableRow1.addView(makeTableRowWithText(i, "Last KM", getResources().getDrawable(R.drawable.table_cell_bg_last), background));
                    mTableRow1.setLayoutParams(TableRowParams1);
                    mTableLayoutHeader.addView(mTableRow1);
                } else {
                    JSONObject resultJson = mJSONarray.getJSONObject(i - 1);
                    if (i == Sunday) {
                        Sunday = Sunday + 7;
                        background = getResources().getColor(R.color.light_green);
                    } else if (resultJson.optString("punchIn", "N/A").equals("null")) {
                        background = getResources().getColor(R.color.light_red);
                    } else {
                        if (!String.valueOf(resultJson.getInt("totalKm")).equals("null") || !String.valueOf(resultJson.getInt("totalKm")).equals("")) {
                            TKM = TKM + resultJson.optInt("totalKm", 0);
                        }
                        presentDay = presentDay + 1;
                        background = getResources().getColor(R.color.white);
                    }
                    mTableRow1.addView(makeTableRowWithText(i, String.valueOf(i), getResources().getDrawable(R.drawable.table_cell_bg_first), background));
                    mTableRow1.addView(makeTableRowWithText(i, resultJson.optString("punchIn", "N/A").replace("null", "N/A"), getResources().getDrawable(R.drawable.table_cell_bg_first), background));
                    mTableRow1.addView(makeTableRowWithText(i, resultJson.optString("punchOut", "N/A").replace("null", "N/A"), getResources().getDrawable(R.drawable.table_cell_bg_first), background));
                    mTableRow1.addView(makeTableRowWithText(i, String.valueOf(resultJson.optInt("openingkm", 0)), getResources().getDrawable(R.drawable.table_cell_bg_first), background));
                    mTableRow1.addView(makeTableRowWithText(i, String.valueOf(resultJson.optInt("closingkm", 0)), getResources().getDrawable(R.drawable.table_cell_bg_inside), background));
                    mTableRow1.setLayoutParams(TableRowParams1);
                    mTableLayoutMain.addView(mTableRow1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SpannableString(textTotalPresentDay, String.valueOf(presentDay), "/" + maxDays);
        SpannableString(textTotalKM, String.valueOf(TKM), " KM");
    }

    public int CheckDay(int day) {
        int x = 0;
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.MONTH, selectMonthValue - 1);
        cal.set(Calendar.YEAR, selectYearValue);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (Calendar.MONDAY == dayOfWeek) {
            day = day + 6;
        } else if (Calendar.TUESDAY == dayOfWeek) {
            day = day + 5;
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            day = day + 4;
        } else if (Calendar.THURSDAY == dayOfWeek) {
            day = day + 3;
        } else if (Calendar.FRIDAY == dayOfWeek) {
            day = day + 2;
        } else if (Calendar.SATURDAY == dayOfWeek) {
            day = day + 1;
        } else if (Calendar.SUNDAY == dayOfWeek) {
            day = day;
        }
        return day;
    }

    private void SpannableString(TextView mTextView, String value1, String value2) {
        SpannableString mSpannable1 = new SpannableString(value1);
        mSpannable1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red_button)), 0, mSpannable1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.setText(mSpannable1);
        SpannableString mSpannable2 = new SpannableString(value2);
        mSpannable2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, mSpannable2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.append(mSpannable2);
    }

    public TextView makeTableRowWithText(int loop, String text, Drawable mdrawable, int background) {
        TableRowParams2 = new TableRow.LayoutParams(0, TableRowParams2.WRAP_CONTENT, 1f);
        recyclableTextView = new TextView(getActivity());
        recyclableTextView.setText(text);
        recyclableTextView.setGravity(Gravity.CENTER);
        recyclableTextView.setTextColor(getResources().getColor(R.color.black));
        recyclableTextView.setTextSize(12);
        recyclableTextView.setLayoutParams(TableRowParams2);
        if (loop == 0) {
            recyclableTextView.setBackground(mdrawable);
            recyclableTextView.setPadding(0, 10, 0, 10);
        } else {
            recyclableTextView.setBackgroundColor(background);
            recyclableTextView.setPadding(0, 5, 0, 5);
        }
        return recyclableTextView;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                if (!NetworkStateReceiver.isOnline(getActivity().getApplicationContext())) {
                    mMainControllerApplication.showError(getResources().getString(R.string.enable_internet), R.drawable.repeating_bg_error);
                } else {
                    if (month.getCurrentItem() + 1 > Calendar.getInstance().get(Calendar.MONTH) + 1 || calendar.get(Calendar.YEAR) > Calendar.getInstance().get(Calendar.YEAR)) {
                        //  mMainControllerApplication.showError(getString(R.string.error_date), R.drawable.repeating_bg_error);
                        BaseActivity.showSnackBar(getActivity().findViewById(R.id.show_error), getActivity(), getString(R.string.error_date));
                    } else {
                        mHitServer();
                    }
                }
                break;
        }
    }
    private void mHitServer() {
        StringBuilder sb = new StringBuilder();
        sb.append("vhNumber=" + vehcle_no)
                .append("&month=" + selectMonthValue)
                .append("&year=" + selectYearValue);
        DialogUtils.showCustomProgressDialog(getActivity(), getResources().getString(R.string.loading), null);
        String URL = UrlService.BASE_URL + UrlService.PUNCH_IN_REPORT + sb.toString();
        URL = URL.replace(" ", "%20");
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.getCache().remove(URL);
        queue.getCache().clear();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject resultJson) {
                DialogUtils.removeCustomProgressDialog();
                try {
                        mJSONArray = resultJson.getJSONArray("data");
                        init(mJSONArray);
                } catch (Exception e) {
                    System.out.println(e.toString());
                   // mMainControllerApplication.showError(getResources().getString(R.string.error_service), R.drawable.repeating_bg_error);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogUtils.removeCustomProgressDialog();
                error.printStackTrace();
                error.getMessage();
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


    private class DateNumericAdapter extends NumericWheelAdapter {
        // Index of current item
        int currentItem;
        // Index of item to be highlighted
        int currentValue;

        /**
         * Constructor
         */
        public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
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
        public DateArrayAdapter(Context context, String[] items, int current) {
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
}



