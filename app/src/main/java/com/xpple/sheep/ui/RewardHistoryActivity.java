package com.xpple.sheep.ui;

import android.view.View;

import com.xpple.sheep.R;
import com.xpple.sheep.adapter.OverageDetailsAdapter;
import com.xpple.sheep.base.BaseListActivity;
import com.xpple.sheep.bean.OverageHistory;
import com.xpple.sheep.proxy.OverageDetailsProxy;
import com.xpple.sheep.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 打赏记录页
 *
 * @author nEdAy
 */
public class RewardHistoryActivity  extends BaseListActivity implements OverageDetailsProxy.IQueryItemListener, OverageDetailsProxy.IQueryCountItemListener, OverageDetailsProxy.IQueryMoreItemListener {

    private OverageDetailsProxy overageDetailsProxy;
    private OverageDetailsAdapter adapter;
    private ArrayList<OverageHistory> items = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_reward_history;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("打赏记录", "返回");
        initListView();
        adapter = new OverageDetailsAdapter(mContext, items);
        listView.setAdapter(adapter);
        overageDetailsProxy = new OverageDetailsProxy();
        overageDetailsProxy.setQueryItemListener(this);
        overageDetailsProxy.setQueryCountItemListener(this);
        overageDetailsProxy.setQueryMoreItemListener(this);
        // 主动查询
        listView.refresh();
    }

    @Override
    protected void QueryItem() {
        overageDetailsProxy.QueryItem(true);
    }

    @Override
    protected void QueryCountItem() {
        overageDetailsProxy.QueryCountItem();
    }

    @Override
    protected void OnItemClickListener(int position) {

    }

    @Override
    public void onQueryItemSuccess(List<OverageHistory> object, boolean isUpdate) {
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
        overageDetailsProxy.QueryMoreItem(page);
    }

    @Override
    public void onQueryMoreItemSuccess(List<OverageHistory> object) {
        if (CommonUtils.isNotNull(object)) {
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
