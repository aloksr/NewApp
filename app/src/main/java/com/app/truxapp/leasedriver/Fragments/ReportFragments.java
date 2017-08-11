package com.app.truxapp.leasedriver.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.activity.MainActivity;

public class ReportFragments extends FragmentBase{
    private Button btnAttendanceReport,btnTripReport;
    View view;
    public static ReportFragments newInstance() {
        ReportFragments fragment = new ReportFragments();
        Bundle bundle=new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.report_main_page,container,false);
        btnAttendanceReport = (Button) view.findViewById(R.id.btnAttendancereport);
        btnTripReport = (Button) view.findViewById(R.id.btnTripReport);
        btnAttendanceReport.setOnClickListener(this);
        btnTripReport.setOnClickListener(this);

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).mAddTitle(getString(R.string.menu_reports));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAttendancereport:
                 FragmentAttendanceReports fragmentAttendanceReports=new FragmentAttendanceReports();
                ((MainActivity)getActivity()).addFragment(fragmentAttendanceReports,getString(R.string.attendance_report));
                break;
            case R.id.btnTripReport:
                 FragmentTripReports fragmentTripReports=new FragmentTripReports();
                 ((MainActivity)getActivity()).addFragment(fragmentTripReports,getString(R.string.trip_report));
                 break;
        }
    }
}
