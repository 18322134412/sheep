package com.xpple.sheep.base;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RelativeLayout;

import com.xpple.sheep.R;

import java.lang.reflect.Field;

/**
 * Fragment基类
 *
 * @author nEdAy
 */
public class BaseFragment extends Fragment {
    /**
     * 获取共通操作机能
     */
    public BaseOperation getOperation() {
        return new BaseOperation(getActivity());
    }

    public void setStatusBarHeight(View parentView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                int sbar = getResources().getDimensionPixelSize(x);
                View paddingView = parentView.findViewById(R.id.paddingView);
                RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) paddingView.getLayoutParams(); // 取控件mGrid当前的布局参数
                linearParams.height = sbar;// 当控件的高强制设成状态栏高度
                paddingView.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
            } catch (Exception ignored) {
            }
        }
    }
}
