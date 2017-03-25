package com.zhstar.nbamanager.team.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "team")
public class Team{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long userid;
    private Integer money;
    private Integer ev;
    @OneToOne(optional = false)
    @JoinColumn(name = "arena_id")
    private Arena arena;
    

	@OneToMany(cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE},fetch=FetchType.EAGER,mappedBy = "team")
    private List<TeamPlayer> players;
   
	public Arena getArena() {
		return arena;
	}
	public void setArena(Arena arena) {
		this.arena = arena;
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
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}
	public Integer getEv() {
		return ev;
	}
	public void setEv(Integer ev) {
		this.ev = ev;
	}
	public List<TeamPlayer> getPlayers() {
		if(players==null){
			players = new ArrayList<TeamPlayer>();
		}
		return players;
	}
	public void setPlayers(List<TeamPlayer> players) {
		this.players = players;
	}
	
}
