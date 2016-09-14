package com.xpple.sheep.ui;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;
import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.R;
import com.xpple.sheep.util.PermissionsUtils;
import com.xpple.sheep.util.SharedPreferencesSettingsUtil;

/**
 * 启动页
 *
 * @author nEdAy
 */
public class SplashActivity extends FragmentActivity {
    private static final int SHOW_TIME_MIN = 1200;

    private static final int GO_GUIDE = 0;
    private static final int GO_MAIN = 1;

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private PermissionsUtils mPermissionsChecker; // 权限检测器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mPermissionsChecker = new PermissionsUtils(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            afterDo();
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    private void afterDo() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                int result;
                long startTime = System.currentTimeMillis();
                result = loadingCache();
                long loadingTime = System.currentTimeMillis() - startTime;
                if (loadingTime < SHOW_TIME_MIN) {
                    try {
                        Thread.sleep(SHOW_TIME_MIN - loadingTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(Integer result) {
                switch (result) {
                    case GO_GUIDE:
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                        break;
                    case GO_MAIN:
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        break;
                    default:
                        break;
                }
                finish();
            }
        }.execute();
    }


    private int loadingCache() {
        SharedPreferencesSettingsUtil mSharedPreferencesSettingsUtil = CustomApplication.getInstance().getSpSettingsUtil();
        Boolean user_first = mSharedPreferencesSettingsUtil.isAllowFirst();
        if (user_first) {
            return GO_GUIDE;
        } else {
            return GO_MAIN;
        }
    }


}
