package com.xpple.sheep.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.xpple.sheep.R;
import com.xpple.sheep.StaticConfig;
import com.xpple.sheep.base.BaseOnlineActivity;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.view.dialog.AlertDialog;

/**
 * 设置昵称
 *
 * @author nEdAy
 */
public class UpdateInfoActivity extends BaseOnlineActivity implements UserProxy.IUpdateListener {
    private TextView tv_update;
    private EditText et_nickname;
    private SVProgressHUD svProgressHUD;

    @Override
    public int bindLayout() {
        return R.layout.activity_update_info;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("设置昵称", "返回");
        tv_update = (TextView) findViewById(R.id.tv_submit);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        et_nickname.addTextChangedListener(mNicknameWatcher);
        tv_update.setOnClickListener(v -> {
            String nickname = et_nickname.getText().toString();
            if (nickname.isEmpty() && nickname.equals("")) {
                CommonUtils.showToast("请填写昵称!");
                return;
            }
            new AlertDialog(mContext).builder().setTitle("设置昵称").setMsg("袋王亲，昵称仅可设置一次且不可修改，是否确定设置？")
                    .setPositiveButton("确定", v1 ->
                            updateInfo(nickname)).setNegativeButton("取消", v1 -> {

            }).show();
        });
    }


    /**
     * 修改资料 updateInfo
     */
    private void updateInfo(String nickname) {
        svProgressHUD = new SVProgressHUD(mContext);
        svProgressHUD.showWithStatus("设置昵称...");
        User user = new User();
        user.setNickname(nickname);
        UserProxy userProxy = getUserProxy();
        userProxy.setOnUpdateListener(this);
        userProxy.update(user);
    }

    @Override
    public void onUpdateSuccess(User user) {
        svProgressHUD.showSuccessWithStatus("更新成功");
        // 发广播通知登陆页面退出
        Intent intent = new Intent(StaticConfig.ACTION_UPDATE_NICKNAME_SUCCESS_FINISH);
        intent.putExtra("nickname", user.getNickname());
        sendBroadcast(intent);
        finish();
    }

    @Override
    public void onUpdateFailure(String msg) {
        svProgressHUD.showErrorWithStatus("更新失败");
    }

    TextWatcher mNicknameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length();
            tv_update.setEnabled(length > 0);
        }
    };
}
