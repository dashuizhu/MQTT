package com.zj.mqtt.bean.toapp;

import com.google.gson.annotations.SerializedName;

/**
 * @author zhuj 2018/8/29 下午4:54.
 */
public class CmdReadNodeResult extends CmdResult{

    /**
     * node-data : {"mac":"string","endpoint":1,"clusterId":1,"attributeId":1,"dataType":1,"data":1}
     */

    @SerializedName("node-data") private NodedataBean nodedata;

    public NodedataBean getNodedata() {
        return nodedata;
    }

    public void setNodedata(NodedataBean nodedata) {
        this.nodedata = nodedata;
    }


}
