package com.app.truxapp.leasedriver.services;


public class UrlService {

	//public final static String BASE_URL = "http://180.151.15.77:8080/";
	public final static String BASE_URL = "http://crm.truxapp.com/";

	public static final String AWS_KEY="truxapiv2/leasedvicecontroller/getAWSCredentials?agentId=";
	public final static String PUNCH_IN_REPORT = "truxapiv2/leasedvicecontroller/getLVTDriverPunchInsReport?";
	public final static String FIND_ALL_TRIP = "truxapiv2/leasedvicecontroller/getBookingByClientIdVehicleIdDate";
	public final static String BOOKING_REPORT = "truxapiv2/app/callByClientRequestId?";
//	public static final String CHECK_LOGIN="truxapiv2/driver/bydeviceUUID/";
	public static String CHECK_VEHICLE_EXIST ="truxapiv2/leasedvicecontroller/checkIsVehicleExist";
    public static String CHECK_IS_DRIVER_FREE_WHILE_PUNCH_IN ="truxapiv2/leasedvicecontroller/driver/punch/checkIsDriverFreeWhilePunchIn2/";
    public static String POST_ADDENDANCE_LOGIN_NEW_URL="truxapiv2/leasedvicecontroller/postAttendanceLoginNew";;
	public static String POST_LOGOUT_URL="truxapiv2/leasedvicecontroller/postAttendanceLoginNew";
	public static String START_JOURNEY_URL="truxapiv2/driver/journey/savePilotJourneyDetails";
	public static String STOP_JOURNEY_URL="truxapiv2/driver/journey/savePilotJourneyDetails";
	public static String SCAN_AWB="truxapiv2/clientcustom/getawbdata/";
	public static String ADD_RESUME_URL="truxapiv2/leasedvicecontroller/insertBookingStopsNew";
	public static String AVAILABLE_URL="truxapiv2/leasedvicecontroller/getOrderDeatils";
}
