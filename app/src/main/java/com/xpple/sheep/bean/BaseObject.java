package com.xpple.sheep.bean;

import java.io.Serializable;

public class BaseObject implements Serializable {
    private String objectId;//唯一键 uuid
    private String createdAt;//YYYY-mm-dd HH:ii:ss
    private String updatedAt;//YYYY-mm-dd HH:ii:ss

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    private String count;

    public String getCount() {
        return count;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

}
