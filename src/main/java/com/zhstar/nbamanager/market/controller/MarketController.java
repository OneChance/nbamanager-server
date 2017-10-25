package com.zhstar.nbamanager.market.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhstar.nbamanager.account.entity.Account;
import com.zhstar.nbamanager.account.service.AccountService;
import com.zhstar.nbamanager.common.NetMessage;
import com.zhstar.nbamanager.market.service.MarketService;
import com.zhstar.nbamanager.player.entity.Player;

@RestController
public class MarketController {

	@RequestMapping("/getMarketPlayer/{page}/")
	public NetMessage getMarketPlayer(@PathVariable("page") int page,HttpServletRequest request, HttpServletResponse response) throws Exception {
		Account account = accountService.getLoginAccount(request, response);
		String searchName = request.getParameter("searchName");
		String searchPos = request.getParameter("searchPos");
		List<Player> players = new ArrayList<Player>();

		players = marketService.getMarketPlayer(account,searchName,searchPos, page);

		return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS,players);
	}

	@Resource
	MarketService marketService;
	@Resource
	AccountService accountService;
}
