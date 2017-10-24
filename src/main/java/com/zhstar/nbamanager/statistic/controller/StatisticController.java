package com.zhstar.nbamanager.statistic.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhstar.nbamanager.account.entity.Account;
import com.zhstar.nbamanager.player.entity.Player;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhstar.nbamanager.common.DateTool;
import com.zhstar.nbamanager.common.NetMessage;
import com.zhstar.nbamanager.statistic.entity.GameEv;
import com.zhstar.nbamanager.statistic.entity.Statistic;
import com.zhstar.nbamanager.statistic.entity.StatisticAll;
import com.zhstar.nbamanager.statistic.service.StatisticService;

@RestController
public class StatisticController {

    @RequestMapping("/getStatistic/{playerId}/")
    public NetMessage getStatistic(@PathVariable("playerId") Long playerId, HttpServletRequest request, HttpServletResponse response) throws Exception {

        StatisticAll statistic = new StatisticAll();
        statistic.setToday(statisticService.findToday(playerId, DateTool.getDateString(new Date())));
        List<GameEv> latest = new ArrayList<GameEv>();
        List<Statistic> latestList = statisticService.findLatest(playerId);
        for (Statistic s : latestList) {
            latest.add(new GameEv(s.getGameDate(), s.getEv()));
        }
        Collections.reverse(latest);
        statistic.setLatest(latest);
        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, statistic);
    }

    @RequestMapping("/getEvRankToday/")
    public NetMessage getEvRankToday(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Player> players;

        players = statisticService.getEvRankToday();

        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, players);
    }

    @Resource
    StatisticService statisticService;
}
