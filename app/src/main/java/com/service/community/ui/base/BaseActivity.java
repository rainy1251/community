package com.service.community.ui.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.service.community.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 记录所有活动的Activity 由于经常要增删所以LinkedList效率比较高
     */
    private static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();
    private static BaseActivity mForegroundActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }
        synchronized (mActivities) {
            mActivities.add(this);
        }

        int layoutId = getLayoutId();
        setContentView(layoutId);
        ButterKnife.bind(this);


        initView();
        initData();
        initActionBar();
        initListener();
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
    }

    /**
     * 添加布局文件
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化ActionBar
     */
    public void initActionBar() {
    }

    ;

    /**
     * 初始化界面
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化事件
     */
    protected abstract void initListener();

    @Override
    protected void onResume() {
        super.onResume();
        mForegroundActivity = this;

    }

    @Override
    protected void onPause() {
        super.onPause();
        mForegroundActivity = null;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3

        return super.dispatchTouchEvent(event);
    }

    /**
     * 获取当前处于前台的activity
     */
    public static BaseActivity getForegroundActivity() {
        return mForegroundActivity;
    }

    @Override
    public void finish() {
        synchronized (mActivities) {
            mActivities.remove(this);
        }
        super.finish();

    }

    /**
     * 退出程序
     */
    public void exitApp() {
        // 结束所有的Activity
        killAll();
        // 杀死进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // 杀死所有Activity
    private void killAll() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
    }

    public void setToolbarNoEN(int toolbarId, String ch) {
        Toolbar mToolbar = (Toolbar) findViewById(toolbarId);
        ImageView mBack = (ImageView) mToolbar.findViewById(R.id.back);
        TextView tvLeft = (TextView) mToolbar.findViewById(R.id.tv_left);
        TextView tv_ch = (TextView) mToolbar.findViewById(R.id.tv_ch);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(false); //显示小箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_ch.setText(ch);
    }

    @Override
    // 点击空白区域 自动隐藏软键盘
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }



}
