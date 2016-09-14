package com.xpple.sheep.api;


import com.xpple.sheep.bean.BaseObject;
import com.xpple.sheep.bean.User;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

//曹林服务器 115.159.119.41
public interface UserApi {
    //注册
//    {
//            "createdAt": YYYY-mm-dd HH:ii:ss,
//            "objectId": objectId,
//            "sessionToken": sessionToken,
//    }
//    @POST("users")
    @POST("http://115.159.119.41/api/v1/users")
    Observable<User> signUp(@Body User user, @Query("code") String code);

    //登陆
//    {
//        "username": username,
//            "mobilePhoneNumber": mobilePhoneNumber,
//            "mobilePhoneVerified": boolValue,
//            "createdAt": YYYY-mm-dd HH:ii:ss,
//            "updatedAt": YYYY-mm-dd HH:ii:ss,
//            "objectId": objectId,
//            "sessionToken": sessionToekn,
//            key1:value1,
//            key2:value2,
//        ...
//    }

    //    @GET("login")
    @GET("http://115.159.119.41/api/v1/users")
    Observable<User> login(@Query("username") String username, @Query("password") String password);
//    一个可行的方法是，客户端提交 md5(password) 密码（如上所述，此方法只是简单保护了密码，是可能被查表获取密码的）。服务端数据库通过 md5(salt+md5(password)) 的规则存储密码，该 salt 仅存储在服务端，且在每次存储密码时都随机生成。这样即使被拖库，制作字典的成本也非常高。
//    密码被 md5() 提交到服务端之后，可通过 md5(salt + form['password']) 与数据库密码比对。此方法可以在避免明文存储密码的前提下，实现密码加密提交与验证。
//    这里还有防止 replay 攻击（请求被重新发出一次即可能通过验证）的问题，由服务端颁发并验证一个带有时间戳的可信 token （或一次性的）即可。
//    当然，传输过程再有 HTTPS 加持那就更好了。
    //获取当前用户
//    {
//        "username": username,
//            "createdAt": YYYY-mm-dd HH:ii:ss,
//            "updatedAt": YYYY-mm-dd HH:ii:ss,
//            "objectId": objectId
//    }

    // 查询所有用户
//   {
//       {
//           "results": [
//           {
//               "username": username,
//                   "createdAt": YYYY-mm-dd HH:ii:ss,
//                   "updatedAt": YYYY-mm-dd HH:ii:ss,
//                   "objectId": objectId,
//                   key1:value1,
//               ...
//           },
//           {
//               "username": username,
//                   "createdAt": YYYY-mm-dd HH:ii:ss,
//                   "updatedAt": YYYY-mm-dd HH:ii:ss,
//                   "objectId": objectId,
//                   key1:value1,
//               ...
//           },
//           ...
//           ]
//       }
//   }

    @GET("http://115.159.119.41/v1/users")
    Observable<User> gerAllUser(@Query("include") String include);

    @GET("http://115.159.119.41/api/v1/users/{objectId}")
    Observable<User> getUser(@Path("objectId") String objectId);

    @GET("http://115.159.119.41/api/v1/users/{objectId}")
    Observable<User> getUserCredit(@Path("objectId") String objectId, @Query("include") String include);

    @GET("http://115.159.119.41/api/v1/users/{objectId}")
    Observable<User> getUserShakeTimes(@Path("objectId") String objectId, @Query("include") String include);

    @GET("http://115.159.119.41/api/v1/users/{objectId}")
    Observable<User> getUserSignIn(@Path("objectId") String objectId, @Query("include") String include);

    //更新用户 需要X-Bmob-Session-Token  username和password可以更改，但是新的username不能重复
//    {
//        "updatedAt": YYYY-mm-dd HH:ii:ss
//    }

    @PUT("http://115.159.119.41/api/v1/users/{objectId}")
    Observable<String> updateUser(@Path("objectId") String objectId, @Body User user);

    @PUT("http://115.159.119.41/api/v1/users")
    Observable<String> resetUserPassword(@Query("username") String oldPassword, @Query("password") String password, @Query("code") String code);

    @PUT("http://115.159.119.41/api/v1/users")
    Observable<String> resetTransactionPassword(@Query("username") String oldPassword, @Query("password") String password, @Query("code") String code);

    @PUT("http://115.159.119.41/api/v1/users/{objectId}")
    Observable<String> updateUserPassword(@Path("objectId") String objectId, @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);

    @GET("http://115.159.119.41/api/v1/transactionPassword/{objectId}")
    Observable<String> isTransactionPassword(@Path("objectId") String objectId);

    @PUT("http://115.159.119.41/api/v1/transactionPassword/{objectId}")
    Observable<String> updateTransactionPassword(@Path("objectId") String objectId, @Query("oldPassword") String oldPassword, @Query("newPassword") String newPassword);

    @GET("http://www.sakula.net.cn/api/asd/ss")
    Observable<BaseObject> getAutoLoginUrl(@Query("uid") String uid, @Query("credits") String credits);


//    //原子计算器
////    body:
////    {
////        key1:{"__op":"Increment","amount":value}
////        ...
////    }
////    {
////        "updatedAt": "YYYY-mm-dd HH:ii:ss"
////    }
//    @PUT("classes/user/{objectId}")
//    Observable<Item> atomicAdd(@Path("tableName") String tableName, @Path("objectId") String objectId, @Body Item item);
//
//    //删除用户 需要X-Bmob-Session-Token
////    {
////        "msg": "ok"
////    }
//    @DELETE("users/{objectId}")
//    Observable<User> deleteUser(@Path("objectId") String objectId);
//
//    //提供旧密码方式安全修改用户密码 需要X-Bmob-Session-Token
////    {
////        "msg": "ok"
////    }
}
