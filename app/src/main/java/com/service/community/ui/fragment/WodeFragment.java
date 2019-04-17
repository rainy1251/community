package com.service.community.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

import com.service.community.R;
import com.service.community.model.UserInfoBean;
import com.service.community.net.GenericsCallback;
import com.service.community.net.JsonGenericsSerializator;
import com.service.community.ui.activity.EditUserDetailActivity;
import com.service.community.ui.activity.RegisterActivity;
import com.service.community.ui.base.BaseFragment;
import com.service.community.ui.utils.NetUtils;
import com.hyphenate.easeui.SPUtils;
import com.service.community.ui.view.ChangeHomeEvent;
import com.service.community.ui.view.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class WodeFragment extends BaseFragment {
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_login)
    TextView tv_login;
    private String errmsg;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_wode;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {

        requestInfo();

    }

    /**
     * 请求用户信息
     */
    private void requestInfo() {
        NetUtils.getBuildByGet("/app/userInfo/detail").execute(new GenericsCallback<UserInfoBean>(new JsonGenericsSerializator()) {
            @Override
            public void onResponse(UserInfoBean response, int id) {
                errmsg = response.errmsg;
                if (response.errmsg.equals("成功")) {
                    Glide.with(getActivity()).load(response.data.headUrl).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(ivAvatar);
                    tv_login.setText(response.data.nickname);
                    SPUtils.save("username", response.data.nickname);
                    SPUtils.save("nickname", response.data.nickname);
                    SPUtils.save("logoUrl", response.data.headUrl);

                }
            }
        });
    }

    @Override
    protected void initListener() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.tv_edit, R.id.tv_register, R.id.ll_login, R.id.back})
    public void onViewClicked(View view) {
        String token = SPUtils.getString("token");
        switch (view.getId()) {
            case R.id.tv_edit:
                if (token.equals("")) {
                    showLoginDialog();
                    return;
                }
                if (errmsg.equals("成功")) {
                    Intent intent_edit = new Intent(getContext(), EditUserDetailActivity.class);
                    startActivity(intent_edit);
                } else {

                }

                break;
            case R.id.tv_register:
//                if (errmsg.equals("成功")) {
//                    return;
//                }
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                EventBus.getDefault().post(new ChangeHomeEvent("gohome"));
                break;
            case R.id.ll_login:

                if (token.equals("")) {
                    Intent intent_login = new Intent(getContext(), RegisterActivity.class);
                    intent_login.putExtra("isLogin", true);
                    startActivity(intent_login);
                } else {
                    showEnterDialog();
                }
//                if (NimUIKit.getAccount() != null) {
//                    showEnterDialog();
//                } else {
//                    Intent intent_login = new Intent(getContext(), RegisterActivity.class);
//                    intent_login.putExtra("isLogin", true);
//                    startActivity(intent_login);
//                }


                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals("更新")) {

            requestInfo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    public void showEnterDialog() {

        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 设置参数
        builder.setTitle("提示")
                .setMessage("切换账号将会退出已有账户")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        Intent intent_login = new Intent(getContext(), RegisterActivity.class);
                        intent_login.putExtra("isLogin", true);
                        startActivity(intent_login);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    public void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // 设置参数
        builder.setTitle("提示")
                .setMessage("请先登录")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(getActivity(), RegisterActivity.class);
                        intent.putExtra("isLogin", true);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }
}
