package com.service.community.ui.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.weather.LocalWeatherLive;
import com.hyphenate.easeui.SPUtils;
import com.service.community.R;
import com.service.community.model.AdListBean;
import com.service.community.model.UserEditBean;
import com.service.community.ui.activity.AdActivity;
import com.service.community.ui.activity.MoreServiceActivity;
import com.service.community.ui.utils.MyToast;
import com.service.community.ui.view.BannerView;
import com.service.community.ui.view.Lunar;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter {


    private List<AdListBean.DatasBean.ItemBean.ListBean> images = new ArrayList<>();
    private List<AdListBean.DatasBean.ItemBean.ListBean> noticeList = new ArrayList<>();
    private List<String> noticeimg = new ArrayList<>();
    private List<String> imgUrls = new ArrayList<>();
    private List<String> adtitles = new ArrayList<>();

    public ArrayList<UserEditBean> getDatas() {
        return datas;
    }

    private ArrayList<UserEditBean> datas = new ArrayList<>();

    private Context context;
    private LocalWeatherLive weatherlive;
    private String nickname;
    public static final int TYPE_HOME = 0;
    public static final int TYPE_WEATHER = 1;
    public static final int TYPE_BANNER = 2;
    public static final int TYPE_SERVICE = 3;
    public static final int TYPE_SERVICE_MORE = 4;

    public HomeAdapter(Context context) {

        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;

        if (TYPE_HOME == viewType) {
            View view = View.inflate(context, R.layout.layout_item_home, null);
            holder = new HomeHolder(view);
        } else if (TYPE_WEATHER == viewType) {
            View view = mInflater.inflate(R.layout.layout_item_weather, parent, false);
            holder = new WeatherHolder(view);
        } else if (TYPE_BANNER == viewType) {
            View view = mInflater.inflate(R.layout.layout_item_banner, parent, false);
            holder = new BannerHolder(view);
        } else if (TYPE_SERVICE == viewType) {
            View view = mInflater.inflate(R.layout.layout_item_service, parent, false);
            holder = new ServiceHolder(view);
        } else if (TYPE_SERVICE_MORE == viewType) {
            View view = mInflater.inflate(R.layout.layout_item_service_more, parent, false);
            holder = new ServiceMoreHolder(view);
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HomeHolder) {
            if (nickname != null) {

                ((HomeHolder) holder).tvName.setText(nickname + "，\n欢迎来到社区助老服务平台");
            } else {
                String username = SPUtils.getString("username");
                if (username.equals("")) {
                    ((HomeHolder) holder).tvName.setText("您好" + "，\n欢迎来到社区助老服务平台");
                } else {
                    ((HomeHolder) holder).tvName.setText(username + "，\n欢迎来到社区助老服务平台");
                }

            }
            if (noticeList.size() > 0) {
                if (noticeList.size()<2){
                    ((HomeHolder) holder).tv_content.setText("公告：" + noticeList.get(0).message);
                }else{
                    noticeimg.add("http://img.bimg.126.net/photo/ZZ5EGyuUCp9hBPk6_s4Ehg==/5727171351132208489.jpg");
                    noticeimg.add("http://img1.imgtn.bdimg.com/it/u=2735633715,2749454924&fm=26&gp=0.jpg");
                    BannerView.startBanner(((HomeHolder) holder).banner, noticeimg, true);
                    ((HomeHolder) holder).banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int i, float v, int i1) {

                        }

                        @Override
                        public void onPageSelected(int i) {
                            if (i<noticeList.size()){

                                ((HomeHolder) holder).tv_content.setText("公告：" + noticeList.get(i).message+"=="+i+"==");
                            }

                        }

                        @Override
                        public void onPageScrollStateChanged(int i) {

                        }
                    });
                }


            }


        } else if (holder instanceof WeatherHolder) {
            if (weatherlive != null) {
                String weather = weatherlive.getWeather();
                String city = weatherlive.getCity();
                String temperature = weatherlive.getTemperature();
                String reportTime = weatherlive.getReportTime();
                String liveweather = city + "/" + weather;
                ((WeatherHolder) holder).tvWeather.setText(liveweather);
                Calendar calendar = Calendar.getInstance();
                Lunar lunar = new Lunar(calendar);
                String lunarString = lunar.toString();
                String week = lunar.getWeek(reportTime);
                String curDate = lunar.getCurDate("yyyy年MM月dd日");
                ((WeatherHolder) holder).tvTime.setText("更新于：" + reportTime.substring(10, reportTime.length() - 3));
                ((WeatherHolder) holder).tvDate.setText(curDate + "," + week + "\n" + lunarString);
            }

        } else if (holder instanceof BannerHolder) {
            for (int i = 0; i < images.size(); i++) {
                imgUrls.add(images.get(i).url);
                adtitles.add(images.get(i).name);
            }
            BannerView.startBanner(((BannerHolder) holder).mbanner, imgUrls, adtitles, true);
            ((BannerHolder) holder).mbanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Intent intent = new Intent(context, AdActivity.class);
                    context.startActivity(intent);
                }
            });


        } else if (holder instanceof ServiceHolder) {

            ((ServiceHolder) holder).llSession1.setOnClickListener(sessionClick);
            ((ServiceHolder) holder).llSession2.setOnClickListener(sessionClick);
            ((ServiceHolder) holder).llSession3.setOnClickListener(sessionClick);
            ((ServiceHolder) holder).llSession4.setOnClickListener(sessionClick);
            ((ServiceHolder) holder).llSession5.setOnClickListener(sessionClick);
            ((ServiceHolder) holder).llSession6.setOnClickListener(sessionClick);


        } else if (holder instanceof ServiceMoreHolder) {
            ((ServiceMoreHolder) holder).llContact.setOnClickListener(sessionClick);
            ((ServiceMoreHolder) holder).llMoreService.setOnClickListener(sessionClick);
        }
    }

    @Override
    public int getItemCount() {
        int count = (datas == null ? 0 : datas.size());
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HOME;
            case 1:
                return TYPE_WEATHER;

            case 2:
                return TYPE_BANNER;

            case 3:
                return TYPE_SERVICE;
            case 4:
                return TYPE_SERVICE_MORE;

            default:
                return 0;
        }
    }

    public void addWeather(LocalWeatherLive weatherlive) {
        this.weatherlive = weatherlive;
    }

    public void addNickName(String nickname) {
        this.nickname = nickname;
    }

    public void addAdList(ArrayList<AdListBean.DatasBean.ItemBean.ListBean> images) {
        this.images = images;
    }

    public void addNoticeList(ArrayList<AdListBean.DatasBean.ItemBean.ListBean> noticeList) {
        this.noticeList = noticeList;
    }


    class HomeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.banner)
        Banner banner;

        public HomeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    class WeatherHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_weather)
        TextView tvWeather;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_date)
        TextView tvDate;

        public WeatherHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    class BannerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner)
        Banner mbanner;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_content)
        TextView tv_content;

        public BannerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


    class ServiceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_session1)
        LinearLayout llSession1;
        @BindView(R.id.ll_session3)
        LinearLayout llSession3;
        @BindView(R.id.ll_session5)
        LinearLayout llSession5;
        @BindView(R.id.ll_session2)
        LinearLayout llSession2;
        @BindView(R.id.ll_session4)
        LinearLayout llSession4;
        @BindView(R.id.ll_session6)
        LinearLayout llSession6;

        public ServiceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ServiceMoreHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_contact)
        LinearLayout llContact;
        @BindView(R.id.ll_more_service)
        LinearLayout llMoreService;

        public ServiceMoreHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void addData(List data, boolean refresh) {
        if (refresh) {
            datas.clear();
        }
        datas.addAll(data);
        notifyDataSetChanged();
    }


    private ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void onItemClick(int position);

    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;

    }

    View.OnClickListener sessionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_session4:
                    MyToast.show("敬请期待");
                    break;
                case R.id.ll_contact:
                    callPhone("16639171251");
                    break;
                case R.id.ll_more_service:
                    Intent intent = new Intent(context, MoreServiceActivity.class);
                    context.startActivity(intent);
                    break;
                default:
//                    if (NimUIKit.getAccount() != null) {
//                        NimUIKit.startP2PSession(context, "32");
//                    } else {
//                        MyToast.show("请先登录");
//                    }
                    break;
            }
        }
    };

    public void callPhone(String phoneNum) {
        if (checkReadPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PERMISSION)) {

            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse("tel:" + phoneNum);
            intent.setData(data);
            context.startActivity(intent);
        }
    }

    public boolean checkReadPermission(String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(context, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions((Activity) context, new String[]{string_permission}, request_code);
        }
        return flag;
    }

    public static final int REQUEST_CALL_PERMISSION = 10111;


}
