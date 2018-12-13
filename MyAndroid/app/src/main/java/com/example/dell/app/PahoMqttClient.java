package com.example.dell.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

import java.io.UnsupportedEncodingException;

public class PahoMqttClient {
    private static final String TAG = "PahoMqttClient";
    private MqttAndroidClient mqttAndroidClient;

    public MqttAndroidClient getMqttClient(Context context, String brokerUrl, String clientId) {
        mqttAndroidClient = new MqttAndroidClient(context, brokerUrl, clientId);
        try{
            IMqttToken token = mqttAndroidClient.connect(getMqttConnectionOption());
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    mqttAndroidClient.setBufferOpts(getDisconnectedBufferOptions());
                    Log.d(TAG, "成功");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "失败" + exception.toString());
                }
            });
        }catch (MqttException e){
            e.printStackTrace();
        }
        return mqttAndroidClient;
    }

    public void disconnect(@NonNull MqttAndroidClient client) throws MqttException{
        IMqttToken mqttToken = client.disconnect();
        mqttToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(TAG, "成功断开连接");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, "未能断开连接" + exception.toString());
            }
        });
    }

    @NonNull
    private DisconnectedBufferOptions getDisconnectedBufferOptions(){
        DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
        disconnectedBufferOptions.setBufferEnabled(true);
        disconnectedBufferOptions.setBufferSize(100);
        disconnectedBufferOptions.setPersistBuffer(false);
        disconnectedBufferOptions.setDeleteOldestMessages(false);
        return disconnectedBufferOptions;
    }

    @NonNull
    private MqttConnectOptions getMqttConnectionOption() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setAutomaticReconnect(true);
        /*mqttConnectOptions.setWill(Constants.PUBLISH_TOPIC, "I am going offline".getBytes(), 1, true);
        mqttConnectOptions.setUserName("nglazy");
        mqttConnectOptions.setPassword("1234".toCharArray());*/
        return mqttConnectOptions;
    }

    public void publishMessage(@NonNull MqttAndroidClient client, @NonNull String msg, int qos, @NonNull String topic)
        throws MqttException, UnsupportedEncodingException{
        byte[] encodedPayload = msg.getBytes("UTF-8");
        MqttMessage message = new MqttMessage(encodedPayload);
        message.setRetained(true);
        message.setQos(qos);
        client.publish(topic, message);
    }

    public void publishSpecialMessage(@NonNull MqttAndroidClient client, @NonNull byte[] payload, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException{
        MqttMessage message = new MqttMessage(payload);
        message.setRetained(true);
        message.setQos(qos);
        client.publish(topic, message);
    }

    public void publishVoiceMessage(@NonNull MqttAndroidClient client, @NonNull String msg, int qos, @NonNull String topic)
            throws MqttException, UnsupportedEncodingException{
        int size = msg.length();
        byte[] payloadArr = new byte[size];
        char[] charArr = msg.toCharArray();
        for (int i = 0; i < size; i++) {
            char value = charArr[i];
            if(value >= '0' && value <= '9') {
                payloadArr[i] = (byte)(value - 0x30);
            }else{
                payloadArr[i] = (byte)Integer.parseInt(value + "", 16);
            }
        }
        MqttMessage message = new MqttMessage(payloadArr);
        message.setRetained(true);
        message.setQos(qos);
        client.publish(topic, message);
    }

    public void subscribe(@NonNull MqttAndroidClient client, @NonNull final String topic, int qos) throws MqttException {
        IMqttToken token = client.subscribe(topic, qos);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(TAG, "订阅成功" + topic);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, "订阅失败" + topic);
            }
        });
    }

    public void unSubscribe(@NonNull MqttAndroidClient client, @NonNull final String topic) throws MqttException{
        IMqttToken token = client.unsubscribe(topic);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.d(TAG, "成功解除订阅" + topic);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, "解除订阅失败" + topic);
            }
        });
    }
}
