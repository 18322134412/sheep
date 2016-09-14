package com.xpple.sheep.api;


import com.xpple.sheep.StaticConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RxService<T> {

    public static final String BASE_URL = "https://api.bmob.cn/1/";

    //Client
    private static OkHttpClient genericClient(String session) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("X-Bmob-Application-Id", StaticConfig.BMOB_KEY)
                            .addHeader("X-Bmob-REST-API-Key", StaticConfig.BMOB_REST_API_Key)
                            .addHeader("X-Bmob-Session-Token", session == null ? "" : session)
                            .addHeader("Session-Token", session == null ? "" : session)
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    T mService = null;

    RxService(Class<T> clazz, String session) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(genericClient(session))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(clazz);
    }

    public T getService() {
        return mService;
    }
}
