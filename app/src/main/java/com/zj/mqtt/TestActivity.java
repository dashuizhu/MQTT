package com.zj.mqtt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.alibaba.fastjson.JSONObject;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.zj.mqtt.bean.toapp.CmdResult;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.ui.BaseActivity;
import com.zj.mqtt.ui.ScenesListActivity;

public class TestActivity extends BaseActivity {

    @BindView(R.id.tv_status) TextView mTvStatus;
    @BindView(R.id.tv_message) TextView mTvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTvMessage = findViewById(R.id.tv_message);

        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApp().publishMsgToServer(" 客户端发送 " + System.currentTimeMillis());
            }
        });
        findViewById(R.id.btn_scenes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, ScenesListActivity.class);
                startActivity(intent);
            }
        });
        registerRxBus();
        getApp().connectService();

        if (getApp().isConnect()) {
            mTvStatus.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        getApp().stopConnect();
        unRegisterRxBus();
        super.onDestroy();
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusString.RXBUS_PARSE))
    public void onReceive(CmdResult cmdResult) {
        mTvMessage.append("\n" + JSONObject.toJSONString(cmdResult));
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag, @Tag(RxBusString.LINK_SUCCESS), @Tag(RxBusString.LINKING),
            @Tag(RxBusString.LINKFAIL)
    })
    public void onReceiveAction(String action) {
        switch (action) {
            case RxBusString.LINK_SUCCESS:
                mTvStatus.setEnabled(false);
                mTvStatus.setVisibility(View.GONE);
                break;
            case RxBusString.LINKING:
                mTvStatus.setVisibility(View.VISIBLE);
                mTvStatus.setEnabled(false);
                mTvStatus.setText(R.string.label_linking);
                break;
            case RxBusString.LINKFAIL:
                mTvStatus.setEnabled(true);
                mTvStatus.setVisibility(View.VISIBLE);
                mTvStatus.setText(R.string.label_link_fail);
                break;
            default:
        }
    }

    @OnClick(R.id.tv_status)
    public void onStatusClick() {
        getApp().connectService();
    }
}
