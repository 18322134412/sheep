package com.xpple.sheep.proxy;


import com.xpple.sheep.api.RxFactory;
import com.xpple.sheep.bean.Notice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NoticeProxy {
    public static final String LIMIT = "10"; //分页，limit的默认值10项
    private IQueryItemListener queryItemLister;
    private IQueryCountItemListener queryCountItemLister;
    private IQueryMoreItemListener queryMoreItemLister;

    public void QueryItem(final boolean isUpdate) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("order", "-updatedAt");
        queryMap.put("limit", LIMIT);
        RxFactory.getPublicServiceInstance(null)
                .queryNotice(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Notices -> queryItemLister.onQueryItemSuccess(Notices.getElements(), isUpdate), throwable -> queryItemLister.onQueryItemFailure());
    }

    public void setQueryItemListener(IQueryItemListener queryItemLister) {
        this.queryItemLister = queryItemLister;
    }

    public void QueryCountOrder() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("count", "1");
        queryMap.put("limit", "0");
        RxFactory.getPublicServiceInstance(null)
                .queryNotice(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Notices -> queryCountItemLister.onQueryCountOrderSuccess(Notices.getCount()), throwable -> queryCountItemLister.onQueryCountOrderFailure());
    }

    public void setQueryCountItemListener(IQueryCountItemListener queryCountItemListener) {
        this.queryCountItemLister = queryCountItemListener;
    }

    public void QueryMoreItem(int page) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("order", "-updatedAt");
        queryMap.put("limit", LIMIT);
        String skip = String.valueOf(page * 10);
        queryMap.put("skip", skip);
        RxFactory.getPublicServiceInstance(null)
                .queryNotice(queryMap)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Notices -> queryMoreItemLister.onQueryMoreItemSuccess(Notices.getElements()), throwable -> queryMoreItemLister.onQueryMoreItemFailure());
    }

    public void setQueryMoreItemListener(IQueryMoreItemListener queryMoreItemLister) {
        this.queryMoreItemLister = queryMoreItemLister;
    }

    public interface IQueryItemListener {
        void onQueryItemSuccess(List<Notice> objects, boolean isUpdate);

        void onQueryItemFailure();
    }

    public interface IQueryCountItemListener {
        void onQueryCountOrderSuccess(String count);

        void onQueryCountOrderFailure();
    }

    public interface IQueryMoreItemListener {
        void onQueryMoreItemSuccess(List<Notice> objects);

        void onQueryMoreItemFailure();
    }
}
