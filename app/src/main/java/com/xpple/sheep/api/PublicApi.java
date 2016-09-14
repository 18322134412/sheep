package com.xpple.sheep.api;


import com.xpple.sheep.bean.Activity;
import com.xpple.sheep.bean.CreditsHistory;
import com.xpple.sheep.bean.Message;
import com.xpple.sheep.bean.Notice;
import com.xpple.sheep.bean.OverageHistory;
import com.xpple.sheep.bean.RechargeHistory;
import com.xpple.sheep.bean.Timestamp;
import com.xpple.sheep.bean.WithdrawalsHistory;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface PublicApi {
    //提现详情条件查询
    @GET("http://gw.api.taobao.com/router/rest")
    Observable<WithdrawalsHistory> pushMessage(
            @QueryMap Map<String, String> options);

    //获取服务器时间
//    {
//        "timestamp": timestamp,
//            "datetime": YYYY-mm-dd HH:ii:ss(北京时间)
//    }
//    {"timestamp":1437531770,"datetime":"2015-07-22 10:22:50"}
    @GET("timestamp")
    Observable<Timestamp> getTimestamp();

    //活动条件查询
    @GET("classes/Activity")
    Observable<Activity> queryActivity(
            @QueryMap Map<String, String> options);

    //通知条件查询
    @GET("classes/Notice")
    Observable<Notice> queryNotice(
            @QueryMap Map<String, String> options);

    //私信查询
    @GET("classes/Message")
    Observable<Message> queryMessage(
            @QueryMap Map<String, String> options);

    //收益详情条件查询
    @GET("classes/OverageDetails")
    Observable<OverageHistory> queryOverageDetails(
            @QueryMap Map<String, String> options);

    //积分详情条件查询
    @GET("classes/CreditsDetails")
    Observable<CreditsHistory> queryCreditsDetails(
            @QueryMap Map<String, String> options);

    //充值详情条件查询
    @GET("classes/RechargeDetails")
    Observable<RechargeHistory> queryRechargeDetails(
            @QueryMap Map<String, String> options);

    //提现详情条件查询
    @GET("classes/WithdrawalsDetails")
    Observable<WithdrawalsHistory> queryWithdrawalsDetails(
            @QueryMap Map<String, String> options);
}
