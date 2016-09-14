package com.xpple.sheep.proxy;

import android.content.Context;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.api.RxFactory;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.util.SharedPreferencesUserUtil;
import com.xpple.sheep.util.StringUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserProxy {
    private Context mContext;
    private ISignUpListener signUpLister;
    private ILoginListener loginListener;
    private IUpdateListener updateListener;
    private IQueryListener queryListener;
    private IPasswordListener passwordListener;
    private IGetAutoLoginUrlListener getAutoLoginUrlListener;
    private SharedPreferencesUserUtil mSharedPreferencesUtil;

    public UserProxy(Context context) {
        this.mContext = context;
        mSharedPreferencesUtil = CustomApplication.getInstance().getSpUserUtil();
    }

    public void signUp(String phone, String password, String code) {
        User user = new User();
        user.setUsername(phone);
        user.setPassword(StringUtils.getMD5(password));
        RxFactory.getUserServiceInstance(null)
                .signUp(user, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentUser -> signUpLister.onSignUpSuccess(phone, password), throwable -> signUpLister.onSignUpFailure(throwable.getMessage()));
    }

    public void setOnSignUpListener(ISignUpListener signUpLister) {
        this.signUpLister = signUpLister;
    }

    public void query(String objectID) {
        RxFactory.getUserServiceInstance(null)
                .getUser(objectID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> queryListener.onQuerySuccess(user), throwable -> queryListener.onQueryFailure());
    }


    public void setOnQueryListener(IQueryListener queryListener) {
        this.queryListener = queryListener;
    }


    public void getUserCredit(String objectID) {
        RxFactory.getUserServiceInstance(null)
                .getUserCredit(objectID, "_User[credit]")
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> queryListener.onQuerySuccess(user), throwable -> queryListener.onQueryFailure());
    }

    public void getUserShakeTimes(String objectID) {
        RxFactory.getUserServiceInstance(null)
                .getUserShakeTimes(objectID, "_User[shake_times]")
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> queryListener.onQuerySuccess(user), throwable -> queryListener.onQueryFailure());
    }

    public void getUserSignIn(String objectID) {
        RxFactory.getUserServiceInstance(null)
                .getUserSignIn(objectID, "_User[sign_in]")
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> queryListener.onQuerySuccess(user), throwable -> queryListener.onQueryFailure());
    }

    public User getCurrentUser() {
        User user = loadCurrentUser();
        if (user != null) {
            return user;
        }
        return null;
    }

    public void saveCurrentUser(User currentUser) {
        mSharedPreferencesUtil.saveCurrentUser(currentUser);
    }

    public User loadCurrentUser() {
        return mSharedPreferencesUtil.loadCurrentUser();
    }

    public void login(String phone, String password) {
        RxFactory.getUserServiceInstance(null)
                .login(phone, StringUtils.getMD5(password))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentUser -> loginListener.onLoginSuccess(currentUser), throwable -> loginListener.onLoginFailure());
    }

    public void setOnLoginListener(ILoginListener loginListener) {
        this.loginListener = loginListener;
    }

    /**
     * 退出登录,清空缓存数据
     */
    public void logout() {
        //解除阿里百川推送 账户绑定
        CloudPushService cloudPushService = AlibabaSDK.getService(CloudPushService.class);
        cloudPushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailed(String s, String s1) {
            }
        });
        //清除本地账户记录
        mSharedPreferencesUtil.cleanSharedPreference();
    }

    public void update(User user) {
        UserProxy userProxy = new UserProxy(mContext);
        User currentUser = userProxy.getCurrentUser();
        RxFactory.getUserServiceInstance(currentUser.getSessionToken())
                .updateUser(currentUser.getObjectId(), user)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateAt -> updateListener.onUpdateSuccess(user), throwable -> updateListener.onUpdateFailure(throwable.getMessage()));
    }

    public void resetUserPassword(String username, String password, String code) {
        RxFactory.getUserServiceInstance(null)
                .resetUserPassword(username, StringUtils.getMD5(password), code)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateAt -> passwordListener.onPasswordSuccess(updateAt), throwable -> passwordListener.onPasswordFailure(throwable.getMessage()));
    }

    public void resetTransactionPassword(String username, String password, String code) {
        UserProxy userProxy = new UserProxy(mContext);
        User currentUser = userProxy.getCurrentUser();
        RxFactory.getUserServiceInstance(currentUser.getSessionToken())
                .resetTransactionPassword(username, StringUtils.getMD5(password), code)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateAt -> passwordListener.onPasswordSuccess(updateAt), throwable -> passwordListener.onPasswordFailure(throwable.getMessage()));
    }

    public void setOnUpdateListener(IUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void updateUserPassword(String oldPassword, String newPassword) {
        UserProxy userProxy = new UserProxy(mContext);
        User currentUser = userProxy.getCurrentUser();
        RxFactory.getUserServiceInstance(currentUser.getSessionToken())
                .updateUserPassword(currentUser.getObjectId(), StringUtils.getMD5(oldPassword), StringUtils.getMD5(newPassword))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateAt -> passwordListener.onPasswordSuccess(updateAt), throwable -> passwordListener.onPasswordFailure(throwable.getMessage()));
    }

    public void isTransactionPasswordNull() {
        UserProxy userProxy = new UserProxy(mContext);
        User currentUser = userProxy.getCurrentUser();
        RxFactory.getUserServiceInstance(currentUser.getSessionToken())
                .isTransactionPassword(currentUser.getObjectId())
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isTransactionPasswordNull -> passwordListener.onPasswordSuccess(isTransactionPasswordNull), throwable -> passwordListener.onPasswordFailure(throwable.getMessage()));
    }


    public void updateTransactionPassword(String oldPassword, String newPassword) {
        UserProxy userProxy = new UserProxy(mContext);
        User currentUser = userProxy.getCurrentUser();
        RxFactory.getUserServiceInstance(currentUser.getSessionToken())
                .updateTransactionPassword(currentUser.getObjectId(), StringUtils.getMD5(oldPassword), StringUtils.getMD5(newPassword))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(updateAt -> passwordListener.onPasswordSuccess(updateAt), throwable -> passwordListener.onPasswordFailure(throwable.getMessage()));
    }

    public void setOnPasswordListener(IPasswordListener passwordListener) {
        this.passwordListener = passwordListener;
    }

    public void getAutoLoginUrl(String objectID, String credit) {
        RxFactory.getUserServiceInstance(null)
                .getAutoLoginUrl(objectID, credit)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseObject -> getAutoLoginUrlListener.onGetAutoLoginUrlSuccess(baseObject.getObjectId()), throwable -> getAutoLoginUrlListener.onGetAutoLoginUrlFailure());
    }

    public void setOnGetAutoLoginUrlListener(IGetAutoLoginUrlListener getAutoLoginUrlListener) {
        this.getAutoLoginUrlListener = getAutoLoginUrlListener;
    }

    public interface IGetAutoLoginUrlListener {
        void onGetAutoLoginUrlSuccess(String url);

        void onGetAutoLoginUrlFailure();
    }

    public interface ISignUpListener {
        void onSignUpSuccess(String phone, String password);

        void onSignUpFailure(String error);
    }

    public interface ILoginListener {
        void onLoginSuccess(User user);

        void onLoginFailure();
    }

    public interface IUpdateListener {
        void onUpdateSuccess(User user);

        void onUpdateFailure(String msg);
    }

    public interface IPasswordListener {
        void onPasswordSuccess(String value);

        void onPasswordFailure(String msg);
    }

    public interface IQueryListener {
        void onQuerySuccess(User user);

        void onQueryFailure();
    }
}
