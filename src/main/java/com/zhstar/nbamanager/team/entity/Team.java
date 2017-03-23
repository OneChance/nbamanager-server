package com.zhstar.nbamanager.team.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zhstar.nbamanager.player.entity.Player;


@Entity
@Table(name = "team")
public class Team{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String players;
    private Long userid;
    private Integer money;
    private Integer ev;
    @OneToOne(optional = false)
    @JoinColumn(name = "arena_id")
    private Arena arena;
    
    @Transient
    List<Player> playerList;
  
	public List<Player> getPlayerList() {
		if(playerList==null){
			playerList = new ArrayList<Player>();
		}
		return playerList;
	}
	public void setPlayerList(List<Player> playerList) {
		this.playerList = playerList;
	}
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
	public String getPlayers() {
		return players;
	}
	public void setPlayers(String players) {
		this.players = players;
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
}
