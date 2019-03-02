package com.service.community.ui.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.service.community.R;
import com.service.community.model.AdListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreServiceAdapter extends RecyclerView.Adapter {


    public ArrayList<AdListBean.DatasBean.ItemBean.ListBean> getDatas() {
        return datas;
    }

    private ArrayList<AdListBean.DatasBean.ItemBean.ListBean> datas = new ArrayList<>();

    private Context context;
    public MoreServiceAdapter(Context context) {

        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.more_service_layout, parent, false);
        RecyclerView.ViewHolder holder = new MoreServiceViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MoreServiceViewHolder) {
            ((MoreServiceViewHolder) holder).tv_name.setText(datas.get(position).serverName);
            ((MoreServiceViewHolder) holder).tv_num.setText(datas.get(position).linkTel);
            if (datas.get(position).url.contains("http")){

                Glide.with(context).load(datas.get(position).url).into(((MoreServiceViewHolder) holder).iv_img);
            }
            ((MoreServiceViewHolder) holder).ll_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callPhone(datas.get(position).linkTel);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count = (datas == null ? 0 : datas.size());
        return count;
    }

    class MoreServiceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_num)
        TextView tv_num;
        @BindView(R.id.iv_img)
        ImageView iv_img;
        @BindView(R.id.ll_bg)
        LinearLayout ll_bg;

        public MoreServiceViewHolder(View itemView) {
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
