package com.app.truxapp.leasedriver.Model;

public class ResponseCode {
	public final static int LOGOUT = 0;
	public final static int LOGIN = 1;
	public final static int STOPJOURNEY = 0;
	public final static int STARTJOURNEY = 1;
	public final static int RESUMEDELIVERY = 0;
	public final static int STARTDELIVERY = 1;
	public final static int CURRENT_LOCATION = 2;
	public final static int START_JOURNEY_POINT = 3;
	public final static int STOP_JOURNEY_POINT = 4;
	public final static int RESUME_DELIVERY_POINT = 6;
	public static final int SCANING_AWB = 8;
	public final static int FIND_ALL_TRIP = 10;
	public final static int BOOKING_REPORT = 11;
	public final static int DRIVER_ROUTE = 12;
	public final static int LOGIN_PAGE_MESSAGE = 13;
	public final static int AWS_KEY = 14;
	public static final long FASTEST_INTERVAL = 1000 * 15;
	public static int NOTIFIY_TAG = 100;
	public static double ROUTE_DISTANCE = 0.250;

}
