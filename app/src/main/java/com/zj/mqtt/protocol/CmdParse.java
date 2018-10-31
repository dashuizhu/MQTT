package com.zj.mqtt.protocol;

import com.google.gson.Gson;
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
import com.zj.mqtt.utils.LogUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;

/**
 * @author zhuj 2018/8/29 上午11:43.
 */
public class CmdParse {

    private final static String TAG = CmdParse.class.getSimpleName();

    static Gson sGson;
    /**
     * 读取属性
     */
    public final static String CMD_READ_NODE = "read_node";
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
    public final static String CMD_ZCL_CMD = "zcl_command";
    /**
     * ZCL数据，属性
     */
    public final static String CMD_ZCL_ATTRIBUTE = "zcl_attribute";
    /**
     * 心跳包
     */
    public final static String CMD_HEART_BEAT = "heartbeat";

    public static CmdResult parseMsg(String msg) {
        LogUtils.logW(TAG, "解析 " + msg);
        try {
            if (sGson == null) {
                sGson = new Gson();
            }
            JSONObject json = new JSONObject(msg);
            if (!json.has("cmd")) {
                return null;
            }
            String cmd = json.getString("cmd");
            CmdResult result = null;
            switch (cmd) {
                case CMD_READ_NODE:
                    result = sGson.fromJson(msg, CmdReadNodeResult.class);
                    break;
                case CMD_NODE_LIST:
                    result = sGson.fromJson(msg, CmdNodeListResult.class);
                    List<DeviceBean> deviceList = ((CmdNodeListResult) result).castDeviceList();

                    AppApplication.getApp().updateDeviceList(deviceList);
                    try {
                        DeviceDao.saveOrUpdate(deviceList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case CMD_DEVICE_JOIN:
                    result = sGson.fromJson(msg, CmdNodeResult.class);
                    delayUpdateList();
                    break;
                case CMD_NODE_STATE:
                    result = sGson.fromJson(msg, CmdStateChagneResult.class);
                    CmdStateChagneResult.StatechangeBean stateBean =
                            ((CmdStateChagneResult) result).getStatechange();
                    AppApplication.getApp().updateDevice(stateBean);
                    break;
                case CMD_DEVICE_LEFT:
                    result = sGson.fromJson(msg, CmdNodeLeftResult.class);
                    CmdNodeLeftResult.DeviceLeftBean leftBean =
                            ((CmdNodeLeftResult) result).getDeviceleft();
                    AppApplication.getApp().updateDevice(leftBean);
                    break;
                case CMD_ZCL_CMD://无作用，暂时废弃
                    //result = sGson.fromJson(msg, CmdZclCmdResult.class);
                    break;
                case CMD_ZCL_ATTRIBUTE:
                    result = sGson.fromJson(msg, CmdZclAttributeResult.class);
                    break;
                case CMD_HEART_BEAT:
                    result = sGson.fromJson(msg, HeartBeatResult.class);
                    return result;
                default:
                    result = sGson.fromJson(msg, CmdResult.class);
                    parseNormal(result);
                    break;
            }
            if (result != null) {

                //Log.w(TAG, "解析success1 " + msg);
                //Log.w(TAG, "解析success " + sGson.toJson(result));
                RxBus.get().post(RxBusString.RXBUS_PARSE, result);
            }
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

    static Disposable mDelay;

    private static void delayUpdateList() {
        if (mDelay != null) {
            if (!mDelay.isDisposed()) {
                mDelay.dispose();
            }
            mDelay = null;
        }
        mDelay = Observable.timer(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        //重新读取节点列表
                        AppApplication.getApp().publishMsgToServer(CmdPackage.getNodeList());
                    }
                });
    }
}
