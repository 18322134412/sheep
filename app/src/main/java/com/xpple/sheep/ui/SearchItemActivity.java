package com.xpple.sheep.ui;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.view.ClearEditText;
import com.xpple.sheep.view.dialog.AlertDialog;

/**
 * 2.搜索爆料好价页
 *
 * @author nEdAy
 */

public class SearchItemActivity extends BaseActivity {
    private TextView tv_get;

    @Override
    public int bindLayout() {
        return R.layout.activity_search_item;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("爆料好价", "返回");
        tv_get = (TextView) findViewById(R.id.tv_get);
        ClearEditText et_search = (ClearEditText) findViewById(R.id.et_search);
        et_search.addTextChangedListener(mSearchWatcher);
        tv_get.setOnClickListener(vv -> {
//            if(false){
//            Item item = new Item();
//            Intent intent = new Intent(mContext,
//                    ItemDetailsActivity.class);
//            intent.putExtra("item", item);
//            startActivity(intent);
//            }else {
            new AlertDialog(mContext).builder().setTitle("提示").setMsg("该链接暂时无法获取信息，您可以手动填写信息填报")
                    .setPositiveButton("手动填写", v -> {
                        getOperation().startActivity(AddItemActivity.class);
                    })
                    .setNegativeButton("重新获取", v -> {
                                tv_get.performClick();
                            }
                    ).show();
//            }
        });
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
                tv_get.setEnabled(true);
            }
        }
    };
}
