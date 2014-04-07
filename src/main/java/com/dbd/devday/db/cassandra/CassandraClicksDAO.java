package com.dbd.devday.db.cassandra;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.dbd.devday.db.ClicksDAO;
import com.dbd.devday.db.ConnectionManager;
import com.dbd.devday.model.UserClicks;

public class CassandraClicksDAO implements ClicksDAO {

	public static final String DEFAULT_KEYSPACE_NAME = "developer_day";

	private ConnectionManager<Session> dataSource = null;
	private Session session = null;
	private String keyspaceName = null;

	private PreparedStatement getResultForUser;
	private PreparedStatement findAllUsers;
	private PreparedStatement insertStatement;

	public CassandraClicksDAO(ConnectionManager<Session> dataSource) {
		this.dataSource = dataSource;
		prepareStatements();
	}

	public CassandraClicksDAO(ConnectionManager<Session> ds, String keyspaceName) {
		this.dataSource = ds;
		this.keyspaceName = keyspaceName;
		prepareStatements();
	}

	private void prepareStatements() {
		Session session = getSession();
		findAllUsers = session.prepare("select distinct user_id from clicks");
		getResultForUser = session
				.prepare("select count(*) from clicks where user_id = ?");
		insertStatement = session
				.prepare("INSERT INTO clicks (user_id, event_time) VALUES (?,?) USING TTL 5");
	}

	private Session getSession() {
		if (session == null) {
			if (keyspaceName == null) {
				session = dataSource.getSession(DEFAULT_KEYSPACE_NAME);
			} else {
				session = dataSource.getSession(keyspaceName);
			}
		}
		return session;
	}

	public List<UserClicks> getResults() throws Exception {
		List<String> users = findAllUsers();
		List<UserClicks> userClicks = new ArrayList<UserClicks>(users.size());
		for (String user : users) {
			BoundStatement boundStatement = new BoundStatement(getResultForUser);
			boundStatement.bind(user);
			ResultSet resultSet = getSession().execute(boundStatement);
			userClicks.add(new UserClicks(user, resultSet.one().getLong(0)));
		}
		return userClicks;
	}

	public List<String> findAllUsers() throws Exception {

		List<String> results = new ArrayList<String>();
		ResultSet resultSet = getSession().execute(
				findAllUsers.getQueryString());
		for (Row row : resultSet.all()) {
			results.add(row.getString(0));
		}

		return results;
	}

	@Autowired
	public void setDataSource(ConnectionManager<Session> ds) {
		this.dataSource = ds;
	}

	@Override
	public void click(String user, long timestamp) throws Exception {
		BoundStatement boundStatement = new BoundStatement(insertStatement);
		Date date = new Date(timestamp);
		boundStatement.bind(user, date);
		getSession().execute(boundStatement);
	}
}
