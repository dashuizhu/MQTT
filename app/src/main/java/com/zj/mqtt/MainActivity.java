package com.zj.mqtt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.android.service.MqttService;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private String mTopic="home/garden/fountain/test", mQos = "0";

    private String serverUrl = "tcp://m2m.eclipse.org:1883";
    private String clientId = "a850006dbd6f441ba21b2d4021330856";

    //private String clientId = "MQTT_FX_Client";
    //private String serverUrl = "tcp://192.168.0.108:1883";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            connectMQTT1();
        } catch (MqttException e) {
            e.printStackTrace();
        }


        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishMsgToServer("我发送的消息");
            }
        });
    }

    //声明一个MQTT客户端对象
    private MqttAndroidClient mMqttClient;

    //连接到服务器

    private void connectMQTT1() throws MqttException {

        //连接时使用的clientId

        mMqttClient = new MqttAndroidClient(this, serverUrl, clientId);
        //设置连接参数
        MqttConnectOptions options;

        options = new MqttConnectOptions();
        // 清除缓存
        options.setCleanSession(true);
        // 设置超时时间，单位：秒
        options.setConnectionTimeout(15);
        // 心跳包发送间隔，单位：秒
        options.setKeepAliveInterval(15);
        // 用户名
        //options.setUserName("admin");
        //// 密码
        //options.setPassword("admin".toCharArray());
        // 设置MQTT监听
        //mMqttClient.setCallback(new MqttCallback() {
        //    @Override
        //    public void connectionLost(Throwable cause) {
        //        //这里是做断开重连操作的  看个人需求
        //        //mHandler.sendEmptyMessageDelayed(MSG_DELAY_CONNECT_MQTT, 10000);
        //    }
        //
        //    @Override
        //    public void messageArrived(String topic, MqttMessage message) throws Exception {
        //
        //    }
        //
        //    @Override
        //    public void deliveryComplete(IMqttDeliveryToken token) {
        //
        //    }
        //});

        try {
            //mMqttClient.connect(options);
            //进行连接  有多个重载方法  看需求选择
            mMqttClient.connect(options, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w(TAG, " 连接成功  ");
                    try {
                        //连接成功后订阅主题, qos 质量， 0 只管发送 ， 1 会重复发送知道确认收到 ，2 保证重复消息不会二次接收
                        mMqttClient.subscribe(mTopic, 0, new IMqttMessageListener() {
                            @Override
                            public void messageArrived(String topic, MqttMessage message)
                                    throws Exception {
                                Log.w(TAG, "messageArrived  " + message.toString());
                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, "onFailure  " + exception.getMessage());
                    //这里是做断开重连操作的  看个人需求
                    //mHandler.sendEmptyMessageDelayed(MSG_DELAY_CONNECT_MQTT, 10000);
                }
            });
            mMqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    //断开连接
                    Log.w(TAG, "connectionLost  ");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    //接收消息
                    Log.w(TAG, "messageArrived  " + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //消息发送
                    Log.w(TAG, "delive ");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void publishMsgToServer(String msg) {
        try {
            mMqttClient.publish(mTopic, msg.getBytes(), 0, false, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w(TAG, "publish onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w(TAG, "publish onFailure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private MqttConnectOptions initMqttConnectionOptions() {

        char[] pass = new char[] { 'p', 'a', 's', 's' };

        MqttConnectOptions mOptions = new MqttConnectOptions();
        mOptions.setAutomaticReconnect(false);//断开后，是否自动连接
        mOptions.setCleanSession(true);//是否清空客户端的连接记录。若为true，则断开后，broker将自动清除该客户端连接信息
        mOptions.setConnectionTimeout(60);//设置超时时间，单位为秒
        mOptions.setUserName("Admin");//设置用户名。跟Client ID不同。用户名可以看做权限等级
        mOptions.setPassword(pass);//设置登录密码
        mOptions.setKeepAliveInterval(60);//心跳时间，单位为秒。即多长时间确认一次Client端是否在线
        mOptions.setMaxInflight(10);//允许同时发送几条消息（未收到broker确认信息）
        mOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);//选择MQTT版本
        return mOptions;
    }
}
