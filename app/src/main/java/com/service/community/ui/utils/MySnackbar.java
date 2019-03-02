package com.service.community.ui.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Administrator on 2017/5/10.
 */

public class MySnackbar {
    public static void show(String str,View view){

        Snackbar.make(view,str, Snackbar.LENGTH_LONG).show();
    }

}
