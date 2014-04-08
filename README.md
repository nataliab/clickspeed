###Multiplayer web based mouseclicking competition game. 

Available at http://natalia.cloudapp.net/clickspeed

Project inspired by and initiated during [Apache Cassandra workshop with DigBigData](http://tech.gilt.com/post/81325000353/bigchat-and-other-highlights-from-last-weeks-cassandra).

Uses Spring Web MVC framework and Apache Cassandra time series feature to store the click events of multiple users.

### Requirements 

* JDK7
* Gradle
* Apache Cassandra

### Configuration

* Default Cassandra port: 9042
   - See: com.dbd.devday.db.cassandra.CassandraConnectionManager.DEFAULT_CASSANDRA_PORT = 9042
* Uses  keyspace "developer_day"
   - See: com.dbd.devday.db.cassandra.CassandraClicksDAO.DEFAULT_KEYSPACE_NAME = "developer_day"


### Building and running

Call src\test\resources\create.cql to create the KEYSPACE and TABLES.

Call gradle tasks in the root of the project to build .war and deploy to local Jetty server:
```
gradle build jettyRunWar
```

or with stopping jetty before deployment:

```
gradle jettyStop build jettyRunWar
```

In case of troubles use -d switch for debug output:

```
gradle -d jettyStop build jettyRunWar
```

Application will be started at http://localhost:8080/clickspeed.

Test results are available in build\reports\tests\index.html.

### Importing project into Eclipse

From Eclipse: File > Import > Existing project into workspace > Select location of cloned repository (root includes Eclipse .project file). 
After import call 

```
gradle -d cleanEclipse eclipse
```
to update build path. 

### Playing
Enter your name and start clicking! Chart shows all users currently playing.


### How is the average speed computed?

Every click's timestamp is inserted into Cassanrda database with time to live = 5sec (TTL = 5). Result chart is actively polling database for the total number of entries existing for each user, which gives the numer of clicks within the last 5 seconds. Entries older than 5 sec are removed from database and when all click events are removed player dissapears from the results chart (and from database). 

### Cassandra schema

```
CREATE TABLE clicks (user_id text, event_time timestamp, PRIMARY KEY(user_id, event_time));
```

Example database content:
```
cqlsh:developer_day> select * from clicks;

 user_id | event_time
---------+--------------------------------------
    adam | 2013-05-13 10:42:51GMT Daylight Time
    adam | 2013-05-13 10:42:51GMT Daylight Time
     nat | 2013-05-13 10:43:01GMT Daylight Time
     nat | 2013-05-13 13:29:31GMT Daylight Time
     nat | 2013-05-19 05:36:21GMT Daylight Time

(5 rows)
```


