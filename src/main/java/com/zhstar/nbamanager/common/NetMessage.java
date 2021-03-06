package com.zhstar.nbamanager.common;

public class NetMessage {

    public static String DANGER = "danger";
    public static String SUCCESS = "success";
    public static String WARNING = "warning";
    public static String INFO = "info";

    public static String STATUS_ACCOUNT_ERROR = "login_account_error";
    public static String STATUS_ACCOUNT_INVALID = "account_invalid";
    public static String STATUS_INVALID_OPERATION = "invalid_operation";
    public static String STATUS_LOGIN_STATUS_ERROR = "login_status_error";
    public static String STATUS_TEAM_FULL = "team_full";
    public static String STATUS_PLAYER_NOT_EXIST = "player_not_exist";
    public static String STATUS_C_FULL = "c_full";
    public static String STATUS_F_FULL = "f_full";
    public static String STATUS_G_FULL = "g_full";
    public static String STATUS_NOT_ENOUGH_MONEY = "not_enough_money";
    public static String STATUS_OK = "ok";
    public static String STATUS_TRADE_STILL_LIMIT = "trade_still_limit";

    private String type;
    private String content;
    private Object data;

    public NetMessage() {

    }

    public NetMessage(String content, String type) {
        this.content = content;
        this.type = type;
    }

    public NetMessage(String content, String type, Object data) {
        this.content = content;
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


}
