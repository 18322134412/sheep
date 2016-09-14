package com.xpple.sheep.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xpple.sheep.R;
import com.xpple.sheep.adapter.ActivityAdapter;
import com.xpple.sheep.base.BaseFragment;
import com.xpple.sheep.bean.Activity;
import com.xpple.sheep.proxy.ActivityProxy;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.view.xlistview.SimpleFooter;
import com.xpple.sheep.view.xlistview.SimpleHeader;
import com.xpple.sheep.view.xlistview.ZrcListView;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends BaseFragment implements ActivityProxy.IQueryItemListener, ActivityProxy.IQueryCountItemListener, ActivityProxy.IQueryMoreItemListener {
    private View parentView;
    private ZrcListView listView;
    private ActivityProxy queryProxy;
    private ActivityAdapter adapter;
    private List<Activity> items = new ArrayList<>();
    private int curPage;
    private static final String FRAGMENT_INDEX = "fragment_index";
    private final static int FIRST_FRAGMENT = 0;
    private final static int SECOND_FRAGMENT = 1;
    private int mCurIndex = -1;
    protected RelativeLayout rl_no_data, rl_no_network;

    /**
     * 创建新实例
     */
    public static ActivityFragment newInstance(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, index);
        ActivityFragment fragment = new ActivityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (parentView == null) {
            parentView = inflater.inflate(R.layout.include_anything_list, container, false);
            //获得索引值
            Bundle bundle = getArguments();
            if (bundle != null) {
                mCurIndex = bundle.getInt(FRAGMENT_INDEX);
            }
            setUpView();
            queryProxy = new ActivityProxy();
            queryProxy.setQueryItemListener(this);
            queryProxy.setQueryCountItemListener(this);
            queryProxy.setQueryMoreItemListener(this);
            // 主动查询
            listView.refresh();
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) parentView.getParent();
        if (parent != null) {
            parent.removeView(parentView);
        }
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
        switch (mCurIndex) {
            case FIRST_FRAGMENT:
                // 下拉刷新事件回调（可选）
                listView.setOnRefreshStartListener(() -> queryProxy.QueryItem(true));
                // 加载更多事件回调（可选）
                listView.setOnLoadMoreStartListener(() -> queryProxy.QueryCountItem());
                break;
            case SECOND_FRAGMENT:
                // 下拉刷新事件回调（可选）
                listView.setOnRefreshStartListener(() -> queryProxy.QueryItem(true));
                // 加载更多事件回调（可选）
                listView.setOnLoadMoreStartListener(() -> queryProxy.QueryCountItem());
                break;
        }
        listView.setOnItemClickListener((parent, view, position, id) ->
                getOperation().startWebActivity(items.get(position).getUrl(), items.get(position).getTitle())
        );
    }

    /**
     * 主动查询/刷新查询
     */

    @Override
    public void onQueryItemSuccess(List<Activity> object, boolean isUpdate) {
        curPage = 0;//重置页码
        adapter = new ActivityAdapter(getActivity(), items);
        listView.setAdapter(adapter);
        if (CommonUtils.isNotNull(object)) {
            rl_no_network.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.GONE);
            if (isUpdate) {
                items.clear();
            }
            items = object;
            adapter.addAll(items);
            if (items.size() < 10) {
                listView.setRefreshSuccess("加载完成"); // 通知加载完成
                listView.stopLoadMore();
            } else {
                listView.setRefreshSuccess(); // 通知刷新成功
                listView.startLoadMore(); // 开启LoadingMore功能
            }
        } else {
            listView.setRefreshSuccess("暂无数据");
            rl_no_network.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.VISIBLE);
            rl_no_data.setOnClickListener(v -> queryProxy.QueryItem(true));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onQueryItemFailure() {
        rl_no_network.setVisibility(View.VISIBLE);
        rl_no_network.setOnClickListener(v -> queryProxy.QueryItem(true));
        listView.setRefreshFail();// 通知刷新失败
    }

    /**
     * 查询总数
     */

    @Override
    public void onQueryCountItemSuccess(String count) {
        if (Integer.parseInt(count) > items.size()) {
            curPage++;
            queryMoreNearList(curPage);
        } else {
            listView.stopLoadMore();
        }
        listView.setLoadMoreSuccess(); // 通知加载完成
    }

    @Override
    public void onQueryCountItemFailure() {
        listView.stopLoadMore();
    }

    /**
     * 查询更多
     */

    private void queryMoreNearList(int page) {
        switch (mCurIndex) {
            case FIRST_FRAGMENT:
                queryProxy.QueryMoreItem(page);
                break;
            case SECOND_FRAGMENT:
                queryProxy.QueryMoreItem(page);
                break;
        }
    }

    @Override
    public void onQueryMoreItemSuccess(List<Activity> object) {
        if (CommonUtils.isNotNull(object)) {
            items.addAll(object);
            adapter.addAll(object);
        }
        adapter.notifyDataSetChanged();
        listView.setLoadMoreSuccess();
    }

    @Override
    public void onQueryMoreItemFailure() {
        listView.stopLoadMore();
    }


}

