package com.video;

import android.app.Application;

import com.videogo.openapi.EZOpenSDK;

/**
 * 自定义应用
 */
public class MonitorApplication extends Application {
    // 开发者填入自己申请的appkey
    public static String AppKey = "0b41210808f74024a9bd1b6f059ada63";
    public static final String AppSecret = "dd8a902318cfd405e98e817d2aea0b34";

    public static EZOpenSDK getOpenSDK() {
        return EZOpenSDK.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initSDK();
    }

    private void initSDK() {
        /**
         * sdk日志开关，正式发布需要去掉
         */
        EZOpenSDK.showSDKLog(true);

        /**
         * 设置是否支持P2P取流,详见api
         */
        EZOpenSDK.enableP2P(true);

        /**
         * APP_KEY请替换成自己申请的
         */
        EZOpenSDK.initLib(this, AppKey, "");
    }
}
