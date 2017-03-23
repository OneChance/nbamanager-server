package com.zhstar.nbamanager.statistic.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "game_data")
public class Statistic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long playerId;
	private String gameDate;
	private Integer min;
	private String fg;
	private String p3;
	private String ft;
	private Integer oreb;
	private Integer dreb;
	private Integer reb;
	private Integer ast;
	private Integer stl;
	private Integer blk;
	private Integer to;
	private Integer pf;
	private Integer pts;
	private Integer ev;
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
	public void setMin(Integer min) {
		this.min = min;
	}
	public String getGameDate() {
		return gameDate;
	}
	public void setGameDate(String gameDate) {
		this.gameDate = gameDate;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public String getFg() {
		return fg;
	}
	public void setFg(String fg) {
		this.fg = fg;
	}
	public String getP3() {
		return p3;
	}
	public void setP3(String p3) {
		this.p3 = p3;
	}
	public String getFt() {
		return ft;
	}
	public void setFt(String ft) {
		this.ft = ft;
	}
	public Integer getOreb() {
		return oreb;
	}
	public void setOreb(Integer oreb) {
		this.oreb = oreb;
	}
	public Integer getDreb() {
		return dreb;
	}
	public void setDreb(Integer dreb) {
		this.dreb = dreb;
	}
	public Integer getReb() {
		return reb;
	}
	public void setReb(Integer reb) {
		this.reb = reb;
	}
	public Integer getAst() {
		return ast;
	}
	public void setAst(Integer ast) {
		this.ast = ast;
	}
	public Integer getStl() {
		return stl;
	}
	public void setStl(Integer stl) {
		this.stl = stl;
	}
	public Integer getBlk() {
		return blk;
	}
	public void setBlk(Integer blk) {
		this.blk = blk;
	}
	public Integer getTo() {
		return to;
	}
	public void setTo(Integer to) {
		this.to = to;
	}
	public Integer getPf() {
		return pf;
	}
	public void setPf(Integer pf) {
		this.pf = pf;
	}
	public Integer getPts() {
		return pts;
	}
	public void setPts(Integer pts) {
		this.pts = pts;
	}
	public Integer getEv() {
		return ev;
	}
	public void setEv(Integer ev) {
		this.ev = ev;
	}
}
