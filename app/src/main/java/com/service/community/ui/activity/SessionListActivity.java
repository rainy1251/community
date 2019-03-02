package com.service.community.ui.activity;

import com.service.community.R;
import com.service.community.ui.base.BaseActivity;

public class SessionListActivity extends BaseActivity {


    @Override
    public int getLayoutId() {
        return R.layout.activity_session_list;
    }

    @Override
    public void initView() {
        setToolbarNoEN(R.id.toolbar,"消息列表");

    }

    @Override
    public void initData() {

    }

    @Override
    protected void initListener() {

    }
}
