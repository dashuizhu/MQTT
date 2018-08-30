package com.zj.mqtt.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.hwangjr.rxbus.RxBus;
import com.zj.mqtt.AppApplication;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.toapp.CmdResult;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.CmdString;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.protocol.CmdParse;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

/**
 * @author zhuj 2018/8/29 下午3:15.
 */
public class ConnectService extends Service {

    private final static String TAG = "MQTT_service";

    private final static String TOPIC_PUBLISH = "Z3Gateway/mac/todev";
    private final static String TOPIC_SUBSCRIBE = "Z3Gateway/mac/toapp";
    private final static String CLIENT_ID = "MQTT_FX_Client";
    private String serverUrl = "tcp://192.168.0.108:1883";

    private final IBinder mBinder = new LocalBinder();

    private MqttAndroidClient mMqttClient;

    private boolean isLinking = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public boolean isConnect() {
        if (mMqttClient == null) {
            return false;
        }
        return mMqttClient.isConnected();
    }

    public class LocalBinder extends Binder {

        public ConnectService getService() {
            return ConnectService.this;
        }
    }

    /**
     * @throws MqttException
     */
    public void connectMQTT() throws MqttException {
        if (mMqttClient == null) {
            //连接时使用的clientId
            mMqttClient = new MqttAndroidClient(this, serverUrl, CLIENT_ID);
        }
        if (isLinking || mMqttClient.isConnected()) {
            return;
        }
        //设置连接参数
        MqttConnectOptions options;

        options = new MqttConnectOptions();
        // 清除缓存
        options.setCleanSession(true);
        // 设置超时时间，单位：秒
        options.setConnectionTimeout(15);
        // 心跳包发送间隔，单位：秒
        options.setKeepAliveInterval(15);
        //断开重连
        options.setAutomaticReconnect(true);
        // 用户名
        options.setUserName("admin");
        // //密码
        options.setPassword("admin".toCharArray());
        //心跳时间，单位为秒。即多长时间确认一次Client端是否在线
        options.setKeepAliveInterval(60);
        //允许同时发送几条消息（未收到broker确认信息）
        options.setMaxInflight(10);
        //进行连接  有多个重载方法  看需求选择
        RxBus.get().post(RxBusString.LINKING);
        isLinking = true;
        mMqttClient.connect(options, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.w(TAG, " 连接成功  ");
                RxBus.get().post(RxBusString.LINK_SUCCESS);
                isLinking = false;
                try {
                    //连接成功后订阅主题, qos 质量， 0 只管发送 ， 1 会重复发送知道确认收到 ，2 保证重复消息不会二次接收
                    mMqttClient.subscribe(TOPIC_SUBSCRIBE, 2);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.w(TAG, "onFailure  ");
                isLinking = false;
                RxBus.get().post(RxBusString.LINKFAIL);
                if (exception != null) {
                    exception.printStackTrace();
                    String str2 = exception.getMessage();
                    String str = exception.getCause().getMessage();
                    Log.e(TAG, "onFailure  " + str2);
                    Log.e(TAG, "onFailure  " + str);
                    //if ("已连接客户机".equals(exception.getMessage())) {
                        try {
                            mMqttClient.disconnect();
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    //}
                }
            }
        });
        mMqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //断开连接
                Log.w(TAG, "connectionLost  ");
                RxBus.get().post(RxBusString.LINKFAIL);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String receMsg = new String(message.getPayload());
                ////接收消息
                Log.d(TAG, "messageArrived  "
                        + receMsg
                        + " ~"
                        + message.getId()
                        + " ~"
                        + message.getQos());
                CmdParse.parseMsg(receMsg);

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //消息发送
                Log.w(TAG, "delive ");
            }
        });
    }

    public void publishMsgToServer(final String msg) {
        try {
            Log.w(TAG, " isConnect " + mMqttClient.isConnected());

            mMqttClient.publish(TOPIC_PUBLISH, msg.getBytes(), 2, false, null,
                    new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.w(TAG, "publish onSuccess");
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.w(TAG, "publish onFailure ");
                            if (exception != null) {
                                exception.printStackTrace();
                            }
                        }
                    });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //private Map<Integer, CmdControlBean> map = new HashMap<>();
    //public void publishMsgToServer(final String cmd) {
    //    try {
    //        Log.w(TAG, " isConnect " + mMqttClient.isConnected());
    //        mMqttClient.publish(TOPIC_PUBLISH, cmd.getBytes(), 2, false, null,
    //                new IMqttActionListener() {
    //                    @Override
    //                    public void onSuccess(IMqttToken asyncActionToken) {
    //                        Log.w(TAG, "publish onSuccess");
    //                    }
    //
    //                    @Override
    //                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
    //                        Log.w(TAG, "publish onFailure ");
    //                        if (exception != null) {
    //                            exception.printStackTrace();
    //                        }
    //                    }
    //                });
    //    } catch (MqttException e) {
    //        e.printStackTrace();
    //    }
    //}

    /**
     * 停止连接
     */
    public void stopConnect() {
        if (mMqttClient != null) {
            mMqttClient.close();
            mMqttClient.unregisterResources();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopConnect();
        return super.onUnbind(intent);
    }
}
