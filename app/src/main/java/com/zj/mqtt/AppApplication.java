package com.zj.mqtt;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.person.commonlib.utils.DensityHelp;
import com.person.commonlib.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zj.mqtt.bean.ScenesBean;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.toapp.CmdNodeLeftResult;
import com.zj.mqtt.bean.toapp.CmdStateChagneResult;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.database.DeviceDao;
import com.zj.mqtt.database.MqttRealm;
import com.zj.mqtt.database.ScenesDao;
import com.zj.mqtt.services.ConnectService;
import io.realm.Realm;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

        getDevcieList();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    public List<ScenesBean> getScenesList() {
        List<ScenesBean> list = ScenesDao.queryList();
        if (list == null || list.size() == 0) {
            ScenesBean bean1 = new ScenesBean();
            bean1.setName("回家");
            bean1.setPicture(ScenesBean.SCENES_HOME);

            ScenesBean bean4 = new ScenesBean();
            bean4.setName("阅读");
            bean4.setPicture(ScenesBean.SCENES_READ);

            ScenesBean bean2 = new ScenesBean();
            bean2.setName("睡眠");
            bean2.setPicture(ScenesBean.SCENES_SLEEP);


            ScenesBean bean3 = new ScenesBean();
            bean3.setName("离家");
            bean3.setPicture(ScenesBean.SCENES_OUT);

            list.add(bean1);
            list.add(bean2);
            list.add(bean3);
            list.add(bean4);

            ScenesDao.saveOrUpdate(list);
        }
        return list;
    }

    public List<ScenesBean> getScenesListShow() {
        List<ScenesBean> array = getScenesList();
        int subSize = Math.min(4, array.size());
        return array.subList(0, subSize);
    }

    public List<DeviceBean> getDevcieList() {
        if (mDeviceList == null) {
            mDeviceList = DeviceDao.queryList();
        }
        return mDeviceList;
    }

    //public List<DeviceBean> getDeviceListShow() {
    //    mDeviceList = getDevcieList();
    //
    //    int subSize = Math.min(8, mDeviceList.size());
    //    List<DeviceBean> array = mDeviceList.subList(0, subSize);
    //
    //    //DeviceBean moreDevice = new DeviceBean();
    //    //moreDevice.setMoreDevice(true);
    //    //moreDevice.setName("更多");
    //    //array.add(moreDevice);
    //    return array;
    //}

    public void updateDeviceList(List<DeviceBean> list) {
        if (mDeviceList == null || mDeviceList.size() == 0) {
            mDeviceList = list;
            return;
        }
        Set<String> updateSet = new HashSet<>();
        for (DeviceBean bean : list) {

            DeviceBean nowBean = getDevice(bean.getDeviceMac());
            Log.d("mqttDebug","application update:" +nowBean.getDeviceMac() + " "+  bean.getInfo());
            if (nowBean != null) {
                nowBean.setEndpointList(bean.getEndpointList());
                nowBean.setDeviceState(bean.getDeviceState());
            }  else {
                mDeviceList.add(bean);
            }
            updateSet.add(nowBean.getDeviceMac());
        }


        for (DeviceBean bean : mDeviceList) {
            //不在节点列表返回的数据， 就直接当做不在线。
            if (!updateSet.contains(bean.getDeviceMac())) {
                bean.setDeviceState(DeviceBean.STATE_LEfT);
            }
        }


    }

    public void updateDevice(CmdNodeLeftResult.DeviceLeftBean leftBean) {
        removeDevice(leftBean.getMac());
    }

    public void removeDevice(String deviceMac) {
        if (mDeviceList == null || mDeviceList.size() == 0) {
            return;
        }
        int listSize = mDeviceList.size();
        for (int i = 0; i < listSize; i++) {
            if (mDeviceList.get(i).getDeviceMac().equals(deviceMac)) {
                mDeviceList.remove(i);
                break;
            }
        }
    }

    public void updateDevice(String mac, String name, String place) {
        if (mDeviceList == null || mDeviceList.size() == 0) {
            return;
        }
        int listSize = mDeviceList.size();
        for (int i = 0; i < listSize; i++) {
            if (mDeviceList.get(i).getDeviceMac().equals(mac)) {
                mDeviceList.get(i).setName(name);
                mDeviceList.get(i).setPlace(place);
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
            if (mDeviceList.get(i).getDeviceMac().equals(stateBean.getMac())) {
                mDeviceList.get(i).setDeviceState(stateBean.getDeviceState());
                break;
            }
        }
    }

    //public void addDevice(DeviceBean deviceBean) {
    //    if (mDeviceList == null) {
    //        mDeviceList = new ArrayList<>();
    //    }
    //    mDeviceList.add(deviceBean);
    //}

    public DeviceBean getDevice(String mac) {
        if (mDeviceList == null || mDeviceList.size() == 0) {
            return null;
        }
        int listSize = mDeviceList.size();
        for (int i = 0; i < listSize; i++) {
            if (mac.equals(mDeviceList.get(i).getDeviceMac())) {
                return mDeviceList.get(i);
            }
        }
        DeviceBean db = DeviceDao.getDeviceByMac(mac);
        if (db != null) {
            mDeviceList.add(db);
        }
        return db;
    }

    //public DeviceBean getDevice(String mac, int endPoint) {
    //    if (mDeviceList == null || mDeviceList.size() == 0) {
    //        return null;
    //    }
    //    int listSize = mDeviceList.size();
    //    for (int i = 0; i < listSize; i++) {
    //        if (mac.equals(mDeviceList.get(i).getDeviceMac()) && endPoint == mDeviceList.get(i).getDeviceEndpoint().getEndpoint()) {
    //            return mDeviceList.get(i);
    //        }
    //    }
    //    DeviceBean db = DeviceDao.getDeviceByMac(mac);
    //    if (db != null) {
    //        mDeviceList.add(db);
    //    }
    //    return db;
    //}

    public static AppApplication getApp() {
        return mApp;
    }

    /**
     * 发送数据
     */
    public boolean publishMsgToServer(CmdControlBean controlBean) {
        if (mConnectService == null || !mConnectService.isConnect()) {
            ToastUtils.showToast(this, R.string.label_unlink);
            return false;
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
        return true;
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
            if (mServiceConnection != null) {
                unbindService(mServiceConnection);
            }
            bindService();
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
     */
    public boolean isConnect() {
        if (mConnectService == null) {
            return false;
        }
        return mConnectService.isConnect();
    }

    public boolean isConnecting() {
        if (mConnectService == null) {
            return false;
        }
        return mConnectService.isConnecting();
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

    @Override
    public void onTerminate() {
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
        }
        super.onTerminate();
    }
}
