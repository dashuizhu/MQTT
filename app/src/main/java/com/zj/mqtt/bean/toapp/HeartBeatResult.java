package com.zj.mqtt.bean.toapp;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/29 下午2:41.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class HeartBeatResult extends CmdResult {

    /**
     * networkUp : <bool>
     * networkPanId : 1
     * radioTxPower : 1
     * radioChannel : 1
     * ncpStackVersion : string
     */

    private String networkUp;
    private int networkPanId;
    private int radioTxPower;
    private int radioChannel;
    private String ncpStackVersion;

}
