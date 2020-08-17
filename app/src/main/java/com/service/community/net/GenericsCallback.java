package com.service.community.net;


import android.view.View;

import com.google.gson.Gson;
import com.service.community.ui.utils.MyLog;
import com.service.community.ui.utils.MySnackbar;
import com.service.community.ui.utils.MyToast;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by JimGong on 2016/6/23.
 */

public abstract class GenericsCallback<T> extends Callback<T> {
    IGenericsSerializator mGenericsSerializator;
    View mview;
    private int code;
    private String message;

    public GenericsCallback(IGenericsSerializator serializator, View view) {
        mGenericsSerializator = serializator;
        mview = view;
    }

    public GenericsCallback(IGenericsSerializator serializator) {
        mGenericsSerializator = serializator;
    }

    @Override
    public T parseNetworkResponse(Response response, int id) throws IOException {
        String string = response.body().string();

        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (entityClass == String.class) {
            return (T) string;
        }
        T bean = mGenericsSerializator.transform(string, entityClass);
        return bean;
    }

    @Override
    public boolean validateReponse(Response response, int id) {


        try {

            if (response.code() == 400) {
                String ff = response.body().string();
                Gson gson = new Gson();
//                StateBean bean = gson.fromJson(ff, StateBean.class);
//                if (bean.Message != null) {
//
//                    message = bean.Message;
//                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.validateReponse(response, id);
    }

    @Override
    public void onError(Call call, Exception e, int id) {

MyLog.show(e.toString());
        if (e.toString().contains("400")) {
            if (mview != null) {

                MySnackbar.show(message.trim(), mview);
            }
            return;
        }
        if (e.toString().contains("401")) {

//            Intent intent = new Intent(UiUtils.getContext(), LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            UiUtils.getContext().startActivity(intent);

            return;
        }
        if (e.toString().contains("UnknownHostException")) {
            if (mview != null) {
                MySnackbar.show("网络异常,请检查网络", mview);
            }
            return;
        }
        if (e.toString().contains("404")) {
            if (mview != null) {
                MySnackbar.show("资源未找到", mview);
            }
            return;
        }

        // MySnackbar.show(e.toString(), mview);
       // MyLog.show(e.toString());
    }


}
