package com.zj.mqtt.constant;

/**
 * @author zhuj 2018/9/14 上午12:37.
 */
public class AppType {

    /**
     * 开关
     */
    public static final String DEVICE_SWITCH = "0x0000";
    /**
     * 带调光的开光
     */
    public static final String DEVICE_SWITCH_LIGHT = "0x0001";
    /**
     * 灯
     */
    public static final String DEVICE_LIGHT = "0x0100";

    /**
     * 开关
     */
    public static final int CLUSTER_ONOFF = 0x0006;
    /**
     * 温度
     */
    public static final int CLUSTER_TEMPERATURE = 1026;
    /**
     * 湿度
     */
    public static final int CLUSTER_HUMIDITY = 1029;

}