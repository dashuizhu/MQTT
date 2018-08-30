package com.zj.mqtt.bean.todev;

import lombok.Data;

/**
 * 节点自动上报属性
 *
 * @author zhuj 2018/8/30 下午2:44.
 */
@Data
public class ReportParaBean {

    /**
     * clusterId : 1029
     * attributeId : 0
     * dataType : 37
     * minReportTime : 1
     * maxReportTime : 1800
     * reportableChange : 500
     */

    private int clusterId;
    private int attributeId;
    private int dataType;
    private int minReportTime;
    private int maxReportTime;
    private int reportableChange;

}
