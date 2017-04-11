package com.appspy.services;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.appspy.requestHandlers.RequestHandler;
import com.appspy.requestHandlers.RequestHandlerFactory;
import com.appspy.util.HelperFunctions;
import com.appspy.util.Logger;
import com.mysql.cj.api.log.Log;

public class PortMonitoring extends Thread{
	
private JSONObject servicesJsonObject;
	
	HelperFunctions helper;
	// Feed the JSON Data from File
	public PortMonitoring(JSONObject servicesJsonObject){
		this.servicesJsonObject = servicesJsonObject;
	}
	
	public void run(){
		helper = new HelperFunctions();
		while(true){
			runPortMonitoring();
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public void runPortMonitoring(){
			
			HashMap<String, String> alertData = new HashMap<String, String>();
			
			Set<String> serviceList = servicesJsonObject.keySet();
			String checkService = "";
			boolean raiseAlert = false;
			for(String serviceKey : serviceList){
				
				// Fetch Data from JSON
				JSONObject serviceData = (JSONObject)servicesJsonObject.get(serviceKey);
				String dataMonitorString = serviceData.get("port_monitoring").toString();
				String host = serviceData.get("host").toString();
				int port = Integer.parseInt(serviceData.get("port").toString());
				
				JSONParser parser = new JSONParser();
				JSONObject portMonitor = null;
				try {
					portMonitor = (JSONObject)parser.parse(dataMonitorString);
				} catch (ParseException e) {
					Logger.exceptionsLogMessages("Exception While Reading Json");
				}
				
				checkService = portMonitor.get("status").toString();
				
				// Do not check Service, if the disabled in config
				if(!checkService.equals("on")) continue;
				
				// Create Socket
				alertData = helper.isPortInUse(host, port);
				
				if(alertData.get("status").equals("true")){
		      	 	 raiseAlert = true;
		      	}else{
		      		 raiseAlert = false;
		      	}
				if(raiseAlert == true){
					 helper.generateAlerts(host,serviceKey,"Port not connected : Host : "+host+" Port : "+port);
					 Logger.genericLogMessages("Port not connected : Host : "+host+" Port : "+port);
					 
				 }else {
					 Logger.genericLogMessages("Success : Port Monitoring : "+serviceKey+" running Fine");
				 }
			}
		}

	
	


}
