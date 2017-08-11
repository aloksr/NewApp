package com.app.truxapp.leasedriver.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.truxapp.leasedriver.Model.TripDropDetailsData;
import com.app.truxapp.leasedriver.R;

import java.util.ArrayList;


public class DropDetailAdapter extends BaseAdapter {

    private ArrayList listData;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public DropDetailAdapter(Context context, ArrayList listData) {
        this.listData = listData;
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.trip_drop_detail_item, null);
            holder = new ViewHolder();
            holder.textDestination = (TextView) convertView.findViewById(R.id.textDestination);
            holder.textSTime = (TextView) convertView.findViewById(R.id.textSTime);
            holder.textETime = (TextView) convertView.findViewById(R.id.textETime);
            holder.textBox = (TextView) convertView.findViewById(R.id.textBox);
            holder.textAwbNo = (TextView) convertView.findViewById(R.id.textAwbNo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TripDropDetailsData Item = (TripDropDetailsData) listData.get(position);
        SpannableString(holder.textDestination, mContext.getResources().getString(R.string.destination), Item.getDropLocation());
        SpannableString(holder.textSTime, mContext.getResources().getString(R.string.start_time),  Item.getAfterDropStartTime());
        SpannableString(holder.textETime, mContext.getResources().getString(R.string.end_time), Item.getDropLocationReachTime());
        SpannableString(holder.textBox, mContext.getResources().getString(R.string.no_of_box),String.valueOf(Item.getDropedBoxes()));
        SpannableString(holder.textAwbNo, mContext.getResources().getString(R.string.awb_no), String.valueOf(Item.getAwbNumber()));
        return convertView;
    }
    private void SpannableString(TextView mTextView,String value1,String value2){
        SpannableString mSpannable1 = new SpannableString(value1);
        mSpannable1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black)), 0, mSpannable1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.setText(mSpannable1);
        SpannableString mSpannable2 = new SpannableString(value2);
        mSpannable2.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.red_button)), 0, mSpannable2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTextView.append(mSpannable2);
    }
    static class ViewHolder {
        TextView textDestination,textSTime,textETime,textBox,textAwbNo;
    }
}
