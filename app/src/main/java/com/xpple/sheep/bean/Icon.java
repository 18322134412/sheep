package com.xpple.sheep.bean;


public class Icon extends BaseObject {
    // 图标分类
    private String type;
    // 图标活动及编号
    private String content;
    // 图标地址
    private String url;

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


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
