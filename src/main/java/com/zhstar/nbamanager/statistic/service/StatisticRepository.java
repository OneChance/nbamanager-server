package com.zhstar.nbamanager.statistic.service;

import java.util.List;

import com.zhstar.nbamanager.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.zhstar.nbamanager.statistic.entity.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic, String> {

    Statistic findByUuidAndGameDate(String playerId, String gameDate);

    List<Statistic> findTop7ByUuidOrderByGameDateDesc(String playerId);


    @Modifying
    @Transactional
    @Query(value = "update player_not_exist set player_ids = concat(player_ids,?1)", nativeQuery = true)
    void recordPlayersNotExist(String addIds);
}
