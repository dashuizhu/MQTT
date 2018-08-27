package com.zj.mqtt.bean.device;

import java.util.List;
import lombok.Data;

@Data
public class DeviceEndpointBean {
    /**
     * mac : 1
     * endpoint : 1
     * clusterInfo : [{"clusterId":1,"clusterType":"1"}]
     */

    private String mac;
    private int endpoint;
    private List<ClusterInfoBean> clusterInfo;
}