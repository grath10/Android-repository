package com.example.dell.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.app.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryConditionActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "HistoryConditionActivity";
    private Spinner spinner;
    private RelativeLayout selectStartDateTime;
    private RelativeLayout selectEndDateTime;
    private CustomDatePicker startDatePicker;
    private CustomDatePicker endDatePicker;
    private TextView startDateTime;
    private TextView endDateTime;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_condition);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        spinner = (Spinner)findViewById(R.id.history_client_picker);
        selectStartDateTime = (RelativeLayout)findViewById(R.id.selectStartTime);
        selectStartDateTime.setOnClickListener(this);
        selectEndDateTime = (RelativeLayout)findViewById(R.id.selectEndTime);
        selectEndDateTime.setOnClickListener(this);
        startDateTime = (TextView)findViewById(R.id.startTime);
        endDateTime = (TextView)findViewById(R.id.endTime);
        btn = (Button)findViewById(R.id.query_history);
        btn.setOnClickListener(this);
        initSpinner();
        initDateTimePicker();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.selectStartTime:
                startDatePicker.show(startDateTime.getText().toString());
                break;
            case R.id.selectEndTime:
                endDatePicker.show(endDateTime.getText().toString());
            case R.id.query_history:
                queryHistoryData();
                break;
            default:
                break;
        }
    }

    private void queryHistoryData(){
        String startTime = startDateTime.getText().toString();
        String endTime = endDateTime.getText().toString();
        if(startTime.compareTo(endTime) > 0){
            Toast.makeText(this, getString(R.string.wrong_time_tip), Toast.LENGTH_SHORT).show();
            return;
        }
        String clientId = (String)spinner.getSelectedItem();
//        Intent intent = new Intent(this, HistoryDataActivity.class);
        Intent intent = new Intent(this, LineChartActivity.class);
        intent.putExtra("startTime", startTime);
        intent.putExtra("endTime", endTime);
        intent.putExtra("clientId", clientId);
        startActivity(intent);
    }

    private void initSpinner(){
        String[] clients = new String[]{"61780001", "61780002"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, clients);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initDateTimePicker(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        startDateTime.setText(now);
        startDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                startDateTime.setText(time);
            }
        }, "2017-01-01 00:00", now);
        startDatePicker.showSpecificTime(true);
        startDatePicker.setIsLoop(true);

        endDateTime.setText(now);
        endDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler(){
            @Override
            public void handle(String time) {
                endDateTime.setText(time);
            }
        }, "2017-01-01 00:00", now);
        endDatePicker.showSpecificTime(true);
        endDatePicker.setIsLoop(true);
    }
}
