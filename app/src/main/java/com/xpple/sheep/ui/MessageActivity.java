package com.xpple.sheep.ui;

import android.view.View;

import com.xpple.sheep.R;
import com.xpple.sheep.adapter.MessageAdapter;
import com.xpple.sheep.base.BaseListActivity;
import com.xpple.sheep.bean.Message;
import com.xpple.sheep.proxy.MessageProxy;
import com.xpple.sheep.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 私信页
 *
 * @author nEdAy
 */
public class MessageActivity extends BaseListActivity implements MessageProxy.IQueryMessageListener, MessageProxy.IQueryCountMessageListener, MessageProxy.IQueryMoreMessageListener {
    private MessageProxy messageProxy;
    private MessageAdapter adapter;
    private ArrayList<Message> items = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_message;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("消息列表", "返回");
        initListView();
        adapter = new MessageAdapter(mContext, items);
        listView.setAdapter(adapter);
        messageProxy = new MessageProxy();
        messageProxy.setQueryOrderListener(this);
        messageProxy.setQueryCountOrderListener(this);
        messageProxy.setQueryMoreOrderListener(this);
        // 主动查询
        listView.refresh();
    }


    @Override
    protected void QueryItem() {
        messageProxy.QueryOrder(true);
    }

    @Override
    protected void QueryCountItem() {
        messageProxy.QueryCountOrder();
    }

    @Override
    protected void OnItemClickListener(int position) {

    }

    @Override
    public void onQueryMessageSuccess(List<Message> object, boolean isUpdate) {
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
    public void onQueryMessageFailure() {
        rl_no_network.setVisibility(View.VISIBLE);
        rl_no_network.setOnClickListener(v -> QueryItem());
        listView.setRefreshFail();// 通知刷新失败
    }

    /**
     * 查询更多-1
     */

    @Override
    public void onQueryCountMessageSuccess(String count) {
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
    public void onQueryCountMessageFailure() {
        listView.stopLoadMore();
    }

    /**
     * 查询更多-2
     */

    private void queryMoreNearList(int page) {
        messageProxy.QueryMoreOrder(page);
    }

    @Override
    public void onQueryMoreMessageSuccess(List<Message> object) {
        if (CommonUtils.isNotNull(object)) {
            adapter.addAll(object);
        }
        adapter.notifyDataSetChanged();
        listView.setLoadMoreSuccess();
    }

    @Override
    public void onQueryMoreMessageFailure() {
        listView.stopLoadMore();
    }

}
