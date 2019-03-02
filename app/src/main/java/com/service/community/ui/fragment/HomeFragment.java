package com.service.community.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.service.community.R;
import com.service.community.model.AdListBean;
import com.service.community.model.UserInfoBean;
import com.service.community.net.GenericsCallback;
import com.service.community.net.JsonGenericsSerializator;
import com.service.community.ui.activity.EditUserDetailActivity;
import com.service.community.ui.adapter.HomeAdapter;
import com.service.community.ui.base.BaseFragment;
import com.service.community.ui.utils.MyLog;
import com.service.community.ui.utils.MyToast;
import com.service.community.ui.utils.NetUtils;
import com.service.community.ui.utils.UiUtils;
import com.service.community.ui.view.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HomeFragment extends BaseFragment implements WeatherSearch.OnWeatherSearchListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerview;
    private HomeAdapter homeAdapter;
    List<String> datas = new ArrayList<>();
    private WeatherSearch mweathersearch;
    private WeatherSearchQuery mquery;
    private List<LocalDayWeatherForecast> data;
    public AMapLocationClient mLocationClient = null;
    private String mCity;
    private List<String> images = new ArrayList<>();
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        if (checkReadPermission(Manifest.permission.ACCESS_FINE_LOCATION, 200)) {

            initGaoDe();
        }

    }

    @Override
    public void initData() {
        datas.add("one");
        datas.add("one");
        datas.add("one");
        datas.add("one");
        datas.add("one");
        homeAdapter.addData(datas, true);
        EventBus.getDefault().register(this);
        requestInfo();
        requestAdList();
        requestNoticeList();
    }

    private void requestAdList() {
        NetUtils.getBuildByGet("/app/ad/list?type=1&page=1&limit=10&sort=add_time&order=desc")
                .execute(new GenericsCallback<AdListBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onResponse(AdListBean response, int id) {
                        ArrayList<AdListBean.DatasBean.ItemBean.ListBean> list = response.data.items.list;
                        homeAdapter.addAdList(list);
                        homeAdapter.notifyItemChanged(2);

                    }
                });
    }

    /**
     * 请求公告
     */
    private void requestNoticeList() {
        NetUtils.getBuildByGet("/app/notice/list?page=1&limit=10&sort=id&order=desc")
                .execute(new GenericsCallback<AdListBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onResponse(AdListBean response, int id) {
                        ArrayList<AdListBean.DatasBean.ItemBean.ListBean> list = response.data.items.list;
                        list.addAll(list);
                        homeAdapter.addNoticeList(list);
                        homeAdapter.notifyItemChanged(0);
                    }
                });
    }

    @Override
    protected void initListener() {

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
                    homeAdapter.addNickName(nickname);
                    homeAdapter.notifyItemChanged(0);
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
        mRecyclerview.setLayoutManager(linearLayoutManager);
        homeAdapter = new HomeAdapter(getActivity());
        mRecyclerview.setAdapter(homeAdapter);
    }

    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == 1000) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();

                homeAdapter.addWeather(weatherlive);
                homeAdapter.notifyItemChanged(1);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("更新")) {
           requestInfo();
        }
    }
}
