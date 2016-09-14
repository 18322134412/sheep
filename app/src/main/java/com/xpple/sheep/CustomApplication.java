package com.xpple.sheep;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xpple.sheep.util.SharedPreferencesSettingsUtil;
import com.xpple.sheep.util.SharedPreferencesUserUtil;

// ┏┓　　　┏┓
// ┏┛┻━━━┛┻┓
// ┃　　　　　　　┃ 　
// ┃　　　━　　　┃
// ┃　┳┛　┗┳　┃
// ┃　　　　　　　┃
// ┃　　　┻　　　┃
// ┃　　　　　　　┃
// ┗━┓　　　┏━┛
// ┃　　　┃ 神兽保佑　　　　　　　　
// ┃　　　┃ 有BUG请现身！
// ┃　　　┗━━━┓
// ┃　　　　　　　┣┓
// ┃　　　　　　　┏┛
// ┗┓┓┏━┳┓┏┛
// ┃┫┫　┃┫┫
// ┗┻┛　┗┻┛

/**
 * 自定义全局Application类
 *
 * @author nEdAy
 */
public class CustomApplication extends Application {
    public static final String PREFERENCE_SETTINGS = "_Settings";
    public static final String PREFERENCE_USER = "_User";
    /**
     * 对外提供整个应用生命周期的Context
     **/
    public static CustomApplication mInstance;
    public SharedPreferencesUserUtil mSharedPreferencesUserUtil;
    public SharedPreferencesSettingsUtil mSharedPreferencesSettingsUtil;
    public NotificationManager mNotificationManager;
    private MediaPlayer mMediaPlayer;

    /**
     * 对外提供Application Context
     *
     * @return Application
     */
    public static CustomApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (BuildConfig.LOG_DEBUG) {
            MobclickAgent.setDebugMode(true);
            Logger.init("Sheep");
        }
        /** 设置是否对日志信息进行加密, 默认false(不加密). */
        AnalyticsConfig.enableEncrypt(true);
        LeakCanary.install(mInstance);
//        //wifi无网或弱网下 超时等待时间过长
//        if (CommonUtils.isNetworkAvailable(mInstance)) {
//            //調試模式
//            AlibabaSDK.turnOnDebug();
//            //请在初始化之前调用,设置默认的淘客 pid 值, 此值会在相关方法调用时未指定 pid 时使用
//            TradeConfigs.defaultTaokePid = StaticConfig.DEFAULT_TAOKE_PID;
//            //初始化百川SDK
//            AlibabaSDK.asyncInit(mInstance, new InitResultCallback() {
//                @Override
//                public void onSuccess() {
//                    //初始化云推送通道
//                    initCloudChannel();
//                    MediaService.enableHttpDNS(); //果用户为了避免域名劫持，可以启用HttpDNS
//                    MediaService.enableLog(); //在调试时，可以打印日志。正式上线前可以关闭
//                    Logger.d("TaeSDK 初始化成功");
//                }
//
//                @Override
//                public void onFailure(int code, String message) {
//                    Logger.e("初始化异常，code = " + code + ", info = " + message);
//                }
//            });
//            //初始化百川 反馈SDK
//            FeedbackAPI.initAnnoy(mInstance, StaticConfig.ALIBABA_APP_KEY);
//            //初始化JD开普勒SDK
//            KeplerApiManager.asyncInitSdk(mInstance, StaticConfig.JD_APP_KEY, StaticConfig.JD_KEY_SECRET, new AsyncInitListener() {
//                @Override
//                public void onSuccess() {
//                    Logger.d("Kepler 初始化成功");
//                }
//
//                @Override
//                public void onFailure() {
//                    Logger.e("Kepler asyncInitSdk 授权失败，请检查lib 工程资源引用；包名,签名证书是否和注册一致");
//                }
//            });
//
//        }
        //初始化Fresco
        Fresco.initialize(mInstance, StaticConfig.getOkHttpImagePipelineConfig(mInstance));
    }

    /**
     * 初始化云推送通道
     */
    private void initCloudChannel() {
        CloudPushService cloudPushService = AlibabaSDK.getService(CloudPushService.class);
        if (cloudPushService != null) {
            cloudPushService.register(mInstance.getApplicationContext(), new CommonCallback() {
                @Override
                public void onSuccess() {
                    Logger.d("init cloudchannel success" +
                            cloudPushService.getDeviceId());
                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    Logger.e("init cloudchannel fail" + "err:" + errorCode + " - message:" + errorMessage);
                }
            });
        } else {
            Logger.i("CloudPushService is null");
        }
    }

    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

    //单例模式，才能及时返回数据
    public synchronized MediaPlayer getMediaPlayer() {
        if (mMediaPlayer == null)
            mMediaPlayer = MediaPlayer.create(mInstance, R.raw.beep);
        return mMediaPlayer;
    }

    public synchronized SharedPreferencesUserUtil getSpUserUtil() {
        if (mSharedPreferencesUserUtil == null) {
            mSharedPreferencesUserUtil = new SharedPreferencesUserUtil(mInstance, PREFERENCE_USER);
        }
        return mSharedPreferencesUserUtil;
    }

    public synchronized SharedPreferencesSettingsUtil getSpSettingsUtil() {
        if (mSharedPreferencesSettingsUtil == null) {
            mSharedPreferencesSettingsUtil = new SharedPreferencesSettingsUtil(mInstance, PREFERENCE_SETTINGS);
        }
        return mSharedPreferencesSettingsUtil;
    }

}
