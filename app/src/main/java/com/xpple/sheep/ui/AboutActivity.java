package com.xpple.sheep.ui;

import android.view.View;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;

/**
 * 关于页
 *
 * @author nEdAy
 */
public class AboutActivity extends BaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_about;
    }

    @override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("关于", "返回");
    }
}
