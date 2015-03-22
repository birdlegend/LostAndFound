package com.zsb.lostandfound;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.Toast;

import com.zsb.lostandfound.config.Constants;

import cn.bmob.v3.Bmob;

/**
 * Created by zsb on 2015/3/22.
 */
public abstract class BaseActivity extends Activity {
    protected int mScreenWidth;
    protected int mScreenHeight;

    public static final String  TAG = "bmob";
    /**
     * Called when the activity is starting.  This is where most initialization
     * should go: calling {@link #setContentView(int)} to inflate the
     * activity's UI, using {@link #findViewById} to programmatically interact
     * with widgets in the UI, calling
     * {@link #managedQuery(android.net.Uri, String[], String, String[], String)} to retrieve
     * cursors for data being displayed, etc.
     * <p/>
     * <p>You can call {@link #finish} from within this function, in
     * which case onDestroy() will be immediately called without any of the rest
     * of the activity lifecycle ({@link #onStart}, {@link #onResume},
     * {@link #onPause}, etc) executing.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     * @see #onStart
     * @see #onSaveInstanceState
     * @see #onRestoreInstanceState
     * @see #onPostCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化 Bmob SDK
        // 使用时请将第二个参数Application ID
        // 替换成你在Bmob服务器端创建的Application ID
        Bmob.initialize(this, Constants.BMOB_APPID);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //获取当前屏幕宽高
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        setContentView();
        initViews();
        initListeners();
        initData();
    }

    /**
     * 设置布局文件
     */
    public abstract void setContentView();

    /**
     * 初始化布局文件中控件
     */
    public abstract void initViews();

    /**
     * 初始化控件监听
     */
    public abstract void initListeners();

    /**
     * 进行数据初始化
     */
    public abstract void initData();

    Toast mToast;

    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null)
                mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
            else
                mToast.setText(text);
            mToast.show();
        }
    }

    /**
     * @return 状态栏底部高度
     */
    public int getStateBar() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top; //return statebar height
    }

    /**
     * 手机分辨率从dip转为px
     *
     * @param context
     * @param dpValue dip
     * @return 像素值
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;  //屏幕密度
        return (int) (scale * dpValue + 0.5f);
    }
}
