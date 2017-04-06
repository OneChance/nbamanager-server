package com.zhstar.nbamanager.statistic.entity;

import java.util.List;

public class StatisticAll {
	private Statistic today;
	private List<GameEv> latest;
	
	public Statistic getToday() {
		return today;
	}
	public void setToday(Statistic today) {
		this.today = today;
	}
	public List<GameEv> getLatest() {
		return latest;
	}
	public void setLatest(List<GameEv> latest) {
		this.latest = latest;
	}
}
