package com.xpple.sheep.base;

import android.view.View;

/**
 * Activity接口
 *
 * @author nEdAy
 */
public interface IBaseActivity {

    /**
     * 绑定渲染视图的布局文件
     *
     * @return 布局文件资源id
     */
    int bindLayout();

    /**
     * 初始化控件
     */
    void initView(final View view);


}
