package com.service.community.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.service.community.R;
import com.service.community.avchat.PermissionUtils;
import com.service.community.ui.base.BaseActivity;
import com.service.community.ui.fragment.FindFragment;
import com.service.community.ui.fragment.Home2Fragment;
import com.service.community.ui.fragment.HomeFragment;
import com.service.community.ui.fragment.WodeFragment;
import com.service.community.ui.view.ChangeHomeEvent;
import com.service.community.ui.view.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.rb_menu_home)
    RadioButton rbMenuHome;
    @BindView(R.id.rb_menu_find)
    RadioButton rbMenuFind;
    @BindView(R.id.rb_menu_wode)
    RadioButton rbMenuWode;
    @BindView(R.id.rg_menu)
    RadioGroup rgMenu;
    private Fragment preFragment;
    private List<Fragment> fragments;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        if (PermissionUtils.checkReadPermission(new String[]{
                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , PermissionUtils.REQUEST_Location, this)) {
        }
        EventBus.getDefault().register(this);
    }
    public static void logout(Context context, boolean b) {

    }
    @Override
    public void initData() {

        fragments = new ArrayList<Fragment>();
        fragments.add(new Home2Fragment());
        fragments.add(new FindFragment());
        fragments.add(new WodeFragment());
        rgMenu.setOnCheckedChangeListener(this);
        rbMenuHome.setChecked(true);

    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_menu_home:
                changeFragment(0);
                break;
            case R.id.rb_menu_find:
                changeFragment(1);
//                Intent intent = new Intent(this, FindActivity.class);
//                startActivity(intent);
                break;
            case R.id.rb_menu_wode:

                changeFragment(2);
                break;
        }
    }

    private void changeFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        Fragment fragment = fragments.get(index);
        if (preFragment != null) {
            transaction.hide(preFragment);
        }
        if (fragment.isAdded()) {

            transaction.show(fragment);
        } else {

            transaction.add(R.id.container, fragment);
        }
        //transaction.commit();
        transaction.commitAllowingStateLoss();
        preFragment = fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            showDialog(100);
//            return true;
//        } else
//            return super.onKeyDown(keyCode, event);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof FindFragment) {//判断是不是所属Fragment
            WebView mwebview = (WebView) fragment.getView().findViewById(R.id.mwebview);
            if (mwebview.canGoBack()) {
                mwebview.goBack();
            } else {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    showDialog(100);
                    return true;
                } else
                    return super.onKeyDown(keyCode, event);

            }
        }else{
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                showDialog(100);
                return true;
            } else
                return super.onKeyDown(keyCode, event);

        }
        return false;

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 100) {
            return new AlertDialog.Builder(this)
                    .setMessage("确定要退出?")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    android.os.Process
                                            .killProcess(android.os.Process
                                                    .myPid());
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            }).create();
        }
        return null;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onSaveInstanceState(Bundle outState) {
    //        super.onSaveInstanceState(outState, outPersistentState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(ChangeHomeEvent messageEvent) {
        if (messageEvent.getMessage().equals("gohome")) {
           changeFragment(0);
            rbMenuHome.setChecked(true);
        }
    }
}