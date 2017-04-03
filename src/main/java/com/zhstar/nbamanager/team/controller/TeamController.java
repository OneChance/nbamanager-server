package com.zhstar.nbamanager.team.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhstar.nbamanager.account.entity.Account;
import com.zhstar.nbamanager.account.service.AccountService;
import com.zhstar.nbamanager.common.NetMessage;
import com.zhstar.nbamanager.player.entity.Player;
import com.zhstar.nbamanager.team.entity.Team;
import com.zhstar.nbamanager.team.service.TeamService;


@RestController
public class TeamController {

    @RequestMapping("/getTeamInfo/")
    public NetMessage getTeamInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Account account = accountService.getLoginAccount(request, response);
    	return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS,teamService.getTeamByUser(account.getId(),true));
    }
    
    @RequestMapping("/signPlayer/")
    public NetMessage signPlayer(@RequestBody Player player, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Account account = accountService.getLoginAccount(request, response);
    	return teamService.signPlayer(account.getId(), player);
    }
    
    @RequestMapping("/breakPlayer/")
    public NetMessage breakPlayer(@RequestBody Player player, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Account account = accountService.getLoginAccount(request, response);
    	return teamService.breakPlayer(account.getId(), player);
    }
    
    @RequestMapping("/changePlayerPos/")
    public NetMessage changePlayerPos(@RequestBody Player player, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Account account = accountService.getLoginAccount(request, response);
    	return teamService.changePlayerPos(account.getId(), player);
    }
    
    @RequestMapping("/changeTeamName/")
    public NetMessage changeTeamName(@RequestBody Team team, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Account account = accountService.getLoginAccount(request, response);
    	return teamService.changeTeamName(account.getId(),team.getName());
    }

    @RequestMapping("/getContractLog/{page}")
    public NetMessage getContractLog(@PathVariable("page") int page,HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Account account = accountService.getLoginAccount(request, response);
    	String searchName = request.getParameter("searchName");
    	return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS,teamService.getContractLogs(account.getId(),searchName,page));
    }
    
    @Resource
    AccountService accountService;
    @Resource
    TeamService teamService;
    
}
