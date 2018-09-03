package com.zj.mqtt;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.alibaba.fastjson.JSON;
import com.person.commonlib.utils.DensityHelp;
import com.person.commonlib.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zj.mqtt.bean.ScenesBean;
import com.zj.mqtt.bean.toapp.CmdNodeLeftResult;
import com.zj.mqtt.bean.toapp.CmdStateChagneResult;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.device.DeviceEndpointBean;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.constant.AppConstants;
import com.zj.mqtt.database.MqttRealm;
import com.zj.mqtt.database.ScenesDao;
import com.zj.mqtt.services.ConnectService;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * @author zhuj 2018/8/27 下午4:20.
 */
public class AppApplication extends Application {

    private ConnectService mConnectService;

    private static AppApplication mApp;

    private List<DeviceBean> mDeviceList;

    /**
     * 发送数据
     */
    private int seq;
    private List<CmdControlBean> mControlCacheList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        DensityHelp.setDensity(this);
        Realm.init(this);
        MqttRealm.setDefaultRealmForUser("mqtt");

        mApp = this;
        bindService();
        //UMConfigure.setLogEnabled(true);
        //MobclickAgent.setDebugMode(true);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

    }

    public List<ScenesBean> getScenesList() {
        List<ScenesBean> list = ScenesDao.queryList();
        if (list == null || list.size() == 0) {
            ScenesBean bean1 = new ScenesBean();
            bean1.setName("回家");

            ScenesBean bean2 = new ScenesBean();
            bean2.setName("睡眠");

            ScenesBean bean3 = new ScenesBean();
            bean3.setName("离家");

            ScenesBean bean4 = new ScenesBean();
            bean4.setName("阅读");

            list.add(bean1);
            list.add(bean2);
            list.add(bean3);
            list.add(bean4);

            ScenesDao.saveOrUpdate(list);
        }
        return list;
    }

    public List<DeviceBean> getDevcieList() {
        if (mDeviceList == null) {
            mDeviceList = new ArrayList<>();
            DeviceBean bean;
            for (int i = 0; i < 5; i++) {
                bean = new DeviceBean();
                bean.setName("device" + i);

                DeviceEndpointBean endpointBean = new DeviceEndpointBean();
                endpointBean.setMac("mac" + i);

                bean.setDeviceEndpoint(endpointBean);
                mDeviceList.add(bean);
            }
        }
        return mDeviceList;
    }

    public void setDeviceList(List<DeviceBean> list) {
        mDeviceList = list;
    }

    public void updateDevice(CmdNodeLeftResult.DeviceLeftBean leftBean) {
        if (mDeviceList == null || mDeviceList.size() == 0) {
            return;
        }
        int listSize = mDeviceList.size();
        for (int i = 0; i < listSize; i++) {
            if (mDeviceList.get(i).getDeviceEndpoint().getMac().equals(leftBean.getMac())) {
                mDeviceList.remove(i);
                break;
            }
        }
    }

    public void updateDevice(CmdStateChagneResult.StatechangeBean stateBean) {
        if (mDeviceList == null || mDeviceList.size() == 0) {
            return;
        }
        int listSize = mDeviceList.size();
        for (int i = 0; i < listSize; i++) {
            if (mDeviceList.get(i).getDeviceEndpoint().getMac().equals(stateBean.getMac())) {
                mDeviceList.get(i).setDeviceState(stateBean.getDeviceState());
                break;
            }
        }
    }

    public void addDevice(DeviceBean deviceBean) {
        if (mDeviceList == null) {
            mDeviceList = new ArrayList<>();
        }
        mDeviceList.add(deviceBean);
    }

    public DeviceBean getDevice(String mac) {
        if (mDeviceList == null || mDeviceList.size() == 0) {
            return null;
        }
        int listSize = mDeviceList.size();
        for (int i = 0; i < listSize; i++) {
            if (mac.equals(mDeviceList.get(i).getDeviceEndpoint().getMac())) {
                return mDeviceList.get(i);
            }
        }
        return null;
    }

    public static AppApplication getApp() {
        return mApp;
    }

    /**
     * 发送数据
     * @param controlBean
     */
    public void publishMsgToServer(CmdControlBean controlBean) {
        if (mConnectService == null || !mConnectService.isConnect()) {
            ToastUtils.showToast(this, R.string.label_unlink);
            return;
        }
        seq++;
        controlBean.setSeq(seq);
        String cmd = JSON.toJSONString(controlBean);
        mControlCacheList.add(controlBean);

        mConnectService.publishMsgToServer(cmd);

        //及时清理内存
        if (mControlCacheList.size() > 1000) {
            mControlCacheList = mControlCacheList.subList(800, mControlCacheList.size());
        }
    }

    /**
     * 模拟发送数据给自己，
     */
    public void testPublishToApp(String string) {
        if (mConnectService == null || !mConnectService.isConnect()) {
            ToastUtils.showToast(this, R.string.label_unlink);
            return;
        }

        mConnectService.testPublishMsgToApp(string);
    }

    /**
     * 发送数据
     * @param msg
     */
    public void publishMsgToServer(String msg) {
        if (mConnectService == null || !mConnectService.isConnect()) {
            ToastUtils.showToast(this, R.string.label_unlink);
            return;
        }
        mConnectService.publishMsgToServer(msg);
    }

    /**
     * 获取缓存的控制数据
     * @param seq
     * @return
     */
    public CmdControlBean getControlCacheBean(int seq) {
        int listSize = mControlCacheList.size();
        for (int i = listSize - 1; i >= 0; i--) {
            if (mControlCacheList.get(i).getSeq() == seq) {
                return mControlCacheList.get(i);
            }
        }
        return null;
    }

    /**
     * 连接服务器
     */
    public void connectService() {
        if (mConnectService == null) {
            return;
        }
        try {
            mConnectService.connectMQTT();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止连接
     */
    public void stopConnect() {
        if (mConnectService != null) {
            mConnectService.stopConnect();
        }
    }

    /**
     * 是否连接
     * @return
     */
    public boolean isConnect() {
        if (mConnectService == null) {
            return false;
        }
        return mConnectService.isConnect();
    }

    private void bindService() {
        Intent intent = new Intent(this, ConnectService.class);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mConnectService = ((ConnectService.LocalBinder) service).getService();
            try {
                mConnectService.connectMQTT();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mConnectService = null;
        }
    };
}
