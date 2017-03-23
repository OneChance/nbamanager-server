package com.zhstar.nbamanager.team.service;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zhstar.nbamanager.team.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
	Team findByUserid(Long userid);
}
