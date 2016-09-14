package com.xpple.sheep.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;
import com.xpple.sheep.util.SharedPreferencesSettingsUtil;

import org.json.JSONArray;

import java.util.Arrays;

/**
 * 推送设置页
 *
 * @author nEdAy
 */
public class PushSettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_notification, rl_voice, rl_vibrate, rl_quiet, rl_quiet_period, rl_time;
    private ImageView iv_open_notification, iv_close_notification, iv_open_voice,
            iv_close_voice, iv_open_vibrate, iv_close_vibrate, iv_open_quiet, iv_close_quiet;
    private SharedPreferencesSettingsUtil mSharedPreferencesSettingsUtil;
    private int mHour_0, mMinute_0, mHour_1, mMinute_1;
    private NumberPicker mHourSpinner_0, mMinuteSpinner_0, mHourSpinner_1, mMinuteSpinner_1;
    private TextView tv_quiet_period, tv_btn_submit;

    @Override
    public int bindLayout() {
        return R.layout.activity_setting_push;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForLeft("推送设置", "返回");
        rl_notification = (RelativeLayout) findViewById(R.id.rl_notification);
        rl_voice = (RelativeLayout) findViewById(R.id.rl_voice);
        rl_vibrate = (RelativeLayout) findViewById(R.id.rl_vibrate);
        rl_quiet = (RelativeLayout) findViewById(R.id.rl_quiet);
        rl_quiet_period = (RelativeLayout) findViewById(R.id.rl_quiet_period);
        rl_time = (RelativeLayout) findViewById(R.id.rl_time);
        iv_open_notification = (ImageView) findViewById(R.id.iv_open_notification);
        iv_close_notification = (ImageView) findViewById(R.id.iv_close_notification);
        iv_open_voice = (ImageView) findViewById(R.id.iv_open_voice);
        iv_close_voice = (ImageView) findViewById(R.id.iv_close_voice);
        iv_open_vibrate = (ImageView) findViewById(R.id.iv_open_vibrate);
        iv_close_vibrate = (ImageView) findViewById(R.id.iv_close_vibrate);
        iv_open_quiet = (ImageView) findViewById(R.id.iv_open_quiet);
        iv_close_quiet = (ImageView) findViewById(R.id.iv_close_quiet);
        mHourSpinner_0 = (NumberPicker) findViewById(R.id.mHourSpinner_0);
        mMinuteSpinner_0 = (NumberPicker) findViewById(R.id.mMinuteSpinner_0);
        mHourSpinner_1 = (NumberPicker) findViewById(R.id.mHourSpinner_1);
        mMinuteSpinner_1 = (NumberPicker) findViewById(R.id.mMinuteSpinner_1);
        tv_quiet_period = (TextView) findViewById(R.id.tv_quiet_period);
        tv_btn_submit = (TextView) findViewById(R.id.tv_btn_submit);
        rl_notification.setOnClickListener(this);
        rl_voice.setOnClickListener(this);
        rl_vibrate.setOnClickListener(this);
        rl_quiet.setOnClickListener(this);
        rl_quiet_period.setOnClickListener(this);
        mSharedPreferencesSettingsUtil =  CustomApplication.getInstance().getSpSettingsUtil();
        int[] resArray = new int[4];
        Arrays.fill(resArray, 0);
        try {
            JSONArray jsonArray = new JSONArray(mSharedPreferencesSettingsUtil.getQuitePeriod());
            for (int i = 0; i < jsonArray.length(); i++) {
                resArray[i] = jsonArray.getInt(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHour_0 = resArray[0];
        mMinute_0 = resArray[1];
        mHour_1 = resArray[2];
        mMinute_1 = resArray[3];
        tv_quiet_period.setText(String.format(getResources().getString(R.string.tx_quiet_period), getDateFormat(mHour_0), getDateFormat(mMinute_0), getDateFormat(mHour_1), getDateFormat(mMinute_1)));

        mHourSpinner_0.setMaxValue(23);
        mHourSpinner_0.setMinValue(0);
        mHourSpinner_0.setValue(mHour_0);
        mHourSpinner_0.setOnValueChangedListener(mOnHourChangedListener);
        mMinuteSpinner_0.setMaxValue(59);
        mMinuteSpinner_0.setMinValue(0);
        mMinuteSpinner_0.setValue(mMinute_0);
        mMinuteSpinner_0.setOnValueChangedListener(mOnHourChangedListener);
        mHourSpinner_1.setMaxValue(23);
        mHourSpinner_1.setMinValue(0);
        mHourSpinner_1.setValue(mHour_1);
        mHourSpinner_1.setOnValueChangedListener(mOnHourChangedListener);
        mMinuteSpinner_1.setMaxValue(59);
        mMinuteSpinner_1.setMinValue(0);
        mMinuteSpinner_1.setValue(mMinute_1);
        mMinuteSpinner_1.setOnValueChangedListener(mOnHourChangedListener);
        tv_btn_submit.setOnClickListener(this);
        // 初始化
        if (mSharedPreferencesSettingsUtil.isAllowPushNotify()) {
            iv_open_notification.setVisibility(View.VISIBLE);
            iv_close_notification.setVisibility(View.INVISIBLE);
        } else {
            iv_open_notification.setVisibility(View.INVISIBLE);
            iv_close_notification.setVisibility(View.VISIBLE);
        }
        if (mSharedPreferencesSettingsUtil.isAllowVoice()) {
            iv_open_voice.setVisibility(View.VISIBLE);
            iv_close_voice.setVisibility(View.INVISIBLE);
        } else {
            iv_open_voice.setVisibility(View.INVISIBLE);
            iv_close_voice.setVisibility(View.VISIBLE);
        }
        if (mSharedPreferencesSettingsUtil.isAllowVibrate()) {
            iv_open_vibrate.setVisibility(View.VISIBLE);
            iv_close_vibrate.setVisibility(View.INVISIBLE);
        } else {
            iv_open_vibrate.setVisibility(View.INVISIBLE);
            iv_close_vibrate.setVisibility(View.VISIBLE);
        }
        if (mSharedPreferencesSettingsUtil.isAllowQuiet()) {
            iv_open_quiet.setVisibility(View.VISIBLE);
            iv_close_quiet.setVisibility(View.INVISIBLE);
        } else {
            iv_open_quiet.setVisibility(View.INVISIBLE);
            iv_close_quiet.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_notification:
                if (iv_open_notification.getVisibility() == View.VISIBLE) {
                    iv_open_notification.setVisibility(View.INVISIBLE);
                    iv_close_notification.setVisibility(View.VISIBLE);
                    mSharedPreferencesSettingsUtil.setPushNotifyEnable(false);
                    rl_vibrate.setVisibility(View.GONE);
                    rl_voice.setVisibility(View.GONE);
                } else {
                    iv_open_notification.setVisibility(View.VISIBLE);
                    iv_close_notification.setVisibility(View.INVISIBLE);
                    mSharedPreferencesSettingsUtil.setPushNotifyEnable(true);
                    rl_vibrate.setVisibility(View.VISIBLE);
                    rl_voice.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rl_voice:
                if (iv_open_voice.getVisibility() == View.VISIBLE) {
                    iv_open_voice.setVisibility(View.INVISIBLE);
                    iv_close_voice.setVisibility(View.VISIBLE);
                    mSharedPreferencesSettingsUtil.setAllowVoiceEnable(false);
                } else {
                    iv_open_voice.setVisibility(View.VISIBLE);
                    iv_close_voice.setVisibility(View.INVISIBLE);
                    mSharedPreferencesSettingsUtil.setAllowVoiceEnable(true);
                }
                break;
            case R.id.rl_vibrate:
                if (iv_open_vibrate.getVisibility() == View.VISIBLE) {
                    iv_open_vibrate.setVisibility(View.INVISIBLE);
                    iv_close_vibrate.setVisibility(View.VISIBLE);
                    mSharedPreferencesSettingsUtil.setAllowVibrateEnable(false);
                } else {
                    iv_open_vibrate.setVisibility(View.VISIBLE);
                    iv_close_vibrate.setVisibility(View.INVISIBLE);
                    mSharedPreferencesSettingsUtil.setAllowVibrateEnable(true);
                }
                break;
            case R.id.rl_quiet:
                if (iv_open_quiet.getVisibility() == View.VISIBLE) {
                    iv_open_quiet.setVisibility(View.INVISIBLE);
                    iv_close_quiet.setVisibility(View.VISIBLE);
                    rl_quiet_period.setVisibility(View.INVISIBLE);
                    rl_time.setVisibility(View.INVISIBLE);
                    mSharedPreferencesSettingsUtil.setAllowQuietEnable(false);
                } else {
                    iv_open_quiet.setVisibility(View.VISIBLE);
                    iv_close_quiet.setVisibility(View.INVISIBLE);
                    rl_quiet_period.setVisibility(View.VISIBLE);
                    mSharedPreferencesSettingsUtil.setAllowQuietEnable(true);
                }
                break;
            case R.id.rl_quiet_period:
                rl_time.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_btn_submit:
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(mHour_0);
                jsonArray.put(mMinute_0);
                jsonArray.put(mHour_1);
                jsonArray.put(mMinute_1);
                mSharedPreferencesSettingsUtil.setQuitePeriod(jsonArray.toString());
                rl_time.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private NumberPicker.OnValueChangeListener mOnHourChangedListener = (picker, oldVal, newVal) -> {
        mHour_0 = mHourSpinner_0.getValue();
        mMinute_0 = mMinuteSpinner_0.getValue();
        mHour_1 = mHourSpinner_1.getValue();
        mMinute_1 = mMinuteSpinner_1.getValue();
        tv_quiet_period.setText(String.format(getResources().getString(R.string.tx_quiet_period), getDateFormat(mHour_0), getDateFormat(mMinute_0), getDateFormat(mHour_1), getDateFormat(mMinute_1)));
    };

    public String getDateFormat(int time) {
        String date = time + "";
        if (time < 10) {
            date = "0" + date;
        }
        return date;
    }
}
