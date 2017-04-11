package com.appspy.requestHandlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.appspy.util.Logger;

public class MysqlRequestHandler implements RequestHandler {

	Connection con;
	HashMap<String, String> finalResult;
	
	@Override
	public HashMap<String, String> monitorRequests(String serviceKey, JSONObject serviceData, JSONObject dataMonitorObject) {
		
		
		try {
			String dbConnectionString = dataMonitorObject.get("dbconn").toString();
			String userName = dataMonitorObject.get("username").toString();
			String password = dataMonitorObject.get("password").toString();
			
			int timeout = Integer.parseInt(dataMonitorObject.get("timeout").toString());
			
			finalResult = new HashMap<String, String>();
			finalResult.put("raiseAlert", "false");
			
			// Create DB Connection
			boolean connectionSuccess = createDBConnection(dbConnectionString, userName, password, timeout);
			if(!connectionSuccess){
				return finalResult;
			}

			String sql = dataMonitorObject.get("sql").toString();
			
			// Runs the SQL Query
			long executionTime = selectData(sql, timeout);
			
			// Check the exection Time Check
			if(executionTime != -1){
				String responseTimeCheck = dataMonitorObject.get("responseTimeCheck").toString();
				int maxResponseTime = Integer.parseInt(dataMonitorObject.get("responseTime").toString());
				if(responseTimeCheck.equals("true") && executionTime > maxResponseTime){
					finalResult.put("raiseAlert", "true");
					finalResult.put("errorMessage", "Response NOT Recieved in Threshold Limit : Threshold ["+maxResponseTime+"ms] : Actual Response Time ["+executionTime+"ms]");
				}else{
					finalResult.put("raiseAlert", "false");
				}
			}
		} catch (Exception e1) {
			finalResult.put("raiseAlert", "true");
			finalResult.put("errorMessage", e1.getClass().getName()+" occurs while processing Service");
		}
		finally{
			if(con != null){
				try {
					con.close();
				} catch (SQLException e) {
					finalResult.put("raiseAlert", "true");
					finalResult.put("errorMessage", e.getClass().getName()+" occurs while closing DB Connection");
					e.printStackTrace();
				}
			}
		}
		
		return finalResult;
		
	}
	
	boolean createDBConnection(String dbConnectionString, String userName, String password, int timeout){
		
		boolean isSuccess = true;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			DriverManager.setLoginTimeout(timeout);
			con = DriverManager.getConnection(dbConnectionString, userName, password);
		} catch (SQLException e) {
			isSuccess = false;
			finalResult.put("raiseAlert","true");
			finalResult.put("errorMessage",e.getClass().getName()+" Exception occurs while connecting to Database : "+dbConnectionString);
			Logger.exceptionsLogMessages(e.getClass().getName()+" Exception occurs while connecting to Database : "+dbConnectionString);
			e.printStackTrace();
		}   
		catch (Exception e) {
			isSuccess = false;
			finalResult.put("raiseAlert","true");
			finalResult.put("errorMessage",e.getClass().getName()+" Exception occurs while connecting to Database : "+dbConnectionString);
			Logger.exceptionsLogMessages(e.getClass().getName()+" Exception occurs while connecting to Database : "+dbConnectionString);
			e.printStackTrace();
		}   
		return isSuccess;
	}
	
	long selectData(String q, int queryTimeout) {
		boolean isSuccess = true;
		long diff = 0;
		try {
			Statement st = con.createStatement();
			st.setQueryTimeout(queryTimeout);
			long startTime = System.currentTimeMillis();
			st.executeQuery(q);
			long endTime = System.currentTimeMillis();
			diff = endTime - startTime;
		} 
		catch(SQLSyntaxErrorException e){
			
			finalResult.put("raiseAlert","true");
			finalResult.put("errorMessage",e.getClass().getName()+" Exception as Invalid SQL Provided");
			Logger.exceptionsLogMessages(e.getClass().getName()+" Exception as Invalid SQL Provided");
			isSuccess = false;
		}
		catch(SQLTimeoutException e){
			
			finalResult.put("raiseAlert","true");
			finalResult.put("errorMessage",e.getClass().getName()+" Exception as SQL Query Timeout : Expected "+queryTimeout+" seconds");
			Logger.exceptionsLogMessages(e.getClass().getName()+" Exception as SQL Query Timeout : Expected "+queryTimeout+" seconds");
			isSuccess = false;
		}
		catch (SQLException e) {
			
			finalResult.put("raiseAlert","true");
			finalResult.put("errorMessage",e.getClass().getName()+" Exception while Running SQL");
			Logger.exceptionsLogMessages(e.getClass().getName()+" Exception while Running SQL");
			isSuccess = false;
		}
		catch(Exception e){
			e.printStackTrace();
			finalResult.put("raiseAlert","true");
			finalResult.put("errorMessage",e.getClass().getName()+" Exception while Running SQL");
			Logger.exceptionsLogMessages(e.getClass().getName()+" Exception while Running SQL");
			isSuccess = false;
		}
		
		if(isSuccess){
			return diff;
		}
		
		return -1;
	}	

	
}
