package com.app.truxapp.leasedriver.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.truxapp.leasedriver.R;

import java.util.Timer;
import java.util.TimerTask;

public class ErrorView extends View {

	private final Context mContext;
	private final TextView mPopupLayout;
	private final ViewGroup mParentView;
	private int TIMER_DURATION = 3000;
	private Timer mTimer;

	@SuppressLint("InflateParams")
	public ErrorView(final Context context) {
		super(context);

		mContext = context;

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
						| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				PixelFormat.TRANSLUCENT);

		final WindowManager mWinManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopupLayout = (TextView) inflater.inflate(R.layout.error, null);
		mPopupLayout.setVisibility(GONE);

		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.WRAP_CONTENT;

		// Default variant
		// params.windowAnimations = android.R.style.Animation_Translucent;

		params.y = -context.getResources().getDisplayMetrics().heightPixels / 2;
		mParentView = new FrameLayout(mContext);

		mWinManager.addView(mParentView, params);

		mParentView.addView(mPopupLayout);
		mPopupLayout.setVisibility(GONE);

	}

	class MYTimerTask extends TimerTask {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(0);
		}

	}

	Handler mHandler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			hide();
			return false;
		}
	});

	/**
	 * Shows view
	 */
	public void show(String textId, int background) {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		mPopupLayout.setText(textId);
		mPopupLayout.setBackgroundResource(background);

		mTimer = new Timer();
		mTimer.schedule(new MYTimerTask(), TIMER_DURATION);

		final Animation in = AnimationUtils.loadAnimation(this.mContext,
				R.anim.push_top_in);

		mPopupLayout.setVisibility(VISIBLE);
		mPopupLayout.startAnimation(in);
	}

	/**
	 * Shows view
	 */
	public void show(int textId, int background) {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		mPopupLayout.setText(textId);
		mPopupLayout.setBackgroundResource(background);

		mTimer = new Timer();
		mTimer.schedule(new MYTimerTask(), TIMER_DURATION);

		final Animation in = AnimationUtils.loadAnimation(this.mContext,
				R.anim.push_top_in);

		mPopupLayout.setVisibility(VISIBLE);
		mPopupLayout.startAnimation(in);
	}

	/**
	 * Hides view
	 */
	public void hide() {
		final Animation in = AnimationUtils.loadAnimation(this.mContext,
				R.anim.push_top_out);

		mPopupLayout.startAnimation(in);
		in.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mPopupLayout.setVisibility(GONE);
			}
		});
	}
}