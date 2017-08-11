package com.app.truxapp.leasedriver.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.truxapp.leasedriver.Model.LanguageModel;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.adapters.LanguageAdapter;
import com.app.truxapp.leasedriver.utility.CommonUtils;
import com.app.truxapp.leasedriver.utility.LanguageList;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;
import java.util.Locale;

public class LanguageSelector extends BaseActivity implements
        DiscreteScrollView.ScrollStateChangeListener<LanguageAdapter.ViewHolder>,
        DiscreteScrollView.OnItemChangedListener<LanguageAdapter.ViewHolder>,
        View.OnClickListener{
    TextView Title, SubTitle;
    TextView currentItemPrice;
    ImageView SelectedButton;
    DiscreteScrollView itemPicker;
    InfiniteScrollAdapter infiniteAdapter;
    private List<LanguageModel> languageModels;
    private DiscreteScrollView cityPicker;
    String  salected_language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selector);
        if (Build.VERSION.SDK_INT >= 23) {
            insertDummyContactWrapper();
        }
        SelectedButton = (ImageView) findViewById(R.id.language_selected);
        Title = (TextView) findViewById(R.id.selectLanguageTV);
        SubTitle = (TextView) findViewById(R.id.selectLanguageTV2);
        SelectedButton.setOnClickListener(this);
        languageModels = LanguageList.get().getForecasts();
        cityPicker = (DiscreteScrollView) findViewById(R.id.forecast_city_picker);
        cityPicker.setAdapter(new LanguageAdapter(languageModels));
        cityPicker.addOnItemChangedListener(this);
        cityPicker.addScrollStateChangeListener(this);
        cityPicker.scrollToPosition(2);
       // cityPicker.setItemTransitionTimeMillis(DiscreteScrollViewOptions.getTransitionTime());
        cityPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.9f)
                .build());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.language_selected:
                setSelected(salected_language);
                Intent intent=new Intent(this,LoginScreen.class);
                startActivity(intent);
                finish();

        }

    }

    @Override
    public void onCurrentItemChanged(@Nullable LanguageAdapter.ViewHolder holder, int position) {
        if (holder != null) {
            LanguageModel sdfv = languageModels.get(position);
                     salected_language = sdfv.getSub_title();
                      if(salected_language ==null || salected_language.equalsIgnoreCase("")){
                        salected_language ="English";
                       }

                     setSelected(salected_language);

            holder.showText();
        }
    }
    @Override
    public void onScrollStart(@NonNull LanguageAdapter.ViewHolder holder, int position) {
        holder.hideText();
    }
    @Override
    public void onScroll(float position, @NonNull LanguageAdapter.ViewHolder currentHolder, @NonNull LanguageAdapter.ViewHolder newHolder) {
        LanguageModel current = languageModels.get(cityPicker.getCurrentItem());
        int nextPosition = cityPicker.getCurrentItem() + (position > 0 ? -1 : 1);
        if (nextPosition >= 0 && nextPosition < cityPicker.getAdapter().getItemCount()) {
            LanguageModel next = languageModels.get(nextPosition);
        }
    }
    @Override
    public void onScrollEnd(@NonNull LanguageAdapter.ViewHolder holder, int position) {

    }
    public void setSelected(String slectedLanguage) {
        Locale srcLanguage = CommonUtils.getInstance().getLocale(slectedLanguage);
        CommonUtils.getInstance().setLocale(srcLanguage.toString(), getApplicationContext(), prefs);


    }
    @Override
    public void onBackPressed() {
        finish();

    }
}


