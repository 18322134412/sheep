package com.xpple.sheep.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.adapter.ItemAdapter;
import com.xpple.sheep.base.BaseListActivity;
import com.xpple.sheep.bean.Item;
import com.xpple.sheep.proxy.ItemProxy;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.view.ClearEditText;
import com.xpple.sheep.view.KeywordsFlow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SearchActivity extends BaseListActivity implements ItemProxy.IQueryItemListener, ItemProxy.IQueryCountItemListener, ItemProxy.IQueryMoreItemListener {
    public static final String SEARCH_HISTORY = "search_history";
    private ClearEditText et_search;
    private TextView tv_search;
    private KeywordsFlow flow;
    private static final int FEEDER_START = 1;
    private int STATE = 1;
    private boolean isSearch;
    private ItemProxy queryProxy;
    private ItemAdapter adapter;
    private List<Item> items = new ArrayList<>();

    private String[] keywords = new String[]{"单买好价", "日用",
            "母婴", "数码", "美妆", "图书", "运动", "亚马逊", "京东",
            "聚美优品", "历史新低", "优惠券", "包邮", "支付宝", "微信", "流量", "好价", "双重优惠",
            "满减", "0撸", "低价折扣", "苏宁", "凑单满减", " 天猫", " 我买网", "移动端"};

    @Override
    public int bindLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("搜索", "返回");
        initListView();
        et_search = (ClearEditText) findViewById(R.id.et_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        flow = (KeywordsFlow) findViewById(R.id.flow);
        et_search.addTextChangedListener(mSearchWatcher);
        // 先隐藏列表 等待搜索结果
        listView.setVisibility(View.GONE);
        initSearchHistory();
        flow.setDuration(1000L);
        flow.setOnItemClickListener(v -> {
            if (view instanceof TextView) {
                String keyword = ((TextView) view).getText().toString().trim();
                et_search.setText(keyword);
                et_search.setSelection(keyword.length());
                tv_search.performClick();//自動模擬點擊操作
            }
        });
        flow.go2Show(KeywordsFlow.ANIMATION_IN);
        handler.sendEmptyMessageDelayed(FEEDER_START, 5000);
        tv_search.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(et_search.getText())) {
                saveSearchHistory();
                queryProxy = new ItemProxy();
                queryProxy.setQueryItemListener(this);
                // 主动查询
                listView.refresh();
                /**隐藏软键盘**/
                View _view = getWindow().peekDecorView();
                if (_view != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(_view.getWindowToken(), 0);
                }
            }
        });
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

    /*
    * 保存搜索记录
    */
    private void saveSearchHistory() {
        String text = et_search.getText().toString().trim();
        if (text.length() < 1) {
            return;
        }
        SharedPreferences sp = getSharedPreferences(SEARCH_HISTORY, 0);
        String long_history = sp.getString(SEARCH_HISTORY, "");
        String[] tmpHistory = long_history.split(",");
        ArrayList<String> history = new ArrayList<>(
                Arrays.asList(tmpHistory));
        if (history.size() > 0) {
            int i;
            for (i = 0; i < history.size(); i++) {
                if (text.equals(history.get(i))) {
                    history.remove(i);
                    break;
                }
            }
            history.add(0, text);
        }
        if (history.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < history.size(); i++) {
                sb.append(history.get(i)).append(",");
            }
            sp.edit().putString(SEARCH_HISTORY, sb.toString()).apply();
        } else {
            sp.edit().putString(SEARCH_HISTORY, text + ",").apply();
        }
    }

    /**
     * 读取历史搜索记录
     */
    public void initSearchHistory() {
        SharedPreferences sp = mContext.getSharedPreferences(
                SEARCH_HISTORY, 0);
        String long_history = sp.getString(SEARCH_HISTORY, "");
        String[] hisArrays = long_history.split(",");
        if (hisArrays.length <= 1) {
            return;
        }
        keywords = hisArrays;
        feedKeywordsFlow(flow, keywords);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case FEEDER_START:
                    flow.rubKeywords();
                    feedKeywordsFlow(flow, keywords);
                    flow.go2Show(KeywordsFlow.ANIMATION_OUT);
                    sendEmptyMessageDelayed(FEEDER_START, 5000);
                    break;
                default:
                    break;
            }
        }
    };

    private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {
        Random random = new Random();
        for (int i = 0; i < KeywordsFlow.MAX; i++) {
            int ran = random.nextInt(arr.length);
            String tmp = arr[ran];
            keywordsFlow.feedKeyword(tmp);
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (!isSearch) {
            super.onBackPressed();
        } else {
            listView.setVisibility(View.GONE);
            flow.setVisibility(View.VISIBLE);
            if (STATE == 0) {
                flow.rubKeywords();
                handler.sendEmptyMessageDelayed(FEEDER_START, 3000);
            }
            isSearch = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(FEEDER_START);
        STATE = 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeMessages(FEEDER_START);
        STATE = 0;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeMessages(FEEDER_START);
        STATE = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (STATE == 0) {
            flow.rubKeywords();
            handler.sendEmptyMessageDelayed(FEEDER_START, 3000);
        }
    }

    /**
     * 主动查询/刷新查询
     */

    @Override
    public void onQueryItemSuccess(List<Item> object, boolean isUpdate) {
        isSearch = true;
        listView.setVisibility(View.VISIBLE);
        flow.setVisibility(View.GONE);
        handler.removeMessages(FEEDER_START);
        STATE = 0;
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

    TextWatcher mSearchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                tv_search.setEnabled(true);
            }
        }
    };

}
