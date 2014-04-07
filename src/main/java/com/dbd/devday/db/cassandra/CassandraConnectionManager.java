package com.dbd.devday.db.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.dbd.devday.db.ConnectionManager;

/**
 * Simple class for getting a Cassandra Session
 * 
 * TODO - exception handling, input validation etc. 
 */
public class CassandraConnectionManager implements ConnectionManager<Session> {
	
	public static final int DEFAULT_CASSANDRA_PORT = 9042;
	
	private Cluster cluster = null;
	
	public CassandraConnectionManager(String[] nodes, int port) {
		this.cluster = buildCluster(nodes, port);
	}
	
	public CassandraConnectionManager(String[] nodes) {
		this.cluster = buildCluster(nodes, DEFAULT_CASSANDRA_PORT);
	}
	
	private Cluster buildCluster(String[] nodes, int port) {
		return Cluster.builder()
				.addContactPoints(nodes)
				.withPort(port)
				.build();	
	}
	
	public Session getSession(String keyspace) {
		return cluster.connect(keyspace);
	}

	public void close() throws Exception {
		cluster.close();
	}
}
