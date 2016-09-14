package com.xpple.sheep.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.callback.CallbackContext;
import com.alibaba.sdk.android.login.LoginService;
import com.alibaba.sdk.android.login.callback.LoginCallback;
import com.alibaba.sdk.android.login.callback.LogoutCallback;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.session.model.Session;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.orhanobut.logger.Logger;
import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.R;
import com.xpple.sheep.StaticConfig;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.util.SharedPreferencesSettingsUtil;
import com.xpple.sheep.util.StringUtils;
import com.xpple.sheep.view.ClearEditText;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

/**
 * 登录页
 *
 * @author nEdAy
 */
public class LoginActivity extends BaseActivity implements UserProxy.ILoginListener, PlatformActionListener {
    private ClearEditText et_phone;
    private EditText et_password;
    private UserProxy userProxy;
    private TextView tv_login;
    private boolean phone_input = false, password_input = false;
    private SVProgressHUD svProgressHUD;
    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();
    private SharedPreferencesSettingsUtil mSharedPreferencesSettingsUtil;

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("登录", "返回");
        et_phone = (ClearEditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_login = (TextView) findViewById(R.id.tv_login);
        TextView tv_register = (TextView) findViewById(R.id.tv_register);
        TextView tv_lostPassword = (TextView) findViewById(R.id.tv_lostPassword);
        ImageView iv_login_qq = (ImageView) findViewById(R.id.iv_login_qq);
        ImageView iv_login_weibo = (ImageView) findViewById(R.id.iv_login_weibo);
        ImageView iv_login_taobao = (ImageView) findViewById(R.id.iv_login_taobao);
        mSharedPreferencesSettingsUtil = CustomApplication.getInstance().getSpSettingsUtil();
        String oldPhone = mSharedPreferencesSettingsUtil.getUserPhone();
        if (!TextUtils.isEmpty(oldPhone)) {
            et_phone.setText(oldPhone);
            et_password.setFocusable(true);
            et_password.setFocusableInTouchMode(true);
            et_password.requestFocus();
            et_password.requestFocusFromTouch();
            phone_input = true;
        }
        //initial ShareSDK
        ShareSDK.initSDK(mContext);
        userProxy = new UserProxy(mContext);
        tv_register.setOnClickListener(v ->
                getOperation().startActivity(RegisterActivity.class)
        );
        tv_lostPassword.setOnClickListener(v ->
                getOperation().startActivity(LostPasswordActivity.class));
        tv_login.setOnClickListener(v -> {
            if (!CommonUtils.isNetworkAvailable(mContext)) {
                CommonUtils.showToast(R.string.network_tips);
                return;
            }
            login();
        });
        iv_login_qq.setOnClickListener(v -> {
            if (!CommonUtils.isNetworkAvailable(mContext)) {
                CommonUtils.showToast(R.string.network_tips);
                return;
            }
            //初始化新浪平台
            Platform pf = ShareSDK.getPlatform(mContext, QQ.NAME);
            //关闭SSO授权，用网页授权代替；true-关闭；false，开启
            pf.SSOSetting(false);
            //设置监听
            pf.setPlatformActionListener(this);
            //执行授权
            pf.authorize();
        });
        iv_login_weibo.setOnClickListener(v -> {
            if (!CommonUtils.isNetworkAvailable(mContext)) {
                CommonUtils.showToast(R.string.network_tips);
                return;
            }
            //初始化新浪平台
            Platform pf = ShareSDK.getPlatform(mContext, SinaWeibo.NAME);
            //关闭SSO授权，用网页授权代替；true-关闭；false，开启
            pf.SSOSetting(false);
            //设置监听
            pf.setPlatformActionListener(this);
            //执行授权
            pf.authorize();
        });
        iv_login_taobao.setOnClickListener(v -> showAlipayLogin());
        et_phone.addTextChangedListener(mPhoneWatcher);
        et_password.addTextChangedListener(mPasswordWatcher);
        // 注册退出广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(StaticConfig.ACTION_REGISTER_SUCCESS_FINISH);
        registerReceiver(receiver, filter);
    }


    private void showAlipayLogin() {
        LoginService loginService = AlibabaSDK.getService(LoginService.class);
        loginService.showLogin(this, new LoginCallback() {
            @Override
            public void onSuccess(Session session) {
                CommonUtils.showToast("欢迎" + session.getUser().nick + session.getUser().avatarUrl);
//                isLogin	当前是否登录状态
//                getLoginTime	登录授权时间
//                getUserId	当前用户ID
//                getUser	用户其他属性,用户名，头像地址等
                Logger.i("-AuthorizationCode-" + session.getAuthorizationCode() + "-isLogin-" + session.isLogin() + "-UserId-" + session.getUserId() +
                        "-LoginTime-" + session.getLoginTime() + "[user]:nick=" + session.getUser().nick +
                        "头像" + session.getUser().avatarUrl);
                // -AuthorizationCode-8HbqCs4RDZ7VpnsVf76vNHOl298308-isLogin-true
                // -UserId-AAFBwAg6ACXcfdsTiIVhA3po-LoginTime-1460940821699[user]:
                // nick=m**6头像http://wwc.taobaocdn.com/avatar/getAvatar.do?userNick=AAFBwAg6ACXcfdsTiIVhA3po&width=160&height=160&type=open&appKey=23346374
            }

            @Override
            public void onFailure(int code, String message) {
                CommonUtils.showToast("授权取消" + code + message);
            }
        });
    }

