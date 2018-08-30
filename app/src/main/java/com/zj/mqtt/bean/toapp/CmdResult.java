package com.zj.mqtt.bean.toapp;

import lombok.Data;

/**
 * @author zhuj 2018/8/29 下午2:03.
 */
@Data
public class CmdResult {

    /**
     * cmd : read-node
     * seq : 1
     * result : 1
     */

    private String cmd;
    private int seq;
    private int result;

}
