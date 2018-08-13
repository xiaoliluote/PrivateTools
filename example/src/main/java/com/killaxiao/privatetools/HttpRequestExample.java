package com.killaxiao.privatetools;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.killaxiao.library.asynctask.HttpUniversalPostAsyncTask;
import com.killaxiao.library.asynctask.UniversalInterface;
import com.killaxiao.library.help.AppConstants;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018/8/13.
 */

public class HttpRequestExample extends AppCompatActivity implements View.OnClickListener{
    private final String url = "https://www.sojson.com/open/api/weather/json.shtml?city=%E5%8C%97%E4%BA%AC";

    private Button bt_dialog_normal,bt_no_dialog,bt_dialog_error;
    private TextView request_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_httprequest);

        initView();
        initListener();
    }

    private void initView(){
        bt_dialog_normal = (Button)findViewById(R.id.bt_dialog_normal);
        bt_no_dialog = (Button)findViewById(R.id.bt_no_dialog);
        bt_dialog_error = (Button)findViewById(R.id.bt_dialog_error);
        request_text = (TextView)findViewById(R.id.request_text);
    }

    private void initListener(){
        bt_dialog_normal.setOnClickListener(this);
        bt_no_dialog.setOnClickListener(this);
        bt_dialog_error.setOnClickListener(this);
    }

    private void RequestByDialog(){
        HttpUniversalPostAsyncTask task = new HttpUniversalPostAsyncTask(this, url, true, new UniversalInterface() {
            @Override
            public void result(JSONObject json) {
                request_text.setText("请求成功：返回至方法result(),请求结果:\r\n"+json.toString());
            }

            @Override
            public void failed(JSONObject json) {
                request_text.setText("请求失败：返回至方法failed(),请求结果:\r\n"+json.toString());
            }
        });
        task.setRequest_method(AppConstants.REQUEST_GET);
        task.execute();
    }

    private void RequestByNoDialog(){
        HttpUniversalPostAsyncTask task = new HttpUniversalPostAsyncTask(this, url, new UniversalInterface() {
            @Override
            public void result(JSONObject json) {
                request_text.setText("请求成功：返回至方法result(),请求结果:\r\n"+json.toString());
            }

            @Override
            public void failed(JSONObject json) {
                request_text.setText("请求失败：返回至方法failed(),请求结果:\r\n"+json.toString());
            }
        });
        task.setRequest_method(AppConstants.REQUEST_GET);
        task.execute();
    }
    private void RequestByNoDialogErrorUrl(){
        HttpUniversalPostAsyncTask task = new HttpUniversalPostAsyncTask(this, "", new UniversalInterface() {
            @Override
            public void result(JSONObject json) {
                request_text.setText("请求成功：返回至方法result(),请求结果:\r\n"+json.toString());
            }

            @Override
            public void failed(JSONObject json) {
                request_text.setText("请求失败：返回至方法failed(),请求结果:\r\n"+json.toString());
            }
        });
        task.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_dialog_normal:
                RequestByDialog();
                break;
            case R.id.bt_no_dialog:
                RequestByNoDialog();
                break;
            case R.id.bt_dialog_error:
                RequestByNoDialogErrorUrl();
                break;
        }
    }
}
