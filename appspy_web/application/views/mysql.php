
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
                <div class="pure-controls">
                        <label for="dbconn">DataBase : </label>
                        <input type="text" name="dbconn" id="dbconn" validate="validateStr" caption="Db Connection String" placeholder="DB Connection String(jdbc:mysql://IP:PORT/DB_NAME)" style="width: 400px;">
                        <span class="error" id="dbconn_error"></span>
                </div>
                <div class="pure-controls">
                        <label for="username">Username :</label>
                        <input type="text" name="username" id="username" validate="validateStr" caption="Username ">
                        <span class="error" id="username_error"></span>
                </div>
                <div class="pure-controls">
                        <label for="password">Password :</label>
                        <input type="password" name="password" id="password"  caption="Password">
                        <span class="error" id="password_error"></span>
                </div>

                <div class="pure-controls">
                        <label for="sql">SQL Query:</label>
                        <textarea name="sql" id="sql" style="width: 400px;height: 100px;"></textarea>
                        <span class="error" id="sql_error"></span>
                </div>
                
                
                <label for="responseTimeCheck" class="pure-checkbox" class="erm">
                        <input type="checkbox" name="responseTimeCheck" id="responseTimeCheck" caption="Response Time"> Check Query Time
                </label>
                <div id="resp_time" style="display: none;" class="erm">
                        <label for="responseTime">Query Time</label>
                        <input type="text" name="responseTime" id="responseTime" value="200" validate="validateInt" minValue="1"> ms
                        <span class="error" id="responseTime_error"></span>  
                </div>
                <div style="margin-top: 10px;">
                        <label for="exceptionCheckCount">Failure Count :</label>
                        <input type="text" name="exceptionCheckCount" id="exceptionCheckCount" value="3" validate="validateInt" minValue="1" caption="Failure Count">
                        <span class="error" id="exceptionCheckCount_error"></span>  

                </div>

                <div style="margin-top: 10px;">
                        <label for="timeout">Timeout :</label>
                        <input type="text" name="timeout" id="timeout" value="1000" validate="validateInt" minValue="1" caption="Timeout"> seconds(0 means no Limit)
                        <span class="error" id="timeout_error"></span>  

                </div>
                
        </div>
</div>

        