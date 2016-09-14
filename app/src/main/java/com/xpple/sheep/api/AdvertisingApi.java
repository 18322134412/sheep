package com.xpple.sheep.api;


import com.xpple.sheep.bean.Advertising;

import retrofit2.http.GET;
import rx.Observable;

public interface AdvertisingApi {
    //广告
//    @GET("http://192.168.191.1/api/v1/Advertisings")
    @GET("classes/Advertising")
    Observable<Advertising> queryAdvertising();
}
