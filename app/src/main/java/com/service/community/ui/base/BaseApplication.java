package com.service.community.ui.base;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.multidex.MultiDex;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.push.EMPushHelper;
import com.hyphenate.push.EMPushType;
import com.hyphenate.push.PushListener;
import com.hyphenate.util.EMLog;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import com.service.community.hxim.CallReceiver;
import com.service.community.hxim.DemoHelper;
import com.service.community.ui.activity.SessionListActivity;
import com.hyphenate.easeui.SPUtils;
import com.service.community.ui.view.MyLoadingView;

import java.util.ArrayList;
import java.util.List;


public class BaseApplication extends Application {

    private static BaseApplication instance;


//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//
//
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRefreshLayout();
        MultiDex.install(this);
        SPUtils.instance(this);

        getInitHXIM();
    }



    /**
     * 初始化下拉刷新控件
     */
    private void initRefreshLayout() {

        TwinklingRefreshLayout.setDefaultHeader(ProgressLayout.class.getName());
        TwinklingRefreshLayout.setDefaultFooter(MyLoadingView.class.getName());
    }


    public static BaseApplication getInstance() {
        return instance;
    }



    private void getInitHXIM() {
//        EMOptions options = new EMOptions();
//// 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
//        EaseUI.getInstance().init(this, options);
//        //EMClient.getInstance().setDebugMode(true);
//        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
//        registerReceiver(new CallReceiver(), callFilter);

        DemoHelper.getInstance().init(this);
        if (EaseUI.getInstance().isMainProcess(this)) {
            EMPushHelper.getInstance().setPushListener(new PushListener() {
                @Override
                public void onError(EMPushType pushType, long errorCode) {
                }
            });
        }

    }

}
