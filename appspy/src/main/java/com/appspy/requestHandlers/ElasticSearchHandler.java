package com.appspy.requestHandlers;

import java.util.HashMap;

import org.json.simple.JSONObject;

import com.appspy.util.HelperFunctions;
import com.appspy.util.Logger;

public class ElasticSearchHandler implements RequestHandler {
	
	HelperFunctions helperClass;
	public HashMap<String, String> monitorRequests(String serviceKey, JSONObject serviceData, JSONObject dataMonitor){
		
		HashMap<String, String> result;
		
	  	String url = dataMonitor.get("url").toString();
		//String url = "http://10.10.16.91:9200/trafficdata_pageviews_new/_search";
	    int timeout = Integer.parseInt(dataMonitor.get("timeout").toString());
	    
	    String postJson = dataMonitor.get("query").toString();
	    
	    // Fetch Params for Verifying Elastic Search Result
	    int maxResponseTime = 200; // DUMMY
	    
	    String responseTimeCheck = dataMonitor .get("responseTimeCheck").toString();
		String maxResponseTimeString = dataMonitor .get("responseTime").toString();
		
		if(responseTimeCheck.equals("true")){
			maxResponseTime = Integer.parseInt(maxResponseTimeString);
		}
		
		String responseCodeCheck = dataMonitor.get("responseCodeCheck").toString();
		String expectedResponseCodeString = dataMonitor.get("responseCode").toString();
		int expectedRespCode = 200;
			
		if(responseCodeCheck.equals("true")){
			expectedRespCode = Integer.parseInt(expectedResponseCodeString);
		}
		
		// Send Elastic Search Request
		helperClass = new HelperFunctions();
	    result = helperClass.sendJsonPostRequest(url, postJson, timeout);
	    
		// Get Result Data
		int responseCode = Integer.parseInt(result.get("responseCode"));
		int responseTime = Integer.parseInt(result.get("totalTime"));

		String customException = result.get("customException");
		
		result.clear();
		
		if(customException != null){  // Exception Occurs while sending Request
			result.put("raiseAlert", "true");
			result.put("errorMessage", customException);
			Logger.debugLogMessages("Alert Generated For "+serviceKey+": "+customException);
		}
		else if(responseCode != expectedRespCode || responseTime > maxResponseTime){
			if(responseTime > maxResponseTime && responseTimeCheck.equals("true")){
				result.put("raiseAlert", "true");
				result.put("errorMessage", "Response NOT Recieved in Threshold Limit : Threshold ["+maxResponseTime+"ms] : Actual Response Time ["+responseTime+"ms]");
				Logger.debugLogMessages("Alert Generated For "+serviceKey+" FOR TIME ## Actual: "+responseTime+" Expected: "+maxResponseTime);
			}
			else if(responseCode != expectedRespCode && responseCodeCheck.equals("true")){
				result.put("raiseAlert", "true");
				result.put("errorMessage", "Response Code is NOT "+expectedRespCode+": Response Code Recieved : "+responseCode);
				Logger.debugLogMessages("Alert Generated For "+serviceKey+" for RESPONSE CODE ## Actual: "+responseCode+" Expected : "+expectedRespCode);
			} else{
				result.put("raiseAlert", "false");
				Logger.debugLogMessages("Nothing Checked, hence not raising alert");
			}
		}
		else{
			Logger.debugLogMessages("Alert NOT Generated For "+serviceKey+" CODE : "+responseCode+" TIME : "+responseTime);
			result.put("raiseAlert", "false");
		}

	    return result;
	}
}
