package com.appspy.util;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

	/**
	 * Represents a connection with a queue
	 * @author syntx
	 *
	 */
public abstract class RabbitMQEndPoint{
		
    protected Channel channel;
    protected Connection connection;
    public String queueName;
    public String hostName;
    public int port;
	
    public RabbitMQEndPoint(String queueName, String hostName, int port, int timeout) throws IOException{
    	
         this.queueName = queueName;
		
         //Create a connection factory
         ConnectionFactory factory = new ConnectionFactory();
	    
         //hostname of your rabbitmq server
         factory.setHost(hostName);
         
         // Port for Rabbit MQ
         factory.setPort(port);
        
         factory.setConnectionTimeout(timeout);
		
         //getting a connection
         connection = factory.newConnection();
	    
         //creating a channel
         channel = connection.createChannel();
	    
         //declaring a queue for this channel. If queue does not exist,
         //it will be created on the server.
         channel.queueDeclare(queueName, false, false, false, null);
      
    }
	
	
    /**
     * Close channel and connection. Not necessary as it happens implicitly any way. 
     * @throws IOException
     */
     public void close() throws IOException{
         this.channel.close();
         this.connection.close();
     }
}
