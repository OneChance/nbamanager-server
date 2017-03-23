package com.zhstar.nbamanager.team.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhstar.nbamanager.account.entity.Account;
import com.zhstar.nbamanager.account.service.AccountService;
import com.zhstar.nbamanager.common.NetMessage;
import com.zhstar.nbamanager.team.service.TeamService;


@RestController
public class TeamController {

    @RequestMapping("/getTeamInfo/")
    public NetMessage getTeamInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Account account = accountService.getLoginAccount(request, response);
    	return new NetMessage(NetMessage.STATUS_OK, NetMessage.SUCCESS,teamService.getTeamByUser(account.getId()));
    } 

    @Resource
    AccountService accountService;
    @Resource
    TeamService teamService;
    
}
