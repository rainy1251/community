package com.service.community.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.service.community.R;
import com.service.community.model.AdListBean;
import com.service.community.net.GenericsCallback;
import com.service.community.net.JsonGenericsSerializator;
import com.service.community.ui.adapter.MoreServiceAdapter;
import com.service.community.ui.adapter.UserDetailAdapter;
import com.service.community.ui.base.BaseActivity;
import com.service.community.ui.utils.NetUtils;

import butterknife.BindView;

public class MoreServiceActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerview;
    private MoreServiceAdapter mRecycleAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_more_service;
    }

    @Override
    public void initView() {
        setToolbarNoEN(R.id.toolbar,"更多服务");
        initRecycleView();
    }

    @Override
    public void initData() {
        requestService();

    }

    private void requestService() {
        NetUtils.getBuildByGet("/app/server/list?page=1&limit=10&sort=add_time&order=desc")
                .execute(new GenericsCallback<AdListBean>(new JsonGenericsSerializator()) {
            @Override
            public void onResponse(AdListBean response, int id) {
                mRecycleAdapter.addData(response.data.items.list,true);
            }
        });
    }

    @Override
    protected void initListener() {

    }

    /**
     * 初始化recycleview
     */
    private void initRecycleView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, OrientationHelper.VERTICAL, false);
        mRecyclerview.setLayoutManager(layoutManager);
        mRecycleAdapter = new MoreServiceAdapter(this);
        mRecyclerview.setAdapter(mRecycleAdapter);
    }


}
