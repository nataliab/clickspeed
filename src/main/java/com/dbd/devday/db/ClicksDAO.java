package com.dbd.devday.db;

import java.util.List;

import com.datastax.driver.core.Session;
import com.dbd.devday.model.UserClicks;

public interface ClicksDAO {

	public void setDataSource(ConnectionManager<Session> ds);

	public List<UserClicks> getResults() throws Exception;

	public List<String> findAllUsers() throws Exception;
	
	public void click(String user, long timestamp) throws Exception;

}
