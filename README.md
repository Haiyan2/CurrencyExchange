# Currency Exchange 
======================

#System Overview
The objective of this project is to provide an overview of the trades activities. Based on the input trade data, a chord diagram visualization is generated that shows the number of trades between currencies within the latest hours.

##Message Consumption
Implemented an efficient endpoint in Java / Spring. The messages are then stored in a Cassandra database optimized for writing a large number of transactions.

##Message Processor
A processor updates periodically the aggregation of trades within currencies that are used for the visualization. The data is obtained from Cassandra and aggregated in Java (no aggregation is natively supported in Cassandra).

##Message Front-end
The data processed by the Processor is used to display a chord diagram implemented in HTML / Javascript with the D3.js library. The visualization is based on the data that are already processed and thus could be displayed in many browsers without impacting the performance.


##Main technologies
Back-end:
* Apache Cassandra
* Java 8 with Spring Boot and embedded Tomcat Server

Front-end:
* HTML / JavaScript with D3 library.

Build:
* Apache Maven

Deployment:
* Docker

Test:
* JUnit (Unit and Integration test)
* SoapUI (REST API stress test)
* Simulator embedded in the application.

#Usage
##Quick start

A machine with public IP has been setup and is running the system.
For visualization:
```
http://185.87.184.99
```

API Endpoint:
```
http://185.87.184.99/api/trades
```
# Endpoints

##Endpoint Assumptions
1. The time (with field: timePlaced) in the POST request is in UTC and in the format of the example: ```24-JAN-15 10:27:44```
2. All the fields are mandatory and can't be empty. For flexibility in this exercise, no validation on the length of the fields is done (e.g. no validation that the currencies are in a list of supported currencies).

##Endpoint Usage
Once the System is running (version online or version deployed using the instruction in section “Build and Run the System within Docker”, you can access it use your application or a test tool like Postman or SoapUI to post trades through rest endpoint: 
```
http://[SERVER_IP]/api/trades
```
Note that the end point accepts the JSON form, e.g.,
```
{"userId": "134256", "currencyFrom": "EUR", "currencyTo": "GBP", "amountSell": 1000, "amountBuy": 747.10, "rate": 0.7471, "timePlaced" : "24-JAN-15 10:27:44", "originatingCountry" : "FR"}
```
The end point returns only an HTTP status code. Main status codes:
  * 201 if the trade is correctly received.
  * 400 if the format of the request is not correct.
  * 500 if there is an internal error (e.g. The Database is not available).
The posted request shall contain the Header Content-Type with value application/json.


Open a browser, go to below link to view the analytics result:
```
http://[SERVER_IP]
```

Optionally, you can view the analytics data from below link:
```
http://[SERVER_IP]/exchangeDyn.csv
```


#System Main Components
##TradeAPIController: 
API Endpoint, receives trade post and returns an HTTP status code. 
  
##TradeServiceImpl:
Service layer between client and DAO. 

##TradeAnalyticsDataProcessor: 
Use scheduled service to constantly process the updated data for analytics, at a configurable refresh interval.

##TradeAnalyticsService: 
Perform the analytics for the trade data, e.g., aggregation on the number of transactions based on the currencyFrom and CurrencyTo.

##TradeController: 
A data updated listener, which then updates the analytics data to file exchangeDyn.csv which is used for the web visualization.

##exchange.html: 
HTML file that uses D3.js to create the chord diagram visualization of the number of transactions between currencies, with the data from /exchangeDyn.csv.
Based on the project d3-chord-diagrams with MIT license: https://github.com/sghall/d3-chord-diagrams.

##CassandraClientImpl: Database persistence
A Cassandra Client. Persist the trade data, creates keyspace and schema.


#Design Considerations
1. Use new technologies, e.g., Java 8, Spring Boot, Cassandra, Docker. 
2. Producer - Consumer pattern. The visualization do not query directly the database for data, instead a data processors is periodically producing the data to be consumed by the front-end controller. 
3. Open design: Make use of interface and dependency injection on main components to decouple them.
    
4. Separate the environments for unit test and development/production, e.g., using a test.properties for unit test and application.properties for development/production usage.

5. Created a simulator to generate the data to the end points.

6. Configurable setting, e.g., for enabling or disabling the simulators, database connections, analytics refreshing intervals and etc.

Future improvements:
* Use Cassandra multi-site architecture with replication to have different clusters, one to store the operational data (trade operations) and another to query for analytics processing.
* Use Kafka for messaging middleware as which is good at handling large throughput of data: TradeServiceImpl send the trade to Kafka producer, which generates the messaging and sends to Kafka consumer for DB persistence and also for trade analytics. This design could potentially improve the performance as well as reducing the accessing of database to retrieve the data for analytics.
* In the analytics UI, apart from having more analytics, future enhancement could just refresh the data instead of refreshing the whole page.
* Use Spark Stream to consume the trades from Kafka and process the data required for the visualization.
* Use Elasticsearch to store the trades to allow to compute more statistics and show them in Kibana.




#Build and Run the System - Option 1: within Docker (recommended)

Prerequisite: Docker 1.10.3 or greater (see https://docs.docker.com/engine/installation/).

##Create custom Docker network:
This network is required to ensure the containers visible to each other.

```
docker network create yan2_nw
```

##Run Cassandra container
To run Cassandra, we use the official Docker image (see https://hub.docker.com/_/cassandra/).

```
docker run --name cassandra --net=yan2_nw -p 7000:7000 -p 9160:9160 -p 9042:9042 -p 7199:7199 -d cassandra:latest
```

##Build Currency Exchange image
An automated build of the Docker image has been setup using DockerHub (see https://hub.docker.com/r/haiyan/currencyexchange/) and is triggered each time there is a push to the GitHub repository. Thus it is not required to build it manually. The latest image is publicly available with the tag ```haiyan/currencyexchange:latest```.

Even if it is done by DockerHub, if you prefer to build the Docker image by yourself, clone the github repository locally, open a command line and locate to the root of the project and execute the following command:
```
docker build -t haiyan/currencyexchange:latest .
```

##Run Currency Exchange container
Note to replace ```[CASSANDRA_IP]``` with the IP of the Cassandra container.
Run (by default the simulator is disabled) either:
```
docker run --name currencyexchange --net=yan2_nw -e "PARAMS=--cassandra.node=[CASSANDRA_IP]" -p 80:8080 -d haiyan/currencyexchange:latest
```
Or if you would like to run the application with the simulator enabled, run:
```
docker run --name currencyexchange --net=yan2_nw -e "PARAMS=--cassandra.node=[CASSANDRA_IP] --trade.simulator.enabled=true" -p 80:8080 -d haiyan/currencyexchange:latest
```


#Build and Run the System - Option 2: without Docker

Prerequisite: Cassandra version 3.3 is installed and running.

Note, the port to use is 8080.

##Steps to Build the Application
Maven 3.3 and Java 8 must be installed.

Run:
```
mvn install
```

## Steps to Run the Application

1. The properties file ```\config\application.properties```, contains the default configuration. The properties can be overridden using the command line.

2. Go to the ```\target```, from the command line, run (by default the simulator is disabled) either:
```
java -jar CurrencyExchange-0.0.1-SNAPSHOT.jar --cassandra.node=[CASSANDRA_IP]
```
Or if you would like to run the application with the simulator enabled, run:
```
java -jar CurrencyExchange-0.0.1-SNAPSHOT.jar --cassandra.node=[CASSANDRA_IP] --trade.simulator.enabled=true
```



