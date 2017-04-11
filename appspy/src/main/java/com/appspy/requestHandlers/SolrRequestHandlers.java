package com.appspy.requestHandlers;

import java.util.HashMap;

import org.json.simple.JSONObject;

import com.appspy.util.HelperFunctions;
import com.appspy.util.Logger;

public class SolrRequestHandlers implements RequestHandler {
		
		public HelperFunctions helperClass;
		
		@Override
		public HashMap<String, String> monitorRequests(String serviceKey, JSONObject serviceData, JSONObject dataMonitorObject) {
			helperClass = new HelperFunctions();
			HashMap<String, String> result = new HashMap<String, String>();
			
			/// Fetch Data From JSON Object
			
			String url = dataMonitorObject .get("url").toString();
			String responseTimeCheck = dataMonitorObject .get("responseTimeCheck").toString();
			String maxResponseTimeString = dataMonitorObject .get("responseTime").toString();
			int timeout = Integer.parseInt(dataMonitorObject.get("timeout").toString());
			
			int maxResponseTime = 200; // DUMMY
			
			if(responseTimeCheck.equals("true")){
				maxResponseTime = Integer.parseInt(maxResponseTimeString);
			}
		
			String responseCodeCheck = dataMonitorObject.get("responseCodeCheck").toString();
			String expectedResponseCodeString = dataMonitorObject.get("responseCode").toString();
			int expectedRespCode = 200;
			
			if(responseCodeCheck.equals("true")){
				expectedRespCode = Integer.parseInt(expectedResponseCodeString);
			}
			
			try {
				// send Request
				result = helperClass.sendPostRequest(url,"",timeout);
				
				// Get Result Data
				int responseCode = Integer.parseInt(result.get("responseCode"));
				int responseTime = Integer.parseInt(result.get("totalTime"));
				String customException = result.get("customException");
				
				result.clear();
				
				// Check Response Code / Response Time, generate alert if needed
				if(customException != null){  // Exception Occurs while sending Request
					result.put("raiseAlert", "true");
					result.put("errorMessage", customException);
					Logger.debugLogMessages("Alert Generated For "+serviceKey+": "+customException);
				}
				else if(responseCode != expectedRespCode || responseTime > maxResponseTime){ //When request is sucesss, check response params 
					
					 if(responseCode != expectedRespCode && responseCodeCheck.equals("true")){
							result.put("raiseAlert", "true");
							result.put("errorMessage", "Response Code is NOT "+expectedRespCode+": Response Code Recieved : "+responseCode);
							Logger.debugLogMessages("Alert Generated For "+serviceKey+" for RESPONSE CODE ## Actual: "+responseCode+" Expected : "+expectedRespCode);
						}
					 else if(responseTime > maxResponseTime && responseTimeCheck.equals("true")){
						result.put("raiseAlert", "true");
						result.put("errorMessage", "Response NOT Recieved in Threshold Limit : Threshold ["+maxResponseTime+"ms] : Actual Response Time ["+responseTime+"ms]");
						Logger.debugLogMessages("Alert Generated For "+serviceKey+" FOR TIME ## Actual: "+responseTime+" Expected: "+maxResponseTime);
					}
					else{
						result.put("raiseAlert", "false");
						Logger.debugLogMessages("Nothing Checked, hence not raising alert");
					}
				}
				else{// Running Fine
					Logger.debugLogMessages("Alert NOT Generated For "+serviceKey+" CODE : "+responseCode+" TIME : "+responseTime);
					result.put("raiseAlert", "false");
				}
			} catch (Exception e) {
				//Raising alert if exception occurs
				Logger.exceptionsLogMessages("Exception Caused "+e.getMessage());
				result.put("raiseAlert", "true");
				result.put("errorMessage", "Exception Occured while Processing Data : "+e.getClass().getName());
				e.printStackTrace();
			}
			return result;

		}

}
