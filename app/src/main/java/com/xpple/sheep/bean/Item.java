package com.xpple.sheep.bean;

import java.util.List;


public class Item extends BaseObject {
    // 发布者 非空 objectId
    private User _User;
    // private User author;
    // 标题 非空
    private String title;
    // 价格 非空
    private String price;
    // 优惠力度
    private String discount;
    // 商品详情
    private String details;
    // 类别 非空 电脑数码 家用电器 户外运动 服饰鞋包 个护化妆 母婴用品
    private String type;
    // 商城名称 非空
    private String mall_name;
    // 标签
    private String label;
    // 浏览量 默认值0
    private Integer hot;
    // 點贊（批判） 默认值0
    private Integer love;
    // 打赏人数
    private Integer reward;
    // 商品主图a 非空
    private String pic_a;
    // 商品輔图b 订单截图
    private String pic_b;
    // 商品輔图c 保留图
    private String pic_c;
    // 商品 ID
    private String item_id;
    // 直達链接 非空
    private String url;
    // 评论数量 默认值0
    private Integer commentCount;
    // 審核状态 默认值 待审核
    private String state;
    // 審核人
    private String admin;

    private List<Item> results;

    public List<Item> getElements() {
        return results;
    }

//
//    public User getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(User author) {
//        this.author = author;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMall_name() {
        return mall_name;
    }

    public void setMall_name(String mall_name) {
        this.mall_name = mall_name;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public String getPic_a() {
        return pic_a;
    }

    public void setPic_a(String pic_a) {
        this.pic_a = pic_a;
    }

    public String getPic_b() {
        return pic_b;
    }

    public void setPic_b(String pic_b) {
        this.pic_b = pic_b;
    }

    public String getPic_c() {
        return pic_c;
    }

    public void setPic_c(String pic_c) {
        this.pic_c = pic_c;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }


    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User get_User() {
        return _User;
    }

    public void set_User(User _User) {
        this._User = _User;
    }
}
