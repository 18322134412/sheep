package com.xpple.sheep.proxy;


import com.xpple.sheep.api.RxFactory;
import com.xpple.sheep.bean.Activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ActivityProxy {
    public static final String LIMIT = "10"; //分页，limit的默认值10项
    private IQueryItemListener queryItemListener;
    private IQueryCountItemListener queryCountItemListener;
    private IQueryMoreItemListener queryMoreItemListener;


    public void QueryItem(boolean isUpdate) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("order", "-updatedAt");
        queryMap.put("limit", LIMIT);
        RxFactory.getPublicServiceInstance(null)
                .queryActivity(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Activitys -> queryItemListener.onQueryItemSuccess(Activitys.getElements(), isUpdate), throwable -> queryItemListener.onQueryItemFailure());
    }

    public void setQueryItemListener(IQueryItemListener queryItemListener) {
        this.queryItemListener = queryItemListener;
    }

    public void QueryCountItem() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("count", "1");
        queryMap.put("limit", "0");
        RxFactory.getPublicServiceInstance(null)
                .queryActivity(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Activitys -> queryCountItemListener.onQueryCountItemSuccess(Activitys.getCount()), throwable -> queryCountItemListener.onQueryCountItemFailure());
    }

    public void setQueryCountItemListener(IQueryCountItemListener queryCountItemListener) {
        this.queryCountItemListener = queryCountItemListener;
    }

    public void QueryMoreItem(int page) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("order", "-updatedAt");
        queryMap.put("limit", LIMIT);
        String skip = String.valueOf(page * 10);
        queryMap.put("skip", skip);
        RxFactory.getPublicServiceInstance(null)
                .queryActivity(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Activitys -> queryMoreItemListener.onQueryMoreItemSuccess(Activitys.getElements()), throwable -> queryMoreItemListener.onQueryMoreItemFailure());
    }

    public void setQueryMoreItemListener(IQueryMoreItemListener queryMoreItemLister) {
        this.queryMoreItemListener = queryMoreItemLister;
    }

    public interface IQueryItemListener {
        void onQueryItemSuccess(List<Activity> objects, boolean isUpdate);
        void onQueryItemFailure();
    }

    public interface IQueryCountItemListener {
        void onQueryCountItemSuccess(String count);

        void onQueryCountItemFailure();
    }

    public interface IQueryMoreItemListener {
        void onQueryMoreItemSuccess(List<Activity> objects);

        void onQueryMoreItemFailure();
    }
}
