<style type="text/css">
#wrap {width:100%;margin:0 auto;}
#left_col {float:left;width:60%;}
#right_col {float:right;width:40%;}
.erm{margin-left: 30px;}
.error{color: red;font-size:10px;}
.loader{display: none;}
</style>
<?php
	$clientIP=$_SERVER['SERVER_NAME'];
	define("CSSURL", $clientIP);
	define("JSURL", $clientIP);
?>
<link rel="stylesheet" href="//<?php echo CSSURL;?>/public/css/pure-min.css" />

<div id="wrap">
    <div id="left_col">
    <form class="pure-form pure-form-aligned" id="serviceAddForm">
    	<div class="pure-controls">
			<fieldset>
	    		<div class="pure-control-group">
	    			<label for="serviceList"> Select Service </label>
			        <select name="serviceList" id="serviceList">
			        	<option value="-1">Select</option>
			        	<option value="solr">Solr</option>
			        	<option value="redis">Redis</option>
			        	<option value="memcache">Memcache</option>
			        </select> 
		        </div>
			    <div id="serviceData"></div>    
	    	</fieldset>
	    	<div style="margin-left: 45px">
	    		<button type="button" class="pure-button pure-button-primary" id="serviceAddBtn">Add Service</button>
	    		<span class="loader"><img src="/public/images/loader.gif"></span>
	    		<span class="post_error error"></span>
	    	</div>
    	</div>
 		
    </form>
    </div>
    <div id="right_col">
        Hello
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script type="text/javascript" src="//<?php echo JSURL;?>/public/js/main.js"></script>
<script type="text/javascript">
	
	var appSpyConfig = new AppSpyConfig();
	appSpyConfig.bindElementsAndInitialize();

    
		
	


</script>