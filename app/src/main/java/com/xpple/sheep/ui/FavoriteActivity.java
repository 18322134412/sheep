package com.xpple.sheep.ui;

import android.content.Intent;
import android.view.View;

import com.xpple.sheep.R;
import com.xpple.sheep.adapter.ItemAdapter;
import com.xpple.sheep.base.BaseListActivity;
import com.xpple.sheep.bean.Item;
import com.xpple.sheep.proxy.ItemProxy;
import com.xpple.sheep.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏页
 *
 * @author nEdAy
 */
public class FavoriteActivity extends BaseListActivity implements ItemProxy.IQueryItemListener, ItemProxy.IQueryCountItemListener, ItemProxy.IQueryMoreItemListener {
    private ItemProxy queryProxy;
    private ItemAdapter adapter;
    private List<Item> items = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_favorite;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("我的收藏", "返回");
        initListView();

        queryProxy = new ItemProxy();
        queryProxy.setQueryItemListener(this);
        // 主动查询
        listView.refresh();
    }

    @Override
    protected void QueryItem() {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("include", "author[objectId|nickname|avatar]");
        queryProxy.QueryItem(true, queryMap);
    }

    @Override
    protected void QueryCountItem() {
        Map<String, Object> queryMap = new HashMap<>();
        queryProxy.QueryCountItem(queryMap);
    }

    @Override
    protected void OnItemClickListener(int position) {
        Intent item = new Intent(mContext,
                ItemDetailsActivity.class);
        item.putExtra("item", items.get(position));
        startActivity(item);
    }

    /**
     * 主动查询/刷新查询
     */

    @Override
    public void onQueryItemSuccess(List<Item> object, boolean isUpdate) {
        curPage = 0;//重置页码
        adapter = new ItemAdapter(mContext, items);
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
                queryProxy.setQueryCountItemListener(this);
                listView.startLoadMore(); // 开启LoadingMore功能
            }
        } else {
            listView.setRefreshSuccess("暂无数据");
            rl_no_network.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.VISIBLE);
            rl_no_data.setOnClickListener(v -> QueryItem());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onQueryItemFailure() {
        rl_no_network.setVisibility(View.VISIBLE);
        rl_no_network.setOnClickListener(v -> QueryItem());
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
        queryProxy.setQueryMoreItemListener(this);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("include", "author[objectId|nickname|avatar]");
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
