package com.example.dell.app.utils;

public class Constants {
    // backup tcp://iot.eclipse.org:1883
    public static final String MQTT_BROKER_URL = "tcp://192.168.1.100:1883";
    public static final String TOPIC_BACKWARD_CONTROL = "A/CN";  // 反向控制
    public static final String CLIENT_ID = "android";
    public static final String TOPIC_BROADCAST = "A/BC";    // 广播消息
    public static final String TOPIC_DISPATCH_PARAMS = "A/PM";  // 下发参数
    public static final String TOPIC_SHORT_CMD = "A/PA";    // 短命令订阅
    public static final String TOPIC_SUBSCRIBE = "A/+";
    public static final String TOPIC_VERSION = "X/VR";  // 查询版本
}
