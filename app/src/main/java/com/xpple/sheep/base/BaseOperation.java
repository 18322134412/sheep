package com.xpple.sheep.base;

import android.app.Activity;
import android.content.Intent;

import com.xpple.sheep.ui.WebActivity;

/**
 * 基本的操作共通抽取
 *
 * @author nEdAy
 */
public class BaseOperation {

    private Activity mContext = null;

    public BaseOperation(Activity mContext) {
        this.mContext = mContext;
    }

    /**
     * 启动Activity
     */
    public void startActivity(Class<?> cla) {
        mContext.startActivity(new Intent(mContext, cla));
    }

    public void startActivity(Intent intent) {
        mContext.startActivity(intent);
    }

    /**
     * 启动WebActivity
     */
    public void startWebActivity(String url, String title) {
        startActivity(WebActivity.newIntent(mContext, url, title));
    }


}
