package com.killaxiao.library.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.killaxiao.library.help.AppConstants;
import com.killaxiao.library.widget.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by KillaXiao on 2016/8/29.
 * http请求post 通用接口,返回JSONObject
 */
public class HttpUniversalPostAsyncTask extends AsyncTask<String,String,JSONObject>{

    private UniversalInterface exectu;
    private Context mContext;
    private String mUrl;
    private CustomProgressDialog progressDialog;
    private int request_time=10000;

    private final String ERROR_IO = "{\"statusCode\":-10000002,\"errorMsg\":\"服务器出现故障\"}";
    private final String ERROR_JSON ="{\"statusCode\":-10000003,\"errorMsg\":\"json解析异常\"}";
    private String Request_method ="POST";
    private HashMap<String,String> mRequestProperty;

    /**
     * 不显示dialog的构造函数
     * @param context
     * @param url  请求的网址
     * @param inter  回调接口
     */
    public HttpUniversalPostAsyncTask(Context context, String url,UniversalInterface inter){
        this.mContext = context;
        this.mUrl = url;
        this.exectu = inter;
        Log.e(AppConstants.LOG_E,"当前访问网址："+mUrl);
    }

    /**
     * 带dialog的构造函数
     * @param context
     * @param url 请求的网址
     * @param dialog_show 是否显示dialog
     * @param inter 回调接口
     */
    public HttpUniversalPostAsyncTask(Context context, String url,boolean dialog_show,UniversalInterface inter){
        this.mContext = context;
        this.mUrl = url;
        this.exectu = inter;
        if(dialog_show){
            progressDialog = CustomProgressDialog.createDialog(mContext);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        Log.e(AppConstants.LOG_E,"当前访问网址："+mUrl);
    }

    public void setDialogCancelable(boolean cancelable){
        if(progressDialog != null){
            progressDialog.setCancelable(cancelable);
            progressDialog.setCanceledOnTouchOutside(cancelable);
        }
    }

    public void setRequest_method(String method){
        this.Request_method = method;
    }

    public void setRequestProperty(HashMap<String,String> requestProperty){
        this.mRequestProperty =requestProperty;
    }

    /**
     * 带dialog的构造函数
     * @param context
     * @param url 请求的网址
     * @param dialog_show 是否显示dialog
     * @param inter 回调接口
     */
    public HttpUniversalPostAsyncTask(Context context, String url,boolean dialog_show,int time,UniversalInterface inter){
        this.mContext = context;
        this.mUrl = url;
        this.exectu = inter;
        this.request_time = time;
        if(dialog_show){
            progressDialog = CustomProgressDialog.createDialog(mContext);
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(true);
        }
        Log.e(AppConstants.LOG_E,"当前访问网址："+mUrl);
    }

    @Override
    protected void onPreExecute() {
        if(progressDialog != null){
            progressDialog.show();
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject obj = null;

        try {
            URL url = new URL(mUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(Request_method);
            if(Request_method.equals(AppConstants.REQUEST_POST)){
                connection.setDoOutput(true);
                connection.setDoInput(true);
            }
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Content-Type", "application/json");
            if(mRequestProperty != null){
                for (Map.Entry<String, String> entry : mRequestProperty.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            connection.setConnectTimeout(request_time);
            connection.setReadTimeout(request_time);

//            if(mParams != null){
//                connection.setRequestProperty("Content-Length",
//                        String.valueOf(mParams.getBytes().length));
//                OutputStream os = connection.getOutputStream();
//                os.write(mParams.getBytes());
//                os.flush();
//            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "UTF-8");
                sb.append(lines);
            }
            //System.out.println(sb);
            reader.close();
            connection.disconnect();
            obj = new JSONObject(sb.toString());
        }catch (Exception e){
            e.printStackTrace();
            try {
                obj = new JSONObject(ERROR_IO);
            } catch (JSONException e1) {
            }
        }

        return obj;
    }


    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        if(exectu != null) {
            if (jsonObject != null) {
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
                exectu.result(jsonObject);
            }else{
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
                try {
                    exectu.failed(new JSONObject(ERROR_JSON));
                }catch (Exception e){}
            }
        }

    }

}
