package com.xpple.sheep.base;

import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.ui.LoginActivity;

//import com.xpple.sheep.proxy.UserProxy;


/**
 * 必须处于用户登录状态Activity的基类-用于检测用户是否登陆或是否有其他设备登录了同一账号
 *
 * @author nEdAy
 */
public abstract class BaseOnlineActivity extends BaseActivity {

    @Override
    public void onResume() {
        super.onResume();
        //锁屏状态下的检测
        checkLogin();
    }

    public void checkLogin() {
        UserProxy userProxy = getUserProxy();
        if (userProxy.getCurrentUser() == null) {
            getOperation().startActivity(LoginActivity.class);
            finish();
        }
    }


}
