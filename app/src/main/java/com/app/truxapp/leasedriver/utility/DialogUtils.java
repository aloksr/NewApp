package com.app.truxapp.leasedriver.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.app.truxapp.leasedriver.R;
import com.app.truxapp.leasedriver.interfaces.OnProgressCancelListener;

public class DialogUtils {
	private static Activity mActivity;
	static float density = 1;
	private static ProgressDialog mProgressDialog;
	private static Dialog mDialog;
	private static OnProgressCancelListener progressCancelListener;
	private static AlertDialog  locationAlertDialog;

	public static void showCustomProgressDialog(Context context, String title,
                                                OnProgressCancelListener progressCancelListener) {
		if (mDialog != null && mDialog.isShowing())
			return;
		DialogUtils.progressCancelListener = progressCancelListener;
		mDialog = new Dialog(context, R.style.MyDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		mDialog.setContentView(R.layout.progress_bar_cancel);
		ImageView imageView = (ImageView) mDialog.findViewById(R.id.ivProgressBar);
		if (!TextUtils.isEmpty(title)) {
			TextView tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
			tvTitle.setText(title);
		}
		if(!((Activity)context).isDestroyed()) {
			Glide.with(context)
					.load(R.drawable.loading_partner).asGif()
					.fitCenter()
					.into(imageView);

			mDialog.show();
		}
	}
///////////////////////////////////////
	public static void removeCustomProgressDialog() {
		try {
			DialogUtils.progressCancelListener = null;
			if (mDialog != null) {
				// if (mProgressDialog.isShowing()) {
				mDialog.dismiss();
				mDialog = null;
				// }
			}
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();

		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
