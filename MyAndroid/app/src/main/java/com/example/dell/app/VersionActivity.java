package com.example.dell.app;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.app.utils.Constants;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class VersionActivity extends AppCompatActivity {
    private static final String TAG = "VersionActivity";
    private TextView versionInfo;
    private Button version_btn;
    private SingletonMqttClient client;
    private ListenerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        versionInfo = (TextView)findViewById(R.id.version_detail);
        version_btn = (Button)findViewById(R.id.get_version);
        client = ClientManager.getInstance(getApplicationContext());
        client.processMessage(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "connection has been lost for some reason...");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                switch (topic){
                    case "X/VR":
                        getVersion(message.getPayload());
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "Delivery has completed...");
            }
        });
        version_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] payload = new byte[]{5, 0};
                try {
                    task = new ListenerTask();
                    task.execute();
                    client.publishSpecialMessage(payload, 1, Constants.TOPIC_SHORT_CMD);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private class ListenerTask extends AsyncTask<Void, Integer, Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                client.subscribe(Constants.TOPIC_VERSION, 1);
            }catch(Exception e){
                e.printStackTrace();
            }
            return true;
        }
    }

    private void getVersion(byte[] payload){
        StringBuilder sb = new StringBuilder();
        for (int i = 26; i < payload.length; i++) {
            if (payload[i] >= 0 && payload[i] <= 15) {
                sb.append(Integer.toHexString(payload[i]));
            } else {
                char c = (char) payload[i];
                sb.append(c);
            }
        }
        versionInfo.setText(sb);
        Log.d(TAG, sb.toString());
    }
}
