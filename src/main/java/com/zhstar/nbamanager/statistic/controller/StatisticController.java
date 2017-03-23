package com.zhstar.nbamanager.statistic.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhstar.nbamanager.common.DateTool;
import com.zhstar.nbamanager.common.NetMessage;
import com.zhstar.nbamanager.statistic.service.StatisticService;

@RestController
public class StatisticController {

	@RequestMapping("/getGameToday/{playerId}/")
    public NetMessage getTeamInfo(@PathVariable("playerId") Long playerId,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS,statisticService.findStatisticToday(playerId, DateTool.getDateString(new Date())));
    } 
	
	@Resource
	StatisticService statisticService;
}
