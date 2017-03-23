package com.zhstar.nbamanager.player.service;


import org.springframework.data.jpa.repository.JpaRepository;

import com.zhstar.nbamanager.player.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
	
}
