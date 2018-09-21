package com.zj.mqtt.bean.toapp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/29 下午2:08.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmdZclCmdResult extends CmdResult {

    /**
     * cmd : zcl-command
     * seq : 1
     * clusterId : 1
     * commandId : 1
     * commandData : 1
     * clusterSpecific : false
     * node : {"mac":"string","endpoint":1}
     */

    private String cmd;
    private int seq;
    private int clusterId;
    private int commandId;
    private String commandData;
    private boolean clusterSpecific;
    private NodeBean node;

}
