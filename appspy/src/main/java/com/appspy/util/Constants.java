package com.appspy.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.HashMap;

import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.common.io.stream.InputStreamStreamInput;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

public class Constants {

	
	final static public boolean ISDEBUGMODE = false;
	final static public int CONNECT_TIMEOUT = 10000;
	final static public int READ_TIMEOUT = 10000;
	final static public int CHECK_CNT = 5;
	final static public boolean CREATE_JSON_FILE = true;
	
	static public String DB_CONN_STRING = "jdbc:mysql://127.0.0.1/test";
	static public String DB_USER = "root";
	static public String DB_PASS = "root";
	static public String serviceFileName = "services_list.json";
	
	/*final static public String DB_CONN_STRING = "jdbc:mysql://172.16.3.248:3306/shiksha";
	final static public String DB_USER = "shiksha";
	final static public String DB_PASS = "shiKm7Iv80l";*/
	
	/*
	 * Function reading json file with service data
	 */
	public JSONObject populateServicesData(){
		boolean result = true;
		if(CREATE_JSON_FILE){
			result = createConfigFile();
		}
		
		if(result == false){
			return null;
		}
		
		JSONParser parser = new JSONParser();
		try{
		
			File f = new File(serviceFileName);

			FileReader filereader = new FileReader(f);
			Object obj = parser.parse(filereader);
			JSONObject jsonObject = (JSONObject) obj;
			if(jsonObject == null){
				throw new Exception("File Not Present");
			}
			return jsonObject;
		}catch(FileNotFoundException e){
			e.printStackTrace();
			return null;
		}	
		catch(ParseException e){
			e.printStackTrace();
			return null;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean createConfigFile(){
		boolean result = true;
		try{
			
			
			DBAccess.createConnection();
			String q = "select * from alertsConfig where status = 'live'";
			ResultSet rs = DBAccess.selectData(q);
			HashMap<String, HashMap<String, String>> finalServiceList = new HashMap<String, HashMap<String,String>>();
			HashMap<String, String> service;

			while (rs.next()) {
				service = new HashMap<String, String>();
				service.put("serviceType",rs.getString("serviceType"));
				service.put("host",rs.getString("host"));
				service.put("port",rs.getString("port"));
				service.put("port_monitoring",rs.getString("port_monitoring"));
				service.put("data_monitoring",rs.getString("data_monitoring"));
				finalServiceList.put(rs.getString("unique_id"), service);
			}
			
			StringWriter out = new StringWriter();
			   JSONValue.writeJSONString(finalServiceList, out);
			   String jsonText = out.toString();
			   
			   JSONParser parser = new JSONParser();
			   JSONObject json = (JSONObject) parser.parse(jsonText);
			   
			   
			   ObjectMapper mapper = new ObjectMapper();
			   Object json1 = mapper.readValue(json.toString(), Object.class);
			   String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
			  
			   File f = new File(serviceFileName);
			   
			   FileWriter fw = new FileWriter(f);
			   fw.write(indented);
			   fw.flush();
			   fw.close();
			   
	
		}catch(Exception e){
			result = false;
		}
		return result;
	}
	
	
}
