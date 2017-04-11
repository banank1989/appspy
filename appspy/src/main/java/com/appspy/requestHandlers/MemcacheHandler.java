package com.appspy.requestHandlers;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.MemcachedClient;

import org.json.simple.JSONObject;

import com.appspy.util.HelperFunctions;
import com.appspy.util.Logger;

public class MemcacheHandler implements RequestHandler {
	
	HelperFunctions helperClass;
	public HashMap<String, String> monitorRequests(String serviceKey, JSONObject serviceData, JSONObject dataMonitor){
		
		HashMap<String, String> result = null;
		MemcachedClient mcc = null;
		try{
			result = new HashMap<String, String>();
			String host = serviceData.get("host").toString();
			int port = Integer.parseInt(serviceData.get("port").toString());
			int timeout = Integer.parseInt(dataMonitor.get("timeout").toString());
	         String responseTimeCheck = dataMonitor .get("responseTimeCheck").toString();
			 String maxResponseTimeString = dataMonitor .get("responseTime").toString();
			 
			 int maxResponseTime = 0;
			 if(responseTimeCheck.equals("true")){
				 maxResponseTime = Integer.parseInt(maxResponseTimeString);
			 }
			
			mcc = new MemcachedClient(new InetSocketAddress(host, port));
			
			ArrayList<Integer> data = new ArrayList<Integer>();
	         for(int i=0;i<10;i++){
	        	 data.add(i* 10);
	         }
	         long startTime = System.currentTimeMillis();
	         Future fo = mcc.set("demoSpearKey", 900, data);
	         Object myObj = null;
	         try{
	        	 myObj = fo.get(timeout, TimeUnit.MILLISECONDS);
	         } catch(Exception e){
	        	 result.put("raiseAlert", "true");
		         result.put("errorMessage", "Timeout occurs in Set Operation : Timeout :"+timeout+" sec.");
		         return result;
	         }
	         
	         Future<Object> f = mcc.asyncGet("demoSpearKey");
	         try{
	        	 myObj = f.get(timeout, TimeUnit.MILLISECONDS);
	         } catch(Exception e){
	        	result.put("raiseAlert", "true");
	           	result.put("errorMessage", "Timeout occurs in Get Operation : Timeout :"+timeout+" sec.");
	           	return result;
	         }
	         
	         Future fd = mcc.delete("demoSpearKey");
	         try{
	        	 myObj = fd.get(timeout, TimeUnit.MILLISECONDS);
	         } catch(Exception e){
	        	result.put("raiseAlert", "true");
	           	result.put("errorMessage", "Timeout occurs in Get Operation : Timeout :"+timeout+" sec.");
	           	return result;
	         }
	         
	         
	         long endTime = System.currentTimeMillis();
	         long operationTime = endTime - startTime;
	         
	         if(responseTimeCheck.equals("true") && operationTime > maxResponseTime){
	        	 result.put("raiseAlert", "true");
	        	 result.put("errorMessage", "Operation not performed in threshold limit. Actual : ["+operationTime+"ms] , Threshold : ["+maxResponseTime+"ms]");
	         }else{
	        	 result.put("raiseAlert", "false");
	         }
	         return result;
	         
		}catch(Exception e){
			e.printStackTrace();
			result.put("raiseAlert", "true");
       	 	result.put("errorMessage", e.getClass().getName()+" Occurs while processing service");
       	 	
       	 	return result;
		}
		finally{
			if(mcc != null){
				mcc.shutdown();
			}
		}
	}
}
