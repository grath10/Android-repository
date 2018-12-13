package com.example.dell.app;

import android.content.Context;

public class ClientManager {
    private static SingletonMqttClient client;

    public static SingletonMqttClient getInstance(Context context){
        if(client == null){
            client = new SingletonMqttClient(context);
        }
        return client;
    }

    private ClientManager(){

    }
}
