package com.app.truxapp.leasedriver.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.truxapp.leasedriver.R;

import java.util.ArrayList;

public class CommonAutoCompleteAdapter  extends ArrayAdapter<String> {
    int main_int = 0;
    LayoutInflater inflater;
    private ArrayList<String> data;
    public CommonAutoCompleteAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Context _context = context;
        this.data = objects;
        this.main_int = resource;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    public View getCustomView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //return super.getView(position, convertView, parent);
        View row = convertView;
        try {
            String spinnerValues = data.get(position);
            row = inflater.inflate(R.layout.spinner_rows, parent, false);
            final TextView label = (TextView) row.findViewById(R.id.text);
            label.setText(spinnerValues);
            label.setTag(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }



}


