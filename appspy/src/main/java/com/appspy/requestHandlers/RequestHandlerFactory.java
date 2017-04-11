package com.appspy.requestHandlers;

public class RequestHandlerFactory {

	public static RequestHandler getRequestHandler(String serviceName){
		
		switch(serviceName){
			case "solr" :
				return new SolrRequestHandlers();
			case "web_api":
				return new ApiRequestHandler();
			case "redis":
				return new RedisRequestHandler();
			case "elasticsearch":
				return new ElasticSearchHandler();
			case "rabbitmq_server":
				return new RabbitMQServerHandler();
			case "mysql":
				return new MysqlRequestHandler();
			case "memcache":
				return new MemcacheHandler();
			default:
				return null;
		}
	}
	
}
