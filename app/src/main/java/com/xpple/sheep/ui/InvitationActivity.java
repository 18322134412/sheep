package com.xpple.sheep.ui;

import android.view.View;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseOnlineActivity;
import com.xpple.sheep.view.ShareDialog;

/**
 * 我的邀请页-邀请二维码信息
 *
 * @author nEdAy
 */
public class InvitationActivity extends BaseOnlineActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_invitation;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForBoth("我的邀请", "返回", "规则", () -> {
            getOperation().startWebActivity("http://neday.kuaizhan.com/71/75/p333127659e894d", "规则");
        });
        TextView tv_invitation_number = (TextView) findViewById(R.id.tv_invitation_number);
        TextView tv_invitation_money = (TextView) findViewById(R.id.tv_invitation_money);
        TextView tv_invitation_vip = (TextView) findViewById(R.id.tv_invitation_vip);
        TextView tv_btn_invitation = (TextView) findViewById(R.id.tv_btn_invitation);
        tv_invitation_number.setText(String.format(getResources().getString(R.string.invitation_number), 5));
        tv_invitation_number.setOnClickListener(v -> getOperation().startActivity(InvitationFriendActivity.class));
        tv_invitation_money.setText(String.format(getResources().getString(R.string.invitation_money), 500));
        tv_invitation_vip.setText(String.format(getResources().getString(R.string.invitation_vip), "高級代言人（vip5~vip10）", 100));
        tv_btn_invitation.setOnClickListener(v ->
                new ShareDialog(mContext).builder("標題", "主體", "http://www.neday.cn/iis-85.png", "http://www.neday.cn").show());

    }


}
