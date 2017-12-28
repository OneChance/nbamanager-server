package com.zhstar.nbamanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.zhstar.nbamanager.common.DateTool;
import com.zhstar.nbamanager.player.entity.Player;
import com.zhstar.nbamanager.player.service.PlayerRepository;
import com.zhstar.nbamanager.statistic.entity.Statistic;
import com.zhstar.nbamanager.statistic.service.StatisticRepository;
import com.zhstar.nbamanager.team.service.TeamRepository;

@Configuration
@EnableScheduling
public class Schedules {

    public static final float bonus = 1.2f;
    public static int TIMEOUT = 300000;

    @Value("${system.fetch}")
    private boolean fetch;

    @Transactional
    @Scheduled(cron = "0 40 14 ? * *")
    public void getWebData() throws Exception {
        if (fetch) {
            while (true) {
                try {
                    System.out.println("fetch game data begin...");

                    String playerNotExist = "";
                    List<Player> notExist = new ArrayList<>();
                    List<Statistic> statistics = new ArrayList<>();
                    List<Player> dbPlayers = playerRepository.findAll();
                    List<String> playerIds = new ArrayList<>();
                    List<String> updatePlayerIds = new ArrayList<>();
                    String fetchDate = DateTool.getCurrentString();

                    JsonParser parse = new JsonParser();

                    String matchesJsonString = getJsonContent("&s=schedule&a=date_span&date=" + fetchDate + "&span=6", "https://slamdunk.sports.sina.com.cn/match");

                    JsonObject matchesJson = (JsonObject) parse.parse(matchesJsonString);

                    JsonArray matches = getData(matchesJson).get("matchs").getAsJsonArray();


                    for (Player player : dbPlayers) {
                        playerIds.add(player.getUuid());
                    }

                    for (JsonElement match : matches) {
                        JsonObject matchObject = match.getAsJsonObject();
                        String matchId = matchObject.get("mid").getAsString();
                        String gameDate = matchObject.get("date").getAsString();

                        if (!fetchDate.equals(gameDate)) {
                            continue;
                        }

                        String playerDataJsonString = getJsonContent("&p=radar&s=summary&a=game_player&mid=" + matchId, "https://slamdunk.sports.sina.com.cn/match/stats?mid=" + matchId);

                        JsonObject playerDataJson = (JsonObject) parse.parse(playerDataJsonString);

                        JsonArray playerDatas = getData(playerDataJson)
                                .get("home").getAsJsonObject()
                                .get("players").getAsJsonArray();

                        JsonArray awayPlayerDatas = getData(playerDataJson)
                                .get("away").getAsJsonObject()
                                .get("players").getAsJsonArray();

                        playerDatas.addAll(awayPlayerDatas);

                        for (JsonElement playerData : playerDatas) {
                            Statistic statistic = new Statistic();
                            JsonObject playerDataObject = playerData.getAsJsonObject();
                            String min = playerDataObject.get("minutes").getAsString().split(":")[0];
                            String uuid = playerDataObject.get("pid").getAsString();

                            if (!playerIds.contains(uuid)) {
                                if (!playerNotExist.contains("," + uuid + ",")) {
                                    playerNotExist = playerNotExist + uuid + ",";
                                }
                                Player needAdd = getPlayerByUUID(uuid);
                                notExist.add(needAdd);
                            } else {
                                updatePlayerIds.add(uuid);
                            }

                            statistic.setUuid(uuid);
                            statistic.setGameDate(gameDate);
                            statistic.setMin(min);
                            statistic.setFg(playerDataObject.get("field_goals_made").getAsString() + "-" + playerDataObject.get("field_goals_att"));
                            statistic.setP3(playerDataObject.get("three_points_made").getAsString() + "-" + playerDataObject.get("three_points_att"));
                            statistic.setFt(playerDataObject.get("free_throws_made").getAsString() + "-" + playerDataObject.get("free_throws_att"));
                            statistic.setOreb(playerDataObject.get("offensive_rebounds").getAsString());
                            statistic.setDreb(playerDataObject.get("defensive_rebounds").getAsString());
                            statistic.setReb(playerDataObject.get("rebounds").getAsString());
                            statistic.setAst(playerDataObject.get("assists").getAsString());
                            statistic.setStl(playerDataObject.get("steals").getAsString());
                            statistic.setBlk(playerDataObject.get("blocks").getAsString());
                            statistic.setFa(playerDataObject.get("turnovers").getAsString());
                            statistic.setFo(playerDataObject.get("personal_fouls").getAsString());
                            statistic.setPts(playerDataObject.get("points").getAsString());
                            calEV(statistic);

                            statistics.add(statistic);
                        }
                    }

                    System.out.println("fetch game data complete... size:" + statistics.size());

                    if (statistics.size() > 0) {
                        // 更新球员工资
                        List<Player> updatePlayers = playerRepository.findByPlayerIdIn(updatePlayerIds);
                        if (updatePlayers != null && updatePlayers.size() > 0) {
                            for (Player player : updatePlayers) {
                                for (Statistic statistic : statistics) {
                                    if (statistic.getUuid().equals(player.getUuid())) {
                                        player.setSal(calSal(player.getSal(), statistic.getEv()));
                                        break;
                                    }
                                }
                            }
                        }

                        playerRepository.save(updatePlayers);
                        statisticRepository.save(statistics);
                    }

                    if (!playerNotExist.equals("")) {
                        statisticRepository.recordPlayersNotExist(playerNotExist);
                    }
                    System.out.println("database updated...");

                    teamRepository.closeMoney();
                    System.out.println("money closed...");

                    //添加不存在的球员
                    if (notExist.size() > 0) {
                        playerRepository.save(notExist);
                    }

                    break;
                } catch (Exception e) {
                    //出现异常,休眠5分钟后再次尝试
                    Thread.sleep(300000);
                    e.printStackTrace();
                    System.out.println("try again...");
                }
            }
        }
    }


