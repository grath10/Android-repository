package com.example.dell.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dell.app.utils.Constants;

public class FeedbackActivity extends AppCompatActivity {
    private static final String TAG = "FeedbackActivity";
    private String[] items = new String[]{"61780001:2", "61780002:6"};
    private Button control_btn;
    private Spinner client_picker;
    private LinearLayout outerLayout;
    private SingletonMqttClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        control_btn = (Button)findViewById(R.id.button);
        client_picker = (Spinner)findViewById(R.id.clientPicker);
        outerLayout = (LinearLayout)findViewById(R.id.switchContainer);
        control_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String clientInfo = (String)client_picker.getSelectedItem();
                String[] clientArr = clientInfo.split(":");
                String clientId = clientArr[0];
                int children = outerLayout.getChildCount();
                byte[] states = new byte[children];
                for (int i = 0; i < children; i++) {
                   LinearLayout innerLayout = (LinearLayout)outerLayout.getChildAt(i);
                   SwitchCompat oneSwitch = (SwitchCompat) innerLayout.getChildAt(1);
                   boolean status = oneSwitch.isChecked();
                   states[i] = status ? 1: (byte)0;
                }
                dispatchControlMessages(clientId, states);
            }
        });
        client = ClientManager.getInstance(getApplicationContext());
        initSpinner();
    }

    private void initSpinner(){
        String[] clients = new String[]{"61780001", "61780002"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, clients);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        client_picker.setAdapter(adapter);
        client_picker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = items[i];
                createDynamicSwitches(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void createDynamicSwitches(String selectedItem){
        String[] strArr = selectedItem.split(":");
        int num = Integer.parseInt(strArr[1]);
        cleanLastLayout();
        for (int i = 0; i < num; i++) {
            View childView = createHorizontalLine(this, i + 1);
            outerLayout.addView(childView);
        }
    }

    private void cleanLastLayout(){
        outerLayout.removeAllViews();
    }

    private View createHorizontalLine(Context context, int index){
        LinearLayout lineLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lineLayout.setLayoutParams(layoutParams);
        lineLayout.setOrientation(LinearLayout.HORIZONTAL);
        lineLayout.setPadding(30, 0, 0 ,0);
        TextView label = new TextView(context);
        label.setText(index + "");
        SwitchCompat oneSwitch = new SwitchCompat(context);
        lineLayout.addView(label);
        lineLayout.addView(oneSwitch);
        return  lineLayout;
    }

    private void dispatchControlMessages(String clientId, byte[] state){
        int size = state.length;
        int length = 16;
        for (int i = 0; i < size; i++) {
            byte[] payloadArr = new byte[length];
            payloadArr[3] = 6;
            char[] charArr = clientId.toCharArray();
            for (int j = 0; j < charArr.length; j++) {
                char value = charArr[j];
                if(value >= '0' && value <= '9') {
                    payloadArr[4 + j] = (byte)(value - 0x30);
                }else{
                    payloadArr[4 + j] = (byte)Integer.parseInt(value + "", 16);
                }
            }
            payloadArr[13] = (byte)(i + 1);
            payloadArr[15] = state[i];
            try {
                client.publishSpecialMessage(payloadArr, 1, Constants.TOPIC_BACKWARD_CONTROL);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
