package com.appspy.requestHandlers;

import java.util.HashMap;

import org.json.simple.JSONObject;

public interface RequestHandler {
	HashMap<String, String> monitorRequests(String key, JSONObject jsonObject, JSONObject dataMonitoringObject);
}
