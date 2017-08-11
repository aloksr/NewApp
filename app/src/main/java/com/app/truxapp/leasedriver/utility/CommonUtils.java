package com.app.truxapp.leasedriver.utility;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.app.truxapp.leasedriver.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;


public class CommonUtils {
	public static boolean GpsStatus;
	private static String provider;
	private static Intent poke;
	private static String mIMEI;
	private static TelephonyManager mTelephonyManager;
	private static Locale myLocale;
	public Context mContext;
	public static String aws_key;
	public static String aws_password;

	//private  static  CommonUtils commonUtils;
	public static CommonUtils getInstance() {
//		if(commonUtils==null)
//			commonUtils=;
		return new CommonUtils();
	}

	public CommonUtils() {

	}
	//	public CommonUtils(Activity activity) {
//		// TODO Auto-generated constructor stub
//	}
//



	public boolean isLocationEnabled(Context context) {
		int locationMode = 0;
		String locationProviders;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			try {
				locationMode = Settings.Secure.getInt(
						context.getContentResolver(),
						Settings.Secure.LOCATION_MODE);

			} catch (SettingNotFoundException e) {
				e.printStackTrace();
			}

			return locationMode != Settings.Secure.LOCATION_MODE_OFF;

		} else {
			locationProviders = Settings.Secure.getString(
					context.getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			return !TextUtils.isEmpty(locationProviders);
		}

	}
	public Locale getLocale(String language) {
		StringTokenizer mStringTokenizer = new StringTokenizer(language, " ");
		language = mStringTokenizer.nextToken();
		Locale local = Locale.ENGLISH;
		if (language.contains("Hindi"))
			local = new Locale("hi");
		else if (language.contains("Bengali"))
			local = new Locale("bn");
		else if (language.contains("Kannada"))
			local = new Locale("kn");
		else if (language.contains("Malayalam"))
			local = new Locale("ml");
		else if (language.contains("Marathi"))
			local = new Locale("mr");
		else if (language.contains("Punjabi"))
			local = new Locale("pa");
		else if (language.contains("Tamil"))
			local = new Locale("ta");
		else if (language.contains("Telugu"))
			local = new Locale("te");
		else if (language.contains("Arabic"))
			local = new Locale("ar");
		return local;
	}
	public String getLanguage(String language) {

		String local = "English";
		if (language.contains("hi"))
			local = ("Hindi");
		else if (language.contains("bn"))
			local = ("Bengali");
		else if (language.contains("kn"))
			local = ("Kannada");
		else if (language.contains("ml"))
			local = ("Malayalam");
		else if (language.contains("mr"))
			local = ("Marathi");
		else if (language.contains("pa"))
			local = ("Punjabi");
		else if (language.contains("ta"))
			local = ("Tamil");
		else if (language.contains("te"))
			local = ("Telugu");
		else if (language.contains("ar"))
			local = ("Arabic");
		return local;
	}

	public void setLocaleEn(String lang, Context context) {
		mContext = context;
		myLocale = new Locale(lang);
		Locale.setDefault(myLocale);
		Configuration config = mContext.getResources().getConfiguration();
		config.locale = myLocale;
		mContext.getResources().updateConfiguration(config,
				mContext.getResources().getDisplayMetrics());
	}

	public void setLocale(String lang, Context context, AppPreference prefs) {
		prefs.setStringValueForTag(Constants.SELECT_LANGUAGE, lang);
		mContext = context;
		myLocale = new Locale(lang);
		Locale.setDefault(myLocale);
		Configuration config = mContext.getResources().getConfiguration();
		config.locale = myLocale;
		mContext.getResources().updateConfiguration(config,
				mContext.getResources().getDisplayMetrics());
	}


	public String getIMEI(Context mContext) {
		try {
			mTelephonyManager = (TelephonyManager) mContext
					.getSystemService(Context.TELEPHONY_SERVICE);
			mIMEI = mTelephonyManager.getDeviceId();
		} catch (Exception e1) {
			System.out.println("exception " + e1);
		}
		return mIMEI;
	}

	public static String s3UrGenerator(String url) throws AmazonClientException {
		String temp;
		temp = url.substring(url.lastIndexOf(".com/") + 5);
		System.out.println(temp);
		String bucket = temp.substring(0, temp.indexOf("/"));
		System.out.println(bucket);
		temp = temp.substring(temp.indexOf("/") + 1);
		System.out.println(temp);

		AWSCredentials credentials = new BasicAWSCredentials(
				CommonUtils.aws_key,
				CommonUtils.aws_password);
		ClientConfiguration clientConfig = new ClientConfiguration();
		clientConfig.setProtocol(Protocol.HTTP);
		AmazonS3 conn = new AmazonS3Client(credentials, clientConfig);
		conn.setEndpoint("s3-ap-southeast-1.amazonaws.com");
		GeneratePresignedUrlRequest request1 = new GeneratePresignedUrlRequest(
				bucket, temp);
		return conn.generatePresignedUrl(request1).toString();
	}
	public static long getNumberOfDay(String CurrDate,String lastDate) {
		long diffDays = 0;
		try {
			Date dateCurrDate;
			Date datelastDate;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US);
			dateCurrDate = df.parse(CurrDate);
			datelastDate= df.parse(lastDate);
			long diff = Math.abs(dateCurrDate.getTime() - datelastDate.getTime());
			diffDays = diff / (24 * 60 * 60 * 1000);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return diffDays;
	}

}
