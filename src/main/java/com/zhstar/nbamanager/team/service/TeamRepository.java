package com.zhstar.nbamanager.team.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zhstar.nbamanager.team.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
	Team findByUserId(Long userId);
	
	@Modifying
	@Query("update Team t set t.money = ?2 where t.id = ?1")
	void updateMoney(Long teamId,Integer money);
	
	@Modifying
	@Query("update Team t set t.name = ?2 where t.userId = ?1")
	void updateName(Long userId,String name);
	
	@Modifying
	@Query(value = "update team set money = money + "
			+ "if((select count(*) from team_player where team_id = team.id)<5,?2,(SELECT ifnull(sum(ev),0) ev FROM nba_game.game_data where player_id in "
			+ "(select player_id from team_player where team_id = team.id) and game_date = ?1))", nativeQuery = true)
	void closeMoney(String closeDay,int lackPlayerPunishment);
}
