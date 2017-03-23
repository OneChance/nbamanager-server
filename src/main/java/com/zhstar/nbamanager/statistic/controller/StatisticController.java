package com.zhstar.nbamanager.statistic.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhstar.nbamanager.common.DateTool;
import com.zhstar.nbamanager.common.NetMessage;
import com.zhstar.nbamanager.statistic.entity.Statistic;
import com.zhstar.nbamanager.statistic.entity.StatisticAll;
import com.zhstar.nbamanager.statistic.service.StatisticService;

@RestController
public class StatisticController {

	@RequestMapping("/getStatistic/{playerId}/")
    public NetMessage getStatistic(@PathVariable("playerId") Long playerId,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
		StatisticAll statistic = new StatisticAll();
		statistic.setToday(statisticService.findToday(playerId, DateTool.getDateString(new Date())));
		Map<String,Integer> latest = new HashMap<String,Integer>();
		List<Statistic> latestList = statisticService.findLatest(playerId);
		for(Statistic s:latestList){
			latest.put(s.getGameDate(), s.getEv());
		}	
		statistic.setLatest(latest);
		return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS,statistic);
    } 
	
	@Resource
	StatisticService statisticService;
}
