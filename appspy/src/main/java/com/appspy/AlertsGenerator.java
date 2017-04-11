package com.appspy;

import java.io.FileOutputStream;
import java.io.PrintStream;

import org.json.simple.JSONObject;

import com.appspy.services.DataMonitoring;
import com.appspy.services.PortMonitoring;
import com.appspy.util.Constants;

public class AlertsGenerator  {


	public static void main(String s[]) {
		 
		Constants constants = new Constants();
		// Reading JSON File
		JSONObject servicesJsonObject = constants.populateServicesData();
		
		if(servicesJsonObject == null){
			System.out.println("File Not present or Incorrent config file");
			System.exit(0);
		}
		
		// Runs the Data Monitor Thread
		DataMonitoring dataMonitoringService = new DataMonitoring(servicesJsonObject);
		dataMonitoringService.start();
		
		PortMonitoring portMonitoringService = new PortMonitoring(servicesJsonObject);
		portMonitoringService.start();
	}
		

}
