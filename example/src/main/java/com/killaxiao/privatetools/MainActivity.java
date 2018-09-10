package com.killaxiao.privatetools;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.killaxiao.library.activity.WelcomeActivity;
import com.killaxiao.library.bean.WelcomeBean;

import me.nereo.multi_image_selector.MultiImageSelector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_httprequest,bt_photo_album,bt_countdown,bt_countdown2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView(){
        bt_httprequest = (Button)findViewById(R.id.bt_httprequest);
        bt_photo_album = (Button)findViewById(R.id.bt_photo_album);
        bt_countdown = (Button)findViewById(R.id.bt_countdown);
        bt_countdown2 = (Button)findViewById(R.id.bt_countdown2);
    }

    private void initListener(){
        bt_httprequest.setOnClickListener(this);
        bt_photo_album.setOnClickListener(this);
        bt_countdown.setOnClickListener(this);
        bt_countdown2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.bt_httprequest:
                intent = new Intent(this,HttpRequestExample.class);
                startActivity(intent);
                break;
            case R.id.bt_photo_album:
                PermissionsUtil.requestPermission(this, new PermissionListener() {

                    @Override
                    public void permissionGranted(@NonNull String[] permissions) {

                        MultiImageSelector selector = MultiImageSelector.create();

                        selector.showCamera(true);   //是否显示拍照功能

                        selector.count(3);   //允许选择几张图片
                        selector.multi();   //是否多张模式， 如果是单张模式，则方法为single();
                        selector.start(MainActivity.this, 0x1);   //自定义一个int参数返回至onActivityResult
                    }


                    @Override
                    public void permissionDenied(@NonNull String[] permissions) {

                    }
                }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
                break;
            case R.id.bt_countdown:
                intent = new Intent(this,WelcomeActivity.class);
                /** 参数分别为，倒计时结束后跳转的类名，背景图片，倒计时时间，右上角倒计时圆圈颜色，倒计时字体颜色，倒计时字体大小（只接受dimen） **/
                WelcomeBean welcomeBean = new WelcomeBean(null,WelcomeBean.DEFAULT_IMG_BG,WelcomeBean.DEFAULT_COUNTDOWNTIME,WelcomeBean.DEFAULT_RINGCOLOR,
                        WelcomeBean.DEFAULT_TEXTCOLOR,WelcomeBean.DEFAULT_TEXTSIZE);
                intent.putExtra("data",welcomeBean);
                startActivity(intent);
                break;
            case R.id.bt_countdown2:
                intent = new Intent(this,WelcomeActivity.class);
                /** 参数分别为，倒计时结束后跳转的类名，背景图片，倒计时时间，右上角倒计时圆圈颜色，倒计时字体颜色，倒计时字体大小（只接受dimen） **/
                WelcomeBean welcomeBean2 = new WelcomeBean("com.killaxiao.privatetools.HttpRequestExample",R.drawable.test_bg,WelcomeBean.DEFAULT_COUNTDOWNTIME,WelcomeBean.DEFAULT_RINGCOLOR,
                        Color.parseColor("#ffff00"),WelcomeBean.DEFAULT_TEXTSIZE);
                intent.putExtra("data",welcomeBean2);
                startActivity(intent);
                break;
        }
    }
}
