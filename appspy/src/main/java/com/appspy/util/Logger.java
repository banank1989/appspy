package com.appspy.util;

import java.util.Date;

public class Logger {
	
	// Only needed while Debugging, not needed in Prod Mode
	public static void debugLogMessages(String message){
		if(Constants.ISDEBUGMODE){
			Date currentDate = new Date();
			System.out.println(currentDate.toString()+" : "+message);
		}
	}
	
	// Needed in Prod Mode Also
	public static void genericLogMessages(String message){
		Date currentDate = new Date();
		System.out.println(currentDate.toString()+" : "+message);
	}
	
	// Only for Messages in case of Exception Occurs in Code
	public static void exceptionsLogMessages(String message){
		if(Constants.ISDEBUGMODE){
			Date currentDate = new Date();
			System.out.println(currentDate.toString()+" : "+message);
		}
	}
}
