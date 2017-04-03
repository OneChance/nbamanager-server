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
}
