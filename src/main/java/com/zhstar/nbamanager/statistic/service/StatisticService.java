package com.zhstar.nbamanager.statistic.service;

import java.util.List;
import javax.annotation.Resource;

import com.zhstar.nbamanager.player.entity.Player;
import com.zhstar.nbamanager.player.service.PlayerRepository;
import org.springframework.stereotype.Component;
import com.zhstar.nbamanager.statistic.entity.Statistic;

@Component
public class StatisticService {

    public Statistic findToday(String playerId, String gameDate) {
        return statisticRepository.findByUuidAndGameDate(playerId, gameDate);
    }

    public List<Statistic> findLatest(String playerId) {
        return statisticRepository.findTop7ByUuidOrderByGameDateDesc(playerId);
    }

    public List<Player> getEvRankToday() {
        return playerRepository.getEvRankToday();
    }

    @Resource
    private StatisticRepository statisticRepository;
    @Resource
    private PlayerRepository playerRepository;
}
