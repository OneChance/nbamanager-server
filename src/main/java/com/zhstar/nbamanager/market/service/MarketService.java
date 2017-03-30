package com.zhstar.nbamanager.market.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public List<Player> getMarketPlayer(Account account,String searchName,String searchPos,int page){
    	return playerRepository.findMarketPlayer(teamService.getPlayerIdsToExclude(account.getId()),searchName,getSearchPos(searchPos),new PageRequest(page,PLAYERS_IN_PAGE));
    }
    
    public Set<String> getSearchPos(String searchPos){
    	Set<String> searchPosList = new HashSet<String>();
    	
    	if(searchPos==null||searchPos.equals("")){
    		searchPosList.add("中锋");
    		searchPosList.add("前锋");
    		searchPosList.add("后卫");
    		searchPosList.add("前锋/中锋");
    		searchPosList.add("后卫/前锋");
    		searchPosList.add("前锋/后卫");
    		searchPosList.add("中锋/前锋");
    	}else{
    		if(searchPos.contains("中锋")){
    			searchPosList.add("中锋");
    			searchPosList.add("中锋/前锋");
    			searchPosList.add("前锋/中锋");
    		}
    		if(searchPos.contains("前锋")){
    			searchPosList.add("前锋");
    			searchPosList.add("中锋/前锋");
    			searchPosList.add("前锋/中锋");
    			searchPosList.add("前锋/后卫");
    			searchPosList.add("后卫/前锋");
    		}
    		if(searchPos.contains("后卫")){
    			searchPosList.add("后卫");
    			searchPosList.add("前锋/后卫");
    			searchPosList.add("后卫/前锋");
    		}
    	}
    	
    	return searchPosList;
    }
    
    @Resource
    private PlayerRepository playerRepository;
    @Resource
    private TeamService teamService;
}
