package com.zhstar.nbamanager.market.service;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.zhstar.nbamanager.account.entity.Account;
import com.zhstar.nbamanager.player.entity.Player;
import com.zhstar.nbamanager.player.service.PlayerRepository;
import com.zhstar.nbamanager.team.service.TeamService;

@Component
public class MarketService {

    public List<Player> getMarketPlayer(Account account,int page){	
    	return playerRepository.findMarketPlayer(teamService.getTeamPlayerIds(account.getId()),new PageRequest(page,10));
    }
    
    public List<Player> getMarketPlayerByName(Account account,String name,int page){
    	return playerRepository.findMarketPlayerByName(teamService.getTeamPlayerIds(account.getId()),name,new PageRequest(page,10));
    }

    @Resource
    private PlayerRepository playerRepository;
    @Resource
    private TeamService teamService;
}
