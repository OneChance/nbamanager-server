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
	
	public final int PLAYERS_IN_PAGE = 10;

    public List<Player> getMarketPlayer(Account account,int page){	
    	return playerRepository.findMarketPlayer(teamService.getPlayerIdsToExclude(account.getId()),new PageRequest(page,PLAYERS_IN_PAGE));
    }
    
    public List<Player> getMarketPlayerByName(Account account,String name,int page){
    	return playerRepository.findMarketPlayerByName(teamService.getPlayerIdsToExclude(account.getId()),name,new PageRequest(page,PLAYERS_IN_PAGE));
    }

    @Resource
    private PlayerRepository playerRepository;
    @Resource
    private TeamService teamService;
}
