package com.app.truxapp.leasedriver.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressDialog {
	private static ProgressDialog progressDialog;

	public static void showProgress(Context context) {
		dismissProgress();
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("Please wait..");
			progressDialog.setCancelable(false);
			if (!((Activity) context).isFinishing())
				try {
					progressDialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}

	}

	public static void dismissProgress() {
		try {
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
