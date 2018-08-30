package com.zj.mqtt.bean.toapp;

import lombok.Data;

@Data
public class NodeBean {
    /**
     * mac : string
     * endpoint : 1
     */

    private String mac;
    private Integer endpoint;
    /**
     * 开关控制，只有在cmd = onoff
     */
    private Integer value;

    private Integer clusterId;

    private Integer attributeId;

    private String key;
}