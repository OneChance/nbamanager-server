package com.zhstar.nbamanager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.jsoup.Jsoup;
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
	public final int LACK_PLAYER_PUNISHMENT = -25;
    public static int TIMEOUT = 300000;

	@Value("${system.fetch}")
	private boolean fetch;

	@Transactional
	@Scheduled(cron = "0 40 14 ? * *")
	public void getWebData() throws Exception {
		if (fetch) {
			System.out.println("fetch game data begin...");

			String playerNotExist = "";
			
			String gamesUrl = "http://nba.sports.sina.com.cn/match_result.php?day=0&years="
					+ DateTool.getNowYear() + "&months=" + DateTool.getNowMonthString() + "&teams=";

			Document doc = Jsoup.connect(gamesUrl)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 LBBROWSER")
					.header("Referer", "http://nba.sports.sina.com.cn/match_result.php")
					.timeout(TIMEOUT).get();
			Elements trs = doc.select("#table980middle tr");

			String today = DateTool.getCurrentStringNoSplit();
			String gameDate = DateTool.getCurrentString();
			List<Statistic> statistics = new ArrayList<Statistic>();
			List<Player> dbPlayers = playerRepository.findAll();
			List<Long> playerIds = new ArrayList<Long>();
			List<Long> updatePlayerIds = new ArrayList<Long>();

			for (Player player : dbPlayers) {
				playerIds.add(player.getPlayerId());
			}

			for (Element tr : trs) {
				Elements tds = tr.select("td");

				String href = tds.get(8).select("a").attr("href");

				if (href == null || href.equals("") || !href.contains("look_scores.php")) {
					continue;
				}

				String id = getIdFromUrl(href);

				if (id.startsWith(today)) {

					Document oneGame = Jsoup.connect("http://nba.sports.sina.com.cn/" + href)
							.header("Referer", gamesUrl)
							.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 LBBROWSER")
                            .timeout(TIMEOUT)
							.get();
					Elements allTr = oneGame.select("#main tr");

					for (Element onePlayer : allTr) {
						Elements gameDatas = onePlayer.select("td");

						String playerHref = gameDatas.get(0).select("a").attr("href");

						if (playerHref == null || playerHref.equals("") || !playerHref.contains("player_one.php")) {
							continue;
						}

						String playerId = getIdFromUrl(playerHref);
						String playerName = gameDatas.get(0).select("a").html();

						if (playerId == null || playerId.equals("")) {
							continue;
						}

						if (!playerIds.contains(Long.parseLong(playerId))) {
							if (!playerNotExist.contains("," + playerId + ",")) {
								playerNotExist = playerNotExist + playerId + ",";
							}
						} else {
							updatePlayerIds.add(Long.parseLong(playerId));
						}

						if (gameDatas.size() == 14) {
							Statistic statistic = new Statistic();
							statistic.setPlayerId(Long.parseLong(playerId));
							statistic.setPlayerName(playerName);
							statistic.setMin(Integer.parseInt(gameDatas.get(1).html()));
							statistic.setFg(gameDatas.get(2).html());
							statistic.setP3(gameDatas.get(3).html());
							statistic.setFt(gameDatas.get(4).html());
							statistic.setOreb(Integer.parseInt(gameDatas.get(5).html()));
							statistic.setDreb(Integer.parseInt(gameDatas.get(6).html()));
							statistic.setReb(Integer.parseInt(gameDatas.get(7).html()));
							statistic.setAst(Integer.parseInt(gameDatas.get(8).html()));
							statistic.setStl(Integer.parseInt(gameDatas.get(9).html()));
							statistic.setBlk(Integer.parseInt(gameDatas.get(10).html()));
							statistic.setFa(Integer.parseInt(gameDatas.get(11).html()));
							statistic.setFo(Integer.parseInt(gameDatas.get(12).html()));
							statistic.setPts(Integer.parseInt(gameDatas.get(13).html()));
							statistic.setGameDate(gameDate);

							calEV(statistic);
							statistics.add(statistic);
						} else if (gameDatas.size() == 2) {
							// 显示没有上场的球员 无统计数据
							Statistic statistic = new Statistic();
							statistic.setPlayerId(Long.parseLong(playerId));
							statistic.setPlayerName(playerName);
							statistic.setGameDate(gameDate);
							statistic.setEv(-5);
							statistics.add(statistic);
						}
					}
				}
			}

			System.out.println("fetch game data complete... size:"+statistics.size());

			if (statistics.size() > 0) {
				// 更新球员工资
				List<Player> updatePlayers = playerRepository.findByPlayerIdIn(updatePlayerIds);
				if (updatePlayers != null && updatePlayers.size() > 0) {
					for (Player player : updatePlayers) {
						for (Statistic statistic : statistics) {
							if (statistic.getPlayerId().longValue() == player.getPlayerId().longValue()) {
								player.setSal(player.getSal() + statistic.getEv());
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
		}
	}

	@Transactional
	@Scheduled(cron = "0 50 14 ? * *")
	public void closeTodayMoney() throws Exception {
		if (fetch) {
			teamRepository.closeMoney(DateTool.getCurrentString(), LACK_PLAYER_PUNISHMENT);
			System.out.println("money closed...");
		}
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

		if (statistic.getMin() == 0) {
			ev = -5;
		}

		statistic.setEv(ev);
	}

	public String getIdFromUrl(String url) {
		int start = url.indexOf("=") + 1;
		String id = url.substring(start, url.length());
		return id;
	}
	
	@Resource
	private PlayerRepository playerRepository;
	@Resource
	private StatisticRepository statisticRepository;
	@Resource
	private TeamRepository teamRepository;
}
