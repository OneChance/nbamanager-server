package com.zhstar.nbamanager.team.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zhstar.nbamanager.team.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
	Team findByUserid(Long userid);
	
	@Modifying
	@Query("update Team t set t.money = ?2 where t.id = ?1")
	void updateMoney(Long teamId,Integer money);
}
