package com.example.dell.app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.dell.app.entity.CollectedPoint;
import com.example.dell.app.entity.PerfDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryDataActivity extends AppCompatActivity {
    private static final String TAG = "HistoryDataActivity";
    private List<CollectedPoint> cpList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_data);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        Intent intent = getIntent();
        String startTime = intent.getStringExtra("startTime");
        String endTime = intent.getStringExtra("endTime");
        String clientId = intent.getStringExtra("clientId");
        initCollectedPoints(startTime, endTime, clientId);
        recyclerView = (RecyclerView)findViewById(R.id.history_data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PerfDataAdapter adapter = new PerfDataAdapter(cpList);
        recyclerView.setAdapter(adapter);
    }

    private void initCollectedPoints(String startTime, String endTime, String clientId){
        for (int i = 0; i < 3; i++) {
            CollectedPoint cp = new CollectedPoint();
            cp.setCollectedTime("2017-09-29 08:2" + i);
            List<String> list = new ArrayList<>();
            list.add("1." + i);
            cp.setDataList(list);
            cpList.add(cp);
        }
    }
}
