package com.xpple.sheep.ui;

import android.view.View;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseOnlineActivity;

/**
 * 我的邀请页-邀请朋友列表
 *
 * @author nEdAy
 */
public class InvitationFriendActivity extends BaseOnlineActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_invitation_friend;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("我的邀请", "返回");
        TextView tv_invitation = (TextView) findViewById(R.id.tv_invitation);
        tv_invitation.setText(String.format(getResources().getString(R.string.invitation), 6, 100));
    }


}
