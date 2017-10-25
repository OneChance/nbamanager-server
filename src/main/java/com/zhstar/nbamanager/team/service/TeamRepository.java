package com.zhstar.nbamanager.team.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zhstar.nbamanager.team.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

    int SEASON_DAYS = 365;

    Team findByUserId(Long userId);

    @Modifying
    @Query("update Team t set t.money = ?2 where t.id = ?1")
    void updateMoney(Long teamId, Integer money);

    @Modifying
    @Query("update Team t set t.name = ?2 where t.userId = ?1")
    void updateName(Long userId, String name);

    @Modifying
    @Query(value = "update team set " +
            "earn_today = if((select count(*) from team_player where team_id = team.id)<5,0,(SELECT ifnull(sum(ev),0) ev FROM nba_game.game_data where player_id in (select player_id from team_player where team_id = team.id) and game_date = DATE_FORMAT(NOW(),'%Y-%m-%d')))," +
            "cost_today = ifnull((select ceil(sum(sign_money/" + SEASON_DAYS + ")) from team_player where team_id = team.id),0)," +
            "money = money + earn_today - cost_today," +
            "ev=ifnull(ev,0) + earn_today", nativeQuery = true)
    void closeMoney();

    @Modifying
    @Query("update Team t set t.earnToday = 0,t.costToday=0")
    void clearTeamOperatingData();
}
