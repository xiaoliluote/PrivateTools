package com.killaxiao.privatetools;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;

import me.nereo.multi_image_selector.MultiImageSelector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_httprequest,bt_photo_album;

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
    }

    private void initListener(){
        bt_httprequest.setOnClickListener(this);
        bt_photo_album.setOnClickListener(this);
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
        }
    }
}
