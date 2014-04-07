package com.dbd.devday.model;

public class UserClicks {
	private String userId;
	private long clicks;
	private long time = 5;

	public UserClicks(String userId, long clicks) {
		super();
		this.userId = userId;
		this.clicks = clicks;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public float getAverage() {
		return (float)clicks / time;
	}

}
