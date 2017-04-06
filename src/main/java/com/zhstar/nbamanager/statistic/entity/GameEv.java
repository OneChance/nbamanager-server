package com.zhstar.nbamanager.statistic.entity;

public class GameEv {
	private String date;
	private Integer ev;
	
	public GameEv(){
		
	}
	
	public GameEv(String date,Integer ev){
		this.date = date;
		this.ev = ev;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getEv() {
		return ev;
	}
	public void setEv(Integer ev) {
		this.ev = ev;
	}
}
