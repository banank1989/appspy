package com.appspy.requestHandlers;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.appspy.util.Logger;
import com.appspy.util.RabbitMQProducer;

public class RabbitMQServerHandler implements RequestHandler {

	RabbitMQProducer producer;
	HashMap<String, String> finalResult;

	@Override
	public HashMap<String, String> monitorRequests(String serviceKey,
			JSONObject serviceData, JSONObject dataMonitorObject) {

		finalResult = new HashMap<String, String>();

		// Fetch Data from JSON
		String responseTimeCheck = dataMonitorObject.get("responseTimeCheck")
				.toString();
		String maxResponseTimeString = dataMonitorObject.get("responseTime")
				.toString();
		int timeout = Integer.parseInt(dataMonitorObject.get("timeout")
				.toString());
		int maxResponseTime = 0;

		if (responseTimeCheck.equals("true")) {
			maxResponseTime = Integer.parseInt(maxResponseTimeString);
		}

		String hostName = serviceData.get("host").toString();
		int port = Integer.parseInt(serviceData.get("port").toString());
		String queueName = dataMonitorObject.get("queueName").toString();

		// Initialize finalReslt
		finalResult.put("raiseAlert", "true");
		finalResult.put("errorMessage", "To be computed...");
		
		long startTime = System.currentTimeMillis();
		
		// Create Queue
		boolean isQueueCreated = createQueue(queueName, hostName, port, timeout);

		if (!isQueueCreated) {
			return finalResult;
		}

		// add data to Queue
		addDataToQueue(queueName, hostName, port, timeout);

		long endTime = System.currentTimeMillis();
		long responseTime = endTime - startTime;

		// Check Response Time(If added)
		if (responseTime > maxResponseTime
				&& finalResult.get("raiseAlert").equals("false")) { // When
																	// request
																	// is
																	// sucesss,
																	// check
																	// response
																	// params

			if (responseTime > maxResponseTime
					&& responseTimeCheck.equals("true")) {
				finalResult.put("raiseAlert", "true");
				finalResult.put("errorMessage",
						"Response NOT Recieved in Threshold Limit : Threshold ["
								+ maxResponseTime
								+ "ms] : Actual Response Time [" + responseTime
								+ "ms]");
				Logger.debugLogMessages("Alert Generated For " + serviceKey
						+ " FOR TIME ## Actual: " + responseTime
						+ " Expected: " + maxResponseTime);
			} else {
				finalResult.put("raiseAlert", "false");
				Logger.debugLogMessages("Nothing Checked, hence not raising alert");
			}
		} else {// Running Fine
			finalResult.put("raiseAlert", "false");
		}

		return finalResult;
	}

	/**
	 * Create Rabbit MQ Queue & set result if any exception occurs
	 * 
	 * @param queueName
	 * @param hostName
	 * @param port
	 * @param timeout
	 */
	boolean createQueue(String queueName, String hostName, int port, int timeout) {

		boolean isSuccess = true;
		try {
			producer = new RabbitMQProducer(queueName, hostName, port, timeout);
		} // Handler Exceptions
		catch (ConnectException e) {
			isSuccess = false;
			finalResult.put("raiseAlert", "true");
			finalResult.put("errorMessage",
					"ConnectException Occurs while Creating Queue("
							+ e.getClass().getName() + "): Host:" + hostName
							+ " Port:" + port + " Timeout :" + timeout);
		} catch (SocketTimeoutException e) {
			isSuccess = false;
			finalResult.put("raiseAlert", "true");
			finalResult.put("errorMessage",
					"SocketTimeoutException Occurs while Creating Queue("
							+ e.getClass().getName() + "): Host:" + hostName
							+ " Port:" + port + " Timeout :" + timeout);
		} catch (IOException e) {
			isSuccess = false;
			finalResult.put("raiseAlert", "true");
			finalResult.put("errorMessage",
					"IO Exception Occurs while Creating Queue("
							+ e.getClass().getName() + "): Host:" + hostName
							+ " Port:" + port + " Timeout :" + timeout);
		} catch (Exception e) {
			isSuccess = false;
			finalResult.put("raiseAlert", "true");
			finalResult.put("errorMessage",
					"Exception Occurs while Creating Queue("
							+ e.getClass().getName() + "): Host:" + hostName
							+ " Port:" + port + " Timeout :" + timeout);
		}

		return isSuccess;
	}

	/**
	 * Add Data To generated Queue & generate result in case of any exceptions
	 * 
	 * @param queueName
	 * @param hostName
	 * @param port
	 * @param timeout
	 */
	void addDataToQueue(String queueName, String hostName, int port, int timeout) {

		HashMap<String, Integer> message = new HashMap<String, Integer>();
		// Proceed only if instance of producer is not null

		if (producer != null) {
			for (int i = 0; i < 100; i++) {
				// Generate Message
				message.put("message number ", i);
				try {
					// Add to Queue
					producer.sendMessage(message);
				} catch (IOException e) {
					finalResult.put("raiseAlert", "true");
					finalResult.put("errorMessage",
							"IO Exception Occurs while Adding Message To Queue("
									+ e.getClass().getName() + "): Host:"
									+ hostName + " Port:" + port + " Timeout :"
									+ timeout);
				} catch (Exception e) {
					finalResult.put("raiseAlert", "true");
					finalResult.put("errorMessage",
							"Exception Occurs while Adding Message To Queue("
									+ e.getClass().getName() + "): Host:"
									+ hostName + " Port:" + port + " Timeout :"
									+ timeout);
				}
			}

			try {
				// Delete Queue
				producer.deleteQueue(queueName);
			} catch (IOException e) {
				finalResult.put("raiseAlert", "true");
				finalResult.put("errorMessage",
						"Exception Occurs while Deleting Queue("
								+ e.getClass().getName() + "): Host:"
								+ hostName + " Port:" + port + " Timeout :"
								+ timeout);
			}

			finalResult.put("raiseAlert", "false");

		}

	}

}
