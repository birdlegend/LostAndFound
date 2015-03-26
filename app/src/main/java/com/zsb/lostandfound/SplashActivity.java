package com.zsb.lostandfound;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.zsb.lostandfound.bean.Found;


/**
 * 闪屏页
 * Created by zsb on 2015/3/22.
 */
public class SplashActivity extends BaseActivity {
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
        mHandler.sendEmptyMessageDelayed(GO_HOME, 2000);
    }

    public void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private static final int GO_HOME = 100;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
            }
        }
    };

}
