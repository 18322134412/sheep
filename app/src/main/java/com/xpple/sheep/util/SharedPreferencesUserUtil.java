package com.xpple.sheep.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.xpple.sheep.bean.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class SharedPreferencesUserUtil {
    private static final String CURRENT_USER = "current_user";
    private static final String CENTER_BG = "center_bg";
    private static SharedPreferences.Editor editor;
    private SharedPreferences mSharedPreferences;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesUserUtil(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public void saveCurrentUser(User currentUser) {
        //序列化当前用户
        String serStr = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    byteArrayOutputStream);
            objectOutputStream.writeObject(currentUser);
            serStr = URLEncoder.encode(byteArrayOutputStream.toString("ISO-8859-1"), "UTF-8");
            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //存储当前用户
        editor.putString(CURRENT_USER, serStr);
        editor.apply();
    }

    public User loadCurrentUser() {
        //反序列化当前用户
        User currentUser = null;
        try {
            String redStr = URLDecoder.decode(mSharedPreferences.getString(CURRENT_USER, ""), "UTF-8");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    redStr.getBytes("ISO-8859-1"));
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    byteArrayInputStream);
            currentUser = (User) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return currentUser;
    }


    public void cleanSharedPreference() {
        editor.clear();
        editor.commit();
    }



    // 收益中心背景图片
    public int getCenterBg() {
        return mSharedPreferences.getInt(CENTER_BG, 0);
    }

    public void setCenterBg(int centerBg) {
        editor.putInt(CENTER_BG, centerBg);
        editor.commit();
    }
}
