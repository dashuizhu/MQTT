package com.zj.mqtt.database;

import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/9/21 下午7:54.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EndpointDao extends RealmObject {

    private int endPoint;

}
