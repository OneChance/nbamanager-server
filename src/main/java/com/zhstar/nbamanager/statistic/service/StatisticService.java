package com.zhstar.nbamanager.statistic.service;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.zhstar.nbamanager.statistic.entity.StatisticToday;

@Component
public class StatisticService {
	
	public StatisticToday findStatisticToday(Long playerId,String gameDate){
		return statisticRepository.findByPlayerIdAndGameDate(playerId, gameDate);
	}
	
    @Resource 
    private StatisticRepository statisticRepository;
}
