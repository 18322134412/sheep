package com.xpple.sheep.proxy;


import com.xpple.sheep.api.RxFactory;
import com.xpple.sheep.bean.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MessageProxy {
    public static final String LIMIT = "10"; //分页，limit的默认值10项
    private IQueryMessageListener queryOrderLister;
    private IQueryCountMessageListener queryCountOrderLister;
    private IQueryMoreMessageListener queryMoreMessageLister;

    public void QueryOrder(final boolean isUpdate) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("order", "-updatedAt");
        queryMap.put("limit", LIMIT);
        RxFactory.getPublicServiceInstance(null)
                .queryMessage(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Messages -> queryOrderLister.onQueryMessageSuccess(Messages.getElements(), isUpdate), throwable -> queryOrderLister.onQueryMessageFailure());
    }

    public void setQueryOrderListener(IQueryMessageListener queryOrderLister) {
        this.queryOrderLister = queryOrderLister;
    }

    public void QueryCountOrder() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("count", "1");
        queryMap.put("limit", "0");
        RxFactory.getPublicServiceInstance(null)
                .queryMessage(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Messages -> queryCountOrderLister.onQueryCountMessageSuccess(Messages.getCount()), throwable -> queryCountOrderLister.onQueryCountMessageFailure());
    }

    public void setQueryCountOrderListener(IQueryCountMessageListener queryCountOrderLister) {
        this.queryCountOrderLister = queryCountOrderLister;
    }

    public void QueryMoreOrder(int page) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("order", "-updatedAt");
        queryMap.put("limit", LIMIT);
        String skip = String.valueOf(page * 10);
        queryMap.put("skip", skip);
        RxFactory.getPublicServiceInstance(null)
                .queryMessage(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Messages -> queryMoreMessageLister.onQueryMoreMessageSuccess(Messages.getElements()), throwable -> queryMoreMessageLister.onQueryMoreMessageFailure());
    }

    public void setQueryMoreOrderListener(IQueryMoreMessageListener queryMoreMessageLister) {
        this.queryMoreMessageLister = queryMoreMessageLister;
    }

    public interface IQueryMessageListener {
        void onQueryMessageSuccess(List<Message> objects, boolean isUpdate);

        void onQueryMessageFailure();
    }

    public interface IQueryCountMessageListener {
        void onQueryCountMessageSuccess(String count);

        void onQueryCountMessageFailure();
    }

    public interface IQueryMoreMessageListener {
        void onQueryMoreMessageSuccess(List<Message> objects);

        void onQueryMoreMessageFailure();
    }
}
