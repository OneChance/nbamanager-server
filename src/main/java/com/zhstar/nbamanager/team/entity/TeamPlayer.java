package com.zhstar.nbamanager.team.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "team_player")
public class TeamPlayer {

	@Id
	@GeneratedValue(generator = "paymentableGenerator")
    @GenericGenerator(name = "paymentableGenerator", strategy = "increment")
	private Long id;
	private Long playerId;
	private String pos;
	private Integer signMoney;
	
	@Transient
	private String name;
	@Transient
	private String ablePos;
	@Transient
	private int sal;
	
	@JsonIgnore
	@ManyToOne(cascade={CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.MERGE},optional=true)
    @JoinColumn(name = "team_id")
	private Team team;
	
	public TeamPlayer(){
		
	}
	
	public TeamPlayer(Long playerId,String pos,Integer signMoney,Team team){
		this.playerId = playerId;
		this.pos = pos;
		this.signMoney = signMoney;
		this.team = team;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public Integer getSignMoney() {
		return signMoney;
	}

	public void setSignMoney(Integer signMoney) {
		this.signMoney = signMoney;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAblePos() {
		return ablePos;
	}

	public void setAblePos(String ablePos) {
		this.ablePos = ablePos;
	}

	public int getSal() {
		return sal;
	}

	public void setSal(int sal) {
		this.sal = sal;
	}
}
