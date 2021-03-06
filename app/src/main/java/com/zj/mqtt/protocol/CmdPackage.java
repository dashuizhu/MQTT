package com.zj.mqtt.protocol;

import com.zj.mqtt.bean.toapp.NodeBean;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.bean.todev.CmdNetworkParaBean;
import com.zj.mqtt.bean.todev.HuaSetBean;
import com.zj.mqtt.bean.todev.LevelBean;
import com.zj.mqtt.bean.todev.OtaBean;
import com.zj.mqtt.bean.todev.ReportParaBean;
import com.zj.mqtt.constant.CmdString;

/**
 * @author zhuj 2018/8/27 下午2:07.
 */
public class CmdPackage {

    /**
     * 获取节点列表
     */
    public static CmdControlBean getNodeList() {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_GET_NODE_LIST);
        return bean;
    }

    /**
     * 格式化网络
     */
    public static CmdControlBean getFormatNetwork() {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_FROM);
        return bean;
    }

    /**
     * 格式化网络
     */
    public static CmdControlBean getFormatNetwork(int channel, int power, String panId) {
        CmdNetworkParaBean networkBean = new CmdNetworkParaBean();
        networkBean.setChannel(channel);
        networkBean.setPower(power);
        networkBean.setPanId(panId);

        CmdControlBean bean = new CmdControlBean(CmdString.DEV_FROM);
        bean.setNetwork_para(networkBean);

        return bean;
    }

    /**
     * 允许加网
     */
    public static CmdControlBean addDevice() {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_OPEN_NETWORK);
        return bean;
    }

    /**
     * 允许加网
     */
    public static CmdControlBean addDevice(String mac, String key) {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_OPEN_NETWORK_KEYS);

        NodeBean nodeBean = new NodeBean();
        nodeBean.setMac(mac);
        nodeBean.setKey(key);

        bean.setNode(nodeBean);
        return bean;
    }

    /**
     * 停止加网
     */
    public static CmdControlBean stopDevice() {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_CLOSE_NETWORK);
        return bean;
    }

    /**
     * 开关控制
     */
    public static CmdControlBean setOnOff(boolean onOff, String mac, int endpoint) {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_ONOFF);

        NodeBean nodeBean = new NodeBean();
        nodeBean.setMac(mac);
        nodeBean.setEndpoint(endpoint);
        nodeBean.setValue(onOff ? 1 : 0);
        bean.setNode(nodeBean);

        return bean;
    }

    /**
     * 节点属性上报
     */
    public static CmdControlBean setReportPara(String mac, int endpoint, ReportParaBean paraBean) {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_NODE_REPORT);

        NodeBean nodeBean = new NodeBean();
        nodeBean.setMac(mac);
        nodeBean.setEndpoint(endpoint);
        bean.setNode(nodeBean);

        bean.setPara(paraBean);
        return bean;
    }

    /**
     * 读取节点数据
     */
    public static CmdControlBean getReadNode(String mac, int endpoint, int clusterId,
            int attributeId) {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_READ_NODE);

        NodeBean nodeBean = new NodeBean();
        nodeBean.setMac(mac);
        nodeBean.setEndpoint(endpoint);
        nodeBean.setClusterId(clusterId);
        nodeBean.setAttributeId(attributeId);

        bean.setNode(nodeBean);
        return bean;
    }

    /**
     * 设置颜色控制
     */
    public static CmdControlBean setColorControl(String mac, int endpoint, HuaSetBean huaSetBean) {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_COLOR_CONTROL);

        NodeBean nodeBean = new NodeBean();
        nodeBean.setMac(mac);
        nodeBean.setEndpoint(endpoint);

        bean.setNode(nodeBean);
        bean.setMovetohueandsat(huaSetBean);
        return bean;
    }

    /**
     * 调光控制
     */
    public static CmdControlBean setLevelControl(String mac, int endpoint, LevelBean levelBean) {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_LEVEL_CONTROL);

        NodeBean nodeBean = new NodeBean();
        nodeBean.setMac(mac);
        nodeBean.setEndpoint(endpoint);

        bean.setNode(nodeBean);
        bean.setMv_to_level(levelBean);
        return bean;
    }

    public static CmdControlBean setBindControl(int nodeId, int clusterId, int srcEndPoint, int desEndPoint,
            String srcMac, String desMac) {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_BIND);

        bean.setNodeId(nodeId);
        bean.setClusterId(clusterId);
        bean.setSrc_endpoint(srcEndPoint);
        bean.setDes_endpoint(desEndPoint);
        bean.setSrc_mac(srcMac);
        bean.setDes_mac(desMac);
        return bean;
    }

    /**
     * 软件升级
     */
    public static CmdControlBean setUpdate() {
        CmdControlBean bean = new CmdControlBean(CmdString.DEV_UPDATE);

        OtaBean info = new OtaBean();
        bean.setOta_info(info);

        return bean;
    }


}
