package com.service.community.ui.activity;

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
import com.service.community.R;
import com.service.community.ui.base.BaseActivity;
import com.service.community.ui.utils.MyLog;
import com.service.community.ui.utils.MyToast;

import java.util.List;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements WeatherSearch.OnWeatherSearchListener {


    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_wind)
    TextView tvWind;
    @BindView(R.id.tv_humidity)
    TextView tvHumidity;
    private WeatherSearch mweathersearch;
    private WeatherSearchQuery mquery;
    private List<LocalDayWeatherForecast> data;
    public AMapLocationClient mLocationClient = null;
    private String mCity;
    private String mAddress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
//初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
        mLocationClient.setLocationListener(mAMapLocationListener);
//启动定位
        mLocationClient.startLocation();



    }


    //异步获取定位结果
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //解析定位结果
                    mAddress = aMapLocation.getAddress();
                    mCity = aMapLocation.getCity();
                    MyLog.show(mCity);
                   // queryWeather(WeatherSearchQuery.WEATHER_TYPE_FORECAST);
                    queryWeather(WeatherSearchQuery.WEATHER_TYPE_LIVE);
                }
            }
        }
    };

    @Override
    public void initData() {

    }


    private void queryWeather(int type) {
        mquery = new WeatherSearchQuery(mCity, type);
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == 1000) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                LocalWeatherLive weatherlive = weatherLiveResult.getLiveResult();
                tvTime.setText(weatherlive.getReportTime() + "发布");
                tvWeather.setText(weatherlive.getWeather());
                tvTemperature.setText(weatherlive.getTemperature() + "°");
                tvWind.setText(weatherlive.getWindDirection() + "风     " + weatherlive.getWindPower() + "级");
                tvHumidity.setText("湿度         " + weatherlive.getHumidity() + "%");
            } else {
                MyToast.show("获取天气失败");
            }
        } else {
            MyToast.show("获取天气失败");
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int rCode) {
        List<LocalDayWeatherForecast> local = localWeatherForecastResult.getForecastResult().getWeatherForecast();


        if (local.size() > 0) {
            MyLog.show(local.size() + "==local.size()");
            MyLog.show(local.get(0).getNightWeather());
        } else {

        }

    }


}
