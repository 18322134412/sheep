package com.xpple.sheep.ui.mainFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseFragment;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.ui.HotActivity;
import com.xpple.sheep.ui.ItemPassActivity;
import com.xpple.sheep.ui.SearchActivity;
import com.xpple.sheep.ui.itemFragment.ItemFragmentLeft;
import com.xpple.sheep.ui.itemFragment.ItemFragmentRight;

import java.util.ArrayList;

public class ItemFragment extends BaseFragment implements View.OnClickListener {
    private View parentView;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"口袋快爆", "羊毛资讯"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_main_item, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        // 配合状态栏下移
        setStatusBarHeight(parentView);
        TextView tv_hot = (TextView) parentView.findViewById(R.id.tv_hot);
        TextView tv_search = (TextView) parentView.findViewById(R.id.tv_search);
        tv_hot.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        mFragments.add(new ItemFragmentLeft());
        mFragments.add(new ItemFragmentRight());
        SegmentTabLayout tabLayout = (SegmentTabLayout) parentView.findViewById(R.id.tl_item);
        tabLayout.setTabData(mTitles, getActivity(), R.id.fl_item, mFragments);
    }

    @Override
    public void onResume() {
        super.onResume();
        FloatingActionButton fab = (FloatingActionButton) parentView.findViewById(R.id.fab);
        UserProxy userProxy = new UserProxy(getActivity());
        if (userProxy.getCurrentUser() == null) {
            fab.setVisibility(View.GONE);
        } else {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hot:
                getOperation().startActivity(HotActivity.class);
                break;
            case R.id.tv_search:
                getOperation().startActivity(SearchActivity.class);
                break;
            case R.id.fab:
                getOperation().startActivity(ItemPassActivity.class);
                break;
            default:
                break;
        }
    }


}
