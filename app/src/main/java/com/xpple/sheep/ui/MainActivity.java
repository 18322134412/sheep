package com.xpple.sheep.ui;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lenovo.lps.sus.SUS;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.base.TabEntity;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.ui.mainFragment.IndexFragment;
import com.xpple.sheep.ui.mainFragment.ItemFragment;
import com.xpple.sheep.ui.mainFragment.MeFragment;
import com.xpple.sheep.ui.mainFragment.MoreFragment;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.view.dialog.ActionSheetDialog;

import java.util.ArrayList;

/**
 * 主页
 *
 * @author nEdAy
 */
public class MainActivity extends BaseActivity {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"首页", "项目", "我的", "更多"};
    private int[] mIconUnSelectIds = {
            R.mipmap.tab_index_unselect, R.mipmap.tab_item_unselect,
            R.mipmap.tab_me_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_index_select, R.mipmap.tab_item_select,
            R.mipmap.tab_me_select, R.mipmap.tab_more_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    /**
     * 四个Fragment页面
     */
    public static final int FRAGMENT_INDEX = 0;
    public static final int FRAGMENT_ITEM = 1;
    public static final int FRAGMENT_ME = 2;
    public static final int FRAGMENT_MORE = 3;
    private int oldPosition;
    /**
     * 底部区域
     */
    private CommonTabLayout mTabLayout;
    /**
     * 连续按两次返回键就退出
     */

    private long firstTime;

    private static boolean isStartVersionUpdateFlag = false;

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {
        mFragments.add(new IndexFragment());
        mFragments.add(new ItemFragment());
        mFragments.add(new MeFragment());
        mFragments.add(new MoreFragment());
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
        }
        mTabLayout = (CommonTabLayout) findViewById(R.id.tl_tab);
        mTabLayout.setTabData(mTabEntities, this, R.id.fl_content, mFragments);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == FRAGMENT_ME) {
                    UserProxy userProxy = getUserProxy();
                    if (userProxy.getCurrentUser() == null) {
                        getOperation().startActivity(LoginActivity.class);
                        mTabLayout.setCurrentTab(oldPosition);
                    } else {
                        mTabLayout.setCurrentTab(FRAGMENT_ME);
                    }
                }
                oldPosition = position == 2 ? oldPosition : position;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        /* 应用首次启动时自动启动版本更新功能 */
        if (!isStartVersionUpdateFlag) {
            isStartVersionUpdateFlag = true;
            if (!SUS.isVersionUpdateStarted()) {
                SUS.AsyncStartVersionUpdate(mContext);//直接启动应用的版本更新过程，更新过程受用户设置控制，用户可设置项：有新版本再提醒。
            }
        }
        //显示未读红点
        mTabLayout.showDot(FRAGMENT_MORE);
        mTabLayout.showDot(FRAGMENT_ME);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (firstTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            CommonUtils.showToast("再按一次退出程序");
        }
        firstTime = System.currentTimeMillis();
    }

    /**
     * 截获Menu键动作
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            // 在这里做你想做的事情
            showActionSheet(); // 调用这个，就可以弹出菜单
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showActionSheet() {
        new ActionSheetDialog(mContext).builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("版本更新", ActionSheetDialog.SheetItemColor.Blue,
                        which -> {
                            //手动更新
                            if (!SUS.isVersionUpdateStarted()) {
                                SUS.AsyncStartVersionUpdate_IgnoreUserSettings(mContext);//忽略“有新版本再提醒”的用户设置，强制启动应用的版本更新过程
                            }
                        })
                .addSheetItem("关于与合作", ActionSheetDialog.SheetItemColor.Blue,
                        which -> getOperation().startActivity(AboutActivity.class))
                .addSheetItem("退出应用", ActionSheetDialog.SheetItemColor.Blue,
                        which -> finish()).show();
    }

    @Override
    protected void onDestroy() {
        SUS.finish();//结束自动更新
        super.onDestroy();
    }
}
