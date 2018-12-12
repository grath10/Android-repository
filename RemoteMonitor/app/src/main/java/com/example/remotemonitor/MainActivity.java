package com.example.remotemonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.remotemonitor.ui.cameralist.EZCameraListActivity;
import com.example.remotemonitor.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.jump_real_play_btn:
                if (TextUtils.isEmpty(MonitorApplication.APP_KEY)){
                    Toast.makeText(this,"Appkey为空",Toast.LENGTH_LONG).show();
                    return;
                }
                MonitorApplication.getOpenSDK().setAccessToken("at.d4a1aatbdo8or36s8n2j7prxbj26rsqw-7lh9jnnyhn-0yyelzd-yu1oo9wsc");
                Intent toIntent = new Intent(MainActivity.this, EZCameraListActivity.class);
                toIntent.putExtra("deviceSerial", "524007615");
                toIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(toIntent);
                break;
            default:
                break;
        }
    }
}
