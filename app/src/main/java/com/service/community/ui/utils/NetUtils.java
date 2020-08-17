package com.service.community.ui.utils;

import com.hyphenate.easeui.SPUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.request.RequestCall;

import okhttp3.MediaType;

/**
 * Created by Administrator on 2018/3/17.
 */

public class NetUtils {


    public static RequestCall getBuildByPost(String api,String content) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestCall build = OkHttpUtils.getInstance().postString()
                .url(Constants.BASE_URL+api)
                .mediaType(JSON)
                .content(content)
                .build();


        return build;

    }
    public static RequestCall getBuildByPostToken(String api,String content) {
        String token = SPUtils.getString("token");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestCall build = OkHttpUtils.getInstance().postString()
                .addHeader("X-communityapp-Token", token)
                .url(Constants.BASE_URL+api)
                .mediaType(JSON)
                .content(content)
                .build();


        return build;

    }
// public static PostFormBuilder getBuildByPostToken(String api) {
//     String token = SPUtils.getString("token");
//
//     PostFormBuilder postFormBuilder = OkHttpUtils.getInstance().post()
//             .addHeader("X-communityapp-Token", token)
//             .url(Constants.BASE_URL + api);
//
//
//     return postFormBuilder;
//
//    }

    public static RequestCall getBuildByGet(String api) {
        String token = SPUtils.getString("token");
        RequestCall build = OkHttpUtils.get().addHeader("X-communityapp-Token", token).url(Constants.BASE_URL+api).build();

        return build;

    }
    public static RequestCall getBuildByGetNoToken(String api) {

        RequestCall build = OkHttpUtils.get().url(Constants.BASE_URL+api).build();

        return build;

    }


}
