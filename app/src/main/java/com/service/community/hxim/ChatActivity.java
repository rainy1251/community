package com.service.community.hxim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.SPUtils;
import com.service.community.R;
import com.service.community.model.UserInfoBean;
import com.service.community.net.GenericsCallback;
import com.service.community.net.JsonGenericsSerializator;
import com.service.community.ui.utils.MyToast;
import com.service.community.ui.utils.NetUtils;

public class ChatActivity extends AppCompatActivity {

    private String username;
    private String nickname;
    private String headUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = getIntent().getStringExtra(Constant.EXTRA_USER_ID);
        NetUtils.getBuildByGetNoToken("/app/userInfo/getUserInfoById?userId=" + username).execute(new GenericsCallback<UserInfoBean>(new JsonGenericsSerializator()) {
            @Override
            public void onResponse(UserInfoBean response, int id) {
                if (response.errno==0){

                  nickname = response.data.nickname;
                    headUrl = response.data.headUrl;
                }else{
                    nickname=username;
                }
                ChatFragment chatFragment = new ChatFragment();
                Bundle args = new Bundle();
                args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                args.putString(EaseConstant.EXTRA_USER_ID, username);
                args.putString(EaseConstant.EXTRA_USER_NICKNAME, nickname);
                chatFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
            }
        });
//
    }
}
