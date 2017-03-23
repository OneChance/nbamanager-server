package com.zhstar.nbamanager.account.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import com.zhstar.nbamanager.account.entity.Account;
import com.zhstar.nbamanager.common.NetMessage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Component
public class AccountService {

    public Account getById(Long id) throws Exception {
        return accountRepository.getOne(id);
    }

    public void save(Account a) {
        accountRepository.save(a);
    }

    public NetMessage accountInputCheck(Account account) {

        if (account.getName() == null || account.getName().equals("")) {
            return new NetMessage("need_account",NetMessage.DANGER);
        }
        if (account.getPassword() == null || account.getPassword().equals("")) {
        	return new NetMessage("need_password",NetMessage.DANGER);
        }

        return null;
    }

    public NetMessage checkAccount(Account account) throws Exception {

        NetMessage inputRes = accountInputCheck(account);

        if (inputRes!=null) {
            return inputRes;
        }

        Account accountDb = accountRepository.getByName(account.getName());

        if (accountDb != null) {
            return new NetMessage("account_exist",NetMessage.DANGER);
        }

        return null;
    }

    public NetMessage checkLogin(Account account) throws Exception {

        NetMessage inputRes = accountInputCheck(account);

        if (inputRes!=null) {
            return inputRes;
        }

        Account account_db = accountRepository.getByIdentity(account.getName());

        if (account_db == null) {
            return new NetMessage("account_error",NetMessage.DANGER);
        } else {
            if (!account_db.getPassword().equals(account.getPassword())) {
                return new NetMessage("account_error",NetMessage.DANGER);
            }
            account.setId(account_db.getId());
            return null;
        }
    }

    public NetMessage login(Account account, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	NetMessage checkRes = checkLogin(account);
        if (checkRes!=null) {
            return checkRes;
        }
        
        this.LogAccount(request, response, account);
        
        return new NetMessage("ok",NetMessage.SUCCESS);
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
        request.getSession().setAttribute("login_account", account.getId());
    }

    public void clearAccount(HttpServletRequest request, HttpServletResponse response){
        request.getSession().setAttribute("login_account", null);
    }

    public void encrypt(Account account) throws NoSuchAlgorithmException {
        SecureRandom csprng = new SecureRandom();
        byte[] randomBytes = new byte[32];
        csprng.nextBytes(randomBytes);

        String salt = bytes2Hex(randomBytes);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((account.getPassword() + salt).getBytes());
        String password = bytes2Hex(md.digest());

        account.setPassword(password);
        //account.setSalt(salt);
    }

    public String getEncryptedPassword(Account account, String password) throws NoSuchAlgorithmException {
       // String salt = account.getSalt();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        //md.update((password + salt).getBytes());
        return bytes2Hex(md.digest());
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

    @Resource
    private AccountRepository accountRepository;
}
