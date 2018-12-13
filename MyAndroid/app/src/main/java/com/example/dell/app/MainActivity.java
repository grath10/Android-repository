package com.example.dell.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private Button btn_broadcast;
    private Button btn_version;
    private Button btn_feedback;
    private Button btn_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        btn_broadcast = (Button)findViewById(R.id.broadcast);
        btn_broadcast.setOnClickListener(this);
        btn_version = (Button)findViewById(R.id.query_version);
        btn_version.setOnClickListener(this);
        btn_history = (Button)findViewById(R.id.history);
        btn_history.setOnClickListener(this);
        btn_feedback = (Button)findViewById(R.id.feedback);
        btn_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.broadcast:
                intent = new Intent(MainActivity.this, BroadcastActivity.class);
                break;
            case R.id.feedback:
                intent = new Intent(MainActivity.this, FeedbackActivity.class);
                break;
            case  R.id.history:
                intent = new Intent(MainActivity.this, HistoryConditionActivity.class);
                break;
            case R.id.query_version:
                intent = new Intent(MainActivity.this, VersionActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
