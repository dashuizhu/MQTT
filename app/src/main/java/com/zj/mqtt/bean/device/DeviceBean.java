package com.zj.mqtt.bean.device;

import lombok.Data;

/**
 * @author zhuj 2018/8/27 下午2:26.
 */
@Data
public class DeviceBean {

    private String name;

    /**
     * nodeId : 1
     * deviceState : 1
     * deviceType : 1
     * deviceEndpoint : {"mac":"1","endpoint":1,"clusterInfo":[{"clusterId":1,"clusterType":"1"}]}
     */

    private int nodeId;
    private int deviceState;
    private String deviceType;
    private DeviceEndpointBean deviceEndpoint;
}


