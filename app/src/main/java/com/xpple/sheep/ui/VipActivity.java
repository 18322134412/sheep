package com.xpple.sheep.ui;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseOnlineActivity;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.IntegrationShopProxy;
import com.xpple.sheep.proxy.UserProxy;

/**
 * 会员中心页
 *
 * @author nEdAy
 */
public class VipActivity extends BaseOnlineActivity implements UserProxy.IQueryListener {
    private TextView tv_integration_shop, tv_sign_in, tv_nickname, tv_credit;
    private SimpleDraweeView riv_avatar;

    @Override
    public int bindLayout() {
        return R.layout.activity_vip;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForBoth("会员中心", "返回", "等级说明", () ->
                getOperation().startActivity(VipHelpActivity.class)
        );
        tv_integration_shop = (TextView) findViewById(R.id.tv_integration_shop);
        tv_sign_in = (TextView) findViewById(R.id.tv_sign_in);
        riv_avatar = (SimpleDraweeView) findViewById(R.id.riv_avatar);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_credit = (TextView) findViewById(R.id.tv_credit);
        ImageView iv_vip = (ImageView) findViewById(R.id.iv_vip);
        UserProxy userProxy = getUserProxy();
        User user = userProxy.getCurrentUser();
        if (user != null) {
            userProxy.setOnQueryListener(this);
            userProxy.query(user.getObjectId());
        } else {
            getOperation().startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
        //积分商城
        tv_integration_shop.setOnClickListener(v -> {
            IntegrationShopProxy integrationShopProxy = new IntegrationShopProxy(mContext, this);
            integrationShopProxy.showIntegrationShop();
        });
        //签到
        tv_sign_in.setOnClickListener(v -> getOperation().startActivity(SignInActivity.class));
    }

    @Override
    public void onQuerySuccess(User user) {
        updateUser(user);
    }

    @Override
    public void onQueryFailure() {

    }

    private void updateUser(User user) {
        // 更改
        String nickname = user.getNickname();
        if (TextUtils.isEmpty(nickname)) {
            tv_nickname.setOnClickListener(view -> getOperation().startActivity(UpdateInfoActivity.class));
        } else {
            tv_nickname.setText(nickname);
        }
        Integer credit = user.getCredit();
        tv_credit.setText(String.format(getResources().getString(R.string.tx_credit_vip_up), credit, credit));
        refreshAvatar(user.getAvatar());
        riv_avatar.setOnClickListener(v -> getOperation().startActivity(AccountActivity.class));
    }

    /**
     * 更新头像 refreshAvatar
     */
    private void refreshAvatar(String avatar) {
        if (avatar != null && !avatar.equals("")) {
            Uri uri = Uri.parse(avatar);
            riv_avatar.setImageURI(uri);
        } else {
            riv_avatar.setImageResource(R.mipmap.avatar_default);
        }
    }
}
