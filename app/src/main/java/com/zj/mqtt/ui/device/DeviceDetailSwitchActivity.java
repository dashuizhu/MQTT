package com.zj.mqtt.ui.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.ui.BaseActivity;
import com.zj.mqtt.utils.StatusBarUtil;

/**
 * 开关类设备
 * @author zhuj 2018/9/13 下午7:35.
 */
public class DeviceDetailSwitchActivity extends DeviceDetailActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.tv_switch) TextView mTvSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail_switch);
        ButterKnife.bind(this);

        StatusBarUtil.immersive(this);
        StatusBarUtil.setPadding(this, mHeaderView);
        initViews();
    }

    //@OnClick(R.id.iv_switch)
    //public void onClickSwitch() {
    //    CmdControlBean control = CmdPackage.setOnOff( !mDeviceBean.isControlOnOff(),
    //            mDeviceBean.getDeviceMac(),
    //            mDeviceBean.getDeviceEndpoint().getEndpoint());
    //
    //    getApp().publishMsgToServer(control);
    //
    //    // TODO: 2018/9/8 测试所有的、 解析所有的协议
    //    //CmdTest.testSendCmd(mDeviceBean.getDeviceMac(),
    //    //        mDeviceBean.getDeviceEndpoint().getEndpoint());
    //    //CmdTest.testParse();
    //}
    //
    //@Subscribe(thread = EventThread.MAIN_THREAD)
    //public void onKidChange(String action) {
    //    if (action.equals(CmdString.DEV_ONOFF)) {
    //
    //    }
    //}

    private void initViews() {
        boolean isOn = false;
        mTvSwitch.setText(isOn ? R.string.label_light_on : R.string.label_light_off);
    }
}