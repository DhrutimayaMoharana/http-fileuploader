package com.https.fileuploader.constants;

public class Constants {

	public final static String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public final static String DATE_FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
	public final static String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public final static String DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public final static String DATE_FORMAT_DD_MM_YYYY_WITH_SLASH = "dd/MM/yyyy";
	
	
	
	public final static String GET_DEVICES = "/api/devices";
	public final static String GET_POSITIONS = "/api/positions";
	public final static String GET_EVENTS = "/api/events";
	
	public final static String ALARM_TYPE_EVENT = "alarm";
	
	public final static Integer SEND_COMMAND_AFTER_INTERVAL = 300000;
	public final static Integer MAX_RECALL_COUNT = 5;	
}
