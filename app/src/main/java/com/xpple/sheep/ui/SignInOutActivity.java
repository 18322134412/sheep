package com.xpple.sheep.ui;

import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.xpple.sheep.R;
import com.xpple.sheep.base.BaseActivity;

public class SignInOutActivity extends BaseActivity {
    private RelativeLayout rl_payInfo;

    @Override
    public int bindLayout() {
        return R.layout.activity_sign_in_out;
    }

    @Override
    public void initView(View view) {
        rl_payInfo = (RelativeLayout) findViewById(R.id.rl_payInfo);
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer result) {
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_down_in);
                anim.setDuration(1000);
                anim.setFillAfter(true);
                rl_payInfo.startAnimation(anim);
            }
        }.execute();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        finish();
        return true;
    }
}
