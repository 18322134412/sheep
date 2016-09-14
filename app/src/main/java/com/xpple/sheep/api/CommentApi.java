package com.xpple.sheep.api;


import com.google.gson.JsonObject;
import com.xpple.sheep.bean.BaseObject;
import com.xpple.sheep.bean.Comment;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface CommentApi {

    //评论条件查询
    @GET("http://192.168.191.1/api/v1/Comments")
    Observable<Comment> queryComment(
            @QueryMap Map<String, String> options);


    //添加数据
//    {
//        "createdAt": create date,
//        "objectId": objectId
//    }
    @POST("http://192.168.191.1/api/v1/Comments")
    Observable<BaseObject> addComment(@Body JsonObject comment);

    //更新数据
//    {
//        "updatedAt": "YYYY-mm-dd HH:ii:ss"
//    }
    @PUT("http://192.168.191.1/api/v1/Comments/{objectId}")
    Observable<BaseObject> updateComment(@Path("objectId") String objectId, @Body Comment comment);

    //原子计算器
//    body:
//    {
//        key1:{"__op":"Increment","amount":value}
//        ...
//    }
//    {
//        "updatedAt": "YYYY-mm-dd HH:ii:ss"
//    }
    @PUT("http://192.168.191.1/api/v1/Comments/{objectId}")
    Observable<BaseObject> atomicAdd(@Path("objectId") String objectId, @Body JsonObject jsonObject);

    //删除数据
//    {
//        "msg": "ok"
//    }
    @DELETE("http://192.168.191.1/api/v1/Comments/{objectId}")
    Observable<BaseObject> deleteComment(@Path("objectId") String objectId);

}
