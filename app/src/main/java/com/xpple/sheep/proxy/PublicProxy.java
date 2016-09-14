package com.xpple.sheep.proxy;


import com.xpple.sheep.api.RxFactory;
import com.xpple.sheep.bean.Timestamp;
import com.xpple.sheep.bean.User;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PublicProxy {
    private IGetTimestampListener getTimestampListener;
    //    private IGetQrListener getQrLister;

    //推送消息
    public void pushMessage(String phone, String message) {
        RxFactory.getPublicServiceInstance(null)
                .getTimestamp()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(timestamp -> getTimestampListener.onGetTimestampSuccess(timestamp), throwable -> getTimestampListener.onGetTimestampFailure());
    }

    //获取服务器时间
    public void getTimestamp(String phone, String password) {
        User user = new User();
        user.setUsername(phone);
        user.setPassword(password);
        RxFactory.getPublicServiceInstance(null)
                .getTimestamp()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(timestamp -> getTimestampListener.onGetTimestampSuccess(timestamp), throwable -> getTimestampListener.onGetTimestampFailure());
    }

    public void setGetTimestampListener(IGetTimestampListener getTimestampLister) {
        this.getTimestampListener = getTimestampLister;
    }

    public interface IGetTimestampListener {
        void onGetTimestampSuccess(Timestamp timestamp);

        void onGetTimestampFailure();
    }

//    public void getQr(String phone, String password) {
//        User user = new User();
//        user.setUsername(phone);
//        user.setPassword(password);
//        newPublicApiService().getTimestamp()
//                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(timestamp -> getQrLister.onGetQrSuccess(timestamp), throwable -> getQrLister.onGetQrFailure());
//    }
//
//    public void setGetQrListener(IGetQrListener getQrLister) {
//        this.getQrLister = getQrLister;
//    }
//
//    public interface IGetQrListener {
//        void onGetQrSuccess(String qr);
//
//        void onGetQrFailure();
//    }
}
