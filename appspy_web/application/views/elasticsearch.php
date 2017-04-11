
<!--div class="pure-control-group">
        <label for="name">Unique Name :</label>
        <input type="text" name="unique_name">
</div-->
<div class="pure-control-group">
        <label for="host">Host :</label>
        <input type="text" name="host" validate="validateStr" mandatory="true" caption="Host" id="host">
        <span class="error" id="host_error"></span>
</div>
<div class="pure-control-group">
        <label for="name">Port :</label>
        <input type="text" name="port" validate="validateInt" mandatory="true" caption="Port" id="port">
        <span class="error" id="port_error"></span>
</div>
<div class="pure-controls">
        <label for="port_monitoring" class="pure-checkbox"><input type="checkbox" name="port_monitoring" id="port_monitoring"> Port Monitoring</label>
</div>
<div class="pure-controls">
        <label for="data_monitoring" class="pure-checkbox">
                <input type="checkbox" name="data_monitoring" id="data_monitoring"> Data Monitoring
        </label>
        <div id="data_monitoring_data" style="display: none;" class="erm">
                
                <label for="url">URL :</label>
                <input type="text" name="url" id="url" validate="validateStr" caption="Query">
                <span class="error" id="url_error"></span>
                
                <br /><br />
                <label for="query">JSON Query :</label>
                <input type="text" name="query" id="query" validate="validateStr" caption="Query">
                <span class="error" id="query_error"></span>

                <label for="responseCodeCheck" class="pure-checkbox">
                        <input type="checkbox" name="responseCodeCheck" id="responseCodeCheck"> Check Response Code
                </label>
                <div id="resp_code" style="display: none;" class="erm">
                        <label for="responseCode">Response Code</label>
                        <input type="text" name="responseCode" id="responseCode" value="200" validate="validateInt" caption="Response Code" minValue="1"> 
                        <span class="error" id="responseCode_error"></span>
                </div>
                <label for="responseTimeCheck" class="pure-checkbox" class="erm">
                        <input type="checkbox" name="responseTimeCheck" id="responseTimeCheck" caption="Response Time"> Check Response Time
                </label>
                <div id="resp_time" style="display: none;" class="erm">
                        <label for="responseTime">Response Time</label>
                        <input type="text" name="responseTime" id="responseTime" value="200" validate="validateInt" minValue="1"> ms
                        <span class="error" id="responseTime_error"></span>  
                </div>
                <div style="margin-top: 10px;">
                        <label for="exceptionCheckCount">Failure Count :</label>
                        <input type="text" name="exceptionCheckCount" id="exceptionCheckCount" value="3" validate="validateInt"  minValue="1" caption="Failure Count">
                        <span class="error" id="exceptionCheckCount_error"></span>  

                </div>

                <div style="margin-top: 10px;">
                        <label for="timeout">Timeout :</label>
                        <input type="text" name="timeout" id="timeout" value="1000" validate="validateInt"  minValue="1" caption="Timeout">
                        <span class="error" id="timeout_error"></span>  

                </div>
                
        </div>
</div>

        