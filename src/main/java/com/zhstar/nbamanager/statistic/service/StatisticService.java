package com.zhstar.nbamanager.statistic.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.zhstar.nbamanager.statistic.entity.Statistic;

@Component
public class StatisticService {
	
	public Statistic findToday(Long playerId,String gameDate){
		return statisticRepository.findByPlayerIdAndGameDate(playerId, gameDate);
	}
	
	public List<Statistic> findLatest(Long playerId){
		return statisticRepository.findTop7ByPlayerIdOrderByGameDateDesc(playerId);
	}
	
    @Resource 
    private StatisticRepository statisticRepository;
}
