package com.zj.mqtt.bean;

import lombok.Data;

/**
 * @author zhuj 2018/8/27 下午2:11.
 */
@Data
public class CmdBean {

    /**
     * cmd : form
     * seq : 1
     */

    private String cmd;
    private int seq;
    private CmdNetworkParaBean network_para;

    /**
     * 结果参数
     */
    private int result;

    public CmdBean(String cmd, int seq) {
        this.cmd = cmd;
        this.seq = seq;
    }
}
