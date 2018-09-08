package com.zj.mqtt.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.thread.EventThread;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.CmdString;
import com.zj.mqtt.protocol.CmdPackage;
import com.zj.mqtt.protocol.CmdTest;
import com.zj.mqtt.ui.BaseActivity;

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
        if (mDeviceBean == null) {
            showToast(R.string.toast_device_notfound);
            finish();
            return;
        }
        mHeaderView.setTitle(mDeviceBean.getName());
        mTvName.setText(mDeviceBean.getDeviceMac());

        mIvSwitch.setSelected(mDeviceBean.isControlOnOff());
    }

    @OnClick(R.id.layout_header_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.layout_header_right)
    public void onRightClick() {
        Intent intent = new Intent(this, DeviceSettingActivity.class);
        intent.putExtra(AppString.KEY_BEAN, mDeviceBean);
        startActivityForResult(intent, 11);
    }

    @OnClick(R.id.iv_switch)
    public void onClickSwitch() {
        CmdControlBean control = CmdPackage.setOnOff( !mDeviceBean.isControlOnOff(),
                mDeviceBean.getDeviceMac(),
                mDeviceBean.getDeviceEndpoint().getEndpoint());

        getApp().publishMsgToServer(control);

        // TODO: 2018/9/8 测试所有的、 解析所有的协议
        //CmdTest.testSendCmd(mDeviceBean.getDeviceMac(),
        //        mDeviceBean.getDeviceEndpoint().getEndpoint());
        //CmdTest.testParse();
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void onKidChange(String action) {
        if (action.equals(CmdString.DEV_ONOFF)) {
            mIvSwitch.setSelected(mDeviceBean.isControlOnOff());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data != null && data.hasExtra(AppString.KEY_DELETE)) {
            finish();
        } else {
            mHeaderView.setTitle(mDeviceBean.getName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterRxBus();
    }
}
