package com.xpple.sheep.bean;
import java.util.List;

//通知公告
public class Notice extends BaseObject {
    //標題
    private String title;
    //連接
    private String url;
    //內容
    private String content;

    private List<Notice> results;

    public List<Notice> getElements() {
        return results;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
