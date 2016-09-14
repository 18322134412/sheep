package com.xpple.sheep.api;


import android.text.TextUtils;

public class RxFactory {
    private static UserApi mUserApi;
    private static ItemApi mItemApi;
    private static PublicApi mPublicApi;
    private static AdvertisingApi mAdvertisingApi;
    private static CommentApi mCommentApi;

    protected static final Object monitor = new Object();

    public static UserApi getUserServiceInstance(String session) {
        synchronized (monitor) {
            if (mUserApi == null || !TextUtils.isEmpty(session)) {
                mUserApi = new RxService<>(UserApi.class, session).getService();
            }
            return mUserApi;
        }
    }

    public static ItemApi getItemServiceInstance(String session) {
        synchronized (monitor) {
            if (mItemApi == null || !TextUtils.isEmpty(session)) {
                mItemApi = new RxService<>(ItemApi.class, session).getService();
            }
            return mItemApi;
        }
    }

    public static CommentApi getCommentServiceInstance(String session) {
        synchronized (monitor) {
            if (mCommentApi == null || !TextUtils.isEmpty(session)) {
                mCommentApi = new RxService<>(CommentApi.class, session).getService();
            }
            return mCommentApi;
        }
    }

    public static PublicApi getPublicServiceInstance(String session) {
        synchronized (monitor) {
            if (mPublicApi == null || !TextUtils.isEmpty(session)) {
                mPublicApi = new RxService<>(PublicApi.class, session).getService();
            }
            return mPublicApi;
        }
    }

    public static AdvertisingApi getAdvertisingServiceInstance(String session) {
        synchronized (monitor) {
            if (mAdvertisingApi == null || !TextUtils.isEmpty(session)) {
                mAdvertisingApi = new RxService<>(AdvertisingApi.class, session).getService();
            }
            return mAdvertisingApi;
        }
    }
}
