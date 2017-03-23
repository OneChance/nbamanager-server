package com.zhstar.nbamanager.statistic.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zhstar.nbamanager.statistic.entity.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
	Statistic findByPlayerIdAndGameDate(Long playerId,String gameDate);
	List<Statistic> findTop7ByPlayerIdOrderByGameDateDesc(Long playerId);
}
