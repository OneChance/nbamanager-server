package com.zhstar.nbamanager.team.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "team_player")
public class TeamPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerId;
    private String pos;
    private Integer signMoney;
    private String nextTradeableDate;

    @Transient
    private String name;
    @Transient
    private String ablePos;
    @Transient
    private int sal;

    @Column(name = "team_id")
    private Long teamId;

    public TeamPlayer() {

    }

    public TeamPlayer(Long playerId, String pos, Integer signMoney) {
        this.playerId = playerId;
        this.pos = pos;
        this.signMoney = signMoney;
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

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
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

    public String getNextTradeableDate() {
        return nextTradeableDate;
    }

    public void setNextTradeableDate(String nextTradeableDate) {
        this.nextTradeableDate = nextTradeableDate;
    }
}
