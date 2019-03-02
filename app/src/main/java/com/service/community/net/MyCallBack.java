package com.service.community.net;

/**
 * Created by Administrator on 2017/7/25.
 */

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


public abstract class MyCallBack<T> {
    /**
     * UI Thread
     *
     * @param request
     */
    public void onBefore(Request request, int id) {
    }

    /**
     * UI Thread
     *
     * @param
     */
    public void onAfter(int id) {
    }

    /**
     * UI Thread
     *
     * @param progress
     */
    public void inProgress(float progress, long total, int id) {

    }

    /**
     * if you parse reponse code in parseNetworkResponse, you should make this method return true.
     *
     * @param response
     * @return
     */
    public boolean validateReponse(Response response, int id) {
        return response.isSuccessful();
    }

    /**
     * Thread Pool Thread
     *
     * @param response
     */
    public abstract T parseNetworkResponse(Response response, int id) throws Exception;

    public abstract void onError(Call call, Exception e, T  response, int id);

    public abstract void onResponse(T response, int id);


    public static MyCallBack CALLBACK_DEFAULT = new MyCallBack() {

        @Override
        public Object parseNetworkResponse(Response response, int id) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e, Object response, int id) {

        }

        @Override
        public void onResponse(Object response, int id) {

        }
    };

}

