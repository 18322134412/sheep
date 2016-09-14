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
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpple.sheep.R;
import com.xpple.sheep.StaticConfig;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.util.CommonUtils;
import com.xpple.sheep.util.StringUtils;
import com.xpple.sheep.view.ClearEditText;
import com.xpple.sheep.view.dialog.AlertDialog;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * 注册页
 *
 * @author nEdAy
 */
public class RegisterActivity extends BaseActivity implements UserProxy.ISignUpListener {
    private ClearEditText et_phone;
    private EditText et_password, et_sms;
    private UserProxy userProxy;
    private ImageView iv_password_see, iv_agreement;
    private TextView tv_sms, tv_submit;
    private boolean phone_input, password_input, sms_input, isVoice;
    private TimeCount timeCount;
    private boolean password_saw = false, agreement = true;
    private static final String country = "86";
    //服务号码
    private static final String SERVICE_CHECK_NUM = "1069003200004";

    @Override
    public int bindLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("注册", "返回");
        et_phone = (ClearEditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        et_sms = (EditText) findViewById(R.id.et_sms);
        tv_sms = (TextView) findViewById(R.id.tv_sms);
        //显示密码
        iv_password_see = (ImageView) findViewById(R.id.iv_password_see);
        //用户使用协议
        iv_agreement = (ImageView) findViewById(R.id.iv_agreement);
        TextView tv_agreement = (TextView) findViewById(R.id.tv_agreement);
        RelativeLayout rl_agreement = (RelativeLayout) findViewById(R.id.rl_greement);
        //确认注册
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        initSms();
        userProxy = getUserProxy();
        et_phone.addTextChangedListener(mPhoneWatcher);
        et_password.addTextChangedListener(mPasswordWatcher);
        et_sms.addTextChangedListener(mSmsWatcher);
        tv_sms.setOnClickListener(vv -> {
            String phone = et_phone.getText().toString().trim().replace(" ", "");
            //先短信验证码，闲置30s后切换语音验证码
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
                            timeCount = new TimeCount(30000, 1000);
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
        iv_password_see.setOnClickListener(v -> {
            if (password_saw) {//false
                password_saw = false;
                iv_password_see.setImageResource(R.mipmap.ic_see_normal);
                et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            } else {
                password_saw = true;
                iv_password_see.setImageResource(R.mipmap.ic_see_pressed);
                et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
            et_password.setSelection(et_password.getText().length());
        });
        rl_agreement.setOnClickListener(v -> {
            if (agreement) {//true
                agreement = false;
                iv_agreement.setImageResource(R.mipmap.check_normal);
            } else {
                agreement = true;
                iv_agreement.setImageResource(R.mipmap.check_pressed);
            }
        });
        tv_submit.setOnClickListener(v -> {
            if (!agreement) {
                CommonUtils.showToast("您尚未同意《用户使用协议》");
                return;
            }
            if (!CommonUtils.isNetworkAvailable(mContext)) {
                CommonUtils.showToast(R.string.network_tips);
                return;
            }
            register();
        });
        tv_agreement.setOnClickListener(v -> getOperation().startWebActivity("http://neday.kuaizhan.com/81/26/p3336309366de35", "帮助"));
    }

    private void initSms() {
        // 初始化 MobSMS SDK
        SMSSDK.initSDK(getBaseContext(), StaticConfig.MOB_APP_KEY, StaticConfig.MOB_APP_SECRET);
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
        userProxy.setOnSignUpListener(this);
        userProxy.signUp(phone, password, sms);
    }

    @Override
    public void onSignUpSuccess(String phone, String password) {
        CommonUtils.showToast("注册成功");
        // 发广播通知登陆页面退出
        Intent intent = new Intent(StaticConfig.ACTION_REGISTER_SUCCESS_FINISH);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        sendBroadcast(intent);
        finish();
    }

    @Override
    public void onSignUpFailure(String error) {
        //TODO:額 那個等錯誤碼確定後 判斷錯誤碼 若錯誤為已註冊 則跳轉登錄頁並自動填寫帳號欄
        CommonUtils.showToast("注册失败:" + error);
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
                //回调完成
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

    TextWatcher mPhoneWatcher = new TextWatcher() {
        int beforeLen = 0;
        int afterLen = 0;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeLen = s.length();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            afterLen = s.length();
            if (afterLen > beforeLen) {
                if (afterLen == 4 || afterLen == 9) {
                    et_phone.setText(new StringBuffer(s).insert(
                            afterLen - 1, " ").toString());
                    et_phone.setSelection(afterLen);
                }
            } else {
                if (et_phone.getText().toString().startsWith(" ")) {
                    et_phone.setText(new StringBuffer(s).delete(
                            afterLen - 1, afterLen).toString());
                    et_phone.setSelection(afterLen);
                }
            }
            phone_input = s.length() == 13;
            tv_sms.setEnabled(phone_input);
            tv_submit.setEnabled(phone_input && password_input && sms_input);
        }
    };
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
            tv_submit.setEnabled(phone_input && password_input && sms_input);
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
            tv_submit.setEnabled(phone_input && password_input && sms_input);
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
