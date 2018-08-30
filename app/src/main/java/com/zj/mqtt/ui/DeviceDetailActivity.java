package com.zj.mqtt.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.toapp.CmdResult;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.CmdString;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.protocol.CmdPackage;

/**
 * 设备控制详情
 *
 * @author zhuj 2018/8/28 下午4:01
 */
public class DeviceDetailActivity extends BaseActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.tv_name) TextView mTvName;
    @BindView(R.id.iv_switch) ImageView mIvSwitch;
    private DeviceBean mDeviceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail);
        ButterKnife.bind(this);
        initViews();
        registerRxBus();
    }

    private void initViews() {
        String mac = getIntent().getStringExtra("mac");
        mDeviceBean = getApp().getDevice(mac);
        mHeaderView.setTitle(mDeviceBean.getName());
        mTvName.setText(mDeviceBean.getDeviceEndpoint().getMac());

        mIvSwitch.setSelected(mDeviceBean.isControlOnOff());
    }

    @OnClick(R.id.layout_header_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.iv_switch)
    public void onClickSwitch() {
        CmdControlBean control = CmdPackage.setOnOff( !mDeviceBean.isControlOnOff(),
                mDeviceBean.getDeviceEndpoint().getMac(),
                mDeviceBean.getDeviceEndpoint().getEndpoint());

        getApp().publishMsgToServer(control);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void onKidChange(String action) {
        if (action.equals(CmdString.DEV_ONOFF)) {
            mIvSwitch.setSelected(mDeviceBean.isControlOnOff());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterRxBus();
    }
}
