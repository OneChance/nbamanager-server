package com.zhstar.nbamanager.team.service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

import com.zhstar.nbamanager.common.NetMessage;
import com.zhstar.nbamanager.player.service.PlayerRepository;
import com.zhstar.nbamanager.team.entity.Team;

@Component
public class TeamService {
	
	public Team getTeamByUser(Long userid){
		Team team = teamRepository.findByUserid(userid);
		if(team==null){
			team = new Team();
		}else{
			team.setPlayerList(playerRepository.findAll(getTeamPlayerIds(team)));
		}
		return team;
	}
	
	public List<Long> getTeamPlayerIds(Long userid){
		Team team = teamRepository.findByUserid(userid);
		return getTeamPlayerIds(team);
	}
	
	public List<Long> getTeamPlayerIds(Team team){
		List<Long> ids = new ArrayList<Long>();
		if(team!=null){
			String players = team.getPlayers();
			if(players!=null&&!players.equals("")){
				String[] playerIds = players.split(",");
				for(String id:playerIds){
					ids.add(Long.parseLong(id));
				}
			}
		}	
		return ids;
	}
	
	public NetMessage signPlayer(Long userid,String playerId){
		Team team = teamRepository.findByUserid(userid);
		String players = team.getPlayers();
		if(players!=null && !players.equals("")){
			String[] playerIds = players.split(",");
			if(playerIds.length==5){
				return new NetMessage(NetMessage.STATUS_TEAM_FULL, NetMessage.DANGER,null);
			}
			team.setPlayers(team.getPlayers()+","+playerId);
		}else{
			team.setPlayers(playerId);
		}
		
		return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS,null);
	}
    
    @Resource
    private TeamRepository teamRepository;
    @Resource 
    private PlayerRepository playerRepository;
}
