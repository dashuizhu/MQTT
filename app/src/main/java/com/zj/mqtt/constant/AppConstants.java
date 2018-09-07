package com.zj.mqtt.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuj 2018/8/30 下午1:51.
 */
public class AppConstants {

    public final static String UMENG_APPID = "5b85fb2fb27b0a4a9200004d";

    public final static String OTA_URL = "http://www.xxx.com";

    public static List<String> getDevicePlaceList() {
        List<String> list = new ArrayList<>();
        list.add("主卧");
        list.add("卧室");
        list.add("次卧");
        list.add("书房");
        list.add("客厅");
        list.add("客房");
        list.add("厨房");
        list.add("餐厅");
        return list;
    }

}
