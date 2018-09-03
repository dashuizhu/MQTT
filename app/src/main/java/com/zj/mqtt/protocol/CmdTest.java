package com.zj.mqtt.protocol;

import com.zj.mqtt.AppApplication;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.bean.todev.HuaSetBean;
import com.zj.mqtt.bean.todev.LevelBean;
import com.zj.mqtt.bean.todev.ReportParaBean;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试用的类
 * @author zhuj 2018/8/31 上午11:41.
 */
public class CmdTest {

    public static void testSendCmd(String mac, int endPoint) {
        // TODO: 2018/8/31 测试发送的协议
        List<CmdControlBean> list = new ArrayList<>();

        list.add(CmdPackage.getNodeList());
        list.add(CmdPackage.getFormatNetwork());
        list.add(CmdPackage.getFormatNetwork(1, 2, "panid"));
        list.add(CmdPackage.addDevice());
        list.add(CmdPackage.addDevice(mac, "key"));
        list.add(CmdPackage.stopDevice());
        list.add(CmdPackage.setOnOff(false, mac, endPoint));

        LevelBean levelBean = new LevelBean();
        levelBean.setLevel(21);
        levelBean.setTime(22);
        list.add(CmdPackage.setLevelControl(mac, endPoint, levelBean));


        HuaSetBean huaSetBean = new HuaSetBean();
        huaSetBean.setHua(11);
        huaSetBean.setSat(12);
        huaSetBean.setTime(13);
        list.add(CmdPackage.setColorControl(mac, endPoint,
                huaSetBean));

        list.add(CmdPackage.getReadNode(mac, endPoint,
                11,
                1111));

        ReportParaBean reportParaBean = new ReportParaBean();
        reportParaBean.setAttributeId(31);
        reportParaBean.setClusterId(32);
        reportParaBean.setDataType(33);
        reportParaBean.setMaxReportTime(34);
        reportParaBean.setMinReportTime(35);
        reportParaBean.setReportableChange(36);
        list.add(CmdPackage.setReportPara(mac, endPoint, reportParaBean));

        list.add(CmdPackage.setUpdate());

        for (int i=0; i<list.size(); i++) {
            AppApplication.getApp().publishMsgToServer(list.get(i));
        }

    }


    public static void testParse() {
        // TODO: 2018/8/31 测试发送的协议
        List<String> list = new ArrayList<>();

        list.add("{"
                + "\"cmd\": \"heartbeat\","
                + "\"seq\": 1,"
                + "\"networkUp\": false,"
                + "\"networkPanId\": 1,"
                + "\"radioTxPower\": 1,"
                + "\"radioChannel\": 1,"
                + "\"ncpStackVersion\": \"string\""
                + "}");

        list.add("{"
                + "\"cmd\": \"node_update\","
                + "\"seq\": 1,"
                + "\"device\": [{"
                + "\"nodeId\": 1,"
                + "\"deviceState\": 1,"
                + "\"deviceType\": \"string\","
                + "\"deviceEndpoint\": {"
                + "\"mac\": \"string\","
                + "\"endpoint\": 1,"
                + "\"clusterInfo\": [{"
                + "\"clusterId\": 1,"
                + "\"clusterType\": \"string\""
                + "}]"
                + "}"
                + "},"
                + "{"
                + "\"nodeId\": 2,"
                + "\"deviceState\": 2,"
                + "\"deviceType\": \"string\","
                + "\"deviceEndpoint\": {"
                + "\"mac\": \"device2\","
                + "\"endpoint\": 1,"
                + "\"clusterInfo\": [{"
                + "\"clusterId\": 1,"
                + "\"clusterType\": \"string\""
                + "}]"
                + "}"
                + "}"
                + "]"
                + "}");


        list.add("{"
                + "\"cmd\": \"node_state\","
                + "\"seq\": 1,"
                + "\"statechange\": {"
                + "\"mac\": \"string\","
                + "\"deviceState\": 1"
                + "}"
                + "}");

        list.add("{"
                + "    \"cmd\": \"devicejoined\","
                + "    \"seq\": 1,"
                + "    \"device\": {"
                + "        \"nodeId\": 1,"
                + "        \"deviceState\": 1,"
                + "        \"deviceType\": \"string\","
                + "        \"deviceEndpoint\": {"
                + "            \"mac\": \"string\","
                + "            \"endpoint\": 1,"
                + "            \"clusterInfo\": ["
                + "                {"
                + "                    \"clusterId\": 1,"
                + "                    \"clusterType\": \"string\""
                + "                }"
                + "            ]"
                + "        }"
                + "    }"
                + "}");


        list.add("{"
                + "    \"cmd\": \"deviceleft\","
                + "    \"seq\": 1,"
                + "    \"deviceleft\": {"
                + "        \"mac\": \"string\""
                + "    }"
                + "}");


        list.add("{"
                + "\"cmd\": \"zcl-command\","
                + "    \"seq\": 1,"
                + "    \"clusterId\": 1,"
                + "    \"commandId\": 1,"
                + "    \"commandData\": 1,"
                + "    \"clusterSpecific\": false,"
                + "    \"node\": {"
                + "        \"mac\": \"string\","
                + "        \"endpoint\": 1"
                + "    }"
                + "}");


        list.add("{"
                + "    \"cmd\": \"zcl-attribute\","
                + "    \"clusterId\": 1,"
                + "    \"attributeId\": 1,"
                + "    \"attributeBuffer\": 1,"
                + "    \"attributeDataType\": 1,"
                + "    \"node\": {"
                + "        \"mac\": \"string\","
                + "        \"endpoint\": 1"
                + "    }"
                + "}");

        list.add("{\n"
                + "    \"cmd\": \"read-node\",\n"
                + "    \"seq\": 1,\n"
                + "    \"node-data\": {\n"
                + "        \"mac\": \"string\",\n"
                + "        \"endpoint\": 1,\n"
                + "        \"clusterId\": 1,\n"
                + "        \"attributeId\": 1,\n"
                + "        \"dataType\": 1,\n"
                + "        \"data\": 1\n"
                + "    },\n"
                + "    \"result\": 1\n"
                + "}");

        for (String str: list) {
            AppApplication.getApp().testPublishToApp(str);
        }
    }

}
