package com.appspy.util;

import java.util.ArrayList;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisLibrary {
	
	private RedisLibrary(){}
	private static RedisLibrary redisLib;
	Jedis jedis;
	
	// Get Instance for Redis
	public static synchronized RedisLibrary getInstance(){
		if(redisLib == null){
			return new RedisLibrary();
		}else{
			return redisLib;
		}
	}
	
	/**
	 * Function to get Redis Connection
	 * @param host
	 * @param port
	 * @return Jedis connection object
	 */
	public Jedis getJedis(String host,int port){
		if(jedis == null){
			jedis = new Jedis(host, port);
		}
		return jedis;
	}
	
	
	/**
	 * Function to add Set to Redis
	 * @param keyName - String Key name for Set
	 * @param setData ArrayList<T> (Generic Type - Data for Set)
	 */
	public void addMembersToSet(String keyName, ArrayList<String> setData, String host, int port){
		try{
			Jedis jedis = getJedis(host, port);
			Pipeline p = jedis.pipelined();
			for(String setDataMember : setData){
				jedis.sadd(keyName, setDataMember);
			}
			p.sync();
		}
		catch(JedisConnectionException e){
			Logger.exceptionsLogMessages("Exception Occurs while connection to JEDIS");
			throw new JedisConnectionException("Custom Exception : Unable to connect To JEDIS");
		}
		
	}

	/**
	 * 
	 * @param host 
	 * @param port
	 * @param keys - Variable no of keys for Intersection
	 * 
	 * @return Intersction of Two Sets
	 */
	public Set<String> intersectionOfSets(String host, int port, String... keys){
		try{
			Jedis jedis = getJedis(host, port);
			Set<String> intersection = jedis.sinter(keys);
			return intersection;
		}
		catch(JedisConnectionException e){
			throw new JedisConnectionException("Custom Exception : Unable to connect To JEDIS");
		}
		
	}
	
	
	/**
	 * 
	 * @param host
	 * @param port
	 * @param keys
	 * 
	 * @return - Variable no of keys to be deleted
	 */
	public void deleteKeys(String host, int port, String... keys){
		try {
			Jedis jedis = getJedis(host, port);
			jedis.del(keys);
		} catch (JedisConnectionException e) {
			Logger.exceptionsLogMessages("Exception Occurs while connection to JEDIS");
			throw new JedisConnectionException("Custom Exception : Unable to connect To JEDIS");
		}
	}
	
	public void closeConnection(){
		try{
			jedis.close();
		} catch(Exception e){
			Logger.exceptionsLogMessages("Ecxeption occurs while closing connection tp JEDIS");
		}
	}
}
