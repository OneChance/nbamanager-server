package com.zhstar.nbamanager.player.service;


import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zhstar.nbamanager.player.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, String> {
    @Query("select p from Player p where p.uuid not in ?1 and p.name like %?2% and p.pos in ?3 and p.status = 0 order by p.sal desc")
    List<Player> findMarketPlayer(List<String> idsInTeam, String searchName, Set<String> searchPos, Pageable pageable);

    @Query("select p from Player p where p.uuid in ?1 and p.status = 0")
    List<Player> findByPlayerIdIn(List<String> ids);

    @Query(value = "select a.* from player_data a join game_data b on a.uuid = b.uuid where game_date = DATE_FORMAT(NOW(),'%Y-%m-%d') order by convert(ev,SIGNED) desc limit 10", nativeQuery = true)
    List<Player> getEvRankToday();
}
