package com.xpple.sheep.ui.mainFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseFragment;
import com.xpple.sheep.proxy.IntegrationShopProxy;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.ui.AboutActivity;
import com.xpple.sheep.ui.AccountActivity;
import com.xpple.sheep.ui.FavoriteActivity;
import com.xpple.sheep.ui.InvitationActivity;
import com.xpple.sheep.ui.LoginActivity;
import com.xpple.sheep.ui.MessageActivity;
import com.xpple.sheep.ui.OverageActivity;
import com.xpple.sheep.ui.OverageHistoryActivity;
import com.xpple.sheep.ui.ItemPassActivity;
import com.xpple.sheep.ui.RechargeActivity;
import com.xpple.sheep.ui.VipActivity;
import com.xpple.sheep.ui.WithdrawalsActivity;
import com.xpple.sheep.view.DampView;
import com.xpple.sheep.view.RiseNumberTextView;
import com.xpple.sheep.view.dialog.AlertDialog;


public class MeFragment extends BaseFragment implements DampView.IRefreshListener {
    private int[] selfcenter_bg = {R.mipmap.selfcenter_bg_0, R.mipmap.selfcenter_bg_1, R.mipmap.selfcenter_bg_2, R.mipmap.selfcenter_bg_3, R.mipmap.selfcenter_bg_4};
    private View parentView;
    private RiseNumberTextView tv_todayProfits_value, tv_totalProfits_value, tv_overage_value;
    private ImageView iv_damp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_main_me, container, false);
        setUpViews();
        return parentView;
    }

    private void setUpViews() {
        //配合状态栏下移
        setStatusBarHeight(parentView);
        DampView dampView = (DampView) parentView.findViewById(R.id.dampView);
        iv_damp = (ImageView) parentView.findViewById(R.id.iv_damp);
        TextView tv_btn_logout = (TextView) parentView.findViewById(R.id.tv_btn_logout);
        tv_todayProfits_value = (RiseNumberTextView) parentView.findViewById(R.id.tv_todayProfits_value);
        tv_totalProfits_value = (RiseNumberTextView) parentView.findViewById(R.id.tv_totalProfits_value);
        tv_overage_value = (RiseNumberTextView) parentView.findViewById(R.id.tv_overage_value);
        RelativeLayout rl_red_package = (RelativeLayout) parentView.findViewById(R.id.rl_red_package);
        rl_red_package.setOnClickListener(view -> getOperation().startActivity(AboutActivity.class));
        RelativeLayout rl_account = (RelativeLayout) parentView.findViewById(R.id.rl_account);
        rl_account.setOnClickListener(view -> getOperation().startActivity(AccountActivity.class));
        RelativeLayout rl_integral = (RelativeLayout) parentView.findViewById(R.id.rl_integral);
        rl_integral.setOnClickListener(view -> {
            IntegrationShopProxy integrationShopProxy = new IntegrationShopProxy(getActivity(), getActivity());
            integrationShopProxy.showIntegrationShop();
        });
        RelativeLayout rl_invitation = (RelativeLayout) parentView.findViewById(R.id.rl_invitation);
        rl_invitation.setOnClickListener(view -> getOperation().startActivity(InvitationActivity.class));
        RelativeLayout rl_project = (RelativeLayout) parentView.findViewById(R.id.rl_project);
        rl_project.setOnClickListener(view -> getOperation().startActivity(ItemPassActivity.class));
        RelativeLayout rl_attention = (RelativeLayout) parentView.findViewById(R.id.rl_attention);
        rl_attention.setOnClickListener(view -> getOperation().startActivity(FavoriteActivity.class));
        RelativeLayout rl_bill = (RelativeLayout) parentView.findViewById(R.id.rl_bill);
        rl_bill.setOnClickListener(view -> getOperation().startActivity(OverageHistoryActivity.class));
        RelativeLayout rl_message = (RelativeLayout) parentView.findViewById(R.id.rl_message);
        rl_message.setOnClickListener(view -> getOperation().startActivity(MessageActivity.class));
        RelativeLayout rl_overage = (RelativeLayout) parentView.findViewById(R.id.rl_overage);
        rl_overage.setOnClickListener(view -> getOperation().startActivity(OverageActivity.class));
        RelativeLayout rl_totalProfits = (RelativeLayout) parentView.findViewById(R.id.rl_totalProfits);
        dampView.setImageView(iv_damp);
        dampView.setOnRefreshListener(this);
        initRiseNumber();
        UserProxy userProxy = new UserProxy(getActivity());
        tv_btn_logout.setOnClickListener(v -> new AlertDialog(getActivity()).builder()
                .setMsg("您确定退出登录么？")
                .setPositiveButton("确定", v1 -> {
                    userProxy.logout();
                    getOperation().startActivity(LoginActivity.class);
                }).setNegativeButton("取消", v1 -> {
                }).show());
        TextView tv_recharge = (TextView) parentView.findViewById(R.id.tv_recharge);
        tv_recharge.setOnClickListener(view -> getOperation().startActivity(AboutActivity.class));
        TextView tv_withdrawals = (TextView) parentView.findViewById(R.id.tv_withdrawals);
        //充值操作页
        tv_recharge.setOnClickListener(v -> getOperation().startActivity(RechargeActivity.class));
        //提现操作页
        tv_withdrawals.setOnClickListener(v -> getOperation().startActivity(WithdrawalsActivity.class));
        ImageView iv_level = (ImageView) parentView.findViewById(R.id.iv_level);
        iv_level.setOnClickListener(v -> getOperation().startActivity(VipActivity.class));
    }

    private void initRiseNumber() {
        // 设置数据
        tv_todayProfits_value.withNumber(92666.50f);
        // 设置数据
        tv_totalProfits_value.withNumber(10000.50f);
        // 设置数据
        tv_overage_value.withNumber(20000.50f);
        tv_todayProfits_value.start();
        tv_totalProfits_value.start();
        tv_overage_value.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        //换皮肤
        iv_damp.setBackgroundResource(selfcenter_bg[CustomApplication.getInstance().getSpUserUtil().getCenterBg()]);
    }

    @Override
    public void onRefresh() {
        tv_todayProfits_value.start();
        tv_totalProfits_value.start();
        tv_overage_value.start();
    }
}
