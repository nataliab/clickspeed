package com.dbd.devday.db.cassandra;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.datastax.driver.core.Session;
import com.dbd.devday.db.ConnectionManager;
import com.dbd.devday.db.KeyValueStoreDAO;
import com.dbd.devday.model.KeyValue;

/**
 * Sample integration test - ideally cassandra would be run as part of gradle task
 */
public class CassandraKeyValueStoreDAOTest {

	final String testUser = "testUser";
	final String testKey = "testKey";
	final String testValue = "testValue";
	
	
	ConnectionManager<Session> connectionManager = null;
	KeyValueStoreDAO dao = null;
	
	@Before
	public void setup() {
		connectionManager = new CassandraConnectionManager(new String[]{"127.0.0.1"}, 9042);
		dao = new CassandraKeyValueStoreDAO(connectionManager);
	}
	
	@Test
	public void testCreate() throws Exception {
		
		dao.save(testUser, new KeyValue(testKey, testValue));
	
		KeyValue kv = null;
		kv = dao.findByUserAndKey(testUser, testKey);
			
		if(kv == null)
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
	
	@Test
	public void testGetAllUsers() throws Exception {
		
		dao.save(testUser, new KeyValue(testKey, testValue));
		dao.save("testUser2", new KeyValue(testKey + "1", testValue));
		
		List<KeyValue> users = dao.findAllUsers();
		assertTrue(users.size() >= 2);
		
	}
	
	
}
