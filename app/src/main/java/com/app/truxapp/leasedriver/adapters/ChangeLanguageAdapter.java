package com.app.truxapp.leasedriver.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.utility.AppPreference;
import com.app.truxapp.leasedriver.utility.CommonUtils;
import com.app.truxapp.leasedriver.utility.Constants;

import java.util.List;


public class ChangeLanguageAdapter extends ArrayAdapter<String> {
    private List<String> objects;
    private AppPreference prefs;
    public ChangeLanguageAdapter(Context context, int textViewResourceId, List<String> objects, AppPreference pHelper) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        this.prefs = pHelper;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.change_language_item, null);
        }
        ImageView changeLanguageCB = (ImageView) convertView.findViewById(R.id.changeLanguageCB);
        TextView changeLanguageTV = (TextView) convertView.findViewById(R.id.changeLanguageTV);
        changeLanguageTV.setText(getItem(position));
        if (getSelectedPosition(objects, CommonUtils.getInstance().getLanguage(prefs.getStringValueForTag(Constants.SELECT_LANGUAGE))) == position){
            changeLanguageTV.setTextColor(Color.RED);
            changeLanguageCB.setVisibility(View.VISIBLE);
        } else{
            changeLanguageTV.setTextColor(Color.parseColor("#666666"));
            changeLanguageCB.setVisibility(View.GONE);
        }
        return convertView;
    }

    private int getSelectedPosition(List<String> arrayList, String item) {
        int position = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).contains(item)) {
                position = i;
            }
        }
        return position;
    }
}
