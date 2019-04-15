package com.service.community.ui.activity;

import android.media.MediaPlayer;
import android.widget.ListView;

import com.service.community.R;
import com.service.community.model.AdListBean;
import com.service.community.net.GenericsCallback;
import com.service.community.net.JsonGenericsSerializator;
import com.service.community.ui.adapter.NoticeListAdapter;
import com.service.community.ui.base.BaseActivity;
import com.service.community.ui.utils.MyLog;
import com.service.community.ui.utils.MyToast;
import com.service.community.ui.utils.NetUtils;
import com.service.community.ui.view.MessageNoticePlayEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;

public class NoticeListActivity extends BaseActivity {

    @BindView(R.id.lv_view)
    ListView mListView;
    private NoticeListAdapter noticeListAdapter;
    boolean isplay = true;
    MediaPlayer mediaPlayer = new MediaPlayer();
    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_list;
    }

    @Override
    public void initView() {
        setToolbarNoEN(R.id.toolbar, "公告列表");
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        requestNoticeList();
        noticeListAdapter = new NoticeListAdapter(this);
        mListView.setAdapter(noticeListAdapter);
    }

    @Override
    protected void initListener() {

    }
    /**
     * 请求公告
     */
    private void requestNoticeList() {
        NetUtils.getBuildByGet("/app/notice/list?page=1&limit=30&sort=id&order=desc")
                .execute(new GenericsCallback<AdListBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onResponse(AdListBean response, int id) {
                        ArrayList<AdListBean.DatasBean.ItemBean.ListBean> list = response.data.items.list;
                        noticeListAdapter.addData(list,true);
                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageNoticePlayEvent messageEvent) {
        if (messageEvent.getMessage()!=null) {
            String url = messageEvent.getMessage();
            MyLog.show(url);
            startmusic(url);
        }
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
    private void startmusic(String url) {
        if (isplay) {

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
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

}
