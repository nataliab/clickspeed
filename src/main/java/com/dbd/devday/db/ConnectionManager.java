package com.dbd.devday.db;

public interface ConnectionManager<T> {
	public T getSession(String tableName);
	public void close() throws Exception;
}
