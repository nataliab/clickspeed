package com.dbd.devday.db.cassandra;

import static org.junit.Assert.*;

import java.util.List;

import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.datastax.driver.core.Session;
import com.dbd.devday.db.ConnectionManager;
import com.dbd.devday.db.KeyValueStoreDAO;
import com.dbd.devday.model.KeyValue;

/**
 * 
 * In the most recent version of the driver the following issue causes some nasty stack trace output
 * 
 * See : https://issues.apache.org/jira/browse/CASSANDRA-6639
 * 
 */
public class EmbeddedCassandraKeyValueStoreDAOTest {

	final String testKeyspace = "developer_day_embedded";
	final String testUser = "testUser";
	final String testKey = "testKey";
	final String testValue = "testValue";

	ConnectionManager<Session> connectionManager = null;
	KeyValueStoreDAO dao = null;

    @Rule
    public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(new ClassPathCQLDataSet("create.cql", testKeyspace));

	@Before
	public void setup() {
		if(connectionManager == null) {
			connectionManager = new CassandraConnectionManager(
					new String[] { "127.0.0.1" }, 9142);
			dao = new CassandraKeyValueStoreDAO(connectionManager, testKeyspace);
		}
	}

	@Test
	public void testCreate() throws Exception {

		dao.save(testUser, new KeyValue(testKey, testValue));

		KeyValue kv = null;
		kv = dao.findByUserAndKey(testUser, testKey);

		if (kv == null)
			fail("Key under test does not exist");

		assertEquals(kv.getKey(), testKey);
		assertEquals(kv.getValue(), testValue);
	}

	@Test
	public void testDelete() throws Exception {
		dao.save(testUser, new KeyValue(testKey, testValue));
		dao.delete(testUser, testKey);
		KeyValue keyValue = dao.findByUserAndKey(testUser, testValue);
		assertNull(keyValue);
	}

	@Test
	public void testDeleteRow() throws Exception {
		dao.save(testUser, new KeyValue(testKey, testValue));
		dao.deleteByUser(testUser);
		List<KeyValue> kvs = dao.findByUser(testUser);
		assertEquals(kvs.size(), 0);
	}

	@Test
	public void testUpdate() throws Exception {

		dao.save(testUser, new KeyValue(testKey, testValue));
		dao.update(testUser, new KeyValue(testKey, testValue + "1"));
		KeyValue keyValue = dao.findByUserAndKey(testUser, testKey);

		assertEquals(keyValue.getValue(), testValue + "1");

	}

	@Test
	public void testGetByUserAndKey() throws Exception {

		dao.save(testUser, new KeyValue(testKey, testValue));
		KeyValue keyValue = dao.findByUserAndKey(testUser, testKey);

		assertEquals(keyValue.getValue(), testValue);

	}

	@Test
	public void testGetAll() throws Exception {

		dao.save(testUser, new KeyValue(testKey, testValue));
		dao.save(testUser, new KeyValue(testKey + "1", testValue));
		List<KeyValue> kvs = dao.findByUser(testUser);

		assertTrue(kvs.size() == 2);
		assertEquals(kvs.get(0).getKey(), testKey);
		assertEquals(kvs.get(1).getKey(), testKey + "1");

	}
}
