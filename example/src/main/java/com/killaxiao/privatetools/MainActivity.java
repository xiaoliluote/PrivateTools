package com.killaxiao.privatetools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_httprequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    private void initView(){
        bt_httprequest = (Button)findViewById(R.id.bt_httprequest);
    }

    private void initListener(){
        bt_httprequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.bt_httprequest:
                intent = new Intent(this,HttpRequestExample.class);
                startActivity(intent);
                break;
        }
    }
}
