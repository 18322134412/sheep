package com.xpple.sheep.ui.itemFragment;

import android.content.Intent;
import android.view.View;

import com.xpple.sheep.adapter.ItemAdapter;
import com.xpple.sheep.base.BaseLazyFragment;
import com.xpple.sheep.bean.Item;
import com.xpple.sheep.proxy.ItemProxy;
import com.xpple.sheep.ui.ItemDetailsActivity;
import com.xpple.sheep.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemFragmentRightF extends BaseLazyFragment implements ItemProxy.IQueryItemListener, ItemProxy.IQueryCountItemListener, ItemProxy.IQueryMoreItemListener {
    private ItemProxy queryProxy;
    private ItemAdapter adapter;
    private List<Item> items = new ArrayList<>();

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        queryProxy = new ItemProxy();
        queryProxy.setQueryItemListener(this);
        queryProxy.setQueryCountItemListener(this);
        queryProxy.setQueryMoreItemListener(this);
        // 主动查询
        listView.refresh();
    }

    /**
     * 点击项目记录回调,主动点击查看详情
     */

    @Override
    protected void OnItemClickListener(int position) {
        Intent item = new Intent(getActivity(),
                ItemDetailsActivity.class);
        item.putExtra("item", items.get(position));
        startActivity(item);
    }

    /**
     * 下拉刷新事件回调,主动查询/刷新查询
     */

    @Override
    protected void QueryItem() {
        super.QueryItem();  //隐藏无网络和无数据界面
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("include", "_User[objectId|nickname|avatar]");
        String where = "[{\"key\":\"hot\",\"value\":\"10\",\"operation\":\"=\",\"relation\":\"\"}]";
        queryMap.put("where", where);
        queryProxy.QueryItem(true, queryMap);
    }

    @Override
    public void onQueryItemSuccess(List<Item> object, boolean isUpdate) {
        curPage = 0;//重置页码
        adapter = new ItemAdapter(getActivity(), items);
        listView.setAdapter(adapter);
        if (CommonUtils.isNotNull(object)) {
            if (isUpdate) {
                items.clear();
            }
            items = object;
            adapter.addAll(items);
            if (items.size() < 10) {
                listView.setRefreshSuccess("加载完成"); // 通知加载完成
            } else {
                listView.setRefreshSuccess(); // 通知刷新成功
                listView.startLoadMore(); // 开启LoadingMore功能
            }
        } else {
            listView.setRefreshSuccess("暂无数据");
            rl_no_data.setVisibility(View.VISIBLE);
            rl_no_data.setOnClickListener(v -> QueryItem());
        }
        adapter.notifyDataSetChanged();
        mHasLoadedOnce = true;
    }

    @Override
    public void onQueryItemFailure() {
        rl_no_network.setVisibility(View.VISIBLE);
        rl_no_network.setOnClickListener(v -> QueryItem());
        listView.setRefreshFail();// 通知刷新失败
    }

    /**
     * 加载更多事件回调,查询总数
     */

    @Override
    protected void QueryCountItem() {
        Map<String, Object> queryMap = new HashMap<>();
        String where = "[{\"key\":\"hot\",\"value\":\"10\",\"operation\":\"=\",\"relation\":\"\"}]";
        queryMap.put("where", where);
        queryProxy.QueryCountItem(queryMap);
    }

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
     * 加载更多事件回调,查询更多
     */

    private void queryMoreNearList(int page) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("include", "_User[objectId|nickname|avatar]");
        String where = "[{\"key\":\"hot\",\"value\":\"10\",\"operation\":\"=\",\"relation\":\"\"}]";
        queryMap.put("where", where);
        queryProxy.QueryMoreItem(page, queryMap);
    }

    @Override
    public void onQueryMoreItemSuccess(List<Item> object) {
        if (CommonUtils.isNotNull(object)) {
            items.addAll(object);
            adapter.addAll(object);
        }
        adapter.notifyDataSetChanged();
        listView.setLoadMoreSuccess();
    }

    @Override
    public void onQueryMoreItemFailure(String message) {
        listView.stopLoadMore();
    }

}

