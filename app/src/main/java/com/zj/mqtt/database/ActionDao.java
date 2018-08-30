package com.zj.mqtt.database;

import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/28 上午9:38.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActionDao extends RealmObject {

    /**
     * 对应的设备
     */
    private String deviceMac;
    /**
     * 对应的操作
     */
    private String cmdJson;
    /**
     *
     */
    private String deviceName;

}
