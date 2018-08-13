package com.killaxiao.library.asynctask;

import org.json.JSONObject;

/**
 * Created by KillaXiao on 2016/8/29.
 * 获取json的通用接口
 */
public interface UniversalInterface {
    public void result(JSONObject json);
    public void failed(JSONObject json);
}
