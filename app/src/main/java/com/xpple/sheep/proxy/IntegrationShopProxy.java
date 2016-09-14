package com.xpple.sheep.proxy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.ui.LoginActivity;
import com.xpple.sheep.ui.WebShopActivity;

public class IntegrationShopProxy implements UserProxy.IQueryListener, UserProxy.IGetAutoLoginUrlListener {

    private UserProxy userProxy;
    private Context mContext;
    private Activity activity;
    private SVProgressHUD svProgressHUD;

    public IntegrationShopProxy(Context mContext, Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
    }

    public void showIntegrationShop() {
        svProgressHUD = new SVProgressHUD(mContext);
        userProxy = new UserProxy(mContext);
        User user = userProxy.getCurrentUser();
        if (user != null) {
            svProgressHUD.showWithStatus("获取用户积分中...");
            userProxy.setOnQueryListener(this);
            userProxy.getUserCredit(user.getObjectId());
        } else {
            activity.startActivity(new Intent(mContext, LoginActivity.class));
        }

    }

    @Override
    public void onQuerySuccess(User user) {
        svProgressHUD.showSuccessWithStatus("获取用户积分成功");
        svProgressHUD.showWithStatus("正在生成免登录url...");
        userProxy.setOnGetAutoLoginUrlListener(this);
        userProxy.getAutoLoginUrl(user.getObjectId(), user.getCredit().toString());
    }

    @Override
    public void onQueryFailure() {
        svProgressHUD.showErrorWithStatus("获取用户积分失败");
    }


    @Override
    public void onGetAutoLoginUrlSuccess(String url) {
        //因跳转页面 使其立即消失
        svProgressHUD.dismissImmediately();
        Intent intent = new Intent();
        intent.setClass(mContext, WebShopActivity.class);
        intent.putExtra("url", url); //配置自动登陆地址，每次需根据服务端时间动态生成。
        activity.startActivity(intent);
    }

    @Override
    public void onGetAutoLoginUrlFailure() {
        svProgressHUD.showErrorWithStatus("免登录url获取成功失败");
    }
}
