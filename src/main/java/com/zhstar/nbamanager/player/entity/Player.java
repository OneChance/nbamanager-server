package com.zhstar.nbamanager.player.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zhstar.nbamanager.statistic.entity.StatisticToday;

@Entity
@Table(name = "player_data")
public class Player {

	@Id
	private Long id;
	private String name;
	private String pos;
	private int sal;
	
	@Transient
	StatisticToday today;
	
	public StatisticToday getToday() {
		return today;
	}
	public void setToday(StatisticToday today) {
		this.today = today;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public int getSal() {
		return sal;
	}
	public void setSal(int sal) {
		this.sal = sal;
	}
}
