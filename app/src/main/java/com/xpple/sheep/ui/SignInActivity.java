package com.xpple.sheep.ui;

import android.view.View;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseOnlineActivity;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.UserProxy;

/**
 * 签到页
 *
 * @author nEdAy
 */
public class SignInActivity extends BaseOnlineActivity implements UserProxy.IQueryListener {
    private TextView tv_sign_in_1, tv_sign_in_2, tv_sign_in_3;
    private Boolean sign_in;

    @Override
    public int bindLayout() {
        return R.layout.activity_sign_in;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForBoth("每日签到", "返回", "规则说明", () ->
            getOperation().startWebActivity( "", "规则说明")
        );
        tv_sign_in_1 = (TextView) findViewById(R.id.tv_sign_in_1);
        tv_sign_in_2 = (TextView) findViewById(R.id.tv_sign_in_2);
        tv_sign_in_3 = (TextView) findViewById(R.id.tv_sign_in_3);
        UserProxy userProxy = getUserProxy();
        userProxy.setOnQueryListener(this);
        userProxy.getUserSignIn(userProxy.getCurrentUser().getObjectId());
        tv_sign_in_1.setOnClickListener(v -> {
            getOperation().startActivity(SignInOutActivity.class);//每天稳赚2积分
        });
        tv_sign_in_2.setOnClickListener(v -> {
            getOperation().startActivity(SignInOutActivity.class);//消耗1分，领1 ~ 5积分
        });
        tv_sign_in_3.setOnClickListener(v -> {
            getOperation().startActivity(SignInOutActivity.class);//随即领取-2 ~ 8积分
        });
    }

    @Override
    public void onQuerySuccess(User user) {
        if(user.getSign_in()){
            tv_sign_in_1.setEnabled(true);
            tv_sign_in_2.setEnabled(true);
            tv_sign_in_3.setEnabled(true);
        }
    }

    @Override
    public void onQueryFailure() {

    }
}
