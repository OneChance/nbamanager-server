package com.zhstar.nbamanager.team.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zhstar.nbamanager.team.entity.TeamPlayer;

public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, Long> {

}
