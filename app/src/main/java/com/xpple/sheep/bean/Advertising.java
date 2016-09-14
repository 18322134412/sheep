package com.xpple.sheep.bean;
import java.util.List;

//广告
public class Advertising extends BaseObject {
    //標題
    private String title;
    //連接
    private String url;
    //圖片
    private String pic;

    private List<Advertising> results;

    public List<Advertising> getElements() {
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
