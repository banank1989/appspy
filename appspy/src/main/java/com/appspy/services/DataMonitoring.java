package com.appspy.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.appspy.requestHandlers.ApiRequestHandler;
import com.appspy.requestHandlers.ElasticSearchHandler;
import com.appspy.requestHandlers.RedisRequestHandler;
import com.appspy.requestHandlers.RequestHandler;
import com.appspy.requestHandlers.RequestHandlerFactory;
import com.appspy.requestHandlers.SolrRequestHandlers;
import com.appspy.util.HelperFunctions;
import com.appspy.util.Logger;

public class DataMonitoring extends Thread{
	
	
	public HelperFunctions helperClass;
	public SolrRequestHandlers solrRequestHandler;
	public ApiRequestHandler apiRequestHandler;
	public RedisRequestHandler redisRequestHandler;
	public ElasticSearchHandler elasticSearchHandler;
	HashMap<String,ArrayList<Boolean>> finalAlertsResult;
	private JSONObject servicesJsonObject;
	
	// Feed the JSON Data from File
	public DataMonitoring(JSONObject servicesJsonObject){
		this.servicesJsonObject = servicesJsonObject;
	}
	
	public void run(){
		
		// Instantiate Variable
		helperClass = new HelperFunctions();
		
		finalAlertsResult = new HashMap<String, ArrayList<Boolean>>();
		solrRequestHandler = new SolrRequestHandlers();
		apiRequestHandler = new ApiRequestHandler();
		redisRequestHandler = new RedisRequestHandler();
		elasticSearchHandler = new ElasticSearchHandler();
				
		// Executing Main Thread
		while(true){
			try {
				runServicesMonitoring(servicesJsonObject);
				System.out.println("#######################################################");
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void runServicesMonitoring(JSONObject servicesJsonObject) {	
		
		HashMap<String, String> alertData = new HashMap<String, String>();
		
		Set<String> serviceList = servicesJsonObject.keySet();
		
		String serviceType = "";
		String status = "";
		boolean raiseAlert = true;
		String host = "";
		for(String serviceKey : serviceList){
			
			JSONObject serviceData = (JSONObject)servicesJsonObject.get(serviceKey);
			serviceType = serviceData.get("serviceType").toString();
			host = serviceData.get("host").toString();
			String dataMonitorString = serviceData.get("data_monitoring").toString();
			JSONParser parser = new JSONParser();
			
			// Fetch Data from JSON File
			JSONObject dataMonitor = null;
			try {
				dataMonitor = (JSONObject)parser.parse(dataMonitorString);
			} catch (ParseException e) {
				Logger.exceptionsLogMessages("Invalid JSON File : Exception in Parsing Data Monitoring Object");
				System.exit(0);
			}
			
			try {
				status = dataMonitor.get("status").toString();
				if(!status.equals("on")) continue;
			} catch (Exception e1) {
				Logger.exceptionsLogMessages("Invalid JSON File : Exception in fetching Status Column");
				System.exit(0);
			}
			
			Logger.genericLogMessages("Checking : "+serviceType+" with Key as : "+serviceKey);
			
			// Get Request Handler
			RequestHandler requestHandler = RequestHandlerFactory.getRequestHandler(serviceType);
			
			// Call the Request Handler monitor method, fail service in case of exception
			try {
				if(requestHandler != null){
					alertData = requestHandler.monitorRequests(serviceKey, serviceData, dataMonitor);
				}else{
					Logger.genericLogMessages("Invalid Service Name");
					continue;
				}
			} catch (Exception e) {
				alertData.put("raiseAlert","true");
				alertData.put("errorMessage",e.getClass().getName()+" While Procressing Service");
			}
			
			
			// Check the result to set the flag for raiseALert
			if(alertData.get("raiseAlert").equals("true")){
	      	 	  raiseAlert = true;
	      	  }else{
	      		  raiseAlert = false;
	      	  }
			
			// Check whether to actually raise based on exceptionCheckCount 
			int reAttempts = Integer.parseInt(dataMonitor.get("exceptionCheckCount").toString()); 
	      	raiseAlert = helperClass.processResults(reAttempts, raiseAlert, finalAlertsResult, serviceKey);
	      	
	      	if(raiseAlert == true){
				 helperClass.generateAlerts(host,serviceKey,alertData.get("errorMessage"));
				 Logger.genericLogMessages(serviceKey+" NOT Running Fine == "+serviceKey);
				 Logger.genericLogMessages("ERROR MSG="+alertData.get("errorMessage"));
				 
			 }else {
				 Logger.genericLogMessages(serviceKey+" running Fine");
			 }
		}
	}
}
