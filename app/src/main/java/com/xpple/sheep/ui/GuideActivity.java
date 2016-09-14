package com.xpple.sheep.ui;

import android.view.View;

import com.flyco.banner.anim.select.ZoomInEnter;
import com.flyco.banner.transform.DepthTransformer;
import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.util.SharedPreferencesSettingsUtil;
import com.xpple.sheep.view.GuideBanner;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity {

    public static ArrayList<Integer> getGuides() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.mipmap.guide_img_1);
        list.add(R.mipmap.guide_img_2);
        list.add(R.mipmap.guide_img_3);
        return list;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView(View view) {
        GuideBanner simpleGuideAdapter = (GuideBanner) findViewById(R.id.banner);
        simpleGuideAdapter.setIndicatorWidth(6)
                .setIndicatorHeight(6)
                .setIndicatorGap(12)
                .setIndicatorCornerRadius(3.5f)
                .setSelectAnimClass(ZoomInEnter.class)
                .setTransformerClass(DepthTransformer.class)
                .barPadding(0, 10, 0, 10)
                .setSource(getGuides())
                .startScroll();
        simpleGuideAdapter.setOnJumpClickL(() -> {
            SharedPreferencesSettingsUtil mSharedPreferencesSettingsUtil = CustomApplication.getInstance().getSpSettingsUtil();
            mSharedPreferencesSettingsUtil.setAllowFirstEnable(false);
            getOperation().startActivity(MainActivity.class);
            finish();
        });
    }
}
