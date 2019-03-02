package com.service.community.ui.view;
import android.os.CountDownTimer;
import android.widget.TextView;
/**
 * 计时器
 */
public class TimerCount extends CountDownTimer {

	private TextView bnt;
	private boolean isCode;


    public TimerCount(long millisInFuture, long countDownInterval, TextView bnt,boolean isCode) {
        super(millisInFuture, countDownInterval);
        this.bnt = bnt;
        this.isCode = isCode;
    }

    public TimerCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);

    }

    @Override
    public void onFinish() {

        bnt.setClickable(true);
        if (isCode){

            bnt.setText("获取验证码");
        }else{
            bnt.setText("跳过 0s");
        }

    }

    @Override
    public void onTick(long arg0) {

        bnt.setClickable(false);
        if (isCode){

            bnt.setText(arg0 / 1000 + "\ts");
        }else{
            bnt.setText("跳过 "+arg0 / 1000 + "\ts");
        }


    }


}