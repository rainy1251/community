package com.service.community.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.service.community.R;
import com.service.community.model.JsonBean;
import com.service.community.model.StateBean;
import com.service.community.model.UserEditBean;
import com.service.community.model.UserInfoBean;
import com.service.community.net.GenericsCallback;
import com.service.community.net.JsonGenericsSerializator;
import com.service.community.ui.base.BaseActivity;
import com.service.community.ui.utils.Constants;
import com.service.community.ui.utils.MyLog;
import com.service.community.ui.utils.MyToast;
import com.service.community.ui.utils.NetUtils;
import com.hyphenate.easeui.SPUtils;
import com.service.community.ui.view.GetJsonDataUtil;
import com.service.community.ui.view.MessageEvent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.common.FileResult;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class EditUserDetailActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_right)
    TextView tvRight;
    //    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerview;
//    private UserDetailAdapter mRecycleAdapter;
    ArrayList<UserEditBean> datas = new ArrayList<>();
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_choose_sex)
    TextView tvChooseSex;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_phone1)
    EditText etPhone1;
    @BindView(R.id.et_phone2)
    EditText etPhone2;
    @BindView(R.id.et_phone3)
    EditText etPhone3;
    @BindView(R.id.tv_choose_address)
    TextView tvChooseAddress;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_shequphone)
    EditText etShequphone;
    private ArrayList<JsonBean> optionsSex = new ArrayList<>();
    public static final int CHANGE_AVATAR = 0x002;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 0x003;
    private ImageView imgView;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options4Items = new ArrayList<>();
    private String avatarUrl;
    private String tx;
    private String nickname;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_user_detail;
    }

    @Override
    public void initView() {
        setToolbarNoEN(R.id.toolbar, "编辑");
        tvRight.setText("保存");
        tvRight.setVisibility(View.VISIBLE);
        //initRecycleView();
    }

    @Override
    public void initData() {

        optionsSex.add(new JsonBean("女", "0", "", ""));
        optionsSex.add(new JsonBean("男", "1", "", ""));
        optionsSex.add(new JsonBean("未知", "2", "", ""));
        requestInfo();
    }

    @Override
    protected void initListener() {

    }

    /**
     * 请求用户信息
     */
    private void requestInfo() {
        NetUtils.getBuildByGet("/app/userInfo/detail").execute(new GenericsCallback<UserInfoBean>(new JsonGenericsSerializator()) {


            @Override
            public void onResponse(UserInfoBean response, int id) {

                if (response.errmsg.equals("成功")) {
                    avatarUrl = response.data.headUrl;
                    nickname = response.data.nickname;
                    tx = response.data.gender;
                    Glide.with(EditUserDetailActivity.this).load(avatarUrl).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(ivAvatar);
                    etName.setText(nickname);
                    tvSex.setText(response.data.gender);
                    etPhone.setText(response.data.mobile);
                    etPhone1.setText(response.data.mobileOne);
                    etPhone2.setText(response.data.mobileTwo);
                    etPhone3.setText(response.data.mobileThree);
                    etAddress.setText(response.data.address);
                    etShequphone.setText(response.data.communityappMobile);

                }
            }
        });
    }

    @OnClick({R.id.back, R.id.tv_right, R.id.iv_avatar, R.id.tv_choose_sex, R.id.tv_choose_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:

                showIsSaveDialog();
                break;
            case R.id.tv_right:
                String addressDetail = etAddress.getText().toString().trim();
                String userName = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String phone1 = etPhone1.getText().toString().trim();
                String phone2 = etPhone2.getText().toString().trim();
                String phone3 = etPhone3.getText().toString().trim();
                String communityappMobile = etShequphone.getText().toString().trim();
                Map<String, String> map = new HashMap();
                map.put("address", address + addressDetail);
                map.put("communityappMobile", communityappMobile);
                map.put("gender", tx);
                map.put("headUrl", avatarUrl);
                map.put("mobile", phone);
                map.put("mobileOne", phone1);
                map.put("mobileThree", phone3);
                map.put("mobileTwo", phone2);
                map.put("nickname", userName);
                String postContent = getPostContent(map);
                NetUtils.getBuildByPostToken("/app/userInfo/saveOrEdit", postContent).execute(new GenericsCallback<StateBean>(new JsonGenericsSerializator()) {
                    @Override
                    public void onResponse(StateBean response, int id) {
                        if (response.errno == 0) {
                            MyToast.show("保存成功");
                            EventBus.getDefault().post(new MessageEvent("更新"));
                            finish();
                        } else {
                            MyToast.show(response.errmsg);
                        }
                    }
                });

                break;

            case R.id.iv_avatar:
                if (ContextCompat.checkSelfPermission(EditUserDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditUserDetailActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);

                } else {
                    changeAvatar();
                }
                break;
            case R.id.tv_choose_sex:
                changeSex();
                break;
            case R.id.tv_choose_address:
                parseLocalJsonData();
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            showIsSaveDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 是否保存
     */
    public void showIsSaveDialog() {

        // 创建构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle("确认要退出吗?")
                .setMessage("退出后将不会保存")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        dialog.dismiss();
                        finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case CHANGE_AVATAR:
                //调用相册
                if (data != null) {
                    if (data.getData() != null) {
                        Cursor cursor = this.getContentResolver().query(data.getData(),
                                new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                        cursor.moveToFirst();
                        String imgPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        cursor.close();
                        if (imgPath != null) {

                            String token = SPUtils.getString("token");
                            File file = new File(imgPath);
                            Tiny.FileCompressOptions compressOptions = new Tiny.FileCompressOptions();
                            compressOptions.size = 200;
                            compressOptions.quality = 100;
                            FileResult result = Tiny.getInstance().source(file).asFile().withOptions(compressOptions).compressSync();
                            String filePath = result.outfile;
                            File compressfile = new File(filePath);
                            OkHttpUtils.getInstance()
                                    .post()
                                    .addHeader("Content-Disposition", "form-data;filename=enctype")
                                    .addHeader("X-communityapp-Token", token)
                                    .url(Constants.BASE_URL + "/app/upload/uploadImg")
                                    .addFile("file", compressfile.getName(), compressfile)
                                    .build().execute(new GenericsCallback<UserInfoBean>(new JsonGenericsSerializator()) {
                                @Override
                                public void onResponse(UserInfoBean response, int id) {
                                    if (response.errno == 0) {
                                        avatarUrl = response.data.url;
                                        MyLog.show(avatarUrl);

                                    }

                                }
                            });
                            Glide.with(this).load(compressfile).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(ivAvatar);

                        }
                    }

                    break;
                }

        }
    }

    /**
     * 调用手机相册
     */
    private void changeAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CHANGE_AVATAR);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                changeAvatar();
            } else {
                MyToast.show("Permission Denied");
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 解析地区json
     */
    public void parseLocalJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        ArrayList<JsonBean> jsonBean = parseData();//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            // ArrayList<JsonBean.CityBean> CityList = (ArrayList<JsonBean.CityBean>) options1Items.get(i).getCityList();
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            ArrayList<ArrayList<String>> Province_IdList = new ArrayList<>();//该省的所有地区ID列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市

                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                ArrayList<String> City_IdList = new ArrayList<>();//该省的城市列表（第二级）
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据

                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d).getName();
                        String CityId = jsonBean.get(i).getCityList().get(c).getArea().get(d).getId();
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                        City_IdList.add(CityId);

                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
                Province_IdList.add(City_IdList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
            options4Items.add(Province_IdList);
        }

        showPickerLocal();
    }

    public ArrayList<JsonBean> parseData() {//Gson 解析
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
        Gson gson = new Gson();
        JsonBean jsonBean = gson.fromJson(JsonData, JsonBean.class);
        ArrayList<JsonBean> country = jsonBean.country;
        return country;
    }

    String address = "";

    /**
     * 显示地区picker
     */
    private void showPickerLocal() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                // tvArea.setText(options3Items.get(options1).get(options2).get(options3));
                String area = options3Items.get(options1).get(options2).get(options3);
                String province = options2Items.get(options1).get(options2);
                if (province.equals(area)) {
                    address = area + "市";
                } else {
                    address = province + "省" + area + "市";
                }
                tvChooseAddress.setText(address);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setCyclic(false, false, false)//循环与否
                .setOutSideCancelable(true)// default is true
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /**
     * 更换性别
     */

    private void changeSex() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tx = optionsSex.get(options1).getPickerViewText();
                tvSex.setText(tx);
            }
        })
                .setTitleText("选择性别")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.BLACK)//设置分割线的颜色
                .setTitleColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .isCenterLabel(false)
                .build();
        pvOptions.setPicker(optionsSex);//一级选择器
        pvOptions.show();
    }


}