    @Transactional
    @Scheduled(cron = "59 59 23 ? * *")//59 59 23 ? * *
    public void clearTeamOperatingData() {
        teamRepository.clearTeamOperatingData();
        System.out.println("operating data clear...");
    }

    private int calSal(int old, String ev) {
        return new BigDecimal(old).add(new BigDecimal(ev)).intValue();
    }

    private void calEV(Statistic statistic) {

        BigDecimal pts = new BigDecimal(statistic.getPts());
        BigDecimal oreb = new BigDecimal(statistic.getOreb());
        BigDecimal dreb = new BigDecimal(statistic.getDreb());
        BigDecimal assist = new BigDecimal(statistic.getAst());
        BigDecimal steal = new BigDecimal(statistic.getStl());
        BigDecimal block = new BigDecimal(statistic.getBlk());
        BigDecimal shootOut = new BigDecimal(statistic.getFg().split("-")[1]);
        BigDecimal shootIn = new BigDecimal(statistic.getFg().split("-")[0]);
        BigDecimal throwOut = new BigDecimal(statistic.getFt().split("-")[1]);
        BigDecimal throwIn = new BigDecimal(statistic.getFt().split("-")[0]);
        BigDecimal fault = new BigDecimal(statistic.getFa());

        int ev = Math.round(pts.add(oreb.multiply(new BigDecimal(bonus))).add(dreb).add(assist)
                .add(steal.multiply(new BigDecimal(bonus))).add(block.multiply(new BigDecimal(bonus)))
                .subtract(shootOut).add(shootIn).subtract(throwOut).add(throwIn).subtract(fault).floatValue());

        if (statistic.getMin().equals("00")) {
            ev = -5;
        }

        statistic.setEv(String.valueOf(ev));
    }

    public Player getPlayerByUUID(String uuid) throws IOException {
        JsonParser parse = new JsonParser();
        String playerInfoJsonString = getJsonContent("&p=radar&s=player&a=info&pid=" + uuid, "https://slamdunk.sports.sina.com.cn/roster");
        JsonObject playerInfoJson = (JsonObject) parse.parse(playerInfoJsonString);
        JsonObject playerObject = getData(playerInfoJson);
        Player player = new Player();
        player.setUuid(uuid);
        player.setName((playerObject.get("first_name_cn").getAsString() + "·" + playerObject.get("last_name_cn").getAsString()).replaceAll("-", "·"));
        player.setNameEn((playerObject.get("first_name").getAsString() + "·" + playerObject.get("last_name").getAsString()).replaceAll("-", "·"));
        player.setPos(getPosFromInfo(playerObject.get("primary_position").getAsString()));
        player.setSal(1500);
        player.setStatus(0);
        return player;
    }

    private static String getJsonContent(String params, String referrer) throws IOException {
        Long reqTime = new Date().getTime();
        String jqueryCallBackName = "jQuery111306562200843946073_" + reqTime;

        Document doc = Jsoup
                .connect("https://slamdunk.sports.sina.com.cn/api?p=radar&callback=" + jqueryCallBackName + params + "&_=" + (reqTime + 1))
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/63.0.3239.84 Chrome/63.0.3239.84 Safari/537.36")
                .ignoreContentType(true)
                .referrer(referrer)
                .timeout(TIMEOUT).get();

        return doc.body().html().replace("try{" + jqueryCallBackName + "(", "")
                .replace(");}catch(e){};", "");
    }

    private static JsonObject getData(JsonObject resultObject) {
        return resultObject.get("result").getAsJsonObject()
                .get("data").getAsJsonObject();
    }

    private String getPosFromInfo(String playerInfo) {
        String pos = "";
        if (playerInfo.indexOf("中锋") > 0) {
            pos = pos + "中锋/";
        }
        if (playerInfo.indexOf("前锋") > 0) {
            pos = pos + "前锋/";
        }
        if (playerInfo.indexOf("后卫") > 0) {
            pos = pos + "后卫/";
        }
        if (pos.endsWith("/")) {
            pos = pos.substring(0, pos.length() - 1);
        }
        return pos;
    }

    @Resource
    private PlayerRepository playerRepository;
    @Resource
    private StatisticRepository statisticRepository;
    @Resource
    private TeamRepository teamRepository;
}
