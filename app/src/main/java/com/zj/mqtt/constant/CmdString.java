package com.zj.mqtt.constant;

/**
 * @author zhuj 2018/8/30 上午10:04.
 */
public class CmdString {

    /**
     * 获取节点列表
     */
    public final static String DEV_GET_NODE_LIST = "get_node_list";
    /**
     * 格式化网络
     */
    public final static String DEV_FROM = "from";
    /**
     * 允许加网
     */
    public final static String DEV_OPEN_NETWORK = "open-network";
    /**
     * 允许加网
     */
    public final static String DEV_OPEN_NETWORK_KEYS = "open-network-keys";
    /**
     * 停止加网
     */
    public final static String DEV_CLOSE_NETWORK= "close-network";
    /**
     * 开关
     */
    public final static String DEV_ONOFF = "onoff";
    /**
     * 调光控制
     */
    public final static String DEV_LEVEL_CONTROL = "level-control";
    /**
     * 颜色控制
     */
    public final static String DEV_COLOR_CONTROL = "color-control";
    /**
     * 读取节点
     */
    public final static String DEV_READ_NODE = "read-node";
    /**
     * 节点数据上报
     */
    public final static String DEV_NODE_REPORT = "send-me-a-report";
    /**
     * OTA升级
     */
    public final static String DEV_UPDATE = "fw_upgrade";
}
