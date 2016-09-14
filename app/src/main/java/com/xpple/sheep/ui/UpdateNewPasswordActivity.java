package com.xpple.sheep.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseOnlineActivity;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.util.StringUtils;

/**
 * 修改登陆密码页
 *
 * @author nEdAy
 */
public class UpdateNewPasswordActivity extends BaseOnlineActivity implements UserProxy.IPasswordListener {
    private SVProgressHUD svProgressHUD;
    private EditText et_old_password, et_new_password, et_verify_password;
    private UserProxy userProxy;

    @Override
    public int bindLayout() {
        return R.layout.activity_update_new_password;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForBoth("修改登录密碼", "返回", "忘记", () -> {
            getOperation().startActivity(LostPasswordActivity.class);
        });
        TextView tv_already = (TextView) findViewById(R.id.tv_already);
        TextView tv_update = (TextView) findViewById(R.id.tv_submit);
        et_old_password = (EditText) findViewById(R.id.et_old_password);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_verify_password = (EditText) findViewById(R.id.et_verify_password);
        userProxy = getUserProxy();
        User user = userProxy.getCurrentUser();
        if (user == null) {
            getOperation().startActivity(LoginActivity.class);
        } else {
            tv_already.setText(StringUtils.hidePhoneNumber(user.getUsername()));
        }
        tv_update.setOnClickListener(v -> {
            String old_password = et_old_password.getText().toString();
            String new_password = et_new_password.getText().toString();
            String verify_password = et_verify_password.getText().toString();
            if (TextUtils.isEmpty(old_password)) {
                et_old_password.requestFocus();
                CommonUtils.setShakeAnimation(et_old_password);
                CommonUtils.showToast(R.string.toast_error_password_null);
                return;
            }
            if (!StringUtils.isValidPassword(old_password)) {
                et_old_password.requestFocus();
                CommonUtils.setShakeAnimation(et_old_password);
                CommonUtils.showToast(R.string.toast_error_password_error);
                return;
            }
            if (TextUtils.isEmpty(new_password)) {
                et_new_password.requestFocus();
                CommonUtils.setShakeAnimation(et_new_password);
                CommonUtils.showToast(R.string.toast_error_password_null);
                return;
            }
            if (!StringUtils.isValidPassword(new_password)) {
                et_new_password.requestFocus();
                CommonUtils.setShakeAnimation(et_new_password);
                CommonUtils.showToast(R.string.toast_error_password_error);
                return;
            }
            if (TextUtils.isEmpty(verify_password)) {
                et_verify_password.requestFocus();
                CommonUtils.setShakeAnimation(et_verify_password);
                CommonUtils.showToast(R.string.toast_error_password_null);
                return;
            }
            if (!verify_password.equals(new_password)) {
                et_verify_password.requestFocus();
                CommonUtils.setShakeAnimation(et_verify_password);
                CommonUtils.showToast(R.string.toast_error_password_verify_error);
                return;
            }
            updateInfo(old_password, new_password);
        });
    }


    /**
     * 修改密码
     */
    private void updateInfo(String old_password, String new_password) {
        svProgressHUD = new SVProgressHUD(this);
        svProgressHUD.showWithStatus("修改密码...");
        User user = new User();
        user.setNickname(old_password);
        userProxy.setOnPasswordListener(this);
        userProxy.updateUserPassword(old_password, new_password);
    }

    @Override
    public void onPasswordSuccess(String value) {
        svProgressHUD.showSuccessWithStatus("修改成功");
        finish();
    }

    @Override
    public void onPasswordFailure(String msg) {
        svProgressHUD.showErrorWithStatus("修改失败");
    }
}
