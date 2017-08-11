package com.app.truxapp.leasedriver.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.truxapp.leasedriver.Model.LanguageModel;
import com.app.truxapp.leasedriver.R;

import java.util.List;


public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
    private RecyclerView parentRecycler;
    private List<LanguageModel> data;
    public LanguageAdapter(List<LanguageModel> data) {
        this.data = data;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecycler = recyclerView;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_city_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LanguageModel languageModel = data.get(position);
        holder.title.setText(languageModel.getTitle());
        holder.sub_title.setText(languageModel.getSub_title());
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView sub_title;
        private LinearLayout frameLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            sub_title = (TextView) itemView.findViewById(R.id.sub_title);
            frameLayout = (LinearLayout) itemView.findViewById(R.id.container);
            itemView.findViewById(R.id.container).setOnClickListener(this);
        }
        public void showText() {
            sub_title.setTextColor(Color.parseColor("#ffffff"));
            title.setTextColor(Color.parseColor("#ffffff"));
            frameLayout.setBackgroundColor(Color.RED);
        }

        public void hideText() {
            sub_title.setTextColor(Color.parseColor("#000000"));
            title.setTextColor(Color.parseColor("#000000"));
            frameLayout.setBackgroundColor(Color.parseColor("#eeeeee"));

        }

        @Override
        public void onClick(View v) {
            parentRecycler.smoothScrollToPosition(getAdapterPosition());
        }
    }


}
