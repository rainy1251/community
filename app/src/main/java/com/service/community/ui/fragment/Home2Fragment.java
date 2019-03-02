package com.service.community.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.uikit.Contents;
import com.netease.nim.uikit.SPUtils;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.service.community.R;
import com.service.community.avchat.PermissionUtils;
import com.service.community.model.AdListBean;
import com.service.community.model.ImAccidBean;
import com.service.community.model.StateBean;
import com.service.community.model.UserInfoBean;
import com.service.community.net.GenericsCallback;
import com.service.community.net.JsonGenericsSerializator;
import com.service.community.ui.activity.AdActivity;
import com.service.community.ui.activity.EditUserDetailActivity;
import com.service.community.ui.activity.MoreServiceActivity;
import com.service.community.ui.activity.RegisterActivity;
import com.service.community.ui.activity.SessionListActivity;
import com.service.community.ui.base.BaseFragment;
import com.service.community.ui.utils.MyLog;
import com.service.community.ui.utils.MyToast;
import com.service.community.ui.utils.NetUtils;
import com.service.community.ui.utils.UiUtils;
import com.service.community.ui.view.BannerView;
import com.service.community.ui.view.Lunar;
import com.service.community.ui.view.MessageEvent;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class Home2Fragment extends BaseFragment implements WeatherSearch.OnWeatherSearchListener {


    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.weather_icon)
    ImageView weatherIcon;
    @BindView(R.id.iv_audio1)
    ImageView iv_audio1;
    @BindView(R.id.iv_audio2)
    ImageView iv_audio2;
    @BindView(R.id.iv_audio3)
    ImageView iv_audio3;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_date)
    TextView tvDate;
    Unbinder unbinder;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_msg_one)
    TextView tvMsgOne;
    @BindView(R.id.tv_msg_two)
    TextView tvMsgTwo;
    @BindView(R.id.ll_session1)
    LinearLayout llSession1;
    @BindView(R.id.ll_session2)
    LinearLayout llSession2;
    @BindView(R.id.ll_session3)
    LinearLayout llSession3;
    @BindView(R.id.ll_session4)
    LinearLayout llSession4;
    @BindView(R.id.ll_session5)
    LinearLayout llSession5;
    @BindView(R.id.ll_session6)
    LinearLayout llSession6;
    @BindView(R.id.ll_session7)
    LinearLayout llSession7;
    @BindView(R.id.ll_session8)
    LinearLayout llSession8;
    private WeatherSearch mweathersearch;
    private WeatherSearchQuery mquery;
    private List<LocalDayWeatherForecast> data;
    public AMapLocationClient mLocationClient = null;
    private String mCity;
    MediaPlayer mediaPlayer = new MediaPlayer();
    //异步获取定位结果
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    mCity = aMapLocation.getCity();
                    queryWeather(WeatherSearchQuery.WEATHER_TYPE_FORECAST);
                    queryWeather(WeatherSearchQuery.WEATHER_TYPE_LIVE);
                }
            }
        }
    };
    private String address;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home2;
    }

    @Override
    public void initView() {
        if (checkReadPermission(Manifest.permission.ACCESS_FINE_LOCATION, 200)) {

            initGaoDe();
        }
        if (PermissionUtils.checkReadPermission(new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , PermissionUtils.REQUEST_Location, getActivity())) {
        }
        // doLogin("1310119485", "0F696AE1D7D1130266F5A647EB860F0B");
    }

    @Override
    public void initData() {

        EventBus.getDefault().register(this);
        requestInfo();
        requestAdList();
        requestNoticeList();
    }

    private List<String> noticeimg = new ArrayList<>();
    private List<String> imgUrls = new ArrayList<>();
    private List<String> adtitles = new ArrayList<>();

    /**
     * 请求广告
     */
    private void requestAdList() {
        NetUtils.getBuildByGet("/app/ad/list?type=1&page=1&limit=10&sort=add_time&order=desc")
                .execute(new GenericsCallback<AdListBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onResponse(AdListBean response, int id) {
                        ArrayList<AdListBean.DatasBean.ItemBean.ListBean> list = response.data.items.list;
                        for (int i = 0; i < list.size(); i++) {
                            imgUrls.add(list.get(i).url);
                            // adtitles.add("【" + list.get(i).name + "】" );
                            adtitles.add("【" + list.get(i).name + "】" + "\n" + UiUtils.delHTMLTag(list.get(i).content));
                        }
                        BannerView.startBanner(banner, imgUrls, adtitles, true);
                    }
                });
    }

    ArrayList<AdListBean.DatasBean.ItemBean.ListBean> textlist = new ArrayList<>();
    ArrayList<AdListBean.DatasBean.ItemBean.ListBean> audiolist = new ArrayList<>();

    /**
     * 请求公告
     */
    private void requestNoticeList() {
        NetUtils.getBuildByGet("/app/notice/list?page=1&limit=10&sort=id&order=desc")
                .execute(new GenericsCallback<AdListBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onResponse(AdListBean response, int id) {
                        ArrayList<AdListBean.DatasBean.ItemBean.ListBean> list = response.data.items.list;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).noticeType == 1) {
                                textlist.add(list.get(i));
                            } else {
                                audiolist.add(list.get(i));
                            }

                        }

                        if (textlist.size() > 0 && textlist.size() < 2) {
                            tvMsgOne.setText("-" + textlist.get(0).message);
                            tvMsgTwo.setVisibility(View.GONE);
                        } else if (list.size() >= 2) {
                            tvMsgOne.setText("-" + textlist.get(0).message);
                            tvMsgTwo.setText("-" + textlist.get(1).message);
                            tvMsgTwo.setVisibility(View.VISIBLE);
                        }
                        if (audiolist.size() > 0 && audiolist.size() < 2) {
                            iv_audio1.setVisibility(View.VISIBLE);
                        } else if (audiolist.size() == 2) {
                            iv_audio1.setVisibility(View.VISIBLE);
                            iv_audio2.setVisibility(View.VISIBLE);
                        } else if (audiolist.size() > 2) {
                            iv_audio1.setVisibility(View.VISIBLE);
                            iv_audio2.setVisibility(View.VISIBLE);
                            iv_audio3.setVisibility(View.VISIBLE);
                        }


                    }
                });
    }

    @Override
    protected void initListener() {
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getContext(), AdActivity.class);
                getContext().startActivity(intent);
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SessionListActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 请求用户信息
     */
    private void requestInfo() {
        NetUtils.getBuildByGet("/app/userInfo/detail").execute(new GenericsCallback<UserInfoBean>(new JsonGenericsSerializator()) {
            @Override
            public void onResponse(UserInfoBean response, int id) {

                if (response.errmsg.equals("成功")) {
                    String nickname = response.data.nickname;
                    String mobile = response.data.mobile;
                    String mobileOne = response.data.mobileOne;
                    String mobileTwo = response.data.mobileTwo;
                    String mobileThree = response.data.mobileThree;
                    address = response.data.address;
                    SPUtils.save(Contents.MobileOne, mobileOne);
                    SPUtils.save(Contents.MobileTwo, mobileTwo);
                    SPUtils.save(Contents.MobileThree, mobileThree);
                    tvName.setText(nickname + "，" + mobile + "\n欢迎来到社区助老服务平台");

                }
            }
        });
    }

    /**
     * 初始化高德地图
     */
    private void initGaoDe() {

//初始化定位
        mLocationClient = new AMapLocationClient(UiUtils.getContext());
//设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
//启动定位
        mLocationClient.startLocation();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

    }

    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == 1000) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();
                tvArea.setText(mCity);
                tvTemperature.setText(weatherlive.getTemperature() + "°");
                tvWeather.setText("/" + weatherlive.getWeather() + "(实时)");
                String reportTime = weatherlive.getReportTime();
                Calendar calendar = Calendar.getInstance();
                Lunar lunar = new Lunar(calendar);
                String lunarString = lunar.toString();
                String week = lunar.getWeek(reportTime);
                String curDate = lunar.getCurDate("yyyy年MM月dd日");
                tvTime.setText(reportTime.substring(10, reportTime.length() - 3));
                tvDate.setText(curDate + "\n" + week + "\n" + lunarString);

                //  tvTime.setText(weatherlive.getReportTime() + "发布");
                // tvWind.setText(weatherlive.getWindDirection() + "风     " + weatherlive.getWindPower() + "级");
            } else {
                MyToast.show("获取天气失败");
            }
        } else {
            MyToast.show("获取天气失败");
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
        List<LocalDayWeatherForecast> local = localWeatherForecastResult.getForecastResult().getWeatherForecast();


        if (local.size() > 0) {
//            MyLog.show(local.size() + "==local.size()");
//            MyLog.show(local.get(0).getNightWeather());
        } else {

        }
    }

    /**
     * 查询天气
     *
     * @param type
     */
    private void queryWeather(int type) {
        mquery = new WeatherSearchQuery(mCity, type);
        mweathersearch = new WeatherSearch(getActivity());
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn();
    }

    public boolean checkReadPermission(String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(getActivity(), string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{string_permission}, request_code);
        }
        return flag;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("更新")) {
            requestInfo();
        }
    }


    @OnClick({R.id.ll_session1, R.id.ll_session2, R.id.ll_session3, R.id.ll_session4, R.id.ll_session5,
            R.id.ll_session6, R.id.ll_session7, R.id.ll_session8, R.id.iv_audio1, R.id.iv_audio2, R.id.iv_audio3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_session1:
                String token = SPUtils.getString("token");
                if (token.equals("")){
                    showLoginDialog();
                    return;
                }
                if (address==null){
                    showSetAddressDialog();
                    return;
                }
                if (address.isEmpty()){
                    showSetAddressDialog();
                    return;
                }
                SPUtils.save(Contents.GOSHOPPING,true);
                getAccid(1);
                break;
            case R.id.ll_session2:
                SPUtils.save(Contents.GOSHOPPING,false);
                getAccid(2);

                break;
            case R.id.ll_session3:
                SPUtils.save(Contents.GOSHOPPING,false);
                getAccid(3);

                break;
            case R.id.ll_session4:
                MyToast.show("敬请期待");
                break;
            case R.id.ll_session5:
                showPhoneDialog();
                break;
            case R.id.ll_session6:
                SPUtils.save(Contents.GOSHOPPING,false);
                getAccid(4);
                break;
            case R.id.ll_session7:
                SPUtils.save(Contents.GOSHOPPING,false);
                 getAccid(5);
               // sendp2p("51");
                break;
            case R.id.ll_session8:
                Intent intent = new Intent(getActivity(), MoreServiceActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.iv_audio1:
                startmusic(0);
                break;
            case R.id.iv_audio2:
                startmusic(1);
                break;
            case R.id.iv_audio3:
                startmusic(2);
                break;
        }
    }

    private void sendp2p(String userId) {
        if (NimUIKit.getAccount() != null) {
            NimUIKit.startP2PSession(getActivity(), userId);
        } else {
            MyToast.show("请先登录");
        }
    }

    public void callPhone(String phoneNum) {
        if (checkReadPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PERMISSION)) {

            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            getActivity().startActivity(intent);
        }
    }

    public static final int REQUEST_CALL_PERMISSION = 10111;

    public void doLogin(String account, String token) {
        //MyToast.show(account+token);

        NimUIKit.login(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                MyToast.show("登录IM成功");
                String account = param.getAccount();
                String token = param.getToken();
                com.netease.nim.uikit.SPUtils.save(Contents.IMAccoune, account);
                com.netease.nim.uikit.SPUtils.save(Contents.IMToken, token);
                NimUIKit.setAccount(account);
                AVChatKit.setAccount(account);
            }

            @Override
            public void onFailed(int code) {

                if (code == 302 || code == 404) {
                    MyToast.show("登录IM失败");
                } else {
                    MyToast.show("登录IM失败:" + code);
                }

            }

            @Override
            public void onException(Throwable exception) {
                MyToast.show("登录IM异常");

            }


        });

