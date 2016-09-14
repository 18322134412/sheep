package com.xpple.sheep.api;


import com.xpple.sheep.bean.BaseObject;
import com.xpple.sheep.bean.Item;
import com.xpple.sheep.bean.ItemUser;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ItemApi {
    //查询单条数据
//    {
//        key1:value1,
//                key2:value2,
//        ...
//    }

    //    @GET("classes/Item/{objectId}")
    @GET("http://115.159.119.41/api/v1/Items/{objectId}")
    Observable<Item> queryItem(@Path("objectId") String objectId);

    //查询多条数据
//    {
//        "results": [
//        {
//            key1:value1,
//                    key2:value2,
//            ...
//        },
//        {
//            key1:value1,
//                    key2:value2,
//            ...
//        },
//        ...

    //项目条件查询

    //        @GET("classes/Item")
    @GET("http://115.159.119.41/api/v1/Items")
    Observable<Item> queryItem(
            @QueryMap Map<String, Object> options);

    //项目和用戶關係条件查询
//    @GET("classes/ItemUser")
    @GET("http://115.159.119.41/api/v1/ItemUser")
    Observable<ItemUser> queryItemUser(
            @QueryMap Map<String, Object> options);

    //添加项目数据
//    {
//        "createdAt": create date,
//        "objectId": objectId
//    }
//    @POST("classes/Items")
    @POST("http://115.159.119.41/api/v1/Items")
    Observable<Item> addItem(@Body Item item);

    //添加项目和用戶關係数据
//    {
//        "createdAt": create date,
//        "objectId": objectId
//    }
//    @POST("classes/ItemUsers")
    @POST("http://115.159.119.41/api/v1/ItemUsers")
    Observable<ItemUser> addItemUser(@Body ItemUser itemUser);

    //更新数据
//    {
//        "updatedAt": "YYYY-mm-dd HH:ii:ss"
//    }
    @PUT("http://115.159.119.41/api/v1/Items/{objectId}")
//    @PUT("classes/Item/{objectId}")
    Observable<BaseObject> updateItem(@Path("objectId") String objectId, @Body Item item);

    //原子计算器
//    body:
//    {
//        key1:{"__op":"Increment","amount":value}
//        ...
//    }
//    {
//        "updatedAt": "YYYY-mm-dd HH:ii:ss"
//    }
    @PUT("http://115.159.119.41/api/v1/Items/{objectId}")
//    @PUT("classes/Item/{objectId}")
    Observable<String> atomicAdd(@Path("objectId") String objectId);


    //删除项目数据
//    {
//        "msg": "ok"
//    }
    @DELETE("http://115.159.119.41/api/v1/Items/{objectId}")
//    @DELETE("classes/Item/{objectId}")
    Observable<BaseObject> deleteItem(@Path("objectId") String objectId);

    //删除项目和用戶關係数据
//    {
//        "msg": "ok"
//    }
    @DELETE("http://115.159.119.41/api/v1/Items/{objectId}")
//    @DELETE("classes/Item/{objectId}")
    Observable<BaseObject> deleteItemUser(@Path("objectId") String objectId);

    //删除字段的值
//    body:
//    {
//        key1:{"__op":"Delete"},
//        key2:{"__op":"Delete"},
//        ...
//    }
//    {
//        "updatedAt": "YYYY-mm-dd HH:ii:ss"
//    }
//    @PUT("http://115.159.119.41/api/v1/{tableName}/{objectId}")
//    Observable<Item> deleteField(@Path("tableName") String tableName, @Path("objectId") String objectId, @Body Item item);
}
