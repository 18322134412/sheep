package com.xpple.sheep.proxy;


import com.xpple.sheep.api.RxFactory;
import com.xpple.sheep.bean.CreditsHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreditsDetailsProxy {
    public static final String LIMIT = "10"; //分页，limit的默认值10项
    private IQueryItemListener queryItemListener;
    private IQueryCountItemListener queryCountItemListener;
    private IQueryMoreItemListener queryMoreItemListener;

    public void QueryItem(final boolean isUpdate) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("order", "-updatedAt");
//        String type = "type";
//        String where = "{\"type\":\"" + type + "\"}";
//        queryMap.put("where", where);//{"playerName":"Jonathan Walsh"}
        queryMap.put("limit", LIMIT);
        RxFactory.getPublicServiceInstance(null)
                .queryCreditsDetails(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> queryItemListener.onQueryItemSuccess(Items.getElements(), isUpdate), throwable -> queryItemListener.onQueryItemFailure());
    }

    public void setQueryItemListener(IQueryItemListener queryItemListener) {
        this.queryItemListener = queryItemListener;
    }

    public void QueryCountItem() {
        Map<String, String> queryMap = new HashMap<>();
//        String type = "type";
//        String where = "{\"type\":\"" + type + "\"}";
//        queryMap.put("where", where);//{"playerName":"Jonathan Walsh"}
        queryMap.put("count", "1");
        queryMap.put("limit", "0");
        RxFactory.getPublicServiceInstance(null)
                .queryCreditsDetails(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> queryCountItemListener.onQueryCountItemSuccess(Items.getCount()), throwable -> queryCountItemListener.onQueryCountItemFailure());
    }

    public void setQueryCountItemListener(IQueryCountItemListener queryCountItemListener) {
        this.queryCountItemListener = queryCountItemListener;
    }

    public void QueryMoreItem(int page) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("order", "-updatedAt");
//        String type = "type";
//        String where = "{\"type\":\"" + type + "\"}";
//        queryMap.put("where", where);//{"playerName":"Jonathan Walsh"}
        queryMap.put("limit", LIMIT);
        String skip = String.valueOf(page * 10);
        queryMap.put("skip", skip);
        RxFactory.getPublicServiceInstance(null)
                .queryCreditsDetails(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Items -> queryMoreItemListener.onQueryMoreItemSuccess(Items.getElements()), throwable -> queryMoreItemListener.onQueryMoreItemFailure());
    }

    public void setQueryMoreItemListener(IQueryMoreItemListener queryMoreItemListener) {
        this.queryMoreItemListener = queryMoreItemListener;
    }

    public interface IQueryItemListener {
        void onQueryItemSuccess(List<CreditsHistory> objects, boolean isUpdate);

        void onQueryItemFailure();
    }

    public interface IQueryCountItemListener {
        void onQueryCountItemSuccess(String count);

        void onQueryCountItemFailure();
    }

    public interface IQueryMoreItemListener {
        void onQueryMoreItemSuccess(List<CreditsHistory> objects);

        void onQueryMoreItemFailure();
    }
}
