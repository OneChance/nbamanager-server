package com.zhstar.nbamanager.account.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zhstar.nbamanager.account.entity.Account;
import com.zhstar.nbamanager.common.CookieTool;
import com.zhstar.nbamanager.common.MD5Tool;
import com.zhstar.nbamanager.common.NetMessage;
import com.zhstar.nbamanager.team.entity.Arena;
import com.zhstar.nbamanager.team.entity.Team;
import com.zhstar.nbamanager.team.entity.TeamPlayer;
import com.zhstar.nbamanager.team.service.TeamRepository;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;

@Component
public class AccountService {

	public Account getById(Long id) throws Exception {
		return accountRepository.getOne(id);
	}
	
	public Account getByName(String name){
		return accountRepository.getByName(name);
	}

	public void save(Account a) {
		accountRepository.save(a);
	}

	public boolean inputCheck(Account account) {

		if (account.getName() == null || account.getName().equals("")) {
			return false;
		}
		if (account.getPassword() == null || account.getPassword().equals("")) {
			return false;
		}

		return true;
	}

	public NetMessage signUpValidation(Account account) throws Exception {

		boolean valid = inputCheck(account);

		if (!valid) {
			return new NetMessage(NetMessage.STATUS_ACCOUNT_ERROR, NetMessage.DANGER);
		}

		Account accountDb = accountRepository.getByName(account.getName());

		if (accountDb != null) {
			return new NetMessage(NetMessage.STATUS_ACCOUNT_INVALID, NetMessage.DANGER);
		}

		return null;
	}

	public Account signInValidation(Account account) throws Exception {

		boolean valid = inputCheck(account);

		if (!valid) {
			return null;
		}

		Account accountDb = accountRepository.getByIdentity(account.getName());

		if (accountDb == null) {
			return null;
		} else {
			if (!accountDb.getPassword().equals(MD5Tool.getEncrypted(account.getPassword() + accountDb.getSalt()))) {
				return null;
			}
			return accountDb;
		}
	}

	@Transactional
	public NetMessage signIn(Account account, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Account accountDb = signInValidation(account);
		if (accountDb == null) {
			return new NetMessage(NetMessage.STATUS_ACCOUNT_ERROR, NetMessage.DANGER);
		}

		this.LogAccount(request, response, accountDb);

		return new NetMessage("ok", NetMessage.SUCCESS);
	}

	@Transactional
	public NetMessage signUp(Account account, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		NetMessage message = signUpValidation(account);
		if (message != null) {
			return message;
		}

		encrypt(account);
		Account accountDb = accountRepository.save(account);
		this.LogAccount(request, response, accountDb);
		
		Team team = new Team();
		team.setUserId(accountDb.getId());
		team.setMoney(10000000);
		team.setName(account.getName()+"的球队");
		team.setArena(new Arena(account.getName()+"的球馆"));
		team.setPlayers(new ArrayList<TeamPlayer>());
		teamRepository.save(team);

		return new NetMessage("ok", NetMessage.SUCCESS);
	}
	
	public NetMessage signOut(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		this.clearAccount(request, response);
		return new NetMessage("ok", NetMessage.SUCCESS);
	}

	/**
	 * <p>
	 * Description:get current login account
	 * </p>
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public Account getLoginAccount(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Object accountId = request.getSession().getAttribute("login_account");
		if (accountId != null) {
			Account account = getById(Long.parseLong(accountId.toString()));
			return account;
		}
		return null;
	}

	/**
	 * <p>
	 * Description:record session and cookie
	 * </p>
	 *
	 * @param request
	 * @param response
	 * @param account
	 * @throws Exception
	 */
	public void LogAccount(HttpServletRequest request, HttpServletResponse response, Account account) throws Exception {
		String tokenOri = account.getName() + new Date().getTime() + CookieTool.getIpAddr(request);
		String token = MD5Tool.getEncrypted(tokenOri);
		account.setToken(token);
		CookieTool.setCookies(response, "nbam_c", account.getName()+":"+token);
		request.getSession().setAttribute("login_account", account.getId());
	}

	public void clearAccount(HttpServletRequest request, HttpServletResponse response) {
		CookieTool.cleanCookies(response, "nbam_c");
		request.getSession().setAttribute("login_account", null);
	}

	public void encrypt(Account account) throws NoSuchAlgorithmException {
		SecureRandom csprng = new SecureRandom();
		byte[] randomBytes = new byte[32];
		csprng.nextBytes(randomBytes);
		String salt = MD5Tool.bytes2Hex(randomBytes);
		account.setPassword(MD5Tool.getEncrypted(account.getPassword() + salt));
		account.setSalt(salt);
	}

	@Resource
	private AccountRepository accountRepository;
	@Resource
	private TeamRepository teamRepository;
}
