package com.zj.mqtt.protocol;

import android.util.Log;
import com.alibaba.fastjson.JSONObject;
import com.hwangjr.rxbus.RxBus;
import com.zj.mqtt.AppApplication;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.toapp.CmdNodeLeftResult;
import com.zj.mqtt.bean.toapp.CmdNodeListResult;
import com.zj.mqtt.bean.toapp.CmdNodeResult;
import com.zj.mqtt.bean.toapp.CmdReadNodeResult;
import com.zj.mqtt.bean.toapp.CmdResult;
import com.zj.mqtt.bean.toapp.CmdStateChagneResult;
import com.zj.mqtt.bean.toapp.CmdZclAttributeResult;
import com.zj.mqtt.bean.toapp.CmdZclCmdResult;
import com.zj.mqtt.bean.toapp.HeartBeatResult;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.constant.CmdString;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.database.DeviceDao;
import java.util.List;

/**
 * @author zhuj 2018/8/29 上午11:43.
 */
public class CmdParse {

    private final static String TAG = CmdParse.class.getSimpleName();

    /**
     * 读取属性
     */
    public final static String CMD_READ_NODE = "read-node";
    /**
     * 节点列表
     */
    public final static String CMD_NODE_LIST = "node_update";
    /**
     * 设备加入
     */
    public final static String CMD_DEVICE_JOIN = "devicejoined";
    /**
     * 节点状态改变
     */
    public final static String CMD_NODE_STATE = "node_state";
    /**
     * 节点设备离开
     */
    public final static String CMD_DEVICE_LEFT = "deviceleft";
    /**
     * ZCL数据上报，COMMAND
     */
    public final static String CMD_ZCL_CMD = "zcl-command";
    /**
     * ZCL数据，属性
     */
    public final static String CMD_ZCL_ATTRIBUTE = "zcl-attribute";
    /**
     * 心跳包
     */
    public final static String CMD_HEART_BEAT = "heartbeat";

    public static CmdResult parseMsg(String msg) {
        Log.w(TAG, "解析 " + msg);
        try {
            JSONObject json = JSONObject.parseObject(msg);
            if (!json.containsKey("cmd")) {
                return null;
            }
            String cmd = json.getString("cmd");
            CmdResult result = null;
            switch (cmd) {
                case CMD_READ_NODE:
                    result = JSONObject.parseObject(msg, CmdReadNodeResult.class);
                    break;
                case CMD_NODE_LIST:
                    result = JSONObject.parseObject(msg, CmdNodeListResult.class);
                    List<DeviceBean> deviceList = ((CmdNodeListResult) result).castDeviceList();
                    AppApplication.getApp().updateDeviceList(deviceList);
                    DeviceDao.saveOrUpdate(deviceList);

                    break;
                case CMD_DEVICE_JOIN:
                    result = JSONObject.parseObject(msg, CmdNodeResult.class);
                    //重新读取节点列表
                    AppApplication.getApp().publishMsgToServer(CmdPackage.getNodeList());
                    break;
                case CMD_NODE_STATE:
                    result = JSONObject.parseObject(msg, CmdStateChagneResult.class);
                    CmdStateChagneResult.StatechangeBean stateBean =
                            ((CmdStateChagneResult) result).getStatechange();
                    AppApplication.getApp().updateDevice(stateBean);
                    break;
                case CMD_DEVICE_LEFT:
                    result = JSONObject.parseObject(msg, CmdNodeLeftResult.class);
                    CmdNodeLeftResult.DeviceLeftBean leftBean =
                            ((CmdNodeLeftResult) result).getDeviceleft();
                    AppApplication.getApp().updateDevice(leftBean);
                    break;
                case CMD_ZCL_CMD:
                    result = JSONObject.parseObject(msg, CmdZclCmdResult.class);
                    break;
                case CMD_ZCL_ATTRIBUTE:
                    result = JSONObject.parseObject(msg, CmdZclAttributeResult.class);
                    break;
                case CMD_HEART_BEAT:
                    result = JSONObject.parseObject(msg, HeartBeatResult.class);
                default:
                    result = JSONObject.parseObject(msg, CmdResult.class);
                    parseNormal(result);
                    break;
            }
            Log.w(TAG, "解析success " + result.getCmd());
            RxBus.get().post(RxBusString.RXBUS_PARSE, result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void parseNormal(CmdResult result) {
        CmdControlBean bean = AppApplication.getApp().getControlCacheBean(result.getSeq());
        //收到回复
        if (bean != null) {
            switch (bean.getCmd()) {
                case CmdString.DEV_ONOFF:
                    DeviceBean device = AppApplication.getApp().getDevice(bean.getNode().getMac());
                    device.setControlOnOff(bean.getNode().getValue() == 1);
                    break;
                default:
            }
        }
        RxBus.get().post(result.getCmd());
    }
}
