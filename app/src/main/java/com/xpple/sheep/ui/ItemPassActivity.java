package com.xpple.sheep.ui;

import android.content.Intent;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.xpple.sheep.R;
import com.xpple.sheep.adapter.ItemPassAdapter;
import com.xpple.sheep.base.BaseListActivity;
import com.xpple.sheep.bean.Item;
import com.xpple.sheep.proxy.ItemProxy;
import com.xpple.sheep.util.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1.我的爆料页
 *
 * @author nEdAy
 */

public class ItemPassActivity extends BaseListActivity implements ItemProxy.IQueryItemListener, ItemProxy.IQueryCountItemListener, ItemProxy.IQueryMoreItemListener {
    private ItemProxy queryProxy;
    private ItemPassAdapter adapter;
    private ArrayList<Item> items = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_item_pass;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("我的爆料", "返回");
        initListView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v -> getOperation().startActivity(SearchItemActivity.class));
        adapter = new ItemPassAdapter(mContext, items);
        listView.setAdapter(adapter);
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

    @Override
    public void onQueryItemSuccess(List<Item> object, boolean isUpdate) {
        curPage = 0;//重置页码
        if (CommonUtils.isNotNull(object)) {
            rl_no_network.setVisibility(View.GONE);
            rl_no_data.setVisibility(View.GONE);
            if (isUpdate) {
                items.clear();
            }
            adapter.addAll(object);
            if (object.size() < 10) {
                listView.setRefreshSuccess("加载完成"); // 通知加载完成
                listView.stopLoadMore();
            } else {
                listView.setRefreshSuccess(); // 通知刷新成功
                queryProxy.setQueryMoreItemListener(this);
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
     * 查询更多-1
     */

    @Override
    public void onQueryCountItemSuccess(String count) {
        try {
            if (Integer.parseInt(count) > items.size()) {
                curPage++;
                queryMoreNearList(curPage);
            } else {
                listView.stopLoadMore();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onQueryCountItemFailure() {
        listView.stopLoadMore();
    }

    /**
     * 查询更多-2
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
