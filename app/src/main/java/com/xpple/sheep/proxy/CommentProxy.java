package com.xpple.sheep.proxy;


import com.google.gson.JsonObject;
import com.xpple.sheep.api.RxFactory;
import com.xpple.sheep.bean.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CommentProxy {
    public static final String LIMIT = "10"; //分页，limit的默认值10项
    private IQueryItemListener queryItemListener;
    private IQueryCountItemListener queryCountItemListener;
    private IQueryMoreItemListener queryMoreItemListener;
    private IAddItemListener addItemListener;
    private IDeleteItemListener deleteItemListener;

    public void QueryItem(boolean isUpdate, boolean isOrder, String itemObjectId) {
        Map<String, String> queryMap = new HashMap<>();
        if (isOrder) {
            queryMap.put("order", "updatedAt");
        } else {
            queryMap.put("order", "-updatedAt");
        }
        queryMap.put("include", "author[objectId|nickname|avatar]");
        String where = "{\"itemObjectId\":\"" + itemObjectId + "\"}";
        queryMap.put("where", where);
        queryMap.put("limit", LIMIT);
        RxFactory.getCommentServiceInstance(null)
                .queryComment(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> queryItemListener.onQueryItemSuccess(Items.getElements(), isUpdate), throwable -> queryItemListener.onQueryItemFailure());
    }

    public void setQueryItemListener(IQueryItemListener queryItemListener) {
        this.queryItemListener = queryItemListener;
    }

    public void QueryCountItem(String itemObjectId) {
        Map<String, String> queryMap = new HashMap<>();
        String where = "{\"itemObjectId\":\"" + itemObjectId + "\"}";
        queryMap.put("where", where);
        queryMap.put("count", "1");
        queryMap.put("limit", "0");
        RxFactory.getCommentServiceInstance(null)
                .queryComment(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> queryCountItemListener.onQueryCountItemSuccess(Items.getCount()), throwable -> queryCountItemListener.onQueryCountItemFailure());
    }

    public void setQueryCountItemListener(IQueryCountItemListener queryCountItemListener) {
        this.queryCountItemListener = queryCountItemListener;
    }

    public void QueryMoreItem(int page, boolean isOrder, String itemObjectId) {
        Map<String, String> queryMap = new HashMap<>();
        if (isOrder) {
            queryMap.put("order", "updatedAt");
        } else {
            queryMap.put("order", "-updatedAt");
        }
        queryMap.put("include", "author[objectId|nickname|avatar]");
        String where = "{\"itemObjectId\":\"" + itemObjectId + "\"}";
        queryMap.put("where", where);
        queryMap.put("limit", LIMIT);
        String skip = String.valueOf(page * 10);
        queryMap.put("skip", skip);
        RxFactory.getCommentServiceInstance(null)
                .queryComment(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> queryMoreItemListener.onQueryMoreItemSuccess(Items.getElements()), throwable -> queryMoreItemListener.onQueryMoreItemFailure());
    }

    public void setQueryMoreItemListener(IQueryMoreItemListener queryMoreItemListener) {
        this.queryMoreItemListener = queryMoreItemListener;
    }

    public void addItem(JsonObject comment, String mContent) {
        RxFactory.getCommentServiceInstance(null)
                .addComment(comment)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> addItemListener.onAddItemSuccess(mContent), throwable -> addItemListener.onAddItemFailure());
    }


    public void setAddItemListener(IAddItemListener addItemListener) {
        this.addItemListener = addItemListener;
    }

    public void deleteItem(String objectId, String session) {
        RxFactory.getCommentServiceInstance(session)
                .deleteComment(objectId)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> deleteItemListener.onDeleteItemSuccess(), throwable -> deleteItemListener.onDeleteItemFailure());
    }


    public void setDeleteItemListener(IDeleteItemListener deleteItemListener) {
        this.deleteItemListener = deleteItemListener;
    }

    public interface IQueryItemListener {
        void onQueryItemSuccess(List<Comment> objects, boolean isUpdate);

        void onQueryItemFailure();
    }

    public interface IQueryCountItemListener {
        void onQueryCountItemSuccess(String count);

        void onQueryCountItemFailure();
    }

    public interface IQueryMoreItemListener {
        void onQueryMoreItemSuccess(List<Comment> objects);

        void onQueryMoreItemFailure();
    }

    public interface IAddItemListener {
        void onAddItemSuccess(String mContent);

        void onAddItemFailure();
    }

    public interface IDeleteItemListener {
        void onDeleteItemSuccess();

        void onDeleteItemFailure();
    }
}
