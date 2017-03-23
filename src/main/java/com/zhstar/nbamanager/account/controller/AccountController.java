package com.zhstar.nbamanager.account.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhstar.nbamanager.account.entity.Account;
import com.zhstar.nbamanager.account.service.AccountService;
import com.zhstar.nbamanager.common.NetMessage;


@RestController
public class AccountController {

    @RequestMapping("/login/")
    public NetMessage login(@RequestBody Account account, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return accountService.login(account, request, response);
    }
    
    @RequestMapping("/isLogin/")
    public NetMessage isLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return new NetMessage("", NetMessage.SUCCESS);
    }

    @Resource
    AccountService accountService;
}
