package com.xpple.sheep.bean;


import java.util.List;

public class OverageHistory extends BaseObject {
    // 自己 1-1
    private User selfUser;
    // 來源人 1-1
    private User originUser;
    // 获益來源內容:打賞收入，打賞支出，提現支出
    private String body;
    // 收益改變前數值
    private Integer before;
    // 收益改編後數值
    private Integer after;
    // 改變的數值
    private Integer change;

    public User getSelfUser() {
        return selfUser;
    }

    public void setSelfUser(User selfUser) {
        this.selfUser = selfUser;
    }

    public User getOriginUser() {
        return originUser;
    }

    public void setOriginUser(User originUser) {
        this.originUser = originUser;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getBefore() {
        return before;
    }

    public void setBefore(Integer before) {
        this.before = before;
    }

    public Integer getAfter() {
        return after;
    }

    public void setAfter(Integer after) {
        this.after = after;
    }

    public Integer getChange() {
        return change;
    }

    public void setChange(Integer change) {
        this.change = change;
    }

    private List<OverageHistory> results;

    public List<OverageHistory> getElements() {
        return results;
    }
}
