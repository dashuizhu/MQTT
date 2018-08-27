package com.zj.mqtt.bean;

import lombok.Data;

/**
 * 网络参数
 * @author zhuj 2018/8/27 下午2:17.
 */
@Data
public class CmdNetworkParaBean {
    int channel;
    int power;
    String panId;
}
