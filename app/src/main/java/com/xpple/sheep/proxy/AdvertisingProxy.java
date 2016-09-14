package com.xpple.sheep.proxy;


import com.xpple.sheep.api.RxFactory;
import com.xpple.sheep.bean.Advertising;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AdvertisingProxy {
    private IQueryAdvertisingListener queryAdvertisingLister;

    public void QueryAdvertising() {
        RxFactory.getAdvertisingServiceInstance(null)
                .queryAdvertising()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Advertising -> queryAdvertisingLister.onQueryAdvertisingSuccess(Advertising.getElements()), throwable -> queryAdvertisingLister.onQueryAdvertisingFailure());
    }

    public void setQueryAdvertisingListener(IQueryAdvertisingListener queryAdvertisingLister) {
        this.queryAdvertisingLister = queryAdvertisingLister;
    }

    public interface IQueryAdvertisingListener {
        void onQueryAdvertisingSuccess(List<Advertising> object);

        void onQueryAdvertisingFailure();
    }
}
