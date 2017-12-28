package com.zhstar.nbamanager.statistic.entity;

public class GameEv {
    private String date;
    private String ev;

    public GameEv() {

    }

    public GameEv(String date, String ev) {
        this.date = date;
        this.ev = ev;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEv() {
        return ev;
    }

}
