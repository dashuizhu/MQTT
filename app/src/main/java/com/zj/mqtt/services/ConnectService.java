package com.zj.mqtt.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.hwangjr.rxbus.RxBus;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.protocol.CmdParse;
import com.zj.mqtt.utils.LogUtils;
import com.zj.mqtt.utils.sharedPresenter.SharedPreApp;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author zhuj 2018/8/29 下午3:15.
 */
public class ConnectService extends Service {

    private final static String TAG = "MQTT_service";

    private static String TOPIC_PUBLISH = "gw/000D6FFFFE02C0F2/todev";
    private static String TOPIC_SUBSCRIBE = "gw/000D6FFFFE02C0F2/toapp";
    private static String CLIENT_ID = "mqtt_";
    private String serverUrl;

    private final IBinder mBinder = new LocalBinder();

    private MqttAndroidClient mMqttClient;

    private boolean isLinking = false;

    Disposable mLinkingDisposable;

    public void resetServer() {
        String server = SharedPreApp.getInstance().getServerMac();
        TOPIC_PUBLISH = String.format("gw/%1$s/todev", server);
        TOPIC_SUBSCRIBE = String.format("gw/%1$s/toapp", server);
        serverUrl = SharedPreApp.getInstance().getServerURL();
        stopConnect();
        try {
            connectMQTT();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String server = SharedPreApp.getInstance().getServerMac();
        TOPIC_PUBLISH = String.format("gw/%1$s/todev", server);
        TOPIC_SUBSCRIBE = String.format("gw/%1$s/toapp", server);
        serverUrl = SharedPreApp.getInstance().getServerURL();
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
        boolean isLink = false;
        try {
            isLink = mMqttClient.isConnected();
        } catch (IllegalArgumentException e) {
            stopConnect();
            try {
                connectMQTT();
            } catch (MqttException e1) {
                e1.printStackTrace();
            }
        }
        return isLink;
    }

    public boolean isConnecting() {
        if (mMqttClient == null) {
            return false;
        }
        return isLinking;
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
        Log.d(TAG, "开始连接1 " + mMqttClient);
        if (mMqttClient == null) {
            CLIENT_ID = "mqtt_" + System.currentTimeMillis();
            //连接时使用的clientId
            mMqttClient = new MqttAndroidClient(this, serverUrl, CLIENT_ID);
        }
        Log.d(TAG, "开始连接2 " + isLinking);
        if (isLinking) {
            LogUtils.d(TAG, " 是否连接 delay " + isLinking);
            startLinkTimeoutCheck();
            return;
        }
        if (mMqttClient.isConnected()) {
            RxBus.get().post(RxBusString.LINK_SUCCESS);
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
        options.setMaxInflight(20);
        //进行连接  有多个重载方法  看需求选择
        RxBus.get().post(RxBusString.LINKING);
        isLinking = true;
        mMqttClient.connect(options, null, new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.w(TAG, " 连接成功  ");
                RxBus.get().post(RxBusString.LINK_SUCCESS);
                disLinkTimeoutCheck();
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
                disLinkTimeoutCheck();
                RxBus.get().post(RxBusString.LINKFAIL);

                //if ("已连接客户机".equals(exception.getMessage())) {
                try {
                    stopConnect();
                    if (exception != null) {
                        exception.printStackTrace();
                        String str2 = exception.getMessage();
                        String str = exception.getCause().getMessage();
                        Log.e(TAG, "onFailure  " + str2);
                        Log.e(TAG, "onFailure  " + str);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
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
            public void messageArrived(String topic, MqttMessage message) {
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
            Log.w(TAG, " send " + msg);

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

    /**
     * 模拟测试使用
     */
    public void testPublishMsgToApp(final String msg) {
        try {
            Log.w(TAG, " isConnect " + mMqttClient.isConnected());

            mMqttClient.publish(TOPIC_SUBSCRIBE, msg.getBytes(), 2, false, null,
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

    /**
     * 停止连接
     */
    public void stopConnect() {
        Log.w("test", " stopConnect ");
        if (mMqttClient != null) {
            mMqttClient.close();
            mMqttClient.unregisterResources();
            mMqttClient = null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopConnect();
        return super.onUnbind(intent);
    }

    private void startLinkTimeoutCheck() {
        if (mLinkingDisposable == null || mLinkingDisposable.isDisposed()) {
            mLinkingDisposable =
                    Observable.timer(15, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            if (isLinking) {
                                if (mMqttClient.isConnected()) {
                                    RxBus.get().post(RxBusString.LINK_SUCCESS);
                                } else {
                                    RxBus.get().post(RxBusString.LINKFAIL);
                                }
                                isLinking = false;
                            }
                        }
                    });
        }
    }

    private void disLinkTimeoutCheck() {
        if (mLinkingDisposable != null) {
            if (!mLinkingDisposable.isDisposed()) {
                mLinkingDisposable.dispose();
            }
            mLinkingDisposable = null;
        }
    }
}
