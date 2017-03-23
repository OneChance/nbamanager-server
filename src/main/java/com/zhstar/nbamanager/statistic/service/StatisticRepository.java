package com.zhstar.nbamanager.statistic.service;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zhstar.nbamanager.statistic.entity.StatisticToday;

public interface StatisticRepository extends JpaRepository<StatisticToday, Long> {
	StatisticToday findByPlayerIdAndGameDate(Long playerId,String gameDate);
}
