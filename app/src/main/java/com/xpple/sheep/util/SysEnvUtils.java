package com.xpple.sheep.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;
import com.xpple.sheep.CustomApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 运行环境信息
 *
 * @author nEdAy
 */
public final class SysEnvUtils {

    /**
     * 操作系统名称(GT-I9100G)
     ***/
    public static final String MODEL_NUMBER = Build.MODEL;
    /**
     * 操作系统名称(I9100G)
     ***/
    public static final String DISPLAY_NAME = Build.DISPLAY;
    /**
     * 操作系统版本(4.4)
     ***/
    public static final String OS_VERSION = Build.VERSION.RELEASE;
    /***
     * Activity之间数据传输数据对象Key
     **/
    public static final String ACTIVITY_DTO_KEY = "ACTIVITY_DTO_KEY";
    /***
     * Log输出标识
     **/
    private static final String TAG = SysEnvUtils.class.getSimpleName();
    /***
     * 屏幕显示材质
     **/
    private static final DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    /**
     * 上下文
     **/
    private static final Context context = CustomApplication.getInstance();
    /**
     * 应用程序版本
     ***/
    public static final String APP_VERSION = getVersion();
    /***
     * 屏幕宽度
     **/
    public static final int SCREEN_WIDTH = getDisplayMetrics().widthPixels;
    /***
     * 屏幕高度
     **/
    public static final int SCREEN_HEIGHT = getDisplayMetrics().heightPixels;
    /***
     * 本机手机号码
     **/
    public static final String PHONE_NUMBER = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
    /***
     * 设备ID
     **/
    public static final String DEVICE_ID = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//    /***
//     * 设备IMEI号码  i756 SCH-I939 K-Touch E780 三款厂商修改了TelephonyManager的?getDeviceID方法，导致无法使用getDeviceID方法获取IMEI号码。
//     **/
//    public static final String IMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber();
    /***
     * 设备IMSI号码
     **/
    public static final String IMSI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();

//    public static String getPhoneInfo() {
//        return "操作系统名称: " + MODEL_NUMBER + "，" + "操作系统名称: " + DISPLAY_NAME + "，" + "系统版本: Android " + OS_VERSION + "，"
//                + "应用程序版本:" + APP_VERSION + "，" + "屏幕显示材质: " + mDisplayMetrics + "，" + "屏幕宽度: " + SCREEN_WIDTH + "，"
//                + "屏幕高度: " + SCREEN_HEIGHT + "，" + "本机手机号码:" + PHONE_NUMBER + ",设备ID:" + DEVICE_ID + "，"
//                + "IMEI:" + IMEI + "，" + "IMSI:" + IMSI;
//    }

    /**
     * 获取系统显示材质
     ***/
    public static DisplayMetrics getDisplayMetrics() {
        WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics;
    }


    /**
     * 获取应用程序版本（versionName）
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageInfo info;
            PackageManager manager = context.getPackageManager();
            info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            Logger.e(TAG, "获取应用程序版本失败，原因：" + e.getMessage());
            return "";
        }

    }

    /**
     * 获取系统内核版本
     *
     * @return Version
     */
    public static String getKernelVersion() {
        String strVersion = "";
        FileReader mFileReader;
        BufferedReader mBufferedReader = null;
        try {
            mFileReader = new FileReader("/proc/version");
            mBufferedReader = new BufferedReader(mFileReader, 8192);
            String str2 = mBufferedReader.readLine();
            strVersion = str2.split("\\s+")[2];//KernelVersion

        } catch (Exception e) {
            Logger.e(TAG, "获取系统内核版本失败，原因：" + e.getMessage());
        } finally {
            try {
                if (mBufferedReader != null) {
                    mBufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return strVersion;
    }


    /***
     * 获取MAC地址
     *
     * @return MAC地址
     */
    public static String getMacAddress() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getMacAddress() != null) {
            return wifiInfo.getMacAddress();
        } else {
            return "";
        }
    }

    /**
     * 获取运行时间
     *
     * @return 运行时间(单位/s)
     */
    public static long getRunTimes() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        if (ut == 0) {
            ut = 1;
        }
        return ut;
    }
}
