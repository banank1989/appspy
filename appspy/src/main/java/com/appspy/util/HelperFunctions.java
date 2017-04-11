package com.appspy.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HelperFunctions {

	/*
	 * Function to send POST Request String URL - > URL on which request needs
	 * to be send String urlParmas -> POST Params(in case of NO Params, blank
	 * String NOT NULL)
	 */
	public HashMap<String, String> sendPostRequest(String url,
			String urlParameters, int timeout) throws Exception {

		HashMap<String, String> result = new HashMap<String, String>();
		URL obj = new URL(url);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// set Connection Params
		con.setRequestMethod("POST");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setDoOutput(true);

		// Timeouts
		con.setConnectTimeout(timeout);
		con.setReadTimeout(timeout);
		DataOutputStream wr = null;
		BufferedReader in = null;
		try {
			long startTime = System.currentTimeMillis();
			wr = new DataOutputStream(con.getOutputStream());
			if (!urlParameters.equals("")) {
				wr.writeBytes(urlParameters);
			}

			int responseCode = con.getResponseCode();
			
			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;
			result.put("responseCode", "" + responseCode);
			result.put("totalTime", "" + elapsedTime);

			/*in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			 String inputLine; StringBuffer response = new StringBuffer();
			 
			 while ((inputLine = in.readLine()) != null) {
			 response.append(inputLine); }
			 in.close();
			 System.out.println(response);
			 System.out.println(result);*/
			 

		} catch (SocketTimeoutException e) {
			result.put("responseCode", "0");
			result.put("totalTime", timeout + "");
			result.put("customException",
					"Socket TimeOut Excection with timeout as " + timeout);
			Logger.exceptionsLogMessages("Socket Timeout Exception while Connecting to URL -> "
					+ url);
		} catch (Exception e) {
			result.put("responseCode", "0");
			result.put("totalTime", timeout + "");
			result.put("customException",
					"Exception Occurs while sending Request : "
							+ e.getClass().getName());
			Logger.exceptionsLogMessages("Exception while Connecting to URL -> "
					+ url + "Exception : " + e.getMessage());
		} finally {
			// Close O/p, I/p Streams & Close the COnnection
			if (wr != null) {
				wr.flush();
				wr.close();
			}
			if (in != null) {
				in.close();
			}
			if (con != null) {
				con.disconnect();
			}
		}
		
		return result;
		/*
		 * O/P Reading, NOT Needed For Now, So commented BufferedReader in = new
		 */
	}

	/**
	 * Function to send JSON POst Request(Request with POST DATA as JSON)
	 * 
	 * @param url
	 * @param postJson
	 * @param timeout
	 * @return HashMap(With two keys , responseCode and time in request)
	 */
	public HashMap<String, String> sendJsonPostRequest(String url,
			String postJson, int timeout) {
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HashMap<String, String> result = new HashMap<String, String>();
		try {
			// Create request Object
			HttpPost request = new HttpPost(url);
			StringEntity params = new StringEntity(postJson);
			
			// Set timeout
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout).setConnectTimeout(timeout).build();
			request.setConfig(requestConfig);
			
			// Set Headers
			request.addHeader("content-type", "application/json");
			// Set Post Params
			request.setEntity(params);

			long startTime = System.currentTimeMillis();
			// Send Post Request
			HttpResponse response = httpClient.execute(request);
			

			long endTime = System.currentTimeMillis();
			long diff = endTime - startTime;

			int responseCode = response.getStatusLine().getStatusCode();

			result.put("responseCode", responseCode + "");
			result.put("totalTime", diff + "");
			 
			//Capture the Output
	        InputStream is = response.getEntity().getContent();
	        Reader reader = new InputStreamReader(is);
	        BufferedReader bufferedReader = new BufferedReader(reader);
	        StringBuilder builder = new StringBuilder();
	        while (true) {
	            try {
	                String line = bufferedReader.readLine();
	                if (line != null) {
	                    builder.append(line);
	                } else {
	                    break;
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
			return result;
		} catch (ClientProtocolException ex) {
			Logger.exceptionsLogMessages("Unable to Connnect to URL :" + url);
			result.put("responseCode", "0");
			result.put("totalTime", timeout + "");
			result.put("customException",
					"ClientProtocolException with timeout as " + timeout);
			Logger.exceptionsLogMessages("ClientProtocolException with timeout as " + timeout);
			return result;
		} catch (Exception ex) {
			result.put("responseCode", "0");
			result.put("totalTime", timeout + "");
			result.put("customException",
					"Exception Occurs while sending Request : "
							+ ex.getClass().getName());
			Logger.exceptionsLogMessages("Exception Occurs while sending Request : "
							+ ex.getClass().getName());
			return result;
		} finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				result.put("responseCode", "0");
				result.put("totalTime", timeout + "");
			}
		}
		
	}

	public HashMap<String, String> isPortInUse(String host, int port) {
		// Assume no connection is possible.
		int timeout = 5000; // 5 seconds
		boolean raiseAlert = true;
		HashMap<String, String> finalResult = new HashMap<String, String>();
		Socket socket = null;
		try {
			SocketAddress sockaddr = new InetSocketAddress(host, port);
			
			// Create your socket
			socket = new Socket();
			
			// Set Timeout
			socket.connect(sockaddr, timeout);
			// Close the Socket
			socket.close();
			
			raiseAlert = false;
			finalResult.put("status", "" + raiseAlert);
		} catch (SocketTimeoutException e) {
			raiseAlert = true;
			finalResult.put("status", "" + raiseAlert);
			finalResult.put("statusMessage",
					"Socket Exception during Connection : Host :" + host
							+ " Port : " + port);
		} catch (SocketException e) {
			raiseAlert = true;
			finalResult.put("status", "" + raiseAlert);
			finalResult.put("statusMessage",
					"Socket Exception during Connection : Host :" + host
							+ " Port : " + port);
		} catch (IOException e) {
			raiseAlert = true;
			finalResult.put("status", "" + raiseAlert);
			finalResult.put("statusMessage", "IO Exception("
					+ e.getClass().getName()
					+ ") during Closing Socket : Host :" + host + " Port : "
					+ port);
		} catch (Exception e) {
			raiseAlert = true;
			finalResult.put("status", "" + raiseAlert);
			finalResult.put("statusMessage", e.getClass().getName()
					+ " Occurs while connection : Host :" + host + " Port : "
					+ port);
		} finally {
			if(socket != null && !socket.isClosed()){
				try {
					socket.close();
				} catch (IOException e) {
					raiseAlert = true;
					finalResult.put("status", "" + raiseAlert);
					finalResult.put("statusMessage", e.getClass().getName()
							+ " Occurs while closing Socket : Host :" + host + " Port : "
							+ port);
					e.printStackTrace();
				}
			}
		}

		return finalResult;
	}

	/**
	 * Function to process the result from request of any server, check if
	 * actual alerts needs to be raised or not based on last n values
	 * 
	 * @param raiseAlert
	 * @param finalAlertsResult
	 * @param serviceKey
	 * @return
	 */

	public boolean processResults(int checkCount, boolean raiseAlert,
			HashMap<String, ArrayList<Boolean>> finalAlertsResult,
			String serviceKey) {

		List<Boolean> tempValues = new ArrayList<Boolean>();
		boolean alertVal = true;
		int size = 0;
		
		// Check if key Exists for Service or NOT
		if (finalAlertsResult.get(serviceKey) != null) {

			// If key exists, just add element to existing ArrayList
			finalAlertsResult.get(serviceKey).add(raiseAlert);
			size = finalAlertsResult.get(serviceKey).size();

			// After Adding, if count Exceeds 5, keep the latest 5 values and
			// update the List
			if (size >= checkCount) {

				tempValues = finalAlertsResult.get(serviceKey).subList(
						size - checkCount, size);
				finalAlertsResult.put(serviceKey, new ArrayList<Boolean>(
						tempValues));

				// Anding last 5 values, to check if all values are true. IF
				// Yes, then return true(RAISE ACTUAL ALERT) , else not
				for (boolean val : tempValues) {
					alertVal = val && alertVal;
					if (alertVal == false) {
						break;
					}
				}
			} else {
				// Size < 5, DO NOT RAISE ALERT
				alertVal = false;
			}
		} else {
			// first time, when even serviceKey not exists, do not raise Alerts
			alertVal = false;
			ArrayList<Boolean> temp = new ArrayList<Boolean>();
			temp.add(raiseAlert);
			finalAlertsResult.put(serviceKey, temp);
			
			// If check Count is 1
			if(raiseAlert == true && checkCount == 1){
				alertVal = true;
			}
		}

		return alertVal;
	}

	public void generateAlerts(String server, String serviceKey,
			String errorMessage) {
		try {
			DBAccess.createConnection();
			String sql = "INSERT into alertsData(server,service,errorMessage) VALUES('"
					+ server + "','" + serviceKey + "','" + errorMessage + "')";
			DBAccess.updateData(sql);
			try {
				DBAccess.con.close();
			} catch (SQLException e) {
				Logger.exceptionsLogMessages("Exception While Closing DB Connections");
				e.printStackTrace();
			}
		} catch (Exception e) {
			Logger.exceptionsLogMessages("Exception while Inserting in DB");
			e.printStackTrace();
		}

	}

}
