package com.xpple.sheep.ui;

import android.view.View;

import com.xpple.sheep.R;
import com.xpple.sheep.adapter.NoticeAdapter;
import com.xpple.sheep.base.BaseListActivity;
import com.xpple.sheep.bean.Notice;
import com.xpple.sheep.proxy.NoticeProxy;
import com.xpple.sheep.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知公告页
 *
 * @author nEdAy
 */
public class NoticeActivity extends BaseListActivity implements NoticeProxy.IQueryItemListener, NoticeProxy.IQueryCountItemListener, NoticeProxy.IQueryMoreItemListener {
    private NoticeProxy noticeProxy;
    private NoticeAdapter adapter;
    private ArrayList<Notice> items = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.activity_notice;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("通知公告", "返回");
        initListView();
        adapter = new NoticeAdapter(mContext, items);
        listView.setAdapter(adapter);
        noticeProxy = new NoticeProxy();
        noticeProxy.setQueryItemListener(this);
        noticeProxy.setQueryCountItemListener(this);
        noticeProxy.setQueryMoreItemListener(this);
        // 主动查询
        listView.refresh();
    }

    @Override
    protected void QueryItem() {
        noticeProxy.QueryItem(true);
    }

    @Override
    protected void QueryCountItem() {
        noticeProxy.QueryCountOrder();
    }

    @Override
    protected void OnItemClickListener(int position) {
        getOperation().startWebActivity(items.get(position).getUrl(), items.get(position).getTitle());
    }

    @Override
    public void onQueryItemSuccess(List<Notice> object, boolean isUpdate) {
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
    public void onQueryCountOrderSuccess(String count) {
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
    public void onQueryCountOrderFailure() {
        listView.stopLoadMore();
    }

    /**
     * 查询更多-2
     */

    private void queryMoreNearList(int page) {
        noticeProxy.QueryMoreItem(page);
    }

    @Override
    public void onQueryMoreItemSuccess(List<Notice> object) {
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
