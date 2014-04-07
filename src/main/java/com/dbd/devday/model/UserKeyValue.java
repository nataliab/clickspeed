package com.dbd.devday.model;

import java.util.Set;

public class UserKeyValue {
	private String userId;
	private Set<KeyValue> keyValues;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Set<KeyValue> getKeyValues() {
		return keyValues;
	}
	public void setKeyValues(Set<KeyValue> keyValues) {
		this.keyValues = keyValues;
	}
}
