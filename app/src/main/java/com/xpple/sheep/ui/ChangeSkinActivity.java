package com.xpple.sheep.ui;


import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.util.SharedPreferencesUserUtil;

public class ChangeSkinActivity extends BaseActivity {
    //背景示例图（上）
    private static final int[] selfcenter_bg_main = {R.mipmap.selfcenter_bg_main_0, R.mipmap.selfcenter_bg_main_1,
            R.mipmap.selfcenter_bg_main_2, R.mipmap.selfcenter_bg_main_3, R.mipmap.selfcenter_bg_main_4,};
    //背景选择图（下）
    private static final int[] selfcenter_bg_banner = {R.mipmap.selfcenter_bg_banner_0, R.mipmap.selfcenter_bg_banner_1,
            R.mipmap.selfcenter_bg_banner_2, R.mipmap.selfcenter_bg_banner_3, R.mipmap.selfcenter_bg_banner_4,};
    private ImageView skin_image;
    private SharedPreferencesUserUtil mSharedPreferencesUserUtil;

    @Override
    public int bindLayout() {
        return R.layout.activity_change_skin;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("自定义皮肤", "返回");
        skin_image = (ImageView) findViewById(R.id.skin_image);
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.my_gallery);
        mSharedPreferencesUserUtil = CustomApplication.getInstance().getSpUserUtil();
        int centerBg = mSharedPreferencesUserUtil.getCenterBg();
        skin_image.setImageResource(selfcenter_bg_main[centerBg]);
        for (int i = 0; i < 5; i++) {
            mLinearLayout.addView(getImageView(i));
        }
    }


    private View getImageView(final int number) {
        RelativeLayout layout = new RelativeLayout(getApplicationContext());
        layout.setLayoutParams(new RelativeLayout.LayoutParams(250, 250));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(selfcenter_bg_banner[number]);
        layout.addView(imageView);

        layout.setOnClickListener(view -> {
            skin_image.setImageResource(selfcenter_bg_main[number]);
            CommonUtils.showToast("已设置此背景皮肤");
            mSharedPreferencesUserUtil.setCenterBg(number);
        });
        return layout;
    }

}