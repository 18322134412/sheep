package com.xpple.sheep.bean;

import java.util.List;

//項目和用戶的關係表
public class ItemUser extends BaseObject {
    private String itemObjectId;
    private String userObjectId;
    private Boolean love;
    private Boolean dislove;
    private Boolean fav;

    private List<ItemUser> results;

    public List<ItemUser> getElements() {
        return results;
    }

    public String getItemObjectId() {
        return itemObjectId;
    }

    public void setItemObjectId(String itemObjectId) {
        this.itemObjectId = itemObjectId;
    }

    public String getUserObjectId() {
        return userObjectId;
    }

    public void setUserObjectId(String userObjectId) {
        this.userObjectId = userObjectId;
    }

    public Boolean getLove() {
        return love;
    }

    public void setLove(Boolean love) {
        this.love = love;
    }

    public Boolean getDislove() {
        return dislove;
    }

    public void setDislove(Boolean dislove) {
        this.dislove = dislove;
    }

    public Boolean getFav() {
        return fav;
    }

    public void setFav(Boolean fav) {
        this.fav = fav;
    }
}
