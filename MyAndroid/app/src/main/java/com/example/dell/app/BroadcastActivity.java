package com.example.dell.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.dell.app.utils.Constants;
import org.eclipse.paho.android.service.MqttAndroidClient;

public class BroadcastActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BroadcastActivity";
    private Button btn_sendMsg;
    private EditText et_messages;
    private SingletonMqttClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        client = ClientManager.getInstance(getApplicationContext());
        et_messages = (EditText) findViewById(R.id.message_broadcast);
        btn_sendMsg = (Button) findViewById(R.id.send_broadcast);
        btn_sendMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_broadcast:
                String msg = et_messages.getText().toString().trim();
                // 删除回车符
                msg = msg.replace("\r\n", "");
                try {
                    int size = calculateMessage(msg);
                    String prefix = String.format("%04x", size);
                    // 发送时手动进行转码处理
                    String payload = prefix + getGBKEncoding(msg);
                    Log.i(TAG, payload);
                    client.publishVoiceMessage(payload, 1, Constants.TOPIC_BROADCAST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    public String getGBKEncoding(String chineseName){
        StringBuilder sb = new StringBuilder();
        try {
            char[]ch = chineseName.toCharArray();
            for (char c : ch){
                if (isChinese(c)){
                    byte[] bytes = String.valueOf(c).getBytes("GBK");
                    for (byte b : bytes){
                        sb.append(Integer.toHexString(b & 0xff));
                    }
                }else{
                    byte b = (byte) c;
                    sb.append(Integer.toHexString(b & 0xff));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString().trim();
    }

    private int calculateMessage(String message){
        char[] arr = message.toCharArray();
        int length = 0;
        for(char character: arr){
            if(isChinese(character)){
                length += 2;
            }else{
                length ++;
            }
        }
        return length;
    }
}
