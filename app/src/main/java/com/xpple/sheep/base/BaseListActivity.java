package com.xpple.sheep.base;

import android.widget.RelativeLayout;

import com.xpple.sheep.R;
import com.xpple.sheep.view.xlistview.SimpleFooter;
import com.xpple.sheep.view.xlistview.SimpleHeader;
import com.xpple.sheep.view.xlistview.ZrcListView;

//import com.xpple.sheep.proxy.UserProxy;


/**
 * 列表Activity基類
 *
 * @author nEdAy
 */
public abstract class BaseListActivity extends BaseActivity {
    protected ZrcListView listView;
    protected RelativeLayout rl_no_data, rl_no_network;
    protected int curPage;

    protected void initListView() {
        listView = (ZrcListView) findViewById(R.id.zlv_anything);
        rl_no_data = (RelativeLayout) findViewById(R.id.rl_no_data);
        rl_no_network = (RelativeLayout) findViewById(R.id.rl_no_network);
        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(mContext);
        header.setTextColor(0xffe41e1e);
        header.setCircleColor(0xfffd5353);
        listView.setHeadable(header);
        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(mContext);
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

    /**
     * 下拉刷新事件回调（可选）
     */
    protected abstract void QueryItem();

    /**
     * 加载更多事件回调（可选）
     */
    protected abstract void QueryCountItem();

    /**
     * Item点击事件回调
     */
    protected abstract void OnItemClickListener(int position);

}
