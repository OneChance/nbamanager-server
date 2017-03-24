package com.zhstar.nbamanager.team.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zhstar.nbamanager.common.NetMessage;
import com.zhstar.nbamanager.player.entity.Player;
import com.zhstar.nbamanager.player.service.PlayerRepository;
import com.zhstar.nbamanager.team.entity.Team;
import com.zhstar.nbamanager.team.entity.TeamPlayer;

@Component
public class TeamService {

	/**
	 * 
	 * @param userid
	 * @param queryOriPlayer
	 *            query from player_data to get metadata.
	 * @return
	 */
	public Team getTeamByUser(Long userid, boolean queryOriPlayer) {
		Team team = teamRepository.findByUserid(userid);
		if (team == null) {
			team = new Team();
		} else {
			if (queryOriPlayer) {
				List<Player> players = playerRepository.findAll(getTeamPlayerIds(team));
				for (TeamPlayer tPlayer : team.getPlayers()) {
					for (Player player : players) {
						if (tPlayer.getPlayerId().longValue() == player.getId().longValue()) {
							tPlayer.setName(player.getName());
							tPlayer.setAblePos(player.getPos());
							tPlayer.setSal(player.getSal());
							break;
						}
					}
				}
			}
		}
		return team;
	}

	public List<Long> getTeamPlayerIds(Long userid) {
		Team team = teamRepository.findByUserid(userid);
		return getTeamPlayerIds(team);
	}

	public List<Long> getTeamPlayerIds(Team team) {
		List<Long> ids = new ArrayList<Long>();
		for (TeamPlayer player : team.getPlayers()) {
			ids.add(player.getPlayerId());
		}
		return ids;
	}

	@Transactional
	public NetMessage signPlayer(Long userid, Player player) {
		Player sign = playerRepository.findOne(player.getId());

		if (sign == null) {
			return new NetMessage(NetMessage.STATUS_PLAYER_NOT_EXIST, NetMessage.DANGER, null);
		}
		
		Team team = getTeamByUser(userid, false);
		
		if(team.getMoney()<sign.getSal()){
			return new NetMessage(NetMessage.STATUS_NOT_ENOUGH_MONEY, NetMessage.DANGER, null);
		}
		
		List<TeamPlayer> players = team.getPlayers();
		if (players.size() > 0) {
			if (players.size() == 5) {
				return new NetMessage(NetMessage.STATUS_TEAM_FULL, NetMessage.DANGER, null);
			}

			String checkRes = rosCheck(team, player);
			
			if (checkRes!=null) {
				return new NetMessage(checkRes, NetMessage.DANGER, null);
			}
		}		

		TeamPlayer teamPlayer = new TeamPlayer(player.getId(), player.getPos(), sign.getSal(),team);
		team.getPlayers().add(teamPlayer);
		team.setMoney(team.getMoney()-sign.getSal());
		
		teamRepository.save(team);
		
		return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, null);
	}

	/**
	 * check ros must match[c:1,f:2,g:2]
	 * 
	 * @param team
	 * @param sign
	 * @return
	 */
	public String rosCheck(Team team, Player sign) {
		List<TeamPlayer> players = team.getPlayers();

		String ros = "";

		for (TeamPlayer p : players) {
			ros += p.getPos();
		}

		if (sign.getPos().equals("中锋")) {
			if(posCount("\u4e2d\u950b",ros)>=1){
				return NetMessage.STATUS_C_FULL;
			}
		}else if (sign.getPos().equals("前锋")) {
			if(posCount("\u524d\u950b",ros)>=2){
				return NetMessage.STATUS_F_FULL;
			}
		}else if (sign.getPos().equals("后卫")) {
			if(posCount("\u540e\u536b",ros)>=2){
				return NetMessage.STATUS_G_FULL;
			}
		}

		return null;
	}

	public static int posCount(String pos, String ros) {
		int count = 0;
		Matcher matcher = Pattern.compile(pos).matcher(ros);
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	@Resource
	private TeamRepository teamRepository;
	@Resource
	private PlayerRepository playerRepository;
}
