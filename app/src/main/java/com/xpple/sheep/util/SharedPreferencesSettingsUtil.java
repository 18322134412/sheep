package com.xpple.sheep.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesSettingsUtil {
    private static final String SETTING_FIRST = "setting_first";
    private static final String SETTING_NOTIFY = "setting_notify";
    private static final String SETTING_VOICE = "setting_voice";
    private static final String SETTING_VIBRATE = "setting_vibrate";
    private static final String SETTING_QUIET = "setting_quiet";
    private static final String SETTING_QUIET_PERIOD = "setting_quiet_period";
    private static final String SETTING_SHAKE_VOICE = "setting_shake_voice";

    private static final String USER_PHONE = "user_phone";
    private static SharedPreferences.Editor editor;
    private SharedPreferences mSharedPreferences;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesSettingsUtil(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public void cleanSharedPreference() {
        editor.clear();
        editor.commit();
    }

    // 首次导航
    public boolean isAllowFirst() {
        return mSharedPreferences.getBoolean(SETTING_FIRST, true);
    }

    public void setAllowFirstEnable(boolean isFirst) {
        editor.putBoolean(SETTING_FIRST, isFirst);
        editor.commit();
    }


    // 是否允许推送通知
    public boolean isAllowPushNotify() {
        return mSharedPreferences.getBoolean(SETTING_NOTIFY, true);
    }

    public void setPushNotifyEnable(boolean isChecked) {
        editor.putBoolean(SETTING_NOTIFY, isChecked);
        editor.commit();
    }

    // 允许声音
    public boolean isAllowVoice() {
        return mSharedPreferences.getBoolean(SETTING_VOICE, true);
    }

    public void setAllowVoiceEnable(boolean isChecked) {
        editor.putBoolean(SETTING_VOICE, isChecked);
        editor.commit();
    }

    // 允许震动
    public boolean isAllowVibrate() {
        return mSharedPreferences.getBoolean(SETTING_VIBRATE, true);
    }

    public void setAllowVibrateEnable(boolean isChecked) {
        editor.putBoolean(SETTING_VIBRATE, isChecked);
        editor.commit();
    }

    // 允许靜音
    public boolean isAllowQuiet() {
        return mSharedPreferences.getBoolean(SETTING_QUIET, true);
    }

    public void setAllowQuietEnable(boolean isChecked) {
        editor.putBoolean(SETTING_QUIET, isChecked);
        editor.commit();
    }

    // 靜音時段
    public String getQuitePeriod() {
        return mSharedPreferences.getString(SETTING_QUIET_PERIOD, "0,0,6,0");
    }

    public void setQuitePeriod(String quitePeriod) {
        editor.putString(SETTING_QUIET_PERIOD, quitePeriod);
        editor.commit();
    }

    // 允许摇一摇声音
    public boolean isAllowShakeVoice() {
        return mSharedPreferences.getBoolean(SETTING_SHAKE_VOICE, true);
    }

    public void setAllowShakeVoiceEnable(boolean isChecked) {
        editor.putBoolean(SETTING_SHAKE_VOICE, isChecked);
        editor.commit();
    }

    // 玩家手机号码
    public String getUserPhone() {
        return mSharedPreferences.getString(USER_PHONE, "");
    }

    public void setUserPhone(String userPhone) {
        editor.putString(USER_PHONE, userPhone);
        editor.commit();
    }


}
