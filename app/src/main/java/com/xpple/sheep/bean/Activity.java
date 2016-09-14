package com.xpple.sheep.bean;
import java.util.List;

//活动
public class Activity extends BaseObject {
    //活動標題
    private String title;
    //活動連接地址
    private String url;
    //活動圖片
    private String pic;
    //活動结束时间
    private String timestamp;

    private List<Activity> results;

    public List<Activity> getElements() {
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }


}
