package com.xpple.sheep.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.R;
import com.xpple.sheep.StaticConfig;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.util.StringUtils;
import com.xpple.sheep.view.ClearEditText;
import com.xpple.sheep.view.dialog.AlertDialog;
import com.xpple.sheep.view.sKB.PasswordEditText;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 重置支付密码页
 *
 * @author nEdAy
 */
public class LostTransactionPasswordActivity extends BaseActivity implements UserProxy.IPasswordListener {
    private ClearEditText et_phone;
    private EditText et_password, et_sms;
    private UserProxy userProxy;
    private TextView tv_sms, tv_submit;
    private boolean password_input, sms_input, isVoice;
    private TimeCount timeCount;
    private static final String country = "86";
    //服务号码
    private static final String SERVICE_CHECK_NUM = "1069003200004";

    @Override
    public int bindLayout() {
        return R.layout.activity_lost_transaction_password;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("重置支付密码", "返回");
        et_phone = (ClearEditText) findViewById(R.id.et_phone);
        et_password = (PasswordEditText) findViewById(R.id.et_password);
        et_sms = (EditText) findViewById(R.id.et_sms);
        tv_sms = (TextView) findViewById(R.id.tv_sms);
        //确认注册
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        initSms();
        userProxy = getUserProxy();
        User user = userProxy.getCurrentUser();
        if (user == null) {
            getOperation().startActivity(LoginActivity.class);
        } else {
            et_phone.setText(StringUtils.hidePhoneNumber(user.getUsername()));
        }
        et_password.addTextChangedListener(mPasswordWatcher);
        et_sms.addTextChangedListener(mSmsWatcher);
        tv_sms.setOnClickListener(vv -> {
            String phone = et_phone.getText().toString().trim().replace(" ", "");
            //先短信验证码，闲置60s后切换语音验证码
            if (isVoice) {
                new AlertDialog(mContext).builder().setTitle("确认发送？").setMsg("确定后将致电您的手机号并语音播报验证码，如不希望被来点打扰请返回。")
                        .setPositiveButton("确定", v ->
                                SMSSDK.getVoiceVerifyCode(country, phone)
                        ).setNegativeButton("返回", v -> {
                        }

                ).show();
            } else {
                new AlertDialog(mContext).builder().setTitle("确认手机号码").setMsg("我们将发送验证码到该手机号：+" + country + phone)
                        .setPositiveButton("确定", v -> {
                            timeCount = new TimeCount(60000, 1000);
                            timeCount.start();
                            IntentFilter filter = new IntentFilter();
                            filter.addAction("android.provider.Telephony.SMS_RECEIVED");
                            filter.setPriority(Integer.MAX_VALUE);
                            registerReceiver(smsReceiver, filter);
                            SMSSDK.getVerificationCode(country, phone);
                        })
                        .setNegativeButton("返回", v -> {
                                }
                        ).show();
            }
        });
        tv_submit.setOnClickListener(v -> {
            if (!CommonUtils.isNetworkAvailable(mContext)) {
                CommonUtils.showToast(R.string.network_tips);
                return;
            }
            register();
        });
    }

    private void initSms() {
        // 初始化 MobSMS SDK
        SMSSDK.initSDK(CustomApplication.getInstance(), StaticConfig.MOB_APP_KEY, StaticConfig.MOB_APP_SECRET);
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }


    private void register() {
        String phone = et_phone.getText().toString().trim().replace(" ", "");
        String password = et_password.getText().toString().trim();
        String sms = et_sms.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            et_phone.requestFocus();
            CommonUtils.setShakeAnimation(et_phone);
            CommonUtils.showToast(R.string.toast_error_phone_null);
            return;
        }
        if (!StringUtils.isValidPassword(password)) {
            et_password.requestFocus();
            CommonUtils.setShakeAnimation(et_password);
            CommonUtils.showToast(R.string.toast_error_password_error);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            et_password.requestFocus();
            CommonUtils.setShakeAnimation(et_password);
            CommonUtils.showToast(R.string.toast_error_password_null);
            return;
        }
        if (TextUtils.isEmpty(sms)) {
            et_sms.requestFocus();
            CommonUtils.setShakeAnimation(et_sms);
            CommonUtils.showToast(R.string.toast_error_sms_null);
            return;
        }
        userProxy.setOnPasswordListener(this);

        userProxy.resetTransactionPassword(phone, password, sms);
    }

    @Override
    public void onPasswordSuccess(String value) {
        CommonUtils.showToast("修改成功");
        finish();
    }

    @Override
    public void onPasswordFailure(String msg) {
        CommonUtils.showToast("修改失败");
    }

    //短信验证码内容 验证码是6位数字的格式
    private String strContent;

    //更新界面
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (et_sms != null) {
                et_sms.setText(strContent);
            }
        }

    };
    //监听短信广播
    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objects != null ? objects : new Object[0]) {
                byte[] pdu = (byte[]) obj;
                SmsMessage sms = SmsMessage.createFromPdu(pdu);
                // 短信的内容
                String message = sms.getMessageBody();
                String from = sms.getOriginatingAddress();
                if (SERVICE_CHECK_NUM.equals(from.trim()) || TextUtils.isEmpty(SERVICE_CHECK_NUM)) {
                    Time time = new Time();
                    time.set(sms.getTimestampMillis());
                    strContent = from + "   " + message;
                    if (!TextUtils.isEmpty(from)) {
                        String code = patternCode(message);
                        if (!TextUtils.isEmpty(code)) {
                            strContent = code;
                            mHandler.sendEmptyMessage(1);
                        }
                    }
                } else {
                    return;
                }
            }

        }
    };

    /**
     * 匹配短信中间的6个数字（验证码等）
     */
    private String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        String patternCoder = "(?<!\\d)\\d{4}(?!\\d)";
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    EventHandler eh = new EventHandler() {

        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //走短信验证
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_sms.setClickable(false);
                        }
                    });
                } else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
                    //请求发送语音验证码，无返回
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_sms.setText(" 致电中...请稍等 ");
                            tv_sms.setEnabled(false);
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeCount.cancel();
                        tv_sms.setEnabled(true);
                        tv_sms.setText(" 异常，请重试 ");
                    }
                });
                try {
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");//错误描述
                    int status = object.optInt("status");//错误代码
                    if (status > 0 && !TextUtils.isEmpty(des)) {
                        CommonUtils.showToast(des);
                    }
                } catch (Exception ignored) {
                }
            }
        }
    };

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_sms.setText(String.format(getString(R.string.countdown_number), millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            tv_sms.setClickable(true);
            isVoice = true;
            tv_sms.setText(" 发送语音验证 ");
        }
    }


    TextWatcher mPasswordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            password_input = s.length() >= 6;
            tv_submit.setEnabled(password_input && sms_input);
        }
    };
    TextWatcher mSmsWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            sms_input = s.length() > 0;
            tv_submit.setEnabled(password_input && sms_input);
        }
    };

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        new AlertDialog(mContext).builder().setTitle("是否返回").setMsg("验证码短信可能略有延迟，确定返回并重新开始？")
                .setPositiveButton("等待", view -> {

                }).setNegativeButton("返回", view -> finish()

        ).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCount != null) {
            timeCount.cancel();
        }
        try {
            unregisterReceiver(smsReceiver);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    public void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(eh);
    }

}
