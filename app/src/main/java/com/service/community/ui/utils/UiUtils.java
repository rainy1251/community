package com.service.community.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.service.community.R;
import com.service.community.ui.base.BaseApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/20.
 */

public class UiUtils {
    public static boolean isAdd;

    public static Context getContext() {
        return BaseApplication.getInstance();
    }

    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static void startActivity(Class<?> activityClass) {
        Intent intent = new Intent(getContext(), activityClass);
        getContext().startActivity(intent);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 验证手机号格式
     */
    public static boolean isMobile(String number) {

        String USERNAME_REGEX = "[1][123456789]\\d{9}";//"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、4，5、7,8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return number.matches(USERNAME_REGEX);

    }

    //判断email格式是否正确
    public static boolean isEmail(String email) {
        String EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

        return email.matches(EMAIL_REGEX);
    }

    /**
     * 验证密码格式(必须为字母+数字)
     */
    public static boolean isPWD(String number) {
        //String PASSWORD_REGEX = "[0-9A-Za-z]{6,18}";
        String PASSWORD_REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";
        return number.matches(PASSWORD_REGEX);
    }

    /**
     * 验证密码格式(支持全数字,字母)
     */
    public static boolean isPWDALL(String number) {
        String PASSWORD_REGEX = "[0-9A-Za-z]{6,18}";
        return number.matches(PASSWORD_REGEX);
    }

    /**
     * 验证用户名
     *
     * @param username 用户名 字母 数字 下划线 汉字
     * @return boolean
     */
    public static boolean checkUsername(String username) {
        String regex = "([a-zA-Z0-9_\\u4e00-\\u9fa5]{2,25})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        return m.matches();
    }
   /**
     * 验证中文
     *
     * @return boolean
     */
    public static boolean isCh (String s) {
        String regex = "^[\\u4e00-\\u9fa5]+$";
        return s.matches(regex);
    }

    /**
     * 压缩图片
     *
     * @param filePath
     * @param targetPath
     * @param quality
     * @return
     */
    public static String compressImage(String filePath, String targetPath, int quality) {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片

        File appDir = new File(targetPath, "yaopai");
        if (!appDir.exists()) {
            // 目录不存在 则创建
            appDir.mkdirs();
        }
        //系统当前时间的毫秒值作为图片的名字
        String fileName = "avatar.jpg";

        File outputFile = new File(appDir, fileName);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
        }

        return outputFile.getPath();
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 720, 1280);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //    public static void noMore(ArrayList list, int index) {
//        if (list.size() < one && index > one) {
//            MyToast.show("被你看光了");
//
//        }
//    }
    public static void noMore(ArrayList list, int index, ListView mListView) {
        if (list.size() == 0) {
            View view = View.inflate(UiUtils.getContext(), R.layout.layout_none_footer, null);
            if (mListView.getFooterViewsCount() < 1 && index > 1) {
                mListView.addFooterView(view, null, false);
                MyToast.show("被你看光了");
            } else {
                mListView.removeFooterView(view);
            }
        }
    }

    /**
     * 格式化价格
     *
     * @param num
     * @return
     */
    public static String formatNum(double num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }

    public static double bigDecimalSub(double d1, double d2) { // 进行减法运算
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        return b1.subtract(b2).doubleValue();
    }


    public static String delHTMLTag(String htmlStr) {
        if (htmlStr==null){
            return "";
        }
        Pattern p_w = Pattern.compile(regEx_w, Pattern.CASE_INSENSITIVE);
        Matcher m_w = p_w.matcher(htmlStr);
        htmlStr = m_w.replaceAll(""); // 过滤script标签


        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签


        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签


        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签


        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签


        htmlStr = htmlStr.replaceAll(" ", ""); //过滤
        return htmlStr.trim(); // 返回文本字符串
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 获取代理
     *
     * @return
     */
    public static String getUserAgent() {

        String appVersion;
        try {
            appVersion = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        String user_agent = "YAOPAI/Android " + appVersion;
        return user_agent;
    }

    /**
     * 验证输入的身份证号是否合法
     */
    public static boolean isLegalId(String id) {
        if (id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     */
    public static String getCompressPath() {
        File appDir = new File(Environment.getExternalStorageDirectory() + "/YAOPAI", "article");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        return appDir.getPath();
    }


    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符
    private static final String regEx_w = "<w[^>]*?>[\\s\\S]*?<\\/w[^>]*?>";//定义所有w标签

    public static String getDirPath() {
        String dirPath = Environment.getExternalStorageDirectory() + File.separator + "SheQu" ;
        return dirPath;
    }

    public static String getPicPath(int objectHandle) {
        String picPath = getDirPath() + File.separator + objectHandle + ".jpg";
        return picPath;
    }


}
