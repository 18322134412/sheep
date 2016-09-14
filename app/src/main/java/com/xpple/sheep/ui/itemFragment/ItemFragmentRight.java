package com.xpple.sheep.ui.itemFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseFragment;

import java.util.ArrayList;

public class ItemFragmentRight extends BaseFragment {
    private View parentView;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "电脑数码", "家用电器", "户外运动", "服饰鞋包"
            , "个护化妆", "母婴用品"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_main_item_right, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        ViewPager vp_paper = (ViewPager) parentView.findViewById(R.id.vpItemRightPaper);
        vp_paper.setOffscreenPageLimit(5);
        mFragments.add(new ItemFragmentRightA());
        mFragments.add(new ItemFragmentRightB());
        mFragments.add(new ItemFragmentRightC());
        mFragments.add(new ItemFragmentRightD());
        mFragments.add(new ItemFragmentRightE());
        mFragments.add(new ItemFragmentRightF());
        vp_paper.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        SlidingTabLayout tabLayout = (SlidingTabLayout) parentView.findViewById(R.id.tl_item_right);

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
