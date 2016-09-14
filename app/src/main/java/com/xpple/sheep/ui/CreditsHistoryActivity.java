package com.xpple.sheep.ui;

import android.view.View;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.adapter.CreditsDetailsAdapter;
import com.xpple.sheep.base.BaseListActivity;
import com.xpple.sheep.bean.CreditsHistory;
import com.xpple.sheep.proxy.CreditsDetailsProxy;
import com.xpple.sheep.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 积分历史页
 *
 * @author nEdAy
 */
public class CreditsHistoryActivity extends BaseListActivity implements CreditsDetailsProxy.IQueryItemListener, CreditsDetailsProxy.IQueryCountItemListener, CreditsDetailsProxy.IQueryMoreItemListener {
    private CreditsDetailsProxy creditsDetailsProxy;
    private CreditsDetailsAdapter adapter;
    private ArrayList<CreditsHistory> items = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_credits_history;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForBoth("积分明细", "返回", "规则说明", () -> {
            getOperation().startWebActivity("http://neday.kuaizhan.com/89/30/p33312925275824", "规则说明");
        });
        initListView();
        TextView tv_credits = (TextView) findViewById(R.id.tv_credits);

        adapter = new CreditsDetailsAdapter(mContext, items);
        listView.setAdapter(adapter);
        creditsDetailsProxy = new CreditsDetailsProxy();
        creditsDetailsProxy.setQueryItemListener(this);
        creditsDetailsProxy.setQueryCountItemListener(this);
        creditsDetailsProxy.setQueryMoreItemListener(this);
        // 主动查询
        listView.refresh();
    }

    @Override
    protected void QueryItem() {
        creditsDetailsProxy.QueryItem(true);
    }

    @Override
    protected void QueryCountItem() {
        creditsDetailsProxy.QueryCountItem();
    }

    @Override
    protected void OnItemClickListener(int position) {

    }

    @Override
    public void onQueryItemSuccess(List<CreditsHistory> object, boolean isUpdate) {
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
        creditsDetailsProxy.QueryMoreItem(page);
    }

    @Override
    public void onQueryMoreItemSuccess(List<CreditsHistory> object) {
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
