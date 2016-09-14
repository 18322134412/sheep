package com.xpple.sheep.bean;

import java.util.List;

public class Message extends BaseObject {
    private User belongId;
    private User toldId;
    private boolean isReaded;
    private String content;

    private List<Message> results;

    public List<Message> getElements() {
        return results;
    }

    public User getBelongId() {
        return belongId;
    }

    public void setBelongId(User belongId) {
        this.belongId = belongId;
    }

    public User getToldId() {
        return toldId;
    }

    public void setToldId(User toldId) {
        this.toldId = toldId;
    }

    public boolean getIsReaded() {
        return isReaded;
    }

    public void setIsReaded(boolean isReaded) {
        this.isReaded = isReaded;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
