package com.example.remotemonitor;

import android.app.Application;

import com.videogo.openapi.EZOpenSDK;

public class MonitorApplication extends Application {
    // 开发者填入申请appKey
    public static String APP_KEY = "0b41210808f74024a9bd1b6f059ada63";

    public static EZOpenSDK getOpenSDK() {
        return EZOpenSDK.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
    }

    // 初始化sdk
    private void initSDK() {
        // sdk日志开关，正式发布需要去掉
        EZOpenSDK.showSDKLog(true);

        //APP_KEY替换成自己申请的
        EZOpenSDK.initLib(this, APP_KEY, "");
    }
}
