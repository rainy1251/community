package com.service.community.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.service.community.R;
import com.service.community.model.UserEditBean;
import com.service.community.ui.utils.MyLog;
import com.service.community.ui.utils.UiUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailAdapter extends RecyclerView.Adapter {


    public ArrayList<UserEditBean> getDatas() {
        return datas;
    }

    private ArrayList<UserEditBean> datas = new ArrayList<>();

    private Context context;
    private String area;
    private String avatar;
    private String sex;

    public UserDetailAdapter(Context context) {

        this.context = context;
    }

    public void addArea(String area) {

        this.area = area;
    }

    public void addAvatar(String avatar) {

        this.avatar = avatar;
    }

    public void addSex(String sex) {

        this.sex = sex;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.userdetail_layout, parent, false);
        RecyclerView.ViewHolder holder = new UserEditViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof UserEditViewHolder) {
            if (position == 0) {
                ViewGroup.LayoutParams params = ((UserEditViewHolder) holder).rlContent.getLayoutParams();
                params.height = UiUtils.dip2px(context, 80);
                ((UserEditViewHolder) holder).rlContent.setLayoutParams(params);
                ((UserEditViewHolder) holder).tvEdit.setVisibility(View.VISIBLE);
                ((UserEditViewHolder) holder).etEdit.setVisibility(View.GONE);
                ((UserEditViewHolder) holder).tvEdit.setHint(datas.get(position).content);
                ((UserEditViewHolder) holder).ivAvatar.setVisibility(View.VISIBLE);
                ((UserEditViewHolder) holder).ivAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mItemClickListener.onItemClick(0);

                    }
                });
                if (avatar != null) {

                    Glide.with(context).load(new File(avatar)).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(((UserEditViewHolder) holder).ivAvatar);

                }
            } else if (position == 2) {
                ((UserEditViewHolder) holder).tvChoose.setVisibility(View.VISIBLE);
                ((UserEditViewHolder) holder).tvChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(position);
                    }
                });
                ((UserEditViewHolder) holder).tvEdit.setVisibility(View.VISIBLE);
                ((UserEditViewHolder) holder).etEdit.setVisibility(View.GONE);
                if (sex != null) {

                    ((UserEditViewHolder) holder).tvEdit.setText(sex);

                } else {
                    ((UserEditViewHolder) holder).tvEdit.setText("请选择");
                }
            } else if (position == 7) {
                ((UserEditViewHolder) holder).tvChoose.setVisibility(View.VISIBLE);
                ((UserEditViewHolder) holder).tvChoose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(7);
                    }
                });
                ((UserEditViewHolder) holder).tvEdit.setVisibility(View.GONE);
                ((UserEditViewHolder) holder).etEdit.setVisibility(View.VISIBLE);
                if (area != null) {

                    ((UserEditViewHolder) holder).tvChoose.setText(area);

                } else {
                    ((UserEditViewHolder) holder).tvChoose.setText("请选择 >");
                    ((UserEditViewHolder) holder).etEdit.setHint("请输入");
                }
            } else {
                ((UserEditViewHolder) holder).tvChoose.setVisibility(View.GONE);
                ((UserEditViewHolder) holder).tvEdit.setVisibility(View.GONE);
                ((UserEditViewHolder) holder).etEdit.setVisibility(View.VISIBLE);
                ((UserEditViewHolder) holder).ivAvatar.setVisibility(View.GONE);
                ViewGroup.LayoutParams params = ((UserEditViewHolder) holder).rlContent.getLayoutParams();
                params.height = UiUtils.dip2px(context, 55);
                ((UserEditViewHolder) holder).rlContent.setLayoutParams(params);
                ((UserEditViewHolder) holder).tvEdit.setHint(datas.get(position).content);
            }
            ((UserEditViewHolder) holder).tvTitle.setText(datas.get(position).title);
            ((UserEditViewHolder) holder).etEdit.setHint(datas.get(position).content);
        }
    }

    @Override
    public int getItemCount() {
        int count = (datas == null ? 0 : datas.size());
        return count;
    }

    class UserEditViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_choose)
        TextView tvChoose;
        @BindView(R.id.tv_edit)
        TextView tvEdit;
        @BindView(R.id.et_edit)
        EditText etEdit;
        @BindView(R.id.rl_content)
        RelativeLayout rlContent;
        @BindView(R.id.iv_avatar)
        ImageView ivAvatar;

        public UserEditViewHolder(View itemView) {
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


}
