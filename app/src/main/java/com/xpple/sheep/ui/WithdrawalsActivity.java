package com.xpple.sheep.ui;

import android.view.View;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseOnlineActivity;
import com.xpple.sheep.view.sKB.PasswordEditText;

/**
 * 提现操作页
 *
 * @author nEdAy
 */
public class WithdrawalsActivity extends BaseOnlineActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_withdrawals;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("提现", "返回");
        PasswordEditText et_password = (PasswordEditText) findViewById(R.id.et_password);
        TextView tv_agree = (TextView) findViewById(R.id.tv_agree);
        tv_agree.setOnClickListener(v -> getOperation().startWebActivity("http://neday.kuaizhan.com/73/90/p33314110237bf2", "充值提现服务协议"));
    }


}
