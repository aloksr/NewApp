package com.app.truxapp.leasedriver.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.activity.MainActivity;
import com.app.truxapp.leasedriver.adapters.ChangeLanguageAdapter;
import com.app.truxapp.leasedriver.utility.CommonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
public class FragmentChengeLanguage extends FragmentBase{
    private ChangeLanguageAdapter adapterChangeLanguage;
    public static FragmentChengeLanguage newInstance() {
        FragmentChengeLanguage fragment = new FragmentChengeLanguage();
        Bundle bundle=new Bundle();
       // bundle.putParcelable("login_data",loginData);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_language,container,false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).mAddTitle(getString(R.string.menu_language));
            initUI(view);
    }
    private void initUI(View view) {
        ListView changeLanguageLV = (ListView) view.findViewById(R.id.changeLanguageLV);
        List<String> arrayList = Arrays.asList(getResources().getStringArray(R.array.languages));
        adapterChangeLanguage = new ChangeLanguageAdapter(getActivity().getApplicationContext(), 0, arrayList, prefs);
        changeLanguageLV.setAdapter(adapterChangeLanguage);
        changeLanguageLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        changeLanguageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showConfirmDialog((String) parent.getItemAtPosition(position));

            }
        });
    }
    private void showConfirmDialog(final String selectedItem) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.menu_language);
        alertDialog.setMessage(R.string.are_you_sure_you_want_to_change_language);
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                setSelected(selectedItem);
                Intent intent = new Intent(getActivity(),MainActivity.class);
                getActivity().startActivity(intent);

            }
        });
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public void setSelected(String slectedLanguage) {
        Locale srcLanguage = CommonUtils.getInstance().getLocale(slectedLanguage);
        CommonUtils.getInstance().setLocale(srcLanguage.toString(), getActivity().getApplicationContext(), prefs);
        adapterChangeLanguage.notifyDataSetChanged();

    }
}
