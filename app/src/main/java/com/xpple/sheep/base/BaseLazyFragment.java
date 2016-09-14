package com.xpple.sheep.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xpple.sheep.R;
import com.xpple.sheep.view.xlistview.SimpleFooter;
import com.xpple.sheep.view.xlistview.SimpleHeader;
import com.xpple.sheep.view.xlistview.ZrcListView;

public abstract class BaseLazyFragment extends BaseFragment {

    //Fragment当前状态是否可见
    protected boolean isVisible;
    //标志位，标志已经初始化完成
    protected boolean isPrepared;
    //是否已被加载过一次，第二次就不再去请求数据了
    protected boolean mHasLoadedOnce;

    protected View parentView;
    protected ZrcListView listView;
    protected RelativeLayout rl_no_data, rl_no_network;
    protected int curPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.include_anything_list, container, false);
        setUpView();
        isPrepared = true;
        lazyLoad();
        return parentView;
    }

    private void setUpView() {
        listView = (ZrcListView) parentView.findViewById(R.id.zlv_anything);
        rl_no_data = (RelativeLayout) parentView.findViewById(R.id.rl_no_data);
        rl_no_network = (RelativeLayout) parentView.findViewById(R.id.rl_no_network);
        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(getActivity());
        header.setTextColor(0xffe41e1e);
        header.setCircleColor(0xfffd5353);
        listView.setHeadable(header);
        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(getActivity());
        footer.setCircleColor(0xfffd5353);
        listView.setFootable(footer);
//        // 设置列表项出现动画（可选）
//        listView.setItemAnimForTopIn(R.anim.top_item_in);
//        listView.setItemAnimForBottomIn(R.anim.bottom_item_in);
        // 下拉刷新事件回调（可选）
        listView.setOnRefreshStartListener(this::QueryItem);
        // 加载更多事件回调（可选）
        listView.setOnLoadMoreStartListener(this::QueryCountItem);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            OnItemClickListener(position);
        });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {

    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();

    /**
     * 下拉刷新事件回调（可选）
     */
    protected void QueryItem() {
        rl_no_network.setVisibility(View.GONE);
        rl_no_data.setVisibility(View.GONE);
    }

    /**
     * 加载更多事件回调（可选）
     */
    protected abstract void QueryCountItem();

    /**
     * Item点击事件回调
     */
    protected abstract void OnItemClickListener(int position);

}
