package com.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.video.ui.cameralist.EZCameraListActivity;
import com.video.ui.common.ResponseMessage;
import com.video.ui.util.NetWorkUtil;
import com.videogo.openapi.bean.EZAccessToken;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 跳转实时预览
 */
public class ButtonEventActivity extends Activity implements OnClickListener {
    private static final String TAG = "ButtonEventActivity";
    public static final int SUCCESS_TOKEN = 1;
    public static final String REQUEST_PATH = "https://open.ys7.com/api/lapp/token/get";

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SUCCESS_TOKEN:
                    ResponseMessage.AccessToken accessToken = (ResponseMessage.AccessToken)msg.obj;
                    String token = accessToken.getAccessToken();
                    MonitorApplication.getOpenSDK().setAccessToken(token);
                    Intent toIntent = new Intent(ButtonEventActivity.this, EZCameraListActivity.class);
                    toIntent.putExtra("deviceSerial", "524007615");
                    toIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(toIntent);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump_page);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.jump_real_play_btn:
                if (TextUtils.isEmpty(MonitorApplication.AppKey)) {
                    Toast.makeText(this, "Appkey为空", Toast.LENGTH_LONG).show();
                    return;
                }
                EZAccessToken ezAccessToken = MonitorApplication.getOpenSDK().getEZAccessToken();
                String accessToken = ezAccessToken.getAccessToken();
                long expire = ezAccessToken.getExpire();
                long current = System.currentTimeMillis();
                if(!NetWorkUtil.isNetConnected(this)){
                    Toast.makeText(getBaseContext(), "检查网络连接设置", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d(TAG, "accessToken标识为: " + accessToken);
                if(accessToken.equals("") || current > expire){
                    getAccessToken();
                }else {
                    Intent toIntent = new Intent(ButtonEventActivity.this, EZCameraListActivity.class);
                    toIntent.putExtra("deviceSerial", "524007615");
                    toIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(toIntent);
                }
                break;
            default:
                break;
        }
    }

    private void getAccessToken(){
        new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                // 构建FormBody，传入要提交的参数
                FormBody formBody = new FormBody.Builder().add("appKey", MonitorApplication.AppKey)
                        .add("appSecret", MonitorApplication.AppSecret).build();

                final Request request = new Request.Builder()
                        .url(REQUEST_PATH).post(formBody).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "accessToken请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new Gson();
                        String responseData = response.body().string();
                        ResponseMessage responseMsg = gson.fromJson(responseData, ResponseMessage.class);
                        Message msg = Message.obtain();
                        msg.obj = responseMsg.getData();
                        msg.what = SUCCESS_TOKEN;
                        handler.sendMessage(msg);
                    }
                });
            }
        }.start();
    }
}
