package com.service.community.ui.adapter.base;

import android.view.View;

/**
 * Created by wangx on 2016/7/30.
 */
public abstract class BaseHolder<Data> {
    private final View contentView;

    private Data data;
    private int position;

    public BaseHolder() {
        contentView = initView();
        contentView.setTag(this);
    }



    public View getContentView() {
        return contentView;
    }

    public void setData(Data data,int position) {
        this.data = data;
        this.position = position;
        refreshView(data,position);
    }


    /**
     * 1.创建view对象 2.将view 初始化操作
     * @return
     */
    protected  abstract  View initView();

    /**
     * 将数据显示到对应的控件上
     * @param data
     */
    protected abstract void refreshView(Data data,int position);

}
