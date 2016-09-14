package com.xpple.sheep.ui;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseOnlineActivity;

/**
 * 余额管理页
 *
 * @author nEdAy
 */
public class OverageActivity extends BaseOnlineActivity {
    private TextView tv_overage,
            tv_overage_enable, tv_recharge_history,
            tv_overage_disable, tv_withdrawals_history;
    private LinearLayout ll_recharge, ll_withdrawals;

    @Override
    public int bindLayout() {
        return R.layout.activity_overage;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("账户余额", "返回");
        tv_overage = (TextView) findViewById(R.id.tv_overage);
        tv_overage_enable = (TextView) findViewById(R.id.tv_overage_enable);
        tv_recharge_history = (TextView) findViewById(R.id.tv_recharge_history);
        tv_overage_disable = (TextView) findViewById(R.id.tv_overage_disable);
        tv_withdrawals_history = (TextView) findViewById(R.id.tv_withdrawals_history);
        ll_recharge = (LinearLayout) findViewById(R.id.ll_recharge);
        ll_withdrawals = (LinearLayout) findViewById(R.id.ll_withdrawals);
        //充值记录页
        tv_recharge_history.setOnClickListener(v -> getOperation().startActivity(RechargeHistoryActivity.class));
        //提现记录页
        tv_withdrawals_history.setOnClickListener(v -> getOperation().startActivity(WithdrawalsHistoryActivity.class));
        //充值操作页
        ll_recharge.setOnClickListener(v -> getOperation().startActivity(RechargeActivity.class));
        //提现操作页
        ll_withdrawals.setOnClickListener(v -> getOperation().startActivity(WithdrawalsActivity.class));
    }


}
