package com.service.community.ui.utils;

import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/26.
 */

public class MyToast {
    public static void show(String str){
        Toast.makeText(UiUtils.getContext(),str,Toast.LENGTH_SHORT).show();
    }



}
