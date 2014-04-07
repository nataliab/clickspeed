Multiplayer real time web based mouseclicking competition game. 

Available at http://natalia.cloudapp.net/clickspeed

Project inspired and initiated during [Apache Cassandra workshop with DigBigData](http://tech.gilt.com/post/81325000353/bigchat-and-other-highlights-from-last-weeks-cassandra).

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

### Playing
Enter your name and start clicking! Chart shows all users currently playing.



