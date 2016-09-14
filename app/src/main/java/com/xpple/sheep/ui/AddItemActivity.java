package com.xpple.sheep.ui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.bean.Item;

/**
 * 3.补全爆料好价页
 *
 * @author nEdAy
 */

public class AddItemActivity extends BaseActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_add_item;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForBoth("爆料好价", "返回", "提交", () -> {
            //提交
        });
        Spinner spinner_type = (Spinner) findViewById(R.id.spinner_type);

        Item item = (Item) getIntent().getSerializableExtra("item");
        spinner_type.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mType = getResources().getStringArray(R.array.item_type_array)[position].trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
//    if (TextUtils.isEmpty(this.a.getText().toString()))
//    {
//        ToastUtil.show(this, "请输入商品标题");
//        return;
//    }
//    if (TextUtils.isEmpty(this.b.getText().toString()))
//    {
//        ToastUtil.show(this, "请输入商城名字");
//        return;
//    }
//    if (TextUtils.isEmpty(this.c.getText().toString()))
//    {
//        ToastUtil.show(this, "请输入商品价格");
//        return;
//    }
//    if (TextUtils.isEmpty(this.o.getText().toString()))
//    {
//        ToastUtil.show(this, "请选择商品分类");
//        return;
//    }
//    if (TextUtils.isEmpty(this.d.getText().toString()))
//    {
//        ToastUtil.show(this, "请输入推荐理由");
//        return;
//    }

}
