package com.zhstar.nbamanager.team.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.xml.crypto.Data;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhstar.nbamanager.common.DateTool;
import com.zhstar.nbamanager.common.NetMessage;
import com.zhstar.nbamanager.player.entity.Player;
import com.zhstar.nbamanager.player.service.PlayerRepository;
import com.zhstar.nbamanager.team.entity.ContractLog;
import com.zhstar.nbamanager.team.entity.Team;
import com.zhstar.nbamanager.team.entity.TeamPlayer;

@Service
public class TeamService {

    public final int LOGS_IN_PAGE = 10;
    public final int TRADE_LIMIT_DAYS = 7;
    public final int DAY_TRADE_TIME = 15;

    /**
     * @param userId
     * @param queryOriPlayer 是否查询球员元数据
     * @return
     */
    public Team getTeamByUser(Long userId, boolean queryOriPlayer) {
        Team team = teamRepository.findByUserId(userId);
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

    public List<Long> getPlayerIdsToExclude(Long userId) {
        Team team = teamRepository.findByUserId(userId);
        List<Long> ids = getTeamPlayerIds(team);
        if (ids.size() == 0) {
            ids.add(-1l);
        }
        return ids;
    }

    public List<Long> getTeamPlayerIds(Long userId) {
        Team team = teamRepository.findByUserId(userId);
        return getTeamPlayerIds(team);
    }

    public List<Long> getTeamPlayerIds(Team team) {
        List<Long> ids = new ArrayList<Long>();
        for (TeamPlayer player : team.getPlayers()) {
            ids.add(player.getPlayerId());
        }
        return ids;
    }

    public List<ContractLog> getContractLogs(Long userId, String searchName, int page) {
        List<ContractLog> logs = contractLogRepository.findByUserId(userId, searchName,
                new PageRequest(page, LOGS_IN_PAGE));
        return logs;
    }

    @Transactional
    public NetMessage signPlayer(Long userId, Player player) {
        Player signPlayer = playerRepository.findOne(player.getId());

        if (DateTool.getHour() < DAY_TRADE_TIME) {
            return new NetMessage(NetMessage.STATUS_INVALID_OPERATION, NetMessage.DANGER, null);
        }

        if (signPlayer == null) {
            return new NetMessage(NetMessage.STATUS_PLAYER_NOT_EXIST, NetMessage.DANGER, null);
        }

        Team team = getTeamByUser(userId, false);

        if (team.getMoney() < signPlayer.getSal()) {
            return new NetMessage(NetMessage.STATUS_NOT_ENOUGH_MONEY, NetMessage.DANGER, null);
        }

        List<TeamPlayer> players = team.getPlayers();
        if (players.size() > 0) {
            if (players.size() == 5) {
                return new NetMessage(NetMessage.STATUS_TEAM_FULL, NetMessage.DANGER, null);
            }

            String checkRes = rosCheck(team, player);

            if (checkRes != null) {
                return new NetMessage(checkRes, NetMessage.DANGER, null);
            }
        }

        TeamPlayer teamPlayer = new TeamPlayer(player.getId(), player.getPos(), signPlayer.getSal());
        teamPlayer.setNextTradeableDate(DateTool.getNDaysAfter(TRADE_LIMIT_DAYS));
        team.getPlayers().add(teamPlayer);
        team.setMoney(team.getMoney() - signPlayer.getSal());

        teamRepository.save(team);

        ContractLog contractLog = new ContractLog();
        contractLog.setDate(DateTool.getCurrentString());
        contractLog.setMoney(signPlayer.getSal());
        contractLog.setPlayerId(signPlayer.getPlayerId());
        contractLog.setType(contractLog.SIGN);
        contractLog.setPlayerName(signPlayer.getName());
        contractLog.setUserId(userId);

        contractLogRepository.save(contractLog);

        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, null);
    }

    @Transactional
    public NetMessage breakPlayer(Long userId, Player player) throws Exception {
        Player breakPlayer = playerRepository.findOne(player.getId());

        if (DateTool.getHour() < DAY_TRADE_TIME) {
            return new NetMessage(NetMessage.STATUS_INVALID_OPERATION, NetMessage.DANGER, null);
        }

        if (breakPlayer == null) {
            return new NetMessage(NetMessage.STATUS_PLAYER_NOT_EXIST, NetMessage.DANGER, null);
        }

        Team team = getTeamByUser(userId, false);

        List<TeamPlayer> players = team.getPlayers();

        if (players == null || players.size() == 0) {
            return new NetMessage(NetMessage.STATUS_INVALID_OPERATION, NetMessage.DANGER, null);
        } else {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPlayerId().longValue() == player.getId().longValue()) {

                    if (DateTool.isBefore(DateTool.getCurrentString(), players.get(i).getNextTradeableDate())) {
                        return new NetMessage(NetMessage.STATUS_TRADE_STILL_LIMIT, NetMessage.DANGER, null);
                    }

                    players.remove(i);
                    break;
                }
            }
        }

        int moneyAfterSign = team.getMoney() + breakPlayer.getSal();
        team.setMoney(moneyAfterSign);

        ContractLog contractLog = new ContractLog();
        contractLog.setDate(DateTool.getCurrentString());
        contractLog.setMoney(breakPlayer.getSal());
        contractLog.setPlayerId(breakPlayer.getPlayerId());
        contractLog.setPlayerName(breakPlayer.getName());
        contractLog.setType(contractLog.BREAK);
        contractLog.setUserId(userId);

        contractLogRepository.save(contractLog);

        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, moneyAfterSign);
    }

    @Transactional
    public NetMessage changePlayerPos(Long userId, Player player) throws Exception {

        Player sign = playerRepository.findOne(player.getId());

        if (sign == null) {
            return new NetMessage(NetMessage.STATUS_PLAYER_NOT_EXIST, NetMessage.DANGER, null);
        }

        Team team = getTeamByUser(userId, false);

        String checkRes = rosCheck(team, player);

        if (checkRes != null) {
            return new NetMessage(checkRes, NetMessage.DANGER, null);
        }

        List<TeamPlayer> players = team.getPlayers();

        if (players == null || players.size() == 0) {
            return new NetMessage(NetMessage.STATUS_INVALID_OPERATION, NetMessage.DANGER, null);
        } else {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getPlayerId().longValue() == player.getId().longValue()) {
                    players.get(i).setPos(player.getPos());
                    break;
                }
            }
        }

        teamRepository.save(team);

        return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS, null);
    }

    @Transactional
    public NetMessage changeTeamName(Long userId, String name) {
        teamRepository.updateName(userId, name);
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
            if (posCount("\u4e2d\u950b", ros) >= 1) {
                return NetMessage.STATUS_C_FULL;
            }
        } else if (sign.getPos().equals("前锋")) {
            if (posCount("\u524d\u950b", ros) >= 2) {
                return NetMessage.STATUS_F_FULL;
            }
        } else if (sign.getPos().equals("后卫")) {
            if (posCount("\u540e\u536b", ros) >= 2) {
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
    private TeamPlayerRepository teamPlayerRepository;
    @Resource
    private PlayerRepository playerRepository;
    @Resource
    private ContractLogRepository contractLogRepository;
}
