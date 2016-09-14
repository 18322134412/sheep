package com.xpple.sheep.ui;

import android.view.View;
import android.widget.TextView;

import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.R;
import com.xpple.sheep.StaticConfig;
import com.xpple.sheep.base.BaseOnlineActivity;
import com.xpple.sheep.util.PayUtils;
import com.xpple.sheep.view.ClearEditText;
import com.xpple.sheep.view.dialog.ActionSheetDialog;

import c.b.BP;

/**
 * 充值页
 *
 * @author nEdAy
 */
public class RechargeActivity extends BaseOnlineActivity {
    private ClearEditText et_money;

    @Override
    public int bindLayout() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("充值", "返回");
        et_money = (ClearEditText) findViewById(R.id.et_money);
        TextView tv_recharge = (TextView) findViewById(R.id.tv_recharge);
        TextView tv_log = (TextView) findViewById(R.id.tv_log);
        TextView tv_agree = (TextView) findViewById(R.id.tv_agree);
        // 初始化BmobPay对象,可以在支付时再初始化
        BP.init(CustomApplication.getInstance(), StaticConfig.BMOB_KEY);
        tv_recharge.setOnClickListener(v ->
                new ActionSheetDialog(mContext).builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("支付宝支付", ActionSheetDialog.SheetItemColor.Blue,
                                which -> PayUtils.payByAli(getPrice(), mContext))
                        .addSheetItem("微信支付", ActionSheetDialog.SheetItemColor.Blue,
                                which -> PayUtils.payByWeiXin(getPrice(), mContext)).show()
        );
        tv_agree.setOnClickListener(v -> getOperation().startWebActivity("http://neday.kuaizhan.com/73/90/p33314110237bf2", "充值提现服务协议"));
    }


    // 默认为0.02
    private double getPrice() {
        double price = 0.02;
        try {
            price = Double.parseDouble(et_money.getText().toString());
        } catch (NumberFormatException ignored) {
        }
        return price;
    }

}
