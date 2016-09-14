package com.xpple.sheep.ui;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;

import java.util.ArrayList;

public class ActivityActivity extends BaseActivity {
    private static final int FIRST_FRAGMENT = 0;
    private static final int SECOND_FRAGMENT = 1;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "进行中", "已结束"
    };

    @Override
    public int bindLayout() {
        return R.layout.activity_activity;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("活动中心", "返回");
        ViewPager vp_paper = (ViewPager) findViewById(R.id.vpActivityPaper);
        vp_paper.setOffscreenPageLimit(1);
        mFragments.add(ActivityFragment.newInstance(FIRST_FRAGMENT));
        mFragments.add(ActivityFragment.newInstance(SECOND_FRAGMENT));
        vp_paper.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        SlidingTabLayout tabLayout = (SlidingTabLayout) findViewById(R.id.tl_activity);
        tabLayout.setViewPager(vp_paper);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

}
