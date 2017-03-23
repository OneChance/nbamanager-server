package com.zhstar.nbamanager.statistic.entity;

import java.util.Map;

public class StatisticAll {
	private Statistic today;
	private Map<String,Integer> latest;
	
	public Statistic getToday() {
		return today;
	}
	public void setToday(Statistic today) {
		this.today = today;
	}
	public Map<String, Integer> getLatest() {
		return latest;
	}
	public void setLatest(Map<String, Integer> latest) {
		this.latest = latest;
	}
	
	
}
