package com.app.truxapp.leasedriver;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.app.truxapp.leasedriver.utility.ErrorView;

import io.fabric.sdk.android.Fabric;

public class MainControllerApplication extends Application {
	private ErrorView mErrorView;
	private static MainControllerApplication mInstance;
	public String aws_key;
	public String aws_password;
	public MainControllerApplication() {
	}
	@Override
	public void onCreate() {
		super.onCreate();
		Fabric.with(this, new Crashlytics());
		mInstance = this;
	}
	public static MainControllerApplication getInstance() {
		return mInstance;
	}
	public void showError(String errorText, int backgroundResource) {
		if (mErrorView == null) {
			mErrorView = new ErrorView(getApplicationContext());
		}
		mErrorView.show(errorText, backgroundResource);
	}
	public void showError(int errorText, int backgroundResource) {
		if (mErrorView == null) {
			mErrorView = new ErrorView(getApplicationContext());
		}
		mErrorView.show(errorText, backgroundResource);
	}

}