//
    }

    boolean isplay = true;

    private void startmusic(int position) {
        if (isplay) {

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(audiolist.get(position).attachUrl);
                MyToast.show("准备播放");
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                        isplay = false;
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (mediaPlayer != null) {
                MyToast.show("正在停止播放");
                mediaPlayer.stop();
                isplay = true;
            }
        }
    }

    public void getAccid(int value) {
        NetUtils.getBuildByGet("/app/im/getImAccid?resourceCode=" + value).execute(new GenericsCallback<ImAccidBean>(new JsonGenericsSerializator()) {
            @Override
            public void onResponse(ImAccidBean response, int id) {
                if (response.errno == 0) {
                    String accid = response.data;
                    sendp2p(accid);
                } else if (response.errno == 501) {

                    showLoginDialog();
                } else {
                    MyToast.show(response.errmsg);
                }
            }
        });
    }


    public void showPhoneDialog() {
        String mobileOne = SPUtils.getString(Contents.MobileOne);
        String mobileTwo = SPUtils.getString(Contents.MobileTwo);
        String mobileThree = SPUtils.getString(Contents.MobileThree);
        final String[] mobiles = new String[]{mobileOne, mobileTwo, mobileThree};
        if (mobileOne.equals("") && mobileTwo.equals("") && mobileThree.equals("")) {

            showTipsDialog();
            return;
        }
        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 设置参数
        builder.setTitle("请点击您想要拨打的亲情号")
                .setItems(mobiles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callPhone(mobiles[which]);
                    }
                });


        builder.create().show();
    }

    public void showTipsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 设置参数
        builder.setTitle("提示")
                .setMessage("请到我的编辑中设置亲情号")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        if (SPUtils.getString("token").equals("")) {
                            return;
                        }
                        Intent intent = new Intent(getActivity(), EditUserDetailActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 设置参数
        builder.setTitle("提示")
                .setMessage("请先登录")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(getActivity(), RegisterActivity.class);
                        intent.putExtra("isLogin", true);
                        startActivity(intent);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public void showSetAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 设置参数
        builder.setTitle("提示")
                .setMessage("请先设置收货地址")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(getActivity(), EditUserDetailActivity.class);

                        startActivity(intent);

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}