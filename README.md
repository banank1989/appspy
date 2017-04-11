# AppSpy
AppSpy is tool to monitor the Third Party Apps.<br /><br />
Currently it supports : 
- Mysql
- Solr
- ElasticSearch
- Any Web API(Get / Post)
- Rabbit MQ Server
- Redis
- Memcache

It checks:
- Port Monitoring
- Data Monitoring

It pings the added service every Minute & Raise the Alert if needed(Store in Database)

### Dependencies
  a) LAMP Stack <br />
  b) JAVA 8 with JAVA_HOME variable pointing towards Java 8<br />
  c) Enable mod_rewrite for apache using(since .htaccess file is there in project) <br />
  **sudo a2enmod rewrite**<br />
  **sudo service apache2 restart**
  d) Once setup is done, run the sql file, It will create the Databases.

  
### Configuration
  Code contains two Parts : <br />
  a) **appspy_web** : This is used to add the services for which monitoring needs to be done. It does not need any configuration. If the setup is done properly, it is good to go.<br /> The services added using this are stored in **alertsConfig** table.<br />
  To run it, http://<SERVER-IP>/Folder e.g. http://127.0.0.1/appspy<br /><br />

  The service to be monitored can be added using Simple Form. All the fields in the form are self-explanatory.
  One field **FailueCount** is there, it is for number of times the response is not recieved properly before raising exception.<br />

  b) **appspy**: It is the actual Java Code that fetches the Services added by **appspy_web** & monitors them. If alerts needs to generated, it generate the alerts(store them in alertsData Table).
<br />
  To run it, download the folder & navigate to appspy directory & run command<br />
  **mvn clean install**<br /><br />
  This will build the code & create the jar file inside target folder(Use the one with-dependencies.jar)
  <br />
**Note** : JAVA_HOME variable must be point to JAVA 8 or change the pom.xml file according to Java version

### Dashboard
![alt tag](https://raw.githubusercontent.com/banank1989/appspy/master/screenshots/appspy1.jpg)<br />
![alt tag](https://raw.githubusercontent.com/banank1989/appspy/master/screenshots/appspy2.jpg)<br />
![alt tag](https://raw.githubusercontent.com/banank1989/appspy/master/screenshots/appspy3.jpg)<br />


 




