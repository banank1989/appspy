 <div class="head">Add Service</div>
    <form class="pure-form pure-form-aligned" id="serviceAddForm">
    	<div class="pure-controls">
			<fieldset>
	    		<div class="pure-control-group">
	    			<label for="serviceList"> Select Service </label>
			        <select name="serviceList" id="serviceList" autocomplete=false>
			        	<option value="-1">Select</option>
			        	<option value="solr">Solr</option>
			        	<option value="web_api">Web API</option>
			        	<option value="redis">Redis</option>
			        	<option value="memcache">Memcache</option>
			        	<option value="elasticsearch">Elastic Search</option>
			        	<option value="mysql">Mysql</option>
			        	<option value="rabbitmq_server">Rabbit MQ Server</option>
			        </select> 
		        </div>
		        <div class="pure-control-group">
				        <label for="serviceName">Service Name :</label>
				        <input type="text" name="serviceName" validate="validateStr" mandatory="true" caption="Service Name" id="serviceName">
				        <span class="error" id="serviceName_error"></span>
				</div>
			    <div id="serviceData"></div>    
	    	</fieldset>
	    	<div style="margin-left: 45px">
	    		<button type="button" class="pure-button pure-button-primary" id="serviceAddBtn">Add Service</button>
	   
	    		<span class="post_error error"></span>
	    	</div>
    	</div>
 		
