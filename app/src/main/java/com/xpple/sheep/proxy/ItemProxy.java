package com.xpple.sheep.proxy;


import com.xpple.sheep.api.RxFactory;
import com.xpple.sheep.bean.Item;
import com.xpple.sheep.bean.ItemUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ItemProxy {
    public static final int LIMIT = 10; //分页，limit的默认值10项
    private IQueryItemListener queryItemListener;
    private IQueryCountItemListener queryCountItemListener;
    private IQueryMoreItemListener queryMoreItemListener;
    private IQueryItemUserListener queryItemUserListener;
    private IDeleteItemListener deleteItemListener;
    private IAddItemUserListener addItemUserListener;

    public void QueryItem(boolean isUpdate, Map<String, Object> queryMap) {
        queryMap.put("order", "-updatedAt");
        queryMap.put("limit", LIMIT);
        queryMap.put("page", 1);
        RxFactory.getItemServiceInstance(null)
                .queryItem(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> queryItemListener.onQueryItemSuccess(Items.getElements(), isUpdate), throwable -> queryItemListener.onQueryItemFailure());
    }

    public void setQueryItemListener(IQueryItemListener queryItemListener) {
        this.queryItemListener = queryItemListener;
    }

    public void QueryCountItem(Map<String, Object> queryMap) {
        queryMap.put("count", "1");
        queryMap.put("limit", "0");
        RxFactory.getItemServiceInstance(null)
                .queryItem(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> queryCountItemListener.onQueryCountItemSuccess(Items.getCount()), throwable -> queryCountItemListener.onQueryCountItemFailure());
    }

    public void setQueryCountItemListener(IQueryCountItemListener queryCountItemListener) {
        this.queryCountItemListener = queryCountItemListener;
    }

    public void QueryMoreItem(int page, Map<String, Object> queryMap) {
        queryMap.put("order", "-updatedAt");
        queryMap.put("limit", LIMIT);
        queryMap.put("page", page);
        RxFactory.getItemServiceInstance(null)
                .queryItem(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> queryMoreItemListener.onQueryMoreItemSuccess(Items.getElements()), throwable -> queryMoreItemListener.onQueryMoreItemFailure(throwable.getMessage()));
    }

    public void setQueryMoreItemListener(IQueryMoreItemListener queryMoreItemLister) {
        this.queryMoreItemListener = queryMoreItemLister;
    }

    public void QueryItemUser(String itemObjectId, String userObjectId) {
        Map<String, Object> queryMap = new HashMap<>();
        String where = "\"$and\":[{\"itemObjectId\":" + itemObjectId + "\"},{\"userObjectId\":" + userObjectId + "}}]";
        queryMap.put("where", where);
        RxFactory.getItemServiceInstance(null)
                .queryItemUser(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ItemUsers -> queryItemUserListener.onQueryItemUserSuccess(ItemUsers.getElements()), throwable -> queryItemUserListener.onQueryItemUserFailure(throwable.getMessage()));
    }

    public void setQueryItemUserListener(IQueryItemUserListener queryItemUserListener) {
        this.queryItemUserListener = queryItemUserListener;
    }

    public void addItemUser(String session, ItemUser itemUser) {
        RxFactory.getItemServiceInstance(session)
                .addItemUser(itemUser)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObject -> addItemUserListener.onAddItemUserSuccess(baseObject), throwable -> addItemUserListener.onAddItemUserFailure(throwable.getMessage()));
    }

    public void setAddItemUserListener(IAddItemUserListener addItemUserListener) {
        this.addItemUserListener = addItemUserListener;
    }

    public void deleteItem(String session, String objectId) {
        RxFactory.getItemServiceInstance(session)
                .deleteItem(objectId)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObject -> deleteItemListener.onDeleteItemSuccess(), throwable -> deleteItemListener.onDeleteItemFailure());
    }

    public void setDeleteItemListener(IDeleteItemListener deleteItemListener) {
        this.deleteItemListener = deleteItemListener;
    }

    public void atomicAdd(String objectId) {
        RxFactory.getItemServiceInstance(null)
                .atomicAdd(objectId)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();//人气增长是隐型调用的自增，不需要回调
    }

    public interface IQueryItemListener {
        void onQueryItemSuccess(List<Item> objects, boolean isUpdate);

        void onQueryItemFailure();
    }

    public interface IQueryCountItemListener {
        void onQueryCountItemSuccess(String count);

        void onQueryCountItemFailure();
    }

    public interface IQueryMoreItemListener {
        void onQueryMoreItemSuccess(List<Item> objects);

        void onQueryMoreItemFailure(String message);
    }

    public interface IQueryItemUserListener {
        void onQueryItemUserSuccess(List<ItemUser> objects);

        void onQueryItemUserFailure(String message);
    }

    public interface IAddItemUserListener {
        void onAddItemUserSuccess(ItemUser objects);

        void onAddItemUserFailure(String message);
    }

    public interface IDeleteItemListener {
        void onDeleteItemSuccess();

        void onDeleteItemFailure();
    }
}
