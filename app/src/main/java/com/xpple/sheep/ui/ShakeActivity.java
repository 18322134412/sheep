package com.xpple.sheep.ui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.xpple.sheep.CustomApplication;
import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseOnlineActivity;
import com.xpple.sheep.bean.User;
import com.xpple.sheep.proxy.UserProxy;
import com.xpple.sheep.util.SharedPreferencesSettingsUtil;
import com.xpple.sheep.view.dialog.AlertDialog;

/**
 * 天天摇页
 *
 * @author nEdAy
 */

public class ShakeActivity extends BaseOnlineActivity implements UserProxy.IQueryListener {
    private CustomApplication mApplication;
    private SensorManager sensorManager;
    private ImageView iv_shake, iv_cancel_voice;
    private TextView tv_shake_history, tv_shake_times;
    private RelativeLayout rl_shake_voice;
    private boolean isShakeVoice;
    private SVProgressHUD svProgressHUD;
    private UserProxy userProxy;
    private User user;
    private Integer shakeTimes;

    @Override
    public int bindLayout() {
        return R.layout.activity_shake;
    }

    @Override
    public void initView(View view) {
        setTintManager();
        initTopBarForBoth("口袋天天摇", "返回", "活动规则", () ->
                getOperation().startWebActivity("http://neday.kuaizhan.com/91/85/p333138498122d7", "活动规则")
        );
        iv_shake = (ImageView) findViewById(R.id.iv_shake);
        iv_cancel_voice = (ImageView) findViewById(R.id.iv_cancel_voice);
        tv_shake_history = (TextView) findViewById(R.id.tv_shake_history);
        tv_shake_times = (TextView) findViewById(R.id.tv_shake_times);
        rl_shake_voice = (RelativeLayout) findViewById(R.id.rl_shake_voice);
        mApplication = CustomApplication.getInstance();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        svProgressHUD = new SVProgressHUD(mContext);
        tv_shake_history.setOnClickListener(v -> getOperation().startActivity(ShakeHistoryActivity.class));
        iv_shake.setOnClickListener(v -> shake());
        SharedPreferencesSettingsUtil mSharedPreferencesSettingsUtil = mApplication.getSpSettingsUtil();
        isShakeVoice = mSharedPreferencesSettingsUtil.isAllowShakeVoice();
        iv_cancel_voice.setImageResource(isShakeVoice ? R.mipmap.voice_open : R.mipmap.voice_close);
        rl_shake_voice.setOnClickListener(v -> {
            if (isShakeVoice) {
                iv_cancel_voice.setImageResource(R.mipmap.voice_close);
            } else {
                iv_cancel_voice.setImageResource(R.mipmap.voice_open);
            }
            isShakeVoice = !isShakeVoice;
            mSharedPreferencesSettingsUtil.setAllowShakeVoiceEnable(isShakeVoice);
        });
        userProxy = getUserProxy();
        user = userProxy.getCurrentUser();
        if (user == null) {
            new AlertDialog(mContext).builder().setTitle("是否登录").setMsg("先登录才能摇大奖哦！")
                    .setPositiveButton("马上登录", v ->
                            getOperation().startActivity(LoginActivity.class)
                    ).setNegativeButton("先不着急", v -> {
                    }

            ).show();
        } else {
            userProxy.setOnQueryListener(this);
            userProxy.getUserShakeTimes(user.getObjectId());
        }
    }


    @Override
    public void onQuerySuccess(User user) {
        shakeTimes = user.getShake_times();
        tv_shake_times.setText(String.format(getResources().getString(R.string.shake_times), shakeTimes));
        if (shakeTimes <= 0) {
            svProgressHUD.showErrorWithStatus("当日摇一摇机会已经用完咯");
        }
    }

    @Override
    public void onQueryFailure() {
        svProgressHUD.showErrorWithStatus("获取用户积分失败");
    }

    private void shake() {
        if (shakeTimes.equals(0) && shakeTimes == null)
            userProxy.getUserShakeTimes(user.getObjectId());
        //摇一摇交互操作
        svProgressHUD.showInfoWithStatus("抽獎中...");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            try {
                //由于有的手机硬件并不支持加速度感应功能，主要有两种表现形式：
                // 1、SensorManager.getDefaultSensor(int type)当传入参数为Sensor.TYPE_ACCELEROMETER时返回为空；
                // 2、SensorEventListener监听器中的接收重力感应值的onSensorChanged()每次传入的值均为0；
                // 因此不能实现程序想要实现的加速度感应功能。
                // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
                sensorManager.registerListener(sensorEventListener,
                        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_NORMAL);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int value = 12;// 三星 i9250怎么晃都不会超过20

            if ((Math.abs(x) > value || Math.abs(y) > value || Math
                    .abs(z) > value)) {
                if (isShakeVoice) {
                    MediaPlayer mMediaPlayer = mApplication.getMediaPlayer();
                    mMediaPlayer.start();
                }
                shake();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
