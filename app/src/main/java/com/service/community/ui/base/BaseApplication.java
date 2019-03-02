package com.service.community.ui.base;

import android.app.Application;
import android.graphics.Color;
import android.support.multidex.MultiDex;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.netease.nim.uikit.SPUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.service.community.avchat.DemoCache;
import com.service.community.avchat.IMConfig;
import com.service.community.ui.activity.SessionListActivity;
import com.service.community.ui.view.MyLoadingView;


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
        getInitIM();

    }

    private void getInitIM() {
//        NIMClient.init(this, loginInfo(), options());
//        if (NIMUtil.isMainProcess(this)) {
//            NimUIKit.init(this);
//            NIMClient.toggleNotification(true);
//        }

        DemoCache.setContext(getApplicationContext());
        com.netease.nim.uikit.SPUtils.instance(getApplicationContext());
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, IMConfig.loginInfo(), IMConfig.options(getApplicationContext()));
        // crash handler
        IMConfig.initUiKit(getApplicationContext());
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


    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = SessionListActivity.class; // 点击通知栏跳转到该Activity
        //  config.notificationSmallIconId = R.drawable.ic_stat_notify_msg;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用采用默认路径作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        //  String sdkPath = getAppCacheDir(context) + "/nim"; // 可以不设置，那么将采用默认路径
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        // options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;
        return options;
    }

    private LoginInfo loginInfo() {
        // 从本地读取上次登录成功时保存的用户登录信息
        String account = SPUtils.getString("userId");
        String token = SPUtils.getString("IMToken");
        if (!account.equals("") && !token.equals("")) {
            NimUIKit.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }


    }

}
