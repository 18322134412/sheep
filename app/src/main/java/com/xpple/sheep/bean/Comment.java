package com.xpple.sheep.bean;

import java.util.List;

public class Comment extends BaseObject {
    //评论的項目
    private String itemObjectId;
    //评论的內容
    private String contents;
    //评论的人
    private User author;
    //评论回复数量
    private Integer replyCount;

    public String getItemObjectId() {
        return itemObjectId;
    }

    public void setItemObjectId(String itemObjectId) {
        this.itemObjectId = itemObjectId;
    }

    public String getContent() {
        return contents;
    }

    public void setContent(String content) {
        this.contents = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    private List<Comment> results;

    public List<Comment> getElements() {
        return results;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }
}
