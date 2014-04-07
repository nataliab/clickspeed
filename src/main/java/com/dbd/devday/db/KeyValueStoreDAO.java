package com.dbd.devday.db;

import java.util.List;

import com.datastax.driver.core.Session;
import com.dbd.devday.model.KeyValue;


public interface KeyValueStoreDAO {

	   public void setDataSource(ConnectionManager<Session> ds);
	   public List<KeyValue> findByUser(String user) throws Exception;
	   public List<KeyValue> findAllUsers() throws Exception;
	   public KeyValue findByUserAndKey(String user, String key) throws Exception;
	   public void save(String user, KeyValue keyValue) throws Exception;
	   public void delete(String user, String key) throws Exception;
	   public void deleteByUser(String user) throws Exception;
	   public void update(String user, KeyValue keyValue) throws Exception;

}
