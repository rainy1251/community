package com.service.community.ui.adapter.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangx on 2016/7/30.
 */
public abstract class DefaultAdapter<T> extends BaseAdapter {


    public DefaultAdapter() {

    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    private List<T> datas = new ArrayList<>(); //  mvc  mvp

    public DefaultAdapter(Context context) {
        this.context = context;
    }

    private Context context;

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder<T> holder = null;
        if (convertView == null) {
            holder = getHolder(position);
        } else {
            holder = (BaseHolder<T>) convertView.getTag();
        }

        T t = datas.get(position);
        holder.setData(t, position);

        return holder.getContentView();
    }

    protected abstract BaseHolder<T> getHolder(int position);
//    public void addData(List<T> data) {
//        if (null != data) {
//            datas.addAll(data);
//        }
//        notifyDataSetChanged();
//    }

    public void addData(List<T> data, boolean refresh) {
        if (refresh) {
            datas.clear();
        }
        datas.addAll(data);
        notifyDataSetChanged();
    }

    public void clearData() {
        datas.clear();
        notifyDataSetChanged();
    }

    public void loadMoreData(List<T> data) {

        if (null == data) {
            return;
        }
        datas.addAll(data);
        notifyDataSetChanged();
    }

}
