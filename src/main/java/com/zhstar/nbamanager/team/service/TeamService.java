package com.zhstar.nbamanager.team.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.zhstar.nbamanager.player.service.PlayerRepository;
import com.zhstar.nbamanager.team.entity.Team;

@Component
public class TeamService {
	
	public Team getTeamByUser(Long userid){
		Team team = teamRepository.findByUserid(userid);
		if(team==null){
			team = new Team();
		}else{
			String players = team.getPlayers();
			if(players!=null&&!players.equals("")){
				List<Long> ids = new ArrayList<Long>();
				String[] playerIds = players.split(",");
				for(String id:playerIds){
					ids.add(Long.parseLong(id));
				}
				team.setPlayerList(playerRepository.findAll(ids));
			}	
		}
		return team;
	}
    
    @Resource
    private TeamRepository teamRepository;
    @Resource 
    private PlayerRepository playerRepository;
}
