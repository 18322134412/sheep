package com.xpple.sheep.ui;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.R;
import com.xpple.sheep.StaticConfig;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.util.PayUtils;
import com.xpple.sheep.view.CircleAddAndSubView;
import com.xpple.sheep.view.dialog.ActionSheetDialog;

import c.b.BP;

/**
 * 打赏页
 *
 * @author nEdAy
 */

public class RewardActivity extends BaseActivity {
    private TextView tv_type;
    private Integer money, type;

    @Override
    public int bindLayout() {
        return R.layout.activity_reward;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        CircleAddAndSubView addAndSub = (CircleAddAndSubView) findViewById(R.id.addAndSub);
        SimpleDraweeView iv_avatar = (SimpleDraweeView) findViewById(R.id.iv_avatar);
        EditText et_message = (EditText) findViewById(R.id.et_message);
        tv_type = (TextView) findViewById(R.id.tv_type);
        TextView tv_type_change = (TextView) findViewById(R.id.tv_type_change);
        TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        BP.init(CustomApplication.getInstance(), StaticConfig.BMOB_KEY);
        addAndSub.setOnNumChangeListener((v, type, num) -> money = num);
        tv_type_change.setOnClickListener(v -> new ActionSheetDialog(mContext).builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("支付宝支付", ActionSheetDialog.SheetItemColor.Blue,
                        which -> {
                            tv_type.setText("使用支付宝支付,");
                            type = 0;
                        })
                .addSheetItem("微信支付", ActionSheetDialog.SheetItemColor.Blue,
                        which -> {
                            tv_type.setText("使用微信支付,");
                            type = 1;
                        })
                .addSheetItem("余额支付:", ActionSheetDialog.SheetItemColor.Blue,
                        which -> {
                            tv_type.setText("使用余额支付,");
                            type = 2;
                        }).show());
        tv_cancel.setOnClickListener(v -> finish());
        tv_confirm.setOnClickListener(v -> {
            switch (type) {
                case 0:
                    PayUtils.payByAli(money, mContext);
                    break;
                case 1:
                    PayUtils.payByWeiXin(money, mContext);
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        });
    }


}
