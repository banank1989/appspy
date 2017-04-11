package com.appspy.requestHandlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONObject;

import redis.clients.jedis.exceptions.JedisConnectionException;

import com.appspy.util.HelperFunctions;
import com.appspy.util.Logger;
import com.appspy.util.RedisLibrary;

public class RedisRequestHandler implements RequestHandler {
		
		public HelperFunctions helperClass;
		
		@Override
		public HashMap<String, String> monitorRequests(String serviceKey, JSONObject serviceData, JSONObject dataMonitor) {
			
			// perform intersection
			HashMap<String, String> result= performIntersectionTest(serviceKey, serviceData);
			
			HashMap<String, String> finalResult = new HashMap<String, String>();
			
			int maxResponseTime =Integer.parseInt(dataMonitor.get("responseTime").toString());
			String maxResponseTimeCheck = dataMonitor.get("responseTimeCheck").toString();
			
			// Generate Alert if needed
			
			// If status is failed, meaning Intersection is not correct
			if(result.get("status").equals("failed")){
				finalResult.put("raiseAlert","true");
				finalResult.put("errorMessage",result.get("statusReason"));
			}
			// Meaning response time is greater than max response time
			else if(maxResponseTimeCheck.equals("true")){
				long responseTime = Integer.parseInt(result.get("responseTime"));
				if(responseTime > maxResponseTime){
					finalResult.put("raiseAlert", "true");
					finalResult.put("errorMessage", "Alert Generated for "+serviceKey+" as operation not in expected Time ## Actual:"+responseTime+"ms Max Response Time: "+maxResponseTime+"ms");
				}else{
					finalResult.put("raiseAlert", "false");
				}
			}
			else{
				finalResult.put("raiseAlert", "false");
				Logger.debugLogMessages("Nothing Checked, hence not raising alert");
			}
			return finalResult;
		}
		
		
		
		public HashMap<String, String> performIntersectionTest(String serviceKey, JSONObject serviceData){
			// Fetch Data for Service
			String host = serviceData.get("host").toString();
			int port = Integer.parseInt(serviceData.get("port").toString());
			
			HashMap<String, String> result = new HashMap<String, String>();
			
			// Generate Test Data
		//	boolean statusTestDataGeneration = generateTestData(host, port);
			boolean statusTestDataGeneration = true;
			if(statusTestDataGeneration == false){
				result.put("status","failed");
				result.put("statusReason", "Jedis Connection Exception with Host :"+host+" and Port: "+port);
				return result;
			}
		
			// Perform Intersection
			RedisLibrary redisLib = RedisLibrary.getInstance();
			long startTime = System.currentTimeMillis();
			try {
				redisLib.intersectionOfSets(host, port, "set1ForRedisVerify","set2ForRedisVerify");
			} catch (JedisConnectionException e) {
				result.put("status","failed");
				result.put("statusReason", "Jedis Connection Exception with Host :"+host+" and Port: "+port);
				return result;
			}
			long endTime = System.currentTimeMillis();
			long diff = endTime - startTime;
			
		
			// Clean Test Data
		//	boolean clearDataResult = clearTestData(host, port);
			boolean clearDataResult = true;
			if(clearDataResult == false){
				result.put("status","failed");
				result.put("statusReason", "Jedis Connection Exception with Host :"+host+" and Port: "+port);
				return result;				
			}
			
			// Close Connection
			try{
				redisLib.closeConnection();
			} catch(Exception e){
				result.put("status","failed");
				result.put("statusReason", "Exception in closing Connection to Redis");
				return result;
			}
			
			result.put("responseTime",""+diff);
			result.put("status", "success");
			return result;
		}
		
		/**
		 * Fucntion to generate Test Data for Intersection
		 * @param host
		 * @param port
		 */
		public boolean generateTestData(String host, int port){
			
			try {
				RedisLibrary redisLib = RedisLibrary.getInstance();
				ArrayList<String> s = new ArrayList<String>();
				Random rand = new Random();
				
				for(int i=0;i<100;i++){
					s.add(rand.nextInt(100000)+"");
				}
			
				redisLib.addMembersToSet("set1ForRedisVerify",s , host, port);
				s.clear();
				
				for(int i=0;i<100;i++){
					s.add(rand.nextInt(100000)+"");
				}
				
				redisLib.addMembersToSet("set2ForRedisVerify",s , host, port);
				return true;
			} catch (JedisConnectionException e) {
				return false;
			}
			
		}
		
		/**
		 * Function to clear the generated Test Data
		 * @param host
		 * @param port
		 */
		public boolean clearTestData(String host, int port){
			try {
				RedisLibrary redisLib = RedisLibrary.getInstance();
				redisLib.deleteKeys(host, port, "set1ForRedisVerify", "set2ForRedisVerify");
				return true;
			} catch (JedisConnectionException e) {
				return false;
			}
		}

}
