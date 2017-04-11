package com.appspy.util;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;


/**
 * The producer endpoint that writes to the queue.
 * @author syntx
 *
 */
public class RabbitMQProducer extends RabbitMQEndPoint{
	
	public RabbitMQProducer(String endPointName, String hostName, int port, int timeout) throws IOException{
		super(endPointName, hostName, port, timeout);
	}
	
	public void sendMessage(Serializable object) throws IOException {
	    channel.basicPublish("",queueName, null, SerializationUtils.serialize(object));
	}
	
	public void deleteQueue(String queueName) throws IOException {
	    channel.queueDelete(queueName);
	}
}