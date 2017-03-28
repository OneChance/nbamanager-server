package com.zhstar.nbamanager.player.service;


import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zhstar.nbamanager.player.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
	@Query("select p from Player p where p.id not in ?1")
	public List<Player> findMarketPlayer(List<Long> idsInTeam,Pageable pageable);
	
	@Query("select p from Player p where p.id not in ?1 and p.name like %?2%")
	public List<Player> findMarketPlayerByName(List<Long> idsInTeam,String name,Pageable pageable);

	@Query("select p from Player p where p.id in ?1")
	public List<Player> findByPlayerIdIn(List<Long> ids);
}
