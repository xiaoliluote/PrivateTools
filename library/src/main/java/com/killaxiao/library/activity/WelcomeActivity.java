package com.killaxiao.library.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.killaxiao.library.R;
import com.killaxiao.library.bean.WelcomeBean;
import com.killaxiao.library.widget.CountDownView;

public class WelcomeActivity extends AppCompatActivity{
    private Context mContext;
    private ImageView img_bg;
    private CountDownView my_countdown;
    private WelcomeBean welcomeBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mContext = this;

        initView();
        initData();
        initListener();
    }

    private void initView(){
        img_bg = (ImageView)findViewById(R.id.img_bg);
        my_countdown = (CountDownView)findViewById(R.id.my_countdown);
    }

    private void initData(){
        Intent intent = getIntent();
        welcomeBean =intent.getParcelableExtra("data");
        if(welcomeBean != null){
            img_bg.setImageResource(welcomeBean.getImg_bg());
            my_countdown.setCountdownTime(welcomeBean.getmCountdownTime());
            my_countdown.setRingColor(welcomeBean.getmRingColor());
            my_countdown.setProgressTextColor(welcomeBean.getmProgessTextColor());
            my_countdown.setProgressTextSize(welcomeBean.getmRingProgessTextSize());
        }
        my_countdown.startCountDown();
    }

    private void initListener(){
        my_countdown.setAddCountDownListener(new CountDownView.OnCountDownFinishListener() {
            @Override
            public void countDownFinished() {
                if(welcomeBean != null){
                    try {
                        if(welcomeBean.getCls() != null) {
                            Class cls = Class.forName(welcomeBean.getCls());
                            Intent intent = new Intent(WelcomeActivity.this, cls);
                            startActivity(intent);
                        }
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    finish();
                }
            }
        });
    }
}