    //为了让LoginCallback生效，需要在开发者代码里的 Activity 重写方法，代码如下：
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CallbackContext.onActivityResult(requestCode, resultCode, data);
    }

    public void logoutAlipay() {
        LoginService loginService = AlibabaSDK.getService(LoginService.class);
        loginService.logout(this, new LogoutCallback() {
            @Override
            public void onFailure(int code, String msg) {
                CommonUtils.showToast("登出失败" + code + msg);
            }

            @Override
            public void onSuccess() {
                CommonUtils.showToast("登出成功");
            }
        });
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        // 成功, successful notification
        CommonUtils.showToast("Auth successfully");
        Logger.e("platform", platform + platform.getName()); //获取用户id
        //ShareSDK只保存以下这几个通用值
        if (platform.getName().equals(QQ.NAME)) {
            Platform pf = ShareSDK.getPlatform(mContext, QQ.NAME);
            Logger.e("sharesdk use_id", pf.getDb().getUserId()); //获取用户id
            Logger.e("sharesdk use_name", pf.getDb().getUserName());//获取用户名称
            Logger.e("sharesdk use_icon", pf.getDb().getUserIcon() + "");//获取用户头像
        } else if (platform.getName().equals(SinaWeibo.NAME)) {
            Platform pf = ShareSDK.getPlatform(mContext, SinaWeibo.NAME);
            Logger.e("sharesdk use_id", pf.getDb().getUserId()); //获取用户id
            Logger.e("sharesdk use_name", pf.getDb().getUserName());//获取用户名称
            Logger.e("sharesdk use_icon", pf.getDb().getUserIcon());//获取用户头像
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        // 失败, fail notification
        String expName = throwable.getClass().getSimpleName();
        if ("QQClientNotExistException".equals(expName)) {
            CommonUtils.showToast("QQ 版本过低或者没有安装，需要升级或安装QQ才能使用！");
        } else {
            CommonUtils.showToast("Auth unsuccessfully");
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        // 取消, cancel notification
        CommonUtils.showToast("Cancel authorization");
    }


    private void login() {
        String phone = et_phone.getText().toString().trim().replace(" ", "");
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            CommonUtils.showToast(R.string.toast_error_phone_null);
            et_phone.requestFocus();
            CommonUtils.setShakeAnimation(et_phone);
            return;
        }
        if (!StringUtils.isPhoneNumberValid(phone)) {
            CommonUtils.showToast(R.string.toast_error_phone_error);
            et_phone.requestFocus();
            CommonUtils.setShakeAnimation(et_phone);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            CommonUtils.showToast(R.string.toast_error_password_null);
            et_password.requestFocus();
            CommonUtils.setShakeAnimation(et_password);
            return;
        }
        if (!StringUtils.isValidPassword(password)) {
            CommonUtils.showToast(R.string.toast_error_password_error);
            et_password.requestFocus();
            CommonUtils.setShakeAnimation(et_password);
            return;
        }
        userProxy.setOnLoginListener(this);
        svProgressHUD = new SVProgressHUD(this);
        userProxy.login(phone, password);
        /**隐藏软键盘**/
        View _view = getWindow().peekDecorView();
        if (_view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(_view.getWindowToken(), 0);
        }
        svProgressHUD.showWithStatus("登录中");
    }

    @Override
    public void onLoginSuccess(User user) {
        String phone = et_phone.getText().toString().trim();
        mSharedPreferencesSettingsUtil.setUserPhone(phone);
        svProgressHUD.showSuccessWithStatus("登录成功");
        // 将私有token保存
        userProxy.saveCurrentUser(user);
        finish();
    }

    @Override
    public void onLoginFailure() {
        svProgressHUD.showErrorWithStatus("登录失败");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ShareSDK.stopSDK(mContext);
        unregisterReceiver(receiver);
    }


    public class MyBroadcastReceiver extends BroadcastReceiver implements UserProxy.ILoginListener {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null
                    && StaticConfig.ACTION_REGISTER_SUCCESS_FINISH
                    .equals(intent.getAction())) {
                userProxy.setOnLoginListener(this);
                userProxy.login(intent.getStringExtra("phone"), intent.getStringExtra("password"));
            }
        }

        @Override
        public void onLoginSuccess(User user) {
            // 将私有token保存
            userProxy.saveCurrentUser(user);
            CommonUtils.showToast("登录成功");
            CloudPushService cloudPushService = AlibabaSDK.getService(CloudPushService.class);
            cloudPushService.bindAccount(user.getObjectId(), new CommonCallback() {
                @Override
                public void onSuccess() {
                    Logger.i("bind account success");
                }

                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    Logger.e("bind account fail" + "err:" + errorCode + " - message:" + errorMessage);
                }
            });
            finish();
        }

        @Override
        public void onLoginFailure() {
            CommonUtils.showToast("登录失败");
        }
    }

    TextWatcher mPhoneWatcher = new TextWatcher() {
        int beforeLen = 0;
        int afterLen = 0;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeLen = s.length();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            afterLen = s.length();
            if (afterLen > beforeLen) {
                if (afterLen == 4 || afterLen == 9) {
                    et_phone.setText(new StringBuffer(s).insert(
                            afterLen - 1, " ").toString());
                    et_phone.setSelection(afterLen);
                }
            } else {
                if (et_phone.getText().toString().startsWith(" ")) {
                    et_phone.setText(new StringBuffer(s).delete(
                            afterLen - 1, afterLen).toString());
                    et_phone.setSelection(afterLen);
                }
            }
            phone_input = afterLen == 13;
            tv_login.setEnabled(phone_input && password_input);
        }
    };
    TextWatcher mPasswordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            password_input = s.length() >= 6;
            tv_login.setEnabled(phone_input && password_input);
        }
    };

}
