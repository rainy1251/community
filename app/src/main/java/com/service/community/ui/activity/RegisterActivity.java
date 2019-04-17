package com.service.community.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.SPUtils;
import com.service.community.R;
import com.service.community.hxim.DemoHelper;
import com.service.community.model.IMBean;
import com.service.community.model.StateBean;
import com.service.community.net.GenericsCallback;
import com.service.community.net.JsonGenericsSerializator;
import com.service.community.ui.base.BaseActivity;
import com.service.community.ui.utils.MyToast;
import com.service.community.ui.utils.NetUtils;
import com.service.community.ui.utils.UiUtils;
import com.service.community.ui.view.MessageEvent;
import com.service.community.ui.view.TimerCount;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_getCode)
    TextView tvGetCode;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_title)
    TextView tv_title;
    private boolean isLogin;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        isLogin = getIntent().getBooleanExtra("isLogin", false);
        if (isLogin) {
            setToolbarNoEN(R.id.toolbar, "登录");
            tv_title.setText("登录");
        } else {

            setToolbarNoEN(R.id.toolbar, "注册");
            tv_title.setText("注册");
        }
    }

    @Override
    public void initData() {

    }

    @Override
    protected void initListener() {

    }


    @OnClick({R.id.tv_getCode, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_getCode:
                String username = etUsername.getText().toString().trim();
                if (UiUtils.isMobile(username)) {
                    Map<String, String> map = new HashMap();
                    map.put("mobile", username);
                    String postContent = getPostContent(map);
                    String api = "/app/auth/regCaptcha";
                    getCode(api, postContent);
                } else {
                    MyToast.show("请输入正确的手机号");
                }
                break;
            case R.id.tv_register:

                String mobile = etUsername.getText().toString().trim();
                String code = etCode.getText().toString().trim();
                if (mobile.isEmpty() && !UiUtils.isMobile(mobile)) {
                    MyToast.show("请输入手机号");
                    return;
                }
                if (!UiUtils.isMobile(mobile)) {
                    MyToast.show("请输入正确的手机号");
                    return;
                }
                if (code.isEmpty()) {
                    MyToast.show("验证码不能为空");
                    return;
                }

                Map<String, String> map = new HashMap();
                map.put("mobile", mobile);
                map.put("code", code);

                String postContent = getPostContent(map);
                String api = "/app/auth/loginByCaptcha";
                getRegister(api, postContent);

                break;
        }
    }

    /***
     * 获取请求体
     * @return
     * @param map
     */
    private String getPostContent(Map<String, String> map) {
        JSONObject jsonObject = (JSONObject) JSONObject.wrap(map);
        String content = jsonObject.toString();
        return content;
    }

    /**
     * 获取验证码
     */
    private void getCode(String api, String content) {
        NetUtils.getBuildByPost(api, content)
                .execute(new GenericsCallback<StateBean>(new JsonGenericsSerializator(), etUsername) {
                    @Override
                    public void onResponse(StateBean response, int id) {

                        if (response.errmsg.equals("成功")) {
                            MyToast.show("已发送");
                            TimerCount timer = new TimerCount(60000, 1000, tvGetCode, true);
                            timer.start();
                        } else {
                            MyToast.show("发送失败");
                        }

                    }
                });
    }

    /**
     * 注册
     */
    private void getRegister(String api, String content) {
        NetUtils.getBuildByPost(api, content)
                .execute(new GenericsCallback<StateBean>(new JsonGenericsSerializator(), etUsername) {
                    @Override
                    public void onResponse(StateBean response, int id) {

                        if (response.errmsg.equals("成功")) {
                            String userId = response.data.userInfo.userId;
                            String token = response.data.token;
                            SPUtils.save("token", token);
                            SPUtils.save("userId", userId);
                            //doLogin(userId, response.data.imToken);
                            String username = etUsername.getText().toString().trim();
                            loginIM(userId,username);
                            SPUtils.save("IMToken", response.data.imToken);
                            if (!isLogin) {
                                Intent intent = new Intent(RegisterActivity.this, EditUserDetailActivity.class);
                                startActivity(intent);
                            }else{

                            }
                            finish();
                            EventBus.getDefault().post(new MessageEvent("更新"));
                        } else {
                            MyToast.show(response.errmsg);
                        }
                    }
                });
    }
    private void loginIM(String userId,String pwd) {
        EMClient.getInstance().login(userId, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！" + message + "," + code);
            }
        });
    }
    /**
     * 请求im登陆令牌
     *
     * @param userId
     * @param token
     */
    private void requestIMSign(String api, final String userId, final String token) {

        NetUtils.getBuildByGet(api)
                .execute(new GenericsCallback<IMBean>(new JsonGenericsSerializator(), etUsername) {
                    @Override
                    public void onResponse(IMBean response, int id) {

                        if (response.errno == 0) {

                            int index1 = response.data.indexOf("token\":\"");
                            int index2 = response.data.indexOf("\",\"accid\"");
                            if (index2 - index1 > 8) {

                                String IMToken = response.data.substring(index1 + 8, index2);

                                SPUtils.save("IMToken", IMToken);
                            }
                        }


                    }

                });

    }


}
