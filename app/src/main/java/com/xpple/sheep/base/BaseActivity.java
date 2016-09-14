package com.xpple.sheep.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.xpple.sheep.R;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.view.HeaderLayout;

/**
 * Activity基类
 *
 * @author nEdAy
 */
public abstract class BaseActivity extends FragmentActivity implements IBaseActivity {
    protected Context mContext;
    /**
     * 头部
     **/
    protected HeaderLayout mHeaderLayout = (HeaderLayout) findViewById(R.id.top_title_bar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
        //设置渲染视图View
        View mContextView = LayoutInflater.from(mContext).inflate(bindLayout(), null);
        setContentView(mContextView);
        //初始化控件
        initView(mContextView);
    }

    /**
     * 获取共通操作机能
     */
    public BaseOperation getOperation() {
        return new BaseOperation(this);
    }

    public UserProxy getUserProxy() {
        return new UserProxy(mContext);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }

    /**
     * back+title
     */
    public void initTopBarForLeft(String titleName, String leftText) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.top_title_bar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LIFT_IMAGE_BUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.mipmap.ic_back, leftText,
                this::finish);
    }

    /**
     * back**+title
     */
    public void initTopBarForLeft(String titleName, String leftText, HeaderLayout.onLeftButtonClickListener onLeftButtonClickListener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.top_title_bar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_LIFT_IMAGE_BUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.mipmap.ic_back, leftText, onLeftButtonClickListener);
    }

    /**
     * back+title+右文字
     */
    public void initTopBarForBoth(String titleName, String leftText, String rightText,
                                  HeaderLayout.onRightButtonClickListener onRightButtonClickListener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.top_title_bar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGE_BUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.mipmap.ic_back, leftText,
                this::finish);
        mHeaderLayout.setTitleAndRightTextButton(titleName, rightText,
                onRightButtonClickListener);
    }

    /**
     * back**+title+右文字
     */
    public void initTopBarForBoth(String titleName, String leftText, HeaderLayout.onLeftButtonClickListener onLeftButtonClickListener, String rightText,
                                  HeaderLayout.onRightButtonClickListener onRightButtonClickListener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.top_title_bar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGE_BUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.mipmap.ic_back, leftText, onLeftButtonClickListener);
        mHeaderLayout.setTitleAndRightTextButton(titleName, rightText,
                onRightButtonClickListener);

    }

    /**
     * back+title+右图标
     */
    public void initTopBarForBoth(String titleName, String leftText, int rightDrawableId,
                                  HeaderLayout.onRightButtonClickListener listener) {
        mHeaderLayout = (HeaderLayout) findViewById(R.id.top_title_bar);
        mHeaderLayout.init(HeaderLayout.HeaderStyle.TITLE_DOUBLE_IMAGE_BUTTON);
        mHeaderLayout.setTitleAndLeftImageButton(titleName,
                R.mipmap.ic_back, leftText,
                this::finish);
        mHeaderLayout.setTitleAndRightImageButton(titleName, rightDrawableId,
                listener);
    }

    /**
     * 设置状态栏颜色
     */
    public void setTintManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.global_red_color);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
