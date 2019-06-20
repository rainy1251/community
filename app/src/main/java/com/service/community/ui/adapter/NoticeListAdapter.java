package com.service.community.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.service.community.R;
import com.service.community.model.AdListBean;
import com.service.community.ui.adapter.base.BaseHolder;
import com.service.community.ui.adapter.base.DefaultAdapter;
import com.service.community.ui.utils.UiUtils;
import com.service.community.ui.view.MessageNoticePlayEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeListAdapter extends DefaultAdapter {


    Context context;

    public NoticeListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected BaseHolder getHolder(int position) {
        return new ViewHolder();
    }


    public class ViewHolder extends BaseHolder<AdListBean.DatasBean.ItemBean.ListBean> {


        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.iv_audio)
        ImageView ivAudio;

        @Override
        protected View initView() {
            View view = View.inflate(context, R.layout.layout_item_noticelist, null);
            ButterKnife.bind(this, view);
            return view;
        }

        @Override
        protected void refreshView(final AdListBean.DatasBean.ItemBean.ListBean resultBean, final int position) {
                if (resultBean.noticeType==1){
                    tvTitle.setText(resultBean.title);
                    tvMessage.setText(UiUtils.delHTMLTag(resultBean.message));
                    ivAudio.setVisibility(View.GONE);
                }else{
                    tvTitle.setText("语音公告");
                    ivAudio.setVisibility(View.VISIBLE);
                    tvMessage.setVisibility(View.GONE);
                    ivAudio.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new MessageNoticePlayEvent(resultBean.attachUrl));
                        }
                    });
                }
        }
    }


}
