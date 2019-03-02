package com.service.community.ui.utils;


import android.util.Log;

/**
 * Created by Administrator on 2018/one/18.
 */

public class MyLog {
    private static final boolean isDeBug = true;
    public static void show(Object value){
        if (isDeBug){

            Log.i("rain",value.toString());
        }
    }
}
