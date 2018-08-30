package com.zj.mqtt.bean.toapp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/29 下午2:10.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmdZclAttributeResult extends CmdResult {

    /**
     * clusterId : 1
     * attributeId : 1
     * attributeBuffer : 1
     * attributeDataType : 1
     * node : {"mac":"string","endpoint":1}
     */

    private int clusterId;
    private int attributeId;
    private int attributeBuffer;
    private int attributeDataType;
    private NodeBean node;


}
