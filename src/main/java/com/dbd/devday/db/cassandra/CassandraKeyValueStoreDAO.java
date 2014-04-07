package com.dbd.devday.db.cassandra;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.dbd.devday.db.ConnectionManager;
import com.dbd.devday.db.KeyValueStoreDAO;
import com.dbd.devday.model.KeyValue;

public class CassandraKeyValueStoreDAO implements KeyValueStoreDAO {

	public static final String DEFAULT_KEYSPACE_NAME = "developer_day";
	
	private ConnectionManager<Session> dataSource = null;
	private Session session = null;
	private String keyspaceName = null;
	
	private PreparedStatement findByUserStatement;
	private PreparedStatement findAllUsers;
	private PreparedStatement findByUserAndKeyStatement;
	private PreparedStatement insertStatement;
	private PreparedStatement updateStatement;
	private PreparedStatement deleteByUserStatement;
	private PreparedStatement deleteByUserAndKeyStatement;
	
	
	
	public CassandraKeyValueStoreDAO(ConnectionManager<Session> dataSource) {
		this.dataSource = dataSource;
		prepareStatements();
	}
	
	public CassandraKeyValueStoreDAO(ConnectionManager<Session> ds, String keyspaceName) {
		this.dataSource = ds;
		this.keyspaceName = keyspaceName;
		prepareStatements();
	}
	
	private void prepareStatements() {
		Session session = getSession();
		findByUserStatement = session.prepare("SELECT key, value FROM kv WHERE user = ?");
		findAllUsers = session.prepare("SELECT key, value FROM kv");
		findByUserAndKeyStatement = session.prepare("SELECT key, value FROM kv WHERE user = ? AND key = ?");
		insertStatement = session.prepare("INSERT INTO kv (user, key, value) VALUES (?,?,?)");
		updateStatement = session.prepare("UPDATE kv SET value = ? WHERE user = ? AND key = ?");
		deleteByUserStatement = session.prepare("DELETE FROM kv WHERE user = ?");
		deleteByUserAndKeyStatement = session.prepare("DELETE FROM kv WHERE user = ? AND key = ?");
	}
	
	private Session getSession() {
		if (session == null) {
			if(keyspaceName == null) {
				session = dataSource.getSession(DEFAULT_KEYSPACE_NAME);
			}
			else { 
				session = dataSource.getSession(keyspaceName);
			}
		}
		return session;
	}
	
	public List<KeyValue> findByUser(String user) throws Exception {
			
		BoundStatement boundStatement = new BoundStatement(findByUserStatement);
		boundStatement.bind(user);
		
		List<KeyValue> results = new ArrayList<KeyValue>();
		ResultSet resultSet = getSession().execute(boundStatement);
		for(Row row : resultSet.all()) {
			results.add(new KeyValue(row.getString(0), row.getString(1)));
		}

		return results;
	}
	
	public List<KeyValue> findAllUsers() throws Exception {
		
		List<KeyValue> results = new ArrayList<KeyValue>();
		ResultSet resultSet = getSession().execute(findAllUsers.getQueryString());
		for(Row row : resultSet.all()) {
			results.add(new KeyValue(row.getString(0), row.getString(1)));
		}

		return results;
	}

	public KeyValue findByUserAndKey(String user, String key) throws Exception {

		BoundStatement boundStatement = new BoundStatement(findByUserAndKeyStatement);
		boundStatement.bind(user, key);
		
		ResultSet resultSet = getSession().execute(boundStatement);
		if(!resultSet.isExhausted()) {
			Row row = resultSet.one();
			return new KeyValue(row.getString(0), row.getString(1));
		}
		else
			return null;
	}	
	
	public void save(String user, KeyValue keyValue) throws Exception {
		
		BoundStatement boundStatement = new BoundStatement(insertStatement);
		boundStatement.bind(user, keyValue.getKey(), keyValue.getValue());
		
		getSession().execute(boundStatement);
	}

	public void delete(String user, String key) throws Exception {

		BoundStatement boundStatement = new BoundStatement(deleteByUserAndKeyStatement);
		boundStatement.bind(user, key);
		
		getSession().execute(boundStatement);
		
	}

	public void deleteByUser(String user) throws Exception {
		
		BoundStatement boundStatement = new BoundStatement(deleteByUserStatement);
		boundStatement.bind(user);
		
		getSession().execute(boundStatement);
	}

	public void update(String user, KeyValue keyValue) throws Exception {
		
		BoundStatement boundStatement = new BoundStatement(updateStatement);
		boundStatement.bind(keyValue.getValue(), user, keyValue.getKey());
		
		getSession().execute(boundStatement);
		
	}

	@Autowired
	public void setDataSource(ConnectionManager<Session> ds) {
		this.dataSource = ds;
	}
}
