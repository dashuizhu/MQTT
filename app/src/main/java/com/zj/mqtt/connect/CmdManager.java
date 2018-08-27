package com.zj.mqtt.connect;

import com.google.gson.Gson;
import com.zj.mqtt.bean.CmdBean;
import com.zj.mqtt.bean.CmdNetworkParaBean;
import org.json.JSONObject;

/**
 * @author zhuj 2018/8/27 下午2:07.
 */
public class CmdManager {

    private static Gson sGson = new Gson();

    /**
     * 获取节点列表
     * @param seq
     * @return
     */
    public static String getNodeList(int seq) {
        CmdBean bean = new CmdBean("get_node_list", seq);
        return sGson.toJson(bean);
    }

    /**
     * 格式化网络
     * @param seq
     * @return
     */
    public static String getFormatNetwork(int seq) {
        CmdBean bean = new CmdBean("form", seq);
        return sGson.toJson(bean);
    }

    /**
     * 格式化网络
     * @param seq
     * @param channel
     * @param power
     * @param panId
     * @return
     */
    public static String getFormatNetwork(int seq, int channel, int power, String panId) {
        CmdBean bean = new CmdBean("form", seq);
        CmdNetworkParaBean networkBean = new CmdNetworkParaBean();
        networkBean.setChannel(channel);
        networkBean.setPower(power);
        networkBean.setPanId(panId);
        bean.setNetwork_para(networkBean);
        return sGson.toJson(bean);
    }

    /**
     * 允许加网
     * @param seq
     * @return
     */
    public static String addDevice(int seq) {
        CmdBean bean = new CmdBean("open-network", seq);
        return sGson.toJson(bean);
    }

    /**
     * 允许加网
     * @param seq
     * @param mac
     * @param key
     * @return
     */
    public static String addDevice(int seq,String mac, String key) {
        CmdBean bean = new CmdBean("open-network-keys", seq);
        return sGson.toJson(bean);
    }

    /**
     * 停止加网
     * @param seq
     * @return
     */
    public static String stopDevice(int seq) {
        CmdBean bean = new CmdBean("close-network", seq);
        return sGson.toJson(bean);
    }



}
