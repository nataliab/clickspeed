package com.dbd.devday.db.cassandra;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.datastax.driver.core.Session;
import com.dbd.devday.db.ClicksDAO;
import com.dbd.devday.db.ConnectionManager;
import com.dbd.devday.model.UserClicks;

/**
 * Sample integration test - ideally cassandra would be run as part of gradle task
 */
public class ClicksDAOTest {

	
	ConnectionManager<Session> connectionManager = null;
	ClicksDAO dao = null;
	
	@Before
	public void setup() {
		connectionManager = new CassandraConnectionManager(new String[]{"127.0.0.1"}, 9042);
		dao = new CassandraClicksDAO(connectionManager);
	}
	
	@Test
	public void testClick() throws Exception {
		dao.click("john", (new Date()).getTime());
		List<UserClicks> results = dao.getResults();
		assertTrue(results.size()>0);
	}
	
	
}
